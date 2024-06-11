package geo.google.datamodel;

import org.junit.Assert;
import org.junit.Test;

public class GeoStatusCodeTest {
    
    @Test
    public void testGetCode_Success() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        Assert.assertEquals(200, statusCode.getCode());
    }
    
    @Test
    public void testGetCodeName_Success() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        Assert.assertEquals("G_GEO_SUCCESS", statusCode.getCodeName());
    }
    
    @Test
    public void testGetDescription_Success() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        Assert.assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", statusCode.getDescription());
    }
    
    @Test
    public void testGetStatusCode_Success() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(200);
        Assert.assertEquals(GeoStatusCode.G_GEO_SUCCESS, statusCode);
    }
    
    @Test
    public void testGetStatusCode_NotFound() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(999);
        Assert.assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, statusCode);
    }
    
    @Test
    public void testGetStatusCode_GeoServerError() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(500);
        Assert.assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, statusCode);
    }
    
    // More test methods for all the other enum values
    
}