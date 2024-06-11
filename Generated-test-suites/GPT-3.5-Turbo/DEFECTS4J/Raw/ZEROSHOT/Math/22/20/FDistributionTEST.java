import org.junit.Assert;
import org.junit.Test;

public class FDistributionTest {
    
    @Test
    public void testDensity() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double density1 = fd.density(1.0);
        double density2 = fd.density(2.0);
        double density3 = fd.density(3.0);
        
        Assert.assertEquals(0.23570226039551587, density1, 0.000001);
        Assert.assertEquals(0.19634954084936206, density2, 0.000001);
        Assert.assertEquals(0.14433756729740643, density3, 0.000001);
    }
    
    @Test
    public void testCumulativeProbability() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double cp1 = fd.cumulativeProbability(1.0);
        double cp2 = fd.cumulativeProbability(2.0);
        double cp3 = fd.cumulativeProbability(3.0);
        
        Assert.assertEquals(0.6999999999999997, cp1, 0.000001);
        Assert.assertEquals(0.8571428571428571, cp2, 0.000001);
        Assert.assertEquals(0.9047619047619048, cp3, 0.000001);
    }
    
    @Test
    public void testGetNumericalMean() {
        FDistribution fd1 = new FDistribution(2.0, 3.0);
        double mean1 = fd1.getNumericalMean();
        FDistribution fd2 = new FDistribution(3.0, 4.0);
        double mean2 = fd2.getNumericalMean();
        
        Assert.assertEquals(3.0, mean1, 0.000001);
        Assert.assertEquals(2.6666666666666665, mean2, 0.000001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        FDistribution fd1 = new FDistribution(2.0, 3.0);
        double variance1 = fd1.getNumericalVariance();
        FDistribution fd2 = new FDistribution(3.0, 4.0);
        double variance2 = fd2.getNumericalVariance();
        
        Assert.assertEquals(18.0, variance1, 0.000001);
        Assert.assertEquals(16.0, variance2, 0.000001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double lowerBound = fd.getSupportLowerBound();
        
        Assert.assertEquals(0.0, lowerBound, 0.000001);
    }
    
    @Test
    public void testGetSupportUpperBound() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double upperBound = fd.getSupportUpperBound();
        
        Assert.assertEquals(Double.POSITIVE_INFINITY, upperBound, 0.000001);
    }
    
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        boolean inclusive = fd.isSupportLowerBoundInclusive();
        
        Assert.assertTrue(inclusive);
    }
    
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        boolean inclusive = fd.isSupportUpperBoundInclusive();
        
        Assert.assertFalse(inclusive);
    }
    
    @Test
    public void testIsSupportConnected() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        boolean connected = fd.isSupportConnected();
        
        Assert.assertTrue(connected);
    }
}