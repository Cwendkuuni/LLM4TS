import org.junit.*;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

    // Test G_GEO_SUCCESS getCode method
    @Test
    public void testGGeoSuccessGetCode() {
        GeoStatusCode code = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals(200, code.getCode());
    }

    // Test G_GEO_BAD_REQUEST getCode method
    @Test
    public void testGGeoBadRequestGetCode() {
        GeoStatusCode code = GeoStatusCode.G_GEO_BAD_REQUEST;
        assertEquals(400, code.getCode());
    }

    // Test G_GEO_SERVER_ERROR getCode method
    @Test
    public void testGGeoServerErrorGetCode() {
        GeoStatusCode code = GeoStatusCode.G_GEO_SERVER_ERROR;
        assertEquals(500, code.getCode());
    }

    // Test G_GEO_MISSING_QUERY getCode method
    @Test
    public void testGGeoMissingQueryGetCode() {
        GeoStatusCode code = GeoStatusCode.G_GEO_MISSING_QUERY;
        assertEquals(601, code.getCode());
    }

    // Test G_GEO_UNKNOWN_ADDRESS getCode method
    @Test
    public void testGGeoUnknownAddressGetCode() {
        GeoStatusCode code = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        assertEquals(602, code.getCode());
    }

    // Test G_GEO_UNAVAILABLE_ADDRESS getCode method
    @Test
    public void testGGeoUnavailableAddressGetCode() {
        GeoStatusCode code = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        assertEquals(603, code.getCode());
    }

    // Test G_GEO_UNKNOWN_DIRECTIONS getCode method
    @Test
    public void testGGeoUnknownDirectionsGetCode() {
        GeoStatusCode code = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        assertEquals(604, code.getCode());
    }

    // Test G_GEO_BAD_KEY getCode method
    @Test
    public void testGGeoBadKeyGetCode() {
        GeoStatusCode code = GeoStatusCode.G_GEO_BAD_KEY;
        assertEquals(610, code.getCode());
    }

    // Test G_GEO_TOO_MANY_QUERIES getCode method
    @Test
    public void testGGeoTooManyQueriesGetCode() {
        GeoStatusCode code = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        assertEquals(620, code.getCode());
    }

    // Test G_GEO_UNKOWN_STATUS getCode method
    @Test
    public void testGGeoUnknownStatusGetCode() {
        GeoStatusCode code = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        assertEquals(-1, code.getCode());
    }

    // Test G_GEO_SUCCESS getDescription method
    @Test
    public void testGGeoSuccessGetDescription() {
        GeoStatusCode code = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", code.getDescription());
    }

    // Test G_GEO_BAD_REQUEST getDescription method
    @Test
    public void testGGeoBadRequestGetDescription() {
        GeoStatusCode code = GeoStatusCode.G_GEO_BAD_REQUEST;
        assertEquals("A directions request could not be successfully parsed. (Since 2.81)", code.getDescription());
    }

    // Test G_GEO_SERVER_ERROR getDescription method
    @Test
    public void testGGeoServerErrorGetDescription() {
        GeoStatusCode code = GeoStatusCode.G_GEO_SERVER_ERROR;
        assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known. (Since 2.55)", code.getDescription());
    }

    // Test G_GEO_MISSING_QUERY getDescription method
    @Test
    public void testGGeoMissingQueryGetDescription() {
        GeoStatusCode code = GeoStatusCode.G_GEO_MISSING_QUERY;
        assertEquals("The HTTP q parameter was either missing or had no value. For geocoding requests, this means that an empty address was specified as input. For directions requests, this means that no query was specified in the input. (Since 2.81)", code.getDescription());
    }

    // Test G_GEO_UNKNOWN_ADDRESS getDescription method
    @Test
    public void testGGeoUnknownAddressGetDescription() {
        GeoStatusCode code = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        assertEquals("No corresponding geographic location could be found for the specified address. This may be due to the fact that the address is relatively new, or it may be incorrect. (Since 2.55)", code.getDescription());
    }

    // Test G_GEO_UNAVAILABLE_ADDRESS getDescription method
    @Test
    public void testGGeoUnavailableAddressGetDescription() {
        GeoStatusCode code = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        assertEquals("The geocode for the given address or the route for the given directions query cannot be returned due to legal or contractual reasons. (Since 2.55)", code.getDescription());
    }

    // Test G_GEO_UNKNOWN_DIRECTIONS getDescription method
    @Test
    public void testGGeoUnknownDirectionsGetDescription() {
        GeoStatusCode code = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        assertEquals("The GDirections object could not compute directions between the points mentioned in the query. This is usually because there is no route available between the two points, or because we do not have data for routing in that region. (Since 2.81)", code.getDescription());
    }

    // Test G_GEO_BAD_KEY getDescription method
    @Test
    public void testGGeoBadKeyGetDescription() {
        GeoStatusCode code = GeoStatusCode.G_GEO_BAD_KEY;
        assertEquals("The given key is either invalid or does not match the domain for which it was given. (Since 2.55)", code.getDescription());
    }

    // Test G_GEO_TOO_MANY_QUERIES getDescription method
    @Test
    public void testGGeoTooManyQueriesGetDescription() {
        GeoStatusCode code = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        assertEquals("The given key has gone over the requests limit in the 24 hour period. ", code.getDescription());
    }

    // Test G_GEO_UNKOWN_STATUS getDescription method
    @Test
    public void testGGeoUnknownStatusGetDescription() {
        GeoStatusCode code = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        assertEquals("Uknown Status", code.getDescription());
    }

    // Test getStatusCode method with valid code
    @Test
    public void testGetStatusCodeValidCode() {
        GeoStatusCode code = GeoStatusCode.getStatusCode(200);
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, code);
    }

    // Test getStatusCode method with invalid code
    @Test
    public void testGetStatusCodeInvalidCode() {
        GeoStatusCode code = GeoStatusCode.getStatusCode(999);
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, code);
    }
}