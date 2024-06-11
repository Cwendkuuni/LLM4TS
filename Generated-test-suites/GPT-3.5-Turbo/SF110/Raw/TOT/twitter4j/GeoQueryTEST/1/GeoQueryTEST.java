import org.junit.*;
import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        
        assertEquals(location, geoQuery.getLocation());
    }
    
    @Test
    public void testGetIp() {
        GeoQuery geoQuery = new GeoQuery("123.456.789.0");
        
        assertEquals("123.456.789.0", geoQuery.getIp());
    }
    
    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery("123.456.789.0");
        geoQuery.setAccuracy("high");
        
        assertEquals("high", geoQuery.getAccuracy());
    }
    
    @Test
    public void testSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery("123.456.789.0");
        geoQuery.setAccuracy("high");
        
        assertEquals("high", geoQuery.getAccuracy());
    }
    
    @Test
    public void testAccuracyMethod() {
        GeoQuery geoQuery = new GeoQuery("123.456.789.0");
        geoQuery.accuracy("low");
        
        assertEquals("low", geoQuery.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery("123.456.789.0");
        geoQuery.setGranularity("country");
        
        assertEquals("country", geoQuery.getGranularity());
    }
    
    @Test
    public void testSetGranularity() {
        GeoQuery geoQuery = new GeoQuery("123.456.789.0");
        geoQuery.setGranularity("country");
        
        assertEquals("country", geoQuery.getGranularity());
    }
    
    @Test
    public void testGranularityMethod() {
        GeoQuery geoQuery = new GeoQuery("123.456.789.0");
        geoQuery.granularity("city");
        
        assertEquals("city", geoQuery.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery("123.456.789.0");
        geoQuery.setMaxResults(10);
        
        assertEquals(10, geoQuery.getMaxResults());
    }
    
    @Test
    public void testSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery("123.456.789.0");
        geoQuery.setMaxResults(10);
        
        assertEquals(10, geoQuery.getMaxResults());
    }
    
    @Test
    public void testMaxResultsMethod() {
        GeoQuery geoQuery = new GeoQuery("123.456.789.0");
        geoQuery.maxResults(5);
        
        assertEquals(5, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArrayWithLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("country");
        geoQuery.setMaxResults(10);
        
        HttpParameter[] expectedParams = {
            new HttpParameter("lat", "37.7749"),
            new HttpParameter("long", "-122.4194"),
            new HttpParameter("accuracy", "high"),
            new HttpParameter("granularity", "country"),
            new HttpParameter("max_results", "10")
        };
        
        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }
    
    @Test
    public void testAsHttpParameterArrayWithIp() {
        GeoQuery geoQuery = new GeoQuery("123.456.789.0");
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("country");
        geoQuery.setMaxResults(10);
        
        HttpParameter[] expectedParams = {
            new HttpParameter("ip", "123.456.789.0"),
            new HttpParameter("accuracy", "high"),
            new HttpParameter("granularity", "country"),
            new HttpParameter("max_results", "10")
        };
        
        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }
    
    @Test
    public void testEqualsWithEqualObjects() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery1 = new GeoQuery(location);
        GeoQuery geoQuery2 = new GeoQuery(location);
        
        assertTrue(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testEqualsWithDifferentObjects() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery1 = new GeoQuery(location);
        GeoQuery geoQuery2 = new GeoQuery("123.456.789.0");
        
        assertFalse(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testHashCode() {
        GeoLocation location1 = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        geoQuery1.setAccuracy("high");
        geoQuery1.setGranularity("country");
        geoQuery1.setMaxResults(10);
        
        GeoLocation location2 = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        geoQuery2.setAccuracy("high");
        geoQuery2.setGranularity("country");
        geoQuery2.setMaxResults(10);
        
        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }
    
    @Test
    public void testToStringWithLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("country");
        geoQuery.setMaxResults(10);
        
        String expectedString = "GeoQuery{location=GeoLocation{latitude=37.7749, longitude=-122.4194}, ip='null', accuracy='high', granularity='country', maxResults=10}";
        
        assertEquals(expectedString, geoQuery.toString());
    }
    
    @Test
    public void testToStringWithIp() {
        GeoQuery geoQuery = new GeoQuery("123.456.789.0");
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("country");
        geoQuery.setMaxResults(10);
        
        String expectedString = "GeoQuery{location=null, ip='123.456.789.0', accuracy='high', granularity='country', maxResults=10}";
        
        assertEquals(expectedString, geoQuery.toString());
    }
}