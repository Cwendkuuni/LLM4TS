package twitter4j;

import java.io.*;
import twitter4j.internal.http.*;
import java.util.*;

public final class GeoQueryTest {

    @Before
    public void setUp() {
        geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
    }

    @Test
    public void testGetLocation() {
        assertEquals(new GeoLocation(37.7749, -122.4194), geoQuery.getLocation());
    }

    @Test
    public void testGetIp() {
        assertNull(geoQuery.getIp());
    }

    @Test
    public void testGetAccuracy() {
        assertNull(geoQuery.getAccuracy());
    }

    @Test
    public void testSetAccuracy() {
        geoQuery.setAccuracy("high");
        assertEquals("high", geoQuery.getAccuracy());
    }

    @Test
    public void testAccuracy() {
        GeoQuery newGeoQuery = geoQuery.accuracy("high");
        assertEquals("high", newGeoQuery.getAccuracy());
    }

    @Test
    public void testGetGranularity() {
        assertNull(geoQuery.getGranularity());
    }

    @Test
    public void testSetGranularity() {
        geoQuery.setGranularity("neighborhood");
        assertEquals("neighborhood", geoQuery.getGranularity());
    }

    @Test
    public void testGranularity() {
        GeoQuery newGeoQuery = geoQuery.granularity("neighborhood");
        assertEquals("neighborhood", newGeoQuery.getGranularity());
    }

    @Test
    public void testGetMaxResults() {
        assertEquals(0, geoQuery.getMaxResults());
    }

    @Test
    public void testSetMaxResults() {
        geoQuery.setMaxResults(10);
        assertEquals(10, geoQuery.getMaxResults());
    }

    @Test
    public void testMaxResults() {
        GeoQuery newGeoQuery = geoQuery.maxResults(10);
        assertEquals(10, newGeoQuery.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArray() {
        HttpParameter[] expectedParams = {
                new HttpParameter("lat", "37.7749"),
                new HttpParameter("long", "-122.4194")
        };
        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }

    @Test
    public void testEquals() {
        GeoQuery otherGeoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertTrue(geoQuery.equals(otherGeoQuery));
    }

    @Test
    public void testHashCode() {
        GeoQuery otherGeoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertEquals(geoQuery.hashCode(), otherGeoQuery.hashCode());
    }

    @Test
    public void testToString() {
        String expectedToString = "GeoQuery{location=GeoLocation{latitude=37.7749, longitude=-122.4194}, ip='null', accuracy='null', granularity='null', maxResults=0}";
        assertEquals(expectedToString, geoQuery.toString());
    }
}