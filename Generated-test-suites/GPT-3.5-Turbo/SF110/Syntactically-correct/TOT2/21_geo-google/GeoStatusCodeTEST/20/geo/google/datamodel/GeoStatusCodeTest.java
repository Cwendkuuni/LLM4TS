package geo.google.datamodel;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals(200, statusCode.getCode());
    }

    @Test
    public void testGetCodeName() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
        assertEquals("G_GEO_BAD_REQUEST", statusCode.getCodeName());
    }

    @Test
    public void testGetDescription() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
        assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known. (Since 2.55)", statusCode.getDescription());
    }

    @Test
    public void testGetStatusCode_Success() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(200);
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, statusCode);
    }

    @Test
    public void testGetStatusCode_Unknown() {
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(999);
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, statusCode);
    }
}