package geo.google.datamodel;
import static org.junit.Assert.*;
import org.junit.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        GeoStatusCode code = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals(200, code.getCode());
    }
    
    @Test
    public void testGetCodeName() {
        GeoStatusCode code = GeoStatusCode.G_GEO_BAD_REQUEST;
        assertEquals("G_GEO_BAD_REQUEST", code.getCodeName());
    }
    
    @Test
    public void testGetDescription() {
        GeoStatusCode code = GeoStatusCode.G_GEO_SERVER_ERROR;
        assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known. (Since 2.55)", code.getDescription());
    }
    
    @Test
    public void testGetStatusCode_ValidCode() {
        GeoStatusCode code = GeoStatusCode.getStatusCode(601);
        assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, code);
    }
    
    @Test
    public void testGetStatusCode_InvalidCode() {
        GeoStatusCode code = GeoStatusCode.getStatusCode(999);
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, code);
    }
}