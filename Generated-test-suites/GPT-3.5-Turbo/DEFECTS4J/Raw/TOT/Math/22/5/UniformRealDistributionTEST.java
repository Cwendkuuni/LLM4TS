import org.junit.Assert;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testConstructorLowerBoundGreaterThanUpperBound() {
        try {
            UniformRealDistribution distribution = new UniformRealDistribution(1.0, 0.0);
            Assert.fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        }
    }

    @Test
    public void testConstructorLowerBoundEqualsUpperBound() {
        try {
            UniformRealDistribution distribution = new UniformRealDistribution(1.0, 1.0);
            Assert.fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        }
    }

    @Test
    public void testDensityInsideBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double density = distribution.density(0.5);
        Assert.assertEquals(1.0, density, 0.0001);
    }

    @Test
    public void testDensityOutsideBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double density = distribution.density(-1.0);
        Assert.assertEquals(0.0, density, 0.0001);
    }

    @Test
    public void testCumulativeProbabilityInsideBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double cumulativeProbability = distribution.cumulativeProbability(0.5);
        Assert.assertEquals(0.5, cumulativeProbability, 0.0001);
    }

    @Test
    public void testCumulativeProbabilityOutsideBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double cumulativeProbability = distribution.cumulativeProbability(-1.0);
        Assert.assertEquals(0.0, cumulativeProbability, 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double mean = distribution.getNumericalMean();
        Assert.assertEquals(0.5, mean, 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double variance = distribution.getNumericalVariance();
        Assert.assertEquals(0.083, variance, 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double lowerBound = distribution.getSupportLowerBound();
        Assert.assertEquals(0.0, lowerBound, 0.0001);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double upperBound = distribution.getSupportUpperBound();
        Assert.assertEquals(1.0, upperBound, 0.0001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        boolean inclusive = distribution.isSupportLowerBoundInclusive();
        Assert.assertTrue(inclusive);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        boolean inclusive = distribution.isSupportUpperBoundInclusive();
        Assert.assertFalse(inclusive);
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        boolean connected = distribution.isSupportConnected();
        Assert.assertTrue(connected);
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double sample = distribution.sample();
        Assert.assertTrue(sample >= 0.0 && sample < 1.0);
    }

}