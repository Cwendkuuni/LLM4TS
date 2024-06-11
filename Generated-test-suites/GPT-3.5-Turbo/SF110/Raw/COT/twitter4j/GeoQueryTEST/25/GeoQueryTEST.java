import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {
  
  @Test
  public void testGetLocation() {
    GeoLocation location = new GeoLocation(37.7749, -122.4194);
    GeoQuery geoQuery = new GeoQuery(location);
    assertEquals(location, geoQuery.getLocation());
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
  }
  
  @Test
  public void testSetAccuracy() {
    GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
    String accuracy = "high";
    geoQuery.setAccuracy(accuracy);
    assertEquals(accuracy, geoQuery.getAccuracy());
  }
  
  @Test
  public void testGetGranularity() {
    GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
    assertNull(geoQuery.getGranularity());
  }
  
  @Test
  public void testSetGranularity() {
    GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
    String granularity = "city";
    geoQuery.setGranularity(granularity);
    assertEquals(granularity, geoQuery.getGranularity());
  }
  
  @Test
  public void testGetMaxResults() {
    GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
    assertEquals(0, geoQuery.getMaxResults());
  }
  
  @Test
  public void testSetMaxResults() {
    GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
    int maxResults = 100;
    geoQuery.setMaxResults(maxResults);
    assertEquals(maxResults, geoQuery.getMaxResults());
  }
  
  @Test
  public void testAsHttpParameterArray() {
    GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
    HttpParameter[] params = geoQuery.asHttpParameterArray();
    assertNotNull(params);
    assertEquals(2, params.length);
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
    GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
    assertNotNull(geoQuery.toString());
  }
}

