import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    private GeoQuery geoQuery1;
    private GeoQuery geoQuery2;
    private GeoQuery geoQuery3;

    @Before
    public void setUp() {
        geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery2 = new GeoQuery("192.168.0.1");
        geoQuery3 = new GeoQuery("invalid ip");
    }

    @Test
    public void testConstructor1() {
        assertNotNull(geoQuery1.getLocation());
        assertNull(geoQuery1.getIp());
        assertNull(geoQuery1.getAccuracy());
        assertNull(geoQuery1.getGranularity());
        assertEquals(0, geoQuery1.getMaxResults());
    }

    @Test
    public void testConstructor2() {
        assertNull(geoQuery2.getLocation());
        assertEquals("192.168.0.1", geoQuery2.getIp());
        assertNull(geoQuery2.getAccuracy());
        assertNull(geoQuery2.getGranularity());
        assertEquals(0, geoQuery2.getMaxResults());
    }

    @Test
    public void testGetSetAccuracy() {
        assertNull(geoQuery1.getAccuracy());
        geoQuery1.setAccuracy("high");
        assertEquals("high", geoQuery1.getAccuracy());
    }

    @Test
    public void testGetSetGranularity() {
        assertNull(geoQuery1.getGranularity());
        geoQuery1.setGranularity("low");
        assertEquals("low", geoQuery1.getGranularity());
    }

    @Test
    public void testGetSetMaxResults() {
        assertEquals(0, geoQuery1.getMaxResults());
        geoQuery1.setMaxResults(10);
        assertEquals(10, geoQuery1.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArrayWithLocation() {
        geoQuery1.setAccuracy("high");
        geoQuery1.setGranularity("low");
        geoQuery1.setMaxResults(10);

        HttpParameter[] params = geoQuery1.asHttpParameterArray();

        assertEquals(5, params.length);
        assertEquals("lat", params[0].getName());
        assertEquals("37.7749", params[0].getValue());
        assertEquals("long", params[1].getName());
        assertEquals("-122.4194", params[1].getValue());
        assertEquals("accuracy", params[2].getName());
        assertEquals("high", params[2].getValue());
        assertEquals("granularity", params[3].getName());
        assertEquals("low", params[3].getValue());
        assertEquals("max_results", params[4].getName());
        assertEquals("10", params[4].getValue());
    }

    @Test
    public void testAsHttpParameterArrayWithIp() {
        geoQuery2.setAccuracy("medium");
        geoQuery2.setGranularity("medium");
        geoQuery2.setMaxResults(5);

        HttpParameter[] params = geoQuery2.asHttpParameterArray();

        assertEquals(4, params.length);
        assertEquals("ip", params[0].getName());
        assertEquals("192.168.0.1", params[0].getValue());
        assertEquals("accuracy", params[1].getName());
        assertEquals("medium", params[1].getValue());
        assertEquals("granularity", params[2].getName());
        assertEquals("medium", params[2].getValue());
        assertEquals("max_results", params[3].getName());
        assertEquals("5", params[3].getValue());
    }

    @Test
    public void testEqualsWithEqualObjects() {
        assertTrue(geoQuery1.equals(geoQuery1));
    }

    @Test
    public void testEqualsWithDifferentObjects() {
        assertFalse(geoQuery1.equals(null));
        assertFalse(geoQuery1.equals("test"));
        assertFalse(geoQuery1.equals(geoQuery2));
        assertFalse(geoQuery1.equals(geoQuery3));
    }

    @Test
    public void testHashCode() {
        assertEquals(geoQuery1.hashCode(), geoQuery1.hashCode());
        assertNotEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "GeoQuery{location=" + geoQuery1.getLocation() + ", ip='" + geoQuery1.getIp() + '\'' +
                ", accuracy='" + geoQuery1.getAccuracy() + '\'' + ", granularity='" + geoQuery1.getGranularity() +
                '\'' + ", maxResults=" + geoQuery1.getMaxResults() + '}';
        assertEquals(expected, geoQuery1.toString());
    }

}