import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        // Typical use case: location is set
        GeoLocation geoLocation = new GeoLocation(1.234, 5.678);
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        
        assertEquals(geoLocation, geoQuery.getLocation());
    }

    @Test
    public void testGetIp() {
        // Typical use case: ip is set
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        
        assertEquals(ip, geoQuery.getIp());
    }

    @Test
    public void testGetAccuracy() {
        // Edge case: accuracy is null
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        
        assertNull(geoQuery.getAccuracy());
    }

    @Test
    public void testSetAccuracy() {
        // Typical use case: accuracy is set
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        String accuracy = "high";
        
        geoQuery.setAccuracy(accuracy);
        
        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testGranularity() {
        // Typical use case: granularity is set
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        String granularity = "large";
        
        geoQuery.setGranularity(granularity);
        
        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testGetMaxResults() {
        // Typical use case: maxResults is set
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        int maxResults = 100;
        
        geoQuery.setMaxResults(maxResults);
        
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testAppendParameter() {
        // Typical use case: parameter with string value is appended
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        String name = "name";
        String value = "value";
        List<HttpParameter> params = new ArrayList<>();
        
        geoQuery.appendParameter(name, value, params);
        
        assertEquals(1, params.size());
        assertEquals(value, params.get(0).getValue());
    }

    @Test
    public void testAppendParameter_int() {
        // Typical use case: parameter with int value is appended
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        String name = "name";
        int value = 123;
        List<HttpParameter> params = new ArrayList<>();
        
        geoQuery.appendParameter(name, value, params);
        
        assertEquals(1, params.size());
        assertEquals(String.valueOf(value), params.get(0).getValue());
    }

    @Test
    public void testAppendParameter_double() {
        // Typical use case: parameter with double value is appended
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        String name = "name";
        double value = 1.234;
        List<HttpParameter> params = new ArrayList<>();
        
        geoQuery.appendParameter(name, value, params);
        
        assertEquals(1, params.size());
        assertEquals(String.valueOf(value), params.get(0).getValue());
    }

    @Test
    public void testEquals() {
        // Typical use case: two GeoQuery objects with same values are equal
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.234, 5.678));
        geoQuery1.setAccuracy("high");
        geoQuery1.setGranularity("large");
        geoQuery1.setMaxResults(100);
        
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.234, 5.678));
        geoQuery2.setAccuracy("high");
        geoQuery2.setGranularity("large");
        geoQuery2.setMaxResults(100);
        
        assertTrue(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testHashCode() {
        // Typical use case: GeoQuery objects with same values have same hash code
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.234, 5.678));
        geoQuery1.setAccuracy("high");
        geoQuery1.setGranularity("large");
        geoQuery1.setMaxResults(100);
        
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.234, 5.678));
        geoQuery2.setAccuracy("high");
        geoQuery2.setGranularity("large");
        geoQuery2.setMaxResults(100);
        
        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }

    @Test
    public void testToString() {
        // Typical use case: expected toString output for GeoQuery object
        GeoLocation geoLocation = new GeoLocation(1.234, 5.678);
        String ip = "192.168.0.1";
        String accuracy = "high";
        String granularity = "large";
        int maxResults = 100;
        
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);
        
        String expectedToString = "GeoQuery{location=" + geoLocation + ", ip='" + ip + '\'' + ", accuracy='" + accuracy + '\'' + ", granularity='" + granularity + '\'' + ", maxResults=" + maxResults + '}';
        
        assertEquals(expectedToString, geoQuery.toString());
    }
}