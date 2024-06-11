import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import twitter4j.GeoLocation;
import static org.junit.Assert.*;

public class GeoQueryTest {

    private GeoQuery geoQuery;

    @Before
    public void setUp() {
        geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(10);
    }

    @After
    public void tearDown() {
        geoQuery = null;
    }

    @Test
    public void testGeoQueryConstructor1() {
        GeoQuery query = new GeoQuery(new GeoLocation(34.0522, -118.2437));
        assertNull(query.getIp());
        assertNotNull(query.getLocation());
    }

    @Test
    public void testGeoQueryConstructor2() {
        GeoQuery query = new GeoQuery("192.168.0.1");
        assertNull(query.getLocation());
        assertNotNull(query.getIp());
    }

    @Test
    public void testGetLocation() {
        assertEquals(new GeoLocation(37.7749, -122.4194), geoQuery.getLocation());
    }

    @Test
    public void testGetIp() {
        assertEquals("192.168.1.1", new GeoQuery("192.168.1.1").getIp());
    }

    @Test
    public void testGetAccuracy() {
        assertEquals("high", geoQuery.getAccuracy());
    }

    @Test
    public void testSetAccuracy() {
        geoQuery.setAccuracy("low");
        assertEquals("low", geoQuery.getAccuracy());
    }

    @Test
    public void testGetGranularity() {
        assertEquals("neighborhood", geoQuery.getGranularity());
    }

    @Test
    public void testSetGranularity() {
        geoQuery.setGranularity("city");
        assertEquals("city", geoQuery.getGranularity());
    }

    @Test
    public void testGetMaxResults() {
        assertEquals(10, geoQuery.getMaxResults());
    }

    @Test
    public void testSetMaxResults() {
        geoQuery.setMaxResults(5);
        assertEquals(5, geoQuery.getMaxResults());
    }

    @Test
    public void testEquals() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        query1.setAccuracy("high");
        query1.setGranularity("neighborhood");
        query1.setMaxResults(10);

        GeoQuery query2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        query2.setAccuracy("high");
        query2.setGranularity("neighborhood");
        query2.setMaxResults(10);

        assertTrue(query1.equals(query2));
    }

    @Test
    public void testNotEquals() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        query1.setAccuracy("high");
        query1.setGranularity("neighborhood");
        query1.setMaxResults(10);

        GeoQuery query2 = new GeoQuery(new GeoLocation(34.0522, -118.2437));
        query2.setAccuracy("low");
        query2.setGranularity("city");
        query2.setMaxResults(5);

        assertFalse(query1.equals(query2));
    }

    @Test
    public void testHashCode() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        query1.setAccuracy("high");
        query1.setGranularity("neighborhood");
        query1.setMaxResults(10);

        GeoQuery query2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        query2.setAccuracy("high");
        query2.setGranularity("neighborhood");
        query2.setMaxResults(10);

        assertEquals(query1.hashCode(), query2.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "GeoQuery{location=GeoLocation{latitude=37.7749, longitude=-122.4194}, ip='null', accuracy='high', granularity='neighborhood', maxResults=10}";
        assertEquals(expected, geoQuery.toString());
    }
}