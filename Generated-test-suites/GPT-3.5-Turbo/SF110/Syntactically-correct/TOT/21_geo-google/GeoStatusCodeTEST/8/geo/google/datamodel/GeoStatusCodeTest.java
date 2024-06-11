import org.junit.Assert;
import org.junit.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        GeoStatusCode geoStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        int expectedCode = 200;
        int actualCode = geoStatusCode.getCode();
        Assert.assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testGetCodeName() {
        GeoStatusCode geoStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        String expectedCodeName = "G_GEO_SUCCESS";
        String actualCodeName = geoStatusCode.getCodeName();
        Assert.assertEquals(expectedCodeName, actualCodeName);
    }

    @Test
    public void testGetDescription() {
        GeoStatusCode geoStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        String expectedDescription = "No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)";
        String actualDescription = geoStatusCode.getDescription();
        Assert.assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testGetStatusCode_Success() {
        int code = 200;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test
    public void testGetStatusCode_BadRequest() {
        int code = 400;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test
    public void testGetStatusCode_ServerError() {
        int code = 500;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test
    public void testGetStatusCode_MissingQuery() {
        int code = 601;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test
    public void testGetStatusCode_UnknownAddress() {
        int code = 602;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test
    public void testGetStatusCode_UnavailableAddress() {
        int code = 603;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test
    public void testGetStatusCode_UnknownDirections() {
        int code = 604;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test
    public void testGetStatusCode_BadKey() {
        int code = 610;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_BAD_KEY;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test
    public void testGetStatusCode_TooManyQueries() {
        int code = 620;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test
    public void testGetStatusCode_UnknownStatus() {
        int code = -1;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }
}