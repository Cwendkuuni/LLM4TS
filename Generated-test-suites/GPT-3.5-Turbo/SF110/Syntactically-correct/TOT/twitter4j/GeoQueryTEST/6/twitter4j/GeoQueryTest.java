import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {
    
    private GeoQuery geoQuery;
    
    @Before
    public void setup() {
        geoQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
    }
    
    @Test
    public void getLocation_validLocation_returnLocation() {
        GeoLocation expectedLocation = new GeoLocation(37.7749, -122.4194);
        
        Assert.assertEquals(expectedLocation, geoQuery.getLocation());
    }
    
    @Test
    public void getIp_nullIp_returnNull() {
        geoQuery = new GeoQuery(null);
        
        Assert.assertNull(geoQuery.getIp());
    }
    
    @Test
    public void getAccuracy_nullAccuracy_returnNull() {
        Assert.assertNull(geoQuery.getAccuracy());
    }
    
    @Test
    public void setAccuracy_validAccuracy_setAccuracy() {
        String expectedAccuracy = "high";
        geoQuery.setAccuracy(expectedAccuracy);
        
        Assert.assertEquals(expectedAccuracy, geoQuery.getAccuracy());
    }
    
    @Test
    public void getGranularity_nullGranularity_returnNull() {
        Assert.assertNull(geoQuery.getGranularity());
    }
    
    @Test
    public void setGranularity_validGranularity_setGranularity() {
        String expectedGranularity = "low";
        geoQuery.setGranularity(expectedGranularity);
        
        Assert.assertEquals(expectedGranularity, geoQuery.getGranularity());
    }
    
    @Test
    public void getMaxResults_zeroMaxResults_returnZero() {
        geoQuery.setMaxResults(0);
        
        Assert.assertEquals(0, geoQuery.getMaxResults());
    }
    
    @Test
    public void setMaxResults_validMaxResults_setMaxResults() {
        int expectedMaxResults = 10;
        geoQuery.setMaxResults(expectedMaxResults);
        
        Assert.assertEquals(expectedMaxResults, geoQuery.getMaxResults());
    }
    
    @Test
    public void asHttpParameterArray_locationAndOtherParameters_returnHttpParameterArray() {
        String expectedParameter1 = "lat";
        String expectedParameter2 = "long";
        String expectedParameter3 = "accuracy";
        String expectedParameter4 = "granularity";
        String expectedParameter5 = "max_results";
        
        HttpParameter[] parameters = geoQuery.asHttpParameterArray();
        
        Assert.assertTrue(hasParameterWithName(parameters, expectedParameter1));
        Assert.assertTrue(hasParameterWithName(parameters, expectedParameter2));
        Assert.assertTrue(hasParameterWithName(parameters, expectedParameter3));
        Assert.assertTrue(hasParameterWithName(parameters, expectedParameter4));
        Assert.assertTrue(hasParameterWithName(parameters, expectedParameter5));
    }
    
    @Test
    public void asHttpParameterArray_ipAndOtherParameters_returnHttpParameterArray() {
        geoQuery = new GeoQuery("192.168.0.1");
        String expectedParameter1 = "ip";
        String expectedParameter2 = "accuracy";
        String expectedParameter3 = "granularity";
        String expectedParameter4 = "max_results";
        
        HttpParameter[] parameters = geoQuery.asHttpParameterArray();
        
        Assert.assertTrue(hasParameterWithName(parameters, expectedParameter1));
        Assert.assertTrue(hasParameterWithName(parameters, expectedParameter2));
        Assert.assertTrue(hasParameterWithName(parameters, expectedParameter3));
        Assert.assertTrue(hasParameterWithName(parameters, expectedParameter4));
    }
    
    @Test
    public void equals_sameObject_returnTrue() {
        Assert.assertTrue(geoQuery.equals(geoQuery));
    }
    
    @Test
    public void equals_differentObject_returnFalse() {
        String differentObject = "not a GeoQuery object";
        
        Assert.assertFalse(geoQuery.equals(differentObject));
    }
    
    @Test
    public void equals_sameAttributes_returnTrue() {
        GeoQuery sameAttributesQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        sameAttributesQuery.setMaxResults(10);
        sameAttributesQuery.setAccuracy("high");
        sameAttributesQuery.setGranularity("low");
        
        Assert.assertTrue(geoQuery.equals(sameAttributesQuery));
    }
    
    @Test
    public void equals_differentLocation_returnFalse() {
        GeoQuery differentLocationQuery = new GeoQuery(new GeoLocation(40.712776, -74.005974));
        differentLocationQuery.setMaxResults(10);
        differentLocationQuery.setAccuracy("high");
        differentLocationQuery.setGranularity("low");
        
        Assert.assertFalse(geoQuery.equals(differentLocationQuery));
    }
    
    @Test
    public void equals_differentMaxResults_returnFalse() {
        GeoQuery differentMaxResultsQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        differentMaxResultsQuery.setMaxResults(20);
        differentMaxResultsQuery.setAccuracy("high");
        differentMaxResultsQuery.setGranularity("low");
        
        Assert.assertFalse(geoQuery.equals(differentMaxResultsQuery));
    }
    
    @Test
    public void equals_differentAccuracy_returnFalse() {
        GeoQuery differentAccuracyQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        differentAccuracyQuery.setMaxResults(10);
        differentAccuracyQuery.setAccuracy("low");
        differentAccuracyQuery.setGranularity("low");
        
        Assert.assertFalse(geoQuery.equals(differentAccuracyQuery));
    }
    
    @Test
    public void equals_differentGranularity_returnFalse() {
        GeoQuery differentGranularityQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        differentGranularityQuery.setMaxResults(10);
        differentGranularityQuery.setAccuracy("high");
        differentGranularityQuery.setGranularity("high");
        
        Assert.assertFalse(geoQuery.equals(differentGranularityQuery));
    }
    
    @Test
    public void equals_differentIp_returnFalse() {
        GeoQuery differentIpQuery = new GeoQuery("192.168.0.1");
        differentIpQuery.setMaxResults(10);
        differentIpQuery.setAccuracy("high");
        differentIpQuery.setGranularity("low");
        
        Assert.assertFalse(geoQuery.equals(differentIpQuery));
    }
    
    @Test
    public void equals_nullValues_returnFalse() {
        GeoQuery nullValuesQuery = new GeoQuery(null);
        
        Assert.assertFalse(geoQuery.equals(nullValuesQuery));
    }
    
    @Test
    public void hashCode_sameAttributes_returnSameHashCode() {
        GeoQuery sameAttributesQuery = new GeoQuery(new GeoLocation(37.7749, -122.4194));
        sameAttributesQuery.setMaxResults(10);
        sameAttributesQuery.setAccuracy("high");
        sameAttributesQuery.setGranularity("low");
        
        Assert.assertEquals(geoQuery.hashCode(), sameAttributesQuery.hashCode());
    }
    
    @Test
    public void toString_validQuery_returnStringRepresentation() {
        String expectedString = "GeoQuery{location=GeoLocation{latitude=37.7749, longitude=-122.4194}, " +
                "ip='null', accuracy='null', granularity='null', maxResults=0}";
        
        Assert.assertEquals(expectedString, geoQuery.toString());
    }
    
    private boolean hasParameterWithName(HttpParameter[] parameters, String name) {
        for (HttpParameter parameter : parameters) {
            if (parameter.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}