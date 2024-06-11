import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    private GeoQuery geoQuery;

    @Before
    public void setUp() {
        geoQuery = new GeoQuery(new GeoLocation(1.234, 5.678));
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("low");
        geoQuery.setMaxResults(10);
    }

    // Test for GeoQuery(GeoLocation location)
    @Test
    public void testGeoQueryWithGeoLocation() {
        assertEquals(new GeoLocation(1.234, 5.678), geoQuery.getLocation());
        assertNull(geoQuery.getIp());
    }

    // Test for GeoQuery(String ip)
    @Test
    public void testGeoQueryWithIp() {
        geoQuery = new GeoQuery("127.0.0.1");
        assertNull(geoQuery.getLocation());
        assertEquals("127.0.0.1", geoQuery.getIp());
    }

    // Test for getAccuracy()
    @Test
    public void testGetAccuracy() {
        assertEquals("high", geoQuery.getAccuracy());
    }

    // Test for setAccuracy(String accuracy)
    @Test
    public void testSetAccuracy() {
        geoQuery.setAccuracy("medium");
        assertEquals("medium", geoQuery.getAccuracy());
    }

    // Test for accuracy(String accuracy)
    @Test
    public void testAccuracyMethod() {
        assertEquals(geoQuery, geoQuery.accuracy("low"));
        assertEquals("low", geoQuery.getAccuracy());
    }

    // Test for getGranularity()
    @Test
    public void testGetGranularity() {
        assertEquals("low", geoQuery.getGranularity());
    }

    // Test for setGranularity(String granularity)
    @Test
    public void testSetGranularity() {
        geoQuery.setGranularity("medium");
        assertEquals("medium", geoQuery.getGranularity());
    }

    // Test for granularity(String granularity)
    @Test
    public void testGranularityMethod() {
        assertEquals(geoQuery, geoQuery.granularity("high"));
        assertEquals("high", geoQuery.getGranularity());
    }

    // Test for getMaxResults()
    @Test
    public void testGetMaxResults() {
        assertEquals(10, geoQuery.getMaxResults());
    }

    // Test for setMaxResults(int maxResults)
    @Test
    public void testSetMaxResults() {
        geoQuery.setMaxResults(5);
        assertEquals(5, geoQuery.getMaxResults());
    }

    // Test for maxResults(int maxResults)
    @Test
    public void testMaxResultsMethod() {
        assertEquals(geoQuery, geoQuery.maxResults(20));
        assertEquals(20, geoQuery.getMaxResults());
    }

    // Test for asHttpParameterArray()
    @Test
    public void testAsHttpParameterArray() {
        HttpParameter[] expectedParams = {
                new HttpParameter("lat", "1.234"),
                new HttpParameter("long", "5.678"),
                new HttpParameter("accuracy", "high"),
                new HttpParameter("granularity", "low"),
                new HttpParameter("max_results", 10)
        };
        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }

    // Test for equals(Object o)
    @Test
    public void testEquals() {
        GeoQuery otherQuery = new GeoQuery(new GeoLocation(1.234, 5.678));
        otherQuery.setAccuracy("high");
        otherQuery.setGranularity("low");
        otherQuery.setMaxResults(10);
        assertTrue(geoQuery.equals(otherQuery));
    }

    // Test for hashCode()
    @Test
    public void testHashCode() {
        GeoQuery otherQuery = new GeoQuery(new GeoLocation(1.234, 5.678));
        otherQuery.setAccuracy("high");
        otherQuery.setGranularity("low");
        otherQuery.setMaxResults(10);
        assertEquals(geoQuery.hashCode(), otherQuery.hashCode());
    }

    // Test for toString()
    @Test
    public void testToString() {
        String expectedString = "GeoQuery{location=GeoLocation{latitude=1.234, longitude=5.678}, ip='null', accuracy='high', granularity='low', maxResults=10}";
        assertEquals(expectedString, geoQuery.toString());
    }
}