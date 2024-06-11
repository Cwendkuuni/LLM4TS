package twitter4j;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GeoQueryTest {

    private GeoQuery geoQuery;

    @Before
    public void setUp() {
        geoQuery = new GeoQuery(new GeoLocation(0, 0));
        geoQuery.setIp("192.168.0.1");
    }

    @Test
    public void testGetLocation() {
        assertEquals(new GeoLocation(0, 0), geoQuery.getLocation());
    }

    @Test
    public void testGetIp() {
        assertEquals("192.168.0.1", geoQuery.getIp());
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
        GeoQuery result = geoQuery.accuracy("high");
        assertEquals("high", result.getAccuracy());
    }

    @Test
    public void testGetGranularity() {
        assertNull(geoQuery.getGranularity());
    }

    @Test
    public void testSetGranularity() {
        geoQuery.setGranularity("city");
        assertEquals("city", geoQuery.getGranularity());
    }

    @Test
    public void testGranularity() {
        GeoQuery result = geoQuery.granularity("city");
        assertEquals("city", result.getGranularity());
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
        GeoQuery result = geoQuery.maxResults(10);
        assertEquals(10, result.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArray() {
        HttpParameter[] expectedParams = new HttpParameter[5];
        expectedParams[0] = new HttpParameter("lat", "0.0");
        expectedParams[1] = new HttpParameter("long", "0.0");
        expectedParams[2] = new HttpParameter("ip", "192.168.0.1");
        expectedParams[3] = null;
        expectedParams[4] = new HttpParameter("max_results", "0");

        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }

    @Test
    public void testEquals() {
        GeoQuery sameGeoQuery = new GeoQuery(new GeoLocation(0, 0));
        sameGeoQuery.setIp("192.168.0.1");
        assertTrue(geoQuery.equals(sameGeoQuery));

        GeoQuery differentGeoQuery = new GeoQuery(new GeoLocation(1, 1));
        differentGeoQuery.setIp("192.168.0.2");
        assertFalse(geoQuery.equals(differentGeoQuery));
    }

    @Test
    public void testHashCode() {
        GeoQuery sameGeoQuery = new GeoQuery(new GeoLocation(0, 0));
        sameGeoQuery.setIp("192.168.0.1");

        assertEquals(geoQuery.hashCode(), sameGeoQuery.hashCode());
    }

    @Test
    public void testToString() {
        String expectedString = "GeoQuery{location=GeoLocation{latitude=0.0, longitude=0.0}, ip='192.168.0.1', accuracy='null', granularity='null', maxResults=0}";
        assertEquals(expectedString, geoQuery.toString());
    }
}