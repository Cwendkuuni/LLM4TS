import org.junit.*;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);
        
        GeoLocation result = geoQuery.getLocation();
        
        Assert.assertEquals(location, result);
    }
    
    @Test
    public void testGetLocation_NullLocation() {
        GeoQuery geoQuery = new GeoQuery((GeoLocation) null);
        
        GeoLocation result = geoQuery.getLocation();
        
        Assert.assertNull(result);
    }
    
    @Test
    public void testGetIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        
        String result = geoQuery.getIp();
        
        Assert.assertEquals(ip, result);
    }
    
    @Test
    public void testGetAccuracy() {
        String accuracy = "high";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery.setAccuracy(accuracy);
        
        String result = geoQuery.getAccuracy();
        
        Assert.assertEquals(accuracy, result);
    }
    
    @Test
    public void testSetAccuracy() {
        String accuracy = "high";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        
        geoQuery.setAccuracy(accuracy);
        
        Assert.assertEquals(accuracy, geoQuery.getAccuracy());
    }
    
    @Test
    public void testAccuracy() {
        String accuracy = "high";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        
        GeoQuery result = geoQuery.accuracy(accuracy);
        
        Assert.assertEquals(accuracy, result.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        String granularity = "neighborhood";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery.setGranularity(granularity);
        
        String result = geoQuery.getGranularity();
        
        Assert.assertEquals(granularity, result);
    }
    
    @Test
    public void testSetGranularity() {
        String granularity = "neighborhood";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        
        geoQuery.setGranularity(granularity);
        
        Assert.assertEquals(granularity, geoQuery.getGranularity());
    }
    
    @Test
    public void testGranularity() {
        String granularity = "neighborhood";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        
        GeoQuery result = geoQuery.granularity(granularity);
        
        Assert.assertEquals(granularity, result.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        int maxResults = 10;
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery.setMaxResults(maxResults);
        
        int result = geoQuery.getMaxResults();
        
        Assert.assertEquals(maxResults, result);
    }
    
    @Test
    public void testSetMaxResults() {
        int maxResults = 10;
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        
        geoQuery.setMaxResults(maxResults);
        
        Assert.assertEquals(maxResults, geoQuery.getMaxResults());
    }
    
    @Test
    public void testMaxResults() {
        int maxResults = 10;
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        
        GeoQuery result = geoQuery.maxResults(maxResults);
        
        Assert.assertEquals(maxResults, result.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);
        String accuracy = "high";
        String granularity = "neighborhood";
        int maxResults = 10;
        
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);
        
        HttpParameter[] expected = new HttpParameter[5];
        expected[0] = new HttpParameter("lat", "1.0");
        expected[1] = new HttpParameter("long", "2.0");
        expected[2] = new HttpParameter("accuracy", accuracy);
        expected[3] = new HttpParameter("granularity", granularity);
        expected[4] = new HttpParameter("max_results", String.valueOf(maxResults));
        
        HttpParameter[] result = geoQuery.asHttpParameterArray();
        
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testAsHttpParameterArray_NullLocation() {
        GeoQuery geoQuery = new GeoQuery((GeoLocation) null);
        String accuracy = "high";
        String granularity = "neighborhood";
        int maxResults = 10;
        
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);
        
        HttpParameter[] expected = new HttpParameter[3];
        expected[0] = new HttpParameter("accuracy", accuracy);
        expected[1] = new HttpParameter("granularity", granularity);
        expected[2] = new HttpParameter("max_results", String.valueOf(maxResults));
        
        HttpParameter[] result = geoQuery.asHttpParameterArray();
        
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testEquals_SameObject() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        
        boolean result = geoQuery.equals(geoQuery);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEquals_NullObject() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        
        boolean result = geoQuery.equals(null);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEquals_DifferentClass() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        
        boolean result = geoQuery.equals(new Object());
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEquals_SameProperties() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.0, 2.0));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.0, 2.0));
        
        boolean result = geoQuery1.equals(geoQuery2);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEquals_DifferentLocation() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.0, 2.0));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(3.0, 4.0));
        
        boolean result = geoQuery1.equals(geoQuery2);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEquals_DifferentIp() {
        GeoQuery geoQuery1 = new GeoQuery("192.168.0.1");
        GeoQuery geoQuery2 = new GeoQuery("192.168.0.2");
        
        boolean result = geoQuery1.equals(geoQuery2);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEquals_DifferentAccuracy() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.0, 2.0));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery1.setAccuracy("high");
        geoQuery2.setAccuracy("low");
        
        boolean result = geoQuery1.equals(geoQuery2);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEquals_DifferentGranularity() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.0, 2.0));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery1.setGranularity("neighborhood");
        geoQuery2.setGranularity("city");
        
        boolean result = geoQuery1.equals(geoQuery2);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEquals_DifferentMaxResults() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.0, 2.0));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery1.setMaxResults(10);
        geoQuery2.setMaxResults(20);
        
        boolean result = geoQuery1.equals(geoQuery2);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEquals_NullAccuracy() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.0, 2.0));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery1.setAccuracy(null);
        geoQuery2.setAccuracy(null);
        
        boolean result = geoQuery1.equals(geoQuery2);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEquals_NullGranularity() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.0, 2.0));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery1.setGranularity(null);
        geoQuery2.setGranularity(null);
        
        boolean result = geoQuery1.equals(geoQuery2);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEquals_NullIp() {
        GeoQuery geoQuery1 = new GeoQuery("192.168.0.1");
        GeoQuery geoQuery2 = new GeoQuery("192.168.0.1");
        geoQuery1.setIp(null);
        geoQuery2.setIp(null);
        
        boolean result = geoQuery1.equals(geoQuery2);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEquals_NullLocation() {
        GeoQuery geoQuery1 = new GeoQuery((GeoLocation) null);
        GeoQuery geoQuery2 = new GeoQuery((GeoLocation) null);
        
        boolean result = geoQuery1.equals(geoQuery2);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testHashCode() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(10);
        
        int expected = 1012097491;
        int result = geoQuery.hashCode();
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(10);
        
        String expected = "GeoQuery{location=GeoLocation{latitude=1.0, longitude=2.0}, ip='null', accuracy='high', granularity='neighborhood', maxResults=10}";
        String result = geoQuery.toString();
        
        Assert.assertEquals(expected, result);
    }
}