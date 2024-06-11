import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        GeoStatusCode code = GeoStatusCode.G_GEO_SUCCESS;
        int expected = 200;
        int actual = code.getCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCodeName() {
        GeoStatusCode code = GeoStatusCode.G_GEO_SUCCESS;
        String expected = "G_GEO_SUCCESS";
        String actual = code.getCodeName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDescription() {
        GeoStatusCode code = GeoStatusCode.G_GEO_SUCCESS;
        String expected = "No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)";
        String actual = code.getDescription();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetStatusCodeForValidCode() {
        int code = 200;
        GeoStatusCode expected = GeoStatusCode.G_GEO_SUCCESS;
        GeoStatusCode actual = GeoStatusCode.getStatusCode(code);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetStatusCodeForInvalidCode() {
        int code = 999;
        GeoStatusCode expected = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        GeoStatusCode actual = GeoStatusCode.getStatusCode(code);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetStatusCodeForBadRequest() {
        int code = 400;
        GeoStatusCode expected = GeoStatusCode.G_GEO_BAD_REQUEST;
        GeoStatusCode actual = GeoStatusCode.getStatusCode(code);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetStatusCodeForServerError() {
        int code = 500;
        GeoStatusCode expected = GeoStatusCode.G_GEO_SERVER_ERROR;
        GeoStatusCode actual = GeoStatusCode.getStatusCode(code);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetStatusCodeForMissingQuery() {
        int code = 601;
        GeoStatusCode expected = GeoStatusCode.G_GEO_MISSING_QUERY;
        GeoStatusCode actual = GeoStatusCode.getStatusCode(code);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetStatusCodeForUnknownAddress() {
        int code = 602;
        GeoStatusCode expected = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        GeoStatusCode actual = GeoStatusCode.getStatusCode(code);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetStatusCodeForUnavailableAddress() {
        int code = 603;
        GeoStatusCode expected = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        GeoStatusCode actual = GeoStatusCode.getStatusCode(code);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetStatusCodeForUnknownDirections() {
        int code = 604;
        GeoStatusCode expected = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        GeoStatusCode actual = GeoStatusCode.getStatusCode(code);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetStatusCodeForBadKey() {
        int code = 610;
        GeoStatusCode expected = GeoStatusCode.G_GEO_BAD_KEY;
        GeoStatusCode actual = GeoStatusCode.getStatusCode(code);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetStatusCodeForTooManyQueries() {
        int code = 620;
        GeoStatusCode expected = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        GeoStatusCode actual = GeoStatusCode.getStatusCode(code);
        assertEquals(expected, actual);
    }

}