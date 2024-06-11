package twitter4j;

import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation geoLocation = new GeoLocation(1.0, 1.0);
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        assertEquals(geoLocation, geoQuery.getLocation());
    }

    @Test
    public void testGetIp() {
        String ip = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        assertEquals(ip, geoQuery.getIp());
    }

    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");
        assertNull(geoQuery.getAccuracy());

        String accuracy = "high";
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");
        assertNull(geoQuery.getGranularity());

        String granularity = "neighborhood";
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");
        assertEquals(0, geoQuery.getMaxResults());

        int maxResults = 10;
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testAccuracy() {
        String accuracy = "high";
        GeoQuery geoQuery = new GeoQuery("192.168.1.1").accuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testGranularity() {
        String granularity = "neighborhood";
        GeoQuery geoQuery = new GeoQuery("192.168.1.1").granularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testMaxResults() {
        int maxResults = 10;
        GeoQuery geoQuery = new GeoQuery("192.168.1.1").maxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0, 1.0);
        GeoLocation location2 = new GeoLocation(1.0, 1.0);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        assertTrue(geoQuery1.equals(geoQuery2));

        geoQuery1.setAccuracy("high");
        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(1.0, 1.0);
        GeoQuery geoQuery = new GeoQuery(location);
        int hashCode = location.hashCode() + 31 * geoQuery.getIp().hashCode();
        assertEquals(hashCode, geoQuery.hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0, 1.0);
        GeoQuery geoQuery = new GeoQuery(location);
        String expected = "GeoQuery{location=" + location + ", ip='null', accuracy='null', granularity='null', maxResults=0}";
        assertEquals(expected, geoQuery.toString());
    }
}