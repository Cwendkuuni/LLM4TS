package twitter4j;
import static org.junit.Assert.*;
import org.junit.Test;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {
    
    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(40.7128, -74.0060);
        GeoQuery query = new GeoQuery(location);
        assertEquals(location, query.getLocation());
    }
    
    @Test
    public void testGetIp() {
        String ip = "192.168.1.1";
        GeoQuery query = new GeoQuery(ip);
        assertEquals(ip, query.getIp());
    }
    
    @Test
    public void testSetAndGetAccuracy() {
        GeoQuery query = new GeoQuery("192.168.1.1");
        query.setAccuracy("100m");
        assertEquals("100m", query.getAccuracy());
    }
    
    @Test
    public void testSetAndGetGranularity() {
        GeoQuery query = new GeoQuery("192.168.1.1");
        query.setGranularity("city");
        assertEquals("city", query.getGranularity());
    }
    
    @Test
    public void testSetAndGetMaxResults() {
        GeoQuery query = new GeoQuery("192.168.1.1");
        query.setMaxResults(10);
        assertEquals(10, query.getMaxResults());
    }
    
    @Test
    public void testEquals() {
        GeoLocation location = new GeoLocation(40.7128, -74.0060);
        GeoQuery query1 = new GeoQuery(location);
        GeoQuery query2 = new GeoQuery(location);
        assertEquals(true, query1.equals(query2));
    }
    
    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(40.7128, -74.0060);
        GeoQuery query = new GeoQuery(location);
        int expected = 31 * (31 * (31 * (31 * location.hashCode()) + 0) + 0) + 0;
        assertEquals(expected, query.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(40.7128, -74.0060);
        GeoQuery query = new GeoQuery(location);
        String expected = "GeoQuery{location=" + location + ", ip='null', accuracy='null', granularity='null', maxResults=0}";
        assertEquals(expected, query.toString());
    }
}