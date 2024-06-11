import org.junit.Test;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        // Basic test for getLocation() method
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);
        
        assertEquals(location, geoQuery.getLocation());
    }

    // Add more test cases here for other public methods

    // Edge cases and exception handling scenarios testing

    // Test for equality and hash code generation

    // Test toString() method
}