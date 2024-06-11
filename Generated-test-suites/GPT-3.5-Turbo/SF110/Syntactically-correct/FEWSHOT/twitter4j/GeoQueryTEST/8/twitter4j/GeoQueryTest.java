package twitter4j;

import static org.junit.Assert.*;

import org.junit.Test;

public class GeoQueryTest {
    
    @Test
    public void testGetLocation() {
        // Create a GeoLocation object
        GeoLocation location = new GeoLocation(1.23, 4.56);
        
        // Create a GeoQuery object with the GeoLocation
        GeoQuery geoQuery = new GeoQuery(location);
        
        // Ensure the getLocation method returns the GeoLocation object
        assertEquals(location, geoQuery.getLocation());
    }
    
    @Test
    public void testGetIp() {
        // Create a GeoQuery object with an IP address
        GeoQuery geoQuery = new GeoQuery("127.0.0.1");
        
        // Ensure the getIp method returns the IP address
        assertEquals("127.0.0.1", geoQuery.getIp());
    }
    
    @Test
    public void testGetAccuracy() {
        // Create a GeoQuery object
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.23, 4.56));
        
        // Set the accuracy
        geoQuery.setAccuracy("low");
        
        // Ensure the getAccuracy method returns the set accuracy
        assertEquals("low", geoQuery.getAccuracy());
    }
    
    @Test
    public void testSetAccuracy() {
        // Create a GeoQuery object
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.23, 4.56));
        
        // Set the accuracy
        geoQuery.setAccuracy("high");
        
        // Ensure the getAccuracy method returns the set accuracy
        assertEquals("high", geoQuery.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        // Create a GeoQuery object
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.23, 4.56));
        
        // Set the granularity
        geoQuery.setGranularity("city");
        
        // Ensure the getGranularity method returns the set granularity
        assertEquals("city", geoQuery.getGranularity());
    }
    
    @Test
    public void testSetGranularity() {
        // Create a GeoQuery object
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.23, 4.56));
        
        // Set the granularity
        geoQuery.setGranularity("neighborhood");
        
        // Ensure the getGranularity method returns the set granularity
        assertEquals("neighborhood", geoQuery.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        // Create a GeoQuery object
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.23, 4.56));
        
        // Set the max results
        geoQuery.setMaxResults(10);
        
        // Ensure the getMaxResults method returns the set max results
        assertEquals(10, geoQuery.getMaxResults());
    }
    
    @Test
    public void testSetMaxResults() {
        // Create a GeoQuery object
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.23, 4.56));
        
        // Set the max results
        geoQuery.setMaxResults(5);
        
        // Ensure the getMaxResults method returns the set max results
        assertEquals(5, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        // Create a GeoQuery object
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.23, 4.56));
        
        // Set the accuracy, granularity, and max results
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(10);
        
        // Convert to HttpParameter array
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        
        // Ensure the HttpParameter array contains the correct parameters
        assertEquals(5, params.length);
        assertEquals("lat", params[0].getName());
        assertEquals("1.23", params[0].getValue());
        assertEquals("long", params[1].getName());
        assertEquals("4.56", params[1].getValue());
        assertEquals("accuracy", params[2].getName());
        assertEquals("high", params[2].getValue());
        assertEquals("granularity", params[3].getName());
        assertEquals("city", params[3].getValue());
        assertEquals("max_results", params[4].getName());
        assertEquals("10", params[4].getValue());
    }
    
    @Test
    public void testEquals() {
        // Create a GeoQuery object with same values
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.23, 4.56));
        geoQuery1.setAccuracy("low");
        geoQuery1.setGranularity("neighborhood");
        geoQuery1.setMaxResults(5);
        
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.23, 4.56));
        geoQuery2.setAccuracy("low");
        geoQuery2.setGranularity("neighborhood");
        geoQuery2.setMaxResults(5);
        
        // Ensure the equals method returns true
        assertTrue(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testEqualsDifferentLocation() {
        // Create a GeoQuery object with different location
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.23, 4.56));
        geoQuery1.setAccuracy("low");
        geoQuery1.setGranularity("neighborhood");
        geoQuery1.setMaxResults(5);
        
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(7.89, 0.12));
        geoQuery2.setAccuracy("low");
        geoQuery2.setGranularity("neighborhood");
        geoQuery2.setMaxResults(5);
        
        // Ensure the equals method returns false
        assertFalse(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testEqualsDifferentAccuracy() {
        // Create a GeoQuery object with different accuracy
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.23, 4.56));
        geoQuery1.setAccuracy("low");
        geoQuery1.setGranularity("neighborhood");
        geoQuery1.setMaxResults(5);
        
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.23, 4.56));
        geoQuery2.setAccuracy("high");
        geoQuery2.setGranularity("neighborhood");
        geoQuery2.setMaxResults(5);
        
        // Ensure the equals method returns false
        assertFalse(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testEqualsDifferentGranularity() {
        // Create a GeoQuery object with different granularity
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.23, 4.56));
        geoQuery1.setAccuracy("low");
        geoQuery1.setGranularity("neighborhood");
        geoQuery1.setMaxResults(5);
        
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.23, 4.56));
        geoQuery2.setAccuracy("low");
        geoQuery2.setGranularity("city");
        geoQuery2.setMaxResults(5);
        
        // Ensure the equals method returns false
        assertFalse(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testEqualsDifferentMaxResults() {
        // Create a GeoQuery object with different max results
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.23, 4.56));
        geoQuery1.setAccuracy("low");
        geoQuery1.setGranularity("neighborhood");
        geoQuery1.setMaxResults(5);
        
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.23, 4.56));
        geoQuery2.setAccuracy("low");
        geoQuery2.setGranularity("neighborhood");
        geoQuery2.setMaxResults(10);
        
        // Ensure the equals method returns false
        assertFalse(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testHashCode() {
        // Create a GeoQuery object with same values
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.23, 4.56));
        geoQuery1.setAccuracy("low");
        geoQuery1.setGranularity("neighborhood");
        geoQuery1.setMaxResults(5);
        
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.23, 4.56));
        geoQuery2.setAccuracy("low");
        geoQuery2.setGranularity("neighborhood");
        geoQuery2.setMaxResults(5);
        
        // Ensure the hashCode method returns the same hashCode
        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }
    
    @Test
    public void testToString() {
        // Create a GeoQuery object
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.23, 4.56));
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(10);
        
        // Ensure the toString method returns the expected string
        assertEquals("GeoQuery{location=GeoLocation{latitude=1.23, longitude=4.56}, ip='null', accuracy='high', granularity='city', maxResults=10}", geoQuery.toString());
    }
}