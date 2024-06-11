package geo.google.datamodel;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        // typical use case
        assertEquals(200, GeoStatusCode.G_GEO_SUCCESS.getCode());

        // edge case
        assertEquals(-1, GeoStatusCode.G_GEO_UNKOWN_STATUS.getCode());
    }

    @Test
    public void testGetCodeName() {
        // typical use case
        assertEquals("G_GEO_SUCCESS", GeoStatusCode.G_GEO_SUCCESS.getCodeName());

        // edge case
        assertNotNull(GeoStatusCode.G_GEO_UNKOWN_STATUS.getCodeName());
    }

    @Test
    public void testGetDescription() {
        // typical use case
        assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", GeoStatusCode.G_GEO_SUCCESS.getDescription());

        // edge case
        assertNotNull(GeoStatusCode.G_GEO_UNKOWN_STATUS.getDescription());
    }

    @Test
    public void testGetStatusCode() {
        // typical use case
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));

        // edge case
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, GeoStatusCode.getStatusCode(999));
    }
}