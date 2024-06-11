import org.junit.Assert;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinAndMaxAndMeanAndStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Assert.assertEquals(1, distance.getMin());
        Assert.assertEquals(10, distance.getMax());
        Assert.assertEquals(5.0f, distance.getMean(), 0.001);
        Assert.assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Distance distance = Distance.buildDistance(5.0f, 2.0f);
        Assert.assertEquals(3.0f, distance.getMin(), 0.001);
        Assert.assertEquals(7.0f, distance.getMax(), 0.001);
        Assert.assertEquals(5.0f, distance.getMean(), 0.001);
        Assert.assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMinAndMax() {
        Distance distance = Distance.buildDistance(1, 10);
        Assert.assertEquals(1, distance.getMin());
        Assert.assertEquals(10, distance.getMax());
        Assert.assertEquals(5.5f, distance.getMean(), 0.001);
        Assert.assertEquals(1.5f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanLessThanDelta() {
        Distance distance = new Distance(1, 10, 3.0f, 1.0f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(1, celeraDistance.getMin());
        Assert.assertEquals(10, celeraDistance.getMax());
        Assert.assertEquals(3.0f, celeraDistance.getMean(), 0.001);
        Assert.assertEquals(0.995f, celeraDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanGreaterThanDelta() {
        Distance distance = new Distance(1, 10, 7.0f, 2.0f);
        Distance celeraDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        Assert.assertEquals(1, celeraDistance.getMin());
        Assert.assertEquals(10, celeraDistance.getMax());
        Assert.assertEquals(7.0f, celeraDistance.getMean(), 0.001);
        Assert.assertEquals(2.0f, celeraDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testGetMean() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        Assert.assertEquals(5.0f, distance.getMean(), 0.001);
    }
    
    @Test
    public void testGetStdDev() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        Assert.assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testGetMin() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        Assert.assertEquals(1, distance.getMin());
    }
    
    @Test
    public void testGetMax() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        Assert.assertEquals(10, distance.getMax());
    }
    
    @Test
    public void testHashCode() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        int hashCode = distance.hashCode();
        Assert.assertEquals(230962385, hashCode);
    }
    
    @Test
    public void testEqualsWithEqualObject() {
        Distance distance1 = new Distance(1, 10, 5.0f, 2.0f);
        Distance distance2 = new Distance(1, 10, 5.0f, 2.0f);
        Assert.assertTrue(distance1.equals(distance2));
    }
    
    @Test
    public void testEqualsWithDifferentObject() {
        Distance distance1 = new Distance(1, 10, 5.0f, 2.0f);
        Distance distance2 = new Distance(10, 20, 15.0f, 4.0f);
        Assert.assertFalse(distance1.equals(distance2));
    }
    
    @Test
    public void testEqualsWithNullObject() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        Assert.assertFalse(distance.equals(null));
    }
    
    @Test
    public void testToString() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        String toString = distance.toString();
        Assert.assertEquals("5.000, std: 2.000", toString);
    }
}