import static org.junit.Assert.*;
import org.junit.Test;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation geoLocation = new GeoLocation(1.23, 4.56);
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        assertEquals(geoLocation, geoQuery.getLocation());
    }

    @Test
    public void testGetIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        assertEquals(ip, geoQuery.getIp());
    }

    @Test
    public void testGetAccuracy() {
        GeoLocation geoLocation = new GeoLocation(1.23, 4.56);
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        assertNull(geoQuery.getAccuracy());

        String accuracy = "high";
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.23, 4.56));
        String accuracy = "high";
        GeoQuery returnedQuery = geoQuery.accuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
        assertEquals(geoQuery, returnedQuery);
    }

    @Test
    public void testGetGranularity() {
        GeoLocation geoLocation = new GeoLocation(1.23, 4.56);
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        assertNull(geoQuery.getGranularity());

        String granularity = "neighborhood";
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.23, 4.56));
        String granularity = "city";
        GeoQuery returnedQuery = geoQuery.granularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
        assertEquals(geoQuery, returnedQuery);
    }

    @Test
    public void testGetMaxResults() {
        GeoLocation geoLocation = new GeoLocation(1.23, 4.56);
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        assertEquals(0, geoQuery.getMaxResults());

        int maxResults = 10;
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.23, 4.56));
        int maxResults = 10;
        GeoQuery returnedQuery = geoQuery.maxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
        assertEquals(geoQuery, returnedQuery);
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoLocation geoLocation = new GeoLocation(1.23, 4.56);
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertEquals(2, params.length);
        assertEquals(new HttpParameter("lat", "1.23"), params[0]);
        assertEquals(new HttpParameter("long", "4.56"), params[1]);

        GeoQuery geoQuery2 = new GeoQuery("192.168.0.1");
        params = geoQuery2.asHttpParameterArray();
        assertEquals(1, params.length);
        assertEquals(new HttpParameter("ip", "192.168.0.1"), params[0]);
    }

    @Test
    public void testAppendParameter() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.23, 4.56));
        List<HttpParameter> params = new ArrayList<>();
        geoQuery.appendParameter("accuracy", "high", params);
        assertEquals(1, params.size());
        assertEquals(new HttpParameter("accuracy", "high"), params.get(0));

        geoQuery.appendParameter("max_results", 10, params);
        assertEquals(2, params.size());
        assertEquals(new HttpParameter("max_results", "10"), params.get(1));

        geoQuery.appendParameter("granularity", "neighborhood", params);
        assertEquals(3, params.size());
        assertEquals(new HttpParameter("granularity", "neighborhood"), params.get(2));

        geoQuery.appendParameter("long", 4.56, params);
        assertEquals(4, params.size());
        assertEquals(new HttpParameter("long", "4.56"), params.get(3));
    }

    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.23, 4.56));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.23, 4.56));
        assertTrue(geoQuery1.equals(geoQuery2));

        geoQuery2 = new GeoQuery(new GeoLocation(7.89, 4.56));
        assertFalse(geoQuery1.equals(geoQuery2));

        geoQuery2 = new GeoQuery("192.168.0.1");
        assertFalse(geoQuery1.equals(geoQuery2));

        geoQuery2 = new GeoQuery(new GeoLocation(1.23, 4.56));
        geoQuery2.setAccuracy("high");
        assertFalse(geoQuery1.equals(geoQuery2));

        geoQuery2 = new GeoQuery(new GeoLocation(1.23, 4.56));
        geoQuery2.setGranularity("neighborhood");
        assertFalse(geoQuery1.equals(geoQuery2));

        geoQuery2 = new GeoQuery(new GeoLocation(1.23, 4.56));
        geoQuery2.setMaxResults(10);
        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testHashCode() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.23, 4.56));

        assertEquals(180868, geoQuery1.hashCode());

        geoQuery1.setAccuracy("high");
        geoQuery1.setGranularity("neighborhood");

        assertEquals(609843264, geoQuery1.hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation geoLocation = new GeoLocation(1.23, 4.56);
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        assertEquals("GeoQuery{location=" + geoLocation + ", ip='null', accuracy='null', granularity='null', maxResults=0}", geoQuery.toString());

        geoQuery.setIp("192.168.0.1");
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(10);
        assertEquals("GeoQuery{location=" + geoLocation + ", ip='192.168.0.1', accuracy='high', granularity='neighborhood', maxResults=10}", geoQuery.toString());
    }
}