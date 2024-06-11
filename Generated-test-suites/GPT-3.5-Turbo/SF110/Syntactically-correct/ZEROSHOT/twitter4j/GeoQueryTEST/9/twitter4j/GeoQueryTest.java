package twitter4j;

import org.junit.*;
import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void getLocationTest() {
        // Setup
        GeoLocation location = new GeoLocation(0, 0);
        GeoQuery geoQuery = new GeoQuery(location);

        // Verify
        assertEquals(location, geoQuery.getLocation());
    }

    @Test
    public void getIpTest() {
        // Setup
        String ip = "127.0.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        // Verify
        assertEquals(ip, geoQuery.getIp());
    }

    @Test
    public void getAccuracyTest() {
        // Setup
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        String accuracy = "high";
        geoQuery.setAccuracy(accuracy);

        // Verify
        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void setAccuracyTest() {
        // Setup
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        String accuracy = "high";

        // Execute
        geoQuery.setAccuracy(accuracy);

        // Verify
        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void getGranularityTest() {
        // Setup
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        String granularity = "city";
        geoQuery.setGranularity(granularity);

        // Verify
        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void setGranularityTest() {
        // Setup
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        String granularity = "city";

        // Execute
        geoQuery.setGranularity(granularity);

        // Verify
        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void getMaxResultsTest() {
        // Setup
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        int maxResults = 100;
        geoQuery.setMaxResults(maxResults);

        // Verify
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void setMaxResultsTest() {
        // Setup
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        int maxResults = 100;

        // Execute
        geoQuery.setMaxResults(maxResults);

        // Verify
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void asHttpParameterArrayTest() {
        // Setup
        GeoLocation location = new GeoLocation(0, 0);
        GeoQuery geoQuery = new GeoQuery(location);
        String accuracy = "high";
        String granularity = "city";
        int maxResults = 100;
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);

        // Execute
        HttpParameter[] httpParams = geoQuery.asHttpParameterArray();

        // Verify
        assertNotNull(httpParams);
        assertEquals(5, httpParams.length);
        assertEquals("lat", httpParams[0].getName());
        assertEquals(String.valueOf(location.getLatitude()), httpParams[0].getValue());
        assertEquals("long", httpParams[1].getName());
        assertEquals(String.valueOf(location.getLongitude()), httpParams[1].getValue());
        assertEquals("accuracy", httpParams[2].getName());
        assertEquals(accuracy, httpParams[2].getValue());
        assertEquals("granularity", httpParams[3].getName());
        assertEquals(granularity, httpParams[3].getValue());
        assertEquals("max_results", httpParams[4].getName());
        assertEquals(String.valueOf(maxResults), httpParams[4].getValue());
    }

    @Test
    public void appendParameterStringStringTest() {
        // Setup
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        String name = "name";
        String value = "value";

        // Execute
        HttpParameter[] httpParams = new HttpParameter[1];
        geoQuery.appendParameter(name, value, Arrays.asList(httpParams));

        // Verify
        assertNotNull(httpParams[0]);
        assertEquals(name, httpParams[0].getName());
        assertEquals(value, httpParams[0].getValue());
    }

    @Test
    public void appendParameterStringIntTest() {
        // Setup
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        String name = "name";
        int value = 100;

        // Execute
        HttpParameter[] httpParams = new HttpParameter[1];
        geoQuery.appendParameter(name, value, Arrays.asList(httpParams));

        // Verify
        assertNotNull(httpParams[0]);
        assertEquals(name, httpParams[0].getName());
        assertEquals(String.valueOf(value), httpParams[0].getValue());
    }

    @Test
    public void appendParameterStringDoubleTest() {
        // Setup
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        String name = "name";
        double value = 0.5;

        // Execute
        HttpParameter[] httpParams = new HttpParameter[1];
        geoQuery.appendParameter(name, value, Arrays.asList(httpParams));

        // Verify
        assertNotNull(httpParams[0]);
        assertEquals(name, httpParams[0].getName());
        assertEquals(String.valueOf(value), httpParams[0].getValue());
    }

    @Test
    public void equalsTest() {
        // Setup
        GeoLocation location1 = new GeoLocation(0, 0);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoLocation location2 = new GeoLocation(1, 1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        String ip = "127.0.0.1";

        // Verify
        assertTrue(geoQuery1.equals(geoQuery1)); // Same object
        assertFalse(geoQuery1.equals(null)); // Null object
        assertFalse(geoQuery1.equals(ip)); // Different class
        assertFalse(geoQuery1.equals(geoQuery2)); // Different locations
        assertFalse(geoQuery1.equals(new GeoQuery(location1))); // Same location, different accuracy
        assertFalse(geoQuery1.equals(new GeoQuery(ip))); // Different type of query
        assertFalse(geoQuery1.equals(new GeoQuery(location1).accuracy("high"))); // Different accuracy
        assertFalse(geoQuery1.equals(new GeoQuery(location1).granularity("city"))); // Different granularity
        assertFalse(geoQuery1.equals(new GeoQuery(location1).maxResults(100))); // Different max results
    }

    @Test
    public void hashCodeTest() {
        // Setup
        GeoLocation location = new GeoLocation(0, 0);
        GeoQuery geoQuery = new GeoQuery(location);
        String ip = "127.0.0.1";
        String accuracy = "high";
        String granularity = "city";
        int maxResults = 100;
        geoQuery.setIp(ip);
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);

        // Execute
        int hashCode = geoQuery.hashCode();

        // Verify
        int expectedHashCode = location.hashCode();
        expectedHashCode = 31 * expectedHashCode + ip.hashCode();
        expectedHashCode = 31 * expectedHashCode + accuracy.hashCode();
        expectedHashCode = 31 * expectedHashCode + granularity.hashCode();
        expectedHashCode = 31 * expectedHashCode + maxResults;
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void toStringTest() {
        // Setup
        GeoLocation location = new GeoLocation(0, 0);
        GeoQuery geoQuery = new GeoQuery(location);
        String ip = "127.0.0.1";
        String accuracy = "high";
        String granularity = "city";
        int maxResults = 100;
        geoQuery.setIp(ip);
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);

        // Verify
        String expectedString = "GeoQuery{location=" + location + ", ip='" + ip + '\'' + ", accuracy='" + accuracy + '\'' + ", granularity='" + granularity + '\'' + ", maxResults=" + maxResults + '}';
        assertEquals(expectedString, geoQuery.toString());
    }

}