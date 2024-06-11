import org.junit.Test;
import org.apache.commons.math3.distribution.UniformRealDistribution;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        // Test within range
        double density1 = distribution.density(0.5);
        assertEquals(1, density1, 0.000001);

        // Test outside range
        double density2 = distribution.density(1.5);
        assertEquals(0, density2, 0.000001);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        // Test within range
        double cumProb1 = distribution.cumulativeProbability(0.5);
        assertEquals(0.5, cumProb1, 0.000001);

        // Test outside range
        double cumProb2 = distribution.cumulativeProbability(1.5);
        assertEquals(1, cumProb2, 0.000001);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        double mean = distribution.getNumericalMean();
        assertEquals(0.5, mean, 0.000001);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        double variance = distribution.getNumericalVariance();
        assertEquals(0.0833333, variance, 0.000001);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        double lowerBound = distribution.getSupportLowerBound();
        assertEquals(0, lowerBound, 0.000001);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        double upperBound = distribution.getSupportUpperBound();
        assertEquals(1, upperBound, 0.000001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        boolean isLowerBoundInclusive = distribution.isSupportLowerBoundInclusive();
        assertTrue(isLowerBoundInclusive);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        boolean isUpperBoundInclusive = distribution.isSupportUpperBoundInclusive();
        assertFalse(isUpperBoundInclusive);
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        boolean isSupportConnected = distribution.isSupportConnected();
        assertTrue(isSupportConnected);
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);

        double sample = distribution.sample();
        assertTrue(sample >= 0 && sample < 1);
    }
}