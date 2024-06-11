import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {
    
    private GeoQuery geoQuery;
    
    @Before
    public void setUp() {
        geoQuery = new GeoQuery(new GeoLocation(40.7128, -74.0060));
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(10);
    }
    
    @Test
    public void testConstructorWithGeoLocation() {
        GeoLocation location = geoQuery.getLocation();
        assertNull(geoQuery.getIp());
        assertNotNull(location);
        assertEquals(40.7128, location.getLatitude(), 0.0001);
        assertEquals(-74.0060, location.getLongitude(), 0.0001);
    }
    
    @Test
    public void testConstructorWithIp() {
        String ip = "123.456.78.90";
        geoQuery = new GeoQuery(ip);
        assertNull(geoQuery.getLocation());
        assertEquals(ip, geoQuery.getIp());
    }
    
    @Test
    public void testGetAccuracy() {
        String accuracy = geoQuery.getAccuracy();
        assertEquals("high", accuracy);
    }
    
    @Test
    public void testSetAccuracy() {
        String accuracy = "low";
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }
    
    @Test
    public void testAccuracyMethod() {
        String accuracy = "medium";
        geoQuery.accuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        String granularity = geoQuery.getGranularity();
        assertEquals("city", granularity);
    }
    
    @Test
    public void testSetGranularity() {
        String granularity = "neighborhood";
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }
    
    @Test
    public void testGranularityMethod() {
        String granularity = "neighborhood";
        geoQuery.granularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        int maxResults = geoQuery.getMaxResults();
        assertEquals(10, maxResults);
    }
    
    @Test
    public void testSetMaxResults() {
        int maxResults = 20;
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }
    
    @Test
    public void testMaxResultsMethod() {
        int maxResults = 30;
        geoQuery.maxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertNotNull(params);
        assertEquals(5, params.length);
        // Verify the values of the parameters, omitting the actual HttpParameter construction as it can be tested separately
    }
    
    @Test
    public void testEquals() {
        GeoQuery sameGeoQuery = new GeoQuery(new GeoLocation(40.7128, -74.0060));
        sameGeoQuery.setAccuracy("high");
        sameGeoQuery.setGranularity("city");
        sameGeoQuery.setMaxResults(10);
        assertTrue(geoQuery.equals(sameGeoQuery));
        
        GeoQuery differentGeoQuery = new GeoQuery("123.456.78.90");
        differentGeoQuery.setAccuracy("low");
        differentGeoQuery.setGranularity("neighborhood");
        differentGeoQuery.setMaxResults(20);
        assertFalse(geoQuery.equals(differentGeoQuery));
    }
    
    @Test
    public void testHashCode() {
        GeoQuery sameGeoQuery = new GeoQuery(new GeoLocation(40.7128, -74.0060));
        sameGeoQuery.setAccuracy("high");
        sameGeoQuery.setGranularity("city");
        sameGeoQuery.setMaxResults(10);
        assertEquals(geoQuery.hashCode(), sameGeoQuery.hashCode());
        
        GeoQuery differentGeoQuery = new GeoQuery("123.456.78.90");
        differentGeoQuery.setAccuracy("low");
        differentGeoQuery.setGranularity("neighborhood");
        differentGeoQuery.setMaxResults(20);
        assertNotEquals(geoQuery.hashCode(), differentGeoQuery.hashCode());
    }
    
    @Test
    public void testToString() {
        String expected = "GeoQuery{location=GeoLocation{latitude=40.7128, longitude=-74.0060}, ip='null', accuracy='high', granularity='city', maxResults=10}";
        assertEquals(expected, geoQuery.toString());
    }
}