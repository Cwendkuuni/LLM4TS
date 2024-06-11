package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GeoQueryTest {
    @Test
    public void testConstructorWithGeoLocation() {
        GeoLocation location = new GeoLocation(0.0, 0.0);
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
    public void testGetSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        String accuracy = "high";
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }
    
    @Test
    public void testAccuracyMethod() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        String accuracy = "high";
        GeoQuery updatedGeoQuery = geoQuery.accuracy(accuracy);
        assertEquals(accuracy, updatedGeoQuery.getAccuracy());
    }
    
    @Test
    public void testGetSetGranularity() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        String granularity = "country";
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }
    
    @Test
    public void testGranularityMethod() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        String granularity = "country";
        GeoQuery updatedGeoQuery = geoQuery.granularity(granularity);
        assertEquals(granularity, updatedGeoQuery.getGranularity());
    }
    
    @Test
    public void testGetSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        int maxResults = 100;
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }
    
    @Test
    public void testMaxResultsMethod() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        int maxResults = 100;
        GeoQuery updatedGeoQuery = geoQuery.maxResults(maxResults);
        assertEquals(maxResults, updatedGeoQuery.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArrayWithLocation() {
        GeoLocation location = new GeoLocation(0.0, 0.0);
        GeoQuery geoQuery = new GeoQuery(location);
        HttpParameter[] expectedParams = {new HttpParameter("lat", "0.0"), new HttpParameter("long", "0.0")};
        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }
    
    @Test
    public void testAsHttpParameterArrayWithIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        HttpParameter[] expectedParams = {new HttpParameter("ip", ip)};
        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }
    
    @Test
    public void testAsHttpParameterArrayWithAccuracy() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        String accuracy = "high";
        geoQuery.setAccuracy(accuracy);
        HttpParameter[] expectedParams = {new HttpParameter("accuracy", accuracy)};
        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }
    
    @Test
    public void testAsHttpParameterArrayWithGranularity() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        String granularity = "country";
        geoQuery.setGranularity(granularity);
        HttpParameter[] expectedParams = {new HttpParameter("granularity", granularity)};
        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }
    
    @Test
    public void testAsHttpParameterArrayWithMaxResults() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        int maxResults = 100;
        geoQuery.setMaxResults(maxResults);
        HttpParameter[] expectedParams = {new HttpParameter("max_results", String.valueOf(maxResults))};
        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        GeoLocation location = new GeoLocation(0.0, 0.0);
        GeoQuery geoQuery1 = new GeoQuery(location);
        GeoQuery geoQuery2 = new GeoQuery(location);
        assertEquals(geoQuery1, geoQuery2);
    }
    
    @Test
    public void testNotEquals() {
        GeoLocation location1 = new GeoLocation(0.0, 0.0);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoLocation location2 = new GeoLocation(1.0, 1.0);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        assertNotEquals(geoQuery1, geoQuery2);
    }
    
    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(0.0, 0.0);
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(geoQuery.hashCode(), geoQuery.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(0.0, 0.0);
        GeoQuery geoQuery = new GeoQuery(location);
        String expectedString = "GeoQuery{location=" + location + ", ip='null', accuracy='null', granularity='null', maxResults=0}";
        assertEquals(expectedString, geoQuery.toString());
    }
}