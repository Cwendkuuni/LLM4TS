package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GeoQueryTest {
    @Test
    public void testConstructorWithLocation() {
        GeoLocation location = new GeoLocation(51.5074, -0.1278);
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
    public void testSettersAndGetters() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(51.5074, -0.1278));
        geoQuery.setAccuracy("high");
        assertEquals("high", geoQuery.getAccuracy());
        
        geoQuery.setGranularity("neighborhood");
        assertEquals("neighborhood", geoQuery.getGranularity());
        
        geoQuery.setMaxResults(10);
        assertEquals(10, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(51.5074, -0.1278);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(10);
        
        HttpParameter[] expectedParams = {
            new HttpParameter("lat", "51.5074"),
            new HttpParameter("long", "-0.1278"),
            new HttpParameter("accuracy", "high"),
            new HttpParameter("granularity", "neighborhood"),
            new HttpParameter("max_results", "10")
        };
        
        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(51.5074, -0.1278);
        GeoLocation location2 = new GeoLocation(51.5074, -0.1278);
        GeoLocation location3 = new GeoLocation(40.7128, -74.0060);
        
        GeoQuery geoQuery1 = new GeoQuery(location1);
        geoQuery1.setAccuracy("high");
        geoQuery1.setGranularity("neighborhood");
        geoQuery1.setMaxResults(10);
        
        GeoQuery geoQuery2 = new GeoQuery(location2);
        geoQuery2.setAccuracy("high");
        geoQuery2.setGranularity("neighborhood");
        geoQuery2.setMaxResults(10);
        
        GeoQuery geoQuery3 = new GeoQuery(location3);
        geoQuery3.setAccuracy("medium");
        geoQuery3.setGranularity("city");
        geoQuery3.setMaxResults(5);
        
        assertEquals(geoQuery1, geoQuery2);
        assertNotEquals(geoQuery1, geoQuery3);
        assertNotEquals(geoQuery2, geoQuery3);
    }
    
    @Test
    public void testHashCode() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(51.5074, -0.1278));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(51.5074, -0.1278));
        
        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(51.5074, -0.1278));
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(10);
        
        assertEquals("GeoQuery{location=GeoLocation{latitude=51.5074, longitude=-0.1278}, ip='null', accuracy='high', granularity='neighborhood', maxResults=10}", geoQuery.toString());
    }
}