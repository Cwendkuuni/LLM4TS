package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GeoQueryTest {
    @Test
    public void testConstructorWithGeoLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery query = new GeoQuery(location);
        
        assertEquals(location, query.getLocation());
        assertNull(query.getIp());
    }
    
    @Test
    public void testConstructorWithIp() {
        String ip = "192.168.0.1";
        GeoQuery query = new GeoQuery(ip);
        
        assertNull(query.getLocation());
        assertEquals(ip, query.getIp());
    }
    
    @Test
    public void testGetAccuracy() {
        GeoQuery query = new GeoQuery("192.168.0.1");
        assertNull(query.getAccuracy());
        
        query.setAccuracy("city");
        assertEquals("city", query.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertNull(query.getGranularity());
        
        query.setGranularity("neighborhood");
        assertEquals("neighborhood", query.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertEquals(0, query.getMaxResults());
        
        query.setMaxResults(10);
        assertEquals(10, query.getMaxResults());
    }
    
    @Test
    public void testEquals() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery query2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertEquals(query1, query2);
        
        query1.setAccuracy("city");
        query2.setAccuracy("city");
        assertEquals(query1, query2);
        
        query1.setMaxResults(10);
        query2.setMaxResults(10);
        assertEquals(query1, query2);
    }
    
    @Test
    public void testHashCode() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery query2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertEquals(query1.hashCode(), query2.hashCode());
        
        query1.setAccuracy("city");
        query2.setAccuracy("city");
        assertEquals(query1.hashCode(), query2.hashCode());
        
        query1.setMaxResults(10);
        query2.setMaxResults(10);
        assertEquals(query1.hashCode(), query2.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertEquals("GeoQuery{location=GeoLocation{latitude=37.7749, longitude=-122.4194}, ip='null', accuracy='null', granularity='null', maxResults=0}", query1.toString());
        
        GeoQuery query2 = new GeoQuery("192.168.0.1");
        assertEquals("GeoQuery{location=null, ip='192.168.0.1', accuracy='null', granularity='null', maxResults=0}", query2.toString());
        
        GeoQuery query3 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        query3.setAccuracy("city");
        query3.setGranularity("neighborhood");
        query3.setMaxResults(10);
        assertEquals("GeoQuery{location=GeoLocation{latitude=37.7749, longitude=-122.4194}, ip='null', accuracy='city', granularity='neighborhood', maxResults=10}", query3.toString());
    }
}