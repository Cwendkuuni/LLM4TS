import org.junit.*;

public class GeoQueryTest {
    
    @Test
    public void testGetLocation() {
        // Test getLocation method with GeoLocation parameter
        GeoLocation location = new GeoLocation(40.7128, -74.0060);
        GeoQuery geoQuery = new GeoQuery(location);
        
        Assert.assertEquals(location, geoQuery.getLocation());
    }
    
    @Test
    public void testGetIp() {
        // Test getIp method with IP address parameter
        GeoQuery geoQuery = new GeoQuery("127.0.0.1");
        
        Assert.assertEquals("127.0.0.1", geoQuery.getIp());
    }
    
    @Test
    public void testSetAccuracy() {
        // Test setAccuracy method with different accuracy values
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(40.7128, -74.0060));
        geoQuery.setAccuracy("high");
        
        Assert.assertEquals("high", geoQuery.getAccuracy());
    }
    
    @Test
    public void testSetGranularity() {
        // Test setGranularity method with different granularity values
        GeoQuery geoQuery = new GeoQuery("127.0.0.1");
        geoQuery.setGranularity("city");
        
        Assert.assertEquals("city", geoQuery.getGranularity());
    }
    
    @Test
    public void testSetMaxResults() {
        // Test setMaxResults method with different maxResults values
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(40.7128, -74.0060));
        geoQuery.setMaxResults(10);
        
        Assert.assertEquals(10, geoQuery.getMaxResults());
    }
}