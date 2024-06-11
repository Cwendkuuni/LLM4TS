import org.junit.Test;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode_Success() {
        GeoStatusCode code = GeoStatusCode.G_GEO_SUCCESS;
        int expectedCode = 200;
        
        assertEquals(expectedCode, code.getCode());
    }
    
    @Test
    public void testGetCodeName_Success() {
        GeoStatusCode code = GeoStatusCode.G_GEO_BAD_REQUEST;
        String expectedCodeName = "G_GEO_BAD_REQUEST";
        
        assertEquals(expectedCodeName, code.getCodeName());
    }
    
    @Test
    public void testGetDescription_Success() {
        GeoStatusCode code = GeoStatusCode.G_GEO_SERVER_ERROR;
        String expectedDescription = "A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known. (Since 2.55)";
        
        assertEquals(expectedDescription, code.getDescription());
    }
    
    @Test
    public void testGetStatusCode_Success() {
        int code = 601;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
        
        assertEquals(expectedStatusCode, GeoStatusCode.getStatusCode(code));
    }
    
    @Test
    public void testGetStatusCode_UnknownCode() {
        int code = 999;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        
        assertEquals(expectedStatusCode, GeoStatusCode.getStatusCode(code));
    }
    
    @Test
    public void testGetStatusCode_SuccessEdgeCase() {
        int code = 200;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        
        assertEquals(expectedStatusCode, GeoStatusCode.getStatusCode(code));
    }
    
    @Test
    public void testGetStatusCode_SuccessEdgeCase2() {
        int code = 620;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        
        assertEquals(expectedStatusCode, GeoStatusCode.getStatusCode(code));
    }
    
    @Test
    public void testGetStatusCode_SuccessEdgeCase3() {
        int code = 610;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_BAD_KEY;
        
        assertEquals(expectedStatusCode, GeoStatusCode.getStatusCode(code));
    }
    
    @Test
    public void testGetStatusCode_Error() {
        int code = -1;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        
        assertEquals(expectedStatusCode, GeoStatusCode.getStatusCode(code));
    }
}