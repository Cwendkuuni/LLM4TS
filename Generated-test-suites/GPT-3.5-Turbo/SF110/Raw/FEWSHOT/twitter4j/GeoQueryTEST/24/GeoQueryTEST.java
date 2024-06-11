package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GeoQueryTest {
    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(10.2, 20.3);
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(location, geoQuery.getLocation());
    }
    
    @Test
    public void testGetIp() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        assertEquals("192.168.0.1", geoQuery.getIp());
    }
    
    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.2, 20.3));
        assertNull(geoQuery.getAccuracy());
        
        geoQuery.setAccuracy("city");
        assertEquals("city", geoQuery.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.2, 20.3));
        assertNull(geoQuery.getGranularity());
        
        geoQuery.setGranularity("neighborhood");
        assertEquals("neighborhood", geoQuery.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.2, 20.3));
        assertEquals(0, geoQuery.getMaxResults());
        
        geoQuery.setMaxResults(10);
        assertEquals(10, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.2, 20.3));
        
        HttpParameter[] expectedParams = new HttpParameter[2];
        expectedParams[0] = new HttpParameter("lat", "10.2");
        expectedParams[1] = new HttpParameter("long", "20.3");
        
        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(10.2, 20.3));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(10.2, 20.3));
        
        assertEquals(geoQuery1, geoQuery2);
        
        GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(30.2, 40.3));
        
        assertNotEquals(geoQuery1, geoQuery3);
    }
    
    @Test
    public void testHashCode() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(10.2, 20.3));
        int hashCode1 = geoQuery1.hashCode();
        
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(10.2, 20.3));
        int hashCode2 = geoQuery2.hashCode();
        
        assertEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.2, 20.3));
        String expectedString = "GeoQuery{location=GeoLocation{latitude=10.2, longitude=20.3}, ip='null', accuracy='null', granularity='null', maxResults=0}";
        
        assertEquals(expectedString, geoQuery.toString());
    }
}