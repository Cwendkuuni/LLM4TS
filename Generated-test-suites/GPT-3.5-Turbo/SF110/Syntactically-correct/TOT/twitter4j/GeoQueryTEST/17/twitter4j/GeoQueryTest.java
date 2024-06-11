import org.junit.Assert;
import org.junit.Test;

public class GeoQueryTest {

    @Test
    public void testGeoQueryConstructorWithGeoLocation() {
        GeoLocation geoLocation = new GeoLocation(1.23, 4.56);
        GeoQuery geoQuery = new GeoQuery(geoLocation);
        Assert.assertEquals(geoLocation, geoQuery.getLocation());
        Assert.assertNull(geoQuery.getIp());
    }

    @Test
    public void testGeoQueryConstructorWithIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        Assert.assertEquals(ip, geoQuery.getIp());
        Assert.assertNull(geoQuery.getLocation());
    }

    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.23, 4.56));
        Assert.assertNull(geoQuery.getAccuracy());
    }

    @Test
    public void testSetAccuracy() {
        String accuracy = "high";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.23, 4.56));
        geoQuery.setAccuracy(accuracy);
        Assert.assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testAccuracyMethod() {
        String accuracy = "low";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.23, 4.56));
        Assert.assertEquals(geoQuery, geoQuery.accuracy(accuracy));
        Assert.assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.23, 4.56));
        Assert.assertNull(geoQuery.getGranularity());
    }

    @Test
    public void testSetGranularity() {
        String granularity = "fine";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.23, 4.56));
        geoQuery.setGranularity(granularity);
        Assert.assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testGranularityMethod() {
        String granularity = "coarse";
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.23, 4.56));
        Assert.assertEquals(geoQuery, geoQuery.granularity(granularity));
        Assert.assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.23, 4.56));
        Assert.assertEquals(0, geoQuery.getMaxResults());
    }

    @Test
    public void testSetMaxResults() {
        int maxResults = 10;
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.23, 4.56));
        geoQuery.setMaxResults(maxResults);
        Assert.assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testMaxResultsMethod() {
        int maxResults = 5;
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.23, 4.56));
        Assert.assertEquals(geoQuery, geoQuery.maxResults(maxResults));
        Assert.assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArrayWithLocation() {
        GeoLocation location = new GeoLocation(1.23, 4.56);
        GeoQuery geoQuery = new GeoQuery(location);
        HttpParameter[] expectedParams = {
            new HttpParameter("lat", String.valueOf(location.getLatitude())),
            new HttpParameter("long", String.valueOf(location.getLongitude())),
            null,
            null,
            null,
            null
        };
        HttpParameter[] actualParams = geoQuery.asHttpParameterArray();
        Assert.assertArrayEquals(expectedParams, actualParams);
    }

    @Test
    public void testAsHttpParameterArrayWithIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        HttpParameter[] expectedParams = {
            null,
            null,
            null,
            null,
            null,
            null,
            new HttpParameter("ip", ip)
        };
        HttpParameter[] actualParams = geoQuery.asHttpParameterArray();
        Assert.assertArrayEquals(expectedParams, actualParams);
    }

    @Test
    public void testAsHttpParameterArrayWithAllFields() {
        GeoLocation location = new GeoLocation(1.23, 4.56);
        String ip = "192.168.0.1";
        String accuracy = "high";
        String granularity = "fine";
        int maxResults = 10;
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);
        HttpParameter[] expectedParams = {
            new HttpParameter("lat", String.valueOf(location.getLatitude())),
            new HttpParameter("long", String.valueOf(location.getLongitude())),
            null,
            null,
            new HttpParameter("accuracy", accuracy),
            new HttpParameter("granularity", granularity),
            new HttpParameter("max_results", String.valueOf(maxResults))
        };
        HttpParameter[] actualParams = geoQuery.asHttpParameterArray();
        Assert.assertArrayEquals(expectedParams, actualParams);
    }

    @Test
    public void testEqualsWithEqualObjects() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.23, 4.56));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.23, 4.56));
        Assert.assertTrue(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsWithDifferentLocations() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.23, 4.56));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(7.89, 10.11));
        Assert.assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsWithDifferentIps() {
        GeoQuery geoQuery1 = new GeoQuery("192.168.0.1");
        GeoQuery geoQuery2 = new GeoQuery("10.0.0.1");
        Assert.assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsWithDifferentAccuracies() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.23, 4.56));
        geoQuery1.setAccuracy("high");
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.23, 4.56));
        geoQuery2.setAccuracy("low");
        Assert.assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsWithDifferentGranularities() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.23, 4.56));
        geoQuery1.setGranularity("fine");
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.23, 4.56));
        geoQuery2.setGranularity("coarse");
        Assert.assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsWithDifferentMaxResults() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.23, 4.56));
        geoQuery1.setMaxResults(10);
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.23, 4.56));
        geoQuery2.setMaxResults(5);
        Assert.assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testEqualsWithNullObject() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.23, 4.56));
        Assert.assertFalse(geoQuery.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClassObject() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.23, 4.56));
        String nonGeoQueryObject = "Not a GeoQuery object";
        Assert.assertFalse(geoQuery.equals(nonGeoQueryObject));
    }

    @Test
    public void testHashCodeWithEqualObjects() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.23, 4.56));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.23, 4.56));
        Assert.assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.23, 4.56);
        String ip = "192.168.0.1";
        String accuracy = "high";
        String granularity = "fine";
        int maxResults = 10;
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setIp(ip);
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);
        String expectedToString = "GeoQuery{location=" + location + ", ip='" + ip + '\'' +
                ", accuracy='" + accuracy + '\'' + ", granularity='" + granularity + '\'' +
                ", maxResults=" + maxResults + '}';
        Assert.assertEquals(expectedToString, geoQuery.toString());
    }

}