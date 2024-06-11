package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GeoQueryTest {
    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(37.7833, -122.4167);
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(location, geoQuery.getLocation());
    }
    
    @Test
    public void testGetIp() {
        String ip = "127.0.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        assertEquals(ip, geoQuery.getIp());
    }
    
    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        assertNull(geoQuery.getAccuracy());
        
        geoQuery.setAccuracy("neighborhood");
        assertEquals("neighborhood", geoQuery.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        assertNull(geoQuery.getGranularity());
        
        geoQuery.setGranularity("city");
        assertEquals("city", geoQuery.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        assertEquals(0, geoQuery.getMaxResults());
        
        geoQuery.setMaxResults(10);
        assertEquals(10, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        
        assertEquals(2, params.length);
        assertEquals("lat", params[0].getName());
        assertEquals("37.7833", params[0].getValue());
        assertEquals("long", params[1].getName());
        assertEquals("-122.4167", params[1].getValue());
        
        geoQuery = new GeoQuery("127.0.0.1");
        params = geoQuery.asHttpParameterArray();
        
        assertEquals(1, params.length);
        assertEquals("ip", params[0].getName());
        assertEquals("127.0.0.1", params[0].getValue());
    }
    
    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        assertEquals(geoQuery1, geoQuery2);
        
        geoQuery1.setAccuracy("neighborhood");
        assertEquals(geoQuery1, geoQuery2);
        
        geoQuery2.setAccuracy("city");
        assertNotEquals(geoQuery1, geoQuery2);
    }
    
    @Test
    public void testHashCode() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
        
        geoQuery1.setAccuracy("neighborhood");
        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
        
        geoQuery2.setAccuracy("city");
        assertNotEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        String expected = "GeoQuery{location=GeoLocation{latitude=37.7833, longitude=-122.4167}, ip='null', accuracy='null', granularity='null', maxResults=0}";
        assertEquals(expected, geoQuery.toString());
        
        geoQuery.setAccuracy("neighborhood");
        expected = "GeoQuery{location=GeoLocation{latitude=37.7833, longitude=-122.4167}, ip='null', accuracy='neighborhood', granularity='null', maxResults=0}";
        assertEquals(expected, geoQuery.toString());
    }
}