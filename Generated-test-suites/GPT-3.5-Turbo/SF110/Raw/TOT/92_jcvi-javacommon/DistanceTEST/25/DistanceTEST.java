import org.junit.*;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        
        Assert.assertEquals(1, distance.getMin());
        Assert.assertEquals(10, distance.getMax());
        Assert.assertEquals(5.0f, distance.getMean(), 0.001);
        Assert.assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMeanStdDev() {
        Distance distance = Distance.buildDistance(5.0f, 2.0f);
        
        int expectedMin = (int) (5.0f - (3.0f * 2.0f));
        int expectedMax = (int) (5.0f + (3.0f * 2.0f));
        
        Assert.assertEquals(expectedMin, distance.getMin());
        Assert.assertEquals(expectedMax, distance.getMax());
        Assert.assertEquals(5.0f, distance.getMean(), 0.001);
        Assert.assertEquals(2.0f, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMinMax() {
        Distance distance = Distance.buildDistance(1, 10);
        
        float expectedMean = (1 / 2.0f) + (10 / 2.0f);
        float expectedStdDev = (10 - 1) / 6.0f;
        
        Assert.assertEquals(1, distance.getMin());
        Assert.assertEquals(10, distance.getMax());
        Assert.assertEquals(expectedMean, distance.getMean(), 0.001);
        Assert.assertEquals(expectedStdDev, distance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanLessThanDelta() {
        Distance distance = new Distance(1, 10, 2.0f, 0.5f);
        
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        float expectedStdDev = (2.0f - 1.015f) / 3.0f;
        
        Assert.assertEquals(1, transformedDistance.getMin());
        Assert.assertEquals(10, transformedDistance.getMax());
        Assert.assertEquals(2.0f, transformedDistance.getMean(), 0.001);
        Assert.assertEquals(expectedStdDev, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistanceWithMeanGreaterThanDelta() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        
        Assert.assertEquals(1, transformedDistance.getMin());
        Assert.assertEquals(10, transformedDistance.getMax());
        Assert.assertEquals(5.0f, transformedDistance.getMean(), 0.001);
        Assert.assertEquals(2.0f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testHashCode() {
        Distance distance1 = new Distance(1, 10, 5.0f, 2.0f);
        Distance distance2 = new Distance(1, 10, 5.0f, 2.0f);
        
        Assert.assertEquals(distance1.hashCode(), distance2.hashCode());
    }
    
    @Test
    public void testEqualsWithSameObjects() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        
        Assert.assertTrue(distance.equals(distance));
    }
    
    @Test
    public void testEqualsWithEqualObjects() {
        Distance distance1 = new Distance(1, 10, 5.0f, 2.0f);
        Distance distance2 = new Distance(1, 10, 5.0f, 2.0f);
        
        Assert.assertTrue(distance1.equals(distance2));
    }
    
    @Test
    public void testEqualsWithDifferentObjects() {
        Distance distance1 = new Distance(1, 10, 5.0f, 2.0f);
        Distance distance2 = new Distance(2, 20, 10.0f, 5.0f);
        
        Assert.assertFalse(distance1.equals(distance2));
    }
    
    @Test
    public void testToString() {
        Distance distance = new Distance(1, 10, 5.0f, 2.0f);
        
        Assert.assertEquals("5.000, std: 2.000", distance.toString());
    }
    
    // Add additional test methods here for edge cases and error scenarios
    
}

