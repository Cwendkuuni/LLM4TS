import static org.junit.Assert.*;
import org.junit.Test;
import twitter4j.GeoLocation;

public class GeoQueryTest {

    @Test
    public void testConstructorWithGeoLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);

        assertEquals(location, geoQuery.getLocation());
        assertNull(geoQuery.getIp());
        assertNull(geoQuery.getAccuracy());
        assertNull(geoQuery.getGranularity());
        assertEquals(0, geoQuery.getMaxResults());
    }

    @Test
    public void testConstructorWithIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        assertNull(geoQuery.getLocation());
        assertEquals(ip, geoQuery.getIp());
        assertNull(geoQuery.getAccuracy());
        assertNull(geoQuery.getGranularity());
        assertEquals(0, geoQuery.getMaxResults());
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
        String accuracy = "high";
        GeoQuery newGeoQuery = geoQuery.accuracy(accuracy);

        assertEquals(accuracy, newGeoQuery.getAccuracy());
    }

    @Test
    public void testSetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        String granularity = "neighborhood";
        geoQuery.setGranularity(granularity);

        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testGranularityMethod() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        String granularity = "neighborhood";
        GeoQuery newGeoQuery = geoQuery.granularity(granularity);

        assertEquals(granularity, newGeoQuery.getGranularity());
    }

    @Test
    public void testSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        int maxResults = 10;
        geoQuery.setMaxResults(maxResults);

        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testMaxResultsMethod() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        int maxResults = 10;
        GeoQuery newGeoQuery = geoQuery.maxResults(maxResults);

        assertEquals(maxResults, newGeoQuery.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArrayWithLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);

        HttpParameter[] expected = { new HttpParameter("lat", "37.7749"), new HttpParameter("long", "-122.4194") };
        assertArrayEquals(expected, geoQuery.asHttpParameterArray());
    }

    @Test
    public void testAsHttpParameterArrayWithIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        HttpParameter[] expected = { new HttpParameter("ip", ip) };
        assertArrayEquals(expected, geoQuery.asHttpParameterArray());
    }

    @Test
    public void testAsHttpParameterArrayWithNullLocationAndIp() {
        GeoQuery geoQuery = new GeoQuery(null);

        HttpParameter[] expected = {};
        assertArrayEquals(expected, geoQuery.asHttpParameterArray());
    }

    @Test
    public void testEqualsSameObject() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));

        assertTrue(geoQuery.equals(geoQuery));
    }

    @Test
    public void testEqualsNullObject() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));

        assertFalse(geoQuery.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));

        assertFalse(geoQuery.equals("random string"));
    }

    @Test
    public void testEqualsDifferentLocation() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(40.7128, -74.0060));

        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsDifferentIp() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery geoQuery2 = new GeoQuery("192.168.0.1");

        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsDifferentAccuracy() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery2.setAccuracy("high");

        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsDifferentGranularity() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery2.setGranularity("city");

        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsDifferentMaxResults() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery2.setMaxResults(10);

        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsSameObjectWithSameAttributes() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));

        assertTrue(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsSameObjectWithNullLocationAndIp() {
        GeoQuery geoQuery1 = new GeoQuery(null);
        GeoQuery geoQuery2 = new GeoQuery(null);

        assertTrue(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsSameObjectWithSameAttributesAndDifferentObjectTypes() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));

        assertTrue(geoQuery1.equals((Object) geoQuery2));
    }

    @Test
    public void testEqualsSameObjectWithSameAttributesAndDifferentObjectTypesNullObject() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));

        assertFalse(geoQuery1.equals((Object) null));
    }

    @Test
    public void testHashCodeWithNullLocationAndIp() {
        GeoQuery geoQuery = new GeoQuery(null);

        int expected = 0;
        assertEquals(expected, geoQuery.hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(10);

        String expected = "GeoQuery{location=" + location + ", ip='null', accuracy='high', granularity='city', maxResults=10}";
        assertEquals(expected, geoQuery.toString());
    }
}