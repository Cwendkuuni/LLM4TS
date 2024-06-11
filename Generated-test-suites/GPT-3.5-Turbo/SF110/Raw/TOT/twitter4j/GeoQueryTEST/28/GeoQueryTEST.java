import org.junit.*;
import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testConstructorWithLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);

        assertEquals(location, geoQuery.getLocation());
        assertNull(geoQuery.getIp());
    }

    @Test
    public void testConstructorWithIP() {
        String ip = "127.0.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        assertEquals(ip, geoQuery.getIp());
        assertNull(geoQuery.getLocation());
    }

    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertNull(geoQuery.getAccuracy());
    }

    @Test
    public void testSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        String accuracy = "high";

        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testAccuracyMethod() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        String accuracy = "low";

        GeoQuery result = geoQuery.accuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
        assertEquals(geoQuery, result);
    }

    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertNull(geoQuery.getGranularity());
    }

    @Test
    public void testSetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        String granularity = "fine";

        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testGranularityMethod() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        String granularity = "coarse";

        GeoQuery result = geoQuery.granularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
        assertEquals(geoQuery, result);
    }

    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertEquals(0, geoQuery.getMaxResults());
    }

    @Test
    public void testSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        int maxResults = 100;

        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testMaxResultsMethod() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        int maxResults = 50;

        GeoQuery result = geoQuery.maxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
        assertEquals(geoQuery, result);
    }

    @Test
    public void testAsHttpParameterArrayWithLocation() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        HttpParameter[] expectedParams = {
            new HttpParameter("lat", "37.7749"),
            new HttpParameter("long", "-122.4194"),
            null, null, null, new HttpParameter("max_results", 0)
        };

        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }

    @Test
    public void testAsHttpParameterArrayWithIP() {
        GeoQuery geoQuery = new GeoQuery("127.0.0.1");
        HttpParameter[] expectedParams = {
            null, null, null, null, null, new HttpParameter("max_results", 0)
        };

        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }

    @Test
    public void testEqualsWithSameObject() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));

        assertTrue(geoQuery.equals(geoQuery));
    }

    @Test
    public void testEqualsWithNullObject() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));

        assertFalse(geoQuery.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        String string = "Some string";

        assertFalse(geoQuery.equals(string));
    }

    @Test
    public void testEqualsWithDifferentLocation() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(34.0522, -118.2437));

        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsWithDifferentIP() {
        GeoQuery geoQuery1 = new GeoQuery("127.0.0.1");
        GeoQuery geoQuery2 = new GeoQuery("192.168.0.1");

        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsWithSameValues() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));

        assertTrue(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testHashCodeWithSameObjects() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        int expectedHashCode = geoQuery.hashCode();

        assertEquals(expectedHashCode, geoQuery.hashCode());
    }

    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        String expectedString = "GeoQuery{location=GeoLocation{latitude=37.7749, longitude=-122.4194}, ip='null', accuracy='null', granularity='null', maxResults=0}";

        assertEquals(expectedString, geoQuery.toString());
    }
}