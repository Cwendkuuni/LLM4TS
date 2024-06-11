import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {
    private GeoQuery geoQuery;

    @Before
    public void setUp() {
        GeoLocation location = new GeoLocation(37.7833, -122.4167);
        geoQuery = new GeoQuery(location);
    }

    @Test
    public void testGetLocation() {
        GeoLocation location = geoQuery.getLocation();
        assertNotNull(location);
        assertEquals(37.7833, location.getLatitude(), 0.0001);
        assertEquals(-122.4167, location.getLongitude(), 0.0001);
    }

    @Test
    public void testGetIp() {
        String ip = geoQuery.getIp();
        assertNull(ip);
    }

    @Test
    public void testGetAccuracy() {
        String accuracy = geoQuery.getAccuracy();
        assertNull(accuracy);
    }

    @Test
    public void testSetAccuracy() {
        geoQuery.setAccuracy("high");
        String accuracy = geoQuery.getAccuracy();
        assertEquals("high", accuracy);
    }

    @Test
    public void testAccuracy() {
        GeoQuery result = geoQuery.accuracy("high");
        assertEquals("high", result.getAccuracy());
    }

    @Test
    public void testGetGranularity() {
        String granularity = geoQuery.getGranularity();
        assertNull(granularity);
    }

    @Test
    public void testSetGranularity() {
        geoQuery.setGranularity("city");
        String granularity = geoQuery.getGranularity();
        assertEquals("city", granularity);
    }

    @Test
    public void testGranularity() {
        GeoQuery result = geoQuery.granularity("city");
        assertEquals("city", result.getGranularity());
    }

    @Test
    public void testGetMaxResults() {
        int maxResults = geoQuery.getMaxResults();
        assertEquals(0, maxResults);
    }

    @Test
    public void testSetMaxResults() {
        geoQuery.setMaxResults(10);
        int maxResults = geoQuery.getMaxResults();
        assertEquals(10, maxResults);
    }

    @Test
    public void testMaxResults() {
        GeoQuery result = geoQuery.maxResults(10);
        assertEquals(10, result.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArray() {
        HttpParameter[] parameters = geoQuery.asHttpParameterArray();
        assertEquals(2, parameters.length);
        assertEquals("lat", parameters[0].getName());
        assertEquals("37.7833", parameters[0].getValue());
        assertEquals("long", parameters[1].getName());
        assertEquals("-122.4167", parameters[1].getValue());
    }

    @Test
    public void testAppendParameterStringStringWithNullValue() {
        List<HttpParameter> params = new ArrayList<>();
        geoQuery.appendParameter("key", null, params);
        assertEquals(0, params.size());
    }

    @Test
    public void testAppendParameterStringStringValue() {
        List<HttpParameter> params = new ArrayList<>();
        geoQuery.appendParameter("key", "value", params);
        assertEquals(1, params.size());
        assertEquals("key", params.get(0).getName());
        assertEquals("value", params.get(0).getValue());
    }

    @Test
    public void testAppendParameterStringIntValue() {
        List<HttpParameter> params = new ArrayList<>();
        geoQuery.appendParameter("key", 10, params);
        assertEquals(1, params.size());
        assertEquals("key", params.get(0).getName());
        assertEquals("10", params.get(0).getValue());
    }

    @Test
    public void testEqualsWithSameObject() {
        assertTrue(geoQuery.equals(geoQuery));
    }

    @Test
    public void testEqualsWithNullObject() {
        assertFalse(geoQuery.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClassObject() {
        assertFalse(geoQuery.equals(1));
    }

    @Test
    public void testEqualsWithDifferentMaxResults() {
        GeoQuery other = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        other.setMaxResults(10);
        assertFalse(geoQuery.equals(other));
    }

    @Test
    public void testEqualsWithDifferentAccuracy() {
        GeoQuery other = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        other.setAccuracy("high");
        assertFalse(geoQuery.equals(other));
    }

    @Test
    public void testEqualsWithDifferentGranularity() {
        GeoQuery other = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        other.setGranularity("city");
        assertFalse(geoQuery.equals(other));
    }

    @Test
    public void testEqualsWithDifferentIp() {
        GeoQuery other = new GeoQuery("192.168.0.1");
        assertFalse(geoQuery.equals(other));
    }

    @Test
    public void testEqualsWithDifferentLocation() {
        GeoQuery other = new GeoQuery(new GeoLocation(37.1234, -122.5678));
        assertFalse(geoQuery.equals(other));
    }

    @Test
    public void testEqualsWithSameValues() {
        GeoQuery other = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        assertTrue(geoQuery.equals(other));
    }

    @Test
    public void testHashCode() {
        int hashCode = geoQuery.hashCode();
        assertEquals(1656098413, hashCode);
    }

    @Test
    public void testToString() {
        String result = geoQuery.toString();
        assertEquals("GeoQuery{location=GeoLocation{latitude=37.7833, longitude=-122.4167}, ip='null', accuracy='null', granularity='null', maxResults=0}", result);
    }
}