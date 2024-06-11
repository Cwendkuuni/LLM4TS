
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    private GeoQuery geoQuery;

    @Before
    public void setUp() {
        this.geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        this.geoQuery.setMaxResults(10);
        this.geoQuery.setAccuracy(GeoQuery.NEIGHBORHOOD);
        this.geoQuery.setGranularity(GeoQuery.CITY);
    }

    @Test
    public void testConstructorWithLocation() {
        assertEquals(37.7749, geoQuery.getLocation().getLatitude(), 0.001);
        assertEquals(-122.4194, geoQuery.getLocation().getLongitude(), 0.001);
        assertNull(geoQuery.getIp());
    }

    @Test
    public void testConstructorWithIp() {
        this.geoQuery = new GeoQuery("127.0.0.1");
        assertNull(geoQuery.getLocation());
        assertEquals("127.0.0.1", geoQuery.getIp());
    }

    @Test
    public void testGetAccuracy() {
        assertEquals(GeoQuery.NEIGHBORHOOD, geoQuery.getAccuracy());
    }

    @Test
    public void testSetAccuracy() {
        geoQuery.setAccuracy(GeoQuery.CITY);
        assertEquals(GeoQuery.CITY, geoQuery.getAccuracy());
    }

    @Test
    public void testGetGranularity() {
        assertEquals(GeoQuery.CITY, geoQuery.getGranularity());
    }

    @Test
    public void testSetGranularity() {
        geoQuery.setGranularity(GeoQuery.NEIGHBORHOOD);
        assertEquals(GeoQuery.NEIGHBORHOOD, geoQuery.getGranularity());
    }

    @Test
    public void testGetMaxResults() {
        assertEquals(10, geoQuery.getMaxResults());
    }

    @Test
    public void testSetMaxResults() {
        geoQuery.setMaxResults(5);
        assertEquals(5, geoQuery.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArrayWithLocation() {
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertEquals(5, params.length);
        assertEquals("lat", params[0].getName());
        assertEquals("37.7749", params[0].getValue());
        assertEquals("long", params[1].getName());
        assertEquals("-122.4194", params[1].getValue());
        assertNull(params[2]);
        assertEquals("granularity", params[3].getName());
        assertEquals("city", params[3].getValue());
        assertEquals("max_results", params[4].getName());
        assertEquals("10", params[4].getValue());
    }

    @Test
    public void testAsHttpParameterArrayWithIp() {
        this.geoQuery = new GeoQuery("127.0.0.1");
        geoQuery.setAccuracy(GeoQuery.NEIGHBORHOOD);
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertEquals(2, params.length);
        assertNull(params[0]);
        assertEquals("ip", params[1].getName());
        assertEquals("127.0.0.1", params[1].getValue());
    }
    
    @Test
    public void testEqualsSameObject() {
        assertTrue(geoQuery.equals(geoQuery));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        assertFalse(geoQuery.equals(new Object()));
    }
    
    @Test
    public void testEqualsNull() {
        assertFalse(geoQuery.equals(null));
    }
    
    @Test
    public void testEqualsDifferentLocation() {
        assertFalse(geoQuery.equals(new GeoQuery(new GeoLocation(0, 0))));
    }
    
    @Test
    public void testEqualsDifferentIp() {
        assertFalse(geoQuery.equals(new GeoQuery("127.0.0.2")));
    }
    
    @Test
    public void testEqualsDifferentAccuracy() {
        GeoQuery other = new GeoQuery(new GeoLocation(37.7749, -122.4194));
	other.setAccuracy(GeoQuery.CITY);
        assertFalse(geoQuery.equals(other));
    }
    
    @Test
    public void testEqualsDifferentGranularity() {
        GeoQuery other = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        other.setGranularity(GeoQuery.NEIGHBORHOOD);
        assertFalse(geoQuery.equals(other));
    }
    
    @Test
    public void testEqualsDifferentMaxResults() {
        GeoQuery other = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        other.setMaxResults(5);
        assertFalse(geoQuery.equals(other));
    }
    
    @Test
    public void testHashCodeSameObject() {
        assertEquals(geoQuery.hashCode(), geoQuery.hashCode());
    }
    
    @Test
    public void testHashCodeDifferentLocation() {
        assertNotEquals(geoQuery.hashCode(), new GeoQuery(new GeoLocation(0, 0)).hashCode());
    }

    @Test
    public void testToString() {
        String expected = "GeoQuery{location=37.7749, -122.4194, ip='null', accuracy='neighborhood', granularity='city', maxResults=10}";
        assertEquals(expected, geoQuery.toString());
    }
}

