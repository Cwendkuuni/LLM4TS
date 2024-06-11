import org.apache.commons.math3.distribution.FDistribution;
import org.junit.Assert;
import org.junit.Test;

public class FDistributionTest {
    
    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        // Typical use case
        double density1 = fDistribution.density(2.0);
        Assert.assertEquals(0.087252051, density1, 0.00001);
        
        // Edge case
        double density2 = fDistribution.density(0.0);
        Assert.assertEquals(0.0, density2, 0.0);
        
        // Error scenario
        double density3 = fDistribution.density(-1.0);
        Assert.assertEquals(0.0, density3, 0.0);
    }
    
    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        // Typical use case
        double cumulative1 = fDistribution.cumulativeProbability(2.0);
        Assert.assertEquals(0.800527, cumulative1, 0.00001);
        
        // Edge case
        double cumulative2 = fDistribution.cumulativeProbability(0.0);
        Assert.assertEquals(0.0, cumulative2, 0.0);
        
        // Error scenario
        double cumulative3 = fDistribution.cumulativeProbability(-1.0);
        Assert.assertEquals(0.0, cumulative3, 0.0);
    }
    
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        double numeratorDF = fDistribution.getNumeratorDegreesOfFreedom();
        Assert.assertEquals(5.0, numeratorDF, 0.0);
    }
    
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        double denominatorDF = fDistribution.getDenominatorDegreesOfFreedom();
        Assert.assertEquals(10.0, denominatorDF, 0.0);
    }
    
    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        // Typical use case
        double mean1 = fDistribution.getNumericalMean();
        Assert.assertEquals(1.25, mean1, 0.0);
        
        // Edge case
        FDistribution fDistribution2 = new FDistribution(2, 10);
        double mean2 = fDistribution2.getNumericalMean();
        Assert.assertEquals(Double.NaN, mean2, 0.0);
    }
    
    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        // Typical use case
        double variance1 = fDistribution.getNumericalVariance();
        Assert.assertEquals(3.7037037, variance1, 0.0000001);
        
        // Edge case
        FDistribution fDistribution2 = new FDistribution(2, 10);
        double variance2 = fDistribution2.getNumericalVariance();
        Assert.assertEquals(Double.NaN, variance2, 0.0);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        double lowerBound = fDistribution.getSupportLowerBound();
        Assert.assertEquals(0.0, lowerBound, 0.0);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        double upperBound = fDistribution.getSupportUpperBound();
        Assert.assertEquals(Double.POSITIVE_INFINITY, upperBound, 0.0);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        boolean isInclusive = fDistribution.isSupportLowerBoundInclusive();
        Assert.assertTrue(isInclusive);
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        boolean isInclusive = fDistribution.isSupportUpperBoundInclusive();
        Assert.assertFalse(isInclusive);
    }
    
    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(5, 10);
        
        boolean isConnected = fDistribution.isSupportConnected();
        Assert.assertTrue(isConnected);
    }
}