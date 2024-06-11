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
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.setAccuracy("high");

        assertEquals("high", geoQuery.getAccuracy());
    }

    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.setGranularity("city");

        assertEquals("city", geoQuery.getGranularity());
    }

    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.setMaxResults(10);

        assertEquals(10, geoQuery.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(10);

        HttpParameter[] expectedParameters = {
            new HttpParameter("lat", "37.7749"),
            new HttpParameter("long", "-122.4194"),
            new HttpParameter("accuracy", "high"),
            new HttpParameter("granularity", "city"),
            new HttpParameter("max_results", "10")
        };

        assertArrayEquals(expectedParameters, geoQuery.asHttpParameterArray());
    }

    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery1.setAccuracy("high");
        geoQuery1.setGranularity("city");
        geoQuery1.setMaxResults(10);

        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery2.setAccuracy("high");
        geoQuery2.setGranularity("city");
        geoQuery2.setMaxResults(10);

        assertEquals(geoQuery1, geoQuery2);
    }

    @Test
    public void testHashCode() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery1.setAccuracy("high");
        geoQuery1.setGranularity("city");
        geoQuery1.setMaxResults(10);

        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery2.setAccuracy("high");
        geoQuery2.setGranularity("city");
        geoQuery2.setMaxResults(10);

        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }

    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(10);

        String expectedString = "GeoQuery{location=GeoLocation{latitude=37.7749, longitude=-122.4194}, " +
                "ip='null', accuracy='high', granularity='city', maxResults=10}";

        assertEquals(expectedString, geoQuery.toString());
    }
}