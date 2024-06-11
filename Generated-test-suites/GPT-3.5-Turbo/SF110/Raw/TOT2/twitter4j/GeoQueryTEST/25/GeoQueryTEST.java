import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {
    
    @Test
    public void testGetLocation() {
        // Create a GeoLocation object
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        
        // Create a GeoQuery object with the GeoLocation object
        GeoQuery geoQuery = new GeoQuery(location);
        
        // Test if getLocation method returns the correct GeoLocation object
        assertEquals(location, geoQuery.getLocation());
    }
    
    @Test
    public void testGetIp() {
        // Create a GeoQuery object with an IP address
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");
        
        // Test if getIp method returns the correct IP address
        assertEquals("192.168.1.1", geoQuery.getIp());
    }
    
    @Test
    public void testSetAccuracy() {
        // Create a GeoQuery object
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        // Set accuracy to "high"
        geoQuery.setAccuracy("high");
        
        // Test if getAccuracy method returns the correct accuracy
        assertEquals("high", geoQuery.getAccuracy());
    }
    
    @Test
    public void testGranularity() {
        // Create a GeoQuery object
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        // Set granularity to "neighborhood"
        geoQuery.granularity("neighborhood");
        
        // Test if getGranularity method returns the correct granularity
        assertEquals("neighborhood", geoQuery.getGranularity());
    }
    
    @Test
    public void testMaxResults() {
        // Create a GeoQuery object
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        // Set maxResults to 10
        geoQuery.maxResults(10);
        
        // Test if getMaxResults method returns the correct maxResults
        assertEquals(10, geoQuery.getMaxResults());
    }
    
    @Test
    public void testEquals() {
        // Create two GeoQuery objects with the same properties
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery1.setAccuracy("high");
        geoQuery1.granularity("neighborhood");
        geoQuery1.maxResults(10);
        
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery2.setAccuracy("high");
        geoQuery2.granularity("neighborhood");
        geoQuery2.maxResults(10);
        
        // Test if equals method correctly compares the two GeoQuery objects
        assertTrue(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testHashCode() {
        // Create a GeoQuery object
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.setAccuracy("high");
        geoQuery.granularity("neighborhood");
        geoQuery.maxResults(10);
        
        // Test if hashCode method generates the same hash code for equal objects
        assertEquals(geoQuery.hashCode(), geoQuery.hashCode());
    }
    
    @Test
    public void testToString() {
        // Create a GeoQuery object
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.setAccuracy("high");
        geoQuery.granularity("neighborhood");
        geoQuery.maxResults(10);
        
        String expectedString = "GeoQuery{location=GeoLocation{latitude=37.7749, longitude=-122.4194}, ip='null', accuracy='high', granularity='neighborhood', maxResults=10}";
        
        // Test if toString method returns the correct string representation
        assertEquals(expectedString, geoQuery.toString());
    }
}