import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        assertEquals(200, GeoStatusCode.G_GEO_SUCCESS.getCode());
    }
    
    @Test
    public void testGetCodeName() {
        assertEquals("G_GEO_BAD_REQUEST", GeoStatusCode.G_GEO_BAD_REQUEST.getCodeName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", GeoStatusCode.G_GEO_SUCCESS.getDescription());
    }

    @Test
    public void testGetStatusCode() {
        assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, GeoStatusCode.getStatusCode(601));
    }

    @Test
    public void testGetStatusCodeUnknown() {
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, GeoStatusCode.getStatusCode(999));
    }
    
    // Additional edge case tests
    
    @Test
    public void testGetCodeOutOfRange() {
        assertEquals(-1, GeoStatusCode.G_GEO_UNKOWN_STATUS.getCode());
    }
    
    @Test
    public void testGetInvalidCodeName() {
        assertEquals("GEO_INVALID", GeoStatusCode.G_GEO_UNKOWN_STATUS.getCodeName());
    }

    @Test
    public void testGetInvalidDescription() {
        assertEquals("Invalid description", GeoStatusCode.G_GEO_UNKOWN_STATUS.getDescription());
    }

    @Test
    public void testGetInvalidStatusCode() {
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, GeoStatusCode.getStatusCode(-999));
    }
}