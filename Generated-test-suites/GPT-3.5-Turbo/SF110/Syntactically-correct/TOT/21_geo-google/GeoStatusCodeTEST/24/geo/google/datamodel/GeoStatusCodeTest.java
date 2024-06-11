package geo.google.datamodel;

import org.junit.Assert;
import org.junit.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        GeoStatusCode geoStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        Assert.assertEquals(200, geoStatusCode.getCode());
    }

    @Test
    public void testGetCodeName() {
        GeoStatusCode geoStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        Assert.assertEquals("G_GEO_SUCCESS", geoStatusCode.getCodeName());
    }

    @Test
    public void testGetDescription() {
        GeoStatusCode geoStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        Assert.assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", geoStatusCode.getDescription());
    }

    @Test
    public void testGetStatusCode() {
        Assert.assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));
    }

    @Test
    public void testGetStatusCodeWithInvalidCode() {
        Assert.assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, GeoStatusCode.getStatusCode(-1));
    }

    @Test
    public void testGetStatusCodeWithBadKey() {
        Assert.assertEquals(GeoStatusCode.G_GEO_BAD_KEY, GeoStatusCode.getStatusCode(610));
    }
  
  // Additional Test Cases
  
    @Test
    public void testGetStatusCodeWithTooManyQueries() {
        Assert.assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, GeoStatusCode.getStatusCode(620));
    }
  
    @Test
    public void testGetStatusCodeWithUnknownDirections() {
        Assert.assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, GeoStatusCode.getStatusCode(604));
    }
  
    @Test
    public void testGetStatusCodeWithUnavailableAddress() {
        Assert.assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, GeoStatusCode.getStatusCode(603));
    }
  
    @Test
    public void testGetStatusCodeWithUnknownAddress() {
        Assert.assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, GeoStatusCode.getStatusCode(602));
    }
  
    @Test
    public void testGetStatusCodeWithMissingQuery() {
        Assert.assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, GeoStatusCode.getStatusCode(601));
    }
  
    @Test
    public void testGetStatusCodeWithServerError() {
        Assert.assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, GeoStatusCode.getStatusCode(500));
    }
  
    @Test
    public void testGetStatusCodeWithBadRequest() {
        Assert.assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, GeoStatusCode.getStatusCode(400));
    }

}