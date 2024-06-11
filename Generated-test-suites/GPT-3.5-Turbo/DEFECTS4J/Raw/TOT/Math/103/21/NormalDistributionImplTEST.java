import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        // Test typical case
        NormalDistributionImpl distribution1 = new NormalDistributionImpl(2.0, 1.0);
        Assert.assertEquals(2.0, distribution1.getMean(), 0.0);
        
        // Test edge case
        NormalDistributionImpl distribution2 = new NormalDistributionImpl();
        Assert.assertEquals(0.0, distribution2.getMean(), 0.0);
    }
    
    @Test
    public void testSetMean() {
        // Test typical case
        NormalDistributionImpl distribution1 = new NormalDistributionImpl();
        distribution1.setMean(2.0);
        Assert.assertEquals(2.0, distribution1.getMean(), 0.0);
        
        // Test error scenario
        try {
            NormalDistributionImpl distribution2 = new NormalDistributionImpl();
            distribution2.setMean(-2.0);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }   
    }
    
    @Test
    public void testGetStandardDeviation() {
        // Test typical case
        NormalDistributionImpl distribution1 = new NormalDistributionImpl(2.0, 1.0);
        Assert.assertEquals(1.0, distribution1.getStandardDeviation(), 0.0);
        
        // Test edge case
        NormalDistributionImpl distribution2 = new NormalDistributionImpl();
        Assert.assertEquals(1.0, distribution2.getStandardDeviation(), 0.0);
    }
    
    @Test
    public void testSetStandardDeviation() {
        // Test typical case
        NormalDistributionImpl distribution1 = new NormalDistributionImpl();
        distribution1.setStandardDeviation(2.0);
        Assert.assertEquals(2.0, distribution1.getStandardDeviation(), 0.0);
        
        // Test error scenario
        try {
            NormalDistributionImpl distribution2 = new NormalDistributionImpl();
            distribution2.setStandardDeviation(-2.0);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }   
    }
    
    @Test
    public void testCumulativeProbability() {
        // Test typical case
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        double probability = distribution.cumulativeProbability(1.0);
        Assert.assertEquals(0.841344746, probability, 0.000001);
        
        // Test edge cases
        double probability2 = distribution.cumulativeProbability(Double.POSITIVE_INFINITY);
        double probability3 = distribution.cumulativeProbability(Double.NEGATIVE_INFINITY);
        Assert.assertEquals(1.0, probability2, 0.0);
        Assert.assertEquals(0.0, probability3, 0.0);
        
        // Test error scenario
        try {
            NormalDistributionImpl distribution2 = new NormalDistributionImpl();
            distribution2.cumulativeProbability(Double.NaN);
            Assert.fail("Expected MathException");
        } catch (MathException e) {
            // success
        }
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        // Test typical case
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        double x = distribution.inverseCumulativeProbability(0.841344746);
        Assert.assertEquals(1.0, x, 0.000001);
        
        // Test edge cases
        double x2 = distribution.inverseCumulativeProbability(0.0);
        double x3 = distribution.inverseCumulativeProbability(1.0);
        Assert.assertEquals(Double.NEGATIVE_INFINITY, x2, 0.0);
        Assert.assertEquals(Double.POSITIVE_INFINITY, x3, 0.0);
        
        // Test error scenario
        try {
            NormalDistributionImpl distribution2 = new NormalDistributionImpl();
            distribution2.inverseCumulativeProbability(1.1);
            Assert.fail("Expected MathException");
        } catch (MathException e) {
            // success
        }
        
        try {
            NormalDistributionImpl distribution3 = new NormalDistributionImpl();
            distribution3.inverseCumulativeProbability(-0.1);
            Assert.fail("Expected MathException");
        } catch (MathException e) {
            // success
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        // Test typical case
        NormalDistributionImpl distribution1 = new NormalDistributionImpl(2.0, 1.0);
        double lowerBound = distribution1.getDomainLowerBound(0.3);
        Assert.assertEquals(2.0, lowerBound, 0.0);
        
        // Test edge case
        NormalDistributionImpl distribution2 = new NormalDistributionImpl();
        double lowerBound2 = distribution2.getDomainLowerBound(0.7);
        Assert.assertEquals(-Double.MAX_VALUE, lowerBound2, 0.0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        // Test typical case
        NormalDistributionImpl distribution1 = new NormalDistributionImpl(2.0, 1.0);
        double upperBound = distribution1.getDomainUpperBound(0.3);
        Assert.assertEquals(Double.MAX_VALUE, upperBound, 0.0);
        
        // Test edge case
        NormalDistributionImpl distribution2 = new NormalDistributionImpl();
        double upperBound2 = distribution2.getDomainUpperBound(0.7);
        Assert.assertEquals(0.0, upperBound2, 0.0);
    }
    
    @Test
    public void testGetInitialDomain() {
        // Test typical case
        NormalDistributionImpl distribution1 = new NormalDistributionImpl(2.0, 1.0);
        double initialDomain = distribution1.getInitialDomain(0.3);
        Assert.assertEquals(3.0, initialDomain, 0.0);
        
        // Test edge cases
        NormalDistributionImpl distribution2 = new NormalDistributionImpl();
        double initialDomain2 = distribution2.getInitialDomain(0.7);
        double initialDomain3 = distribution2.getInitialDomain(0.5);
        Assert.assertEquals(0.0, initialDomain2, 0.0);
        Assert.assertEquals(-1.0, initialDomain3, 0.0);
    }

}