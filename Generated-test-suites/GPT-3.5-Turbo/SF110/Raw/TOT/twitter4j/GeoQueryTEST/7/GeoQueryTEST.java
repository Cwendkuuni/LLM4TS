import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GeoQuery.class})

public class GeoQueryTest {
    
    @Spy
    private GeoQuery geoQuery;
    
    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        
        assertEquals("Should return the correct location", location, geoQuery.getLocation());
    }
    
    @Test
    public void testGetIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        
        assertEquals("Should return the correct IP address", ip, geoQuery.getIp());
    }
    
    @Test
    public void testSetAccuracy() {
        String accuracy = "high";
        geoQuery.setAccuracy(accuracy);
        
        assertEquals("Should set the accuracy correctly", accuracy, geoQuery.getAccuracy());
    }
    
    @Test
    public void testAccuracy() {
        String accuracy = "low";
        GeoQuery geoQuery = new GeoQuery();
        geoQuery.accuracy(accuracy);
        
        assertEquals("Should set the accuracy correctly", accuracy, geoQuery.getAccuracy());
    }
    
    @Test
    public void testSetGranularity() {
        String granularity = "specific";
        geoQuery.setGranularity(granularity);
        
        assertEquals("Should set the granularity correctly", granularity, geoQuery.getGranularity());
    }
    
    @Test
    public void testGranularity() {
        String granularity = "general";
        GeoQuery geoQuery = new GeoQuery();
        geoQuery.granularity(granularity);
        
        assertEquals("Should set the granularity correctly", granularity, geoQuery.getGranularity());
    }
    
    @Test
    public void testSetMaxResults() {
        int maxResults = 50;
        geoQuery.setMaxResults(maxResults);
        
        assertEquals("Should set the max results correctly", maxResults, geoQuery.getMaxResults());
    }
    
    @Test
    public void testMaxResults() {
        int maxResults = 100;
        GeoQuery geoQuery = new GeoQuery();
        geoQuery.maxResults(maxResults);
        
        assertEquals("Should set the max results correctly", maxResults, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArrayWithLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        
        assertEquals("Should have two parameters for latitude and longitude", 2, params.length);
        assertEquals("Should have latitude parameter with correct value", "lat", params[0].getName());
        assertEquals("Should have longitude parameter with correct value", "long", params[1].getName());
        assertEquals("Should have correct latitude value", String.valueOf(location.getLatitude()), params[0].getValue());
        assertEquals("Should have correct longitude value", String.valueOf(location.getLongitude()), params[1].getValue());
    }
    
    @Test
    public void testAsHttpParameterArrayWithIP() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        
        assertEquals("Should have one parameter for IP", 1, params.length);
        assertEquals("Should have IP parameter with correct value", "ip", params[0].getName());
        assertEquals("Should have correct IP value", ip, params[0].getValue());
    }
    
    @Test
    public void testAsHttpParameterArrayWithNullValues() {
        GeoQuery geoQuery = new GeoQuery();
        
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        
        assertEquals("Should have no parameters if no values are set", 0, params.length);
    }
    
    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(37.7749, -122.4194);
        GeoLocation location2 = new GeoLocation(37.7749, -122.4194);
        String ip = "192.168.0.1";
        String accuracy = "high";
        String granularity = "specific";
        int maxResults = 50;
        
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        geoQuery2.setAccuracy(accuracy);
        geoQuery2.setGranularity(granularity);
        geoQuery2.setMaxResults(maxResults);
        
        assertTrue("Should return true for equal GeoQuery objects", geoQuery1.equals(geoQuery1));
        assertTrue("Should return true for equal GeoQuery objects", geoQuery1.equals(geoQuery2));
        
        geoQuery2.setAccuracy("low");
        
        assertFalse("Should return false for unequal GeoQuery objects", geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        String ip = "192.168.0.1";
        String accuracy = "high";
        String granularity = "specific";
        int maxResults = 50;
        
        geoQuery.setLocation(location);
        geoQuery.setIp(ip);
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);
        
        int expectedHashCode = location.hashCode() * 31 + ip.hashCode() * 31 + accuracy.hashCode() * 31 + granularity.hashCode() * 31 + maxResults;
        
        assertEquals("Should return the correct hash code", expectedHashCode, geoQuery.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        String ip = "192.168.0.1";
        String accuracy = "high";
        String granularity = "specific";
        int maxResults = 50;
        
        geoQuery.setLocation(location);
        geoQuery.setIp(ip);
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);
        
        String expectedString = "GeoQuery{location=" + location + ", ip='" + ip + '\'' + ", accuracy='" + accuracy + '\'' + ", granularity='" + granularity + '\'' + ", maxResults=" + maxResults + '}';
        
        assertEquals("Should return the correct string representation", expectedString, geoQuery.toString());
    }
}