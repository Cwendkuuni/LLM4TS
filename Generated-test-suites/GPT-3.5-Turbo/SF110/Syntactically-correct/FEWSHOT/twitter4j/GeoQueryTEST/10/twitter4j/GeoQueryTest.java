package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GeoQueryTest {
    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery query = new GeoQuery(location);
        assertEquals(location, query.getLocation());
    }
    
    @Test
    public void testGetIp() {
        GeoQuery query = new GeoQuery("192.168.0.1");
        assertEquals("192.168.0.1", query.getIp());
    }
    
    @Test
    public void testGetAccuracy() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        query.setAccuracy("high");
        assertEquals("high", query.getAccuracy());
    }
    
    @Test
    public void testSetAccuracy() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        query.setAccuracy("high");
        assertEquals("high", query.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        query.setGranularity("city");
        assertEquals("city", query.getGranularity());
    }
    
    @Test
    public void testSetGranularity() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        query.setGranularity("city");
        assertEquals("city", query.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        query.setMaxResults(100);
        assertEquals(100, query.getMaxResults());
    }
    
    @Test
    public void testSetMaxResults() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        query.setMaxResults(100);
        assertEquals(100, query.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        query.setAccuracy("high");
        query.setGranularity("city");
        query.setMaxResults(100);
        
        HttpParameter[] params = query.asHttpParameterArray();
        
        assertEquals("lat", params[0].getName());
        assertEquals("37.7749", params[0].getValue());
        assertEquals("long", params[1].getName());
        assertEquals("-122.4194", params[1].getValue());
        assertEquals("accuracy", params[2].getName());
        assertEquals("high", params[2].getValue());
        assertEquals("granularity", params[3].getName());
        assertEquals("city", params[3].getValue());
        assertEquals("max_results", params[4].getName());
        assertEquals("100", params[4].getValue());
    }
    
    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(37.7749, -122.4194);
        GeoQuery query1 = new GeoQuery(location1);
        query1.setAccuracy("high");
        query1.setGranularity("city");
        query1.setMaxResults(100);
        
        GeoLocation location2 = new GeoLocation(37.7749, -122.4194);
        GeoQuery query2 = new GeoQuery(location2);
        query2.setAccuracy("high");
        query2.setGranularity("city");
        query2.setMaxResults(100);
        
        assertEquals(query1, query2);
    }
    
    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery query1 = new GeoQuery(location);
        query1.setAccuracy("high");
        query1.setGranularity("city");
        query1.setMaxResults(100);
        
        query1.hashCode();
    }
    
    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery query = new GeoQuery(location);
        query.setAccuracy("high");
        query.setGranularity("city");
        query.setMaxResults(100);
        
        query.toString();
    }
}