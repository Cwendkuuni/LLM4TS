import org.junit.*;
import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(40.7128, -74.0060);
        GeoQuery query = new GeoQuery(location);

        assertEquals(location, query.getLocation());
    }

    @Test
    public void testGetIp() {
        String ip = "192.168.0.1";
        GeoQuery query = new GeoQuery(ip);

        assertEquals(ip, query.getIp());
    }

    @Test
    public void testGetAccuracy() {
        GeoLocation location = new GeoLocation(40.7128, -74.0060);
        GeoQuery query = new GeoQuery(location);

        assertNull(query.getAccuracy());

        query.setAccuracy("high");

        assertEquals("high", query.getAccuracy());
    }

    @Test
    public void testGetGranularity() {
        GeoLocation location = new GeoLocation(40.7128, -74.0060);
        GeoQuery query = new GeoQuery(location);

        assertNull(query.getGranularity());

        query.setGranularity("neighborhood");

        assertEquals("neighborhood", query.getGranularity());
    }

    @Test
    public void testGetMaxResults() {
        GeoLocation location = new GeoLocation(40.7128, -74.0060);
        GeoQuery query = new GeoQuery(location);

        assertEquals(0, query.getMaxResults());

        query.setMaxResults(10);

        assertEquals(10, query.getMaxResults());
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(40.7128, -74.0060);
        GeoLocation location2 = new GeoLocation(40.7128, -74.0060);
        GeoLocation location3 = new GeoLocation(37.7749, -122.4194);

        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location2);
        GeoQuery query3 = new GeoQuery(location3);

        assertTrue(query1.equals(query2));
        assertFalse(query1.equals(query3));
    }

    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(40.7128, -74.0060);
        GeoQuery query = new GeoQuery(location);

        int expectedHashCode = location.hashCode();
        expectedHashCode = 31 * expectedHashCode;
        expectedHashCode = 31 * expectedHashCode;
        expectedHashCode = 31 * expectedHashCode;
        expectedHashCode = 31 * expectedHashCode + 0;

        assertEquals(expectedHashCode, query.hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(40.7128, -74.0060);
        GeoQuery query = new GeoQuery(location);

        String expectedString = "GeoQuery{location=" + location + ", ip='null', accuracy='null', granularity='null', maxResults=0}";

        assertEquals(expectedString, query.toString());
    }
}