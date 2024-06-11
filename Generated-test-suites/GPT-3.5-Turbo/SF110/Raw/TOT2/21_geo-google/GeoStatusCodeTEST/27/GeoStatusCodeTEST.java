
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals(200, status.getCode());
    }

    @Test
    public void testGetCodeName() {
        GeoStatusCode status = GeoStatusCode.G_GEO_BAD_REQUEST;
        assertEquals("G_GEO_BAD_REQUEST", status.getCodeName());
    }

    @Test
    public void testGetDescription() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SERVER_ERROR;
        assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known. (Since 2.55)", status.getDescription());
    }

    @Test
    public void testGetStatusCode() {
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));
    }

    @Test
    public void testInvalidCode() {
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, GeoStatusCode.getStatusCode(-1));
    }
}
