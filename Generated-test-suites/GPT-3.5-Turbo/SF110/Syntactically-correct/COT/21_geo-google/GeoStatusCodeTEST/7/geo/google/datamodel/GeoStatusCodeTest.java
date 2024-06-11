import org.junit.Test;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals(200, statusCode.getCode());
        
        statusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
        assertEquals(400, statusCode.getCode());
        
        statusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
        assertEquals(500, statusCode.getCode());
        
        statusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
        assertEquals(601, statusCode.getCode());
        
        statusCode = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        assertEquals(602, statusCode.getCode());
        
        statusCode = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        assertEquals(603, statusCode.getCode());
        
        statusCode = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        assertEquals(604, statusCode.getCode());
        
        statusCode = GeoStatusCode.G_GEO_BAD_KEY;
        assertEquals(610, statusCode.getCode());
        
        statusCode = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        assertEquals(620, statusCode.getCode());
        
        statusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        assertEquals(-1, statusCode.getCode());
    }
    
    @Test
    public void testGetCodeName() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals("G_GEO_SUCCESS", statusCode.getCodeName());
        
        statusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
        assertEquals("G_GEO_BAD_REQUEST", statusCode.getCodeName());
        
        statusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
        assertEquals("G_GEO_SERVER_ERROR", statusCode.getCodeName());
        
        statusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
        assertEquals("G_GEO_MISSING_QUERY", statusCode.getCodeName());
        
        statusCode = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        assertEquals("G_GEO_UNKNOWN_ADDRESS", statusCode.getCodeName());
        
        statusCode = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        assertEquals("G_GEO_UNAVAILABLE_ADDRESS", statusCode.getCodeName());
        
        statusCode = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        assertEquals("G_GEO_UNKNOWN_DIRECTIONS", statusCode.getCodeName());
        
        statusCode = GeoStatusCode.G_GEO_BAD_KEY;
        assertEquals("G_GEO_BAD_KEY", statusCode.getCodeName());
        
        statusCode = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        assertEquals("G_GEO_TOO_MANY_QUERIES", statusCode.getCodeName());
        
        statusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        assertEquals("G_GEO_UNKOWN_STATUS", statusCode.getCodeName());
    }
    
    @Test
    public void testGetDescription() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", statusCode.getDescription());
        
        statusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
        assertEquals("A directions request could not be successfully parsed. (Since 2.81)", statusCode.getDescription());
        
        statusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
        assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known. (Since 2.55)", statusCode.getDescription());
        
        statusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
        assertEquals("The HTTP q parameter was either missing or had no value. For geocoding requests, this means that an empty address was specified as input. For directions requests, this means that no query was specified in the input. (Since 2.81)", statusCode.getDescription());
        
        statusCode = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        assertEquals("No corresponding geographic location could be found for the specified address. This may be due to the fact that the address is relatively new, or it may be incorrect. (Since 2.55)", statusCode.getDescription());
        
        statusCode = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        assertEquals("The geocode for the given address or the route for the given directions query cannot be returned due to legal or contractual reasons. (Since 2.55)", statusCode.getDescription());
        
        statusCode = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        assertEquals("The GDirections object could not compute directions between the points mentioned in the query. This is usually because there is no route available between the two points, or because we do not have data for routing in that region. (Since 2.81)", statusCode.getDescription());
        
        statusCode = GeoStatusCode.G_GEO_BAD_KEY;
        assertEquals("The given key is either invalid or does not match the domain for which it was given. (Since 2.55)", statusCode.getDescription());
        
        statusCode = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        assertEquals("The given key has gone over the requests limit in the 24 hour period. ", statusCode.getDescription());
        
        statusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        assertEquals("Uknown Status", statusCode.getDescription());
    }
    
    @Test
    public void testGetStatusCode() {
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));
        assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, GeoStatusCode.getStatusCode(400));
        assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, GeoStatusCode.getStatusCode(500));
        assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, GeoStatusCode.getStatusCode(601));
        assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, GeoStatusCode.getStatusCode(602));
        assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, GeoStatusCode.getStatusCode(603));
        assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, GeoStatusCode.getStatusCode(604));
        assertEquals(GeoStatusCode.G_GEO_BAD_KEY, GeoStatusCode.getStatusCode(610));
        assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, GeoStatusCode.getStatusCode(620));
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, GeoStatusCode.getStatusCode(999));
    }
}