package geo.google.datamodel;

import org.junit.*;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        GeoStatusCode code = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals(200, code.getCode());
        
        code = GeoStatusCode.G_GEO_BAD_REQUEST;
        assertEquals(400, code.getCode());
        
        code = GeoStatusCode.G_GEO_SERVER_ERROR;
        assertEquals(500, code.getCode());
        
        code = GeoStatusCode.G_GEO_MISSING_QUERY;
        assertEquals(601, code.getCode());
        
        code = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        assertEquals(602, code.getCode());
        
        code = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        assertEquals(603, code.getCode());
        
        code = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        assertEquals(604, code.getCode());
        
        code = GeoStatusCode.G_GEO_BAD_KEY;
        assertEquals(610, code.getCode());
        
        code = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        assertEquals(620, code.getCode());
        
        code = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        assertEquals(-1, code.getCode());
    }
    
    @Test
    public void testGetCodeName() {
        GeoStatusCode code = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals("G_GEO_SUCCESS", code.getCodeName());
        
        code = GeoStatusCode.G_GEO_BAD_REQUEST;
        assertEquals("G_GEO_BAD_REQUEST", code.getCodeName());
        
        code = GeoStatusCode.G_GEO_SERVER_ERROR;
        assertEquals("G_GEO_SERVER_ERROR", code.getCodeName());
        
        code = GeoStatusCode.G_GEO_MISSING_QUERY;
        assertEquals("G_GEO_MISSING_QUERY", code.getCodeName());
        
        code = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        assertEquals("G_GEO_UNKNOWN_ADDRESS", code.getCodeName());
        
        code = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        assertEquals("G_GEO_UNAVAILABLE_ADDRESS", code.getCodeName());
        
        code = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        assertEquals("G_GEO_UNKNOWN_DIRECTIONS", code.getCodeName());
        
        code = GeoStatusCode.G_GEO_BAD_KEY;
        assertEquals("G_GEO_BAD_KEY", code.getCodeName());
        
        code = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        assertEquals("G_GEO_TOO_MANY_QUERIES", code.getCodeName());
        
        code = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        assertEquals("G_GEO_UNKOWN_STATUS", code.getCodeName());
    }
    
    @Test
    public void testGetDescription() {
        GeoStatusCode code = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", code.getDescription());
        
        code = GeoStatusCode.G_GEO_BAD_REQUEST;
        assertEquals("A directions request could not be successfully parsed. (Since 2.81)", code.getDescription());
        
        code = GeoStatusCode.G_GEO_SERVER_ERROR;
        assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known. (Since 2.55)", code.getDescription());
        
        code = GeoStatusCode.G_GEO_MISSING_QUERY;
        assertEquals("The HTTP q parameter was either missing or had no value. For geocoding requests, this means that an empty address was specified as input. For directions requests, this means that no query was specified in the input. (Since 2.81)", code.getDescription());
        
        code = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        assertEquals("No corresponding geographic location could be found for the specified address. This may be due to the fact that the address is relatively new, or it may be incorrect. (Since 2.55)", code.getDescription());
        
        code = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        assertEquals("The geocode for the given address or the route for the given directions query cannot be returned due to legal or contractual reasons. (Since 2.55)", code.getDescription());
        
        code = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        assertEquals("The GDirections object could not compute directions between the points mentioned in the query. This is usually because there is no route available between the two points, or because we do not have data for routing in that region. (Since 2.81)", code.getDescription());
        
        code = GeoStatusCode.G_GEO_BAD_KEY;
        assertEquals("The given key is either invalid or does not match the domain for which it was given. (Since 2.55)", code.getDescription());
        
        code = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        assertEquals("The given key has gone over the requests limit in the 24 hour period. ", code.getDescription());
        
        code = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        assertEquals("Uknown Status", code.getDescription());
    }
    
    @Test
    public void testGetStatusCode() {
        GeoStatusCode code = GeoStatusCode.getStatusCode(200);
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, code);
        
        code = GeoStatusCode.getStatusCode(400);
        assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, code);
        
        code = GeoStatusCode.getStatusCode(500);
        assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, code);
        
        code = GeoStatusCode.getStatusCode(601);
        assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, code);
        
        code = GeoStatusCode.getStatusCode(602);
        assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, code);
        
        code = GeoStatusCode.getStatusCode(603);
        assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, code);
        
        code = GeoStatusCode.getStatusCode(604);
        assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, code);
        
        code = GeoStatusCode.getStatusCode(610);
        assertEquals(GeoStatusCode.G_GEO_BAD_KEY, code);
        
        code = GeoStatusCode.getStatusCode(620);
        assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, code);
        
        code = GeoStatusCode.getStatusCode(123);
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, code);
    }
}