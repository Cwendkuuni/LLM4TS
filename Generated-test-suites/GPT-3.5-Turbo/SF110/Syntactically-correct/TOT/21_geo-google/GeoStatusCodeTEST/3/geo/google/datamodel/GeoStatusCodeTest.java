import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GeoStatusCodeTest {
  
  // Test cases for getCode()
  
  @Test
  public void testGetCode() {
    GeoStatusCode code = GeoStatusCode.G_GEO_SUCCESS;
    assertEquals(200, code.getCode());
  }
  
  // Test cases for getCodeName()
  
  @Test
  public void testGetCodeName() {
    GeoStatusCode code = GeoStatusCode.G_GEO_SUCCESS;
    assertEquals("G_GEO_SUCCESS", code.getCodeName());
  }
  
  // Test cases for getDescription()
  
  @Test
  public void testGetDescription() {
    GeoStatusCode code = GeoStatusCode.G_GEO_SUCCESS;
    String expected = "No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)";
    assertEquals(expected, code.getDescription());
  }
  
  // Test cases for getStatusCode()
  
  @Test
  public void testGetStatusCode_Success() {
    GeoStatusCode code = GeoStatusCode.getStatusCode(200);
    assertEquals(GeoStatusCode.G_GEO_SUCCESS, code);
  }
  
  @Test
  public void testGetStatusCode_BadRequest() {
    GeoStatusCode code = GeoStatusCode.getStatusCode(400);
    assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, code);
  }
  
  @Test
  public void testGetStatusCode_ServerError() {
    GeoStatusCode code = GeoStatusCode.getStatusCode(500);
    assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, code);
  }
  
  @Test
  public void testGetStatusCode_MissingQuery() {
    GeoStatusCode code = GeoStatusCode.getStatusCode(601);
    assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, code);
  }
  
  @Test
  public void testGetStatusCode_UnknownAddress() {
    GeoStatusCode code = GeoStatusCode.getStatusCode(602);
    assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, code);
  }
  
  @Test
  public void testGetStatusCode_UnavailableAddress() {
    GeoStatusCode code = GeoStatusCode.getStatusCode(603);
    assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, code);
  }
  
  @Test
  public void testGetStatusCode_UnknownDirections() {
    GeoStatusCode code = GeoStatusCode.getStatusCode(604);
    assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, code);
  }
  
  @Test
  public void testGetStatusCode_BadKey() {
    GeoStatusCode code = GeoStatusCode.getStatusCode(610);
    assertEquals(GeoStatusCode.G_GEO_BAD_KEY, code);
  }
  
  @Test
  public void testGetStatusCode_TooManyQueries() {
    GeoStatusCode code = GeoStatusCode.getStatusCode(620);
    assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, code);
  }
  
  @Test
  public void testGetStatusCode_UnknownStatus() {
    GeoStatusCode code = GeoStatusCode.getStatusCode(-1);
    assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, code);
  }
}