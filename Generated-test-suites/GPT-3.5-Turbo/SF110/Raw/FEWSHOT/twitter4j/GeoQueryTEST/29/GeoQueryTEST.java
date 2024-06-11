package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

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
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        assertNull(geoQuery.getLocation());
        assertEquals(ip, geoQuery.getIp());
    }

    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));

        assertNull(geoQuery.getAccuracy());

        String accuracy = "neighborhood";
        geoQuery.setAccuracy(accuracy);

        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));

        assertNull(geoQuery.getGranularity());

        String granularity = "city";
        geoQuery.setGranularity(granularity);

        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));

        assertEquals(0, geoQuery.getMaxResults());

        int maxResults = 10;
        geoQuery.setMaxResults(maxResults);

        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);

        HttpParameter[] params = geoQuery.asHttpParameterArray();

        assertEquals(2, params.length);
        assertEquals("lat", params[0].getName());
        assertEquals(String.valueOf(location.getLatitude()), params[0].getValue());
        assertEquals("long", params[1].getName());
        assertEquals(String.valueOf(location.getLongitude()), params[1].getValue());
    }

    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));

        assertEquals(geoQuery1, geoQuery2);

        geoQuery1.setAccuracy("neighborhood");
        geoQuery2.setAccuracy("city");

        assertEquals(geoQuery1, geoQuery2);

        geoQuery1.setGranularity("city");
        geoQuery2.setGranularity("country");

        assertEquals(geoQuery1, geoQuery2);
    }

    @Test
    public void testHashCode() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));

        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());

        geoQuery1.setAccuracy("neighborhood");
        geoQuery2.setAccuracy("city");

        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());

        geoQuery1.setGranularity("city");
        geoQuery2.setGranularity("country");

        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        String expectedToString = "GeoQuery{location=" + location + ", ip='" + null + '\'' + ", accuracy='" + null + '\'' + ", granularity='" + null + '\'' + ", maxResults=" + 0 + '}';

        assertEquals(expectedToString, geoQuery.toString());
    }
}