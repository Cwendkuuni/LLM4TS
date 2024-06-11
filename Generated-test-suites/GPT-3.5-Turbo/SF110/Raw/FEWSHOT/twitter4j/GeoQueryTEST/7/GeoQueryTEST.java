
package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GeoQueryTest {
    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(12.34, 56.78);
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
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(12.34, 56.78));
        
        assertNull(geoQuery.getAccuracy());
    }
    
    @Test
    public void testSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(12.34, 56.78));
        String accuracy = "high";
        
        geoQuery.setAccuracy(accuracy);
        
        assertEquals(accuracy, geoQuery.getAccuracy());
    }
    
    @Test
    public void testGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(12.34, 56.78));
        String granularity = "neighborhood";
        
        geoQuery.setGranularity(granularity);
        
        assertEquals(granularity, geoQuery.getGranularity());
    }
    
    @Test
    public void testMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(12.34, 56.78));
        int maxResults = 10;
        
        geoQuery.setMaxResults(maxResults);
        
        assertEquals(maxResults, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(12.34, 56.78));
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(10);
        
        HttpParameter[] httpParams = geoQuery.asHttpParameterArray();
        
        // Assert the size and values of the HttpParameter array
    }
    
    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(12.34, 56.78);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        geoQuery1.setGranularity("neighborhood");
        
        GeoLocation location2 = new GeoLocation(12.34, 56.78);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        geoQuery2.setGranularity("neighborhood");
        
        assertEquals(geoQuery1, geoQuery2);
    }
    
    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(12.34, 56.78);
        GeoQuery geoQuery = new GeoQuery(location);
        
        assertEquals(location.hashCode(), geoQuery.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(12.34, 56.78);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setGranularity("neighborhood");
        
        String expectedToString = "GeoQuery{location=" + location + ", ip='null', accuracy='null', granularity='neighborhood', maxResults=0}";
        
        assertEquals(expectedToString, geoQuery.toString());
    }
}
