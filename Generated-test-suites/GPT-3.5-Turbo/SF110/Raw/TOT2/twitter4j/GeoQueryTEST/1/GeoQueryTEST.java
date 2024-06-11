import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testConstructorWithGeoLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(location, geoQuery.getLocation());
        assertNull(geoQuery.getIp());
    }

    @Test
    public void testConstructorWithIp() {
        String ip = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        assertEquals(ip, geoQuery.getIp());
        assertNull(geoQuery.getLocation());
    }

    @Test
    public void testAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        String accuracy = "1111";
        geoQuery.accuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        String granularity = "city";
        geoQuery.granularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        int maxResults = 10;
        geoQuery.maxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery1.maxResults(5).setAccuracy("1111").granularity("city");
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery2.maxResults(5).setAccuracy("1111").granularity("city");
        assertEquals(geoQuery1, geoQuery2);
    }

    @Test
    public void testHashCode() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.maxResults(5).setAccuracy("1111").granularity("city");
        assertEquals(geoQuery.hashCode(), geoQuery.hashCode());
    }

    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.maxResults(5).setAccuracy("1111").granularity("city");
        String expected = "GeoQuery{location=GeoLocation{latitude=37.7749, longitude=-122.4194}, ip='null', accuracy='1111', granularity='city', maxResults=5}";
        assertEquals(expected, geoQuery.toString());
    }

}