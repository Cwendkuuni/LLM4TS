import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HypergeometricDistributionTest {
    
    private HypergeometricDistribution hypergeometricDistribution;
    
    @Before
    public void setUp() {
        hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
    }
    
    @Test
    public void testConstructor() {
        Assert.assertNotNull(hypergeometricDistribution);
    }
    
    @Test
    public void testCumulativeProbability() {
        Assert.assertEquals(0.1, hypergeometricDistribution.cumulativeProbability(0), 0.0001);
        Assert.assertEquals(0.5, hypergeometricDistribution.cumulativeProbability(1), 0.0001);
        Assert.assertEquals(0.8, hypergeometricDistribution.cumulativeProbability(2), 0.0001);
        Assert.assertEquals(1.0, hypergeometricDistribution.cumulativeProbability(3), 0.0001);
        Assert.assertEquals(1.0, hypergeometricDistribution.cumulativeProbability(4), 0.0001);
    }
    
    @Test
    public void testGetNumberOfSuccesses() {
        Assert.assertEquals(5, hypergeometricDistribution.getNumberOfSuccesses());
    }
    
    @Test
    public void testGetPopulationSize() {
        Assert.assertEquals(10, hypergeometricDistribution.getPopulationSize());
    }
    
    @Test
    public void testGetSampleSize() {
        Assert.assertEquals(3, hypergeometricDistribution.getSampleSize());
    }
    
    @Test
    public void testProbability() {
        Assert.assertEquals(0.1667, hypergeometricDistribution.probability(0), 0.0001);
        Assert.assertEquals(0.5, hypergeometricDistribution.probability(1), 0.0001);
        Assert.assertEquals(0.3333, hypergeometricDistribution.probability(2), 0.0001);
        Assert.assertEquals(0.0, hypergeometricDistribution.probability(3), 0.0001);
        Assert.assertEquals(0.0, hypergeometricDistribution.probability(4), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        Assert.assertEquals(1.0, hypergeometricDistribution.upperCumulativeProbability(0), 0.0001);
        Assert.assertEquals(0.9, hypergeometricDistribution.upperCumulativeProbability(1), 0.0001);
        Assert.assertEquals(0.5, hypergeometricDistribution.upperCumulativeProbability(2), 0.0001);
        Assert.assertEquals(0.2, hypergeometricDistribution.upperCumulativeProbability(3), 0.0001);
        Assert.assertEquals(0.0, hypergeometricDistribution.upperCumulativeProbability(4), 0.0001);
    }
    
    @Test
    public void testGetNumericalMean() {
        Assert.assertEquals(1.5, hypergeometricDistribution.getNumericalMean(), 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        Assert.assertEquals(0.375, hypergeometricDistribution.getNumericalVariance(), 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        Assert.assertEquals(0, hypergeometricDistribution.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        Assert.assertEquals(3, hypergeometricDistribution.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        Assert.assertTrue(hypergeometricDistribution.isSupportConnected());
    }
}