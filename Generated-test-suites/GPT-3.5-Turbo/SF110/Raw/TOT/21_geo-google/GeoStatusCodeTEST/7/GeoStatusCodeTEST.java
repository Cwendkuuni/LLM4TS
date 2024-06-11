import geo.google.datamodel.GeoStatusCode;
import org.junit.Assert;
import org.junit.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode_success() {
        Assert.assertEquals(200, GeoStatusCode.G_GEO_SUCCESS.getCode());
    }
    
    @Test
    public void testGetCode_badRequest() {
        Assert.assertEquals(400, GeoStatusCode.G_GEO_BAD_REQUEST.getCode());
    }
    
    @Test
    public void testGetCode_serverError() {
        Assert.assertEquals(500, GeoStatusCode.G_GEO_SERVER_ERROR.getCode());
    }
    
    @Test
    public void testGetCode_missingQuery() {
        Assert.assertEquals(601, GeoStatusCode.G_GEO_MISSING_QUERY.getCode());
    }
    
    @Test
    public void testGetCode_unknownAddress() {
        Assert.assertEquals(602, GeoStatusCode.G_GEO_UNKNOWN_ADDRESS.getCode());
    }
    
    @Test
    public void testGetCode_unavailableAddress() {
        Assert.assertEquals(603, GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS.getCode());
    }
    
    @Test
    public void testGetCode_unknownDirections() {
        Assert.assertEquals(604, GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS.getCode());
    }
    
    @Test
    public void testGetCode_badKey() {
        Assert.assertEquals(610, GeoStatusCode.G_GEO_BAD_KEY.getCode());
    }
    
    @Test
    public void testGetCode_tooManyQueries() {
        Assert.assertEquals(620, GeoStatusCode.G_GEO_TOO_MANY_QUERIES.getCode());
    }
    
    @Test
    public void testGetCode_unknownStatus() {
        Assert.assertEquals(-1, GeoStatusCode.G_GEO_UNKOWN_STATUS.getCode());
    }
    
    @Test
    public void testGetCodeName_success() {
        Assert.assertEquals("G_GEO_SUCCESS", GeoStatusCode.G_GEO_SUCCESS.getCodeName());
    }
    
    @Test
    public void testGetCodeName_badRequest() {
        Assert.assertEquals("G_GEO_BAD_REQUEST", GeoStatusCode.G_GEO_BAD_REQUEST.getCodeName());
    }
    
    @Test
    public void testGetCodeName_serverError() {
        Assert.assertEquals("G_GEO_SERVER_ERROR", GeoStatusCode.G_GEO_SERVER_ERROR.getCodeName());
    }
    
    @Test
    public void testGetCodeName_missingQuery() {
        Assert.assertEquals("G_GEO_MISSING_QUERY", GeoStatusCode.G_GEO_MISSING_QUERY.getCodeName());
    }
    
    @Test
    public void testGetCodeName_unknownAddress() {
        Assert.assertEquals("G_GEO_UNKNOWN_ADDRESS", GeoStatusCode.G_GEO_UNKNOWN_ADDRESS.getCodeName());
    }
    
    @Test
    public void testGetCodeName_unavailableAddress() {
        Assert.assertEquals("G_GEO_UNAVAILABLE_ADDRESS", GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS.getCodeName());
    }
    
    @Test
    public void testGetCodeName_unknownDirections() {
        Assert.assertEquals("G_GEO_UNKNOWN_DIRECTIONS", GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS.getCodeName());
    }
    
    @Test
    public void testGetCodeName_badKey() {
        Assert.assertEquals("G_GEO_BAD_KEY", GeoStatusCode.G_GEO_BAD_KEY.getCodeName());
    }
    
    @Test
    public void testGetCodeName_tooManyQueries() {
        Assert.assertEquals("G_GEO_TOO_MANY_QUERIES", GeoStatusCode.G_GEO_TOO_MANY_QUERIES.getCodeName());
    }
    
    @Test
    public void testGetCodeName_unknownStatus() {
        Assert.assertEquals("G_GEO_UNKOWN_STATUS", GeoStatusCode.G_GEO_UNKOWN_STATUS.getCodeName());
    }
    
    @Test
    public void testGetDescription_success() {
        Assert.assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", GeoStatusCode.G_GEO_SUCCESS.getDescription());
    }
    
    @Test
    public void testGetDescription_badRequest() {
        Assert.assertEquals("A directions request could not be successfully parsed. (Since 2.81)", GeoStatusCode.G_GEO_BAD_REQUEST.getDescription());
    }
    
    @Test
    public void testGetDescription_serverError() {
        Assert.assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known. (Since 2.55)", GeoStatusCode.G_GEO_SERVER_ERROR.getDescription());
    }
    
    @Test
    public void testGetDescription_missingQuery() {
        Assert.assertEquals("The HTTP q parameter was either missing or had no value. For geocoding requests, this means that an empty address was specified as input. For directions requests, this means that no query was specified in the input. (Since 2.81)", GeoStatusCode.G_GEO_MISSING_QUERY.getDescription());
    }
    
    @Test
    public void testGetDescription_unknownAddress() {
        Assert.assertEquals("No corresponding geographic location could be found for the specified address. This may be due to the fact that the address is relatively new, or it may be incorrect. (Since 2.55)", GeoStatusCode.G_GEO_UNKNOWN_ADDRESS.getDescription());
    }
    
    @Test
    public void testGetDescription_unavailableAddress() {
        Assert.assertEquals("The geocode for the given address or the route for the given directions query cannot be returned due to legal or contractual reasons. (Since 2.55)", GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS.getDescription());
    }
    
    @Test
    public void testGetDescription_unknownDirections() {
        Assert.assertEquals("The GDirections object could not compute directions between the points mentioned in the query. This is usually because there is no route available between the two points, or because we do not have data for routing in that region. (Since 2.81)", GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS.getDescription());
    }
    
    @Test
    public void testGetDescription_badKey() {
        Assert.assertEquals("The given key is either invalid or does not match the domain for which it was given. (Since 2.55)", GeoStatusCode.G_GEO_BAD_KEY.getDescription());
    }
    
    @Test
    public void testGetDescription_tooManyQueries() {
        Assert.assertEquals("The given key has gone over the requests limit in the 24 hour period. ", GeoStatusCode.G_GEO_TOO_MANY_QUERIES.getDescription());
    }
    
    @Test
    public void testGetDescription_unknownStatus() {
        Assert.assertEquals("Uknown Status", GeoStatusCode.G_GEO_UNKOWN_STATUS.getDescription());
    }
    
    @Test
    public void testGetStatusCode_success() {
        Assert.assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));
    }
    
    @Test
    public void testGetStatusCode_badRequest() {
        Assert.assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, GeoStatusCode.getStatusCode(400));
    }
    
    @Test
    public void testGetStatusCode_serverError() {
        Assert.assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, GeoStatusCode.getStatusCode(500));
    }
    
    @Test
    public void testGetStatusCode_missingQuery() {
        Assert.assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, GeoStatusCode.getStatusCode(601));
    }
    
    @Test
    public void testGetStatusCode_unknownAddress() {
        Assert.assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, GeoStatusCode.getStatusCode(602));
    }
    
    @Test
    public void testGetStatusCode_unavailableAddress() {
        Assert.assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, GeoStatusCode.getStatusCode(603));
    }
    
    @Test
    public void testGetStatusCode_unknownDirections() {
        Assert.assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, GeoStatusCode.getStatusCode(604));
    }
    
    @Test
    public void testGetStatusCode_badKey() {
        Assert.assertEquals(GeoStatusCode.G_GEO_BAD_KEY, GeoStatusCode.getStatusCode(610));
    }
    
    @Test
    public void testGetStatusCode_tooManyQueries() {
        Assert.assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, GeoStatusCode.getStatusCode(620));
    }
    
    @Test
    public void testGetStatusCode_unknownStatus() {
        Assert.assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, GeoStatusCode.getStatusCode(999));
    }
}