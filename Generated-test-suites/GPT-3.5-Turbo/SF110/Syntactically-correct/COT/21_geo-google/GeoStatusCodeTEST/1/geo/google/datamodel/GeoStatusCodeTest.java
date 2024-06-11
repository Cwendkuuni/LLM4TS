
import org.junit.Assert;
import org.junit.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        Assert.assertEquals(200, statusCode.getCode());
    }

    @Test
    public void testGetCodeName() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        Assert.assertEquals("G_GEO_SUCCESS", statusCode.getCodeName());
    }

    @Test
    public void testGetDescription() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        Assert.assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", statusCode.getDescription());
    }

    @Test
    public void testGetStatusCode_G_GEO_SUCCESS() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(200);
        Assert.assertEquals(GeoStatusCode.G_GEO_SUCCESS, statusCode);
    }

    @Test
    public void testGetStatusCode_G_GEO_BAD_REQUEST() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(400);
        Assert.assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, statusCode);
    }

    @Test
    public void testGetStatusCode_G_GEO_SERVER_ERROR() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(500);
        Assert.assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, statusCode);
    }

    @Test
    public void testGetStatusCode_G_GEO_MISSING_QUERY() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(601);
        Assert.assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, statusCode);
    }

    @Test
    public void testGetStatusCode_G_GEO_UNKNOWN_ADDRESS() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(602);
        Assert.assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, statusCode);
    }

    @Test
    public void testGetStatusCode_G_GEO_UNAVAILABLE_ADDRESS() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(603);
        Assert.assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, statusCode);
    }

    @Test
    public void testGetStatusCode_G_GEO_UNKNOWN_DIRECTIONS() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(604);
        Assert.assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, statusCode);
    }

    @Test
    public void testGetStatusCode_G_GEO_BAD_KEY() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(610);
        Assert.assertEquals(GeoStatusCode.G_GEO_BAD_KEY, statusCode);
    }

    @Test
    public void testGetStatusCode_G_GEO_TOO_MANY_QUERIES() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(620);
        Assert.assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, statusCode);
    }

    @Test
    public void testGetStatusCode_G_GEO_UNKOWN_STATUS() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(-1);
        Assert.assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, statusCode);
    }
}
