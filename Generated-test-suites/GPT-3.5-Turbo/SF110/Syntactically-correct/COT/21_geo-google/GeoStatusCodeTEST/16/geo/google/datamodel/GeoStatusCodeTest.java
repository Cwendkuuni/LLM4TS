import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GeoStatusCodeTest {

   @Test
   public void testGetCode() {
      GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
      assertEquals(200, statusCode.getCode());
   }

   @Test
   public void testGetCodeName() {
      GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
      assertEquals("G_GEO_SUCCESS", statusCode.getCodeName());
   }

   @Test
   public void testGetDescription() {
      GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
      assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", statusCode.getDescription());
   }

   @Test
   public void testGetStatusCode_Success() {
      GeoStatusCode statusCode = GeoStatusCode.getStatusCode(200);
      assertEquals(GeoStatusCode.G_GEO_SUCCESS, statusCode);
   }

   @Test
   public void testGetStatusCode_BadRequest() {
      GeoStatusCode statusCode = GeoStatusCode.getStatusCode(400);
      assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, statusCode);
   }

   @Test
   public void testGetStatusCode_ServerError() {
      GeoStatusCode statusCode = GeoStatusCode.getStatusCode(500);
      assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, statusCode);
   }

   @Test
   public void testGetStatusCode_MissingQuery() {
      GeoStatusCode statusCode = GeoStatusCode.getStatusCode(601);
      assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, statusCode);
   }

   @Test
   public void testGetStatusCode_UnknownAddress() {
      GeoStatusCode statusCode = GeoStatusCode.getStatusCode(602);
      assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, statusCode);
   }

   @Test
   public void testGetStatusCode_UnavailableAddress() {
      GeoStatusCode statusCode = GeoStatusCode.getStatusCode(603);
      assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, statusCode);
   }

   @Test
   public void testGetStatusCode_UnknownDirections() {
      GeoStatusCode statusCode = GeoStatusCode.getStatusCode(604);
      assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, statusCode);
   }

   @Test
   public void testGetStatusCode_BadKey() {
      GeoStatusCode statusCode = GeoStatusCode.getStatusCode(610);
      assertEquals(GeoStatusCode.G_GEO_BAD_KEY, statusCode);
   }

   @Test
   public void testGetStatusCode_TooManyQueries() {
      GeoStatusCode statusCode = GeoStatusCode.getStatusCode(620);
      assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, statusCode);
   }

   @Test
   public void testGetStatusCode_UnknownStatus() {
      GeoStatusCode statusCode = GeoStatusCode.getStatusCode(-1);
      assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, statusCode);
   }
}