package testapi;

import io.restassured.response.Response;
import logger.Log;
import org.openqa.selenium.devtools.v85.layertree.model.LayerId;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.CheckInOutTime;
import pojo.BookingInformation;
import pojo.AuthenticationDetails;
import restutils.RestUtils;

import java.util.ArrayList;
import java.util.List;

public class TestApi {
    public static String bookingId;


    @Test
    public void authorizationToken(){
        AuthenticationDetails payload=new AuthenticationDetails();
        payload.setUsername("admin");
        payload.setPassword("password123");

        Response response=RestUtils.postData(payload,"auth/");

        String authToken= response.asPrettyString();
        Log.info("AuthToken = "+authToken);
    }
    @Test
    public void getDataOfInAndOut(){
        Log.info("***************************Data*********************************");
        Response response= RestUtils.getData("booking?checkin=2018-07-22&checkout=2021-03-21");
        int id = response.path("[0].bookingid");
        Log.info("id = "+id);
        RestUtils.getData("booking/"+id);
    }
    @Test(priority = 1)
    public void verifyTheSite(){
        Log.info("**************************Verify The Site********************");
        Assert.assertEquals(200, RestUtils.getData("booking/").getStatusCode(),"Verified");
    }
    @Test(priority = 2)
    public void getBookingIds(){
        Log.info("***********************Get Booking Id**************************");

        Response response=RestUtils.getData("booking/");
        bookingId=response.path("bookingid[0]").toString();
        Log.info("booking Id = "+bookingId);
    }
    @Test(priority = 3)
    public void getBookingData(){
        Log.info("************************Get Booking pojo.Data**********************");

        RestUtils.getData("booking/");
    }
    @Test(priority = 4)
    public void createBooking(){
        Log.info("**********************Create Booking*************************");

        BookingInformation payload=new BookingInformation();
        payload.setFirstname("Diksha");
        payload.setLastname("Popli");
        payload.setTotalprice(111);
        payload.setDepositpaid(true);
        CheckInOutTime data=new CheckInOutTime();
        data.setCheckin("2018-01-01");
        data.setCheckout("2018-01-01");
        payload.setBookingdates(data);
        payload.setAdditionalneeds("lunch");
        RestUtils.postData(payload,"booking/");
    }
    @Test(priority = 5)
    public void updateBooking(){
        Log.info("**********************Update Booking*************************");
        BookingInformation payload=new BookingInformation();
        payload.setFirstname("changed");
        payload.setLastname("changed");
        payload.setTotalprice(111);
        payload.setDepositpaid(true);

        CheckInOutTime data=new CheckInOutTime();
        data.setCheckin("2018-01-01");
        data.setCheckout("2018-01-01");

        payload.setBookingdates(data);
        payload.setAdditionalneeds("lunch");

       RestUtils.putData(bookingId,payload,"booking/");
    }
    @Test(priority = 6)
    public void partialUpdate(){
        Log.info("**********************Partial Update Booking*************************");

        BookingInformation payload=new BookingInformation();
        payload.setAdditionalneeds("brunch");
        RestUtils.patchData(bookingId,payload,"booking/");
    }
    @Test(priority = 7)
    public void delete(){
        Log.info("*****************************Delete********************");

        RestUtils.deleteData(bookingId,"booking/");
    }



}
