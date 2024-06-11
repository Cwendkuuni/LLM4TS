import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import twitter4j.GeoLocation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GeoQueryTest {

    private GeoQuery geoQuery;

    @BeforeEach
    public void setUp() {
        geoQuery = new GeoQuery(new GeoLocation(10.0, 10.0));
    }

    @Test
    public void testGetLocation() {
        assertEquals(new GeoLocation(10.0, 10.0), geoQuery.getLocation());
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
        geoQuery.setAccuracy("high");
        assertEquals("high", geoQuery.getAccuracy());
    }

    @Test
    public void testGranularity() {
        assertNull(geoQuery.getGranularity());
    }

    @Test
    public void testSetGranularity() {
        geoQuery.setGranularity("city");
        assertEquals("city", geoQuery.getGranularity());
    }

    @Test
    public void testMaxResults() {
        assertEquals(0, geoQuery.getMaxResults());
    }

    @Test
    public void testSetMaxResults() {
        geoQuery.setMaxResults(100);
        assertEquals(100, geoQuery.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArray() {
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertEquals(2, params.length);
        assertEquals("lat", params[0].getName());
        assertEquals("10.0", params[0].getValue());
        assertEquals("long", params[1].getName());
        assertEquals("10.0", params[1].getValue());
    }

    @Test
    public void testAppendParameterString() {
        HttpParameter[] params = new HttpParameter[3];
        geoQuery.appendParameter("test", "value", Arrays.asList(params));
        assertEquals(1, params.length);
        assertEquals("test", params[0].getName());
        assertEquals("value", params[0].getValue());
    }

    @Test
    public void testAppendParameterInt() {
        HttpParameter[] params = new HttpParameter[3];
        geoQuery.appendParameter("test", 10, Arrays.asList(params));
        assertEquals(1, params.length);
        assertEquals("test", params[0].getName());
        assertEquals("10", params[0].getValue());
    }

    @Test
    public void testAppendParameterDouble() {
        HttpParameter[] params = new HttpParameter[3];
        geoQuery.appendParameter("test", 10.0, Arrays.asList(params));
        assertEquals(1, params.length);
        assertEquals("test", params[0].getName());
        assertEquals("10.0", params[0].getValue());
    }

    @Test
    public void testEquals() {
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(10.0, 10.0));
        assertEquals(geoQuery, geoQuery2);
    }

    @Test
    public void testHashCode() {
        int hashCode = geoQuery.hashCode();
        assertEquals(hashCode, geoQuery.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "GeoQuery{location=GeoLocation{latitude=10.0, longitude=10.0}, ip='null', accuracy='null', granularity='null', maxResults=0}";
        assertEquals(expected, geoQuery.toString());
    }
}