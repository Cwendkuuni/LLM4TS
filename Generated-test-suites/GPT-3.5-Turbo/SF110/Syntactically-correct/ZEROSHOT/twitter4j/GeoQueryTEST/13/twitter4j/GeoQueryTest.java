import org.junit.Test;

import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testConstructorWithGeoLocation() {
        GeoLocation location = new GeoLocation(37.7833, -122.4167);
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(location, geoQuery.getLocation());
        assertNull(geoQuery.getIp());
    }

    @Test
    public void testConstructorWithIp() {
        String ip = "127.0.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        assertEquals(ip, geoQuery.getIp());
        assertNull(geoQuery.getLocation());
    }

    @Test
    public void testSetAndGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        assertNull(geoQuery.getAccuracy());
        String accuracy = "fine";
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testAccuracyMethod() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        assertNull(geoQuery.getAccuracy());
        String accuracy = "fine";
        GeoQuery updatedGeoQuery = geoQuery.accuracy(accuracy);
        assertEquals(accuracy, updatedGeoQuery.getAccuracy());
    }

    @Test
    public void testSetAndGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        assertNull(geoQuery.getGranularity());
        String granularity = "city";
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testGranularityMethod() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        assertNull(geoQuery.getGranularity());
        String granularity = "city";
        GeoQuery updatedGeoQuery = geoQuery.granularity(granularity);
        assertEquals(granularity, updatedGeoQuery.getGranularity());
    }

    @Test
    public void testSetAndgetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        assertEquals(0, geoQuery.getMaxResults());
        int maxResults = 10;
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testMaxResultsMethod() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        assertEquals(0, geoQuery.getMaxResults());
        int maxResults = 10;
        GeoQuery updatedGeoQuery = geoQuery.maxResults(maxResults);
        assertEquals(maxResults, updatedGeoQuery.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(37.7833, -122.4167);
        String accuracy = "fine";
        String granularity = "city";
        int maxResults = 10;
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);

        HttpParameter[] expectedParams = {
                new HttpParameter("lat", "37.7833"),
                new HttpParameter("long", "-122.4167"),
                new HttpParameter("accuracy", accuracy),
                new HttpParameter("granularity", granularity),
                new HttpParameter("max_results", String.valueOf(maxResults))
        };

        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(37.7833, -122.4167);
        GeoLocation location2 = new GeoLocation(37.7833, -122.4167);
        GeoLocation location3 = new GeoLocation(40.7128, -74.0060);

        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        GeoQuery geoQuery3 = new GeoQuery(location3);

        assertEquals(geoQuery1, geoQuery2);
        assertNotEquals(geoQuery1, geoQuery3);
    }

    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(37.7833, -122.4167);
        String ip = "127.0.0.1";
        String accuracy = "fine";
        String granularity = "city";
        int maxResults = 10;
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setIp(ip);
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);

        int expectedHashCode = 31 * (31 * (31 * (31 * (31 * location.hashCode() + Objects.hashCode(ip)) + Objects.hashCode(accuracy)) + Objects.hashCode(granularity)) + maxResults);

        assertEquals(expectedHashCode, geoQuery.hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(37.7833, -122.4167);
        String ip = "127.0.0.1";
        String accuracy = "fine";
        String granularity = "city";
        int maxResults = 10;
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setIp(ip);
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);

        String expectedToString = "GeoQuery{location=" + location + ", ip='" + ip + '\'' + ", accuracy='" + accuracy + '\'' + ", granularity='" + granularity + '\'' + ", maxResults=" + maxResults + '}';

        assertEquals(expectedToString, geoQuery.toString());
    }
}