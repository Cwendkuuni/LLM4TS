package twitter4j;

import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testGeolocationConstructor() {
        GeoLocation location = new GeoLocation(37.4, -122.1);
        GeoQuery query = new GeoQuery(location);
        assertNotNull(query);
        assertEquals(location, query.getLocation());
        assertNull(query.getIp());
    }

    @Test
    public void testIpConstructor() {
        String ip = "192.168.1.1";
        GeoQuery query = new GeoQuery(ip);
        assertNotNull(query);
        assertNull(query.getLocation());
        assertEquals(ip, query.getIp());
    }

    @Test
    public void testGetAccuracy() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.4, -122.1));
        assertNull(query.getAccuracy());
        query.setAccuracy("high");
        assertEquals("high", query.getAccuracy());
    }

    @Test
    public void testGetGranularity() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.4, -122.1));
        assertNull(query.getGranularity());
        query.setGranularity("city");
        assertEquals("city", query.getGranularity());
    }

    @Test
    public void testGetMaxResults() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.4, -122.1));
        assertEquals(0, query.getMaxResults());
        query.setMaxResults(10);
        assertEquals(10, query.getMaxResults());
    }

    @Test
    public void testAccuracyMethod() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.4, -122.1));
        assertEquals(query, query.accuracy("high"));
        assertEquals("high", query.getAccuracy());
    }

    @Test
    public void testGranularityMethod() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.4, -122.1));
        assertEquals(query, query.granularity("city"));
        assertEquals("city", query.getGranularity());
    }

    @Test
    public void testMaxResultsMethod() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.4, -122.1));
        assertEquals(query, query.maxResults(10));
        assertEquals(10, query.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoQuery query = new GeoQuery(new GeoLocation(37.4, -122.1));
        query.setAccuracy("high");
        query.setGranularity("city");
        query.setMaxResults(10);

        HttpParameter[] expectedParams = {
            new HttpParameter("lat", "37.4"),
            new HttpParameter("long", "-122.1"),
            new HttpParameter("accuracy", "high"),
            new HttpParameter("granularity", "city"),
            new HttpParameter("max_results", "10")
        };

        assertArrayEquals(expectedParams, query.asHttpParameterArray());
    }

    @Test
    public void testEquals() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.4, -122.1));
        GeoQuery query2 = new GeoQuery(new GeoLocation(37.4, -122.1));
        assertEquals(query1, query2);

        GeoQuery query3 = new GeoQuery("192.168.1.1");
        GeoQuery query4 = new GeoQuery("192.168.1.1");
        assertEquals(query3, query4);

        query2.setAccuracy("high");
        assertNotEquals(query1, query2);

        query4.setMaxResults(10);
        assertNotEquals(query3, query4);
    }

    @Test
    public void testHashCode() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.4, -122.1));
        GeoQuery query2 = new GeoQuery(new GeoLocation(37.4, -122.1));
        assertEquals(query1.hashCode(), query2.hashCode());

        GeoQuery query3 = new GeoQuery("192.168.1.1");
        GeoQuery query4 = new GeoQuery("192.168.1.1");
        assertEquals(query3.hashCode(), query4.hashCode());

        query2.setAccuracy("high");
        assertNotEquals(query1.hashCode(), query2.hashCode());

        query4.setMaxResults(10);
        assertNotEquals(query3.hashCode(), query4.hashCode());
    }

    @Test
    public void testToString() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.4, -122.1));
        assertEquals("GeoQuery{location=GeoLocation{latitude=37.4, longitude=-122.1}, ip='null', accuracy='null', granularity='null', maxResults=0}", query1.toString());

        GeoQuery query2 = new GeoQuery("192.168.1.1");
        assertEquals("GeoQuery{location=null, ip='192.168.1.1', accuracy='null', granularity='null', maxResults=0}", query2.toString());

        query1.setAccuracy("high");
        query1.setGranularity("city");
        query1.setMaxResults(10);
        assertEquals("GeoQuery{location=GeoLocation{latitude=37.4, longitude=-122.1}, ip='null', accuracy='high', granularity='city', maxResults=10}", query1.toString());
    }
}