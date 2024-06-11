import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {
    @Test
    public void testConstructorWithGeoLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery query = new GeoQuery(location);

        assertEquals(location, query.getLocation());
        assertNull(query.getIp());
    }

    @Test
    public void testConstructorWithIp() {
        String ip = "192.168.0.1";
        GeoQuery query = new GeoQuery(ip);

        assertNull(query.getLocation());
        assertEquals(ip, query.getIp());
    }

    @Test
    public void testSetAccuracy() {
        GeoQuery query = new GeoQuery((GeoLocation) null);
        String accuracy = "neighborhood";
        query.setAccuracy(accuracy);

        assertEquals(accuracy, query.getAccuracy());
    }

    @Test
    public void testGranularity() {
        GeoQuery query = new GeoQuery((GeoLocation) null);
        String granularity = "city";
        query.setGranularity(granularity);

        assertEquals(granularity, query.getGranularity());
    }

    @Test
    public void testMaxResults() {
        GeoQuery query = new GeoQuery((GeoLocation) null);
        int maxResults = 100;
        query.setMaxResults(maxResults);

        assertEquals(maxResults, query.getMaxResults());
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(37.7749, -122.4194);
        GeoLocation location2 = new GeoLocation(37.7749, -122.4194);

        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location1);
        GeoQuery query3 = new GeoQuery(location2);

        assertEquals(query1, query2);
        assertNotEquals(query1, query3);
    }

    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        String ip = "192.168.0.1";
        String accuracy = "neighborhood";
        String granularity = "city";
        int maxResults = 100;

        GeoQuery query = new GeoQuery(location);
        query.setIp(ip);
        query.setAccuracy(accuracy);
        query.setGranularity(granularity);
        query.setMaxResults(maxResults);

        int expectedHashCode = location.hashCode();
        expectedHashCode = 31 * expectedHashCode + ip.hashCode();
        expectedHashCode = 31 * expectedHashCode + accuracy.hashCode();
        expectedHashCode = 31 * expectedHashCode + granularity.hashCode();
        expectedHashCode = 31 * expectedHashCode + maxResults;

        assertEquals(expectedHashCode, query.hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        String ip = "192.168.0.1";
        String accuracy = "neighborhood";
        String granularity = "city";
        int maxResults = 100;

        GeoQuery query = new GeoQuery(location);
        query.setIp(ip);
        query.setAccuracy(accuracy);
        query.setGranularity(granularity);
        query.setMaxResults(maxResults);

        String expectedToString = "GeoQuery{location=" + location + ", ip='" + ip + '\'' +
                ", accuracy='" + accuracy + '\'' + ", granularity='" + granularity + '\'' +
                ", maxResults=" + maxResults + '}';

        assertEquals(expectedToString, query.toString());
    }
}