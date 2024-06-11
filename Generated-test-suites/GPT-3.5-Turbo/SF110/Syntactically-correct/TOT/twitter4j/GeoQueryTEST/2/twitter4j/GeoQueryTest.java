import org.junit.*;
import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation geoLocation = new GeoLocation(10.0, 20.0);
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        assertEquals(geoLocation, geoQuery.getLocation());
    }

    @Test
    public void testGetIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        assertEquals(ip, geoQuery.getIp());
    }

    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(null);
        assertNull(geoQuery.getAccuracy());
    }

    @Test
    public void testSetAccuracy() {
        String accuracy = "high";
        GeoQuery geoQuery = new GeoQuery(null);
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testAccuracyMethod() {
        String accuracy = "medium";
        GeoQuery geoQuery = new GeoQuery(null);
        assertEquals(geoQuery, geoQuery.accuracy(accuracy));
        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(null);
        assertNull(geoQuery.getGranularity());
    }

    @Test
    public void testSetGranularity() {
        String granularity = "low";
        GeoQuery geoQuery = new GeoQuery(null);
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testGranularityMethod() {
        String granularity = "medium";
        GeoQuery geoQuery = new GeoQuery(null);
        assertEquals(geoQuery, geoQuery.granularity(granularity));
        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(null);
        assertEquals(0, geoQuery.getMaxResults());
    }

    @Test
    public void testSetMaxResults() {
        int maxResults = 100;
        GeoQuery geoQuery = new GeoQuery(null);
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testMaxResultsMethod() {
        int maxResults = 50;
        GeoQuery geoQuery = new GeoQuery(null);
        assertEquals(geoQuery, geoQuery.maxResults(maxResults));
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoLocation geoLocation = new GeoLocation(10.0, 20.0);
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("low");
        geoQuery.setMaxResults(100);

        HttpParameter[] expectedParams = new HttpParameter[]{
                new HttpParameter("lat", "10.0"),
                new HttpParameter("long", "20.0"),
                new HttpParameter("accuracy", "high"),
                new HttpParameter("granularity", "low"),
                new HttpParameter("max_results", 100)
        };

        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }

    @Test
    public void testEquals() {
        GeoLocation geoLocation1 = new GeoLocation(10.0, 20.0);
        GeoLocation geoLocation2 = new GeoLocation(10.0, 20.0);
        String ip1 = "192.168.0.1";
        String ip2 = "192.168.0.1";
        String accuracy1 = "high";
        String accuracy2 = "high";
        String granularity1 = "low";
        String granularity2 = "low";
        int maxResults1 = 100;
        int maxResults2 = 100;

        GeoQuery geoQuery1 = new GeoQuery(geoLocation1);
        geoQuery1.setAccuracy(accuracy1);
        geoQuery1.setGranularity(granularity1);
        geoQuery1.setMaxResults(maxResults1);

        GeoQuery geoQuery2 = new GeoQuery(geoLocation2);
        geoQuery2.setAccuracy(accuracy2);
        geoQuery2.setGranularity(granularity2);
        geoQuery2.setMaxResults(maxResults2);

        assertEquals(geoQuery1, geoQuery2);
    }

    @Test
    public void testHashCode() {
        GeoLocation geoLocation = new GeoLocation(10.0, 20.0);
        String ip = "192.168.0.1";
        String accuracy = "high";
        String granularity = "low";
        int maxResults = 100;

        GeoQuery geoQuery = new GeoQuery(geoLocation);
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);

        int expectedHashCode = 31 * (31 * (31 * (31 * (31 * geoLocation.hashCode() + ip.hashCode()) + accuracy.hashCode()) + granularity.hashCode()) + maxResults);
        assertEquals(expectedHashCode, geoQuery.hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation geoLocation = new GeoLocation(10.0, 20.0);
        String ip = "192.168.0.1";
        String accuracy = "high";
        String granularity = "low";
        int maxResults = 100;

        GeoQuery geoQuery = new GeoQuery(geoLocation);
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);

        String expectedString = "GeoQuery{location=" + geoLocation + ", ip='" + ip + '\'' + ", accuracy='" + accuracy + '\'' + ", granularity='" + granularity + '\'' + ", maxResults=" + maxResults + '}';
        assertEquals(expectedString, geoQuery.toString());
    }

}