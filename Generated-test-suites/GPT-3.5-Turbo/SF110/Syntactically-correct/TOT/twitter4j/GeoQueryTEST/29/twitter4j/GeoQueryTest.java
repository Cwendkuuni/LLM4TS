import org.junit.*;
import static org.junit.Assert.*;

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
        assertNull(geoQuery.getAccuracy());
    }
    
    @Test
    public void testSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.setAccuracy("high");
        assertEquals("high", geoQuery.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertNull(geoQuery.getGranularity());
    }
    
    @Test
    public void testSetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.setGranularity("low");
        assertEquals("low", geoQuery.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertEquals(0, geoQuery.getMaxResults());
    }
    
    @Test
    public void testSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.setMaxResults(10);
        assertEquals(10, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAccuracyBuilder() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery result = geoQuery.accuracy("high");
        assertEquals("high", result.getAccuracy());
        assertSame(geoQuery, result);
    }
    
    @Test
    public void testGranularityBuilder() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery result = geoQuery.granularity("low");
        assertEquals("low", result.getGranularity());
        assertSame(geoQuery, result);
    }
    
    @Test
    public void testMaxResultsBuilder() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery result = geoQuery.maxResults(10);
        assertEquals(10, result.getMaxResults());
        assertSame(geoQuery, result);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        GeoLocation geoLocation = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("low");
        geoQuery.setMaxResults(10);
        
        HttpParameter[] expectedParams = new HttpParameter[5];
        expectedParams[0] = new HttpParameter("lat", "37.7749");
        expectedParams[1] = new HttpParameter("long", "-122.4194");
        expectedParams[2] = new HttpParameter("accuracy", "high");
        expectedParams[3] = new HttpParameter("granularity", "low");
        expectedParams[4] = new HttpParameter("max_results", "10");
        
        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }
    
    @Test
    public void testEquals_sameObject() {
        GeoLocation geoLocation = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        assertTrue(geoQuery.equals(geoQuery));
    }
    
    @Test
    public void testEquals_differentClass() {
        GeoLocation geoLocation = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        assertFalse(geoQuery.equals("test"));
    }
    
    @Test
    public void testEquals_differentAccuracy() {
        GeoLocation geoLocation1 = new GeoLocation(37.7749, -122.4194);
        GeoLocation geoLocation2 = new GeoLocation(37.7749, -122.4194);
        
        GeoQuery geoQuery1 = new GeoQuery(geoLocation1);
        GeoQuery geoQuery2 = new GeoQuery(geoLocation2);
        
        geoQuery1.setAccuracy("high");
        
        assertFalse(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testEquals_nullAccuracy() {
        GeoLocation geoLocation1 = new GeoLocation(37.7749, -122.4194);
        GeoLocation geoLocation2 = new GeoLocation(37.7749, -122.4194);
        
        GeoQuery geoQuery1 = new GeoQuery(geoLocation1);
        GeoQuery geoQuery2 = new GeoQuery(geoLocation2);
        
        assertFalse(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testEquals_differentGranularity() {
        GeoLocation geoLocation1 = new GeoLocation(37.7749, -122.4194);
        GeoLocation geoLocation2 = new GeoLocation(37.7749, -122.4194);
        
        GeoQuery geoQuery1 = new GeoQuery(geoLocation1);
        GeoQuery geoQuery2 = new GeoQuery(geoLocation2);
        
        geoQuery1.setGranularity("low");
        
        assertFalse(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testEquals_nullGranularity() {
        GeoLocation geoLocation1 = new GeoLocation(37.7749, -122.4194);
        GeoLocation geoLocation2 = new GeoLocation(37.7749, -122.4194);
        
        GeoQuery geoQuery1 = new GeoQuery(geoLocation1);
        GeoQuery geoQuery2 = new GeoQuery(geoLocation2);
        
        assertFalse(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testEquals_differentIp() {
        GeoQuery geoQuery1 = new GeoQuery("192.168.0.1");
        GeoQuery geoQuery2 = new GeoQuery("192.168.0.2");
        
        assertFalse(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testEquals_nullIp() {
        GeoLocation geoLocation1 = new GeoLocation(37.7749, -122.4194);
        GeoLocation geoLocation2 = new GeoLocation(37.7749, -122.4194);
        
        GeoQuery geoQuery1 = new GeoQuery(geoLocation1);
        GeoQuery geoQuery2 = new GeoQuery(geoLocation2);
        
        assertFalse(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testEquals_differentLocation() {
        GeoLocation geoLocation1 = new GeoLocation(37.7749, -122.4194);
        GeoLocation geoLocation2 = new GeoLocation(37.7750, -122.4195);
        
        GeoQuery geoQuery1 = new GeoQuery(geoLocation1);
        GeoQuery geoQuery2 = new GeoQuery(geoLocation2);
        
        assertFalse(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testEquals_nullLocation() {
        GeoQuery geoQuery1 = new GeoQuery("192.168.0.1");
        GeoQuery geoQuery2 = new GeoQuery("192.168.0.1");
        
        assertFalse(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testHashCode() {
        GeoLocation geoLocation = new GeoLocation(37.7749, -122.4194);
        
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("low");
        geoQuery.setLocation(geoLocation);
        geoQuery.setMaxResults(10);
        
        int hashCode = geoLocation.hashCode();
        hashCode = 31 * hashCode + "192.168.0.1".hashCode();
        hashCode = 31 * hashCode + "high".hashCode();
        hashCode = 31 * hashCode + "low".hashCode();
        hashCode = 31 * hashCode + 10;
        
        assertEquals(hashCode, geoQuery.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoLocation geoLocation = new GeoLocation(37.7749, -122.4194);
        
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("low");
        geoQuery.setLocation(geoLocation);
        geoQuery.setMaxResults(10);
        
        String expectedString = "GeoQuery{location=" + geoLocation + ", ip='192.168.0.1', accuracy='high', granularity='low', maxResults=10}";
        assertEquals(expectedString, geoQuery.toString());
    }

}