import org.junit.Test;

public class GeoStatusCodeTest {

    // Test getCode() method
    @Test
    public void testGetCode() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        int code = statusCode.getCode();
        assertEquals(200, code);
    }

    // Test getCodeName() method
    @Test
    public void testGetCodeName() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        String codeName = statusCode.getCodeName();
        assertEquals("G_GEO_SUCCESS", codeName);
    }

    // Test getDescription() method
    @Test
    public void testGetDescription() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        String description = statusCode.getDescription();
        assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", description);
    }

    // Test getStatusCode() method for existing code
    @Test
    public void testGetStatusCodeExistingCode() {
        int code = 200;
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(code);
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, statusCode);
    }

    // Test getStatusCode() method for non-existing code
    @Test
    public void testGetStatusCodeNonExistingCode() {
        int code = 999;
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(code);
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, statusCode);
    }

    // Test the class constructor
    @Test
    public void testConstructor() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals(200, statusCode.getCode());
        assertEquals("G_GEO_SUCCESS", statusCode.getCodeName());
        assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", statusCode.getDescription());
    }
}