import static org.junit.Assert.*;
import org.junit.Test;

public class GeoQueryTest {
    @Test
    public void testLocationConstructor() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(location, geoQuery.getLocation());
        assertNull(geoQuery.getIp());
    }
    
    @Test
    public void testIPConstructor() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        assertNull(geoQuery.getLocation());
        assertEquals(ip, geoQuery.getIp());
    }
    
    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        assertNull(geoQuery.getAccuracy());
    }
    
    @Test
    public void testSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery.setAccuracy("high");
        assertEquals("high", geoQuery.getAccuracy());
    }
    
    @Test
    public void testAccuracyMethod() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        GeoQuery returnedGeoQuery = geoQuery.accuracy("low");
        assertEquals("low", geoQuery.getAccuracy());
        assertEquals(geoQuery, returnedGeoQuery);
    }
    
    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        assertNull(geoQuery.getGranularity());
    }
    
    @Test
    public void testSetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery.setGranularity("neighborhood");
        assertEquals("neighborhood", geoQuery.getGranularity());
    }
    
    @Test
    public void testGranularityMethod() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        GeoQuery returnedGeoQuery = geoQuery.granularity("city");
        assertEquals("city", geoQuery.getGranularity());
        assertEquals(geoQuery, returnedGeoQuery);
    }
    
    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        assertEquals(0, geoQuery.getMaxResults());
    }
    
    @Test
    public void testSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery.setMaxResults(50);
        assertEquals(50, geoQuery.getMaxResults());
    }
    
    @Test
    public void testMaxResultsMethod() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        GeoQuery returnedGeoQuery = geoQuery.maxResults(100);
        assertEquals(100, geoQuery.getMaxResults());
        assertEquals(geoQuery, returnedGeoQuery);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(50);
        
        HttpParameter[] expectedParams = new HttpParameter[5];
        expectedParams[0] = new HttpParameter("lat", "10.0");
        expectedParams[1] = new HttpParameter("long", "20.0");
        expectedParams[2] = new HttpParameter("accuracy", "high");
        expectedParams[3] = new HttpParameter("granularity", "neighborhood");
        expectedParams[4] = new HttpParameter("max_results", "50");
        
        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }
    
    @Test
    public void testAppendParameterString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        List<HttpParameter> params = new ArrayList<>();
        
        geoQuery.appendParameter("param", "value", params);
        assertEquals(1, params.size());
        assertEquals(new HttpParameter("param", "value"), params.get(0));
    }
    
    @Test
    public void testAppendParameterInt() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        List<HttpParameter> params = new ArrayList<>();
        
        geoQuery.appendParameter("param", 100, params);
        assertEquals(1, params.size());
        assertEquals(new HttpParameter("param", "100"), params.get(0));
    }
    
    @Test
    public void testAppendParameterDouble() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        List<HttpParameter> params = new ArrayList<>();
        
        geoQuery.appendParameter("param", 10.5, params);
        assertEquals(1, params.size());
        assertEquals(new HttpParameter("param", "10.5"), params.get(0));
    }
    
    @Test
    public void testEqualsSameObject() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery geoQuery = new GeoQuery(location);
        
        assertTrue(geoQuery.equals(geoQuery));
    }
    
    @Test
    public void testEqualsNullObject() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery geoQuery = new GeoQuery(location);
        
        assertFalse(geoQuery.equals(null));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery geoQuery = new GeoQuery(location);
        
        assertFalse(geoQuery.equals(location));
    }
    
    @Test
    public void testEqualsDifferentLocation() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(10.0, 20.0));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(30.0, 40.0));
        
        assertFalse(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testEqualsDifferentIp() {
        GeoQuery geoQuery1 = new GeoQuery("192.168.0.1");
        GeoQuery geoQuery2 = new GeoQuery("10.0.0.1");
        
        assertFalse(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testEqualsDifferentAccuracy() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery1.setAccuracy("high");
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery2.setAccuracy("low");
        
        assertFalse(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testEqualsDifferentGranularity() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery1.setGranularity("neighborhood");
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery2.setGranularity("city");
        
        assertFalse(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testEqualsDifferentMaxResults() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery1.setMaxResults(50);
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery2.setMaxResults(100);
        
        assertFalse(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testEqualsSameValues() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery1.setAccuracy("high");
        geoQuery1.setGranularity("neighborhood");
        geoQuery1.setMaxResults(50);
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery2.setAccuracy("high");
        geoQuery2.setGranularity("neighborhood");
        geoQuery2.setMaxResults(50);
        
        assertTrue(geoQuery1.equals(geoQuery2));
    }
    
    @Test
    public void testHashCode() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(50);
        
        int expectedHashCode = Objects.hash(geoQuery.getLocation(), geoQuery.getIp(), geoQuery.getAccuracy(), geoQuery.getGranularity(), geoQuery.getMaxResults());
        
        assertEquals(expectedHashCode, geoQuery.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(50);
        
        String expectedToString = "GeoQuery{location=GeoLocation{latitude=10.0, longitude=20.0}, ip='null', accuracy='high', granularity='neighborhood', maxResults=50}";
        
        assertEquals(expectedToString, geoQuery.toString());
    }
}