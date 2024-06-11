
package geo.google.datamodel;

import org.junit.Test;

import static org.junit.Assert.*;

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
    public void testGetStatusCode_success() {
        int statusCode = 200;
        GeoStatusCode code = GeoStatusCode.getStatusCode(statusCode);
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, code);
    }
    
    @Test
    public void testGetStatusCode_badRequest() {
        int statusCode = 400;
        GeoStatusCode code = GeoStatusCode.getStatusCode(statusCode);
        assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, code);
    }
    
    @Test
    public void testGetStatusCode_serverError() {
        int statusCode = 500;
        GeoStatusCode code = GeoStatusCode.getStatusCode(statusCode);
        assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, code);
    }
    
    @Test
    public void testGetStatusCode_missingQuery() {
        int statusCode = 601;
        GeoStatusCode code = GeoStatusCode.getStatusCode(statusCode);
        assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, code);
    }
    
    @Test
    public void testGetStatusCode_unknownAddress() {
        int statusCode = 602;
        GeoStatusCode code = GeoStatusCode.getStatusCode(statusCode);
        assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, code);
    }
    
    @Test
    public void testGetStatusCode_unavailableAddress() {
        int statusCode = 603;
        GeoStatusCode code = GeoStatusCode.getStatusCode(statusCode);
        assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, code);
    }
    
    @Test
    public void testGetStatusCode_unknownDirections() {
        int statusCode = 604;
        GeoStatusCode code = GeoStatusCode.getStatusCode(statusCode);
        assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, code);
    }
    
    @Test
    public void testGetStatusCode_badKey() {
        int statusCode = 610;
        GeoStatusCode code = GeoStatusCode.getStatusCode(statusCode);
        assertEquals(GeoStatusCode.G_GEO_BAD_KEY, code);
    }
    
    @Test
    public void testGetStatusCode_tooManyQueries() {
        int statusCode = 620;
        GeoStatusCode code = GeoStatusCode.getStatusCode(statusCode);
        assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, code);
    }
    
    @Test
    public void testGetStatusCode_unknownStatus() {
        int statusCode = -1;
        GeoStatusCode code = GeoStatusCode.getStatusCode(statusCode);
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, code);
    }

}
