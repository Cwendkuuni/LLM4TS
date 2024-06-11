import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DistanceTest {
    
    private Distance distance;
    
    @Before
    public void setUp() {
        distance = Distance.buildDistance(5, 10, 7.5f, 1.5f);
    }
    
    @After
    public void tearDown() {
        distance = null;
    }
    
    @Test
    public void testBuildDistance_withMinMaxMeanStdDev_returnsValidDistanceObject() {
        Distance testDistance = Distance.buildDistance(5, 10, 7.5f, 1.5f);
        Assert.assertEquals(distance, testDistance);
    }
    
    @Test
    public void testBuildDistance_withMeanStdDev_returnsValidDistanceObject() {
        Distance testDistance = Distance.buildDistance(7.5f, 1.5f);
        Assert.assertEquals(distance, testDistance);
    }
    
    @Test
    public void testBuildDistance_withMinMax_returnsValidDistanceObject() {
        Distance testDistance = Distance.buildDistance(5, 10);
        Assert.assertEquals(distance, testDistance);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_withDistanceBelowDelta_returnsCorrectedDistance() {
        Distance modifiedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        float expectedStdDev = (distance.getMean() - 1.015f) / 3.0f;
        Distance expectedDistance = new Distance(distance.getMin(), distance.getMax(), distance.getMean(), expectedStdDev);
        Assert.assertEquals(expectedDistance, modifiedDistance);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_withDistanceNotBelowDelta_returnsSameDistance() {
        Distance testDistance = Distance.buildDistance(10, 20, 15.0f, 2.5f);
        Distance modifiedDistance = Distance.transformIntoCeleraAssemblerDistance(testDistance);
        Assert.assertEquals(testDistance, modifiedDistance);
    }
    
    @Test
    public void testGetMean_returnsCorrectMean() {
        float expectedMean = 7.5f;
        float actualMean = distance.getMean();
        Assert.assertEquals(expectedMean, actualMean, 0.001);
    }
    
    @Test
    public void testGetStdDev_returnsCorrectStdDev() {
        float expectedStdDev = 1.5f;
        float actualStdDev = distance.getStdDev();
        Assert.assertEquals(expectedStdDev, actualStdDev, 0.001);
    }
    
    @Test
    public void testGetMin_returnsCorrectMin() {
        int expectedMin = 5;
        int actualMin = distance.getMin();
        Assert.assertEquals(expectedMin, actualMin);
    }
    
    @Test
    public void testGetMax_returnsCorrectMax() {
        int expectedMax = 10;
        int actualMax = distance.getMax();
        Assert.assertEquals(expectedMax, actualMax);
    }
    
    @Test
    public void testEquals_withSameObject_returnsTrue() {
        Assert.assertTrue(distance.equals(distance));
    }
    
    @Test
    public void testEquals_withNullObject_returnsFalse() {
        Assert.assertFalse(distance.equals(null));
    }
    
    @Test
    public void testEquals_withDifferentClass_returnsFalse() {
        Object obj = new Object();
        Assert.assertFalse(distance.equals(obj));
    }
    
    @Test
    public void testEquals_withSameAttributes_returnsTrue() {
        Distance testDistance = Distance.buildDistance(5, 10, 7.5f, 1.5f);
        Assert.assertTrue(distance.equals(testDistance));
    }
    
    @Test
    public void testEquals_withDifferentMin_returnsFalse() {
        Distance testDistance = Distance.buildDistance(6, 10, 7.5f, 1.5f);
        Assert.assertFalse(distance.equals(testDistance));
    }
    
    @Test
    public void testEquals_withDifferentMax_returnsFalse() {
        Distance testDistance = Distance.buildDistance(5, 12, 7.5f, 1.5f);
        Assert.assertFalse(distance.equals(testDistance));
    }
    
    @Test
    public void testEquals_withDifferentMean_returnsFalse() {
        Distance testDistance = Distance.buildDistance(5, 10, 8.0f, 1.5f);
        Assert.assertFalse(distance.equals(testDistance));
    }
    
    @Test
    public void testEquals_withDifferentStdDev_returnsFalse() {
        Distance testDistance = Distance.buildDistance(5, 10, 7.5f, 2.0f);
        Assert.assertFalse(distance.equals(testDistance));
    }
    
    @Test
    public void testHashCode_withSameAttributes_returnsSameHashCode() {
        Distance testDistance = Distance.buildDistance(5, 10, 7.5f, 1.5f);
        Assert.assertEquals(distance.hashCode(), testDistance.hashCode());
    }
    
    @Test
    public void testToString_returnsExpectedString() {
        String expectedString = "7.500, std: 1.500";
        String actualString = distance.toString();
        Assert.assertEquals(expectedString, actualString);
    }
}