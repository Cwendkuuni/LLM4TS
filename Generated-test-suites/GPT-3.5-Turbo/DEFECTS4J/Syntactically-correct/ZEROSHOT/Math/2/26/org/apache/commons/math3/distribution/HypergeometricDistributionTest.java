import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        // Test when x < lower domain
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.0, distribution.cumulativeProbability(1), 0.000001);

        // Test when lower domain <= x < upper domain
        distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.428571, distribution.cumulativeProbability(2), 0.000001);

        // Test when x > upper domain
        distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(1.0, distribution.cumulativeProbability(4), 0.000001);
    }

    @Test
    public void testProbability() {
        // Test when x < lower domain
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.0, distribution.probability(1), 0.000001);

        // Test when lower domain <= x <= upper domain
        distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.12698413, distribution.probability(2), 0.000001);

        // Test when x > upper domain
        distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.0, distribution.probability(4), 0.000001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        // Test when x <= lower domain
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(1.0, distribution.upperCumulativeProbability(1), 0.000001);

        // Test when lower domain < x <= upper domain
        distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.57142857, distribution.upperCumulativeProbability(2), 0.000001);

        // Test when x > upper domain
        distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.0, distribution.upperCumulativeProbability(4), 0.000001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(1.5, distribution.getNumericalMean(), 0.000001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        Assert.assertEquals(0.55555555, distribution.getNumericalVariance(), 0.000001);
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