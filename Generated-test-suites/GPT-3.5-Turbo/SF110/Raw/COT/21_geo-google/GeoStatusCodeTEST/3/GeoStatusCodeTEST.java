
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GeoStatusCodeTest {
    @Test
    public void testGetCode() {
        assertEquals(200, GeoStatusCode.G_GEO_SUCCESS.getCode());
        assertEquals(400, GeoStatusCode.G_GEO_BAD_REQUEST.getCode());
        assertEquals(500, GeoStatusCode.G_GEO_SERVER_ERROR.getCode());
        assertEquals(601, GeoStatusCode.G_GEO_MISSING_QUERY.getCode());
        assertEquals(602, GeoStatusCode.G_GEO_UNKNOWN_ADDRESS.getCode());
        assertEquals(603, GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS.getCode());
        assertEquals(604, GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS.getCode());
        assertEquals(610, GeoStatusCode.G_GEO_BAD_KEY.getCode());
        assertEquals(620, GeoStatusCode.G_GEO_TOO_MANY_QUERIES.getCode());
        assertEquals(-1, GeoStatusCode.G_GEO_UNKOWN_STATUS.getCode());
    }

    @Test
    public void testGetCodeName() {
        assertEquals("G_GEO_SUCCESS", GeoStatusCode.G_GEO_SUCCESS.getCodeName());
        assertEquals("G_GEO_BAD_REQUEST", GeoStatusCode.G_GEO_BAD_REQUEST.getCodeName());
        assertEquals("G_GEO_SERVER_ERROR", GeoStatusCode.G_GEO_SERVER_ERROR.getCodeName());
        assertEquals("G_GEO_MISSING_QUERY", GeoStatusCode.G_GEO_MISSING_QUERY.getCodeName());
        assertEquals("G_GEO_UNKNOWN_ADDRESS", GeoStatusCode.G_GEO_UNKNOWN_ADDRESS.getCodeName());
        assertEquals("G_GEO_UNAVAILABLE_ADDRESS", GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS.getCodeName());
        assertEquals("G_GEO_UNKNOWN_DIRECTIONS", GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS.getCodeName());
        assertEquals("G_GEO_BAD_KEY", GeoStatusCode.G_GEO_BAD_KEY.getCodeName());
        assertEquals("G_GEO_TOO_MANY_QUERIES", GeoStatusCode.G_GEO_TOO_MANY_QUERIES.getCodeName());
        assertEquals("G_GEO_UNKOWN_STATUS", GeoStatusCode.G_GEO_UNKOWN_STATUS.getCodeName());
    }

    @Test
    public void testGetDescription() {
        assertNotNull(GeoStatusCode.G_GEO_SUCCESS.getDescription());
        assertNotNull(GeoStatusCode.G_GEO_BAD_REQUEST.getDescription());
        assertNotNull(GeoStatusCode.G_GEO_SERVER_ERROR.getDescription());
        assertNotNull(GeoStatusCode.G_GEO_MISSING_QUERY.getDescription());
        assertNotNull(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS.getDescription());
        assertNotNull(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS.getDescription());
        assertNotNull(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS.getDescription());
        assertNotNull(GeoStatusCode.G_GEO_BAD_KEY.getDescription());
        assertNotNull(GeoStatusCode.G_GEO_TOO_MANY_QUERIES.getDescription());
        assertNotNull(GeoStatusCode.G_GEO_UNKOWN_STATUS.getDescription());
    }

    @Test
    public void testGetStatusCode() {
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));
        assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, GeoStatusCode.getStatusCode(400));
        assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, GeoStatusCode.getStatusCode(500));
        assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, GeoStatusCode.getStatusCode(601));
        assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, GeoStatusCode.getStatusCode(602));
        assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, GeoStatusCode.getStatusCode(603));
        assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, GeoStatusCode.getStatusCode(604));
        assertEquals(GeoStatusCode.G_GEO_BAD_KEY, GeoStatusCode.getStatusCode(610));
        assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, GeoStatusCode.getStatusCode(620));
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, GeoStatusCode.getStatusCode(999));
    }
}
