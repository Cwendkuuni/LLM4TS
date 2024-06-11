
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.GeoLocation;

public class GeoQueryTest {
    private GeoQuery geoQuery;
    private GeoLocation location;
    
    @Before
    public void setUp() {
        location = new GeoLocation(12.34, 56.78);
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
        geoQuery.setAccuracy("high");
        assertEquals("high", geoQuery.getAccuracy());
    }
    
    @Test
    public void testAccuracy() {
        GeoQuery result = geoQuery.accuracy("high");
        assertEquals("high", result.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        assertNull(geoQuery.getGranularity());
    }
    
    @Test
    public void testSetGranularity() {
        geoQuery.setGranularity("city");
        assertEquals("city", geoQuery.getGranularity());
    }
    
    @Test
    public void testGranularity() {
        GeoQuery result = geoQuery.granularity("city");
        assertEquals("city", result.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        assertEquals(0, geoQuery.getMaxResults());
    }
    
    @Test
    public void testSetMaxResults() {
        geoQuery.setMaxResults(50);
        assertEquals(50, geoQuery.getMaxResults());
    }
    
    @Test
    public void testMaxResults() {
        GeoQuery result = geoQuery.maxResults(50);
        assertEquals(50, result.getMaxResults());
    }
}
