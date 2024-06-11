import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testConstructorWithGeoLocation() {
        GeoLocation location = new GeoLocation(37.7833, -122.4167);
        GeoQuery geoQuery = new GeoQuery(location);
        
        assertEquals(location, geoQuery.getLocation());
        assertNull(geoQuery.getIp());
    }
    
    @Test
    public void testConstructorWithIp() {
        String ip = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        
        assertEquals(ip, geoQuery.getIp());
        assertNull(geoQuery.getLocation());
    }
    
    @Test
    public void testGetAccuracyAndSetAccuracy() {
        GeoLocation location = new GeoLocation(37.7833, -122.4167);
        GeoQuery geoQuery = new GeoQuery(location);
        
        assertNull(geoQuery.getAccuracy());
        
        String accuracy = "high";
        geoQuery.setAccuracy(accuracy);
        
        assertEquals(accuracy, geoQuery.getAccuracy());
    }
    
    @Test
    public void testGetGranularityAndSetGranularity() {
        GeoLocation location = new GeoLocation(37.7833, -122.4167);
        GeoQuery geoQuery = new GeoQuery(location);
        
        assertNull(geoQuery.getGranularity());
        
        String granularity = "low";
        geoQuery.setGranularity(granularity);
        
        assertEquals(granularity, geoQuery.getGranularity());
    }
    
    @Test
    public void testGetMaxResultsAndSetMaxResults() {
        String ip = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        
        assertEquals(0, geoQuery.getMaxResults());
        
        int maxResults = 10;
        geoQuery.setMaxResults(maxResults);
        
        assertEquals(maxResults, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArrayWithLocation() {
        GeoLocation location = new GeoLocation(37.7833, -122.4167);
        GeoQuery geoQuery = new GeoQuery(location);
        
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        
        assertEquals(2, params.length);
        assertEquals("lat", params[0].getName());
        assertEquals("long", params[1].getName());
        assertEquals("37.7833", params[0].getValue());
        assertEquals("-122.4167", params[1].getValue());
    }
    
    @Test
    public void testAsHttpParameterArrayWithIp() {
        String ip = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        
        assertEquals(1, params.length);
        assertEquals("ip", params[0].getName());
        assertEquals("192.168.1.1", params[0].getValue());
    }
    
    @Test
    public void testEquals() {
        GeoLocation location = new GeoLocation(37.7833, -122.4167);
        GeoQuery geoQuery1 = new GeoQuery(location);
        GeoQuery geoQuery2 = new GeoQuery(location);
        
        assertTrue(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(37.7833, -122.4167);
        GeoQuery geoQuery1 = new GeoQuery(location);
        GeoQuery geoQuery2 = new GeoQuery(location);
        
        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(37.7833, -122.4167);
        GeoQuery geoQuery = new GeoQuery(location);
        String expected = "GeoQuery{location=" + location + ", ip='null', accuracy='null', granularity='null', maxResults=0}";
        
        assertEquals(expected, geoQuery.toString());
    }
}