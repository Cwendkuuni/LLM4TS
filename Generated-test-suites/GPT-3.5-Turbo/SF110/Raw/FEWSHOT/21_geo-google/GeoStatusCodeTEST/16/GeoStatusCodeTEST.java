package geo.google.datamodel;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode_G_GEO_SUCCESS() {
        assertEquals(200, GeoStatusCode.G_GEO_SUCCESS.getCode());
    }
    
    @Test
    public void testGetCodeName_G_GEO_SUCCESS() {
        assertEquals("G_GEO_SUCCESS", GeoStatusCode.G_GEO_SUCCESS.getCodeName());
    }
    
    @Test
    public void testGetDescription_G_GEO_SUCCESS() {
        assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)",
                GeoStatusCode.G_GEO_SUCCESS.getDescription());
    }
    
    @Test
    public void testGetStatusCode_200() {
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));
    }
    
    @Test
    public void testGetCode_G_GEO_BAD_REQUEST() {
        assertEquals(400, GeoStatusCode.G_GEO_BAD_REQUEST.getCode());
    }
    
    @Test
    public void testGetCodeName_G_GEO_BAD_REQUEST() {
        assertEquals("G_GEO_BAD_REQUEST", GeoStatusCode.G_GEO_BAD_REQUEST.getCodeName());
    }
    
    @Test
    public void testGetDescription_G_GEO_BAD_REQUEST() {
        assertEquals("A directions request could not be successfully parsed. (Since 2.81)",
                GeoStatusCode.G_GEO_BAD_REQUEST.getDescription());
    }
    
    @Test
    public void testGetStatusCode_400() {
        assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, GeoStatusCode.getStatusCode(400));
    }
    
    @Test
    public void testGetCode_G_GEO_SERVER_ERROR() {
        assertEquals(500, GeoStatusCode.G_GEO_SERVER_ERROR.getCode());
    }
    
    @Test
    public void testGetCodeName_G_GEO_SERVER_ERROR() {
        assertEquals("G_GEO_SERVER_ERROR", GeoStatusCode.G_GEO_SERVER_ERROR.getCodeName());
    }
    
    @Test
    public void testGetDescription_G_GEO_SERVER_ERROR() {
        assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known. (Since 2.55)",
                GeoStatusCode.G_GEO_SERVER_ERROR.getDescription());
    }
    
    @Test
    public void testGetStatusCode_500() {
        assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, GeoStatusCode.getStatusCode(500));
    }
    
    @Test
    public void testGetCode_G_GEO_MISSING_QUERY() {
        assertEquals(601, GeoStatusCode.G_GEO_MISSING_QUERY.getCode());
    }
    
    @Test
    public void testGetCodeName_G_GEO_MISSING_QUERY() {
        assertEquals("G_GEO_MISSING_QUERY", GeoStatusCode.G_GEO_MISSING_QUERY.getCodeName());
    }
    
    @Test
    public void testGetDescription_G_GEO_MISSING_QUERY() {
        assertEquals("The HTTP q parameter was either missing or had no value. For geocoding requests, this means that an empty address was specified as input. For directions requests, this means that no query was specified in the input. (Since 2.81)",
                GeoStatusCode.G_GEO_MISSING_QUERY.getDescription());
    }
    
    @Test
    public void testGetStatusCode_601() {
        assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, GeoStatusCode.getStatusCode(601));
    }
    
    @Test
    public void testGetCode_G_GEO_UNKNOWN_ADDRESS() {
        assertEquals(602, GeoStatusCode.G_GEO_UNKNOWN_ADDRESS.getCode());
    }
    
    @Test
    public void testGetCodeName_G_GEO_UNKNOWN_ADDRESS() {
        assertEquals("G_GEO_UNKNOWN_ADDRESS", GeoStatusCode.G_GEO_UNKNOWN_ADDRESS.getCodeName());
    }
    
    @Test
    public void testGetDescription_G_GEO_UNKNOWN_ADDRESS() {
        assertEquals("No corresponding geographic location could be found for the specified address. This may be due to the fact that the address is relatively new, or it may be incorrect. (Since 2.55)",
                GeoStatusCode.G_GEO_UNKNOWN_ADDRESS.getDescription());
    }
    
    @Test
    public void testGetStatusCode_602() {
        assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, GeoStatusCode.getStatusCode(602));
    }
    
    @Test
    public void testGetCode_G_GEO_UNAVAILABLE_ADDRESS() {
        assertEquals(603, GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS.getCode());
    }
    
    @Test
    public void testGetCodeName_G_GEO_UNAVAILABLE_ADDRESS() {
        assertEquals("G_GEO_UNAVAILABLE_ADDRESS", GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS.getCodeName());
    }
    
    @Test
    public void testGetDescription_G_GEO_UNAVAILABLE_ADDRESS() {
        assertEquals("The geocode for the given address or the route for the given directions query cannot be returned due to legal or contractual reasons. (Since 2.55)",
                GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS.getDescription());
    }
    
    @Test
    public void testGetStatusCode_603() {
        assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, GeoStatusCode.getStatusCode(603));
    }
    
    @Test
    public void testGetCode_G_GEO_UNKNOWN_DIRECTIONS() {
        assertEquals(604, GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS.getCode());
    }
    
    @Test
    public void testGetCodeName_G_GEO_UNKNOWN_DIRECTIONS() {
        assertEquals("G_GEO_UNKNOWN_DIRECTIONS", GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS.getCodeName());
    }
    
    @Test
    public void testGetDescription_G_GEO_UNKNOWN_DIRECTIONS() {
        assertEquals("The GDirections object could not compute directions between the points mentioned in the query. This is usually because there is no route available between the two points, or because we do not have data for routing in that region. (Since 2.81)",
                GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS.getDescription());
    }
    
    @Test
    public void testGetStatusCode_604() {
        assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, GeoStatusCode.getStatusCode(604));
    }
    
    @Test
    public void testGetCode_G_GEO_BAD_KEY() {
        assertEquals(610, GeoStatusCode.G_GEO_BAD_KEY.getCode());
    }
    
    @Test
    public void testGetCodeName_G_GEO_BAD_KEY() {
        assertEquals("G_GEO_BAD_KEY", GeoStatusCode.G_GEO_BAD_KEY.getCodeName());
    }
    
    @Test
    public void testGetDescription_G_GEO_BAD_KEY() {
        assertEquals("The given key is either invalid or does not match the domain for which it was given. (Since 2.55)",
                GeoStatusCode.G_GEO_BAD_KEY.getDescription());
    }
    
    @Test
    public void testGetStatusCode_610() {
        assertEquals(GeoStatusCode.G_GEO_BAD_KEY, GeoStatusCode.getStatusCode(610));
    }
    
    @Test
    public void testGetCode_G_GEO_TOO_MANY_QUERIES() {
        assertEquals(620, GeoStatusCode.G_GEO_TOO_MANY_QUERIES.getCode());
    }
    
    @Test
    public void testGetCodeName_G_GEO_TOO_MANY_QUERIES() {
        assertEquals("G_GEO_TOO_MANY_QUERIES", GeoStatusCode.G_GEO_TOO_MANY_QUERIES.getCodeName());
    }
    
    @Test
    public void testGetDescription_G_GEO_TOO_MANY_QUERIES() {
        assertEquals("The given key has gone over the requests limit in the 24 hour period. ",
                GeoStatusCode.G_GEO_TOO_MANY_QUERIES.getDescription());
    }
    
    @Test
    public void testGetStatusCode_620() {
        assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, GeoStatusCode.getStatusCode(620));
    }
    
    @Test
    public void testGetCode_G_GEO_UNKOWN_STATUS() {
        assertEquals(-1, GeoStatusCode.G_GEO_UNKOWN_STATUS.getCode());
    }
    
    @Test
    public void testGetCodeName_G_GEO_UNKOWN_STATUS() {
        assertEquals("G_GEO_UNKOWN_STATUS", GeoStatusCode.G_GEO_UNKOWN_STATUS.getCodeName());
    }
    
    @Test
    public void testGetDescription_G_GEO_UNKOWN_STATUS() {
        assertEquals("Uknown Status",
                GeoStatusCode.G_GEO_UNKOWN_STATUS.getDescription());
    }
    
    @Test
    public void testGetStatusCode_invalidCode() {
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, GeoStatusCode.getStatusCode(123));
    }
    
}