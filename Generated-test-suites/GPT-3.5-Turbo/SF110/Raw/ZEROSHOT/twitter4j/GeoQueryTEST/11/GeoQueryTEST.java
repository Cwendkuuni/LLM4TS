package twitter4j;

import org.junit.Test;

import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void getLocation() {
        GeoLocation location = new GeoLocation(0.0, 0.0);
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(location, geoQuery.getLocation());
    }

    @Test
    public void getIp() {
        String ip = "127.0.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        assertEquals(ip, geoQuery.getIp());
    }

    @Test
    public void getAccuracy() {
        GeoLocation location = new GeoLocation(0.0, 0.0);
        GeoQuery geoQuery = new GeoQuery(location);
        assertNull(geoQuery.getAccuracy());

        String accuracy = "high";
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void setAccuracy() {
        GeoLocation location = new GeoLocation(0.0, 0.0);
        GeoQuery geoQuery = new GeoQuery(location);

        String accuracy = "high";
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void getGranularity() {
        GeoLocation location = new GeoLocation(0.0, 0.0);
        GeoQuery geoQuery = new GeoQuery(location);
        assertNull(geoQuery.getGranularity());

        String granularity = GeoQuery.CITY;
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void setGranularity() {
        GeoLocation location = new GeoLocation(0.0, 0.0);
        GeoQuery geoQuery = new GeoQuery(location);

        String granularity = GeoQuery.CITY;
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void getMaxResults() {
        GeoLocation location = new GeoLocation(0.0, 0.0);
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(0, geoQuery.getMaxResults());

        int maxResults = 100;
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void setMaxResults() {
        GeoLocation location = new GeoLocation(0.0, 0.0);
        GeoQuery geoQuery = new GeoQuery(location);

        int maxResults = 100;
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void accuracy() {
        GeoLocation location = new GeoLocation(0.0, 0.0);
        GeoQuery geoQuery = new GeoQuery(location);

        String accuracy = "high";
        GeoQuery newGeoQuery = geoQuery.accuracy(accuracy);
        assertEquals(accuracy, newGeoQuery.getAccuracy());
    }

    @Test
    public void granularity() {
        GeoLocation location = new GeoLocation(0.0, 0.0);
        GeoQuery geoQuery = new GeoQuery(location);

        String granularity = GeoQuery.CITY;
        GeoQuery newGeoQuery = geoQuery.granularity(granularity);
        assertEquals(granularity, newGeoQuery.getGranularity());
    }

    @Test
    public void maxResults() {
        GeoLocation location = new GeoLocation(0.0, 0.0);
        GeoQuery geoQuery = new GeoQuery(location);

        int maxResults = 100;
        GeoQuery newGeoQuery = geoQuery.maxResults(maxResults);
        assertEquals(maxResults, newGeoQuery.getMaxResults());
    }

    @Test
    public void asHttpParameterArray() {
        GeoLocation location = new GeoLocation(0.0, 0.0);
        GeoQuery geoQuery = new GeoQuery(location);
        
        HttpParameter[] paramArray = geoQuery.asHttpParameterArray();
        assertNotNull(paramArray);
    }

    @Test
    public void equals() {
        GeoLocation location1 = new GeoLocation(0.0, 0.0);
        GeoLocation location2 = new GeoLocation(0.0, 0.0);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location1);

        assertTrue(geoQuery1.equals(geoQuery1));
        assertTrue(geoQuery1.equals(geoQuery2));

        geoQuery2 = new GeoQuery(location2);
        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void hashCode() {
        GeoLocation location = new GeoLocation(0.0, 0.0);
        GeoQuery geoQuery1 = new GeoQuery(location);
        GeoQuery geoQuery2 = new GeoQuery(location);

        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }

    @Test
    public void toStringTest() {
        GeoLocation location = new GeoLocation(0.0, 0.0);
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals("GeoQuery{location=" + location + ", ip='null', accuracy='null', granularity='null', maxResults=0}", geoQuery.toString());
    }
}