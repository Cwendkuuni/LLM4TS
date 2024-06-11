package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation geoLocation = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        
        assertEquals(geoLocation, geoQuery.getLocation());
    }
    
    @Test
    public void testGetIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        
        assertEquals(ip, geoQuery.getIp());
    }
    
    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        String accuracy = "city";
        
        geoQuery.setAccuracy(accuracy);
        
        assertEquals(accuracy, geoQuery.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        String granularity = "neighborhood";
        
        geoQuery.setGranularity(granularity);
        
        assertEquals(granularity, geoQuery.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        int maxResults = 10;
        
        geoQuery.setMaxResults(maxResults);
        
        assertEquals(maxResults, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAccuracyMethod() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        String accuracy = "neighborhood";
        
        geoQuery.accuracy(accuracy);
        
        assertEquals(accuracy, geoQuery.getAccuracy());
    }
    
    @Test
    public void testGranularityMethod() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        String granularity = "city";
        
        geoQuery.granularity(granularity);
        
        assertEquals(granularity, geoQuery.getGranularity());
    }
    
    @Test
    public void testMaxResultsMethod() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        int maxResults = 5;
        
        geoQuery.maxResults(maxResults);
        
        assertEquals(maxResults, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.setAccuracy("city");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(10);
        
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        
        assertEquals(6, params.length);
        assertEquals("lat", params[0].getName());
        assertEquals("37.7749", params[0].getValue());
        assertEquals("long", params[1].getName());
        assertEquals("-122.4194", params[1].getValue());
        assertEquals("accuracy", params[2].getName());
        assertEquals("city", params[2].getValue());
        assertEquals("granularity", params[3].getName());
        assertEquals("neighborhood", params[3].getValue());
        assertEquals("max_results", params[4].getName());
        assertEquals("10", params[4].getValue());
    }
    
    @Test
    public void testAppendParameterStringString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        List<HttpParameter> params = new ArrayList<HttpParameter>();
        String name = "accuracy";
        String value = "city";
        
        geoQuery.appendParameter(name, value, params);
        
        assertEquals(1, params.size());
        assertEquals(name, params.get(0).getName());
        assertEquals(value, params.get(0).getValue());
    }
    
    @Test
    public void testAppendParameterStringInt() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        List<HttpParameter> params = new ArrayList<HttpParameter>();
        String name = "max_results";
        int value = 10;
        
        geoQuery.appendParameter(name, value, params);
        
        assertEquals(1, params.size());
        assertEquals(name, params.get(0).getName());
        assertEquals(String.valueOf(value), params.get(0).getValue());
    }
    
    @Test
    public void testAppendParameterStringDouble() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        List<HttpParameter> params = new ArrayList<HttpParameter>();
        String name = "lat";
        double value = 37.7749;
        
        geoQuery.appendParameter(name, value, params);
        
        assertEquals(1, params.size());
        assertEquals(name, params.get(0).getName());
        assertEquals(String.valueOf(value), params.get(0).getValue());
    }
    
    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        assertEquals(geoQuery1, geoQuery2);
        
        geoQuery2.setAccuracy("city");
        
        assertNotEquals(geoQuery1, geoQuery2);
    }
    
    @Test
    public void testHashCode() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
        
        geoQuery2.setAccuracy("city");
        
        assertNotEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.setAccuracy("city");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(10);
        
        String expectedToString = "GeoQuery{location=GeoLocation{latitude=37.7749, longitude=-122.4194}, ip='null', accuracy='city', granularity='neighborhood', maxResults=10}";
        
        assertEquals(expectedToString, geoQuery.toString());
    }
}