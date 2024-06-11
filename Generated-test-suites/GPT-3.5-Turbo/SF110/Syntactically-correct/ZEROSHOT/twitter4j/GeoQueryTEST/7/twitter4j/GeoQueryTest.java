package twitter4j;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class GeoQueryTest {
    private GeoQuery geoQuery;
    
    @Before
    public void setUp() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        geoQuery = new GeoQuery(location);
    }
    
    @Test
    public void testGetLocation() {
        assertEquals(new GeoLocation(37.7749, -122.4194), geoQuery.getLocation());
    }
    
    @Test
    public void testGetIp() {
        assertEquals(null, geoQuery.getIp());
    }
    
    @Test
    public void testGetAccuracy() {
        assertEquals(null, geoQuery.getAccuracy());
    }
    
    @Test
    public void testSetAndGetAccuracy() {
        geoQuery.setAccuracy("low");
        assertEquals("low", geoQuery.getAccuracy());
    }
    
    @Test
    public void testAccuracy() {
        assertEquals(geoQuery, geoQuery.accuracy("medium"));
        assertEquals("medium", geoQuery.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        assertEquals(null, geoQuery.getGranularity());
    }
    
    @Test
    public void testSetAndGetGranularity() {
        geoQuery.setGranularity("city");
        assertEquals("city", geoQuery.getGranularity());
    }
    
    @Test
    public void testGranularity() {
        assertEquals(geoQuery, geoQuery.granularity("neighborhood"));
        assertEquals("neighborhood", geoQuery.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        assertEquals(0, geoQuery.getMaxResults());
    }
    
    @Test
    public void testSetAndGetMaxResults() {
        geoQuery.setMaxResults(10);
        assertEquals(10, geoQuery.getMaxResults());
    }
    
    @Test
    public void testMaxResults() {
        assertEquals(geoQuery, geoQuery.maxResults(20));
        assertEquals(20, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertEquals(2, params.length);
        assertEquals(new HttpParameter("lat", "37.7749"), params[0]);
        assertEquals(new HttpParameter("long", "-122.4194"), params[1]);
    }
    
    @Test
    public void testAppendParameterWithStringValue() {
        List<HttpParameter> params = new ArrayList<>();
        geoQuery.appendParameter("name", "value", params);
        assertEquals(1, params.size());
        assertEquals(new HttpParameter("name", "value"), params.get(0));
    }
    
    @Test
    public void testAppendParameterWithIntValue() {
        List<HttpParameter> params = new ArrayList<>();
        geoQuery.appendParameter("name", 10, params);
        assertEquals(1, params.size());
        assertEquals(new HttpParameter("name", "10"), params.get(0));
    }
    
    @Test
    public void testAppendParameterWithDoubleValue() {
        List<HttpParameter> params = new ArrayList<>();
        geoQuery.appendParameter("name", 0.5, params);
        assertEquals(1, params.size());
        assertEquals(new HttpParameter("name", "0.5"), params.get(0));
    }
    
    @Test
    public void testEquals() {
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery2.setAccuracy("medium");
        geoQuery2.setGranularity("city");
        geoQuery2.setMaxResults(10);
        assertEquals(true, geoQuery.equals(geoQuery2));
    }
    
    @Test
    public void testNotEquals() {
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertEquals(false, geoQuery.equals(geoQuery2));
    }
    
    @Test
    public void testHashCode() {
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        geoQuery2.setAccuracy("medium");
        geoQuery2.setGranularity("city");
        geoQuery2.setMaxResults(10);
        assertEquals(geoQuery.hashCode(), geoQuery2.hashCode());
    }
    
    @Test
    public void testToString() {
        String expected = "GeoQuery{location=GeoLocation{latitude=37.7749, longitude=-122.4194}, ip='null', accuracy='null', granularity='null', maxResults=0}";
        assertEquals(expected, geoQuery.toString());
    }
}