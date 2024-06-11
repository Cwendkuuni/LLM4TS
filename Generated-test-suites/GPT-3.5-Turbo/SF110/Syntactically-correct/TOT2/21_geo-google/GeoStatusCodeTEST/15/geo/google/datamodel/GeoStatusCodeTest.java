package geo.google.datamodel;
import geo.google.datamodel.GeoStatusCode;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals(200, status.getCode());
    }
    
    @Test
    public void testGetCodeName() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals("G_GEO_SUCCESS", status.getCodeName());
    }
    
    @Test
    public void testGetDescription() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", status.getDescription());
    }
    
    @Test
    public void testGetStatusCodeSuccess() {
        GeoStatusCode status = GeoStatusCode.getStatusCode(200);
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, status);
    }
    
    @Test
    public void testGetStatusCodeBadRequest() {
        GeoStatusCode status = GeoStatusCode.getStatusCode(400);
        assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, status);
    }
    
    @Test
    public void testGetStatusCodeUnknown() {
        GeoStatusCode status = GeoStatusCode.getStatusCode(777);
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, status);
    }
    
    // Add more test cases as needed for edge cases and error scenarios
    
}