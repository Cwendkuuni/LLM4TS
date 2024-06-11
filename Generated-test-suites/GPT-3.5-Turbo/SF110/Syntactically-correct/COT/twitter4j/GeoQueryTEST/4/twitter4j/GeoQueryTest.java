
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation geoLocation = new GeoLocation(1.234, 5.678);
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        
        assertEquals(geoLocation, geoQuery.getLocation());
    }
    
    @Test
    public void testGetIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        
        assertEquals(ip, geoQuery.getIp());
    }
    
    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.234, 5.678));
        
        assertNull(geoQuery.getAccuracy());
    }
    
    @Test
    public void testSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.234, 5.678));
        String accuracy = "high";
        
        geoQuery.setAccuracy(accuracy);
        
        assertEquals(accuracy, geoQuery.getAccuracy());
    }
    
    @Test
    public void testAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.234, 5.678));
        String accuracy = "high";
        
        GeoQuery result = geoQuery.accuracy(accuracy);
        
        assertEquals(accuracy, result.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.234, 5.678));
        
        assertNull(geoQuery.getGranularity());
    }
    
    @Test
    public void testSetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.234, 5.678));
        String granularity = "neighborhood";
        
        geoQuery.setGranularity(granularity);
        
        assertEquals(granularity, geoQuery.getGranularity());
    }
    
    @Test
    public void testGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.234, 5.678));
        String granularity = "neighborhood";
        
        GeoQuery result = geoQuery.granularity(granularity);
        
        assertEquals(granularity, result.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.234, 5.678));
        
        assertEquals(0, geoQuery.getMaxResults());
    }
    
    @Test
    public void testSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.234, 5.678));
        int maxResults = 10;
        
        geoQuery.setMaxResults(maxResults);
        
        assertEquals(maxResults, geoQuery.getMaxResults());
    }
    
    @Test
    public void testMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.234, 5.678));
        int maxResults = 10;
        
        GeoQuery result = geoQuery.maxResults(maxResults);
        
        assertEquals(maxResults, result.getMaxResults());
    }
}
