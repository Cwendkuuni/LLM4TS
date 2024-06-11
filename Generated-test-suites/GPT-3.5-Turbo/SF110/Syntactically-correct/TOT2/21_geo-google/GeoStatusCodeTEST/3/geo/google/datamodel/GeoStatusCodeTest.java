package geo.google.datamodel;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        GeoStatusCode code = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals(200, code.getCode());
    }

    @Test
    public void testGetCodeName() {
        GeoStatusCode code = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals("G_GEO_SUCCESS", code.getCodeName());
    }

    @Test
    public void testGetDescription() {
        GeoStatusCode code = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", code.getDescription());
    }

    @Test
    public void testGetStatusCode() {
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));
        assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, GeoStatusCode.getStatusCode(400));
        assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, GeoStatusCode.getStatusCode(500));
        assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, GeoStatusCode.getStatusCode(601));
        assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, GeoStatusCode.getStatusCode(602));
        assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, GeoStatusCode.getStatusCode(603));
        assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, GeoStatusCode.getStatusCode(604));
        assertEquals(GeoStatusCode.G_GEO_BAD_KEY, GeoStatusCode.getStatusCode(610));
        assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, GeoStatusCode.getStatusCode(620));
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, GeoStatusCode.getStatusCode(-1));
    }
}