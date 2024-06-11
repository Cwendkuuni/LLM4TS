import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        GeoStatusCode code = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals(200, code.getCode());
    }

    @Test
    public void testGetCodeName() {
        GeoStatusCode code = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals("G_GEO_SUCCESS", code.getCodeName());
    }
    
    @Test
    public void testGetDescription() {
        GeoStatusCode code = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", code.getDescription());
    }
    
    @Test
    public void testGetStatusCode() {
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));
    }

}