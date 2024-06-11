import org.junit.*;
import static org.junit.Assert.*;

public class GeoQueryTest {

   @Test
   public void testGetLocation() {
      GeoLocation geo = new GeoLocation(12.345, 67.890);
      GeoQuery query = new GeoQuery(geo);
      assertEquals(geo, query.getLocation());
   }

   @Test
   public void testGetIp() {
      String ip = "192.168.1.1";
      GeoQuery query = new GeoQuery(ip);
      assertEquals(ip, query.getIp());
   }

   @Test
   public void testGetAccuracy() {
      GeoQuery query = new GeoQuery(new GeoLocation(12.345, 67.890));
      query.setAccuracy("high");
      assertEquals("high", query.getAccuracy());
   }

   @Test
   public void testSetAccuracy() {
      GeoQuery query = new GeoQuery(new GeoLocation(12.345, 67.890));
      query.setAccuracy("low");
      assertEquals("low", query.getAccuracy());
   }

   @Test
   public void testGetGranularity() {
      GeoQuery query = new GeoQuery(new GeoLocation(12.345, 67.890));
      query.setGranularity("neighborhood");
      assertEquals("neighborhood", query.getGranularity());
   }

   @Test
   public void testSetGranularity() {
      GeoQuery query = new GeoQuery(new GeoLocation(12.345, 67.890));
      query.setGranularity("city");
      assertEquals("city", query.getGranularity());
   }

   @Test
   public void testGetMaxResults() {
      GeoQuery query = new GeoQuery(new GeoLocation(12.345, 67.890));
      query.setMaxResults(10);
      assertEquals(10, query.getMaxResults());
   }

   @Test
   public void testSetMaxResults() {
      GeoQuery query = new GeoQuery(new GeoLocation(12.345, 67.890));
      query.setMaxResults(5);
      assertEquals(5, query.getMaxResults());
   }

   @Test
   public void testEquals() {
      GeoLocation loc1 = new GeoLocation(12.345, 67.890);
      GeoQuery query1 = new GeoQuery(loc1);
      query1.setAccuracy("high");
      query1.setGranularity("neighborhood");
      query1.setMaxResults(10);

      GeoLocation loc2 = new GeoLocation(12.345, 67.890);
      GeoQuery query2 = new GeoQuery(loc2);
      query2.setAccuracy("high");
      query2.setGranularity("neighborhood");
      query2.setMaxResults(10);

      assertTrue(query1.equals(query2));
   }

   @Test
   public void testHashCode() {
      GeoLocation loc = new GeoLocation(12.345, 67.890);
      GeoQuery query = new GeoQuery(loc);
      query.setAccuracy("high");
      query.setGranularity("neighborhood");
      query.setMaxResults(10);

      assertEquals(-1992288362, query.hashCode());
   }

   @Test
   public void testToString() {
      GeoLocation loc = new GeoLocation(12.345, 67.890);
      GeoQuery query = new GeoQuery(loc);
      query.setAccuracy("high");
      query.setGranularity("neighborhood");
      query.setMaxResults(10);

      String expected = "GeoQuery{location=12.345,67.89, ip='null', accuracy='high', granularity='neighborhood', maxResults=10}";
      assertEquals(expected, query.toString());
   }
}