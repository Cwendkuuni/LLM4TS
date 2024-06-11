import org.junit.*;
import static org.junit.Assert.*;

public class GeoQueryTest {

    // Test cases for constructor with GeoLocation parameter
    @Test
    public void testGeoQueryConstructorWithGeoLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(location, geoQuery.getLocation());
        assertNull(geoQuery.getIp());
    }
    
    // Test cases for constructor with IP parameter
    @Test
    public void testGeoQueryConstructorWithIP() {
        String ip = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        assertEquals(ip, geoQuery.getIp());
        assertNull(geoQuery.getLocation());
    }
    
    // Test cases for getAccuracy and setAccuracy methods
    @Test
    public void testGetSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");
        
        // Test typical case
        String accuracy = "high";
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
        
        // Test empty string
        String emptyAccuracy = "";
        geoQuery.setAccuracy(emptyAccuracy);
        assertEquals(emptyAccuracy, geoQuery.getAccuracy());
        
        // Test null value
        String nullValue = null;
        geoQuery.setAccuracy(nullValue);
        assertNull(geoQuery.getAccuracy());
    }
    
    // Test cases for getGranularity and setGranularity methods
    @Test
    public void testGetSetGranularity() {
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");
        
        // Test typical case
        String granularity = "city";
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
        
        // Test empty string
        String emptyGranularity = "";
        geoQuery.setGranularity(emptyGranularity);
        assertEquals(emptyGranularity, geoQuery.getGranularity());
        
        // Test null value
        String nullValue = null;
        geoQuery.setGranularity(nullValue);
        assertNull(geoQuery.getGranularity());
    }
    
    // Test cases for getMaxResults and setMaxResults methods
    @Test
    public void testGetSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        // Test typical case
        int maxResults = 10;
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
        
        // Test zero
        int zeroMaxResults = 0;
        geoQuery.setMaxResults(zeroMaxResults);
        assertEquals(zeroMaxResults, geoQuery.getMaxResults());
        
        // Test negative value
        int negativeMaxResults = -5;
        geoQuery.setMaxResults(negativeMaxResults);
        assertEquals(negativeMaxResults, geoQuery.getMaxResults());
    }
    
    // Test cases for asHttpParameterArray method
    @Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(10);
        
        HttpParameter[] expectedParams = new HttpParameter[5];
        expectedParams[0] = new HttpParameter("lat", "37.7749");
        expectedParams[1] = new HttpParameter("long", "-122.4194");
        expectedParams[2] = new HttpParameter("accuracy", "high");
        expectedParams[3] = new HttpParameter("granularity", "city");
        expectedParams[4] = new HttpParameter("max_results", "10");
        
        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }
    
    // Test cases for appendParameter method with String value
    @Test
    public void testAppendParameterWithStringValue() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        List<HttpParameter> params = new ArrayList<>();
        
        // Test typical case with non-null value
        String name = "accuracy";
        String value = "high";
        geoQuery.appendParameter(name, value, params);
        assertEquals(1, params.size());
        assertEquals(name, params.get(0).getName());
        assertEquals(value, params.get(0).getValue());
        
        // Test empty string value
        String emptyValue = "";
        geoQuery.appendParameter(name, emptyValue, params);
        assertEquals(2, params.size());
        assertEquals(name, params.get(1).getName());
        assertEquals(emptyValue, params.get(1).getValue());
        
        // Test null value
        String nullValue = null;
        geoQuery.appendParameter(name, nullValue, params);
        assertEquals(2, params.size()); // Should not add null value
    }
    
    // Test cases for appendParameter method with int value
    @Test
    public void testAppendParameterWithIntValue() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        List<HttpParameter> params = new ArrayList<>();
        
        // Test typical case with positive value
        String name = "max_results";
        int value = 10;
        geoQuery.appendParameter(name, value, params);
        assertEquals(1, params.size());
        assertEquals(name, params.get(0).getName());
        assertEquals(String.valueOf(value), params.get(0).getValue());
        
        // Test zero value
        int zeroValue = 0;
        geoQuery.appendParameter(name, zeroValue, params);
        assertEquals(1, params.size()); // Should not add zero value
        
        // Test negative value
        int negativeValue = -5;
        geoQuery.appendParameter(name, negativeValue, params);
        assertEquals(1, params.size()); // Should not add negative value
    }
    
    // Test cases for appendParameter method with double value
    @Test
    public void testAppendParameterWithDoubleValue() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        List<HttpParameter> params = new ArrayList<>();
        
        // Test typical case with positive value
        String name = "latitude";
        double value = 37.7749;
        geoQuery.appendParameter(name, value, params);
        assertEquals(1, params.size());
        assertEquals(name, params.get(0).getName());
        assertEquals(String.valueOf(value), params.get(0).getValue());
    }
    
    // Test cases for equals method
    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery("192.168.1.1");
        GeoQuery geoQuery2 = new GeoQuery("192.168.1.1");
        GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery geoQuery4 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        // Test equal objects
        assertTrue(geoQuery1.equals(geoQuery1));
        assertTrue(geoQuery1.equals(geoQuery2));
        assertTrue(geoQuery2.equals(geoQuery1));
        assertTrue(geoQuery3.equals(geoQuery3));
        assertTrue(geoQuery3.equals(geoQuery4));
        assertTrue(geoQuery4.equals(geoQuery3));
        
        // Test different objects
        assertFalse(geoQuery1.equals(null));
        assertFalse(geoQuery1.equals(new Object()));
        assertFalse(geoQuery1.equals("192.168.1.1"));
        assertFalse(geoQuery2.equals(new GeoQuery("192.168.1.2")));
        assertFalse(geoQuery3.equals(new GeoQuery(new GeoLocation(40.7128, -74.0060))));
    }
    
    // Test cases for hashCode method
    @Test
    public void testHashCode() {
        GeoQuery geoQuery1 = new GeoQuery("192.168.1.1");
        GeoQuery geoQuery2 = new GeoQuery("192.168.1.1");
        GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery geoQuery4 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        // Test equal objects
        assertEquals(geoQuery1.hashCode(), geoQuery1.hashCode());
        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
        assertEquals(geoQuery2.hashCode(), geoQuery1.hashCode());
        assertEquals(geoQuery3.hashCode(), geoQuery3.hashCode());
        assertEquals(geoQuery3.hashCode(), geoQuery4.hashCode());
        assertEquals(geoQuery4.hashCode(), geoQuery3.hashCode());
    }
    
    // Test cases for toString method
    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(10);
        
        String expectedString = "GeoQuery{location=" + location + ", ip='null', accuracy='high', granularity='city', maxResults=10}";
        assertEquals(expectedString, geoQuery.toString());
    }
}