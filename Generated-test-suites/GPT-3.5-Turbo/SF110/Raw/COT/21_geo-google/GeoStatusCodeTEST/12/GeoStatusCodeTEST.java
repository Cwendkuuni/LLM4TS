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
    public void testGetStatusCodeSuccess() {
        int code = 200;
        GeoStatusCode status = GeoStatusCode.getStatusCode(code);
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, status);
    }
  
    @Test
    public void testGetStatusCodeBadRequest() {
        int code = 400;
        GeoStatusCode status = GeoStatusCode.getStatusCode(code);
        assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, status);
    }
  
    @Test
    public void testGetStatusCodeServerError() {
        int code = 500;
        GeoStatusCode status = GeoStatusCode.getStatusCode(code);
        assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, status);
    }
  
    @Test
    public void testGetStatusCodeMissingQuery() {
        int code = 601;
        GeoStatusCode status = GeoStatusCode.getStatusCode(code);
        assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, status);
    }
  
    @Test
    public void testGetStatusCodeUnknownAddress() {
        int code = 602;
        GeoStatusCode status = GeoStatusCode.getStatusCode(code);
        assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, status);
    }
  
    @Test
    public void testGetStatusCodeUnavailableAddress() {
        int code = 603;
        GeoStatusCode status = GeoStatusCode.getStatusCode(code);
        assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, status);
    }
  
    @Test
    public void testGetStatusCodeUnknownDirections() {
        int code = 604;
        GeoStatusCode status = GeoStatusCode.getStatusCode(code);
        assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, status);
    }
  
    @Test
    public void testGetStatusCodeBadKey() {
        int code = 610;
        GeoStatusCode status = GeoStatusCode.getStatusCode(code);
        assertEquals(GeoStatusCode.G_GEO_BAD_KEY, status);
    }
  
    @Test
    public void testGetStatusCodeTooManyQueries() {
        int code = 620;
        GeoStatusCode status = GeoStatusCode.getStatusCode(code);
        assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, status);
    }
  
    @Test
    public void testGetStatusCodeUnknownStatus() {
        int code = -1;
        GeoStatusCode status = GeoStatusCode.getStatusCode(code);
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, status);
    }
  
    // Additional test cases for edge cases and exceptions can go here
  
}
