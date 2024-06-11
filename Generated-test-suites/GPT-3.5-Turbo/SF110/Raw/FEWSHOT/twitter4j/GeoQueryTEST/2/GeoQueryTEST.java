package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GeoQueryTest {
    @Test
    public void testConstructorWithGeoLocation() {
        GeoLocation location = new GeoLocation(1.2345, -2.3456);
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(location, geoQuery.getLocation());
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
    public void testSetAccuracy() {
        String accuracy = "high";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.2345, -2.3456));
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }
    
    @Test
    public void testSetGranularity() {
        String granularity = "neighborhood";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.2345, -2.3456));
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }
    
    @Test
    public void testSetMaxResults() {
        int maxResults = 100;
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.2345, -2.3456));
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAccuracyBuilder() {
        String accuracy = "medium";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.2345, -2.3456)).accuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }
    
    @Test
    public void testGranularityBuilder() {
        String granularity = "city";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.2345, -2.3456)).granularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }
    
    @Test
    public void testMaxResultsBuilder() {
        int maxResults = 50;
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.2345, -2.3456)).maxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArrayWithLocation() {
        GeoLocation location = new GeoLocation(1.2345, -2.3456);
        GeoQuery geoQuery = new GeoQuery(location);
        HttpParameter[] expected = {
            new HttpParameter("lat", "1.2345"),
            new HttpParameter("long", "-2.3456")
        };
        assertArrayEquals(expected, geoQuery.asHttpParameterArray());
    }
    
    @Test
    public void testAsHttpParameterArrayWithIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        HttpParameter[] expected = {
            new HttpParameter("ip", ip)
        };
        assertArrayEquals(expected, geoQuery.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.2345, -2.3456));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.2345, -2.3456));
        GeoQuery geoQuery3 = new GeoQuery("192.168.0.1");
        
        assertEquals(geoQuery1, geoQuery2);
        assertNotEquals(geoQuery1, geoQuery3);
    }
    
    @Test
    public void testHashCode() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.2345, -2.3456));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.2345, -2.3456));
        
        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.2345, -2.3456);
        String ip = "192.168.0.1";
        String accuracy = "high";
        String granularity = "neighborhood";
        int maxResults = 100;
        
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setIp(ip);
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);
        
        String expected = "GeoQuery{location=" + location + ", ip='" + ip + "', accuracy='" + accuracy + "', granularity='" + granularity + "', maxResults=" + maxResults + "}";
        assertEquals(expected, geoQuery.toString());
    }
}