import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testConstructorWithGeoLocation() {
        GeoLocation location = new GeoLocation(0.0, 0.0);
        GeoQuery query = new GeoQuery(location);
        assertEquals(location, query.getLocation());
        assertNull(query.getIp());
    }
    
    @Test
    public void testConstructorWithIp() {
        String ip = "127.0.0.1";
        GeoQuery query = new GeoQuery(ip);
        assertEquals(ip, query.getIp());
        assertNull(query.getLocation());
    }
    
    @Test
    public void testGetAccuracyAndSetAccuracy() {
        GeoQuery query = new GeoQuery(new GeoLocation(0.0, 0.0));
        assertNull(query.getAccuracy());
        String accuracy = "high";
        query.setAccuracy(accuracy);
        assertEquals(accuracy, query.getAccuracy());
    }
    
    @Test
    public void testGetGranularityAndSetGranularity() {
        GeoQuery query = new GeoQuery(new GeoLocation(0.0, 0.0));
        assertNull(query.getGranularity());
        String granularity = "city";
        query.setGranularity(granularity);
        assertEquals(granularity, query.getGranularity());
    }
    
    @Test
    public void testGetMaxResultsAndSetMaxResults() {
        GeoQuery query = new GeoQuery(new GeoLocation(0.0, 0.0));
        assertEquals(0, query.getMaxResults());
        int maxResults = 10;
        query.setMaxResults(maxResults);
        assertEquals(maxResults, query.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        GeoQuery query = new GeoQuery(new GeoLocation(0.0, 0.0));
        query.setAccuracy("high");
        query.setGranularity("city");
        query.setMaxResults(10);
        HttpParameter[] params = query.asHttpParameterArray();
        // Assert the expected values in the params array
    }
    
    @Test
    public void testEquals() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(0.0, 0.0));
        GeoQuery query2 = new GeoQuery(new GeoLocation(0.0, 0.0));
        assertTrue(query1.equals(query2));
    }
    
    @Test
    public void testHashCode() {
        GeoQuery query = new GeoQuery(new GeoLocation(0.0, 0.0));
        // Assert the expected hash code value
    }
    
    @Test
    public void testToString() {
        GeoQuery query = new GeoQuery(new GeoLocation(0.0, 0.0));
        // Assert the expected string representation
    }

}