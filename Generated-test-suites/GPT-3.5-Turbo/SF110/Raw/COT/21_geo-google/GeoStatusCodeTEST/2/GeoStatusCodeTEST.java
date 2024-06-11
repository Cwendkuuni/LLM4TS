
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        int code = status.getCode();
        assertEquals(200, code);
    }
    
    @Test
    public void testGetCodeName() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        String codeName = status.getCodeName();
        assertEquals("G_GEO_SUCCESS", codeName);
    }
    
    @Test
    public void testGetDescription() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        String description = status.getDescription();
        assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", description);
    }
    
    @Test
    public void testGetStatusCode() {
        int code = 200;
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(code);
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, statusCode);
    }
    
    @Test
    public void testGetStatusCodeUnknown() {
        int code = -1;
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(code);
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, statusCode);
    }
}
