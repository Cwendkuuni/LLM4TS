package geo.google.datamodel;
import static org.junit.Assert.*;
import org.junit.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals(200, status.getCode());
    }

    @Test
    public void testGetCodeName() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals("G_GEO_SUCCESS", status.getCodeName());
    }

    @Test
    public void testGetDescription() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", status.getDescription());
    }

    @Test
    public void testGetStatusCode() {
        GeoStatusCode status = GeoStatusCode.getStatusCode(200);
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, status);
        
        status = GeoStatusCode.getStatusCode(400);
        assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, status);
        
        status = GeoStatusCode.getStatusCode(620);
        assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, status);
        
        status = GeoStatusCode.getStatusCode(1000);
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, status);
    }

    @Test
    public void testUnknownStatusCode() {
        GeoStatusCode status = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        assertEquals(-1, status.getCode());
        assertEquals("Uknown Status", status.getDescription());
    }
}