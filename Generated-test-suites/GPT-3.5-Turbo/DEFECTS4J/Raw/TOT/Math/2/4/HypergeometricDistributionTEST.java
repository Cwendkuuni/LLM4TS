import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(3, distribution.getSampleSize());
    }
    
    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(10, distribution.getPopulationSize());
    }
    
    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(5, distribution.getNumberOfSuccesses());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(0, 5, 3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, -5, 3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, -3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNumberOfSuccessesGreaterThanPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 15, 3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSampleSizeGreaterThanPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 15);
    }
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.057, distribution.cumulativeProbability(1), 0.001);
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.285, distribution.probability(2), 0.001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.947, distribution.upperCumulativeProbability(1), 0.001);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(1.5, distribution.getNumericalMean(), 0.001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.324, distribution.getNumericalVariance(), 0.001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0, distribution.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(3, distribution.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertTrue(distribution.isSupportConnected());
    }
}