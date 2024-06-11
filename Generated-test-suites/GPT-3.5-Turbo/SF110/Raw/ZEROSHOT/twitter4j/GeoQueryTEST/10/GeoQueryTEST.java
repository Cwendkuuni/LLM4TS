package twitter4j;

import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation geoLocation = new GeoLocation(50.0, 40.0);
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        assertEquals(geoLocation, geoQuery.getLocation());
    }
    
    @Test
    public void testGetIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        assertEquals(ip, geoQuery.getIp());
    }
    
    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0.0, 0.0));
        assertNull(geoQuery.getAccuracy());
    }
    
    @Test
    public void testSetAccuracy() {
        String accuracy = "high";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0.0, 0.0));
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }
    
    @Test
    public void testAccuracy() {
        String accuracy = "medium";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0.0, 0.0));
        assertEquals(geoQuery, geoQuery.accuracy(accuracy));
        assertEquals(accuracy, geoQuery.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0.0, 0.0));
        assertNull(geoQuery.getGranularity());
    }
    
    @Test
    public void testSetGranularity() {
        String granularity = GeoQuery.CITY;
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0.0, 0.0));
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }
    
    @Test
    public void testGranularity() {
        String granularity = GeoQuery.NEIGHBORHOOD;
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0.0, 0.0));
        assertEquals(geoQuery, geoQuery.granularity(granularity));
        assertEquals(granularity, geoQuery.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0.0, 0.0));
        assertEquals(0, geoQuery.getMaxResults());
    }
    
    @Test
    public void testSetMaxResults() {
        int maxResults = 100;
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0.0, 0.0));
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }
    
    @Test
    public void testMaxResults() {
        int maxResults = 50;
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0.0, 0.0));
        assertEquals(geoQuery, geoQuery.maxResults(maxResults));
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

}