import org.junit.Test;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode_Success() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        int expectedCode = 200;
        assertEquals(expectedCode, statusCode.getCode());
    }

    @Test
    public void testGetCodeName_Success() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        String expectedCodeName = "G_GEO_SUCCESS";
        assertEquals(expectedCodeName, statusCode.getCodeName());
    }

    @Test
    public void testGetDescription_Success() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        String expectedDescription = "No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)";
        assertEquals(expectedDescription, statusCode.getDescription());
    }

    @Test
    public void testGetStatusCode_Success() {
        int code = 200;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals(expectedStatusCode, GeoStatusCode.getStatusCode(code));
    }

    @Test
    public void testGetStatusCode_BadRequest() {
        int code = 400;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
        assertEquals(expectedStatusCode, GeoStatusCode.getStatusCode(code));
    }

    @Test
    public void testGetStatusCode_ServerError() {
        int code = 500;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
        assertEquals(expectedStatusCode, GeoStatusCode.getStatusCode(code));
    }

    @Test
    public void testGetStatusCode_MissingQuery() {
        int code = 601;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
        assertEquals(expectedStatusCode, GeoStatusCode.getStatusCode(code));
    }

    @Test
    public void testGetStatusCode_UnknownAddress() {
        int code = 602;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        assertEquals(expectedStatusCode, GeoStatusCode.getStatusCode(code));
    }

    @Test
    public void testGetStatusCode_UnavailableAddress() {
        int code = 603;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        assertEquals(expectedStatusCode, GeoStatusCode.getStatusCode(code));
    }

    @Test
    public void testGetStatusCode_UnknownDirections() {
        int code = 604;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        assertEquals(expectedStatusCode, GeoStatusCode.getStatusCode(code));
    }

    @Test
    public void testGetStatusCode_BadKey() {
        int code = 610;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_BAD_KEY;
        assertEquals(expectedStatusCode, GeoStatusCode.getStatusCode(code));
    }

    @Test
    public void testGetStatusCode_TooManyQueries() {
        int code = 620;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        assertEquals(expectedStatusCode, GeoStatusCode.getStatusCode(code));
    }

    @Test
    public void testGetStatusCode_UnknownStatus() {
        int code = -1;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        assertEquals(expectedStatusCode, GeoStatusCode.getStatusCode(code));
    }
}