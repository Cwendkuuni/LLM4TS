import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    private GeoQuery geoQuery;

    @Before
    public void setUp() {
        // Create GeoLocation object for testing
        GeoLocation location = new GeoLocation(12.345, -67.890);
        geoQuery = new GeoQuery(location);
    }

    @Test
    public void testGetLocation() {
        // Verify that the correct GeoLocation object is returned
        assertEquals(geoQuery.getLocation().getLatitude(), 12.345, 0.001);
        assertEquals(geoQuery.getLocation().getLongitude(), -67.890, 0.001);
    }

    @Test
    public void testGetIp() {
        // Verify that the correct IP is returned
        assertNull(geoQuery.getIp());
    }

    @Test
    public void testGetAccuracy() {
        // Verify that the correct accuracy is returned
        assertNull(geoQuery.getAccuracy());
    }

    @Test
    public void testSetAccuracy() {
        // Set accuracy and verify that it is correctly set
        geoQuery.setAccuracy("high");
        assertEquals(geoQuery.getAccuracy(), "high");
    }

    @Test
    public void testAccuracy() {
        // Set accuracy using the fluent interface and verify that it is correctly set
        GeoQuery updatedGeoQuery = geoQuery.accuracy("medium");
        assertEquals(updatedGeoQuery.getAccuracy(), "medium");
    }

    @Test
    public void testGetGranularity() {
        // Verify that the correct granularity is returned
        assertNull(geoQuery.getGranularity());
    }

    @Test
    public void testSetGranularity() {
        // Set granularity and verify that it is correctly set
        geoQuery.setGranularity("city");
        assertEquals(geoQuery.getGranularity(), "city");
    }

    @Test
    public void testGranularity() {
        // Set granularity using the fluent interface and verify that it is correctly set
        GeoQuery updatedGeoQuery = geoQuery.granularity("neighborhood");
        assertEquals(updatedGeoQuery.getGranularity(), "neighborhood");
    }

    @Test
    public void testGetMaxResults() {
        // Verify that the correct max results is returned
        assertEquals(geoQuery.getMaxResults(), 0);
    }

    @Test
    public void testSetMaxResults() {
        // Set max results and verify that it is correctly set
        geoQuery.setMaxResults(10);
        assertEquals(geoQuery.getMaxResults(), 10);
    }

    @Test
    public void testMaxResults() {
        // Set max results using the fluent interface and verify that it is correctly set
        GeoQuery updatedGeoQuery = geoQuery.maxResults(5);
        assertEquals(updatedGeoQuery.getMaxResults(), 5);
    }

    @Test
    public void testAsHttpParameterArray() {
        // Verify that the correct HTTP parameters are returned
        HttpParameter[] expectedParams = new HttpParameter[] {
                new HttpParameter("lat", "12.345"),
                new HttpParameter("long", "-67.89"),
        };
        assertArrayEquals(geoQuery.asHttpParameterArray(), expectedParams);
    }

    @Test
    public void testEquals() {
        // Create a new GeoQuery object with the same parameters
        GeoLocation location = new GeoLocation(12.345, -67.890);
        GeoQuery sameGeoQuery = new GeoQuery(location);
        sameGeoQuery.setMaxResults(0);

        // Verify that the two GeoQuery objects are considered equal
        assertTrue(geoQuery.equals(sameGeoQuery));
    }

    @Test
    public void testHashCode() {
        // Create a new GeoQuery object with the same parameters
        GeoLocation location = new GeoLocation(12.345, -67.890);
        GeoQuery sameGeoQuery = new GeoQuery(location);
        sameGeoQuery.setMaxResults(0);

        // Verify that the two GeoQuery objects have the same hash code
        assertEquals(geoQuery.hashCode(), sameGeoQuery.hashCode());
    }

    @Test
    public void testToString() {
        // Verify that the toString method returns the expected string representation
        String expectedString = "GeoQuery{location=GeoLocation{latitude=12.345, longitude=-67.89}, ip='null', accuracy='null', granularity='null', maxResults=0}";
        assertEquals(geoQuery.toString(), expectedString);
    }
}