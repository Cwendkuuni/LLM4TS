package twitter4j;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class GeoQueryTest {
    private GeoLocation location;
    private GeoQuery geoQuery;
    
    @Before
    public void setup() {
        location = new GeoLocation(37.7833, -122.4167);
    }
    
    @Test
    public void testConstructorWithGeoLocation() {
        geoQuery = new GeoQuery(location);
        assertNotNull(geoQuery.getLocation());
        assertNull(geoQuery.getIp());
    }
    
    @Test
    public void testConstructorWithIp() {
        String ip = "127.0.0.1";
        geoQuery = new GeoQuery(ip);
        assertNull(geoQuery.getLocation());
        assertEquals(ip, geoQuery.getIp());
    }
    
    @Test
    public void testGettersAndSetters() {
        geoQuery = new GeoQuery(location);
        assertNotNull(geoQuery.getLocation());
        assertNull(geoQuery.getIp());
        assertNull(geoQuery.getAccuracy());
        assertNull(geoQuery.getGranularity());
        assertEquals(0, geoQuery.getMaxResults());
        
        geoQuery.setAccuracy("high");
        assertEquals("high", geoQuery.getAccuracy());
        geoQuery.setGranularity("city");
        assertEquals("city", geoQuery.getGranularity());
        geoQuery.setMaxResults(10);
        assertEquals(10, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAccuracyMethodChaining() {
        geoQuery = new GeoQuery(location);
        assertEquals(geoQuery, geoQuery.accuracy("high"));
        assertEquals("high", geoQuery.getAccuracy());
    }
    
    @Test
    public void testGranularityMethodChaining() {
        geoQuery = new GeoQuery(location);
        assertEquals(geoQuery, geoQuery.granularity("city"));
        assertEquals("city", geoQuery.getGranularity());
    }
    
    @Test
    public void testMaxResultsMethodChaining() {
        geoQuery = new GeoQuery(location);
        assertEquals(geoQuery, geoQuery.maxResults(10));
        assertEquals(10, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        geoQuery = new GeoQuery(location);
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertNotNull(params);
        assertEquals(2, params.length);
        assertEquals("lat", params[0].getName());
        assertEquals("37.7833", params[0].getValue());
        assertEquals("long", params[1].getName());
        assertEquals("-122.4167", params[1].getValue());
        
        geoQuery = new GeoQuery("127.0.0.1");
        params = geoQuery.asHttpParameterArray();
        assertNotNull(params);
        assertEquals(1, params.length);
        assertEquals("ip", params[0].getName());
        assertEquals("127.0.0.1", params[0].getValue());
    }
    
    @Test
    public void testEquals() {
        geoQuery = new GeoQuery(location);
        assertTrue(geoQuery.equals(geoQuery));
        assertFalse(geoQuery.equals(null));
        assertFalse(geoQuery.equals(new Object()));
        
        GeoQuery geoQuery2 = new GeoQuery(location);
        assertTrue(geoQuery.equals(geoQuery2));
        
        geoQuery2 = new GeoQuery("127.0.0.1");
        assertFalse(geoQuery.equals(geoQuery2));
        
        geoQuery2 = new GeoQuery(location);
        geoQuery2.setAccuracy("high");
        assertFalse(geoQuery.equals(geoQuery2));
        
        geoQuery2 = new GeoQuery(location);
        geoQuery2.setAccuracy(geoQuery.getAccuracy());
        geoQuery2.setGranularity("city");
        assertFalse(geoQuery.equals(geoQuery2));
        
        geoQuery2 = new GeoQuery(location);
        geoQuery2.setAccuracy(geoQuery.getAccuracy());
        geoQuery2.setGranularity(geoQuery.getGranularity());
        geoQuery2.setMaxResults(10);
        assertFalse(geoQuery.equals(geoQuery2));
    }
    
    @Test
    public void testHashCode() {
        geoQuery = new GeoQuery(location);
        int hashCode = geoQuery.hashCode();
        
        GeoQuery geoQuery2 = new GeoQuery(location);
        assertEquals(hashCode, geoQuery2.hashCode());
        
        geoQuery2 = new GeoQuery("127.0.0.1");
        assertNotEquals(hashCode, geoQuery2.hashCode());
        
        geoQuery2 = new GeoQuery(location);
        geoQuery2.setAccuracy("high");
        assertNotEquals(hashCode, geoQuery2.hashCode());
        
        geoQuery2 = new GeoQuery(location);
        geoQuery2.setAccuracy(geoQuery.getAccuracy());
        geoQuery2.setGranularity("city");
        assertNotEquals(hashCode, geoQuery2.hashCode());
        
        geoQuery2 = new GeoQuery(location);
        geoQuery2.setAccuracy(geoQuery.getAccuracy());
        geoQuery2.setGranularity(geoQuery.getGranularity());
        geoQuery2.setMaxResults(10);
        assertNotEquals(hashCode, geoQuery2.hashCode());
    }
    
    @Test
    public void testToString() {
        geoQuery = new GeoQuery(location);
        String toString = geoQuery.toString();
        assertNotNull(toString);
        assertFalse(toString.isEmpty());
    }
}