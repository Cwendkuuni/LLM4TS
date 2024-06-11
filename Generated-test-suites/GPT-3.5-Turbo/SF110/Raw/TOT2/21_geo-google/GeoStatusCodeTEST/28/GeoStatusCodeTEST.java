import org.junit.Test;
import static org.junit.Assert.*;

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
        int code = 400;
        assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, GeoStatusCode.getStatusCode(code));
        
        code = 603;
        assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, GeoStatusCode.getStatusCode(code));
        
        code = 999;
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, GeoStatusCode.getStatusCode(code));
    }
}