
package twitter4j;

import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {
  
  @Test
  public void testGetLocation() {
    GeoLocation location = new GeoLocation(1.234, 5.678);
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
    GeoQuery geoQuery = new GeoQuery("192.168.0.1");
    assertNull(geoQuery.getAccuracy());
  }

  @Test
  public void testSetAccuracy() {
    GeoQuery geoQuery = new GeoQuery("192.168.0.1");
    String accuracy = "low";
    geoQuery.setAccuracy(accuracy);
    assertEquals(accuracy, geoQuery.getAccuracy());
  }

  @Test
  public void testAccuracy() {
    GeoQuery geoQuery = new GeoQuery("192.168.0.1");
    String accuracy = "low";
    assertEquals(geoQuery, geoQuery.accuracy(accuracy));
    assertEquals(accuracy, geoQuery.getAccuracy());
  }

  @Test
  public void testGetGranularity() {
    GeoQuery geoQuery = new GeoQuery("192.168.0.1");
    assertNull(geoQuery.getGranularity());
  }

  @Test
  public void testSetGranularity() {
    GeoQuery geoQuery = new GeoQuery("192.168.0.1");
    String granularity = "city";
    geoQuery.setGranularity(granularity);
    assertEquals(granularity, geoQuery.getGranularity());
  }

  @Test
  public void testGranularity() {
    GeoQuery geoQuery = new GeoQuery("192.168.0.1");
    String granularity = "city";
    assertEquals(geoQuery, geoQuery.granularity(granularity));
    assertEquals(granularity, geoQuery.getGranularity());
  }

  @Test
  public void testGetMaxResults() {
    GeoQuery geoQuery = new GeoQuery("192.168.0.1");
    assertEquals(0, geoQuery.getMaxResults());
  }

  @Test
  public void testSetMaxResults() {
    GeoQuery geoQuery = new GeoQuery("192.168.0.1");
    int maxResults = 10;
    geoQuery.setMaxResults(maxResults);
    assertEquals(maxResults, geoQuery.getMaxResults());
  }

  @Test
  public void testMaxResults() {
    GeoQuery geoQuery = new GeoQuery("192.168.0.1");
    int maxResults = 10;
    assertEquals(geoQuery, geoQuery.maxResults(maxResults));
    assertEquals(maxResults, geoQuery.getMaxResults());
  }

  @Test
  public void testAsHttpParameterArray_withLocation() {
    GeoLocation location = new GeoLocation(1.234, 5.678);
    GeoQuery geoQuery = new GeoQuery(location);
    HttpParameter[] expected = {new HttpParameter("lat", "1.234"), new HttpParameter("long", "5.678")};
    assertArrayEquals(expected, geoQuery.asHttpParameterArray());
  }

  @Test
  public void testAsHttpParameterArray_withIp() {
    String ip = "192.168.0.1";
    GeoQuery geoQuery = new GeoQuery(ip);
    HttpParameter[] expected = {new HttpParameter("ip", "192.168.0.1")};
    assertArrayEquals(expected, geoQuery.asHttpParameterArray());
  }

  @Test
  public void testAsHttpParameterArray_withoutLocationAndIp() {
    GeoQuery geoQuery = new GeoQuery((GeoLocation) null);
    HttpParameter[] expected = {};
    assertArrayEquals(expected, geoQuery.asHttpParameterArray());
  }

  @Test
  public void testEquals_equalObjects() {
    GeoLocation location1 = new GeoLocation(1.234, 5.678);
    GeoLocation location2 = new GeoLocation(1.234, 5.678);
    GeoQuery geoQuery1 = new GeoQuery(location1);
    GeoQuery geoQuery2 = new GeoQuery(location2);
    assertTrue(geoQuery1.equals(geoQuery2));
  }

  @Test
  public void testEquals_notEqualObjects() {
    GeoLocation location1 = new GeoLocation(1.234, 5.678);
    GeoLocation location2 = new GeoLocation(2.345, 6.789);
    GeoQuery geoQuery1 = new GeoQuery(location1);
    GeoQuery geoQuery2 = new GeoQuery(location2);
    assertFalse(geoQuery1.equals(geoQuery2));
  }

  @Test
  public void testEquals_notEqualObjectsWithNullValues() {
    GeoLocation location = new GeoLocation(1.234, 5.678);
    GeoQuery geoQuery1 = new GeoQuery(location);
    GeoQuery geoQuery2 = new GeoQuery((GeoLocation) null);
    assertFalse(geoQuery1.equals(geoQuery2));
  }

  @Test
  public void testHashCode_equalObjects() {
    GeoLocation location1 = new GeoLocation(1.234, 5.678);
    GeoLocation location2 = new GeoLocation(1.234, 5.678);
    GeoQuery geoQuery1 = new GeoQuery(location1);
    GeoQuery geoQuery2 = new GeoQuery(location2);
    assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
  }

  @Test
  public void testHashCode_notEqualObjects() {
    GeoLocation location1 = new GeoLocation(1.234, 5.678);
    GeoLocation location2 = new GeoLocation(2.345, 6.789);
    GeoQuery geoQuery1 = new GeoQuery(location1);
    GeoQuery geoQuery2 = new GeoQuery(location2);
    assertNotEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
  }

  @Test
  public void testHashCode_notEqualObjectsWithNullValues() {
    GeoLocation location = new GeoLocation(1.234, 5.678);
    GeoQuery geoQuery1 = new GeoQuery(location);
    GeoQuery geoQuery2 = new GeoQuery((GeoLocation) null);
    assertNotEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
  }

  @Test
  public void testToString_withLocation() {
    GeoLocation location = new GeoLocation(1.234, 5.678);
    GeoQuery geoQuery = new GeoQuery(location);
    String expected = "GeoQuery{location=GeoLocation{latitude=1.234, longitude=5.678}, ip='null', accuracy='null', granularity='null', maxResults=0}";
    assertEquals(expected, geoQuery.toString());
  }

  @Test
  public void testToString_withIp() {
    String ip = "192.168.0.1";
    GeoQuery geoQuery = new GeoQuery(ip);
    String expected = "GeoQuery{location=null, ip='192.168.0.1', accuracy='null', granularity='null', maxResults=0}";
    assertEquals(expected, geoQuery.toString());
  }

}
