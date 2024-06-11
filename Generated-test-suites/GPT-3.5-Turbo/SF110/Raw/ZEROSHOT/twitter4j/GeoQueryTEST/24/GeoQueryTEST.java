import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation geoLocation = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        assertEquals(geoLocation, geoQuery.getLocation());
    }
    
    @Test
    public void testGetIp() {
        String ip = "127.0.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        assertEquals(ip, geoQuery.getIp());
    }
    
    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        assertNull(geoQuery.getAccuracy());
    }
    
    @Test
    public void testSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        String accuracy = "high";
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }
    
    @Test
    public void testAccuray() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        String accuracy = "high";
        GeoQuery result = geoQuery.accuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
        assertEquals(geoQuery, result);
    }
    
    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        assertNull(geoQuery.getGranularity());
    }
    
    @Test
    public void testSetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        String granularity = "neighborhood";
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }
    
    @Test
    public void testGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        String granularity = "neighborhood";
        GeoQuery result = geoQuery.granularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
        assertEquals(geoQuery, result);
    }
    
    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        assertEquals(0, geoQuery.getMaxResults());
    }
    
    @Test
    public void testSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        int maxResults = 10;
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }
    
    @Test
    public void testMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        int maxResults = 10;
        GeoQuery result = geoQuery.maxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
        assertEquals(geoQuery, result);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        GeoLocation geoLocation = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(10);
        
        HttpParameter[] expectedParams = new HttpParameter[] {
            new HttpParameter("lat", "1.0"),
            new HttpParameter("long", "2.0"),
            new HttpParameter("accuracy", "high"),
            new HttpParameter("granularity", "neighborhood"),
            new HttpParameter("max_results", "10")
        };
        
        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }
    
    @Test
    public void testEquals_sameObject() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        assertTrue(geoQuery.equals(geoQuery));
    }
    
    @Test
    public void testEquals_nullObject() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        assertFalse(geoQuery.equals(null));
    }
    
    @Test
    public void testEquals_differentClass() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        assertFalse(geoQuery.equals(new Object()));
    }
    
    @Test
    public void testEquals_differentMaxResults() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery1.setMaxResults(10);
        
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery2.setMaxResults(5);
        
        assertFalse(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testEquals_sameValues() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery1.setAccuracy("high");
        geoQuery1.setGranularity("neighborhood");
        geoQuery1.setMaxResults(10);
        
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery2.setAccuracy("high");
        geoQuery2.setGranularity("neighborhood");
        geoQuery2.setMaxResults(10);
        
        assertTrue(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testEquals_differentAccuracy() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery1.setAccuracy("high");
        
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery2.setAccuracy("medium");
        
        assertFalse(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testEquals_differentGranularity() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery1.setGranularity("neighborhood");
        
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery2.setGranularity("city");
        
        assertFalse(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testEquals_differentIp() {
        GeoQuery geoQuery1 = new GeoQuery("127.0.0.1");
        
        GeoQuery geoQuery2 = new GeoQuery("192.168.0.1");
        
        assertFalse(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testEquals_sameLocation() {
        GeoLocation geoLocation = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery1 = new GeoQuery(geoLocation);
        
        GeoQuery geoQuery2 = new GeoQuery(geoLocation);
        
        assertTrue(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testEquals_nullLocation() {
        GeoQuery geoQuery1 = new GeoQuery("127.0.0.1");
        
        GeoQuery geoQuery2 = new GeoQuery((GeoLocation)null);
        
        assertFalse(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testHashCode_sameLocation() {
        GeoLocation geoLocation = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery1 = new GeoQuery(geoLocation);
        
        GeoQuery geoQuery2 = new GeoQuery(geoLocation);
        
        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }
    
    @Test
    public void testHashCode_nullLocation() {
        GeoQuery geoQuery1 = new GeoQuery("127.0.0.1");
        
        GeoQuery geoQuery2 = new GeoQuery((GeoLocation)null);
        
        assertNotEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoLocation geoLocation = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        String accuracy = "high";
        String granularity = "neighborhood";
        int maxResults = 10;
        
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);
        
        String expectedString = "GeoQuery{location=" + geoLocation + ", ip='null', accuracy='" + accuracy + "', granularity='" + granularity + "', maxResults=" + maxResults + "}";
        
        assertEquals(expectedString, geoQuery.toString());
    }
}