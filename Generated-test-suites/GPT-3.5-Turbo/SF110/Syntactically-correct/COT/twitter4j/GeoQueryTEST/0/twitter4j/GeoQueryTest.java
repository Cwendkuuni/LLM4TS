
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {
    private GeoQuery geoQuery;
    private GeoLocation location;
    
    @Before
    public void setUp() {
        location = new GeoLocation(37.7749, -122.4194);
        geoQuery = new GeoQuery(location);
    }
    
    @Test
    public void testGetLocation() {
        assertEquals(location, geoQuery.getLocation());
    }
    
    @Test
    public void testGetIp() {
        assertNull(geoQuery.getIp());
    }
    
    @Test
    public void testGetAccuracy() {
        assertNull(geoQuery.getAccuracy());
    }
    
    @Test
    public void testSetAccuracy() {
        String accuracy = "high";
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }
    
    @Test
    public void testAccuracy() {
        String accuracy = "low";
        GeoQuery result = geoQuery.accuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
        assertEquals(geoQuery, result);
    }
    
    @Test
    public void testGetGranularity() {
        assertNull(geoQuery.getGranularity());
    }
    
    @Test
    public void testSetGranularity() {
        String granularity = "neighborhood";
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }
    
    @Test
    public void testGranularity() {
        String granularity = "city";
        GeoQuery result = geoQuery.granularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
        assertEquals(geoQuery, result);
    }
    
    @Test
    public void testGetMaxResults() {
        assertEquals(0, geoQuery.getMaxResults());
    }
    
    @Test
    public void testSetMaxResults() {
        int maxResults = 10;
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }
    
    @Test
    public void testMaxResults() {
        int maxResults = 5;
        GeoQuery result = geoQuery.maxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
        assertEquals(geoQuery, result);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        HttpParameter[] expectedParams = {
            new HttpParameter("lat", "37.7749"),
            new HttpParameter("long", "-122.4194"),
            new HttpParameter("accuracy", null),
            new HttpParameter("granularity", null),
            new HttpParameter("max_results", 0)
        };
        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }
}
