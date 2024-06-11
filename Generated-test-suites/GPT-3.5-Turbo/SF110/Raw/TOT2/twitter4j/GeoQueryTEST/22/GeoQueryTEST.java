import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation("37.7749", "-122.4194");
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(location, geoQuery.getLocation());
    }

    @Test
    public void testGetIp() {
        String ip = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        assertEquals(ip, geoQuery.getIp());
    }

    @Test
    public void testSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation("37.7749", "-122.4194"));
        geoQuery.setAccuracy("exact");
        assertEquals("exact", geoQuery.getAccuracy());
    }

    @Test
    public void testSetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation("37.7749", "-122.4194"));
        geoQuery.setGranularity("city");
        assertEquals("city", geoQuery.getGranularity());
    }

    @Test
    public void testSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation("37.7749", "-122.4194"));
        geoQuery.setMaxResults(10);
        assertEquals(10, geoQuery.getMaxResults());
    }

    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation("37.7749", "-122.4194"));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation("37.7749", "-122.4194"));
        assertTrue(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testHashCode() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation("37.7749", "-122.4194"));
        assertNotNull(geoQuery.hashCode());
    }

    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation("37.7749", "-122.4194"));
        assertTrue(geoQuery.toString().contains("location=GeoLocation{latitude='37.7749', longitude='-122.4194', ip='null'}"));
    }

}