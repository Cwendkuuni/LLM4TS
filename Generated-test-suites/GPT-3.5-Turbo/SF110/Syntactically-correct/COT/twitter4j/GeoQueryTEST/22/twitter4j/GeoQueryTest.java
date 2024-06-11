
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {
    
    @Test
    public void testGetLocation() {
        GeoLocation geoLocation = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(geoLocation);
      
        assertEquals(geoLocation, geoQuery.getLocation());
    }
  
    @Test
    public void testGetIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
      
        assertEquals(ip, geoQuery.getIp());
    }
  
    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
      
        assertNull(geoQuery.getAccuracy());
    }
  
    @Test
    public void testSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        String accuracy = "high";
      
        geoQuery.setAccuracy(accuracy);
      
        assertEquals(accuracy, geoQuery.getAccuracy());
    }
  
    @Test
    public void testGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        String granularity = "city";
      
        geoQuery.setGranularity(granularity);
      
        assertEquals(granularity, geoQuery.getGranularity());
    }
  
    @Test
    public void testMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        int maxResults = 10;
      
        geoQuery.setMaxResults(maxResults);
      
        assertEquals(maxResults, geoQuery.getMaxResults());
    }
  
    @Test
    public void testAsHttpParameterArray() {
        GeoLocation geoLocation = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery =  new GeoQuery(geoLocation);
        geoQuery.setGranularity("city");
        geoQuery.setAccuracy("high");
        geoQuery.setMaxResults(10);
      
        HttpParameter[] expectedParams = new HttpParameter[] {
            new HttpParameter("lat", String.valueOf(geoLocation.getLatitude())),
            new HttpParameter("long", String.valueOf(geoLocation.getLongitude())),
            new HttpParameter("accuracy", "high"),
            new HttpParameter("granularity", "city"),
            new HttpParameter("max_results", 10)
        };
      
        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }
  
    @Test
    public void testEquals() {
        GeoLocation geoLocation1 = new GeoLocation(1.0, 2.0);
        GeoLocation geoLocation2 = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery1 = new GeoQuery(geoLocation1);
        GeoQuery geoQuery2 = new GeoQuery(geoLocation2);
      
        assertTrue(geoQuery1.equals(geoQuery2));
    }
  
    @Test
    public void testHashCode() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        int expectedHashCode = Objects.hash(geoQuery.getLocation(), geoQuery.getIp(), geoQuery.getAccuracy(), geoQuery.getGranularity(), geoQuery.getMaxResults());
      
        assertEquals(expectedHashCode, geoQuery.hashCode());
    }
  
    @Test
    public void testToString() {
        GeoLocation geoLocation = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        geoQuery.setGranularity("city");
        geoQuery.setAccuracy("high");
        geoQuery.setMaxResults(10);

        String expectedToString = "GeoQuery{location=" + geoLocation + ", ip='null', accuracy='high', granularity='city', maxResults=10}";

        assertEquals(expectedToString, geoQuery.toString());
    }
}
