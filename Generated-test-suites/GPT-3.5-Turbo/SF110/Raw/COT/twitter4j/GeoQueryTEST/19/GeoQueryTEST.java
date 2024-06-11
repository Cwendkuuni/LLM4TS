
import org.junit.*;
import twitter4j.*;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation geoLocation = new GeoLocation(12.34, 56.78);
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        Assert.assertEquals(geoLocation, geoQuery.getLocation());
    }
    
    @Test
    public void testGetIp() {
        String ip = "127.0.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        Assert.assertEquals(ip, geoQuery.getIp());
    }
    
    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(12.34, 56.78));
        geoQuery.setAccuracy("high");
        Assert.assertEquals("high", geoQuery.getAccuracy());
    }
    
    @Test
    public void testSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(12.34, 56.78));
        geoQuery.setAccuracy("high");
        Assert.assertEquals("high", geoQuery.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(12.34, 56.78));
        geoQuery.setGranularity("neighborhood");
        Assert.assertEquals("neighborhood", geoQuery.getGranularity());
    }
    
    @Test
    public void testSetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(12.34, 56.78));
        geoQuery.setGranularity("neighborhood");
        Assert.assertEquals("neighborhood", geoQuery.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(12.34, 56.78));
        geoQuery.setMaxResults(10);
        Assert.assertEquals(10, geoQuery.getMaxResults());
    }
    
    @Test
    public void testSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(12.34, 56.78));
        geoQuery.setMaxResults(10);
        Assert.assertEquals(10, geoQuery.getMaxResults());
    }
    
    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(12.34, 56.78));
        geoQuery1.setAccuracy("high");
        geoQuery1.setGranularity("neighborhood");
        geoQuery1.setMaxResults(10);
        
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(12.34, 56.78));
        geoQuery2.setAccuracy("high");
        geoQuery2.setGranularity("neighborhood");
        geoQuery2.setMaxResults(10);
        
        GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(12.34, 56.78));
        geoQuery3.setAccuracy("low");
        geoQuery3.setGranularity("city");
        geoQuery3.setMaxResults(5);
        
        Assert.assertEquals(true, geoQuery1.equals(geoQuery2));
        Assert.assertEquals(false, geoQuery1.equals(geoQuery3));
    }
    
    @Test
    public void testHashCode() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(12.34, 56.78));
        geoQuery1.setAccuracy("high");
        geoQuery1.setGranularity("neighborhood");
        geoQuery1.setMaxResults(10);
        
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(12.34, 56.78));
        geoQuery2.setAccuracy("high");
        geoQuery2.setGranularity("neighborhood");
        geoQuery2.setMaxResults(10);
        
        GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(12.34, 56.78));
        geoQuery3.setAccuracy("low");
        geoQuery3.setGranularity("city");
        geoQuery3.setMaxResults(5);
        
        Assert.assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
        Assert.assertNotEquals(geoQuery1.hashCode(), geoQuery3.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(12.34, 56.78));
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(10);
        
        String expected = "GeoQuery{location=GeoLocation{latitude=12.34, longitude=56.78}, ip='null', accuracy='high', granularity='neighborhood', maxResults=10}";
        Assert.assertEquals(expected, geoQuery.toString());
    }
}
