package twitter4j;

import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        
        assertEquals(location, geoQuery.getLocation());
    }
    
    @Test
    public void testGetIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        
        assertEquals(ip, geoQuery.getIp());
    }
    
    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        
        assertNull(geoQuery.getAccuracy());
    }
    
    @Test
    public void testSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        String accuracy = "high";
        
        geoQuery.setAccuracy(accuracy);
        
        assertEquals(accuracy, geoQuery.getAccuracy());
    }
    
    @Test
    public void testAccuracy() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        String accuracy = "medium";
        
        GeoQuery result = geoQuery.accuracy(accuracy);
        
        assertEquals(accuracy, result.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        
        assertNull(geoQuery.getGranularity());
    }
    
    @Test
    public void testSetGranularity() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        String granularity = "city";
        
        geoQuery.setGranularity(granularity);
        
        assertEquals(granularity, geoQuery.getGranularity());
    }
    
    @Test
    public void testGranularity() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        String granularity = "neighborhood";
        
        GeoQuery result = geoQuery.granularity(granularity);
        
        assertEquals(granularity, result.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        
        assertEquals(0, geoQuery.getMaxResults());
    }
    
    @Test
    public void testSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        int maxResults = 10;
        
        geoQuery.setMaxResults(maxResults);
        
        assertEquals(maxResults, geoQuery.getMaxResults());
    }
    
    @Test
    public void testMaxResults() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        int maxResults = 5;
        
        GeoQuery result = geoQuery.maxResults(maxResults);
        
        assertEquals(maxResults, result.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(10);
        
        HttpParameter[] expectedParams = {
            new HttpParameter("lat", "37.7749"),
            new HttpParameter("long", "-122.4194"),
            new HttpParameter("accuracy", "high"),
            new HttpParameter("granularity", "city"),
            new HttpParameter("max_results", "10")
        };
        
        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery1 = new GeoQuery(location);
        GeoQuery geoQuery2 = new GeoQuery(location);
        
        assertTrue(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testNotEquals() {
        GeoLocation location1 = new GeoLocation(37.7749, -122.4194);
        GeoLocation location2 = new GeoLocation(47.6097, -122.3331);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        
        assertFalse(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        
        int expectedHashCode = 31 * (31 * (31 * (31 * location.hashCode() + location.hashCode()) + location.hashCode()) + location.hashCode()) + location.hashCode();
        
        assertEquals(expectedHashCode, geoQuery.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(10);
        
        String expectedString = "GeoQuery{location=" + location + ", ip='null', accuracy='high', granularity='city', maxResults=10}";
        
        assertEquals(expectedString, geoQuery.toString());
    }
}