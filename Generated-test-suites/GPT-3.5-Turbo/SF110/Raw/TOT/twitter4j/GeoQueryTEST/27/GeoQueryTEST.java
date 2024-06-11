
package twitter4j;

import org.junit.Assert;
import org.junit.Test;

public class GeoQueryTest {
    
    @Test
    public void testConstructorWithLocation() {
        // Typical use case
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery geoQuery = new GeoQuery(location);
        
        Assert.assertEquals(location, geoQuery.getLocation());
        Assert.assertNull(geoQuery.getIp());
    }
    
    @Test
    public void testConstructorWithIp() {
        // Typical use case
        String ip = "127.0.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        
        Assert.assertEquals(ip, geoQuery.getIp());
        Assert.assertNull(geoQuery.getLocation());
    }
    
    @Test
    public void testGetAndSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery("127.0.0.1");
        
        // Typical use case
        String accuracy = "city";
        geoQuery.setAccuracy(accuracy);
        Assert.assertEquals(accuracy, geoQuery.getAccuracy());
        
        // Edge case: accuracy is null
        geoQuery.setAccuracy(null);
        Assert.assertNull(geoQuery.getAccuracy());
    }
    
    @Test
    public void testAccuracyMethod() {
        GeoQuery geoQuery = new GeoQuery("127.0.0.1");
        
        // Typical use case
        String accuracy = "city";
        GeoQuery result = geoQuery.accuracy(accuracy);
        
        Assert.assertEquals(accuracy, geoQuery.getAccuracy());
        Assert.assertEquals(geoQuery, result);
    }
    
    @Test
    public void testGetAndSetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        
        // Typical use case
        String granularity = "neighborhood";
        geoQuery.setGranularity(granularity);
        Assert.assertEquals(granularity, geoQuery.getGranularity());
        
        // Edge case: granularity is null
        geoQuery.setGranularity(null);
        Assert.assertNull(geoQuery.getGranularity());
    }
    
    @Test
    public void testGranularityMethod() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        
        // Typical use case
        String granularity = "neighborhood";
        GeoQuery result = geoQuery.granularity(granularity);
        
        Assert.assertEquals(granularity, geoQuery.getGranularity());
        Assert.assertEquals(geoQuery, result);
    }
    
    @Test
    public void testGetAndSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery("127.0.0.1");
        
        // Typical use case
        int maxResults = 100;
        geoQuery.setMaxResults(maxResults);
        Assert.assertEquals(maxResults, geoQuery.getMaxResults());
        
        // Edge case: maxResults is 0
        geoQuery.setMaxResults(0);
        Assert.assertEquals(0, geoQuery.getMaxResults());
        
        // Edge case: maxResults is negative
        geoQuery.setMaxResults(-10);
        Assert.assertEquals(-10, geoQuery.getMaxResults());
    }
    
    @Test
    public void testMaxResultsMethod() {
        GeoQuery geoQuery = new GeoQuery("127.0.0.1");
        
        // Typical use case
        int maxResults = 100;
        GeoQuery result = geoQuery.maxResults(maxResults);
        
        Assert.assertEquals(maxResults, geoQuery.getMaxResults());
        Assert.assertEquals(geoQuery, result);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        
        // Typical use case
        geoQuery.setAccuracy("city");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(100);
        HttpParameter[] parameters = geoQuery.asHttpParameterArray();
        
        Assert.assertEquals(5, parameters.length);
        Assert.assertEquals(new HttpParameter("lat", "10.0"), parameters[0]);
        Assert.assertEquals(new HttpParameter("long", "20.0"), parameters[1]);
        Assert.assertEquals(new HttpParameter("accuracy", "city"), parameters[2]);
        Assert.assertEquals(new HttpParameter("granularity", "neighborhood"), parameters[3]);
        Assert.assertEquals(new HttpParameter("max_results", "100"), parameters[4]);
    }
    
    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(10.0, 20.0));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(10.0, 20.0));
        GeoQuery geoQuery3 = new GeoQuery("127.0.0.1");
        
        // Typical use case: equal objects
        Assert.assertTrue(geoQuery1.equals(geoQuery2));
        
        // Typical use case: unequal objects
        Assert.assertFalse(geoQuery1.equals(geoQuery3));
        
        // Typical use case: comparing to itself
        Assert.assertTrue(geoQuery1.equals(geoQuery1));
        
        // Edge case: comparing to null
        Assert.assertFalse(geoQuery1.equals(null));
        
        // Edge case: comparing to different object type
        Assert.assertFalse(geoQuery1.equals("string"));
    }
    
    @Test
    public void testHashCode() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(10.0, 20.0));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(10.0, 20.0));
        GeoQuery geoQuery3 = new GeoQuery("127.0.0.1");
        
        // Typical use case: equal objects have the same hash code
        Assert.assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
        
        // Typical use case: unequal objects have different hash code
        Assert.assertNotEquals(geoQuery1.hashCode(), geoQuery3.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        
        // Typical use case
        geoQuery.setIp("127.0.0.1");
        geoQuery.setAccuracy("city");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(100);
        
        String expected = "GeoQuery{location=GeoLocation{latitude=10.0, longitude=20.0}, ip='127.0.0.1', accuracy='city', granularity='neighborhood', maxResults=100}";
        Assert.assertEquals(expected, geoQuery.toString());
    }
}

