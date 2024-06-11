import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode_G_GEO_SUCCESS() {
        GeoStatusCode code = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals(200, code.getCode());
    }

    @Test
    public void testGetCodeName_G_GEO_SUCCESS() {
        GeoStatusCode code = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals("G_GEO_SUCCESS", code.getCodeName());
    }

    @Test
    public void testGetDescription_G_GEO_SUCCESS() {
        GeoStatusCode code = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", code.getDescription());
    }

    @Test
    public void testGetStatusCode_G_GEO_SUCCESS() {
        GeoStatusCode code = GeoStatusCode.getStatusCode(200);
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, code);
    }

    @Test
    public void testGetCode_G_GEO_BAD_REQUEST() {
        GeoStatusCode code = GeoStatusCode.G_GEO_BAD_REQUEST;
        assertEquals(400, code.getCode());
    }

    @Test
    public void testGetCodeName_G_GEO_BAD_REQUEST() {
        GeoStatusCode code = GeoStatusCode.G_GEO_BAD_REQUEST;
        assertEquals("G_GEO_BAD_REQUEST", code.getCodeName());
    }

    @Test
    public void testGetDescription_G_GEO_BAD_REQUEST() {
        GeoStatusCode code = GeoStatusCode.G_GEO_BAD_REQUEST;
        assertEquals("A directions request could not be successfully parsed. (Since 2.81)", code.getDescription());
    }

    @Test
    public void testGetStatusCode_G_GEO_BAD_REQUEST() {
        GeoStatusCode code = GeoStatusCode.getStatusCode(400);
        assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, code);
    }

    // ... repeat for the remaining enum values

}