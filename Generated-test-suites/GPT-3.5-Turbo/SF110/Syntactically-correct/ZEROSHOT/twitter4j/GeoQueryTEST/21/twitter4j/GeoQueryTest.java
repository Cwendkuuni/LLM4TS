import org.junit.*;
import static org.junit.Assert.*;

public class GeoQueryTest {
    
    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(40.7128, -74.0060);
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
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(40.7128, -74.0060));
        assertNull(geoQuery.getAccuracy());
        
        geoQuery.setAccuracy("high");
        assertEquals("high", geoQuery.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(40.7128, -74.0060));
        assertNull(geoQuery.getGranularity());
        
        geoQuery.setGranularity("city");
        assertEquals("city", geoQuery.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(40.7128, -74.0060));
        assertEquals(0, geoQuery.getMaxResults());
        
        geoQuery.setMaxResults(10);
        assertEquals(10, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(40.7128, -74.0060));
        GeoQuery updatedQuery = geoQuery.accuracy("high");
        assertEquals("high", updatedQuery.getAccuracy());
    }
    
    @Test
    public void testGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(40.7128, -74.0060));
        GeoQuery updatedQuery = geoQuery.granularity("city");
        assertEquals("city", updatedQuery.getGranularity());
    }
    
    @Test
    public void testMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(40.7128, -74.0060));
        GeoQuery updatedQuery = geoQuery.maxResults(10);
        assertEquals(10, updatedQuery.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(40.7128, -74.0060));
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(10);
        
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        
        assertEquals(5, params.length);
        assertEquals(new HttpParameter("lat", "40.7128"), params[0]);
        assertEquals(new HttpParameter("long", "-74.0060"), params[1]);
        assertEquals(new HttpParameter("accuracy", "high"), params[2]);
        assertEquals(new HttpParameter("granularity", "city"), params[3]);
        assertEquals(new HttpParameter("max_results", "10"), params[4]);
    }
    
    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(40.7128, -74.0060));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(40.7128, -74.0060));
        
        assertTrue(geoQuery1.equals(geoQuery2));
        assertTrue(geoQuery2.equals(geoQuery1));
        
        geoQuery1.setAccuracy("high");
        assertFalse(geoQuery1.equals(geoQuery2));
        assertFalse(geoQuery2.equals(geoQuery1));
    }
    
    @Test
    public void testHashCode() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(40.7128, -74.0060));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(40.7128, -74.0060));
        
        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
        
        geoQuery2.setAccuracy("high");
        assertNotEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(40.7128, -74.0060);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(10);
        
        String expected = "GeoQuery{location=" + location + ", ip='null'" + ", accuracy='high'" + ", granularity='city'" + ", maxResults=10}";
        assertEquals(expected, geoQuery.toString());
    }
}