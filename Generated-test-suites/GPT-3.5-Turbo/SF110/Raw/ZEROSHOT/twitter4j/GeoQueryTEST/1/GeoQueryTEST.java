import org.junit.Test;

import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testGeoQueryConstructorWithLocation() {
        // Arrange
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        
        // Act
        GeoQuery geoQuery = new GeoQuery(location);
        
        // Assert
        assertEquals(location, geoQuery.getLocation());
        assertNull(geoQuery.getIp());
    }
    
    @Test
    public void testGeoQueryConstructorWithIP() {
        // Arrange
        String ip = "123.456.789.0";
        
        // Act
        GeoQuery geoQuery = new GeoQuery(ip);
        
        // Assert
        assertNull(geoQuery.getLocation());
        assertEquals(ip, geoQuery.getIp());
    }
    
    @Test
    public void testGetAccuracy() {
        // Arrange
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.setAccuracy("city");
        
        // Act
        String accuracy = geoQuery.getAccuracy();
        
        // Assert
        assertEquals("city", accuracy);
    }
    
    @Test
    public void testSetAccuracy() {
        // Arrange
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        // Act
        geoQuery.setAccuracy("city");
        
        // Assert
        assertEquals("city", geoQuery.getAccuracy());
    }
    
    @Test
    public void testAccuracyBuilder() {
        // Arrange
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        // Act
        GeoQuery result = geoQuery.accuracy("city");
        
        // Assert
        assertEquals(geoQuery, result);
        assertEquals("city", geoQuery.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        // Arrange
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.setGranularity("neighborhood");
        
        // Act
        String granularity = geoQuery.getGranularity();
        
        // Assert
        assertEquals("neighborhood", granularity);
    }
    
    @Test
    public void testSetGranularity() {
        // Arrange
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        // Act
        geoQuery.setGranularity("neighborhood");
        
        // Assert
        assertEquals("neighborhood", geoQuery.getGranularity());
    }
    
    @Test
    public void testGranularityBuilder() {
        // Arrange
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        // Act
        GeoQuery result = geoQuery.granularity("neighborhood");
        
        // Assert
        assertEquals(geoQuery, result);
        assertEquals("neighborhood", geoQuery.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        // Arrange
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.setMaxResults(100);
        
        // Act
        int maxResults = geoQuery.getMaxResults();
        
        // Assert
        assertEquals(100, maxResults);
    }
    
    @Test
    public void testSetMaxResults() {
        // Arrange
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        // Act
        geoQuery.setMaxResults(100);
        
        // Assert
        assertEquals(100, geoQuery.getMaxResults());
    }
    
    @Test
    public void testMaxResultsBuilder() {
        // Arrange
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        // Act
        GeoQuery result = geoQuery.maxResults(100);
        
        // Assert
        assertEquals(geoQuery, result);
        assertEquals(100, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArrayWithLocation() {
        // Arrange
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("city");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(100);
        
        // Act
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        
        // Assert
        assertEquals(4, params.length);
        assertEquals(new HttpParameter("lat", "37.7749"), params[0]);
        assertEquals(new HttpParameter("long", "-122.4194"), params[1]);
        assertEquals(new HttpParameter("accuracy", "city"), params[2]);
        assertEquals(new HttpParameter("granularity", "neighborhood"), params[3]);
    }
    
    @Test
    public void testAsHttpParameterArrayWithIP() {
        // Arrange
        String ip = "123.456.789.0";
        GeoQuery geoQuery = new GeoQuery(ip);
        geoQuery.setAccuracy("city");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(100);
        
        // Act
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        
        // Assert
        assertEquals(3, params.length);
        assertEquals(new HttpParameter("ip", ip), params[0]);
        assertEquals(new HttpParameter("accuracy", "city"), params[1]);
        assertEquals(new HttpParameter("granularity", "neighborhood"), params[2]);
    }
    
    @Test
    public void testEqualsWithSameObject() {
        // Arrange
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        // Act
        boolean result = geoQuery.equals(geoQuery);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testEqualsWithNullObject() {
        // Arrange
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        
        // Act
        boolean result = geoQuery.equals(null);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testEqualsWithDifferentClassObject() {
        // Arrange
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        Object obj = new Object();
        
        // Act
        boolean result = geoQuery.equals(obj);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testEqualsWithDifferentMaxResults() {
        // Arrange
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery1.setMaxResults(100);
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery2.setMaxResults(200);
        
        // Act
        boolean result = geoQuery1.equals(geoQuery2);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testEqualsWithDifferentAccuracy() {
        // Arrange
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery1.setAccuracy("city");
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery2.setAccuracy("neighborhood");
        
        // Act
        boolean result = geoQuery1.equals(geoQuery2);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testEqualsWithDifferentGranularity() {
        // Arrange
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery1.setGranularity("neighborhood");
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery2.setGranularity("city");
        
        // Act
        boolean result = geoQuery1.equals(geoQuery2);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testEqualsWithDifferentIP() {
        // Arrange
        GeoQuery geoQuery1 = new GeoQuery("123.456.789.0");
        GeoQuery geoQuery2 = new GeoQuery("987.654.321.0");
        
        // Act
        boolean result = geoQuery1.equals(geoQuery2);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testEqualsWithDifferentLocation() {
        // Arrange
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(38.9072, -77.0369));
        
        // Act
        boolean result = geoQuery1.equals(geoQuery2);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testEqualsWithSameProperties() {
        // Arrange
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery1.setAccuracy("city");
        geoQuery1.setGranularity("neighborhood");
        geoQuery1.setMaxResults(100);
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery2.setAccuracy("city");
        geoQuery2.setGranularity("neighborhood");
        geoQuery2.setMaxResults(100);
        
        // Act
        boolean result = geoQuery1.equals(geoQuery2);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testHashCode() {
        // Arrange
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.setAccuracy("city");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(100);
        
        // Act
        int hashCode = geoQuery.hashCode();
        
        // Assert
        assertEquals(528899369, hashCode);
    }
    
    @Test
    public void testToString() {
        // Arrange
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.setAccuracy("city");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(100);
        
        // Act
        String toString = geoQuery.toString();
        
        // Assert
        assertEquals("GeoQuery{location=GeoLocation{latitude=37.7749, longitude=-122.4194}, ip='null', accuracy='city', granularity='neighborhood', maxResults=100}", toString);
    }
}