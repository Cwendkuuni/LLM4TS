import static org.junit.Assert.*;
import org.junit.Test;

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
        assertEquals("A directions request could not be successfully parsed. (Since 2.81)", GeoStatusCode.G_GEO_BAD_REQUEST.getDescription());
    }
    
    @Test
    public void testGetStatusCodeSuccess() {
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));
    }
    
    @Test
    public void testGetStatusCodeUnknown() {
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, GeoStatusCode.getStatusCode(999));
    }
    
    @Test
    public void testGetStatusCodeMissingQuery() {
        assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, GeoStatusCode.getStatusCode(601));
    }
}