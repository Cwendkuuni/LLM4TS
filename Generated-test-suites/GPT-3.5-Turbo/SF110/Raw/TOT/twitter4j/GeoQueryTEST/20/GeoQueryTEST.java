import org.junit.*;
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
        String ip = "192.168.0.1";
        GeoQuery query = new GeoQuery(ip);
        
        assertNull(query.getLocation());
        assertEquals(ip, query.getIp());
    }
    
    @Test
    public void testGetAccuracy() {
        GeoQuery query = new GeoQuery("192.168.0.1");
        
        assertNull(query.getAccuracy());
    }
    
    @Test
    public void testSetAccuracy() {
        GeoQuery query = new GeoQuery("192.168.0.1");
        query.setAccuracy("high");
        
        assertEquals("high", query.getAccuracy());
    }
    
    @Test
    public void testAccuracyMethod() {
        GeoQuery query = new GeoQuery("192.168.0.1");
        query.accuracy("high");
        
        assertEquals("high", query.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        assertNull(query.getGranularity());
    }
    
    @Test
    public void testSetGranularity() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        query.setGranularity("neighborhood");
        
        assertEquals("neighborhood", query.getGranularity());
    }
    
    @Test
    public void testGranularityMethod() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        query.granularity("neighborhood");
        
        assertEquals("neighborhood", query.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        assertEquals(0, query.getMaxResults());
    }
    
    @Test
    public void testSetMaxResults() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        query.setMaxResults(10);
        
        assertEquals(10, query.getMaxResults());
    }
    
    @Test
    public void testMaxResultsMethod() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        query.maxResults(10);
        
        assertEquals(10, query.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArrayWithLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery query = new GeoQuery(location);
        
        HttpParameter[] expected = new HttpParameter[2];
        expected[0] = new HttpParameter("lat", "37.7749");
        expected[1] = new HttpParameter("long", "-122.4194");
        
        assertArrayEquals(expected, query.asHttpParameterArray());
    }
    
    @Test
    public void testAsHttpParameterArrayWithIp() {
        String ip = "192.168.0.1";
        GeoQuery query = new GeoQuery(ip);
        
        HttpParameter[] expected = new HttpParameter[1];
        expected[0] = new HttpParameter("ip", "192.168.0.1");
        
        assertArrayEquals(expected, query.asHttpParameterArray());
    }
    
    @Test
    public void testEqualsWithSameInstance() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        assertTrue(query1.equals(query1));
    }
    
    @Test
    public void testEqualsWithNull() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        assertFalse(query1.equals(null));
    }
    
    @Test
    public void testEqualsWithDifferentClass() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        Object obj = new Object();
        
        assertFalse(query1.equals(obj));
    }
    
    @Test
    public void testEqualsWithDifferentMaxResults() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery query2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        query2.setMaxResults(10);
        
        assertFalse(query1.equals(query2));
    }
    
    @Test
    public void testEqualsWithNullAccuracy() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery query2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        query2.setAccuracy(null);
        
        assertFalse(query1.equals(query2));
    }
    
    @Test
    public void testEqualsWithDifferentAccuracy() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery query2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        query2.setAccuracy("high");
        
        assertFalse(query1.equals(query2));
    }

    @Test
    public void testEqualsWithNullGranularity() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery query2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        query2.setGranularity(null);
        
        assertFalse(query1.equals(query2));
    }
    
    @Test
    public void testEqualsWithDifferentGranularity() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery query2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        query2.setGranularity("neighborhood");
        
        assertFalse(query1.equals(query2));
    }
    
    @Test
    public void testEqualsWithNullIp() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery query2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        query2.setIp(null);
        
        assertFalse(query1.equals(query2));
    }
    
    @Test
    public void testEqualsWithDifferentIp() {
        GeoQuery query1 = new GeoQuery("192.168.0.1");
        GeoQuery query2 = new GeoQuery("192.168.0.2");
        
        assertFalse(query1.equals(query2));
    }
    
    @Test
    public void testEqualsWithNullLocation() {
        GeoQuery query1 = new GeoQuery("192.168.0.1");
        GeoQuery query2 = new GeoQuery("192.168.0.1");
        query2.setLocation(null);
        
        assertFalse(query1.equals(query2));
    }
    
    @Test
    public void testEqualsWithDifferentLocation() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery query2 = new GeoQuery(new GeoLocation(37.7748, -122.4195));
        
        assertFalse(query1.equals(query2));
    }
    
    @Test
    public void testHashCodeWithNullLocation() {
        GeoQuery query = new GeoQuery("192.168.0.1");
        
        assertNotNull(query.hashCode());
    }
    
    @Test
    public void testHashCodeWithNullIp() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        assertNotNull(query.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        query.setAccuracy("high");
        query.setGranularity("neighborhood");
        query.setMaxResults(10);
        
        String expected = "GeoQuery{location=GeoLocation{latitude=37.7749, longitude=-122.4194}, ip='null', accuracy='high', granularity='neighborhood', maxResults=10}";
        
        assertEquals(expected, query.toString());
    }
}