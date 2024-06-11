import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testConstructorWithGeoLocation() {
        GeoLocation geoLocation = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        assertEquals(geoLocation, geoQuery.getLocation());
        assertNull(geoQuery.getIp());
    }
    
    @Test
    public void testConstructorWithIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        assertNull(geoQuery.getLocation());
        assertEquals(ip, geoQuery.getIp());
    }
    
    @Test
    public void testGetSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        assertNull(geoQuery.getAccuracy());
        
        geoQuery.setAccuracy("high");
        assertEquals("high", geoQuery.getAccuracy());
    }
    
    @Test
    public void testGetSetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        assertNull(geoQuery.getGranularity());
        
        geoQuery.setGranularity("medium");
        assertEquals("medium", geoQuery.getGranularity());
    }
    
    @Test
    public void testGetSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        assertEquals(0, geoQuery.getMaxResults());
        
        geoQuery.setMaxResults(10);
        assertEquals(10, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        double latitude = 37.7749;
        double longitude = -122.4194;
        GeoLocation geoLocation = new GeoLocation(latitude, longitude);
        String ip = "192.168.0.1";
        String accuracy = "high";
        String granularity = "medium";
        int maxResults = 10;
        
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        geoQuery.setIp(ip);
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);
        
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        
        assertEquals(6, params.length);
        assertEquals("lat", params[0].getName());
        assertEquals(String.valueOf(latitude), params[0].getValue());
        assertEquals("long", params[1].getName());
        assertEquals(String.valueOf(longitude), params[1].getValue());
        assertEquals("ip", params[2].getName());
        assertEquals(ip, params[2].getValue());
        assertEquals("accuracy", params[3].getName());
        assertEquals(accuracy, params[3].getValue());
        assertEquals("granularity", params[4].getName());
        assertEquals(granularity, params[4].getValue());
        assertEquals("max_results", params[5].getName());
        assertEquals(String.valueOf(maxResults), params[5].getValue());
    }
    
    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(37.7749, -122.4194);
        GeoLocation location2 = new GeoLocation(37.7749, -122.4194);
        GeoLocation location3 = new GeoLocation(0, 0);
        
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        GeoQuery geoQuery3 = new GeoQuery(location3);
        
        assertTrue(geoQuery1.equals(geoQuery2));
        assertFalse(geoQuery1.equals(geoQuery3));
    }
    
    @Test
    public void testHashCode() {
        GeoLocation location1 = new GeoLocation(37.7749, -122.4194);
        GeoLocation location2 = new GeoLocation(37.7749, -122.4194);
        GeoLocation location3 = new GeoLocation(0, 0);
        
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        GeoQuery geoQuery3 = new GeoQuery(location3);
        
        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
        assertNotEquals(geoQuery1.hashCode(), geoQuery3.hashCode());
    }
    
    @Test
    public void testToString() {
        double latitude = 37.7749;
        double longitude = -122.4194;
        GeoLocation geoLocation = new GeoLocation(latitude, longitude);
        String ip = "192.168.0.1";
        String accuracy = "high";
        String granularity = "medium";
        int maxResults = 10;
        
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        geoQuery.setIp(ip);
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);
        
        String expectedToString = "GeoQuery{location=" + geoLocation +
                ", ip='" + ip + '\'' +
                ", accuracy='" + accuracy + '\'' +
                ", granularity='" + granularity + '\'' +
                ", maxResults=" + maxResults +
                '}';
        
        assertEquals(expectedToString, geoQuery.toString());
    }
}