import org.junit.*;
import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testConstructorWithGeoLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        
        assertEquals(location, geoQuery.getLocation());
        assertNull(geoQuery.getIp());
    }
    
    @Test
    public void testConstructorWithIp() {
        String ip = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        
        assertEquals(ip, geoQuery.getIp());
        assertNull(geoQuery.getLocation());
    }
    
    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");
        assertNull(geoQuery.getAccuracy());
        
        geoQuery.setAccuracy("low");
        assertEquals("low", geoQuery.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertNull(geoQuery.getGranularity());
        
        geoQuery.setGranularity("city");
        assertEquals("city", geoQuery.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");
        assertEquals(0, geoQuery.getMaxResults());
        
        geoQuery.setMaxResults(10);
        assertEquals(10, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAccuracyMethod() {
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");
        assertEquals(geoQuery, geoQuery.accuracy("low"));
        assertEquals("low", geoQuery.getAccuracy());
    }
    
    @Test
    public void testGranularityMethod() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertEquals(geoQuery, geoQuery.granularity("city"));
        assertEquals("city", geoQuery.getGranularity());
    }
    
    @Test
    public void testMaxResultsMethod() {
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");
        assertEquals(geoQuery, geoQuery.maxResults(10));
        assertEquals(10, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("low");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(10);
        
        HttpParameter[] expectedParams = new HttpParameter[5];
        expectedParams[0] = new HttpParameter("lat", "37.7749");
        expectedParams[1] = new HttpParameter("long", "-122.4194");
        expectedParams[2] = new HttpParameter("accuracy", "low");
        expectedParams[3] = new HttpParameter("granularity", "city");
        expectedParams[4] = new HttpParameter("max_results", "10");
        
        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        
        GeoLocation location2 = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        
        assertFalse(geoQuery1.equals(null));
        assertFalse(geoQuery1.equals(new Object()));
        assertTrue(geoQuery1.equals(geoQuery1));
        assertTrue(geoQuery1.equals(geoQuery2));
        
        geoQuery1.setAccuracy("low");
        assertFalse(geoQuery1.equals(geoQuery2));
        
        geoQuery2.setAccuracy("low");
        assertTrue(geoQuery1.equals(geoQuery2));
        
        geoQuery1.setGranularity("city");
        assertFalse(geoQuery1.equals(geoQuery2));
        
        geoQuery2.setGranularity("city");
        assertTrue(geoQuery1.equals(geoQuery2));
        
        geoQuery1.setIp("192.168.1.1");
        assertFalse(geoQuery1.equals(geoQuery2));
        
        geoQuery2.setIp("192.168.1.1");
        assertTrue(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("low");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(10);
        
        int expectedHashCode = 990893785;
        assertEquals(expectedHashCode, geoQuery.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.setAccuracy("low");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(10);
        
        String expectedString = "GeoQuery{location=GeoLocation{latitude=37.7749, longitude=-122.4194}, ip='null', accuracy='low', granularity='city', maxResults=10}";
        assertEquals(expectedString, geoQuery.toString());
    }
}