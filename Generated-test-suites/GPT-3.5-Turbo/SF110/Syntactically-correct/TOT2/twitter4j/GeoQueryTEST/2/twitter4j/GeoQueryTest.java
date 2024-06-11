package twitter4j;
import static org.junit.Assert.*;
import org.junit.Test;
import twitter4j.GeoQuery;
import twitter4j.GeoLocation;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        
        assertEquals(location, geoQuery.getLocation());
    }
    
    @Test
    public void testGetIp() {
        String ip = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        
        assertEquals(ip, geoQuery.getIp());
    }
    
    @Test
    public void testSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");
        geoQuery.setAccuracy("high");
        
        assertEquals("high", geoQuery.getAccuracy());
    }
    
    @Test
    public void testSetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.setGranularity("neighborhood");
        
        assertEquals("neighborhood", geoQuery.getGranularity());
    }

    @Test
    public void testSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");
        geoQuery.setMaxResults(10);
        
        assertEquals(10, geoQuery.getMaxResults());
    }
    
    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery("192.168.1.1");
        GeoQuery geoQuery2 = new GeoQuery("192.168.1.1");
        
        assertTrue(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testHashCode() {
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");
        
        assertEquals("192.168.1.1".hashCode(), geoQuery.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");
        
        assertEquals("GeoQuery{location=null, ip='192.168.1.1', accuracy=null, granularity=null, maxResults=0}", geoQuery.toString());
    }
}