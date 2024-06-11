import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.GeoLocation;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation geoLocation = new GeoLocation(37.7833, -122.4167);
        GeoQuery geoQuery = new GeoQuery(geoLocation);

        assertEquals(geoLocation, geoQuery.getLocation());
    }

    @Test
    public void testGetIp() {
        String ip = "127.0.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        assertEquals(ip, geoQuery.getIp());
    }

    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery.setAccuracy("high");

        assertEquals("high", geoQuery.getAccuracy());
    }

    @Test
    public void testSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery.setAccuracy("exact");

        assertEquals("exact", geoQuery.getAccuracy());
    }

    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery.setGranularity("city");

        assertEquals("city", geoQuery.getGranularity());
    }

    @Test
    public void testSetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery.setGranularity("neighborhood");

        assertEquals("neighborhood", geoQuery.getGranularity());
    }

    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery.setMaxResults(10);

        assertEquals(10, geoQuery.getMaxResults());
    }

    @Test
    public void testSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery.setMaxResults(20);

        assertEquals(20, geoQuery.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("city");

        HttpParameter[] httpParameters = geoQuery.asHttpParameterArray();

        assertNotNull(httpParameters);
        assertEquals(4, httpParameters.length);
        assertEquals("lat", httpParameters[0].getName());
        assertEquals("37.7833", httpParameters[0].getValue());
        assertEquals("long", httpParameters[1].getName());
        assertEquals("-122.4167", httpParameters[1].getValue());
        assertEquals("accuracy", httpParameters[2].getName());
        assertEquals("high", httpParameters[2].getValue());
        assertEquals("granularity", httpParameters[3].getName());
        assertEquals("city", httpParameters[3].getValue());
    }

    @Test
    public void testEquals_SameObject() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));

        assertTrue(geoQuery.equals(geoQuery));
    }

    @Test
    public void testEquals_NullObject() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));

        assertFalse(geoQuery.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        Object object = new Object();

        assertFalse(geoQuery.equals(object));
    }

    @Test
    public void testEquals_DifferentMaxResults() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery1.setMaxResults(10);
        geoQuery2.setMaxResults(20);

        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEquals_SameAccuracyNull() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery1.setAccuracy(null);
        geoQuery2.setAccuracy(null);

        assertTrue(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEquals_DifferentAccuracy() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery1.setAccuracy("high");
        geoQuery2.setAccuracy("low");

        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEquals_SameGranularityNull() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery1.setGranularity(null);
        geoQuery2.setGranularity(null);

        assertTrue(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEquals_DifferentGranularity() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery1.setGranularity("city");
        geoQuery2.setGranularity("neighborhood");

        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEquals_SameIpNull() {
        GeoQuery geoQuery1 = new GeoQuery("127.0.0.1");
        GeoQuery geoQuery2 = new GeoQuery("127.0.0.1");
        geoQuery1.setIp(null);
        geoQuery2.setIp(null);

        assertTrue(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEquals_DifferentIp() {
        GeoQuery geoQuery1 = new GeoQuery("127.0.0.1");
        GeoQuery geoQuery2 = new GeoQuery("192.168.0.1");

        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEquals_SameLocationNull() {
        GeoQuery geoQuery1 = new GeoQuery("127.0.0.1");
        GeoQuery geoQuery2 = new GeoQuery("127.0.0.1");
        geoQuery1.setLocation(null);
        geoQuery2.setLocation(null);

        assertTrue(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEquals_DifferentLocation() {
        GeoQuery geoQuery1 = new GeoQuery("127.0.0.1");
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7833, -122.4167));

        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testHashCode() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(10);

        int hashCode = geoQuery.hashCode();

        assertEquals(1466010990, hashCode);
    }

    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(10);

        String expectedString = "GeoQuery{location=GeoLocation{latitude=37.7833, longitude=-122.4167}, ip='null', accuracy='high', granularity='city', maxResults=10}";
        assertEquals(expectedString, geoQuery.toString());
    }
}