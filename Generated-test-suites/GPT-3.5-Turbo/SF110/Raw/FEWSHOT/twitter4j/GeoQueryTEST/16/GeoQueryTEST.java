import org.junit.Assert;
import org.junit.Test;
import twitter4j.GeoLocation;

public class GeoQueryTest {
    
    @Test
    public void testConstructorWithGeoLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        Assert.assertEquals(location, geoQuery.getLocation());
        Assert.assertNull(geoQuery.getIp());
    }
    
    @Test
    public void testConstructorWithIp() {
        String ip = "127.0.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        Assert.assertNull(geoQuery.getLocation());
        Assert.assertEquals(ip, geoQuery.getIp());
    }
    
    @Test
    public void testSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        String accuracy = "high";
        geoQuery.setAccuracy(accuracy);
        Assert.assertEquals(accuracy, geoQuery.getAccuracy());
    }
    
    @Test
    public void testGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        String granularity = "neighborhood";
        geoQuery.setGranularity(granularity);
        Assert.assertEquals(granularity, geoQuery.getGranularity());
    }
    
    @Test
    public void testMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        int maxResults = 100;
        geoQuery.setMaxResults(maxResults);
        Assert.assertEquals(maxResults, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(50);
        
        HttpParameter[] expectedParams = {
            new HttpParameter("lat", "37.7749"),
            new HttpParameter("long", "-122.4194"),
            new HttpParameter("accuracy", "high"),
            new HttpParameter("granularity", "city"),
            new HttpParameter("max_results", "50")
        };
        
        HttpParameter[] actualParams = geoQuery.asHttpParameterArray();
        
        Assert.assertArrayEquals(expectedParams, actualParams);
    }
    
    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        Assert.assertEquals(geoQuery1, geoQuery2);
        Assert.assertEquals(geoQuery2, geoQuery1);
        
        geoQuery1.setAccuracy("high");
        Assert.assertNotEquals(geoQuery1, geoQuery2);
        Assert.assertNotEquals(geoQuery2, geoQuery1);
        geoQuery2.setAccuracy("high");
        Assert.assertEquals(geoQuery1, geoQuery2);
        Assert.assertEquals(geoQuery2, geoQuery1);
        
        geoQuery1.setGranularity("city");
        Assert.assertNotEquals(geoQuery1, geoQuery2);
        Assert.assertNotEquals(geoQuery2, geoQuery1);
        geoQuery2.setGranularity("city");
        Assert.assertEquals(geoQuery1, geoQuery2);
        Assert.assertEquals(geoQuery2, geoQuery1);
        
        geoQuery1.setMaxResults(50);
        Assert.assertNotEquals(geoQuery1, geoQuery2);
        Assert.assertNotEquals(geoQuery2, geoQuery1);
        geoQuery2.setMaxResults(50);
        Assert.assertEquals(geoQuery1, geoQuery2);
        Assert.assertEquals(geoQuery2, geoQuery1);
    }
    
    @Test
    public void testHashCode() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        Assert.assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
        
        geoQuery1.setAccuracy("high");
        Assert.assertNotEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
        geoQuery2.setAccuracy("high");
        Assert.assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
        
        geoQuery1.setGranularity("city");
        Assert.assertNotEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
        geoQuery2.setGranularity("city");
        Assert.assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
        
        geoQuery1.setMaxResults(50);
        Assert.assertNotEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
        geoQuery2.setMaxResults(50);
        Assert.assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(50);
        
        String expectedToString = "GeoQuery{location=GeoLocation{latitude=37.7749, longitude=-122.4194}, ip='null', accuracy='high', granularity='city', maxResults=50}";
        
        Assert.assertEquals(expectedToString, geoQuery.toString());
    }
}