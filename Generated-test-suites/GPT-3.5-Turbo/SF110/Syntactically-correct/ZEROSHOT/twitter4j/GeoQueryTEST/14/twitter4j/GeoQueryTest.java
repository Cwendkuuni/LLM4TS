import static org.junit.Assert.*;
import org.junit.Test;

public class GeoQueryTest {

  @Test
  public void testGetLocation() {
    GeoQuery geoQuery = new GeoQuery(new GeoLocation(40.7128, -74.0060));
    assertEquals(geoQuery.getLocation(), new GeoLocation(40.7128, -74.0060));
  }
  
  @Test
  public void testGetIp() {
    GeoQuery geoQuery = new GeoQuery("127.0.0.1");
    assertEquals(geoQuery.getIp(), "127.0.0.1");
  }
  
  @Test
  public void testGetAccuracy() {
    GeoQuery geoQuery = new GeoQuery(new GeoLocation(40.7128, -74.0060));
    geoQuery.setAccuracy("high");
    assertEquals(geoQuery.getAccuracy(), "high");
  }
  
  @Test
  public void testGetGranularity() {
    GeoQuery geoQuery = new GeoQuery(new GeoLocation(40.7128, -74.0060));
    geoQuery.setGranularity("city");
    assertEquals(geoQuery.getGranularity(), "city");
  }
  
  @Test
  public void testGetMaxResults() {
    GeoQuery geoQuery = new GeoQuery(new GeoLocation(40.7128, -74.0060));
    geoQuery.setMaxResults(10);
    assertEquals(geoQuery.getMaxResults(), 10);
  }
  
  @Test
  public void testAccuracy() {
    GeoQuery geoQuery = new GeoQuery(new GeoLocation(40.7128, -74.0060));
    geoQuery.accuracy("high");
    assertEquals(geoQuery.getAccuracy(), "high");
  }
  
  @Test
  public void testGranularity() {
    GeoQuery geoQuery = new GeoQuery(new GeoLocation(40.7128, -74.0060));
    geoQuery.granularity("city");
    assertEquals(geoQuery.getGranularity(), "city");
  }
  
  @Test
  public void testMaxResults() {
    GeoQuery geoQuery = new GeoQuery(new GeoLocation(40.7128, -74.0060));
    geoQuery.maxResults(10);
    assertEquals(geoQuery.getMaxResults(), 10);
  }
  
}