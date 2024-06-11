import static org.junit.Assert.*;
import org.junit.Test;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(40.7128, -74.0060);
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
        GeoLocation location = new GeoLocation(35.6895, 139.6917);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy(GeoQuery.CITY);
        assertEquals(GeoQuery.CITY, geoQuery.getAccuracy());
    }

    @Test
    public void testSetGranularity() {
        GeoLocation location = new GeoLocation(33.6839, -117.7947);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setGranularity(GeoQuery.NEIGHBORHOOD);
        assertEquals(GeoQuery.NEIGHBORHOOD, geoQuery.getGranularity());
    }

    @Test
    public void testSetMaxResults() {
        GeoLocation location = new GeoLocation(51.5074, -0.1278);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setMaxResults(10);
        assertEquals(10, geoQuery.getMaxResults());
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery1 = new GeoQuery(location1);

        GeoLocation location2 = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        assertEquals(true, geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(52.5200, 13.4050);
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(geoQuery.hashCode(), geoQuery.hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(48.8566, 2.3522);
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals("GeoQuery{location=lat=48.8566,long=2.3522, ip='null', accuracy='null', granularity='null', maxResults=0}", geoQuery.toString());
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(51.5074, -0.1278);
        GeoQuery geoQuery = new GeoQuery(location);
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertNotNull(params);
    }
}