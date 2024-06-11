import org.junit.Assert;
import org.junit.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        GeoStatusCode code = GeoStatusCode.G_GEO_SUCCESS;
        Assert.assertEquals(200, code.getCode());
    }

    @Test
    public void testGetCodeName() {
        GeoStatusCode code = GeoStatusCode.G_GEO_SUCCESS;
        Assert.assertEquals("G_GEO_SUCCESS", code.getCodeName());
    }

    @Test
    public void testGetDescription() {
        GeoStatusCode code = GeoStatusCode.G_GEO_SUCCESS;
        Assert.assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", code.getDescription());
    }

    @Test
    public void testGetStatusCodeSuccess() {
        GeoStatusCode code = GeoStatusCode.getStatusCode(200);
        Assert.assertEquals(GeoStatusCode.G_GEO_SUCCESS, code);
    }

    @Test
    public void testGetStatusCodeBadRequest() {
        GeoStatusCode code = GeoStatusCode.getStatusCode(400);
        Assert.assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, code);
    }

    @Test
    public void testGetStatusCodeServerError() {
        GeoStatusCode code = GeoStatusCode.getStatusCode(500);
        Assert.assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, code);
    }

    @Test
    public void testGetStatusCodeMissingQuery() {
        GeoStatusCode code = GeoStatusCode.getStatusCode(601);
        Assert.assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, code);
    }
    
    @Test
    public void testGetStatusCodeUnknownAddress() {
        GeoStatusCode code = GeoStatusCode.getStatusCode(602);
        Assert.assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, code);
    }

    @Test
    public void testGetStatusCodeUnavailableAddress() {
        GeoStatusCode code = GeoStatusCode.getStatusCode(603);
        Assert.assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, code);
    }

    @Test
    public void testGetStatusCodeUnknownDirections() {
        GeoStatusCode code = GeoStatusCode.getStatusCode(604);
        Assert.assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, code);
    }

    @Test
    public void testGetStatusCodeBadKey() {
        GeoStatusCode code = GeoStatusCode.getStatusCode(610);
        Assert.assertEquals(GeoStatusCode.G_GEO_BAD_KEY, code);
    }

    @Test
    public void testGetStatusCodeTooManyQueries() {
        GeoStatusCode code = GeoStatusCode.getStatusCode(620);
        Assert.assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, code);
    }

    @Test
    public void testGetStatusCodeUnknownStatus() {
        GeoStatusCode code = GeoStatusCode.getStatusCode(999);
        Assert.assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, code);
    }
}