package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GeoQueryTest {
    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        
        assertEquals(location, geoQuery.getLocation());
    }
    
    @Test
    public void testGetIp() {
        String ip = "127.0.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        
        assertEquals(ip, geoQuery.getIp());
    }
    
    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(null);
        
        assertNull(geoQuery.getAccuracy());
        
        geoQuery.setAccuracy("high");
        assertEquals("high", geoQuery.getAccuracy());
        
        geoQuery.setAccuracy("low");
        assertEquals("low", geoQuery.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(null);
        
        assertNull(geoQuery.getGranularity());
        
        geoQuery.setGranularity(GeoQuery.CITY);
        assertEquals(GeoQuery.CITY, geoQuery.getGranularity());
        
        geoQuery.setGranularity(GeoQuery.NEIGHBORHOOD);
        assertEquals(GeoQuery.NEIGHBORHOOD, geoQuery.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(null);
        
        assertEquals(0, geoQuery.getMaxResults());
        
        geoQuery.setMaxResults(10);
        assertEquals(10, geoQuery.getMaxResults());
        
        geoQuery.setMaxResults(100);
        assertEquals(100, geoQuery.getMaxResults());
    }
    
    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        
        GeoLocation location2 = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        
        GeoLocation location3 = new GeoLocation(40.7128, -74.0060);
        GeoQuery geoQuery3 = new GeoQuery(location3);
        
        assertEquals(geoQuery1, geoQuery2);
        assertNotEquals(geoQuery1, geoQuery3);
    }
    
    @Test
    public void testHashCode() {
        GeoLocation location1 = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        
        GeoLocation location2 = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        
        GeoLocation location3 = new GeoLocation(40.7128, -74.0060);
        GeoQuery geoQuery3 = new GeoQuery(location3);
        
        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
        assertNotEquals(geoQuery1.hashCode(), geoQuery3.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity(GeoQuery.CITY);
        geoQuery.setMaxResults(100);
        
        String expected = "GeoQuery{location=GeoLocation{latitude=37.7749, longitude=-122.4194}, ip='null', accuracy='high', granularity='city', maxResults=100}";
        
        assertEquals(expected, geoQuery.toString());
    }
}