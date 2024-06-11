package twitter4j;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GeoQueryTest {

    private static GeoLocation location;
    private static String ip;
    private static String accuracy;
    private static String granularity;
    private static int maxResults;

    @BeforeClass
    public static void setup() {
        location = new GeoLocation(30.0, -90.0);
        ip = "192.168.0.1";
        accuracy = "high";
        granularity = GeoQuery.NEIGHBORHOOD;
        maxResults = 10;
    }

    @Test
    public void testGetLocation() {
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(location, geoQuery.getLocation());
    }

    @Test
    public void testGetIp() {
        GeoQuery geoQuery = new GeoQuery(ip);
        assertEquals(ip, geoQuery.getIp());
    }

    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testAccuracy() {
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(geoQuery, geoQuery.accuracy(accuracy));
        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testGranularity() {
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(geoQuery, geoQuery.granularity(granularity));
        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testMaxResults() {
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(geoQuery, geoQuery.maxResults(maxResults));
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);

        HttpParameter[] expectedParams = {
                new HttpParameter("lat", "30.0"),
                new HttpParameter("long", "-90.0"),
                new HttpParameter("accuracy", accuracy),
                new HttpParameter("granularity", granularity),
                new HttpParameter("max_results", String.valueOf(maxResults))
        };

        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }

    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(location);
        geoQuery1.setAccuracy(accuracy);
        geoQuery1.setGranularity(granularity);
        geoQuery1.setMaxResults(maxResults);

        GeoQuery geoQuery2 = new GeoQuery(location);
        geoQuery2.setAccuracy(accuracy);
        geoQuery2.setGranularity(granularity);
        geoQuery2.setMaxResults(maxResults);

        assertTrue(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testHashCode() {
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);

        int expectedHashCode = 31 * 31 * 31 * 31 + location.hashCode();
        expectedHashCode = 31 * expectedHashCode + accuracy.hashCode();
        expectedHashCode = 31 * expectedHashCode + granularity.hashCode();
        expectedHashCode = 31 * expectedHashCode + maxResults;

        assertEquals(expectedHashCode, geoQuery.hashCode());
    }

    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);

        String expectedToString = "GeoQuery{location=" + location + ", ip='null', accuracy='" + accuracy + "', granularity='" + granularity + "', maxResults=" + maxResults + "}";
        assertEquals(expectedToString, geoQuery.toString());
    }
}