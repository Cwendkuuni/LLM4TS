import static org.junit.Assert.*;
import org.junit.Test;

public class GeoQueryTest {
    
    GeoQuery geoQuery;
    
    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        geoQuery = new GeoQuery(location);
        assertEquals(geoQuery.getLocation(), location);
    }
    
    @Test
    public void testGetIp() {
        String ip = "192.168.1.1";
        geoQuery = new GeoQuery(ip);
        assertEquals(geoQuery.getIp(), ip);
    }
    
    @Test
    public void testSetAccuracy() {
        String accuracy = "100m";
        geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.setAccuracy(accuracy);
        assertEquals(geoQuery.getAccuracy(), accuracy);
    }
    
    @Test
    public void testGranularity() {
        String granularity = "neighborhood";
        geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.granularity(granularity);
        assertEquals(geoQuery.getGranularity(), granularity);
    }
    
    @Test
    public void testMaxResults() {
        int maxResults = 10;
        geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.maxResults(maxResults);
        assertEquals(geoQuery.getMaxResults(), maxResults);
    }
    
    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertTrue(geoQuery1.equals(geoQuery2));
    }
    
    // Additional test cases for edge cases and exception handling scenarios can be added here
}