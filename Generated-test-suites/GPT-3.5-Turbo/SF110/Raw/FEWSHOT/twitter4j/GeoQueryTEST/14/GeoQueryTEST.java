package twitter4j;

import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(123.456, 789.012);
        GeoQuery geoQuery = new GeoQuery(location);

        assertEquals(location, geoQuery.getLocation());
    }

    @Test
    public void testGetIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        assertEquals(ip, geoQuery.getIp());
    }

    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(123.456, 789.012));

        assertNull(geoQuery.getAccuracy());

        String accuracy = "high";
        geoQuery.setAccuracy(accuracy);

        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(123.456, 789.012));

        assertNull(geoQuery.getGranularity());

        String granularity = "neighborhood";
        geoQuery.setGranularity(granularity);

        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(123.456, 789.012));

        assertEquals(0, geoQuery.getMaxResults());

        int maxResults = 10;
        geoQuery.setMaxResults(maxResults);

        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(123.456, 789.012));

        String accuracy = "high";
        GeoQuery newGeoQuery = geoQuery.accuracy(accuracy);

        assertEquals(accuracy, newGeoQuery.getAccuracy());
    }

    @Test
    public void testGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(123.456, 789.012));

        String granularity = "neighborhood";
        GeoQuery newGeoQuery = geoQuery.granularity(granularity);

        assertEquals(granularity, newGeoQuery.getGranularity());
    }

    @Test
    public void testMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(123.456, 789.012));

        int maxResults = 10;
        GeoQuery newGeoQuery = geoQuery.maxResults(maxResults);

        assertEquals(maxResults, newGeoQuery.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(123.456, 789.012);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(10);

        HttpParameter[] expectedParams = {
                new HttpParameter("lat", "123.456"),
                new HttpParameter("long", "789.012"),
                new HttpParameter("accuracy", "high"),
                new HttpParameter("granularity", "neighborhood"),
                new HttpParameter("max_results", "10")
        };

        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(123.456, 789.012);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        geoQuery1.setAccuracy("high");
        geoQuery1.setGranularity("neighborhood");
        geoQuery1.setMaxResults(10);

        GeoLocation location2 = new GeoLocation(123.456, 789.012);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        geoQuery2.setAccuracy("high");
        geoQuery2.setGranularity("neighborhood");
        geoQuery2.setMaxResults(10);

        assertEquals(geoQuery1, geoQuery2);
    }

    @Test
    public void testHashCode() {
        GeoLocation location1 = new GeoLocation(123.456, 789.012);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        geoQuery1.setAccuracy("high");
        geoQuery1.setGranularity("neighborhood");
        geoQuery1.setMaxResults(10);

        GeoLocation location2 = new GeoLocation(123.456, 789.012);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        geoQuery2.setAccuracy("high");
        geoQuery2.setGranularity("neighborhood");
        geoQuery2.setMaxResults(10);

        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(123.456, 789.012);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(10);

        String expectedString = "GeoQuery{location=" + location + ", ip='null', accuracy='high', granularity='neighborhood', maxResults=10}";

        assertEquals(expectedString, geoQuery.toString());
    }
}