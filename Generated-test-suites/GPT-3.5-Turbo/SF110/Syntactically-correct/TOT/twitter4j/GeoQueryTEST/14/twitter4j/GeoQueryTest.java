import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    private GeoQuery geoQuery;

    @Before
    public void setUp() {
        GeoLocation location = new GeoLocation(0, 0);
        geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(5);
    }

    @After
    public void tearDown() {
        geoQuery = null;
    }

    // Test the constructor with GeoLocation
    @Test
    public void testConstructorWithGeoLocation() {
        GeoLocation location = new GeoLocation(1, 1);
        GeoQuery query = new GeoQuery(location);
        assertEquals(location, query.getLocation());
        assertNull(query.getIp());
    }

    // Test the constructor with IP address
    @Test
    public void testConstructorWithIp() {
        String ip = "192.168.0.1";
        GeoQuery query = new GeoQuery(ip);
        assertNull(query.getLocation());
        assertEquals(ip, query.getIp());
    }

    // Test the accuracy setter and getter
    @Test
    public void testSetAndGetAccuracy() {
        String accuracy = "low";
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    // Test the granularity setter and getter
    @Test
    public void testSetAndGetGranularity() {
        String granularity = "city";
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }

    // Test the maxResults setter and getter
    @Test
    public void testSetAndGetMaxResults() {
        int maxResults = 10;
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    // Test asHttpParameterArray method with location and all other parameters set
    @Test
    public void testAsHttpParameterArrayWithLocation() {
        HttpParameter[] parameters = geoQuery.asHttpParameterArray();
        assertEquals(6, parameters.length);
        assertNull(parameters[0]);
        assertNull(parameters[1]);
        assertNull(parameters[2]);
        assertNull(parameters[3]);
        assertNull(parameters[4]);
        assertNull(parameters[5]);
    }

    // Test asHttpParameterArray method with IP and all other parameters set
    @Test
    public void testAsHttpParameterArrayWithIp() {
        GeoQuery query = new GeoQuery("192.168.0.1");
        query.setAccuracy("medium");
        query.setGranularity("neighborhood");
        query.setMaxResults(3);
        HttpParameter[] parameters = query.asHttpParameterArray();
        assertEquals(6, parameters.length);
        assertNull(parameters[0]);
        assertNull(parameters[1]);
        assertNull(parameters[2]);
        assertEquals("ip", parameters[3].getName());
        assertEquals("192.168.0.1", parameters[3].getValue());
        assertEquals("medium", parameters[4].getName());
        assertEquals("neighborhood", parameters[4].getValue());
        assertEquals("3", parameters[5].getName());
        assertEquals("max_results", parameters[5].getValue());
    }

    // Test appendParameter method with a non-null value
    @Test
    public void testAppendParameterWithValue() {
        List<HttpParameter> params = new ArrayList<>();
        geoQuery.appendParameter("test", "value", params);
        assertEquals(1, params.size());
        assertEquals("test", params.get(0).getName());
        assertEquals("value", params.get(0).getValue());
    }

    // Test appendParameter method with a null value
    @Test
    public void testAppendParameterWithNullValue() {
        List<HttpParameter> params = new ArrayList<>();
        geoQuery.appendParameter("test", null, params);
        assertEquals(0, params.size());
    }

    // Test appendParameter method with an integer value greater than 0
    @Test
    public void testAppendParameterWithPositiveIntValue() {
        List<HttpParameter> params = new ArrayList<>();
        geoQuery.appendParameter("test", 5, params);
        assertEquals(1, params.size());
        assertEquals("test", params.get(0).getName());
        assertEquals("5", params.get(0).getValue());
    }

    // Test appendParameter method with an integer value less than or equal to 0
    @Test
    public void testAppendParameterWithNonPositiveIntValue() {
        List<HttpParameter> params = new ArrayList<>();
        geoQuery.appendParameter("test", 0, params);
        assertEquals(0, params.size());
    }

    // Test appendParameter method with a double value
    @Test
    public void testAppendParameterWithDoubleValue() {
        List<HttpParameter> params = new ArrayList<>();
        geoQuery.appendParameter("test", 3.14, params);
        assertEquals(1, params.size());
        assertEquals("test", params.get(0).getName());
        assertEquals("3.14", params.get(0).getValue());
    }

    // Test equals method for equal objects
    @Test
    public void testEqualsForEqualObjects() {
        GeoLocation location = new GeoLocation(0, 0);
        GeoQuery query = new GeoQuery(location);
        query.setAccuracy("high");
        query.setGranularity("neighborhood");
        query.setMaxResults(5);
        assertTrue(geoQuery.equals(query));
    }

    // Test equals method for different objects
    @Test
    public void testEqualsForDifferentObjects() {
        GeoLocation location = new GeoLocation(1, 1);
        GeoQuery query = new GeoQuery(location);
        query.setAccuracy("low");
        query.setGranularity("city");
        query.setMaxResults(10);
        assertFalse(geoQuery.equals(query));
    }

    // Test equals method for null object
    @Test
    public void testEqualsForNullObject() {
        assertFalse(geoQuery.equals(null));
    }

    // Test hashCode method for equal objects
    @Test
    public void testHashCodeForEqualObjects() {
        GeoLocation location = new GeoLocation(0, 0);
        GeoQuery query = new GeoQuery(location);
        query.setAccuracy("high");
        query.setGranularity("neighborhood");
        query.setMaxResults(5);
        assertEquals(geoQuery.hashCode(), query.hashCode());
    }

    // Test hashCode method for different objects
    @Test
    public void testHashCodeForDifferentObjects() {
        GeoLocation location = new GeoLocation(1, 1);
        GeoQuery query = new GeoQuery(location);
        query.setAccuracy("low");
        query.setGranularity("city");
        query.setMaxResults(10);
        assertNotEquals(geoQuery.hashCode(), query.hashCode());
    }

    // Test toString method
    @Test
    public void testToString() {
        String expected = "GeoQuery{location=" + geoQuery.getLocation() +
                ", ip='" + geoQuery.getIp() + '\'' +
                ", accuracy='" + geoQuery.getAccuracy() + '\'' +
                ", granularity='" + geoQuery.getGranularity() + '\'' +
                ", maxResults=" + geoQuery.getMaxResults() +
                '}';
        assertEquals(expected, geoQuery.toString());
    }

}