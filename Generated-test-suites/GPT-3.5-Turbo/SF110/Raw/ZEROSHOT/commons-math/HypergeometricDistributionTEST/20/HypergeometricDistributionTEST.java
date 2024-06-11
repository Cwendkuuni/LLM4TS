import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.*;

public class HypergeometricDistributionTest {
    
    @Test
    public void testConstructor() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(0, 0, 0);
            Assert.fail("Should have thrown NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Success
        } catch (Exception e) {
            Assert.fail("Wrong exception thrown");
        }
    }
    
    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        Assert.assertEquals(0.0, dist.cumulativeProbability(-1), 0.0001);
        Assert.assertEquals(0.0, dist.cumulativeProbability(0), 0.0001);
        Assert.assertEquals(0.0, dist.cumulativeProbability(1), 0.0001);
        Assert.assertEquals(0.01905, dist.cumulativeProbability(2), 0.0001);
        Assert.assertEquals(0.12381, dist.cumulativeProbability(3), 0.0001);
        Assert.assertEquals(0.54285, dist.cumulativeProbability(4), 0.0001);
        Assert.assertEquals(1.0, dist.cumulativeProbability(5), 0.0001);
        Assert.assertEquals(1.0, dist.cumulativeProbability(6), 0.0001);
    }
    
    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        Assert.assertEquals(3, dist.getNumberOfSuccesses());
    }
    
    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        Assert.assertEquals(10, dist.getPopulationSize());
    }
    
    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        Assert.assertEquals(5, dist.getSampleSize());
    }
    
    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        Assert.assertEquals(0.0, dist.probability(-1), 0.0001);
        Assert.assertEquals(0.0, dist.probability(0), 0.0001);
        Assert.assertEquals(0.0, dist.probability(1), 0.0001);
        Assert.assertEquals(0.03809, dist.probability(2), 0.0001);
        Assert.assertEquals(0.30476, dist.probability(3), 0.0001);
        Assert.assertEquals(0.47619, dist.probability(4), 0.0001);
        Assert.assertEquals(0.19047, dist.probability(5), 0.0001);
        Assert.assertEquals(0.0, dist.probability(6), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        Assert.assertEquals(1.0, dist.upperCumulativeProbability(-1), 0.0001);
        Assert.assertEquals(0.90476, dist.upperCumulativeProbability(0), 0.0001);
        Assert.assertEquals(0.78175, dist.upperCumulativeProbability(1), 0.0001);
        Assert.assertEquals(0.47619, dist.upperCumulativeProbability(2), 0.0001);
        Assert.assertEquals(0.17142, dist.upperCumulativeProbability(3), 0.0001);
        Assert.assertEquals(0.02857, dist.upperCumulativeProbability(4), 0.0001);
        Assert.assertEquals(0.0, dist.upperCumulativeProbability(5), 0.0001);
        Assert.assertEquals(0.0, dist.upperCumulativeProbability(6), 0.0001);
    }
    
    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        Assert.assertEquals(1.5, dist.getNumericalMean(), 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        Assert.assertEquals(0.42857, dist.getNumericalVariance(), 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        Assert.assertEquals(1, dist.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        Assert.assertEquals(3, dist.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 3, 5);
        Assert.assertTrue(dist.isSupportConnected());
    }

}