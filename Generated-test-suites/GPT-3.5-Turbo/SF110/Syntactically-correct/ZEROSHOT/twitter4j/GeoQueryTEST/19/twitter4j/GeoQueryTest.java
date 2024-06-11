import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeoQueryTest {
    
    private GeoQuery geoQuery;
    
    @Before
    public void setUp() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        geoQuery = new GeoQuery(location);
    }
    
    @Test
    public void testGetLocation() {
        GeoLocation location = geoQuery.getLocation();
        assertEquals(37.7749, location.getLatitude(), 0.0001);
        assertEquals(-122.4194, location.getLongitude(), 0.0001);
    }
    
    @Test
    public void testGetIp() {
        assertNull(geoQuery.getIp());
    }
    
    @Test
    public void testGetAccuracy() {
        assertNull(geoQuery.getAccuracy());
    }
    
    @Test
    public void testSetAccuracy() {
        geoQuery.setAccuracy("city");
        assertEquals("city", geoQuery.getAccuracy());
    }
    
    @Test
    public void testAccuracy() {
        GeoQuery result = geoQuery.accuracy("city");
        assertEquals("city", result.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        assertNull(geoQuery.getGranularity());
    }
    
    @Test
    public void testSetGranularity() {
        geoQuery.setGranularity("neighborhood");
        assertEquals("neighborhood", geoQuery.getGranularity());
    }
    
    @Test
    public void testGranularity() {
        GeoQuery result = geoQuery.granularity("neighborhood");
        assertEquals("neighborhood", result.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        assertEquals(0, geoQuery.getMaxResults());
    }
    
    @Test
    public void testSetMaxResults() {
        geoQuery.setMaxResults(10);
        assertEquals(10, geoQuery.getMaxResults());
    }
    
    @Test
    public void testMaxResults() {
        GeoQuery result = geoQuery.maxResults(10);
        assertEquals(10, result.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        HttpParameter[] expectedParams = new HttpParameter[]{
            new HttpParameter("lat", "37.7749"),
            new HttpParameter("long", "-122.4194")
        };
        HttpParameter[] actualParams = geoQuery.asHttpParameterArray();
        assertArrayEquals(expectedParams, actualParams);
    }
    
    @Test
    public void testAppendParameterWithStringValue() {
        List<HttpParameter> params = new ArrayList<>();
        geoQuery.appendParameter("accuracy", "city", params);
        assertEquals(1, params.size());
        assertEquals(new HttpParameter("accuracy", "city"), params.get(0));
    }
    
    @Test
    public void testAppendParameterWithIntValue() {
        List<HttpParameter> params = new ArrayList<>();
        geoQuery.appendParameter("max_results", 10, params);
        assertEquals(1, params.size());
        assertEquals(new HttpParameter("max_results", "10"), params.get(0));
    }
    
    @Test
    public void testAppendParameterWithDoubleValue() {
        List<HttpParameter> params = new ArrayList<>();
        geoQuery.appendParameter("lat", 37.7749, params);
        assertEquals(1, params.size());
        assertEquals(new HttpParameter("lat", "37.7749"), params.get(0));
    }
    
    @Test
    public void testEqualsSameObject() {
        assertTrue(geoQuery.equals(geoQuery));
    }
    
    @Test
    public void testEqualsNullObject() {
        assertFalse(geoQuery.equals(null));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        assertFalse(geoQuery.equals(new String()));
    }
    
    @Test
    public void testEqualsDifferentMaxResults() {
        GeoQuery other = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        other.setMaxResults(10);
        assertFalse(geoQuery.equals(other));
    }
    
    @Test
    public void testEqualsDifferentAccuracy() {
        GeoQuery other = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        other.setAccuracy("city");
        assertFalse(geoQuery.equals(other));
    }
    
    @Test
    public void testEqualsDifferentGranularity() {
        GeoQuery other = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        other.setGranularity("neighborhood");
        assertFalse(geoQuery.equals(other));
    }
    
    @Test
    public void testEqualsDifferentIp() {
        GeoQuery other = new GeoQuery("192.168.0.1");
        assertFalse(geoQuery.equals(other));
    }
    
    @Test
    public void testEqualsDifferentLocation() {
        GeoQuery other = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertFalse(geoQuery.equals(other));
    }
    
    @Test
    public void testEqualsSameValues() {
        GeoQuery other = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertTrue(geoQuery.equals(other));
    }
    
    @Test
    public void testHashCode() {
        int hashCode = 31 * 37.7749.hashCode() + -122.4194.hashCode();
        hashCode = 31 * hashCode + 0;
        assertEquals(hashCode, geoQuery.hashCode());
    }
    
    @Test
    public void testToString() {
        String expected = "GeoQuery{location=GeoLocation{latitude=37.7749, longitude=-122.4194}, ip='null', accuracy='null', granularity='null', maxResults=0}";
        assertEquals(expected, geoQuery.toString());
    }
}