import org.junit.Test;
import org.junit.Assert;

public class HypergeometricDistributionTest {

    private HypergeometricDistribution hypergeometricDistribution;

    @Test
    public void testGetNumberOfSuccesses() {
        hypergeometricDistribution = new HypergeometricDistribution(10, 3, 5);
        Assert.assertEquals(3, hypergeometricDistribution.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        hypergeometricDistribution = new HypergeometricDistribution(10, 3, 5);
        Assert.assertEquals(10, hypergeometricDistribution.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        hypergeometricDistribution = new HypergeometricDistribution(10, 3, 5);
        Assert.assertEquals(5, hypergeometricDistribution.getSampleSize());
    }

    @Test
    public void testProbability() {
        hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.119047619, hypergeometricDistribution.probability(2), 0.000001);
    }

    @Test
    public void testCumulativeProbability() {
        hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.809523809, hypergeometricDistribution.cumulativeProbability(2), 0.000001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.190476191, hypergeometricDistribution.upperCumulativeProbability(2), 0.000001);
    }

    @Test
    public void testGetNumericalMean() {
        hypergeometricDistribution = new HypergeometricDistribution(10, 3, 5);
        Assert.assertEquals(1.5, hypergeometricDistribution.getNumericalMean(), 0.000001);
    }

    @Test
    public void testGetNumericalVariance() {
        hypergeometricDistribution = new HypergeometricDistribution(10, 3, 5);
        Assert.assertEquals(0.321428571, hypergeometricDistribution.getNumericalVariance(), 0.000001);
    }

    @Test
    public void testGetSupportLowerBound() {
        hypergeometricDistribution = new HypergeometricDistribution(10, 3, 5);
        Assert.assertEquals(0, hypergeometricDistribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        hypergeometricDistribution = new HypergeometricDistribution(10, 3, 5);
        Assert.assertEquals(3, hypergeometricDistribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        hypergeometricDistribution = new HypergeometricDistribution(10, 3, 5);
        Assert.assertTrue(hypergeometricDistribution.isSupportConnected());
    }
}