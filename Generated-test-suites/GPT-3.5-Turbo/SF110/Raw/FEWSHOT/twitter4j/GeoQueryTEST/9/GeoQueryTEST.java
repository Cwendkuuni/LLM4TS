package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class GeoQueryTest {
    
    private GeoQuery geoQuery;
    
    @Before
    public void setup() {
        geoQuery = new GeoQuery(new GeoLocation(10, 20));
    }
    
    @Test
    public void testGetLocation() {
        assertEquals(new GeoLocation(10, 20), geoQuery.getLocation());
    }
    
    @Test
    public void testGetIp() {
        assertNull(geoQuery.getIp());
    }
    
    @Test
    public void testGetAccuracy() {
        geoQuery.setAccuracy("high");
        assertEquals("high", geoQuery.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        geoQuery.setGranularity("neighborhood");
        assertEquals("neighborhood", geoQuery.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        geoQuery.setMaxResults(50);
        assertEquals(50, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAccuracy() {
        assertEquals(geoQuery, geoQuery.accuracy("medium"));
        assertEquals("medium", geoQuery.getAccuracy());
    }
    
    @Test
    public void testGranularity() {
        assertEquals(geoQuery, geoQuery.granularity("city"));
        assertEquals("city", geoQuery.getGranularity());
    }
    
    @Test
    public void testMaxResults() {
        assertEquals(geoQuery, geoQuery.maxResults(100));
        assertEquals(100, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        geoQuery.setGranularity("city");
        geoQuery.setAccuracy("high");
        geoQuery.setMaxResults(50);
        HttpParameter[] parameters = geoQuery.asHttpParameterArray();
        assertEquals(4, parameters.length);
        assertEquals(new HttpParameter("lat", "10.0"), parameters[0]);
        assertEquals(new HttpParameter("long", "20.0"), parameters[1]);
        assertEquals(new HttpParameter("granularity", "city"), parameters[2]);
        assertEquals(new HttpParameter("max_results", "50"), parameters[3]);
    }
    
    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(10, 20));
        geoQuery1.setAccuracy("medium");
        geoQuery1.setGranularity("neighborhood");
        geoQuery1.setMaxResults(50);
        
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(10, 20));
        geoQuery2.setAccuracy("medium");
        geoQuery2.setGranularity("neighborhood");
        geoQuery2.setMaxResults(50);
        
        assertEquals(geoQuery1, geoQuery2);
    }
    
    @Test
    public void testHashCode() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(10, 20));
        geoQuery1.setAccuracy("medium");
        geoQuery1.setGranularity("neighborhood");
        geoQuery1.setMaxResults(50);
        
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(10, 20));
        geoQuery2.setAccuracy("medium");
        geoQuery2.setGranularity("neighborhood");
        geoQuery2.setMaxResults(50);
        
        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }
    
    @Test
    public void testToString() {
        geoQuery.setAccuracy("medium");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(50);
        
        String expected = "GeoQuery{location=GeoLocation{latitude=10.0, longitude=20.0}, ip='null', accuracy='medium', granularity='neighborhood', maxResults=50}";
        assertEquals(expected, geoQuery.toString());
    }
}