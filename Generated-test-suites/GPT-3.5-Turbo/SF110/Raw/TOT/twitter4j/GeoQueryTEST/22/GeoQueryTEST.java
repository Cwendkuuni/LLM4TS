import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testGeoQueryConstructorWithLocation() {
        GeoLocation location = new GeoLocation(1.234, 2.345);
        GeoQuery geoQuery = new GeoQuery(location);

        assertEquals(location, geoQuery.getLocation());
        assertNull(geoQuery.getIp());
    }

    @Test
    public void testGeoQueryConstructorWithIP() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        assertEquals(ip, geoQuery.getIp());
        assertNull(geoQuery.getLocation());
    }

    @Test
    public void testGetAccuracy() {
        GeoLocation location = new GeoLocation(1.234, 2.345);
        GeoQuery geoQuery = new GeoQuery(location);

        assertNull(geoQuery.getAccuracy());
    }

    @Test
    public void testSetAccuracy() {
        GeoLocation location = new GeoLocation(1.234, 2.345);
        GeoQuery geoQuery = new GeoQuery(location);

        geoQuery.setAccuracy("high");

        assertEquals("high", geoQuery.getAccuracy());
    }

    @Test
    public void testGetGranularity() {
        GeoLocation location = new GeoLocation(1.234, 2.345);
        GeoQuery geoQuery = new GeoQuery(location);

        assertNull(geoQuery.getGranularity());
    }

    @Test
    public void testSetGranularity() {
        GeoLocation location = new GeoLocation(1.234, 2.345);
        GeoQuery geoQuery = new GeoQuery(location);

        geoQuery.setGranularity("neighborhood");

        assertEquals("neighborhood", geoQuery.getGranularity());
    }

    @Test
    public void testGetMaxResults() {
        GeoLocation location = new GeoLocation(1.234, 2.345);
        GeoQuery geoQuery = new GeoQuery(location);

        assertEquals(0, geoQuery.getMaxResults());
    }

    @Test
    public void testSetMaxResults() {
        GeoLocation location = new GeoLocation(1.234, 2.345);
        GeoQuery geoQuery = new GeoQuery(location);

        geoQuery.setMaxResults(10);

        assertEquals(10, geoQuery.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArrayWithLocation() {
        GeoLocation location = new GeoLocation(1.234, 2.345);
        GeoQuery geoQuery = new GeoQuery(location);

        HttpParameter[] expectedParams = new HttpParameter[2];
        expectedParams[0] = new HttpParameter("lat", "1.234");
        expectedParams[1] = new HttpParameter("long", "2.345");

        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }

    @Test
    public void testAsHttpParameterArrayWithIP() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        HttpParameter[] expectedParams = new HttpParameter[1];
        expectedParams[0] = new HttpParameter("ip", "192.168.0.1");

        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }

    @Test
    public void testAsHttpParameterArrayWithoutLocationOrIP() {
        GeoQuery geoQuery = new GeoQuery(null);

        HttpParameter[] expectedParams = new HttpParameter[0];

        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }

    @Test
    public void testEqualsWithSameObject() {
        GeoLocation location = new GeoLocation(1.234, 2.345);
        GeoQuery geoQuery = new GeoQuery(location);

        assertTrue(geoQuery.equals(geoQuery));
    }

    @Test
    public void testEqualsWithNull() {
        GeoLocation location = new GeoLocation(1.234, 2.345);
        GeoQuery geoQuery = new GeoQuery(location);

        assertFalse(geoQuery.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        GeoLocation location = new GeoLocation(1.234, 2.345);
        GeoQuery geoQuery = new GeoQuery(location);

        assertFalse(geoQuery.equals("dummy"));
    }

    @Test
    public void testEqualsWithDifferentMaxResults() {
        GeoLocation location1 = new GeoLocation(1.234, 2.345);
        GeoLocation location2 = new GeoLocation(3.456, 4.567);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        geoQuery1.setMaxResults(10);
        geoQuery2.setMaxResults(20);

        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsWithSameAccuracyAndGranularity() {
        GeoLocation location1 = new GeoLocation(1.234, 2.345);
        GeoLocation location2 = new GeoLocation(3.456, 4.567);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        geoQuery1.setAccuracy("high");
        geoQuery1.setGranularity("city");
        geoQuery2.setAccuracy("high");
        geoQuery2.setGranularity("city");

        assertTrue(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsWithDifferentAccuracy() {
        GeoLocation location1 = new GeoLocation(1.234, 2.345);
        GeoLocation location2 = new GeoLocation(3.456, 4.567);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        geoQuery1.setAccuracy("high");
        geoQuery2.setAccuracy("medium");

        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsWithDifferentGranularity() {
        GeoLocation location1 = new GeoLocation(1.234, 2.345);
        GeoLocation location2 = new GeoLocation(3.456, 4.567);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        geoQuery1.setGranularity("city");
        geoQuery2.setGranularity("neighborhood");

        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsWithSameIP() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery1 = new GeoQuery(ip);
        GeoQuery geoQuery2 = new GeoQuery(ip);

        assertTrue(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsWithDifferentIP() {
        GeoQuery geoQuery1 = new GeoQuery("192.168.0.1");
        GeoQuery geoQuery2 = new GeoQuery("192.168.0.2");

        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsWithNullLocationAndIP() {
        GeoQuery geoQuery1 = new GeoQuery(null);
        GeoQuery geoQuery2 = new GeoQuery(null);

        assertTrue(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testHashCodeWithSameObjects() {
        GeoLocation location1 = new GeoLocation(1.234, 2.345);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location1);

        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }

    @Test
    public void testHashCodeWithDifferentMaxResults() {
        GeoLocation location1 = new GeoLocation(1.234, 2.345);
        GeoLocation location2 = new GeoLocation(3.456, 4.567);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        geoQuery1.setMaxResults(10);
        geoQuery2.setMaxResults(20);

        assertNotEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }

    @Test
    public void testHashCodeWithSameAccuracyAndGranularity() {
        GeoLocation location1 = new GeoLocation(1.234, 2.345);
        GeoLocation location2 = new GeoLocation(3.456, 4.567);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        geoQuery1.setAccuracy("high");
        geoQuery1.setGranularity("city");
        geoQuery2.setAccuracy("high");
        geoQuery2.setGranularity("city");

        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }

    @Test
    public void testHashCodeWithDifferentAccuracy() {
        GeoLocation location1 = new GeoLocation(1.234, 2.345);
        GeoLocation location2 = new GeoLocation(3.456, 4.567);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        geoQuery1.setAccuracy("high");
        geoQuery2.setAccuracy("medium");

        assertNotEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }

    @Test
    public void testHashCodeWithDifferentGranularity() {
        GeoLocation location1 = new GeoLocation(1.234, 2.345);
        GeoLocation location2 = new GeoLocation(3.456, 4.567);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        geoQuery1.setGranularity("city");
        geoQuery2.setGranularity("neighborhood");

        assertNotEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.234, 2.345);
        GeoQuery geoQuery = new GeoQuery(location);

        String expectedString = "GeoQuery{location=GeoLocation{latitude=1.234, longitude=2.345}, ip='null', accuracy='null', granularity='null', maxResults=0}";

        assertEquals(expectedString, geoQuery.toString());
    }
}