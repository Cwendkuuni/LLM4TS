import org.junit.*;
import static org.junit.Assert.*;

public class GeoQueryTest {
    private GeoLocation location;
    private GeoQuery geoQuery1;
    private GeoQuery geoQuery2;

    @Before
    public void setUp() {
        location = new GeoLocation(40.7128, -74.0060);
        geoQuery1 = new GeoQuery(location);
        geoQuery2 = new GeoQuery("127.0.0.1");
    }

    @Test
    public void testGetLocation() {
        assertEquals(location, geoQuery1.getLocation());
    }

    @Test
    public void testGetIp() {
        assertEquals("127.0.0.1", geoQuery2.getIp());
    }

    @Test
    public void testAccuracy() {
        geoQuery1.accuracy("high");
        assertEquals("high", geoQuery1.getAccuracy());
    }

    @Test
    public void testGranularity() {
        geoQuery2.granularity("city");
        assertEquals("city", geoQuery2.getGranularity());
    }

    @Test
    public void testMaxResults() {
        geoQuery1.maxResults(10);
        assertEquals(10, geoQuery1.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArray() {
        HttpParameter[] params1 = geoQuery1.asHttpParameterArray();
        HttpParameter[] params2 = geoQuery2.asHttpParameterArray();

        assertEquals(4, params1.length);
        assertEquals(1, params2.length);
    }

    @Test
    public void testEquals() {
        GeoQuery geoQuery3 = new GeoQuery(location);
        assertTrue(geoQuery1.equals(geoQuery3));
    }

    @Test
    public void testHashCode() {
        assertEquals(geoQuery1.hashCode(), geoQuery1.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "GeoQuery{location=GeoLocation{latitude=40.7128, longitude=-74.006}, ip='127.0.0.1', accuracy='null', granularity='null', maxResults=0}";
        assertEquals(expected, geoQuery2.toString());
    }
}