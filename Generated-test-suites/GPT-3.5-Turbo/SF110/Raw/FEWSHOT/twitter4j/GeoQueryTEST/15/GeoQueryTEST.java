import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class GeoQueryTest {
    private GeoQuery geoQuery;

    @Before
    public void setUp() {
        geoQuery = new GeoQuery(new GeoLocation(12.345, 67.890));
    }

    @Test
    public void testConstructorWithLocation() {
        assertNotNull(geoQuery.getLocation());
        assertNull(geoQuery.getIp());
        assertNull(geoQuery.getAccuracy());
        assertNull(geoQuery.getGranularity());
        assertEquals(0, geoQuery.getMaxResults());
    }

    @Test
    public void testConstructorWithIp() {
        geoQuery = new GeoQuery("192.168.0.1");
        assertNull(geoQuery.getLocation());
        assertNotNull(geoQuery.getIp());
        assertNull(geoQuery.getAccuracy());
        assertNull(geoQuery.getGranularity());
        assertEquals(0, geoQuery.getMaxResults());
    }

    @Test
    public void testGettersAndSetters() {
        geoQuery.setAccuracy("high");
        assertEquals("high", geoQuery.getAccuracy());

        geoQuery.setGranularity("city");
        assertEquals("city", geoQuery.getGranularity());

        geoQuery.setMaxResults(10);
        assertEquals(10, geoQuery.getMaxResults());
    }

    @Test
    public void testAccuracy() {
        geoQuery.accuracy("medium");
        assertEquals("medium", geoQuery.getAccuracy());
    }

    @Test
    public void testGranularity() {
        geoQuery.granularity(GeoQuery.CITY);
        assertEquals("city", geoQuery.getGranularity());
    }

    @Test
    public void testMaxResults() {
        geoQuery.maxResults(5);
        assertEquals(5, geoQuery.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArray() {
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertEquals(2, params.length);
        assertEquals("lat", params[0].getName());
        assertEquals("12.345", params[0].getValue());
        assertEquals("long", params[1].getName());
        assertEquals("67.890", params[1].getValue());

        geoQuery = new GeoQuery("192.168.0.1");
        params = geoQuery.asHttpParameterArray();
        assertEquals(1, params.length);
        assertEquals("ip", params[0].getName());
        assertEquals("192.168.0.1", params[0].getValue());
    }

    @Test
    public void testEquals() {
        GeoQuery sameQuery = new GeoQuery(new GeoLocation(12.345, 67.890));
        assertEquals(geoQuery, sameQuery);

        GeoQuery differentQuery = new GeoQuery(new GeoLocation(98.765, 43.210));
        assertNotEquals(geoQuery, differentQuery);
    }

    @Test
    public void testHashCode() {
        GeoQuery sameQuery = new GeoQuery(new GeoLocation(12.345, 67.890));
        assertEquals(geoQuery.hashCode(), sameQuery.hashCode());

        GeoQuery differentQuery = new GeoQuery(new GeoLocation(98.765, 43.210));
        assertNotEquals(geoQuery.hashCode(), differentQuery.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("GeoQuery{location=GeoLocation{latitude=12.345, longitude=67.89}, ip='null', accuracy='null', granularity='null', maxResults=0}", geoQuery.toString());
    }
}