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
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");
        assertEquals("192.168.1.1", geoQuery.getIp());
    }

    @Test
    public void testSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");
        geoQuery.setAccuracy("low");
        assertEquals("low", geoQuery.getAccuracy());
    }

    @Test
    public void testGranularity() {
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");
        geoQuery.setGranularity("neighborhood");
        assertEquals("neighborhood", geoQuery.getGranularity());
    }

    @Test
    public void testSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");
        geoQuery.setMaxResults(10);
        assertEquals(10, geoQuery.getMaxResults());
    }

    // Additional edge cases and exception handling scenarios can be added below
}