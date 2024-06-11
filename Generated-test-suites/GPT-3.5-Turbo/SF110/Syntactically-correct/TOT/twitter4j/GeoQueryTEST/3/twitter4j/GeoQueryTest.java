import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {
    private GeoLocation location;

    @Before
    public void setUp() {
        location = new GeoLocation(37.7749, -122.4194);
    }

    @Test
    public void testGeoQueryConstructorWithGeoLocation() {
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(location, geoQuery.getLocation());
        assertNull(geoQuery.getIp());
    }

    @Test
    public void testGeoQueryConstructorWithIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        assertEquals(ip, geoQuery.getIp());
        assertNull(geoQuery.getLocation());
    }

    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(location);
        assertNull(geoQuery.getAccuracy());
    }

    @Test
    public void testSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(location);
        String accuracy = "low";
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testAccuracyMethod() {
        GeoQuery geoQuery = new GeoQuery(location);
        String accuracy = "low";
        GeoQuery updatedGeoQuery = geoQuery.accuracy(accuracy);
        assertEquals(accuracy, updatedGeoQuery.getAccuracy());
        assertEquals(geoQuery, updatedGeoQuery);
    }

    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(location);
        assertNull(geoQuery.getGranularity());
    }

    @Test
    public void testSetGranularity() {
        GeoQuery geoQuery = new GeoQuery(location);
        String granularity = "city";
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testGranularityMethod() {
        GeoQuery geoQuery = new GeoQuery(location);
        String granularity = "city";
        GeoQuery updatedGeoQuery = geoQuery.granularity(granularity);
        assertEquals(granularity, updatedGeoQuery.getGranularity());
        assertEquals(geoQuery, updatedGeoQuery);
    } 

    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(0, geoQuery.getMaxResults());
    }

    @Test
    public void testSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(location);
        int maxResults = 100;
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testMaxResultsMethod() {
        GeoQuery geoQuery = new GeoQuery(location);
        int maxResults = 100;
        GeoQuery updatedGeoQuery = geoQuery.maxResults(maxResults);
        assertEquals(maxResults, updatedGeoQuery.getMaxResults());
        assertEquals(geoQuery, updatedGeoQuery);
    }

    @Test
    public void testAsHttpParameterArrayWithLocation() {
        GeoQuery geoQuery = new GeoQuery(location);
        HttpParameter[] expectedParams = {
                new HttpParameter("lat", "37.7749"),
                new HttpParameter("long", "-122.4194")
        };
        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }

    @Test
    public void testAsHttpParameterArrayWithIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        HttpParameter[] expectedParams = {
                new HttpParameter("ip", ip)
        };
        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }

    @Test
    public void testAsHttpParameterArrayWithLocationAndAllParameters() {
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("low");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(100);
        
        HttpParameter[] expectedParams = {
                new HttpParameter("lat", "37.7749"),
                new HttpParameter("long", "-122.4194"),
                new HttpParameter("accuracy", "low"),
                new HttpParameter("granularity", "city"),
                new HttpParameter("max_results", 100)
        };
        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }

    @Test
    public void testEqualsWithSameObject() {
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(geoQuery, geoQuery);
    }

    @Test
    public void testEqualsWithEqualObjects() {
        GeoQuery geoQuery1 = new GeoQuery(location);
        GeoQuery geoQuery2 = new GeoQuery(location);
        assertEquals(geoQuery1, geoQuery2);
    }

    @Test
    public void testEqualsWithUnequalObjects() {
        GeoQuery geoQuery1 = new GeoQuery(location);
        GeoQuery geoQuery2 = new GeoQuery("192.168.0.1");
        assertNotEquals(geoQuery1, geoQuery2);
    }

    @Test
    public void testHashCode() {
        GeoQuery geoQuery1 = new GeoQuery(location);
        GeoQuery geoQuery2 = new GeoQuery("192.168.0.1");
        assertNotEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }

    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(location);
        String expectedString = "GeoQuery{location=" + location + ", ip='null', accuracy='null', granularity='null', maxResults=0}";
        assertEquals(expectedString, geoQuery.toString());
    }

}