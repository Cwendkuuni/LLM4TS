
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        assertEquals(200, GeoStatusCode.G_GEO_SUCCESS.getCode());
    }

    @Test
    public void testGetCodeName() {
        assertEquals("G_GEO_BAD_REQUEST", GeoStatusCode.G_GEO_BAD_REQUEST.getCodeName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", GeoStatusCode.G_GEO_SUCCESS.getDescription());
    }

    @Test
    public void testGetStatusCode_G_GEO_SUCCESS() {
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));
    }

    @Test
    public void testGetStatusCode_G_GEO_BAD_REQUEST() {
        assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, GeoStatusCode.getStatusCode(400));
    }

    @Test
    public void testGetStatusCode_G_GEO_SERVER_ERROR() {
        assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, GeoStatusCode.getStatusCode(500));
    }

    @Test
    public void testGetStatusCode_G_GEO_MISSING_QUERY() {
        assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, GeoStatusCode.getStatusCode(601));
    }

    @Test
    public void testGetStatusCode_G_GEO_UNKNOWN_ADDRESS() {
        assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, GeoStatusCode.getStatusCode(602));
    }

    @Test
    public void testGetStatusCode_G_GEO_UNAVAILABLE_ADDRESS() {
        assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, GeoStatusCode.getStatusCode(603));
    }

    @Test
    public void testGetStatusCode_G_GEO_UNKNOWN_DIRECTIONS() {
        assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, GeoStatusCode.getStatusCode(604));
    }

    @Test
    public void testGetStatusCode_G_GEO_BAD_KEY() {
        assertEquals(GeoStatusCode.G_GEO_BAD_KEY, GeoStatusCode.getStatusCode(610));
    }

    @Test
    public void testGetStatusCode_G_GEO_TOO_MANY_QUERIES() {
        assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, GeoStatusCode.getStatusCode(620));
    }

    @Test
    public void testGetStatusCode_G_GEO_UNKOWN_STATUS() {
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, GeoStatusCode.getStatusCode(-1));
    }
}
