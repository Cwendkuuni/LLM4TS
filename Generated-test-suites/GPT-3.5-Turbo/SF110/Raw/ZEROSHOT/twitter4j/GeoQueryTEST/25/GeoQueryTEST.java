package twitter4j;

import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
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
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery.setAccuracy("high");
        assertEquals("high", geoQuery.getAccuracy());
    }
    
    @Test
    public void testAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery.accuracy("high");
        assertEquals("high", geoQuery.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery.setGranularity("city");
        assertEquals("city", geoQuery.getGranularity());
    }
    
    @Test
    public void testGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery.granularity("city");
        assertEquals("city", geoQuery.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery.setMaxResults(10);
        assertEquals(10, geoQuery.getMaxResults());
    }
    
    @Test
    public void testMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery.maxResults(10);
        assertEquals(10, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(10);
        
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertNotNull(params);
        assertEquals(5, params.length);
        
        // Validate parameter values
        assertEquals("lat", params[0].getName());
        assertEquals("1.0", params[0].getValue());
        
        assertEquals("long", params[1].getName());
        assertEquals("2.0", params[1].getValue());
        
        assertEquals("accuracy", params[2].getName());
        assertEquals("high", params[2].getValue());
        
        assertEquals("granularity", params[3].getName());
        assertEquals("city", params[3].getValue());
        
        assertEquals("max_results", params[4].getName());
        assertEquals("10", params[4].getValue());
    }
    
    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery1.setAccuracy("high");
        geoQuery1.setGranularity("city");
        geoQuery1.setMaxResults(10);
        
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery2.setAccuracy("high");
        geoQuery2.setGranularity("city");
        geoQuery2.setMaxResults(10);
        
        assertTrue(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testHashCode() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery1.setAccuracy("high");
        geoQuery1.setGranularity("city");
        geoQuery1.setMaxResults(10);
        
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery2.setAccuracy("high");
        geoQuery2.setGranularity("city");
        geoQuery2.setMaxResults(10);
        
        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(10);
        
        String expected = "GeoQuery{location=GeoLocation{latitude=1.0, longitude=2.0}, ip='null', accuracy='high', granularity='city', maxResults=10}";
        assertEquals(expected, geoQuery.toString());
    }

}