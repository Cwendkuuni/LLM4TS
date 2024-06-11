import org.junit.Test;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals(200, status.getCode());
        
        status = GeoStatusCode.G_GEO_MISSING_QUERY;
        assertEquals(601, status.getCode());
        
        status = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        assertEquals(620, status.getCode());
    }
    
    @Test
    public void testGetCodeName() {
        GeoStatusCode status = GeoStatusCode.G_GEO_BAD_REQUEST;
        assertEquals("G_GEO_BAD_REQUEST", status.getCodeName());
        
        status = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        assertEquals("G_GEO_UNKNOWN_ADDRESS", status.getCodeName());
        
        status = GeoStatusCode.G_GEO_BAD_KEY;
        assertEquals("G_GEO_BAD_KEY", status.getCodeName());
    }
    
    @Test
    public void testGetDescription() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SERVER_ERROR;
        assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known. (Since 2.55)", status.getDescription());
        
        status = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        assertEquals("The geocode for the given address or the route for the given directions query cannot be returned due to legal or contractual reasons. (Since 2.55)", status.getDescription());
        
        status = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        assertEquals("The GDirections object could not compute directions between the points mentioned in the query. This is usually because there is no route available between the two points, or because we do not have data for routing in that region. (Since 2.81)", status.getDescription());
    }
    
    @Test
    public void testGetStatusCode() {
        int code = 200;
        GeoStatusCode status = GeoStatusCode.getStatusCode(code);
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, status);
        
        code = 601;
        status = GeoStatusCode.getStatusCode(code);
        assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, status);
        
        code = 620;
        status = GeoStatusCode.getStatusCode(code);
        assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, status);
        
        code = 999;
        status = GeoStatusCode.getStatusCode(code);
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, status);
    }
}