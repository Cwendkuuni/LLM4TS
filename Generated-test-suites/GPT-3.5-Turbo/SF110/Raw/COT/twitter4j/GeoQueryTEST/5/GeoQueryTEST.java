
import org.junit.Assert;
import org.junit.Test;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(12.345, 67.890);
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
        String accuracy = "high";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(12.345, 67.890));
        geoQuery.setAccuracy(accuracy);
        Assert.assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testGetGranularity() {
        String granularity = "low";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(12.345, 67.890));
        geoQuery.setGranularity(granularity);
        Assert.assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testGetMaxResults() {
        int maxResults = 100;
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(12.345, 67.890));
        geoQuery.setMaxResults(maxResults);
        Assert.assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testAccuracy() {
        String accuracy = "low";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(12.345, 67.890));
        geoQuery.accuracy(accuracy);
        Assert.assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testGranularity() {
        String granularity = "high";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(12.345, 67.890));
        geoQuery.granularity(granularity);
        Assert.assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testMaxResults() {
        int maxResults = 50;
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(12.345, 67.890));
        geoQuery.maxResults(maxResults);
        Assert.assertEquals(maxResults, geoQuery.getMaxResults());
    }
}
