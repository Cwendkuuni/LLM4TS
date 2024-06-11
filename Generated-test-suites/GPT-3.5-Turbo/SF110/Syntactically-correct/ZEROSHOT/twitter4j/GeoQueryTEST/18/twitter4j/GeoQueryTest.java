import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);

        Assertions.assertEquals(location, geoQuery.getLocation());
    }

    @Test
    public void testGetIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        Assertions.assertEquals(ip, geoQuery.getIp());
    }

    @Test
    public void testGetAccuracy() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("high");

        Assertions.assertEquals("high", geoQuery.getAccuracy());
    }

    @Test
    public void testSetAccuracy() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("low");

        Assertions.assertEquals("low", geoQuery.getAccuracy());
    }

    @Test
    public void testGetGranularity() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setGranularity("neighborhood");

        Assertions.assertEquals("neighborhood", geoQuery.getGranularity());
    }

    @Test
    public void testSetGranularity() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setGranularity("city");

        Assertions.assertEquals("city", geoQuery.getGranularity());
    }

    @Test
    public void testGetMaxResults() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setMaxResults(10);

        Assertions.assertEquals(10, geoQuery.getMaxResults());
    }

    @Test
    public void testSetMaxResults() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setMaxResults(5);

        Assertions.assertEquals(5, geoQuery.getMaxResults());
    }
}