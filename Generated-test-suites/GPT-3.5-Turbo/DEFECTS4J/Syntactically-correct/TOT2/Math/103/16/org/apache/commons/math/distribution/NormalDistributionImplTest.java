import org.junit.Test;
import org.junit.Assert;

public class NormalDistributionImplTest {
    
    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(3.0, 2.0);
        Assert.assertEquals(3.0, normalDist.getMean(), 0);
    }
    
    @Test
    public void testSetMean() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl();
        normalDist.setMean(5.0);
        Assert.assertEquals(5.0, normalDist.getMean(), 0);
    }
    
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(3.0, 2.0);
        Assert.assertEquals(2.0, normalDist.getStandardDeviation(), 0);
    }
    
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl();
        normalDist.setStandardDeviation(3.0);
        Assert.assertEquals(3.0, normalDist.getStandardDeviation(), 0);
    }
    
    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        Assert.assertEquals(0.5, normalDist.cumulativeProbability(0.0), 0.001);
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        Assert.assertEquals(Double.NEGATIVE_INFINITY, normalDist.inverseCumulativeProbability(0), 0);
        Assert.assertEquals(Double.POSITIVE_INFINITY, normalDist.inverseCumulativeProbability(1), 0);
    }
    
    @Test
    public void testInverseCumulativeProbability_exception() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0.0, 1.0);
        try {
            normalDist.inverseCumulativeProbability(2);
            Assert.fail("Expected MathException was not thrown");
        } catch (MathException e) {
            // Expected exception
        }
    }
}