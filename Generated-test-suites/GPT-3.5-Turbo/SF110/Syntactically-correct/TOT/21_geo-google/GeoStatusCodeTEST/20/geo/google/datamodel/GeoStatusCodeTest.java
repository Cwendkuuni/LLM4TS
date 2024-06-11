import org.junit.Assert;
import org.junit.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode_G_GEO_SUCCESS() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        int expectedCode = 200;
        int actualCode = statusCode.getCode();
        Assert.assertEquals(expectedCode, actualCode);
    }
    
    @Test
    public void testGetCode_G_GEO_BAD_REQUEST() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
        int expectedCode = 400;
        int actualCode = statusCode.getCode();
        Assert.assertEquals(expectedCode, actualCode);
    }
    
    @Test
    public void testGetCode_G_GEO_SERVER_ERROR() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
        int expectedCode = 500;
        int actualCode = statusCode.getCode();
        Assert.assertEquals(expectedCode, actualCode);
    }
    
    @Test
    public void testGetCode_G_GEO_MISSING_QUERY() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
        int expectedCode = 601;
        int actualCode = statusCode.getCode();
        Assert.assertEquals(expectedCode, actualCode);
    }
    
    @Test
    public void testGetCode_G_GEO_UNKNOWN_ADDRESS() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        int expectedCode = 602;
        int actualCode = statusCode.getCode();
        Assert.assertEquals(expectedCode, actualCode);
    }
    
    @Test
    public void testGetCode_G_GEO_UNAVAILABLE_ADDRESS() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        int expectedCode = 603;
        int actualCode = statusCode.getCode();
        Assert.assertEquals(expectedCode, actualCode);
    }
    
    @Test
    public void testGetCode_G_GEO_UNKNOWN_DIRECTIONS() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        int expectedCode = 604;
        int actualCode = statusCode.getCode();
        Assert.assertEquals(expectedCode, actualCode);
    }
    
    @Test
    public void testGetCode_G_GEO_BAD_KEY() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_BAD_KEY;
        int expectedCode = 610;
        int actualCode = statusCode.getCode();
        Assert.assertEquals(expectedCode, actualCode);
    }
    
    @Test
    public void testGetCode_G_GEO_TOO_MANY_QUERIES() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        int expectedCode = 620;
        int actualCode = statusCode.getCode();
        Assert.assertEquals(expectedCode, actualCode);
    }
    
    @Test
    public void testGetCode_G_GEO_UNKOWN_STATUS() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        int expectedCode = -1;
        int actualCode = statusCode.getCode();
        Assert.assertEquals(expectedCode, actualCode);
    }
    
    @Test
    public void testGetCodeName() {
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
    public void testGetDescription_G_GEO_BAD_REQUEST() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
        String expectedDescription = "A directions request could not be successfully parsed. (Since 2.81)";
        String actualDescription = statusCode.getDescription();
        Assert.assertEquals(expectedDescription, actualDescription);
    }
    
    @Test
    public void testGetDescription_G_GEO_SERVER_ERROR() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
        String expectedDescription = "A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known. (Since 2.55)";
        String actualDescription = statusCode.getDescription();
        Assert.assertEquals(expectedDescription, actualDescription);
    }
    
    @Test
    public void testGetDescription_G_GEO_MISSING_QUERY() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
        String expectedDescription = "The HTTP q parameter was either missing or had no value. For geocoding requests, this means that an empty address was specified as input. For directions requests, this means that no query was specified in the input. (Since 2.81)";
        String actualDescription = statusCode.getDescription();
        Assert.assertEquals(expectedDescription, actualDescription);
    }
    
    @Test
    public void testGetDescription_G_GEO_UNKNOWN_ADDRESS() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        String expectedDescription = "No corresponding geographic location could be found for the specified address. This may be due to the fact that the address is relatively new, or it may be incorrect. (Since 2.55)";
        String actualDescription = statusCode.getDescription();
        Assert.assertEquals(expectedDescription, actualDescription);
    }
    
    @Test
    public void testGetDescription_G_GEO_UNAVAILABLE_ADDRESS() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        String expectedDescription = "The geocode for the given address or the route for the given directions query cannot be returned due to legal or contractual reasons. (Since 2.55)";
        String actualDescription = statusCode.getDescription();
        Assert.assertEquals(expectedDescription, actualDescription);
    }
    
    @Test
    public void testGetDescription_G_GEO_UNKNOWN_DIRECTIONS() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        String expectedDescription = "The GDirections object could not compute directions between the points mentioned in the query. This is usually because there is no route available between the two points, or because we do not have data for routing in that region. (Since 2.81)";
        String actualDescription = statusCode.getDescription();
        Assert.assertEquals(expectedDescription, actualDescription);
    }
    
    @Test
    public void testGetDescription_G_GEO_BAD_KEY() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_BAD_KEY;
        String expectedDescription = "The given key is either invalid or does not match the domain for which it was given. (Since 2.55)";
        String actualDescription = statusCode.getDescription();
        Assert.assertEquals(expectedDescription, actualDescription);
    }
    
    @Test
    public void testGetDescription_G_GEO_TOO_MANY_QUERIES() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        String expectedDescription = "The given key has gone over the requests limit in the 24 hour period.";
        String actualDescription = statusCode.getDescription();
        Assert.assertEquals(expectedDescription, actualDescription);
    }
    
    @Test
    public void testGetDescription_G_GEO_UNKOWN_STATUS() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        String expectedDescription = "Uknown Status";
        String actualDescription = statusCode.getDescription();
        Assert.assertEquals(expectedDescription, actualDescription);
    }
    
    @Test
    public void testGetStatusCode_G_GEO_SUCCESS() {
        int code = 200;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }
    
    @Test
    public void testGetStatusCode_G_GEO_BAD_REQUEST() {
        int code = 400;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }
    
    @Test
    public void testGetStatusCode_G_GEO_SERVER_ERROR() {
        int code = 500;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }
    
    @Test
    public void testGetStatusCode_G_GEO_MISSING_QUERY() {
        int code = 601;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }
    
    @Test
    public void testGetStatusCode_G_GEO_UNKNOWN_ADDRESS() {
        int code = 602;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }
    
    @Test
    public void testGetStatusCode_G_GEO_UNAVAILABLE_ADDRESS() {
        int code = 603;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }
    
    @Test
    public void testGetStatusCode_G_GEO_UNKNOWN_DIRECTIONS() {
        int code = 604;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }
    
    @Test
    public void testGetStatusCode_G_GEO_BAD_KEY() {
        int code = 610;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_BAD_KEY;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }
    
    @Test
    public void testGetStatusCode_G_GEO_TOO_MANY_QUERIES() {
        int code = 620;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }
    
    @Test
    public void testGetStatusCode_G_GEO_UNKOWN_STATUS() {
        int code = 999;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }
}