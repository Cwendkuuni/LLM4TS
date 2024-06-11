import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GeoQueryTest {

    // Test case for GeoQuery constructor with GeoLocation parameter
    @Test
    public void testGeoQueryConstructorWithGeoLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        assertNotNull(geoQuery);
        assertEquals(location, geoQuery.getLocation());
        assertEquals(null, geoQuery.getIp());
    }

    // Test case for GeoQuery constructor with IP parameter
    @Test
    public void testGeoQueryConstructorWithIp() {
        String ip = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        assertNotNull(geoQuery);
        assertEquals(null, geoQuery.getLocation());
        assertEquals(ip, geoQuery.getIp());
    }

    // Test case for setAccuracy and getAccuracy methods
    @Test
    public void testSetAndGetAccuracy() {
        String accuracy = "high";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    // Test case for setGranularity and getGranularity methods
    @Test
    public void testSetAndGetGranularity() {
        String granularity = "neighborhood";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }

    // Test case for setMaxResults and getMaxResults methods
    @Test
    public void testSetAndGetMaxResults() {
        int maxResults = 10;
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    // Test case for appendParameter with String value
    @Test
    public void testAppendParameterWithStringValue() {
        List<HttpParameter> params = new ArrayList<>();
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");
        geoQuery.appendParameter("ip", "192.168.1.1", params);
        assertEquals(1, params.size());
        assertEquals("ip", params.get(0).getName());
        assertEquals("192.168.1.1", params.get(0).getValue());
    }

    // Test case for appendParameter with int value
    @Test
    public void testAppendParameterWithIntValue() {
        List<HttpParameter> params = new ArrayList<>();
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");
        geoQuery.appendParameter("max_results", 10, params);
        assertEquals(1, params.size());
        assertEquals("max_results", params.get(0).getName());
        assertEquals("10", params.get(0).getValue());
    }

    // Test case for appendParameter with double value
    @Test
    public void testAppendParameterWithDoubleValue() {
        List<HttpParameter> params = new ArrayList<>();
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");
        geoQuery.appendParameter("lat", 37.7749, params);
        assertEquals(1, params.size());
        assertEquals("lat", params.get(0).getName());
        assertEquals("37.7749", params.get(0).getValue());
    }

    // Test case for equals method
    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertEquals(true, geoQuery1.equals(geoQuery2));
    }

    // Test case for hashCode method
    @Test
    public void testHashCode() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertEquals(geoQuery.getLocation().hashCode(), geoQuery.hashCode());
    }

    // Test case for toString method
    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        String expected = "GeoQuery{location=GeoLocation{latitude=37.7749, longitude=-122.4194}, ip='null', accuracy='null', granularity='null', maxResults=0}";
        assertEquals(expected, geoQuery.toString());
    }
}