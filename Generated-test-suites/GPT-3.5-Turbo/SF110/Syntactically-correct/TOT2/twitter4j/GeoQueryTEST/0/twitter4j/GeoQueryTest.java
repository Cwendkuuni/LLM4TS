package twitter4j;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {
    
    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        
        assertEquals(location, geoQuery.getLocation());
    }
    
    @Test
    public void testGetIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        
        assertEquals(ip, geoQuery.getIp());
    }
    
    @Test
    public void testSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        String accuracy = "high";
        
        geoQuery.setAccuracy(accuracy);
        
        assertEquals(accuracy, geoQuery.getAccuracy());
    }
    
    @Test
    public void testSetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        String granularity = "address";
        
        geoQuery.setGranularity(granularity);
        
        assertEquals(granularity, geoQuery.getGranularity());
    }
    
    @Test
    public void testSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        int maxResults = 10;
        
        geoQuery.setMaxResults(maxResults);
        
        assertEquals(maxResults, geoQuery.getMaxResults());
    }
    
    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        assertEquals(geoQuery1, geoQuery2);
        
        GeoQuery geoQuery3 = new GeoQuery("192.168.0.1");
        
        assertNotEquals(geoQuery1, geoQuery3);
    }
    
    @Test
    public void testHashCode() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        assertEquals(geoQuery.hashCode(), geoQuery.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        String expectedString = "GeoQuery{location=GeoLocation{latitude=37.7749, longitude=-122.4194}, ip='null', accuracy='null', granularity='null', maxResults=0}";
        
        assertEquals(expectedString, geoQuery.toString());
    }
}