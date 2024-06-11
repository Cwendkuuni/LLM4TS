import org.junit.Assert;
import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(4, 6);
        
        // Typical use cases
        Assert.assertEquals(0.2824850328, fDistribution.cumulativeProbability(1.0), 0.0000000001);
        Assert.assertEquals(0.6667153073, fDistribution.cumulativeProbability(1.5), 0.0000000001);
        Assert.assertEquals(0.9467863404, fDistribution.cumulativeProbability(2.0), 0.0000000001);
        
        // Edge cases
        Assert.assertEquals(0.0, fDistribution.cumulativeProbability(0.0), 0.0);
        Assert.assertEquals(1.0, fDistribution.cumulativeProbability(Double.POSITIVE_INFINITY), 0.0);
        
        // Error scenarios
        try {
            fDistribution.cumulativeProbability(-1.0);
            Assert.fail("MathException was not thrown for negative input");
        } catch (MathException e) {
            Assert.assertEquals("Cumulative probability cannot be computed", e.getMessage());
        }
    }
    
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(4, 6);

        // Typical use cases
        Assert.assertEquals(0.9956949351, fDistribution.inverseCumulativeProbability(0.9), 0.0000000001);
        Assert.assertEquals(5.5911706415, fDistribution.inverseCumulativeProbability(0.95), 0.0000000001);
        Assert.assertEquals(22.7895226526, fDistribution.inverseCumulativeProbability(0.99), 0.0000000001);
        
        // Edge cases
        Assert.assertEquals(0.0, fDistribution.inverseCumulativeProbability(0.0), 0.0);
        Assert.assertEquals(Double.POSITIVE_INFINITY, fDistribution.inverseCumulativeProbability(1.0), 0.0);
        
        // Error scenarios
        try {
            fDistribution.inverseCumulativeProbability(-1.0);
            Assert.fail("MathException was not thrown for negative input");
        } catch (MathException e) {
            Assert.assertEquals("Inverse cumulative probability cannot be computed", e.getMessage());
        }

        try {
            fDistribution.inverseCumulativeProbability(2.0);
            Assert.fail("MathException was not thrown for probability > 1.0");
        } catch (MathException e) {
            Assert.assertEquals("Inverse cumulative probability cannot be computed", e.getMessage());
        }
    }
    
    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(4, 6);
        Assert.assertEquals(0.0, fDistribution.getDomainLowerBound(0.5), 0.0);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(4, 6);
        Assert.assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.5), 0.0);
    }
    
    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(4, 6);
        Assert.assertEquals(1.3333333333, fDistribution.getInitialDomain(0.5), 0.0000000001);
    }
    
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(4, 6);
        fDistribution.setNumeratorDegreesOfFreedom(8);
        Assert.assertEquals(8, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);
    }
    
    @Test
    public void testSetNumeratorDegreesOfFreedomWithNegativeValue() {
        FDistributionImpl fDistribution = new FDistributionImpl(4, 6);
        try {
            fDistribution.setNumeratorDegreesOfFreedom(-4);
            Assert.fail("IllegalArgumentException was not thrown for negative degrees of freedom");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("degrees of freedom must be positive.", e.getMessage());
        }
    }
    
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(4, 6);
        fDistribution.setDenominatorDegreesOfFreedom(10);
        Assert.assertEquals(10, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);
    }
    
    @Test
    public void testSetDenominatorDegreesOfFreedomWithNegativeValue() {
        FDistributionImpl fDistribution = new FDistributionImpl(4, 6);
        try {
            fDistribution.setDenominatorDegreesOfFreedom(-6);
            Assert.fail("IllegalArgumentException was not thrown for negative degrees of freedom");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("degrees of freedom must be positive.", e.getMessage());
        }
    }
}