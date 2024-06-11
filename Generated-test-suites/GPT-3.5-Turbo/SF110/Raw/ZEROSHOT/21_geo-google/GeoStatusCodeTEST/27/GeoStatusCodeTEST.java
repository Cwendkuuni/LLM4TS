package geo.google.datamodel;

import org.junit.Assert;
import org.junit.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        Assert.assertEquals(200, GeoStatusCode.G_GEO_SUCCESS.getCode());
        Assert.assertEquals(400, GeoStatusCode.G_GEO_BAD_REQUEST.getCode());
        Assert.assertEquals(500, GeoStatusCode.G_GEO_SERVER_ERROR.getCode());
        Assert.assertEquals(601, GeoStatusCode.G_GEO_MISSING_QUERY.getCode());
        Assert.assertEquals(602, GeoStatusCode.G_GEO_UNKNOWN_ADDRESS.getCode());
        Assert.assertEquals(603, GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS.getCode());
        Assert.assertEquals(604, GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS.getCode());
        Assert.assertEquals(610, GeoStatusCode.G_GEO_BAD_KEY.getCode());
        Assert.assertEquals(620, GeoStatusCode.G_GEO_TOO_MANY_QUERIES.getCode());
        Assert.assertEquals(-1, GeoStatusCode.G_GEO_UNKOWN_STATUS.getCode());
    }

    @Test
    public void testGetCodeName() {
        Assert.assertEquals("G_GEO_SUCCESS", GeoStatusCode.G_GEO_SUCCESS.getCodeName());
        Assert.assertEquals("G_GEO_BAD_REQUEST", GeoStatusCode.G_GEO_BAD_REQUEST.getCodeName());
        Assert.assertEquals("G_GEO_SERVER_ERROR", GeoStatusCode.G_GEO_SERVER_ERROR.getCodeName());
        Assert.assertEquals("G_GEO_MISSING_QUERY", GeoStatusCode.G_GEO_MISSING_QUERY.getCodeName());
        Assert.assertEquals("G_GEO_UNKNOWN_ADDRESS", GeoStatusCode.G_GEO_UNKNOWN_ADDRESS.getCodeName());
        Assert.assertEquals("G_GEO_UNAVAILABLE_ADDRESS", GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS.getCodeName());
        Assert.assertEquals("G_GEO_UNKNOWN_DIRECTIONS", GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS.getCodeName());
        Assert.assertEquals("G_GEO_BAD_KEY", GeoStatusCode.G_GEO_BAD_KEY.getCodeName());
        Assert.assertEquals("G_GEO_TOO_MANY_QUERIES", GeoStatusCode.G_GEO_TOO_MANY_QUERIES.getCodeName());
        Assert.assertEquals("G_GEO_UNKOWN_STATUS", GeoStatusCode.G_GEO_UNKOWN_STATUS.getCodeName());
    }

    @Test
    public void testGetDescription() {
        Assert.assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", GeoStatusCode.G_GEO_SUCCESS.getDescription());
        Assert.assertEquals("A directions request could not be successfully parsed. (Since 2.81)", GeoStatusCode.G_GEO_BAD_REQUEST.getDescription());
        Assert.assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known. (Since 2.55)", GeoStatusCode.G_GEO_SERVER_ERROR.getDescription());
        Assert.assertEquals("The HTTP q parameter was either missing or had no value. For geocoding requests, this means that an empty address was specified as input. For directions requests, this means that no query was specified in the input. (Since 2.81)", GeoStatusCode.G_GEO_MISSING_QUERY.getDescription());
        Assert.assertEquals("No corresponding geographic location could be found for the specified address. This may be due to the fact that the address is relatively new, or it may be incorrect. (Since 2.55)", GeoStatusCode.G_GEO_UNKNOWN_ADDRESS.getDescription());
        Assert.assertEquals("The geocode for the given address or the route for the given directions query cannot be returned due to legal or contractual reasons. (Since 2.55)", GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS.getDescription());
        Assert.assertEquals("The GDirections object could not compute directions between the points mentioned in the query. This is usually because there is no route available between the two points, or because we do not have data for routing in that region. (Since 2.81)", GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS.getDescription());
        Assert.assertEquals("The given key is either invalid or does not match the domain for which it was given. (Since 2.55)", GeoStatusCode.G_GEO_BAD_KEY.getDescription());
        Assert.assertEquals("The given key has gone over the requests limit in the 24 hour period. ", GeoStatusCode.G_GEO_TOO_MANY_QUERIES.getDescription());
        Assert.assertEquals("Uknown Status", GeoStatusCode.G_GEO_UNKOWN_STATUS.getDescription());
    }

    @Test
    public void testGetStatusCode() {
        Assert.assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));
        Assert.assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, GeoStatusCode.getStatusCode(400));
        Assert.assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, GeoStatusCode.getStatusCode(500));
        Assert.assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, GeoStatusCode.getStatusCode(601));
        Assert.assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, GeoStatusCode.getStatusCode(602));
        Assert.assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, GeoStatusCode.getStatusCode(603));
        Assert.assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, GeoStatusCode.getStatusCode(604));
        Assert.assertEquals(GeoStatusCode.G_GEO_BAD_KEY, GeoStatusCode.getStatusCode(610));
        Assert.assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, GeoStatusCode.getStatusCode(620));
        Assert.assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, GeoStatusCode.getStatusCode(123));
    }
}