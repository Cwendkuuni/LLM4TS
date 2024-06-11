
import static org.junit.Assert.*;
import org.junit.*;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        GeoStatusCode success = GeoStatusCode.G_GEO_SUCCESS;
        GeoStatusCode badRequest = GeoStatusCode.G_GEO_BAD_REQUEST;
        GeoStatusCode serverError = GeoStatusCode.G_GEO_SERVER_ERROR;
        
        assertEquals(200, success.getCode());
        assertEquals(400, badRequest.getCode());
        assertEquals(500, serverError.getCode());
    }
    
    @Test
    public void testGetCodeName() {
        GeoStatusCode success = GeoStatusCode.G_GEO_SUCCESS;
        GeoStatusCode badRequest = GeoStatusCode.G_GEO_BAD_REQUEST;
        GeoStatusCode serverError = GeoStatusCode.G_GEO_SERVER_ERROR;
        
        assertEquals("G_GEO_SUCCESS", success.getCodeName());
        assertEquals("G_GEO_BAD_REQUEST", badRequest.getCodeName());
        assertEquals("G_GEO_SERVER_ERROR", serverError.getCodeName());
    }
    
    @Test
    public void testGetDescription() {
        GeoStatusCode success = GeoStatusCode.G_GEO_SUCCESS;
        GeoStatusCode badRequest = GeoStatusCode.G_GEO_BAD_REQUEST;
        GeoStatusCode serverError = GeoStatusCode.G_GEO_SERVER_ERROR;
        
        assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)",
                success.getDescription());
        assertEquals("A directions request could not be successfully parsed. (Since 2.81)", 
                badRequest.getDescription());
        assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known. (Since 2.55)",
                serverError.getDescription());
    }
    
    @Test
    public void testGetStatusCode() {
        int successCode = 200;
        int badRequestCode = 400;
        int serverErrorCode = 500;
        int unknownStatusCode = 999;
        
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(successCode));
        assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, GeoStatusCode.getStatusCode(badRequestCode));
        assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, GeoStatusCode.getStatusCode(serverErrorCode));
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, GeoStatusCode.getStatusCode(unknownStatusCode));
    }
}

