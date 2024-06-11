package twitter4j;

import org.junit.Test;

import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        // Create GeoQuery instance
        GeoLocation location = new GeoLocation(50.0, 60.0);
        GeoQuery geoQuery = new GeoQuery(location);

        // Make assertions
        assertEquals(location, geoQuery.getLocation());
    }

    @Test
    public void testGetIp() {
        // Create GeoQuery instance
        String ip = "127.0.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        // Make assertions
        assertEquals(ip, geoQuery.getIp());
    }

    @Test
    public void testGetAccuracy() {
        // Create GeoQuery instance
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));

        // Set accuracy
        String accuracy = "neighborhood";
        geoQuery.setAccuracy(accuracy);

        // Make assertions
        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testGetGranularity() {
        // Create GeoQuery instance
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));

        // Set granularity
        String granularity = "city";
        geoQuery.setGranularity(granularity);

        // Make assertions
        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testGetMaxResults() {
        // Create GeoQuery instance
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));

        // Set max results
        int maxResults = 100;
        geoQuery.setMaxResults(maxResults);

        // Make assertions
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testAccuracy() {
        // Create GeoQuery instance
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));

        // Set accuracy using builder pattern
        String accuracy = "neighborhood";
        GeoQuery modifiedQuery = geoQuery.accuracy(accuracy);

        // Make assertions
        assertEquals(accuracy, modifiedQuery.getAccuracy());
    }

    @Test
    public void testGranularity() {
        // Create GeoQuery instance
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));

        // Set granularity using builder pattern
        String granularity = "city";
        GeoQuery modifiedQuery = geoQuery.granularity(granularity);

        // Make assertions
        assertEquals(granularity, modifiedQuery.getGranularity());
    }

    @Test
    public void testMaxResults() {
        // Create GeoQuery instance
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));

        // Set max results using builder pattern
        int maxResults = 100;
        GeoQuery modifiedQuery = geoQuery.maxResults(maxResults);

        // Make assertions
        assertEquals(maxResults, modifiedQuery.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArray() {
        // Create GeoQuery instance with location
        GeoLocation location = new GeoLocation(50.0, 60.0);
        GeoQuery geoQuery = new GeoQuery(location);

        // Set properties
        geoQuery.setAccuracy("neighborhood");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(100);

        // Convert to HttpParameter array
        HttpParameter[] parameters = geoQuery.asHttpParameterArray();

        // Make assertions
        assertNotNull(parameters);
        assertEquals(5, parameters.length);

        assertEquals("lat", parameters[0].getName());
        assertEquals("50.0", parameters[0].getValue());

        assertEquals("long", parameters[1].getName());
        assertEquals("60.0", parameters[1].getValue());

        assertEquals("accuracy", parameters[2].getName());
        assertEquals("neighborhood", parameters[2].getValue());

        assertEquals("granularity", parameters[3].getName());
        assertEquals("city", parameters[3].getValue());

        assertEquals("max_results", parameters[4].getName());
        assertEquals("100", parameters[4].getValue());
    }

    @Test
    public void testEquals() {
        // Create GeoQuery instances
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(0, 0));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(0, 0));

        // Make assertions
        assertTrue(geoQuery1.equals(geoQuery1));
        assertTrue(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testHashCode() {
        // Create GeoQuery instances
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(0, 0));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(0, 0));

        // Make assertions
        assertTrue(geoQuery1.hashCode() == geoQuery2.hashCode());
    }

    @Test
    public void testToString() {
        // Create GeoQuery instance
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        geoQuery.setAccuracy("neighborhood");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(100);

        // Make assertions
        assertEquals("GeoQuery{location=GeoLocation{latitude=0.0, longitude=0.0}, ip='null', accuracy='neighborhood', granularity='city', maxResults=100}", geoQuery.toString());
    }
}