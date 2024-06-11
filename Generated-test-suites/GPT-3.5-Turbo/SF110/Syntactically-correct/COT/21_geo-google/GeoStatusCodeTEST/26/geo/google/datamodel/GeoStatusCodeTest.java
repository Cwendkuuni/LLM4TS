package geo.google.datamodel;

import static org.junit.Assert.*;
import org.junit.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals(200, statusCode.getCode());
    }

    @Test
    public void testGetCodeName() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals("G_GEO_SUCCESS", statusCode.getCodeName());
    }

    @Test
    public void testGetDescription() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", statusCode.getDescription());
    }

    @Test
    public void testGetStatusCode() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(200);
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, statusCode);
    }

    @Test
    public void testGetStatusCodeWithInvalidCode() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(999);
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, statusCode);
    }

    @Test
    public void testGetStatusCodeWithBadRequestCode() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(400);
        assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, statusCode);
    }

    @Test
    public void testGetStatusCodeWithServerErrorCode() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(500);
        assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, statusCode);
    }

    @Test
    public void testGetStatusCodeWithMissingQueryCode() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(601);
        assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, statusCode);
    }

    @Test
    public void testGetStatusCodeWithUnknownAddressCode() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(602);
        assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, statusCode);
    }

    @Test
    public void testGetStatusCodeWithUnavailableAddressCode() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(603);
        assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, statusCode);
    }

    @Test
    public void testGetStatusCodeWithUnknownDirectionsCode() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(604);
        assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, statusCode);
    }

    @Test
    public void testGetStatusCodeWithBadKeyCode() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(610);
        assertEquals(GeoStatusCode.G_GEO_BAD_KEY, statusCode);
    }

    @Test
    public void testGetStatusCodeWithTooManyQueriesCode() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(620);
        assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, statusCode);
    }

    @Test
    public void testGetStatusCodeWithNegativeCode() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(-1);
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, statusCode);
    }
}