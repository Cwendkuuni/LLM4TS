import org.junit.Test;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {
    
    @Test
    public void testGetCode() {
        GeoStatusCode success = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals(200, success.getCode());
        
        GeoStatusCode badRequest = GeoStatusCode.G_GEO_BAD_REQUEST;
        assertEquals(400, badRequest.getCode());
        
        GeoStatusCode serverError = GeoStatusCode.G_GEO_SERVER_ERROR;
        assertEquals(500, serverError.getCode());
        
        GeoStatusCode missingQuery = GeoStatusCode.G_GEO_MISSING_QUERY;
        assertEquals(601, missingQuery.getCode());
        
        GeoStatusCode unknownAddress = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        assertEquals(602, unknownAddress.getCode());
        
        GeoStatusCode unavailableAddress = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        assertEquals(603, unavailableAddress.getCode());
        
        GeoStatusCode unknownDirections = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        assertEquals(604, unknownDirections.getCode());
        
        GeoStatusCode badKey = GeoStatusCode.G_GEO_BAD_KEY;
        assertEquals(610, badKey.getCode());
        
        GeoStatusCode tooManyQueries = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        assertEquals(620, tooManyQueries.getCode());
        
        GeoStatusCode unknownStatus = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        assertEquals(-1, unknownStatus.getCode());
    }
    
    @Test
    public void testGetCodeName() {
        GeoStatusCode success = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals("G_GEO_SUCCESS", success.getCodeName());
        
        GeoStatusCode badRequest = GeoStatusCode.G_GEO_BAD_REQUEST;
        assertEquals("G_GEO_BAD_REQUEST", badRequest.getCodeName());
        
        GeoStatusCode serverError = GeoStatusCode.G_GEO_SERVER_ERROR;
        assertEquals("G_GEO_SERVER_ERROR", serverError.getCodeName());
        
        GeoStatusCode missingQuery = GeoStatusCode.G_GEO_MISSING_QUERY;
        assertEquals("G_GEO_MISSING_QUERY", missingQuery.getCodeName());
        
        GeoStatusCode unknownAddress = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        assertEquals("G_GEO_UNKNOWN_ADDRESS", unknownAddress.getCodeName());
        
        GeoStatusCode unavailableAddress = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        assertEquals("G_GEO_UNAVAILABLE_ADDRESS", unavailableAddress.getCodeName());
        
        GeoStatusCode unknownDirections = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        assertEquals("G_GEO_UNKNOWN_DIRECTIONS", unknownDirections.getCodeName());
        
        GeoStatusCode badKey = GeoStatusCode.G_GEO_BAD_KEY;
        assertEquals("G_GEO_BAD_KEY", badKey.getCodeName());
        
        GeoStatusCode tooManyQueries = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        assertEquals("G_GEO_TOO_MANY_QUERIES", tooManyQueries.getCodeName());
        
        GeoStatusCode unknownStatus = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        assertEquals("G_GEO_UNKOWN_STATUS", unknownStatus.getCodeName());
    }
    
    @Test
    public void testGetDescription() {
        GeoStatusCode success = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", success.getDescription());
        
        GeoStatusCode badRequest = GeoStatusCode.G_GEO_BAD_REQUEST;
        assertEquals("A directions request could not be successfully parsed. (Since 2.81)", badRequest.getDescription());
        
        GeoStatusCode serverError = GeoStatusCode.G_GEO_SERVER_ERROR;
        assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known. (Since 2.55)", serverError.getDescription());
        
        GeoStatusCode missingQuery = GeoStatusCode.G_GEO_MISSING_QUERY;
        assertEquals("The HTTP q parameter was either missing or had no value. For geocoding requests, this means that an empty address was specified as input. For directions requests, this means that no query was specified in the input. (Since 2.81)", missingQuery.getDescription());
        
        GeoStatusCode unknownAddress = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        assertEquals("No corresponding geographic location could be found for the specified address. This may be due to the fact that the address is relatively new, or it may be incorrect. (Since 2.55)", unknownAddress.getDescription());
        
        GeoStatusCode unavailableAddress = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        assertEquals("The geocode for the given address or the route for the given directions query cannot be returned due to legal or contractual reasons. (Since 2.55)", unavailableAddress.getDescription());
        
        GeoStatusCode unknownDirections = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        assertEquals("The GDirections object could not compute directions between the points mentioned in the query. This is usually because there is no route available between the two points, or because we do not have data for routing in that region. (Since 2.81)", unknownDirections.getDescription());
        
        GeoStatusCode badKey = GeoStatusCode.G_GEO_BAD_KEY;
        assertEquals("The given key is either invalid or does not match the domain for which it was given. (Since 2.55)", badKey.getDescription());
        
        GeoStatusCode tooManyQueries = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        assertEquals("The given key has gone over the requests limit in the 24 hour period. ", tooManyQueries.getDescription());
        
        GeoStatusCode unknownStatus = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        assertEquals("Uknown Status", unknownStatus.getDescription());
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
        
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, GeoStatusCode.getStatusCode(-1));
    }
}