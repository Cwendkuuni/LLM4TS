package twitter4j;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(location, geoQuery.getLocation());
    }

    @Test
    public void testGetIp() {
        String ip = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        assertEquals(ip, geoQuery.getIp());
    }

    @Test
    public void testSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery("www.example.com");
        geoQuery.setAccuracy("high");
        assertEquals("high", geoQuery.getAccuracy());
    }

    @Test
    public void testSetGranularity() {
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");
        geoQuery.setGranularity("city");
        assertEquals("city", geoQuery.getGranularity());
    }

    @Test
    public void testSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.setMaxResults(10);
        assertEquals(10, geoQuery.getMaxResults());
    }

    // Additional test cases for edge cases and exceptions can be added here

}