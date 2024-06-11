import org.junit.Test;

import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testConstructorWithGeoLocation() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery geoQuery = new GeoQuery(location);

        assertEquals(location, geoQuery.getLocation());
    }

    @Test
    public void testConstructorWithIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        assertEquals(ip, geoQuery.getIp());
    }

    @Test
    public void testSetAccuracy() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery geoQuery = new GeoQuery(location);

        String accuracy = "high";
        geoQuery.setAccuracy(accuracy);

        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testSetGranularity() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery geoQuery = new GeoQuery(location);

        String granularity = "fine";
        geoQuery.setGranularity(granularity);

        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testSetMaxResults() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery geoQuery = new GeoQuery(location);

        int maxResults = 100;
        geoQuery.setMaxResults(maxResults);

        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArrayWithLocation() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery geoQuery = new GeoQuery(location);

        HttpParameter[] parameters = geoQuery.asHttpParameterArray();

        assertEquals(2, parameters.length);
        assertEquals("lat", parameters[0].getName());
        assertEquals("10.0", parameters[0].getValue());
        assertEquals("long", parameters[1].getName());
        assertEquals("20.0", parameters[1].getValue());
    }

    @Test
    public void testAsHttpParameterArrayWithIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        HttpParameter[] parameters = geoQuery.asHttpParameterArray();

        assertEquals(1, parameters.length);
        assertEquals("ip", parameters[0].getName());
        assertEquals("192.168.0.1", parameters[0].getValue());
    }

    @Test
    public void testAsHttpParameterArrayWithNullLocationAndIp() {
        GeoQuery geoQuery = new GeoQuery(null);

        HttpParameter[] parameters = geoQuery.asHttpParameterArray();

        assertEquals(0, parameters.length);
    }

    @Test
    public void testEqualsWithSameObject() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery geoQuery = new GeoQuery(location);

        assertTrue(geoQuery.equals(geoQuery));
    }

    @Test
    public void testEqualsWithNullObject() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery geoQuery = new GeoQuery(location);

        assertFalse(geoQuery.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClassObject() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery geoQuery = new GeoQuery(location);

        assertFalse(geoQuery.equals("test"));
    }

    @Test
    public void testEqualsWithDifferentLocation() {
        GeoLocation location1 = new GeoLocation(10.0, 20.0);
        GeoLocation location2 = new GeoLocation(30.0, 40.0);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsWithDifferentIp() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery geoQuery1 = new GeoQuery(location);
        GeoQuery geoQuery2 = new GeoQuery("192.168.0.1");

        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsWithSameLocationAndIp() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery geoQuery1 = new GeoQuery(location);
        GeoQuery geoQuery2 = new GeoQuery(location);

        assertTrue(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testHashCodeWithNullLocationAndIp() {
        GeoQuery geoQuery = new GeoQuery(null);

        assertEquals(0, geoQuery.hashCode());
    }

    @Test
    public void testHashCodeWithLocationAndIp() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("fine");
        geoQuery.setMaxResults(100);

        int expectedHashCode = location.hashCode();
        expectedHashCode = 31 * expectedHashCode + "high".hashCode();
        expectedHashCode = 31 * expectedHashCode + "fine".hashCode();
        expectedHashCode = 31 * expectedHashCode + 100;

        assertEquals(expectedHashCode, geoQuery.hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("fine");
        geoQuery.setMaxResults(100);

        String expectedString = "GeoQuery{location=GeoLocation{latitude=10.0, longitude=20.0}, " +
                "ip='null', accuracy='high', granularity='fine', maxResults=100}";

        assertEquals(expectedString, geoQuery.toString());
    }

}