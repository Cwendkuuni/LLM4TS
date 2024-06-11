import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {
    
    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation();
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(location, geoQuery.getLocation());
    }
    
    @Test
    public void testGetIp() {
        String ip = "127.0.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        assertEquals(ip, geoQuery.getIp());
    }
    
    @Test
    public void testSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(null);
        geoQuery.setAccuracy("high");
        assertEquals("high", geoQuery.getAccuracy());
    }
    
    @Test
    public void testSetGranularity() {
        GeoQuery geoQuery = new GeoQuery(null);
        geoQuery.setGranularity("low");
        assertEquals("low", geoQuery.getGranularity());
    }
    
    @Test
    public void testSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(null);
        geoQuery.setMaxResults(10);
        assertEquals(10, geoQuery.getMaxResults());
    }
    
    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation());
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation());
        assertEquals(true, geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testHashCode() {
        GeoQuery geoQuery = new GeoQuery(null);
        assertEquals(0, geoQuery.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(37.774929, -122.419416);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("low");
        geoQuery.setMaxResults(10);
        assertEquals("GeoQuery{location=GeoLocation{latitude=37.774929, longitude=-122.419416}, ip='null', accuracy='high', granularity='low', maxResults=10}", geoQuery.toString());
    }
    
}