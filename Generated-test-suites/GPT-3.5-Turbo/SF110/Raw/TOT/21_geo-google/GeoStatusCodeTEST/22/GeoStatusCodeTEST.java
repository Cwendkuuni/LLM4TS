import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode_G_GEO_SUCCESS() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        Assertions.assertEquals(200, statusCode.getCode());
    }

    @Test
    public void testGetCodeName_G_GEO_SUCCESS() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        Assertions.assertEquals("G_GEO_SUCCESS", statusCode.getCodeName());
    }

    @Test
    public void testGetDescription_G_GEO_SUCCESS() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        Assertions.assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", statusCode.getDescription());
    }

    @Test
    public void testGetStatusCode_G_GEO_SUCCESS() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(200);
        Assertions.assertEquals(GeoStatusCode.G_GEO_SUCCESS, statusCode);
    }

    @Test
    public void testGetCode_G_GEO_BAD_REQUEST() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
        Assertions.assertEquals(400, statusCode.getCode());
    }

    @Test
    public void testGetCodeName_G_GEO_BAD_REQUEST() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
        Assertions.assertEquals("G_GEO_BAD_REQUEST", statusCode.getCodeName());
    }

    @Test
    public void testGetDescription_G_GEO_BAD_REQUEST() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
        Assertions.assertEquals("A directions request could not be successfully parsed. (Since 2.81)", statusCode.getDescription());
    }

    @Test
    public void testGetStatusCode_G_GEO_BAD_REQUEST() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(400);
        Assertions.assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, statusCode);
    }

    // ... repeat the above tests for the remaining GeoStatusCode values ...

}