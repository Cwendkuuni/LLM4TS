
import static org.junit.Assert.*;
import org.junit.Test;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
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
        GeoQuery query = new GeoQuery("192.168.0.1");
        query.setAccuracy("low");

        assertEquals("low", query.getAccuracy());
    }

    @Test
    public void testGetGranularity() {
        GeoQuery query = new GeoQuery("192.168.0.1");
        query.setGranularity("city");

        assertEquals("city", query.getGranularity());
    }

    @Test
    public void testGetMaxResults() {
        GeoQuery query = new GeoQuery("192.168.0.1");
        query.setMaxResults(10);

        assertEquals(10, query.getMaxResults());
    }

    @Test
    public void testAccuracy() {
        GeoQuery query = new GeoQuery("192.168.0.1");
        GeoQuery result = query.accuracy("low");

        assertEquals("low", query.getAccuracy());
        assertEquals(query, result);
    }

    @Test
    public void testGranularity() {
        GeoQuery query = new GeoQuery("192.168.0.1");
        GeoQuery result = query.granularity("city");

        assertEquals("city", query.getGranularity());
        assertEquals(query, result);
    }

    @Test
    public void testMaxResults() {
        GeoQuery query = new GeoQuery("192.168.0.1");
        GeoQuery result = query.maxResults(10);

        assertEquals(10, query.getMaxResults());
        assertEquals(query, result);
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery query = new GeoQuery(location);
        query.setAccuracy("low");
        query.setGranularity("city");
        query.setMaxResults(10);

        HttpParameter[] parameters = query.asHttpParameterArray();
        assertEquals(6, parameters.length);
    }

    // Additional test cases for potential edge cases and exceptions:

    @Test
    public void testSetAccuracyNull() {
        GeoQuery query = new GeoQuery("192.168.0.1");
        query.setAccuracy(null);

        assertNull(query.getAccuracy());
    }

    @Test
    public void testSetGranularityNull() {
        GeoQuery query = new GeoQuery("192.168.0.1");
        query.setGranularity(null);

        assertNull(query.getGranularity());
    }

    @Test
    public void testSetMaxResultsZero() {
        GeoQuery query = new GeoQuery("192.168.0.1");
        query.setMaxResults(0);

        assertEquals(0, query.getMaxResults());
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(37.7749, -122.4194);
        GeoLocation location2 = new GeoLocation(37.7749, -122.4194);
        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location1);
        GeoQuery query3 = new GeoQuery(location2);

        assertTrue(query1.equals(query2));
        assertFalse(query1.equals(query3));
    }

    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery query = new GeoQuery(location);
        query.setAccuracy("low");
        query.setGranularity("city");
        query.setMaxResults(10);

        int hashCode = query.hashCode();
        assertEquals(409067657, hashCode);
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery query = new GeoQuery(location);
        query.setAccuracy("low");
        query.setGranularity("city");
        query.setMaxResults(10);

        String str = "GeoQuery{location=" + location + ", ip='null', accuracy='low', granularity='city', maxResults=10}";
        assertEquals(str, query.toString());
    }
}

