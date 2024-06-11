
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation geoLocation = new GeoLocation(37.7833, -122.4167);
        GeoQuery geoQuery = new GeoQuery(geoLocation);

        assertEquals(geoLocation, geoQuery.getLocation());
    }

    @Test
    public void testGetIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        assertEquals(ip, geoQuery.getIp());
    }

    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery.setAccuracy("high");

        assertEquals("high", geoQuery.getAccuracy());
    }

    @Test
    public void testSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery.setAccuracy("high");

        assertEquals("high", geoQuery.getAccuracy());
    }

    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery.setGranularity("neighborhood");

        assertEquals("neighborhood", geoQuery.getGranularity());
    }

    @Test
    public void testSetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery.setGranularity("neighborhood");

        assertEquals("neighborhood", geoQuery.getGranularity());
    }

    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery.setMaxResults(10);

        assertEquals(10, geoQuery.getMaxResults());
    }

    @Test
    public void testSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery.setMaxResults(10);

        assertEquals(10, geoQuery.getMaxResults());
    }

    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7833, -122.4167));

        assertTrue(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testHashCode() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));

        assertNotEquals(0, geoQuery.hashCode());
    }

    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(10);

        String expected = "GeoQuery{location=GeoLocation{latitude=37.7833, longitude=-122.4167}, ip='null', accuracy='high', granularity='neighborhood', maxResults=10}";
        assertEquals(expected, geoQuery.toString());
    }
}
