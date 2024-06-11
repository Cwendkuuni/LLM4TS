package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GeoQueryTest {
    @Test
    public void testConstructorWithGeoLocation() {
        GeoLocation location = new GeoLocation(37.7833, -122.4167);
        GeoQuery geoQuery = new GeoQuery(location);
        
        assertEquals(location, geoQuery.getLocation());
        assertNull(geoQuery.getIp());
        assertNull(geoQuery.getAccuracy());
        assertNull(geoQuery.getGranularity());
        assertEquals(0, geoQuery.getMaxResults());
    }
    
    @Test
    public void testConstructorWithIp() {
        String ip = "127.0.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        
        assertEquals(ip, geoQuery.getIp());
        assertNull(geoQuery.getLocation());
        assertNull(geoQuery.getAccuracy());
        assertNull(geoQuery.getGranularity());
        assertEquals(0, geoQuery.getMaxResults());
    }
    
    @Test
    public void testSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        String accuracy = "city";
        
        assertNull(geoQuery.getAccuracy());
        
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }
    
    @Test
    public void testAccuracyMethod() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        String accuracy = "neighborhood";
        
        assertNull(geoQuery.getAccuracy());
        
        GeoQuery updatedGeoQuery = geoQuery.accuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
        assertEquals(geoQuery, updatedGeoQuery);
    }
    
    @Test
    public void testSetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        String granularity = "city";
        
        assertNull(geoQuery.getGranularity());
        
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }
    
    @Test
    public void testGranularityMethod() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        String granularity = "neighborhood";
        
        assertNull(geoQuery.getGranularity());
        
        GeoQuery updatedGeoQuery = geoQuery.granularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
        assertEquals(geoQuery, updatedGeoQuery);
    }
    
    @Test
    public void testSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        int maxResults = 10;
        
        assertEquals(0, geoQuery.getMaxResults());
        
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }
    
    @Test
    public void testMaxResultsMethod() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        int maxResults = 20;
        
        assertEquals(0, geoQuery.getMaxResults());
        
        GeoQuery updatedGeoQuery = geoQuery.maxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
        assertEquals(geoQuery, updatedGeoQuery);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(37.7833, -122.4167);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("city");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(10);
        
        HttpParameter[] expectedParams = {
            new HttpParameter("lat", "37.7833"),
            new HttpParameter("long", "-122.4167"),
            new HttpParameter("accuracy", "city"),
            new HttpParameter("granularity", "neighborhood"),
            new HttpParameter("max_results", "10")
        };
        
        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }
    
    @Test
    public void testAppendParameterWithStringValue() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        List<HttpParameter> params = new ArrayList<HttpParameter>();
        
        String name = "accuracy";
        String value = "city";
        
        geoQuery.appendParameter(name, value, params);
        
        assertEquals(1, params.size());
        assertEquals(new HttpParameter(name, value), params.get(0));
    }
    
    @Test
    public void testAppendParameterWithIntValue() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        List<HttpParameter> params = new ArrayList<HttpParameter>();
        
        String name = "max_results";
        int value = 10;
        
        geoQuery.appendParameter(name, value, params);
        
        assertEquals(1, params.size());
        assertEquals(new HttpParameter(name, String.valueOf(value)), params.get(0));
    }
    
    @Test
    public void testAppendParameterWithDoubleValue() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        List<HttpParameter> params = new ArrayList<HttpParameter>();
        
        String name = "lat";
        double value = 37.7833;
        
        geoQuery.appendParameter(name, value, params);
        
        assertEquals(1, params.size());
        assertEquals(new HttpParameter(name, String.valueOf(value)), params.get(0));
    }
    
    @Test
    public void testEquals() {
        GeoLocation location = new GeoLocation(37.7833, -122.4167);
        GeoQuery geoQuery1 = new GeoQuery(location);
        GeoQuery geoQuery2 = new GeoQuery(location);
        GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(0, 0));
        
        assertEquals(geoQuery1, geoQuery2);
        assertNotEquals(geoQuery1, geoQuery3);
        assertNotEquals(geoQuery2, geoQuery3);
    }
    
    @Test
    public void testHashCode() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        
        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(37.7833, -122.4167);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("city");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(10);
        
        String expectedString = "GeoQuery{location=" + location + ", ip='null', accuracy='city', granularity='neighborhood', maxResults=10}";
        
        assertEquals(expectedString, geoQuery.toString());
    }
}