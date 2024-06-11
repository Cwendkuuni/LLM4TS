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
    public void testGetStatusCode_success() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(200);
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, statusCode);
    }

    @Test
    public void testGetStatusCode_unknown() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(999);
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, statusCode);
    }

    // Edge case: Testing for code 620 which should return G_GEO_TOO_MANY_QUERIES
    @Test
    public void testGetStatusCode_tooManyQueries() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(620);
        assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, statusCode);
    }

}