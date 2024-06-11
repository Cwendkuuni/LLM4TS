import org.junit.Test;

import static org.junit.Assert.*;

public class GeoStatusCodeTest {
    
    @Test
    public void testGetCode() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        int expectedCode = 200;
        
        assertEquals(expectedCode, status.getCode());
    }
    
    @Test
    public void testGetCodeName() {
        GeoStatusCode status = GeoStatusCode.G_GEO_MISSING_QUERY;
        String expectedCodeName = "G_GEO_MISSING_QUERY";
        
        assertEquals(expectedCodeName, status.getCodeName());
    }
    
    @Test
    public void testGetDescription() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SERVER_ERROR;
        String expectedDescription = "A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known. (Since 2.55)";
        
        assertEquals(expectedDescription, status.getDescription());
    }
    
    @Test
    public void testGetStatusCode_ValidCode() {
        int code = 200;
        GeoStatusCode expectedStatus = GeoStatusCode.G_GEO_SUCCESS;
        
        assertEquals(expectedStatus, GeoStatusCode.getStatusCode(code));
    }
    
    @Test
    public void testGetStatusCode_InvalidCode() {
        int code = 999;
        GeoStatusCode expectedStatus = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        
        assertEquals(expectedStatus, GeoStatusCode.getStatusCode(code));
    }
    
    @Test
    public void testGetStatusCode_LowerBound() {
        int code = 0;
        GeoStatusCode expectedStatus = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        
        assertEquals(expectedStatus, GeoStatusCode.getStatusCode(code));
    }
    
    @Test
    public void testGetStatusCode_UpperBound() {
        int code = 1000;
        GeoStatusCode expectedStatus = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        
        assertEquals(expectedStatus, GeoStatusCode.getStatusCode(code));
    }
}