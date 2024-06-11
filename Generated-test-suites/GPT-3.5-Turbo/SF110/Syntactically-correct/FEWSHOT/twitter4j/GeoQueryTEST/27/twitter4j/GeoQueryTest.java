package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GeoQueryTest {
    
    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(location, geoQuery.getLocation());
    }
    
    @Test
    public void testGetIp() {
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");
        assertEquals("192.168.1.1", geoQuery.getIp());
    }
    
    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertNull(geoQuery.getAccuracy());
        
        geoQuery.setAccuracy("high");
        assertEquals("high", geoQuery.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertNull(geoQuery.getGranularity());
        
        geoQuery.setGranularity("neighborhood");
        assertEquals("neighborhood", geoQuery.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertEquals(0, geoQuery.getMaxResults());
        
        geoQuery.setMaxResults(100);
        assertEquals(100, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertEquals(geoQuery, geoQuery.accuracy("high"));
        assertEquals("high", geoQuery.getAccuracy());
    }
    
    @Test
    public void testGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertEquals(geoQuery, geoQuery.granularity("neighborhood"));
        assertEquals("neighborhood", geoQuery.getGranularity());
    }
    
    @Test
    public void testMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertEquals(geoQuery, geoQuery.maxResults(100));
        assertEquals(100, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        HttpParameter[] expectedParams1 = {
            new HttpParameter("lat", "37.7749"),
            new HttpParameter("long", "-122.4194")
        };
        assertArrayEquals(expectedParams1, geoQuery1.asHttpParameterArray());
        
        GeoQuery geoQuery2 = new GeoQuery("192.168.1.1");
        HttpParameter[] expectedParams2 = {
            new HttpParameter("ip", "192.168.1.1")
        };
        assertArrayEquals(expectedParams2, geoQuery2.asHttpParameterArray());
        
        GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery3.setAccuracy("high");
        geoQuery3.setGranularity("neighborhood");
        geoQuery3.setMaxResults(100);
        HttpParameter[] expectedParams3 = {
            new HttpParameter("lat", "37.7749"),
            new HttpParameter("long", "-122.4194"),
            new HttpParameter("accuracy", "high"),
            new HttpParameter("granularity", "neighborhood"),
            new HttpParameter("max_results", "100")
        };
        assertArrayEquals(expectedParams3, geoQuery3.asHttpParameterArray());
    }
    
    @Test
    public void testEqualsAndHashCode() {
        GeoLocation location1 = new GeoLocation(37.7749, -122.4194);
        GeoLocation location2 = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        
        assertEquals(geoQuery1, geoQuery2);
        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertEquals("GeoQuery{location=GeoLocation{latitude=37.7749, longitude=-122.4194}, ip='null', accuracy='null', granularity='null', maxResults=0}", geoQuery.toString());
    }
    
}