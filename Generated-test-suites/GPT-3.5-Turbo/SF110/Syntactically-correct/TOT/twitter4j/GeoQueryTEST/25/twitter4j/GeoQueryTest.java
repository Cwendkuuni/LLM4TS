import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testConstructorWithGeoLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
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
    public void testGetAccuracy() {
        GeoQuery query = new GeoQuery("127.0.0.1");
        query.setAccuracy("high");
        
        assertEquals("high", query.getAccuracy());
    }
    
    @Test
    public void testSetAccuracy() {
        GeoQuery query = new GeoQuery(new GeoLocation(1.0, 2.0));
        query.setAccuracy("low");
        
        assertEquals("low", query.getAccuracy());
    }
    
    @Test
    public void testAccuracyMethod() {
        GeoQuery query = new GeoQuery(new GeoLocation(1.0, 2.0));
        GeoQuery result = query.accuracy("medium");
        
        assertEquals(query, result);
        assertEquals("medium", query.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        GeoQuery query = new GeoQuery("127.0.0.1");
        query.setGranularity("neighborhood");
        
        assertEquals("neighborhood", query.getGranularity());
    }
    
    @Test
    public void testSetGranularity() {
        GeoQuery query = new GeoQuery(new GeoLocation(1.0, 2.0));
        query.setGranularity("city");
        
        assertEquals("city", query.getGranularity());
    }
    
    @Test
    public void testGranularityMethod() {
        GeoQuery query = new GeoQuery(new GeoLocation(1.0, 2.0));
        GeoQuery result = query.granularity("neighborhood");
        
        assertEquals(query, result);
        assertEquals("neighborhood", query.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        GeoQuery query = new GeoQuery("127.0.0.1");
        query.setMaxResults(10);
        
        assertEquals(10, query.getMaxResults());
    }
    
    @Test
    public void testSetMaxResults() {
        GeoQuery query = new GeoQuery(new GeoLocation(1.0, 2.0));
        query.setMaxResults(5);
        
        assertEquals(5, query.getMaxResults());
    }
    
    @Test
    public void testMaxResultsMethod() {
        GeoQuery query = new GeoQuery(new GeoLocation(1.0, 2.0));
        GeoQuery result = query.maxResults(3);
        
        assertEquals(query, result);
        assertEquals(3, query.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(1.0, 2.0));
        GeoQuery query2 = new GeoQuery("127.0.0.1");
        
        query1.setAccuracy("high");
        query1.setGranularity("neighborhood");
        query1.setMaxResults(10);
        
        query2.setAccuracy("low");
        query2.setGranularity("city");
        query2.setMaxResults(5);
        
        HttpParameter[] params1 = {
            new HttpParameter("lat", "1.0"),
            new HttpParameter("long", "2.0"),
            new HttpParameter("accuracy", "high"),
            new HttpParameter("granularity", "neighborhood"),
            new HttpParameter("max_results", String.valueOf(10))
        };
        
        HttpParameter[] params2 = {
            new HttpParameter("ip", "127.0.0.1"),
            new HttpParameter("accuracy", "low"),
            new HttpParameter("granularity", "city"),
            new HttpParameter("max_results", String.valueOf(5))
        };
        
        assertArrayEquals(params1, query1.asHttpParameterArray());
        assertArrayEquals(params2, query2.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        
        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location1);
        GeoQuery query3 = new GeoQuery(location2);
        
        query1.setAccuracy("high");
        query1.setGranularity("neighborhood");
        query1.setMaxResults(10);
        
        query2.setAccuracy("high");
        query2.setGranularity("neighborhood");
        query2.setMaxResults(10);
        
        query3.setAccuracy("low");
        query3.setGranularity("city");
        query3.setMaxResults(5);
        
        assertTrue(query1.equals(query2));
        assertFalse(query1.equals(query3));
    }
    
    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        
        GeoQuery query1 = new GeoQuery(location);
        GeoQuery query2 = new GeoQuery(location);
        
        query1.setAccuracy("high");
        query1.setGranularity("neighborhood");
        query1.setMaxResults(10);
        
        query2.setAccuracy("high");
        query2.setGranularity("neighborhood");
        query2.setMaxResults(10);
        
        assertEquals(query1.hashCode(), query2.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery query = new GeoQuery(location);
        
        query.setAccuracy("high");
        query.setGranularity("neighborhood");
        query.setMaxResults(10);
        
        String expected = "GeoQuery{location=GeoLocation{latitude=1.0, longitude=2.0}, ip='null', accuracy='high', granularity='neighborhood', maxResults=10}";
        
        assertEquals(expected, query.toString());
    }
}