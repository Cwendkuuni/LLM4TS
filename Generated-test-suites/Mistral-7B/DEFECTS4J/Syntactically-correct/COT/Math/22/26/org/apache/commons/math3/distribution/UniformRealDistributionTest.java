import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionDefaultConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.0, distribution.getLowerBound(), 0.0);
        assertEquals(1.0, distribution.getUpperBound(), 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionConstructorWithInvalidBounds() {
        double lower = 5.0;
        double upper = 3.0;
        UniformRealDistribution distribution = null;
        try {
            distribution = new UniformRealDistribution(lower, upper);
        } catch (NumberIsTooLargeException e) {
            distribution = e.getSource();
        }
        fail("Expected NumberIsTooLargeException, but no exception was thrown.");
    }

    @Test
    public void testUniformRealDistributionConstructorWithValidBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(1.5, 3.5);
        assertEquals(1.5, distribution.getLowerBound(), 0.0);
        assertEquals(3.5, distribution.getUpperBound(), 0.0);
    }

    @Test
    public void testUniformRealDistributionWithCustomRNG() {
        Well19937c rng = new Well19937c(123456L);
        UniformRealDistribution distribution = new UniformRealDistribution(rng, 0.1, 0.4);
        assertEquals(0.1, distribution.getLowerBound(), 0.0);
        assertEquals(0.4, distribution.getUpperBound(), 0.0);
        assertEquals(rng, distribution.getRandomGenerator());
    }

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.5, 1.5);
        for (double x = 0.5; x <= 1.5; x += 0.01) {
            double density = distribution.density(x);
            double expectedDensity = 1 / (1.5 - 0.5);
            assertEquals(expectedDensity, density, 0.001);
        }
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.5, 1.5);
        for (double x = 0.5; x <= 1.5; x += 0.01) {
            double cumProb = distribution.cumulativeProbability(x);
            double expectedCumProb = x - 0.5 / (1.5 - 0.5);
            assertEquals(expectedCumProb, cumProb, 0.001);
        }
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.5, 1.5);
        assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, distribution.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.5, 1.5);
        double numericalMean = distribution.getNumericalMean();
        double expectedNumericalMean = 0.75;
        assertEquals(expectedNumericalMean, numericalMean, 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.5, 1.5);
        double numericalVariance = distribution.getNumericalVariance();
        double expectedNumericalVariance = 0.0014285714285714285;
        assertEquals(expectedNumericalVariance, numericalVariance, 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.5, 1.5);
        assertEquals(0.5, distribution.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.5, 1.5);
        assertEquals(1.5, distribution.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.5, 1.5);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.5, 1.5);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.5, 1.5);
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.5, 1.5);
        double sample = distribution.sample();
        for (double boundary = 0.5; boundary <= 1.5; boundary += 0.01) {
            if (sample < boundary) {
                double lowerBoundProbability = boundary - 0.5 / (1.5 - 0.5);
                double expectedSample = lowerBoundProbability * 1.5 + (1 - lowerBoundProbability) * 0.5;
                assertEquals(expectedSample, sample, 0.001);
                break;
            }
        }
    }
}