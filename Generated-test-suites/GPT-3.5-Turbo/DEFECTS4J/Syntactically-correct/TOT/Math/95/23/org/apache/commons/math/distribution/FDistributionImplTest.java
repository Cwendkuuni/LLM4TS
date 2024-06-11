import org.junit.Test;
import org.junit.Assert;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        // Test with x = 0, should return 0.0
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        double result = fDistribution.cumulativeProbability(0);
        Assert.assertEquals(0.0, result, 0.0001);
        
        // Test with x > 0, should return a value between 0.0 and 1.0
        fDistribution = new FDistributionImpl(2.0, 3.0);
        result = fDistribution.cumulativeProbability(1.5);
        Assert.assertTrue(result >= 0.0 && result <= 1.0);
        
        // Test with x < 0, should return 0.0
        fDistribution = new FDistributionImpl(2.0, 3.0);
        result = fDistribution.cumulativeProbability(-1.5);
        Assert.assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        // Test with p = 0, should return 0.0
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        double result = fDistribution.inverseCumulativeProbability(0);
        Assert.assertEquals(0.0, result, 0.0001);
        
        // Test with p = 1, should return Double.POSITIVE_INFINITY
        fDistribution = new FDistributionImpl(2.0, 3.0);
        result = fDistribution.inverseCumulativeProbability(1);
        Assert.assertEquals(Double.POSITIVE_INFINITY, result, 0.0001);
        
        // Test with p between 0 and 1, should return a value between 0.0 and Double.POSITIVE_INFINITY
        fDistribution = new FDistributionImpl(2.0, 3.0);
        result = fDistribution.inverseCumulativeProbability(0.5);
        Assert.assertTrue(result >= 0.0 && result <= Double.POSITIVE_INFINITY);
    }
    
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        // Test with positive degrees of freedom, should not throw an exception
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        fDistribution.setNumeratorDegreesOfFreedom(4.5);
        
        // Test with non-positive degrees of freedom, should throw an exception
        try {
            fDistribution.setNumeratorDegreesOfFreedom(0);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        double result = fDistribution.getNumeratorDegreesOfFreedom();
        Assert.assertEquals(2.0, result, 0.0001);
    }
    
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        // Test with positive degrees of freedom, should not throw an exception
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        fDistribution.setDenominatorDegreesOfFreedom(5.5);
        
        // Test with non-positive degrees of freedom, should throw an exception
        try {
            fDistribution.setDenominatorDegreesOfFreedom(0);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(2.0, 3.0);
        double result = fDistribution.getDenominatorDegreesOfFreedom();
        Assert.assertEquals(3.0, result, 0.0001);
    }

}