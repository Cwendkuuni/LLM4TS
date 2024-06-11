package twitter4j;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testGeoQueryConstructorWithGeoLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(location, geoQuery.getLocation());
    }
    
    @Test
    public void testGeoQueryConstructorWithIp() {
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");
        assertEquals("192.168.1.1", geoQuery.getIp());
    }
    
    @Test
    public void testAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.accuracy("high");
        assertEquals("high", geoQuery.getAccuracy());
    }
    
    @Test
    public void testGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.granularity("city");
        assertEquals("city", geoQuery.getGranularity());
    }
    
    @Test
    public void testMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.maxResults(10);
        assertEquals(10, geoQuery.getMaxResults());
    }

    // Add more test cases for other methods and edge cases here

}