package twitter4j;

import org.junit.Assert;
import org.junit.Test;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation location = new GeoLocation(37.7833, -122.4167);
        GeoQuery geoQuery = new GeoQuery(location);
        Assert.assertEquals(location, geoQuery.getLocation());
    }

    @Test
    public void testGetIp() {
        String ip = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        Assert.assertEquals(ip, geoQuery.getIp());
    }

    @Test
    public void testGetAccuracy() {
        String accuracy = "neighborhood";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery.setAccuracy(accuracy);
        Assert.assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testSetAccuracy() {
        String accuracy = "city";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery.setAccuracy(accuracy);
        Assert.assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testGetGranularity() {
        String granularity = "neighborhood";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery.setGranularity(granularity);
        Assert.assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testSetGranularity() {
        String granularity = "city";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery.setGranularity(granularity);
        Assert.assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testGetMaxResults() {
        int maxResults = 10;
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery.setMaxResults(maxResults);
        Assert.assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testSetMaxResults() {
        int maxResults = 50;
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery.setMaxResults(maxResults);
        Assert.assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery1.setAccuracy("neighborhood");
        geoQuery1.setGranularity("city");
        geoQuery1.setMaxResults(100);

        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery2.setAccuracy("neighborhood");
        geoQuery2.setGranularity("city");
        geoQuery2.setMaxResults(100);

        Assert.assertEquals(geoQuery1, geoQuery2);
    }

    @Test
    public void testHashCode() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery1.setAccuracy("neighborhood");
        geoQuery1.setGranularity("city");
        geoQuery1.setMaxResults(100);

        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery2.setAccuracy("neighborhood");
        geoQuery2.setGranularity("city");
        geoQuery2.setMaxResults(100);

        Assert.assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }

    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(37.7833, -122.4167));
        geoQuery.setAccuracy("neighborhood");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(100);

        String expectedString = "GeoQuery{location=GeoLocation{latitude=37.7833, longitude=-122.4167}, ip='null', accuracy='neighborhood', granularity='city', maxResults=100}";
        Assert.assertEquals(expectedString, geoQuery.toString());
    }
}