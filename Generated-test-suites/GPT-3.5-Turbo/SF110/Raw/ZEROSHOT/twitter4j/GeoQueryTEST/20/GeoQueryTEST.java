import org.junit.Assert;
import org.junit.Test;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(10.0, 20.0);
        GeoQuery geoQuery = new GeoQuery(location);
        Assert.assertEquals(location, geoQuery.getLocation());
    }

    @Test
    public void testGetIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        Assert.assertEquals(ip, geoQuery.getIp());
    }

    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery.setAccuracy("high");
        Assert.assertEquals("high", geoQuery.getAccuracy());
    }

    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery.setGranularity("neighborhood");
        Assert.assertEquals("neighborhood", geoQuery.getGranularity());
    }

    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery.setMaxResults(100);
        Assert.assertEquals(100, geoQuery.getMaxResults());
    }

    @Test
    public void testAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery.accuracy("high");
        Assert.assertEquals("high", geoQuery.getAccuracy());
    }

    @Test
    public void testGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery.granularity("neighborhood");
        Assert.assertEquals("neighborhood", geoQuery.getGranularity());
    }

    @Test
    public void testMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery.maxResults(100);
        Assert.assertEquals(100, geoQuery.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(100);

        HttpParameter[] expectedParams = {
            new HttpParameter("lat", "10.0"),
            new HttpParameter("long", "20.0"),
            new HttpParameter("accuracy", "high"),
            new HttpParameter("granularity", "neighborhood"),
            new HttpParameter("max_results", "100")
        };

        HttpParameter[] actualParams = geoQuery.asHttpParameterArray();

        Assert.assertArrayEquals(expectedParams, actualParams);
    }

    @Test
    public void testAppendParameterWithValue() {
        List<HttpParameter> params = new ArrayList<>();
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery.appendParameter("name", "value", params);

        HttpParameter expectedParam = new HttpParameter("name", "value");
        HttpParameter actualParam = params.get(0);

        Assert.assertEquals(expectedParam.getName(), actualParam.getName());
        Assert.assertEquals(expectedParam.getValue(), actualParam.getValue());
    }

    @Test
    public void testAppendParameterWithStringValue() {
        List<HttpParameter> params = new ArrayList<>();
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery.appendParameter("name", 100, params);

        HttpParameter expectedParam = new HttpParameter("name", "100");
        HttpParameter actualParam = params.get(0);

        Assert.assertEquals(expectedParam.getName(), actualParam.getName());
        Assert.assertEquals(expectedParam.getValue(), actualParam.getValue());
    }

    @Test
    public void testAppendParameterWithIntValue() {
        List<HttpParameter> params = new ArrayList<>();
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery.appendParameter("name", 100.5, params);

        HttpParameter expectedParam = new HttpParameter("name", "100.5");
        HttpParameter actualParam = params.get(0);

        Assert.assertEquals(expectedParam.getName(), actualParam.getName());
        Assert.assertEquals(expectedParam.getValue(), actualParam.getValue());
    }

    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery1.setAccuracy("high");
        geoQuery1.setGranularity("neighborhood");
        geoQuery1.setMaxResults(100);

        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery2.setAccuracy("high");
        geoQuery2.setGranularity("neighborhood");
        geoQuery2.setMaxResults(100);

        Assert.assertEquals(geoQuery1, geoQuery2);
    }

    @Test
    public void testHashCode() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(100);

        int expectedHashCode = geoQuery.getLocation().hashCode() +
                                geoQuery.getIp().hashCode() +
                                geoQuery.getAccuracy().hashCode() +
                                geoQuery.getGranularity().hashCode() +
                                geoQuery.getMaxResults();

        Assert.assertEquals(expectedHashCode, geoQuery.hashCode());
    }

    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(10.0, 20.0));
        geoQuery.setAccuracy("high");
        geoQuery.setGranularity("neighborhood");
        geoQuery.setMaxResults(100);

        String expectedString = "GeoQuery{location=GeoLocation{latitude=10.0, longitude=20.0}, ip='null', accuracy='high', granularity='neighborhood', maxResults=100}";
        Assert.assertEquals(expectedString, geoQuery.toString());
    }
}