import org.junit.Before;
import org.junit.Test;
import twitter4j.GeoQuery;
import twitter4j.GeoLocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GeoQueryTest {
    
    private GeoQuery geoQuery;
    
    @Before
    public void setUp() {
        GeoLocation location = new GeoLocation(0.0, 0.0);
        geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(10);
    }
    
    @Test
    public void testGetLocation() {
        GeoLocation location = geoQuery.getLocation();
        assertEquals(0.0, location.getLatitude(), 0.001);
        assertEquals(0.0, location.getLongitude(), 0.001);
    }
    
    @Test
    public void testGetIp() {
        String ip = geoQuery.getIp();
        assertNull(ip);
    }
    
    @Test
    public void testGetAccuracy() {
        String accuracy = geoQuery.getAccuracy();
        assertEquals("high", accuracy);
    }
    
    @Test
    public void testSetAccuracy() {
        geoQuery.setAccuracy("low");
        String accuracy = geoQuery.getAccuracy();
        assertEquals("low", accuracy);
    }
    
    @Test
    public void testAccuracy() {
        GeoQuery query = geoQuery.accuracy("low");
        String accuracy = query.getAccuracy();
        assertEquals("low", accuracy);
    }
    
    @Test
    public void testGetGranularity() {
        String granularity = geoQuery.getGranularity();
        assertEquals("neighborhood", granularity);
    }
    
    @Test
    public void testSetGranularity() {
        geoQuery.setGranularity("city");
        String granularity = geoQuery.getGranularity();
        assertEquals("city", granularity);
    }
    
    @Test
    public void testGranularity() {
        GeoQuery query = geoQuery.granularity("city");
        String granularity = query.getGranularity();
        assertEquals("city", granularity);
    }
    
    @Test
    public void testGetMaxResults() {
        int maxResults = geoQuery.getMaxResults();
        assertEquals(10, maxResults);
    }
    
    @Test
    public void testSetMaxResults() {
        geoQuery.setMaxResults(5);
        int maxResults = geoQuery.getMaxResults();
        assertEquals(5, maxResults);
    }
    
    @Test
    public void testMaxResults() {
        GeoQuery query = geoQuery.maxResults(5);
        int maxResults = query.getMaxResults();
        assertEquals(5, maxResults);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        // TODO: Add test case for asHttpParameterArray() method
    }
    
    @Test
    public void testEquals() {
        GeoLocation location = new GeoLocation(0.0, 0.0);
        GeoQuery query1 = new GeoQuery(location);
        query1.setAccuracy("high");
        query1.setGranularity("neighborhood");
        query1.setMaxResults(10);
        
        GeoQuery query2 = new GeoQuery(location);
        query2.setAccuracy("high");
        query2.setGranularity("neighborhood");
        query2.setMaxResults(10);
        
        assertEquals(query1, query2);
    }
    
    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(0.0, 0.0);
        GeoQuery query1 = new GeoQuery(location);
        query1.setAccuracy("high");
        query1.setGranularity("neighborhood");
        query1.setMaxResults(10);
        
        GeoQuery query2 = new GeoQuery(location);
        query2.setAccuracy("high");
        query2.setGranularity("neighborhood");
        query2.setMaxResults(10);
        
        assertEquals(query1.hashCode(), query2.hashCode());
    }
    
    @Test
    public void testToString() {
        String expectedString = "GeoQuery{location=GeoLocation{latitude=0.0, longitude=0.0}, ip='null', accuracy='high', granularity='neighborhood', maxResults=10}";
        String actualString = geoQuery.toString();
        assertEquals(expectedString, actualString);
    }
}