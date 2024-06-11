package geo.google.datamodel;

import org.junit.Test;
import org.junit.Assert;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode_G_GEO_SUCCESS() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        int expectedCode = 200;
        int actualCode = statusCode.getCode();
        Assert.assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testGetCodeName_G_GEO_SUCCESS() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        String expectedCodeName = "G_GEO_SUCCESS";
        String actualCodeName = statusCode.getCodeName();
        Assert.assertEquals(expectedCodeName, actualCodeName);
    }

    @Test
    public void testGetDescription_G_GEO_SUCCESS() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        String expectedDescription = "No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)";
        String actualDescription = statusCode.getDescription();
        Assert.assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testGetStatusCode_200() {
        int code = 200;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test
    public void testGetStatusCode_400() {
        int code = 400;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test
    public void testGetStatusCode_500() {
        int code = 500;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test
    public void testGetStatusCode_601() {
        int code = 601;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test
    public void testGetStatusCode_602() {
        int code = 602;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test
    public void testGetStatusCode_603() {
        int code = 603;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test
    public void testGetStatusCode_604() {
        int code = 604;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test
    public void testGetStatusCode_610() {
        int code = 610;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_BAD_KEY;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test
    public void testGetStatusCode_620() {
        int code = 620;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }
    
    @Test
    public void testGetStatusCode_Default() {
        int code = -1;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }

}