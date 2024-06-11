import org.junit.Assert;
import org.junit.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        int code = statusCode.getCode();
        Assert.assertEquals(200, code);

        statusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
        code = statusCode.getCode();
        Assert.assertEquals(400, code);

        statusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
        code = statusCode.getCode();
        Assert.assertEquals(500, code);

        statusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
        code = statusCode.getCode();
        Assert.assertEquals(601, code);

        statusCode = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        code = statusCode.getCode();
        Assert.assertEquals(602, code);

        statusCode = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        code = statusCode.getCode();
        Assert.assertEquals(603, code);

        statusCode = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        code = statusCode.getCode();
        Assert.assertEquals(604, code);

        statusCode = GeoStatusCode.G_GEO_BAD_KEY;
        code = statusCode.getCode();
        Assert.assertEquals(610, code);

        statusCode = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        code = statusCode.getCode();
        Assert.assertEquals(620, code);

        statusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        code = statusCode.getCode();
        Assert.assertEquals(-1, code);
    }

    @Test
    public void testGetCodeName() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        String codeName = statusCode.getCodeName();
        Assert.assertEquals("G_GEO_SUCCESS", codeName);

        statusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
        codeName = statusCode.getCodeName();
        Assert.assertEquals("G_GEO_BAD_REQUEST", codeName);

        statusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
        codeName = statusCode.getCodeName();
        Assert.assertEquals("G_GEO_SERVER_ERROR", codeName);

        statusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
        codeName = statusCode.getCodeName();
        Assert.assertEquals("G_GEO_MISSING_QUERY", codeName);

        statusCode = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        codeName = statusCode.getCodeName();
        Assert.assertEquals("G_GEO_UNKNOWN_ADDRESS", codeName);

        statusCode = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        codeName = statusCode.getCodeName();
        Assert.assertEquals("G_GEO_UNAVAILABLE_ADDRESS", codeName);

        statusCode = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        codeName = statusCode.getCodeName();
        Assert.assertEquals("G_GEO_UNKNOWN_DIRECTIONS", codeName);

        statusCode = GeoStatusCode.G_GEO_BAD_KEY;
        codeName = statusCode.getCodeName();
        Assert.assertEquals("G_GEO_BAD_KEY", codeName);

        statusCode = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        codeName = statusCode.getCodeName();
        Assert.assertEquals("G_GEO_TOO_MANY_QUERIES", codeName);

        statusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        codeName = statusCode.getCodeName();
        Assert.assertEquals("G_GEO_UNKOWN_STATUS", codeName);
    }

    @Test
    public void testGetDescription() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        String description = statusCode.getDescription();
        Assert.assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", description);

        statusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
        description = statusCode.getDescription();
        Assert.assertEquals("A directions request could not be successfully parsed. (Since 2.81)", description);

        statusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
        description = statusCode.getDescription();
        Assert.assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known. (Since 2.55)", description);

        statusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
        description = statusCode.getDescription();
        Assert.assertEquals("The HTTP q parameter was either missing or had no value. For geocoding requests, this means that an empty address was specified as input. For directions requests, this means that no query was specified in the input. (Since 2.81)", description);

        statusCode = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        description = statusCode.getDescription();
        Assert.assertEquals("No corresponding geographic location could be found for the specified address. This may be due to the fact that the address is relatively new, or it may be incorrect. (Since 2.55)", description);

        statusCode = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        description = statusCode.getDescription();
        Assert.assertEquals("The geocode for the given address or the route for the given directions query cannot be returned due to legal or contractual reasons. (Since 2.55)", description);

        statusCode = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        description = statusCode.getDescription();
        Assert.assertEquals("The GDirections object could not compute directions between the points mentioned in the query. This is usually because there is no route available between the two points, or because we do not have data for routing in that region. (Since 2.81)", description);

        statusCode = GeoStatusCode.G_GEO_BAD_KEY;
        description = statusCode.getDescription();
        Assert.assertEquals("The given key is either invalid or does not match the domain for which it was given. (Since 2.55)", description);

        statusCode = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        description = statusCode.getDescription();
        Assert.assertEquals("The given key has gone over the requests limit in the 24 hour period. ", description);

        statusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        description = statusCode.getDescription();
        Assert.assertEquals("Uknown Status", description);
    }

    @Test
    public void testGetStatusCode() {
        int code = 200;
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(GeoStatusCode.G_GEO_SUCCESS, statusCode);

        code = 400;
        statusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, statusCode);

        code = 500;
        statusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, statusCode);

        code = 601;
        statusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, statusCode);

        code = 602;
        statusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, statusCode);

        code = 603;
        statusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, statusCode);

        code = 604;
        statusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, statusCode);

        code = 610;
        statusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(GeoStatusCode.G_GEO_BAD_KEY, statusCode);

        code = 620;
        statusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, statusCode);

        code = 999;
        statusCode = GeoStatusCode.getStatusCode(code);
        Assert.assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, statusCode);
    }
}