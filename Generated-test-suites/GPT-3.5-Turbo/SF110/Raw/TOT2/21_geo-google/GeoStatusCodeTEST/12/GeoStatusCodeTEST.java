import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        GeoStatusCode geoStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals(200, geoStatusCode.getCode());
    }

    @Test
    public void testGetCodeName() {
        GeoStatusCode geoStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals("G_GEO_SUCCESS", geoStatusCode.getCodeName());
    }

    @Test
    public void testGetDescription() {
        GeoStatusCode geoStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", geoStatusCode.getDescription());
    }

    @Test
    public void testGetStatusCode() {
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));
        assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, GeoStatusCode.getStatusCode(602));
        assertEquals(GeoStatusCode.G_GEO_BAD_KEY, GeoStatusCode.getStatusCode(610));
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, GeoStatusCode.getStatusCode(-1));
    }

}