package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GeoQueryTest {
    @Test
    public void testConstructorWithGeoLocation() {
        GeoLocation geoLocation = new GeoLocation(1.2345, 2.3456);
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        
        assertEquals(geoLocation, geoQuery.getLocation());
        assertNull(geoQuery.getIp());
        assertNull(geoQuery.getAccuracy());
        assertNull(geoQuery.getGranularity());
        assertEquals(0, geoQuery.getMaxResults());
    }
    
    @Test
    public void testConstructorWithIp() {
        String ip = "1.2.3.4";
        GeoQuery geoQuery = new GeoQuery(ip);
        
        assertNull(geoQuery.getLocation());
        assertEquals(ip, geoQuery.getIp());
        assertNull(geoQuery.getAccuracy());
        assertNull(geoQuery.getGranularity());
        assertEquals(0, geoQuery.getMaxResults());
    }
    
    @Test
    public void testSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.2345, 2.3456));
        
        geoQuery.setAccuracy("high");
        assertEquals("high", geoQuery.getAccuracy());
    }
    
    @Test
    public void testSetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.2345, 2.3456));
        
        geoQuery.setGranularity("neighborhood");
        assertEquals("neighborhood", geoQuery.getGranularity());
    }
    
    @Test
    public void testSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.2345, 2.3456));
        
        geoQuery.setMaxResults(10);
        assertEquals(10, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        GeoLocation geoLocation = new GeoLocation(1.2345, 2.3456);
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        
        HttpParameter[] httpParameters = geoQuery.asHttpParameterArray();
        assertEquals(2, httpParameters.length);
        assertEquals("lat", httpParameters[0].getName());
        assertEquals(String.valueOf(geoLocation.getLatitude()), httpParameters[0].getValue());
        assertEquals("long", httpParameters[1].getName());
        assertEquals(String.valueOf(geoLocation.getLongitude()), httpParameters[1].getValue());
    }
    
    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.2345, 2.3456));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.2345, 2.3456));
        GeoQuery geoQuery3 = new GeoQuery("1.2.3.4");
        
        assertEquals(geoQuery1, geoQuery2);
        assertEquals(geoQuery2, geoQuery1);
        assertEquals(geoQuery3, geoQuery3);
        assertEquals(geoQuery1, geoQuery1);
    }
    
    @Test
    public void testHashCode() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.2345, 2.3456));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.2345, 2.3456));
        
        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.2345, 2.3456));
        
        String expected = "GeoQuery{location=GeoLocation{latitude=1.2345, longitude=2.3456}, ip='null', accuracy='null', granularity='null', maxResults=0}";
        assertEquals(expected, geoQuery.toString());
    }
}