
package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class GeoQueryTest {
    
    @Test
    public void testConstructorWithGeoLocation() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery query = new GeoQuery(location);
        assertEquals(location, query.getLocation());
        assertNull(query.getIp());
        assertNull(query.getAccuracy());
        assertNull(query.getGranularity());
        assertEquals(0, query.getMaxResults());
    }
    
    @Test
    public void testConstructorWithIp() {
        String ip = "192.168.0.1";
        GeoQuery query = new GeoQuery(ip);
        assertNull(query.getLocation());
        assertEquals(ip, query.getIp());
        assertNull(query.getAccuracy());
        assertNull(query.getGranularity());
        assertEquals(0, query.getMaxResults());
    }
    
    @Test
    public void testSetAccuracy() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery query = new GeoQuery(location);
        
        String accuracy = "100m";
        query.setAccuracy(accuracy);
        assertEquals(accuracy, query.getAccuracy());
    }
    
    @Test
    public void testGranularity() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery query = new GeoQuery(location);
        
        String granularity = "city";
        query.setGranularity(granularity);
        assertEquals(granularity, query.getGranularity());
    }
    
    @Test
    public void testMaxResults() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery query = new GeoQuery(location);
        
        int maxResults = 10;
        query.setMaxResults(maxResults);
        assertEquals(maxResults, query.getMaxResults());
    }
    
    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(10.0, 20.0);
        GeoQuery query1 = new GeoQuery(location1);
        
        GeoLocation location2 = new GeoLocation(10.0, 20.0);
        GeoQuery query2 = new GeoQuery(location2);
        
        assertTrue(query1.equals(query2));
    }
    
    @Test
    public void testNotEquals() {
        GeoLocation location1 = new GeoLocation(10.0, 20.0);
        GeoQuery query1 = new GeoQuery(location1);
        
        GeoLocation location2 = new GeoLocation(30.0, 40.0);
        GeoQuery query2 = new GeoQuery(location2);
        
        assertFalse(query1.equals(query2));
    }
    
    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery query = new GeoQuery(location);
        
        int expectedHashCode = 961;
        
        assertEquals(expectedHashCode, query.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery query = new GeoQuery(location);
        
        String expectedToString = "GeoQuery{location=GeoLocation{latitude=10.0, longitude=20.0}, ip='null', accuracy='null', granularity='null', maxResults=0}";
        
        assertEquals(expectedToString, query.toString());
    }
}
