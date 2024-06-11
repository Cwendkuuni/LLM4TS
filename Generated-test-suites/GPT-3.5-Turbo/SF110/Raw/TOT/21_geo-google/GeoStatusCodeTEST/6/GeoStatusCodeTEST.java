import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GeoStatusCodeTest {
    
    private GeoStatusCode geoStatusCode;
    
    @Before
    public void setup() {
        geoStatusCode = GeoStatusCode.G_GEO_SUCCESS;
    }
    
    @Test
    public void testGetCode() {
        assertEquals(200, geoStatusCode.getCode());
    }
    
    @Test
    public void testGetCodeName() {
        assertEquals("G_GEO_SUCCESS", geoStatusCode.getCodeName());
    }
    
    @Test
    public void testGetDescription() {
        assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", geoStatusCode.getDescription());
    }
    
    @Test
    public void testGetStatusCodeSuccess() {
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));
    }
    
    @Test
    public void testGetStatusCodeBadRequest() {
        assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, GeoStatusCode.getStatusCode(400));
    }
    
    @Test
    public void testGetStatusCodeServerError() {
        assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, GeoStatusCode.getStatusCode(500));
    }
    
    @Test
    public void testGetStatusCodeMissingQuery() {
        assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, GeoStatusCode.getStatusCode(601));
    }
    
    @Test
    public void testGetStatusCodeUnknownAddress() {
        assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, GeoStatusCode.getStatusCode(602));
    }
    
    @Test
    public void testGetStatusCodeUnavailableAddress() {
        assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, GeoStatusCode.getStatusCode(603));
    }
    
    @Test
    public void testGetStatusCodeUnknownDirections() {
        assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, GeoStatusCode.getStatusCode(604));
    }
    
    @Test
    public void testGetStatusCodeBadKey() {
        assertEquals(GeoStatusCode.G_GEO_BAD_KEY, GeoStatusCode.getStatusCode(610));
    }
    
    @Test
    public void testGetStatusCodeTooManyQueries() {
        assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, GeoStatusCode.getStatusCode(620));
    }
    
    @Test
    public void testGetStatusCodeUnknownStatus() {
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, GeoStatusCode.getStatusCode(-1));
    }
}