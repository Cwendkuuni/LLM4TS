
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GeoQueryTest {

    @Test
    public void testConstructorWithLocation() {
        GeoLocation location = new GeoLocation(40.7128, -74.0060);
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(location, geoQuery.getLocation());
        assertNull(geoQuery.getIp());
        assertNull(geoQuery.getAccuracy());
        assertNull(geoQuery.getGranularity());
        assertEquals(0, geoQuery.getMaxResults());
    }

    @Test
    public void testConstructorWithIP() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        assertNull(geoQuery.getLocation());
        assertEquals(ip, geoQuery.getIp());
        assertNull(geoQuery.getAccuracy());
        assertNull(geoQuery.getGranularity());
        assertEquals(0, geoQuery.getMaxResults());
    }

    @Test
    public void testGettersAndSetters() {
        GeoLocation location = new GeoLocation(40.7128, -74.0060);
        String ip = "192.168.0.1";

        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(10);

        assertEquals(location, geoQuery.getLocation());
        assertNull(geoQuery.getIp());
        assertEquals("high", geoQuery.getAccuracy());
        assertEquals("city", geoQuery.getGranularity());
        assertEquals(10, geoQuery.getMaxResults());

        geoQuery = new GeoQuery(ip);
        geoQuery.setAccuracy("low");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(5);

        assertNull(geoQuery.getLocation());
        assertEquals(ip, geoQuery.getIp());
        assertEquals("low", geoQuery.getAccuracy());
        assertEquals("neighborhood", geoQuery.getGranularity());
        assertEquals(5, geoQuery.getMaxResults());
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(40.7128, -74.0060);
        GeoLocation location2 = new GeoLocation(40.7128, -74.0060);
        String ip1 = "192.168.0.1";
        String ip2 = "192.168.0.1";

        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        GeoQuery geoQuery3 = new GeoQuery(ip1);
        GeoQuery geoQuery4 = new GeoQuery(ip2);

        assertEquals(geoQuery1, geoQuery2);
        assertEquals(geoQuery3, geoQuery4);
        assertNotEquals(geoQuery1, geoQuery3);
        assertNotEquals(geoQuery2, geoQuery4);
    }

    @Test
    public void testHashCode() {
        GeoLocation location1 = new GeoLocation(40.7128, -74.0060);
        GeoLocation location2 = new GeoLocation(40.7128, -74.0060);
        String ip1 = "192.168.0.1";
        String ip2 = "192.168.0.1";

        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        GeoQuery geoQuery3 = new GeoQuery(ip1);
        GeoQuery geoQuery4 = new GeoQuery(ip2);

        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
        assertEquals(geoQuery3.hashCode(), geoQuery4.hashCode());
        assertNotEquals(geoQuery1.hashCode(), geoQuery3.hashCode());
        assertNotEquals(geoQuery2.hashCode(), geoQuery4.hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(40.7128, -74.0060);
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals("GeoQuery{location=" + location + ", ip='null', accuracy='null', granularity='null', maxResults=0}", geoQuery.toString());

        String ip = "192.168.0.1";
        geoQuery = new GeoQuery(ip);
        assertEquals("GeoQuery{location=null, ip='" + ip + "', accuracy='null', granularity='null', maxResults=0}", geoQuery.toString());
    }
}
