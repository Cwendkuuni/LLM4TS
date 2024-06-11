import static org.junit.Assert.*;
import org.junit.Test;

public class GeoQueryTest {
  
  @Test
  public void testConstructorWithGeoLocation() {
    GeoLocation location = new GeoLocation(37.7749, -122.4194);
    GeoQuery geoQuery = new GeoQuery(location);
    assertEquals(location, geoQuery.getLocation());
    assertNull(geoQuery.getIp());
  }
  
  @Test
  public void testConstructorWithIp() {
    String ip = "192.168.1.1";
    GeoQuery geoQuery = new GeoQuery(ip);
    assertEquals(ip, geoQuery.getIp());
    assertNull(geoQuery.getLocation());
  }
  
  @Test
  public void testAccuracy() {
    GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
    String accuracy = "100m";
    geoQuery.setAccuracy(accuracy);
    assertEquals(accuracy, geoQuery.getAccuracy());
  }
  
  @Test
  public void testGranularity() {
    GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
    String granularity = "neighborhood";
    geoQuery.setGranularity(granularity);
    assertEquals(granularity, geoQuery.getGranularity());
  }
  
  @Test
  public void testMaxResults() {
    GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
    int maxResults = 10;
    geoQuery.setMaxResults(maxResults);
    assertEquals(maxResults, geoQuery.getMaxResults());
  }
  
  @Test
  public void testEquals() {
    GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
    GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7749, -122.4194));
    assertTrue(geoQuery1.equals(geoQuery2));
  }
  
  @Test
  public void testHashCode() {
    GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
    assertNotNull(geoQuery.hashCode());
  }
  
  @Test
  public void testToString() {
    GeoLocation location = new GeoLocation(37.7749, -122.4194);
    GeoQuery geoQuery = new GeoQuery(location);
    String expected = "GeoQuery{location=" + location + ", ip='null', accuracy='null', granularity='null', maxResults=0}";
    assertEquals(expected, geoQuery.toString());
  }
  
}