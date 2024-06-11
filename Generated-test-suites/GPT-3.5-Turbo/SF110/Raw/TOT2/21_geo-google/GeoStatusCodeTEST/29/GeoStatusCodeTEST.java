import static org.junit.Assert.assertEquals;
import org.junit.Test;
import geo.google.datamodel.GeoStatusCode;

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
    }
}