package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GeoQueryTest {
    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(location, geoQuery.getLocation());
    }

    @Test
    public void testGetIp() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        assertEquals("192.168.0.1", geoQuery.getIp());
    }

    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertNull(geoQuery.getAccuracy());

        geoQuery.setAccuracy("city");
        assertEquals("city", geoQuery.getAccuracy());
    }

    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertNull(geoQuery.getGranularity());

        geoQuery.setGranularity("neighborhood");
        assertEquals("neighborhood", geoQuery.getGranularity());
    }

    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertEquals(0, geoQuery.getMaxResults());

        geoQuery.setMaxResults(100);
        assertEquals(100, geoQuery.getMaxResults());
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(37.7749, -122.4194);
        GeoLocation location2 = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        assertEquals(geoQuery1, geoQuery2);

        geoQuery1.setAccuracy("city");
        geoQuery2.setAccuracy("city");

        assertEquals(geoQuery1, geoQuery2);

        geoQuery1.setGranularity("neighborhood");
        geoQuery2.setGranularity("neighborhood");

        assertEquals(geoQuery1, geoQuery2);

        geoQuery1.setIp("192.168.0.1");
        geoQuery2.setIp("192.168.0.1");

        assertEquals(geoQuery1, geoQuery2);

        geoQuery1.setMaxResults(100);
        geoQuery2.setMaxResults(100);

        assertEquals(geoQuery1, geoQuery2);
    }

    @Test
    public void testHashCode() {
        GeoLocation location1 = new GeoLocation(37.7749, -122.4194);

        GeoQuery geoQuery1 = new GeoQuery(location1);

        // Default values
        int expectedHashCode = 31 * (31 * (31 * (31 * (location1.hashCode()) + 0) + 0) + 0) + 0;

        assertEquals(expectedHashCode, geoQuery1.hashCode());

        geoQuery1.setAccuracy("city");
        expectedHashCode = 31 * (31 * (31 * (31 * (location1.hashCode()) + 0) + "city".hashCode()) + 0) + 0;
        assertEquals(expectedHashCode, geoQuery1.hashCode());

        geoQuery1.setGranularity("neighborhood");
        expectedHashCode = 31 * (31 * (31 * (31 * (location1.hashCode()) + 0) + "city".hashCode()) + "neighborhood".hashCode()) + 0;
        assertEquals(expectedHashCode, geoQuery1.hashCode());

        geoQuery1.setIp("192.168.0.1");
        expectedHashCode = 31 * (31 * (31 * (31 * (location1.hashCode()) + "192.168.0.1".hashCode()) + "city".hashCode()) + "neighborhood".hashCode()) + 0;
        assertEquals(expectedHashCode, geoQuery1.hashCode());

        geoQuery1.setMaxResults(100);
        expectedHashCode = 31 * (31 * (31 * (31 * (location1.hashCode()) + "192.168.0.1".hashCode()) + "city".hashCode()) + "neighborhood".hashCode()) + 100;
        assertEquals(expectedHashCode, geoQuery1.hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);

        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals("GeoQuery{location=" + location + ", ip='null', accuracy='null', granularity='null', maxResults=0}", geoQuery.toString());

        geoQuery.setAccuracy("city");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setIp("192.168.0.1");
        geoQuery.setMaxResults(100);

        assertEquals("GeoQuery{location=" + location + ", ip='192.168.0.1', accuracy='city', granularity='neighborhood', maxResults=100}", geoQuery.toString());
    }
}