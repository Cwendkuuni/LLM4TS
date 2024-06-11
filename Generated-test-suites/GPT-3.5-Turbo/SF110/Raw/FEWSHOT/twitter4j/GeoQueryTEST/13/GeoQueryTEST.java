package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GeoQueryTest {
    @Test
    public void testConstructorWithLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery query = new GeoQuery(location);
        assertEquals(location, query.getLocation());
        assertNull(query.getIp());
        assertNull(query.getAccuracy());
        assertNull(query.getGranularity());
        assertEquals(0, query.getMaxResults());
    }

    @Test
    public void testConstructorWithIp() {
        String ip = "192.168.1.1";
        GeoQuery query = new GeoQuery(ip);
        assertNull(query.getLocation());
        assertEquals(ip, query.getIp());
        assertNull(query.getAccuracy());
        assertNull(query.getGranularity());
        assertEquals(0, query.getMaxResults());
    }

    @Test
    public void testSetAccuracy() {
        GeoQuery query = new GeoQuery("192.168.1.1");
        String accuracy = "100m";
        query.setAccuracy(accuracy);
        assertEquals(accuracy, query.getAccuracy());
    }

    @Test
    public void testAccuracyMethod() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        String accuracy = "100m";
        GeoQuery returnQuery = query.accuracy(accuracy);
        assertNotNull(returnQuery);
        assertEquals(accuracy, query.getAccuracy());
    }

    @Test
    public void testSetGranularity() {
        GeoQuery query = new GeoQuery("192.168.1.1");
        String granularity = "city";
        query.setGranularity(granularity);
        assertEquals(granularity, query.getGranularity());
    }

    @Test
    public void testGranularityMethod() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        String granularity = "neighborhood";
        GeoQuery returnQuery = query.granularity(granularity);
        assertNotNull(returnQuery);
        assertEquals(granularity, query.getGranularity());
    }

    @Test
    public void testSetMaxResults() {
        GeoQuery query = new GeoQuery("192.168.1.1");
        int maxResults = 100;
        query.setMaxResults(maxResults);
        assertEquals(maxResults, query.getMaxResults());
    }

    @Test
    public void testMaxResultsMethod() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        int maxResults = 100;
        GeoQuery returnQuery = query.maxResults(maxResults);
        assertNotNull(returnQuery);
        assertEquals(maxResults, query.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArrayWithLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery query = new GeoQuery(location);
        HttpParameter[] parameters = query.asHttpParameterArray();
        assertNotNull(parameters);
        assertEquals(2, parameters.length);
        assertEquals("lat", parameters[0].getName());
        assertEquals(String.valueOf(location.getLatitude()), parameters[0].getValue());
        assertEquals("long", parameters[1].getName());
        assertEquals(String.valueOf(location.getLongitude()), parameters[1].getValue());
    }

    @Test
    public void testAsHttpParameterArrayWithIp() {
        String ip = "192.168.1.1";
        GeoQuery query = new GeoQuery(ip);
        HttpParameter[] parameters = query.asHttpParameterArray();
        assertNotNull(parameters);
        assertEquals(1, parameters.length);
        assertEquals("ip", parameters[0].getName());
        assertEquals(ip, parameters[0].getValue());
    }

    @Test
    public void testEquals() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery query2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertEquals(query1, query2);
    }

    @Test
    public void testHashCode() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery query2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertEquals(query1.hashCode(), query2.hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery query = new GeoQuery(location);
        String expected = "GeoQuery{location=" + location + ", ip='null', accuracy='null', granularity='null', maxResults=0}";
        assertEquals(expected, query.toString());
    }
}