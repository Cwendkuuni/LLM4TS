package twitter4j;

import org.junit.Test;
import static org.junit.Assert.*;

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
        String ip = "127.0.0.1";
        GeoQuery query = new GeoQuery(ip);
        
        assertEquals(ip, query.getIp());
        assertNull(query.getLocation());
    }
    
    @Test
    public void testGetAndSetAccuracy() {
        GeoQuery query = new GeoQuery("127.0.0.1");
        
        query.setAccuracy("high");
        assertEquals("high", query.getAccuracy());
    }
    
    @Test
    public void testGetAndSetGranularity() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        query.setGranularity("neighborhood");
        assertEquals("neighborhood", query.getGranularity());
    }
    
    @Test
    public void testGetAndSetMaxResults() {
        GeoQuery query = new GeoQuery("127.0.0.1");
        
        query.setMaxResults(10);
        assertEquals(10, query.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArrayWithLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery query = new GeoQuery(location);
        
        HttpParameter[] parameters = query.asHttpParameterArray();
        
        assertEquals(2, parameters.length);
        assertEquals("lat", parameters[0].getName());
        assertEquals("37.7749", parameters[0].getValue());
        assertEquals("long", parameters[1].getName());
        assertEquals("-122.4194", parameters[1].getValue());
    }
    
    @Test
    public void testAsHttpParameterArrayWithIp() {
        String ip = "127.0.0.1";
        GeoQuery query = new GeoQuery(ip);
        
        HttpParameter[] parameters = query.asHttpParameterArray();
        
        assertEquals(1, parameters.length);
        assertEquals("ip", parameters[0].getName());
        assertEquals("127.0.0.1", parameters[0].getValue());
    }
    
    @Test
    public void testEqualsWithSameObject() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        assertTrue(query.equals(query));
    }
    
    @Test
    public void testEqualsWithDifferentClass() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        String notQuery = "Not a GeoQuery object";
        
        assertFalse(query.equals(notQuery));
    }
    
    @Test
    public void testEqualsWithDifferentMaxResults() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery query2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        query1.setMaxResults(10);
        query2.setMaxResults(20);
        
        assertFalse(query1.equals(query2));
    }
    
    @Test
    public void testEqualsWithNullAccuracy() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery query2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        query1.setAccuracy(null);
        query2.setAccuracy("high");
        
        assertFalse(query1.equals(query2));
    }
    
    @Test
    public void testEqualsWithNullGranularity() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery query2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        query1.setGranularity(null);
        query2.setGranularity("neighborhood");
        
        assertFalse(query1.equals(query2));
    }
    
    @Test
    public void testEqualsWithNullIp() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery query2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        query1.setIp(null);
        query2.setIp("127.0.0.1");
        
        assertFalse(query1.equals(query2));
    }
    
    @Test
    public void testEqualsWithNullLocation() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery query2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        query1.setLocation(null);
        query2.setLocation(new GeoLocation(37.7749, -122.4194));
        
        assertFalse(query1.equals(query2));
    }
    
    @Test
    public void testEqualsWithSameValues() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery query2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        assertEquals(query1, query2);
    }
    
    @Test
    public void testHashCodeWithNullLocation() {
        GeoQuery query = new GeoQuery("127.0.0.1");
        
        query.setLocation(null);
        
        int hashCode = query.hashCode();
        
        assertEquals(1639, hashCode);
    }
    
    @Test
    public void testHashCodeWithNullMaxResults() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        query.setMaxResults(0);
        
        int hashCode = query.hashCode();
        
        assertEquals(281200739, hashCode);
    }
    
    @Test
    public void testToString() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        query.setAccuracy("high");
        query.setGranularity("neighborhood");
        query.setMaxResults(10);
        
        String expected = "GeoQuery{location=GeoLocation{latitude=37.7749, longitude=-122.4194}, ip='null', accuracy='high', granularity='neighborhood', maxResults=10}";
        String actual = query.toString();
        
        assertEquals(expected, actual);
    }
    
}