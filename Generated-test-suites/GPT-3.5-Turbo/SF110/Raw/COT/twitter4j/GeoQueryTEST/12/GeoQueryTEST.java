import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

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
    public void testGetAccuracyAndSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery("");
        assertNull(geoQuery.getAccuracy());
        
        String accuracy = "high";
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }
    
    @Test
    public void testGetGranularityAndSetGranularity() {
        GeoQuery geoQuery = new GeoQuery("");
        assertNull(geoQuery.getGranularity());
        
        String granularity = "city";
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }
    
    @Test
    public void testGetMaxResultsAndSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery("");
        assertEquals(0, geoQuery.getMaxResults());
        
        int maxResults = 100;
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAccuracyMethod() {
        GeoQuery geoQuery = new GeoQuery("");
        assertNull(geoQuery.getAccuracy());
        
        String accuracy = "low";
        GeoQuery updatedGeoQuery = geoQuery.accuracy(accuracy);
        assertEquals(accuracy, updatedGeoQuery.getAccuracy());
    }
    
    @Test
    public void testGranularityMethod() {
        GeoQuery geoQuery = new GeoQuery("");
        assertNull(geoQuery.getGranularity());
        
        String granularity = "neighborhood";
        GeoQuery updatedGeoQuery = geoQuery.granularity(granularity);
        assertEquals(granularity, updatedGeoQuery.getGranularity());
    }
    
    @Test
    public void testMaxResultsMethod() {
        GeoQuery geoQuery = new GeoQuery("");
        assertEquals(0, geoQuery.getMaxResults());
        
        int maxResults = 50;
        GeoQuery updatedGeoQuery = geoQuery.maxResults(maxResults);
        assertEquals(maxResults, updatedGeoQuery.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(100);
        
        HttpParameter[] expected = { 
            new HttpParameter("lat", "37.7749"),
            new HttpParameter("long", "-122.4194"),
            new HttpParameter("accuracy", "high"),
            new HttpParameter("granularity", "city"),
            new HttpParameter("max_results", "100")
        };

        HttpParameter[] actual = geoQuery.asHttpParameterArray();
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testAppendParameterWithStringValue() {
        String name = "accuracy";
        String value = "medium";
        List<HttpParameter> params = new ArrayList<>();
        
        GeoQuery geoQuery = new GeoQuery("");
        geoQuery.appendParameter(name, value, params);
        
        HttpParameter expected = new HttpParameter(name, value);
        HttpParameter actual = params.get(0);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppendParameterWithIntValue() {
        String name = "max_results";
        int value = 50;
        List<HttpParameter> params = new ArrayList<>();
        
        GeoQuery geoQuery = new GeoQuery("");
        geoQuery.appendParameter(name, value, params);
        
        HttpParameter expected = new HttpParameter(name, String.valueOf(value));
        HttpParameter actual = params.get(0);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAppendParameterWithDoubleValue() {
        String name = "lat";
        double value = 37.7749;
        List<HttpParameter> params = new ArrayList<>();
        
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(value, -122.4194));
        geoQuery.appendParameter(name, value, params);
        
        HttpParameter expected = new HttpParameter(name, String.valueOf(value));
        HttpParameter actual = params.get(0);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(37.7749, -122.4194);
        GeoLocation location2 = new GeoLocation(37.7749, -122.4194);
        
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        
        assertTrue(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        String ip = "192.168.1.1";
        String accuracy = "high";
        String granularity = "city";
        int maxResults = 100;
        
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setIp(ip);
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);
        
        int expected = 1071584993;
        int actual = geoQuery.hashCode();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        String ip = "192.168.1.1";
        String accuracy = "high";
        String granularity = "city";
        int maxResults = 100;
        
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setIp(ip);
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);
        
        String expected = "GeoQuery{location=GeoLocation{latitude=37.7749, longitude=-122.4194}, ip='192.168.1.1', accuracy='high', granularity='city', maxResults=100}";
        String actual = geoQuery.toString();
        
        assertEquals(expected, actual);
    }
}