import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jcvi.jillion.assembly.ca.frg.Distance;

public class DistanceTest {

    private Distance distance1;
    private Distance distance2;
    private Distance distance3;
    
    @Before
    public void setUp() {
        distance1 = Distance.buildDistance(1, 10, 5.0f, 1.0f);
        distance2 = Distance.buildDistance(10, 20, 15.0f, 2.0f);
        distance3 = Distance.buildDistance(20, 30, 25.0f, 3.0f);
    }
    
    @Test
    public void testBuildDistanceWithMinAndMax() {
        Assert.assertEquals(1, distance1.getMin());
        Assert.assertEquals(10, distance1.getMax());
        Assert.assertEquals(5.0f, distance1.getMean(), 0.001);
        Assert.assertEquals(1.0f, distance1.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMeanAndStdDev() {
        Assert.assertEquals(10, distance2.getMin());
        Assert.assertEquals(20, distance2.getMax());
        Assert.assertEquals(15.0f, distance2.getMean(), 0.001);
        Assert.assertEquals(2.0f, distance2.getStdDev(), 0.001);
    }
    
    @Test
    public void testBuildDistanceWithMinAndMax_missing_stdDev() {
        Assert.assertEquals(20, distance3.getMin());
        Assert.assertEquals(30, distance3.getMax());
        Assert.assertEquals(25.0f, distance3.getMean(), 0.001);
        Assert.assertEquals((30 - 20) / 6.0f, distance3.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_below_delta() {
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance1);
        Assert.assertEquals(distance1.getMin(), transformedDistance.getMin());
        Assert.assertEquals(distance1.getMax(), transformedDistance.getMax());
        Assert.assertEquals(distance1.getMean(), transformedDistance.getMean(), 0.001);
        Assert.assertEquals((distance1.getMean() - Distance.MAX_ROUNDING_ERROR) / 3.0f, transformedDistance.getStdDev(), 0.001);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_above_delta() {
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance2);
        Assert.assertEquals(distance2, transformedDistance);
    }
    
    @Test
    public void testHashCode() {
        Distance distance4 = Distance.buildDistance(1, 10, 5.0f, 1.0f);
        Assert.assertEquals(distance1.hashCode(), distance4.hashCode());
    }
    
    @Test
    public void testEquals_sameObject() {
        Assert.assertTrue(distance1.equals(distance1));
    }
    
    @Test
    public void testEquals_nullObject() {
        Assert.assertFalse(distance1.equals(null));
    }
    
    @Test
    public void testEquals_differentClass() {
        Assert.assertFalse(distance1.equals(""));
    }
    
    @Test
    public void testEquals_differentMax() {
        Distance distance5 = Distance.buildDistance(1, 5, 5.0f, 1.0f);
        Assert.assertFalse(distance1.equals(distance5));
    }
    
    @Test
    public void testEquals_differentMean() {
        Distance distance6 = Distance.buildDistance(1, 10, 10.0f, 1.0f);
        Assert.assertFalse(distance1.equals(distance6));
    }
    
    @Test
    public void testEquals_differentMin() {
        Distance distance7 = Distance.buildDistance(2, 10, 5.0f, 1.0f);
        Assert.assertFalse(distance1.equals(distance7));
    }
    
    @Test
    public void testEquals_differentStdDev() {
        Distance distance8 = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        Assert.assertFalse(distance1.equals(distance8));
    }
    
    @Test
    public void testToString() {
        Assert.assertEquals("5.000, std: 1.000", distance1.toString());
    }

}