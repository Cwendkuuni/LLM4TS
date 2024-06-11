import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    // Test cases for constructor
    @Test
    public void testConstructorWithGeoLocation() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(location, geoQuery.getLocation());
        assertNull(geoQuery.getIp());
    }

    @Test
    public void testConstructorWithIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        assertEquals(ip, geoQuery.getIp());
        assertNull(geoQuery.getLocation());
    }

    // Test cases for accuracy
    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        assertNull(geoQuery.getAccuracy());
    }

    @Test
    public void testSetAccuracy() {
        String accuracy = "high";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testAccuracyMethod() {
        String accuracy = "medium";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        GeoQuery result = geoQuery.accuracy(accuracy);
        assertEquals(accuracy, result.getAccuracy());
    }

    // Test cases for granularity
    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        assertNull(geoQuery.getGranularity());
    }

    @Test
    public void testSetGranularity() {
        String granularity = "city";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testGranularityMethod() {
        String granularity = "neighborhood";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        GeoQuery result = geoQuery.granularity(granularity);
        assertEquals(granularity, result.getGranularity());
    }

    // Test cases for maxResults
    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        assertEquals(0, geoQuery.getMaxResults());
    }

    @Test
    public void testSetMaxResults() {
        int maxResults = 100;
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testMaxResultsMethod() {
        int maxResults = 50;
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        GeoQuery result = geoQuery.maxResults(maxResults);
        assertEquals(maxResults, result.getMaxResults());
    }

    // Test cases for asHttpParameterArray
    @Test
    public void testAsHttpParameterArrayWithGeoLocation() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery geoQuery = new GeoQuery(location);
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertEquals(2, params.length);
        assertEquals("lat", params[0].getName());
        assertEquals(String.valueOf(location.getLatitude()), params[0].getValue());
        assertEquals("long", params[1].getName());
        assertEquals(String.valueOf(location.getLongitude()), params[1].getValue());
    }

    @Test
    public void testAsHttpParameterArrayWithIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertEquals(1, params.length);
        assertEquals("ip", params[0].getName());
        assertEquals(ip, params[0].getValue());
    }

    // Test cases for equals
    @Test
    public void testEqualsWithEqualObjects() {
        GeoLocation location1 = new GeoLocation(10.0, 20.0);
        GeoLocation location2 = new GeoLocation(10.0, 20.0);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        assertTrue(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsWithDifferentObjects() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery geoQuery = new GeoQuery(location);
        assertFalse(geoQuery.equals(null));
        assertFalse(geoQuery.equals("String"));
        assertFalse(geoQuery.equals(new Object()));
    }

    // Test cases for hashCode
    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        String ip = "192.168.0.1";
        String accuracy = "high";
        String granularity = "city";
        int maxResults = 100;
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setIp(ip);
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);
        int expectedHashCode = location.hashCode();
        expectedHashCode = 31 * expectedHashCode + ip.hashCode();
        expectedHashCode = 31 * expectedHashCode + accuracy.hashCode();
        expectedHashCode = 31 * expectedHashCode + granularity.hashCode();
        expectedHashCode = 31 * expectedHashCode + maxResults;
        assertEquals(expectedHashCode, geoQuery.hashCode());
    }

    // Test cases for toString
    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        String ip = "192.168.0.1";
        String accuracy = "high";
        String granularity = "city";
        int maxResults = 100;
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setIp(ip);
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);
        String expectedString = "GeoQuery{location=" + location + ", ip='" + ip + '\'' +
                ", accuracy='" + accuracy + '\'' + ", granularity='" + granularity +
                '\'' + ", maxResults=" + maxResults + '}';
        assertEquals(expectedString, geoQuery.toString());
    }
}