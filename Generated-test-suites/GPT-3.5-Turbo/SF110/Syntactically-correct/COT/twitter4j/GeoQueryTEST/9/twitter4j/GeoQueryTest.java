import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);

        assertEquals(location, geoQuery.getLocation());
    }

    @Test
    public void testGetIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        assertEquals(ip, geoQuery.getIp());
    }

    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");

        assertNull(geoQuery.getAccuracy());
    }

    @Test
    public void testSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        String accuracy = "high";

        geoQuery.setAccuracy(accuracy);

        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testAccuracy() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        String accuracy = "high";

        GeoQuery returnedQuery = geoQuery.accuracy(accuracy);

        assertEquals(accuracy, returnedQuery.getAccuracy());
    }

    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");

        assertNull(geoQuery.getGranularity());
    }

    @Test
    public void testSetGranularity() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        String granularity = "city";

        geoQuery.setGranularity(granularity);

        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testGranularity() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        String granularity = "city";

        GeoQuery returnedQuery = geoQuery.granularity(granularity);

        assertEquals(granularity, returnedQuery.getGranularity());
    }

    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");

        assertEquals(0, geoQuery.getMaxResults());
    }

    @Test
    public void testSetMaxResults() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        int maxResults = 100;

        geoQuery.setMaxResults(maxResults);

        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testMaxResults() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        int maxResults = 100;

        GeoQuery returnedQuery = geoQuery.maxResults(maxResults);

        assertEquals(maxResults, returnedQuery.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArrayWithLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);
        GeoQuery geoQuery = new GeoQuery(location);

        HttpParameter[] expectedParams = {
                new HttpParameter("lat", "37.7749"),
                new HttpParameter("long", "-122.4194"),
                null,
                null,
                null,
                null
        };

        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }

    @Test
    public void testAsHttpParameterArrayWithIp() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");

        HttpParameter[] expectedParams = {
                null,
                new HttpParameter("ip", "192.168.0.1"),
                null,
                null,
                null,
                null
        };

        assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
    }

    @Test
    public void testAppendParameterWithStringValue() {
        HttpParameter[] expectedParams = {
                new HttpParameter("name", "value")
        };

        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        geoQuery.appendParameter("name", "value", new ArrayList<HttpParameter>());

        HttpParameter[] actualParams = geoQuery.asHttpParameterArray();

        assertArrayEquals(expectedParams, actualParams);
    }

    @Test
    public void testAppendParameterWithIntValue() {
        HttpParameter[] expectedParams = {
                new HttpParameter("name", "100")
        };

        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        geoQuery.appendParameter("name", 100, new ArrayList<HttpParameter>());

        HttpParameter[] actualParams = geoQuery.asHttpParameterArray();

        assertArrayEquals(expectedParams, actualParams);
    }

    @Test
    public void testAppendParameterWithDoubleValue() {
        HttpParameter[] expectedParams = {
                new HttpParameter("name", "3.14")
        };

        GeoQuery geoQuery = new GeoQuery("192.168.0.1");
        geoQuery.appendParameter("name", 3.14, new ArrayList<HttpParameter>());

        HttpParameter[] actualParams = geoQuery.asHttpParameterArray();

        assertArrayEquals(expectedParams, actualParams);
    }

    @Test
    public void testEqualsWithSameObject() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");

        assertTrue(geoQuery.equals(geoQuery));
    }

    @Test
    public void testEqualsWithNullObject() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");

        assertFalse(geoQuery.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        GeoQuery geoQuery = new GeoQuery("192.168.0.1");

        assertFalse(geoQuery.equals("test"));
    }

    @Test
    public void testEqualsWithDifferentMaxResults() {
        GeoQuery geoQuery1 = new GeoQuery("192.168.0.1");
        geoQuery1.setMaxResults(100);

        GeoQuery geoQuery2 = new GeoQuery("192.168.0.1");
        geoQuery2.setMaxResults(200);

        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsWithNullAccuracyAndGranularityAndIpAndLocation() {
        GeoQuery geoQuery1 = new GeoQuery("192.168.0.1");

        GeoQuery geoQuery2 = new GeoQuery("192.168.0.1");

        assertTrue(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsWithSameAccuracy() {
        GeoQuery geoQuery1 = new GeoQuery("192.168.0.1");
        geoQuery1.setAccuracy("high");

        GeoQuery geoQuery2 = new GeoQuery("192.168.0.1");
        geoQuery2.setAccuracy("high");

        assertTrue(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsWithDifferentAccuracy() {
        GeoQuery geoQuery1 = new GeoQuery("192.168.0.1");
        geoQuery1.setAccuracy("high");

        GeoQuery geoQuery2 = new GeoQuery("192.168.0.1");
        geoQuery2.setAccuracy("low");

        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsWithSameGranularity() {
        GeoQuery geoQuery1 = new GeoQuery("192.168.0.1");
        geoQuery1.setGranularity("city");

        GeoQuery geoQuery2 = new GeoQuery("192.168.0.1");
        geoQuery2.setGranularity("city");

        assertTrue(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsWithDifferentGranularity() {
        GeoQuery geoQuery1 = new GeoQuery("192.168.0.1");
        geoQuery1.setGranularity("city");

        GeoQuery geoQuery2 = new GeoQuery("192.168.0.1");
        geoQuery2.setGranularity("neighborhood");

        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsWithSameIpAndLocation() {
        GeoQuery geoQuery1 = new GeoQuery("192.168.0.1");
        GeoQuery geoQuery2 = new GeoQuery("192.168.0.1");

        assertTrue(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsWithDifferentIp() {
        GeoQuery geoQuery1 = new GeoQuery("192.168.0.1");
        GeoQuery geoQuery2 = new GeoQuery("192.168.0.2");

        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsWithSameLocation() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);

        GeoQuery geoQuery1 = new GeoQuery(location);
        GeoQuery geoQuery2 = new GeoQuery(location);

        assertTrue(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsWithDifferentLocation() {
        GeoLocation location1 = new GeoLocation(37.7749, -122.4194);
        GeoLocation location2 = new GeoLocation(40.7128, -74.0060);

        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);

        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setIp("192.168.0.1");
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(100);

        int expectedHashCode = 31 * (31 * (31 * (31 * (31 * location.hashCode() + geoQuery.getIp().hashCode()) + geoQuery.getAccuracy().hashCode()) + geoQuery.getGranularity().hashCode()) + geoQuery.getMaxResults());

        assertEquals(expectedHashCode, geoQuery.hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(37.7749, -122.4194);

        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setIp("192.168.0.1");
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(100);

        String expectedString = "GeoQuery{location=" + location + ", ip='192.168.0.1', accuracy='high', granularity='city', maxResults=100}";

        assertEquals(expectedString, geoQuery.toString());
    }
}