import org.junit.Assert;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance_withMinAndMax_shouldReturnCorrectDistanceObject() {
        Distance distance = Distance.buildDistance(1, 5);
        Assert.assertEquals(1, distance.getMin());
        Assert.assertEquals(5, distance.getMax());
        Assert.assertEquals(3, distance.getMean(), 0.001);
        Assert.assertEquals(0.666, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withMeanAndStdDev_shouldReturnCorrectDistanceObject() {
        Distance distance = Distance.buildDistance(3.0f, 0.7f);
        Assert.assertEquals(0, distance.getMin());
        Assert.assertEquals(6, distance.getMax());
        Assert.assertEquals(3.0, distance.getMean(), 0.001);
        Assert.assertEquals(2.1, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistance_withMinAndMaxOnly_shouldReturnCorrectDistanceObject() {
        Distance distance = Distance.buildDistance(1, 10);
        Assert.assertEquals(1, distance.getMin());
        Assert.assertEquals(10, distance.getMax());
        Assert.assertEquals(5.5, distance.getMean(), 0.001);
        Assert.assertEquals(1.5, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_withDeltaGreaterThanMean_shouldReturnCorrectDistanceObject() {
        Distance distance = Distance.buildDistance(1, 5);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(1, transformedDistance.getMin());
        Assert.assertEquals(5, transformedDistance.getMax());
        Assert.assertEquals(3, transformedDistance.getMean(), 0.001);
        Assert.assertEquals(0.995, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_withDeltaLowerThanMean_shouldReturnSameDistanceObject() {
        Distance distance = Distance.buildDistance(10, 20);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(distance, transformedDistance);
    }
    
    @Test
    public void testHashCode_shouldReturnCorrectHashCode() {
        Distance distance1 = Distance.buildDistance(1, 5);
        Distance distance2 = Distance.buildDistance(1, 5);
        Assert.assertEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    @Test
    public void testEquals_withSameObject_shouldReturnTrue() {
        Distance distance = Distance.buildDistance(1, 5);
        Assert.assertTrue(distance.equals(distance));
    }
    
    @Test
    public void testEquals_withNullObject_shouldReturnFalse() {
        Distance distance = Distance.buildDistance(1, 5);
        Assert.assertFalse(distance.equals(null));
    }
    
    @Test
    public void testEquals_withDifferentClassObject_shouldReturnFalse() {
        Distance distance = Distance.buildDistance(1, 5);
        Assert.assertFalse(distance.equals(new Object()));
    }
    
    @Test
    public void testEquals_withDifferentMax_shouldReturnFalse() {
        Distance distance1 = Distance.buildDistance(1, 5);
        Distance distance2 = Distance.buildDistance(1, 10);
        Assert.assertFalse(distance1.equals(distance2));
    }
    
    @Test
    public void testEquals_withDifferentMean_shouldReturnFalse() {
        Distance distance1 = Distance.buildDistance(1, 5);
        Distance distance2 = Distance.buildDistance(1, 5);
        Assert.assertFalse(distance1.equals(distance2));
    }
    
    @Test
    public void testEquals_withDifferentMin_shouldReturnFalse() {
        Distance distance1 = Distance.buildDistance(1, 5);
        Distance distance2 = Distance.buildDistance(5, 10);
        Assert.assertFalse(distance1.equals(distance2));
    }
    
    @Test
    public void testEquals_withDifferentStdDev_shouldReturnFalse() {
        Distance distance1 = Distance.buildDistance(1, 5);
        Distance distance2 = Distance.buildDistance(1, 5);
        Assert.assertFalse(distance1.equals(distance2));
    }
    
    @Test
    public void testToString_shouldReturnCorrectStringRepresentation() {
        Distance distance = Distance.buildDistance(1, 5);
        Assert.assertEquals("3.000, std: 0.666", distance.toString());
    }
}