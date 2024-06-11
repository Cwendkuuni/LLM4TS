
package twitter4j;

import org.junit.Test;
import static org.junit.Assert.*;

import twitter4j.internal.http.HttpParameter;
import java.util.ArrayList;
import java.util.List;

public class GeoQueryTest {
    @Test
    public void testGetLocation() {
        GeoLocation geoLocation = new GeoLocation(37.7749, -122.4194);
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
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertNull(geoQuery.getAccuracy());
        
        String accuracy = "100m";
        geoQuery.setAccuracy(accuracy);
        
        assertEquals(accuracy, geoQuery.getAccuracy());
    }
    
    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertNull(geoQuery.getGranularity());
        
        String granularity = "city";
        geoQuery.setGranularity(granularity);
        
        assertEquals(granularity, geoQuery.getGranularity());
    }
    
    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertEquals(0, geoQuery.getMaxResults());
        
        int maxResults = 10;
        geoQuery.setMaxResults(maxResults);
        
        assertEquals(maxResults, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertNull(geoQuery.getAccuracy());
        
        String accuracy = "100m";
        geoQuery.accuracy(accuracy);
        
        assertEquals(accuracy, geoQuery.getAccuracy());
    }
    
    @Test
    public void testGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertNull(geoQuery.getGranularity());
        
        String granularity = "city";
        geoQuery.granularity(granularity);
        
        assertEquals(granularity, geoQuery.getGranularity());
    }
    
    @Test
    public void testMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertEquals(0, geoQuery.getMaxResults());
        
        int maxResults = 10;
        geoQuery.maxResults(maxResults);
        
        assertEquals(maxResults, geoQuery.getMaxResults());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoLocation location = geoQuery.getLocation();
        String ip = geoQuery.getIp();
        String accuracy = geoQuery.getAccuracy();
        String granularity = geoQuery.getGranularity();
        int maxResults = geoQuery.getMaxResults();
        
        List<HttpParameter> params = new ArrayList<HttpParameter>();
        if (location != null) {
            // Test lat and long parameters
            params.add(new HttpParameter("lat", String.valueOf(location.getLatitude())));
            params.add(new HttpParameter("long", String.valueOf(location.getLongitude())));
        }
        if (ip != null) {
            // Test ip parameter
            params.add(new HttpParameter("ip", ip));
        }
        if (accuracy != null) {
            // Test accuracy parameter
            params.add(new HttpParameter("accuracy", accuracy));
        }
        if (granularity != null) {
            // Test granularity parameter
            params.add(new HttpParameter("granularity", granularity));
        }
        // Test max_results parameter
        params.add(new HttpParameter("max_results", String.valueOf(maxResults)));
        
        HttpParameter[] expected = new HttpParameter[params.size()];
        assertEquals(params.toArray(expected), geoQuery.asHttpParameterArray());
    }
    
    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertEquals(geoQuery1, geoQuery2);
        
        geoQuery2.accuracy("100m");
        assertNotEquals(geoQuery1, geoQuery2);
    }
    
    @Test
    public void testHashCode() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
        
        geoQuery2.maxResults(10);
        assertNotEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }
    
    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        String expected = "GeoQuery{location=GeoLocation{latitude=37.7749, longitude=-122.4194}, ip='null', accuracy='null', granularity='null', maxResults=0}";
        assertEquals(expected, geoQuery.toString());
    }
}
