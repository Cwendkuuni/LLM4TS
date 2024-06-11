import org.junit.*;
import static org.junit.Assert.*;

public class GeoQueryTest {

    private GeoQuery geoQuery;

    @Before
    public void setUp() {
        geoQuery = new GeoQuery(new GeoLocation(12.34, 56.78));
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("fine");
        geoQuery.setMaxResults(10);
    }

    @Test
    public void testGeoQueryConstructorWithGeoLocation() {
        assertEquals(12.34, geoQuery.getLocation().getLatitude(), 0.001);
        assertEquals(56.78, geoQuery.getLocation().getLongitude(), 0.001);
        assertNull(geoQuery.getIp());
    }

    @Test
    public void testGeoQueryConstructorWithIp() {
        geoQuery = new GeoQuery("127.0.0.1");
        assertNull(geoQuery.getLocation());
        assertEquals("127.0.0.1", geoQuery.getIp());
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
    public void testAccuracy() {
        geoQuery.accuracy("medium");
        assertEquals("medium", geoQuery.getAccuracy());
    }

    @Test
    public void testGetGranularity() {
        assertEquals("fine", geoQuery.getGranularity());
    }

    @Test
    public void testSetGranularity() {
        geoQuery.setGranularity("coarse");
        assertEquals("coarse", geoQuery.getGranularity());
    }

    @Test
    public void testGranularity() {
        geoQuery.granularity("medium");
        assertEquals("medium", geoQuery.getGranularity());
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
    public void testMaxResults() {
        geoQuery.maxResults(7);
        assertEquals(7, geoQuery.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArray() {
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertEquals(5, params.length);
        assertEquals("lat", params[0].getName());
        assertEquals("12.34", params[0].getValue());
        assertEquals("long", params[1].getName());
        assertEquals("56.78", params[1].getValue());
        assertEquals("accuracy", params[2].getName());
        assertEquals("high", params[2].getValue());
        assertEquals("granularity", params[3].getName());
        assertEquals("fine", params[3].getValue());
        assertEquals("max_results", params[4].getName());
        assertEquals("10", params[4].getValue());
    }

    @Test
    public void testEquals() {
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(12.34, 56.78));
        geoQuery2.setAccuracy("high");
        geoQuery2.setGranularity("fine");
        geoQuery2.setMaxResults(10);
        assertTrue(geoQuery.equals(geoQuery2));
    }

    @Test
    public void testEqualsWhenLocationIsNull() {
        geoQuery = new GeoQuery("127.0.0.1");
        GeoQuery geoQuery2 = new GeoQuery("127.0.0.1");
        geoQuery2.setAccuracy("high");
        geoQuery2.setGranularity("fine");
        geoQuery2.setMaxResults(10);
        assertFalse(geoQuery.equals(geoQuery2));
    }

    @Test
    public void testEqualsWhenIpIsNull() {
        geoQuery.setGranularity("coarse");
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(12.34, 56.78));
        geoQuery2.setAccuracy("high");
        geoQuery2.setGranularity("coarse");
        geoQuery2.setMaxResults(10);
        assertFalse(geoQuery.equals(geoQuery2));
    }

    @Test
    public void testEqualsWhenAccuracyIsDifferent() {
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(12.34, 56.78));
        geoQuery2.setAccuracy("low");
        geoQuery2.setGranularity("fine");
        geoQuery2.setMaxResults(10);
        assertFalse(geoQuery.equals(geoQuery2));
    }

    @Test
    public void testEqualsWhenGranularityIsDifferent() {
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(12.34, 56.78));
        geoQuery2.setAccuracy("high");
        geoQuery2.setGranularity("coarse");
        geoQuery2.setMaxResults(10);
        assertFalse(geoQuery.equals(geoQuery2));
    }

    @Test
    public void testEqualsWhenMaxResultsIsDifferent() {
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(12.34, 56.78));
        geoQuery2.setAccuracy("high");
        geoQuery2.setGranularity("fine");
        geoQuery2.setMaxResults(5);
        assertFalse(geoQuery.equals(geoQuery2));
    }

    @Test
    public void testHashCode() {
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(12.34, 56.78));
        geoQuery2.setAccuracy("high");
        geoQuery2.setGranularity("fine");
        geoQuery2.setMaxResults(10);
        assertEquals(geoQuery.hashCode(), geoQuery2.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("GeoQuery{location=GeoLocation{latitude=12.34, longitude=56.78}, ip='null', accuracy='high', granularity='fine', maxResults=10}", geoQuery.toString());
    }

    @Test
    public void testToStringWhenLocationIsNull() {
        geoQuery = new GeoQuery("127.0.0.1");
        assertEquals("GeoQuery{location=null, ip='127.0.0.1', accuracy='high', granularity='fine', maxResults=10}", geoQuery.toString());
    }
    
    // Add more test cases for edge cases and error scenarios
    
}



// Add more test cases here

