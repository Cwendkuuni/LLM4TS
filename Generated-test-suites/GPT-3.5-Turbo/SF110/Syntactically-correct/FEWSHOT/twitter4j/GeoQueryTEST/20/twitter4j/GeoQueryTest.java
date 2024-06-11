import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {

    @Test
    public void testConstructorWithLocation() {
        GeoLocation location = new GeoLocation(37.7833, -122.4167);
        GeoQuery geoQuery = new GeoQuery(location);

        assertEquals(location, geoQuery.getLocation());
        assertNull(geoQuery.getIp());
    }

    @Test
    public void testConstructorWithIp() {
        String ip = "127.0.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        assertNull(geoQuery.getLocation());
        assertEquals(ip, geoQuery.getIp());
    }

    @Test
    public void testGetAndSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));

        assertNull(geoQuery.getAccuracy());

        geoQuery.setAccuracy("high");
        assertEquals("high", geoQuery.getAccuracy());
    }

    @Test
    public void testGetAndSetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));

        assertNull(geoQuery.getGranularity());

        geoQuery.setGranularity("city");
        assertEquals("city", geoQuery.getGranularity());
    }

    @Test
    public void testGetAndSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));

        assertEquals(0, geoQuery.getMaxResults());

        geoQuery.setMaxResults(10);
        assertEquals(10, geoQuery.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(37.7833, -122.4167);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(10);

        HttpParameter[] httpParams = geoQuery.asHttpParameterArray();

        assertEquals(5, httpParams.length);
        assertEquals("lat", httpParams[0].getName());
        assertEquals("37.7833", httpParams[0].getValue());
        assertEquals("long", httpParams[1].getName());
        assertEquals("-122.4167", httpParams[1].getValue());
        assertEquals("accuracy", httpParams[2].getName());
        assertEquals("high", httpParams[2].getValue());
        assertEquals("granularity", httpParams[3].getName());
        assertEquals("city", httpParams[3].getValue());
        assertEquals("max_results", httpParams[4].getName());
        assertEquals("10", httpParams[4].getValue());
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(37.7833, -122.4167);
        GeoLocation location2 = new GeoLocation(37.7833, -122.4167);
        GeoLocation location3 = new GeoLocation(38.7833, -123.4167);

        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        GeoQuery geoQuery3 = new GeoQuery(location3);

        assertEquals(geoQuery1, geoQuery2);
        assertEquals(geoQuery2, geoQuery1);
        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
        
        assertNotEquals(geoQuery1, geoQuery3);
        assertNotEquals(geoQuery3, geoQuery1);
        assertNotEquals(geoQuery1.hashCode(), geoQuery3.hashCode());

        assertNotEquals(geoQuery2, geoQuery3);
        assertNotEquals(geoQuery3, geoQuery2);
        assertNotEquals(geoQuery2.hashCode(), geoQuery3.hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(37.7833, -122.4167);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(10);

        String expectedString = "GeoQuery{location=GeoLocation(latitude=37.7833, longitude=-122.4167), ip='null', accuracy='high', granularity='city', maxResults=10}";

        assertEquals(expectedString, geoQuery.toString());
    }
}