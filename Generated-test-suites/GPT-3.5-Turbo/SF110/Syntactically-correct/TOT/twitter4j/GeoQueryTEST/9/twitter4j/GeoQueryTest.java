import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testConstructor1() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        
        assertEquals(location, geoQuery.getLocation());
        assertNull(geoQuery.getIp());
    }
    
    @Test
    public void testConstructor2() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        
        assertEquals(ip, geoQuery.getIp());
        assertNull(geoQuery.getLocation());
    }
    
    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        
        assertNull(geoQuery.getAccuracy());
    }
    
    @Test
    public void testSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        
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
        
        geoQuery.setGranularity("neighborhood");
        assertEquals("neighborhood", geoQuery.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        
        assertEquals(0, geoQuery.getMaxResults());
    }
    
    @Test
    public void testSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        
        geoQuery.setMaxResults(10);
        assertEquals(10, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(10);
        
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        
        // Check if the parameters are correct
        for (HttpParameter param : params) {
            if (param.getName().equals("lat")) {
                assertEquals("37.7749", param.getValue());
            } else if (param.getName().equals("long")) {
                assertEquals("-122.4194", param.getValue());
            } else if (param.getName().equals("accuracy")) {
                assertEquals("high", param.getValue());
            } else if (param.getName().equals("granularity")) {
                assertEquals("neighborhood", param.getValue());
            } else if (param.getName().equals("max_results")) {
                assertEquals("10", param.getValue());
            } else {
                fail("Invalid parameter name: " + param.getName());
            }
        }
    }
    
    @Test
    public void testEquals_SameObject() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        
        assertTrue(geoQuery.equals(geoQuery));
    }
    
    @Test
    public void testEquals_NullObject() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        
        assertFalse(geoQuery.equals(null));
    }
    
    @Test
    public void testEquals_DifferentClass() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        
        assertFalse(geoQuery.equals(location));
    }
    
    @Test
    public void testEquals_SameAttributes() {
        GeoLocation location1 = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        geoQuery1.setMaxResults(10);
        geoQuery1.setAccuracy("high");
        geoQuery1.setGranularity("neighborhood");
        
        GeoLocation location2 = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        geoQuery2.setMaxResults(10);
        geoQuery2.setAccuracy("high");
        geoQuery2.setGranularity("neighborhood");
        
        assertTrue(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testEquals_DifferentAttributes() {
        GeoLocation location1 = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        geoQuery1.setMaxResults(10);
        geoQuery1.setAccuracy("high");
        geoQuery1.setGranularity("neighborhood");
        
        GeoLocation location2 = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        geoQuery2.setMaxResults(20);
        geoQuery2.setAccuracy("medium");
        geoQuery2.setGranularity("city");
        
        assertFalse(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setMaxResults(10);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("neighborhood");
        
        // Calculate the expected hash code based on the attributes
        int expectedHashCode = 31 * location.hashCode();
        expectedHashCode = 31 * expectedHashCode + (geoQuery.getIp() != null ? geoQuery.getIp().hashCode() : 0);
        expectedHashCode = 31 * expectedHashCode + (geoQuery.getAccuracy() != null ? geoQuery.getAccuracy().hashCode() : 0);
        expectedHashCode = 31 * expectedHashCode + (geoQuery.getGranularity() != null ? geoQuery.getGranularity().hashCode() : 0);
        expectedHashCode = 31 * expectedHashCode + geoQuery.getMaxResults();
        
        assertEquals(expectedHashCode, geoQuery.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setMaxResults(10);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("neighborhood");
        
        String expectedString = "GeoQuery{location=" + location + ", ip='null', accuracy='high', granularity='neighborhood', maxResults=10}";
        
        assertEquals(expectedString, geoQuery.toString());
    }
}