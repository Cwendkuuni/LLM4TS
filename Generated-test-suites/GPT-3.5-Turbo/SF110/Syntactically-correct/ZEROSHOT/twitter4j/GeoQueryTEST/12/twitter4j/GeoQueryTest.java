package twitter4j;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import twitter4j.internal.http.*;
import java.util.*;

public class GeoQueryTest {

    private GeoLocation location;
    private String ip;
    private GeoQuery geoQuery;

    @Before
    public void setUp() {
        location = new GeoLocation(1.23, 4.56);
        ip = "192.168.0.1";
        geoQuery = new GeoQuery(location);
    }

    @Test
    public void getLocationTest() {
        assertEquals(location, geoQuery.getLocation());
    }

    @Test
    public void getIpTest() {
        assertNull(geoQuery.getIp());
        geoQuery = new GeoQuery(ip);
        assertEquals(ip, geoQuery.getIp());
    }

    @Test
    public void getAccuracyTest() {
        assertNull(geoQuery.getAccuracy());
        String accuracy = "high";
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void setAccuracyTest() {
        String accuracy = "high";
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void accuracyTest() {
        String accuracy = "high";
        assertEquals(geoQuery, geoQuery.accuracy(accuracy));
        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void getGranularityTest() {
        assertNull(geoQuery.getGranularity());
        String granularity = "neighborhood";
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void setGranularityTest() {
        String granularity = "neighborhood";
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void granularityTest() {
        String granularity = "neighborhood";
        assertEquals(geoQuery, geoQuery.granularity(granularity));
        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void getMaxResultsTest() {
        assertEquals(0, geoQuery.getMaxResults());
        int maxResults = 100;
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void setMaxResultsTest() {
        int maxResults = 100;
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void maxResultsTest() {
        int maxResults = 100;
        assertEquals(geoQuery, geoQuery.maxResults(maxResults));
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void asHttpParameterArrayTest() {
        HttpParameter[] expectedParams = {
            new HttpParameter("lat", "1.23"),
            new HttpParameter("long", "4.56"),
            new HttpParameter("accuracy", null),
            new HttpParameter("granularity", null),
            new HttpParameter("max_results", 0)
        };

        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }

    @Test
    public void equalsTest() {
        GeoQuery sameGeoQuery = new GeoQuery(location);
        sameGeoQuery.setAccuracy(null);
        sameGeoQuery.setGranularity(null);
        sameGeoQuery.setMaxResults(0);

        assertEquals(geoQuery, sameGeoQuery);

        GeoQuery differentGeoQuery = new GeoQuery("127.0.0.1");
        assertNotEquals(geoQuery, differentGeoQuery);

        differentGeoQuery = new GeoQuery(location);
        differentGeoQuery.setAccuracy("medium");
        assertNotEquals(geoQuery, differentGeoQuery);

        differentGeoQuery = new GeoQuery(location);
        differentGeoQuery.setGranularity("city");
        assertNotEquals(geoQuery, differentGeoQuery);

        differentGeoQuery = new GeoQuery(location);
        differentGeoQuery.setMaxResults(10);
        assertNotEquals(geoQuery, differentGeoQuery);
    }

    @Test
    public void hashCodeTest() {
        int expectedHashCode = (location != null) ? location.hashCode() : 0;
        expectedHashCode = 31 * expectedHashCode + ((ip != null) ? ip.hashCode() : 0);
        expectedHashCode = 31 * expectedHashCode + ((geoQuery.getAccuracy() != null) ? geoQuery.getAccuracy().hashCode() : 0);
        expectedHashCode = 31 * expectedHashCode + ((geoQuery.getGranularity() != null) ? geoQuery.getGranularity().hashCode() : 0);
        expectedHashCode = 31 * expectedHashCode + geoQuery.getMaxResults();

        assertEquals(expectedHashCode, geoQuery.hashCode());
    }

    @Test
    public void toStringTest() {
        String expectedString = "GeoQuery{location=" + location + ", ip='" + null + '\'' + ", accuracy='" + null + '\'' + ", granularity='" + null + '\'' + ", maxResults=" + 0 + '}';
        assertEquals(expectedString, geoQuery.toString());
    }
}