import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionConstructorWithDefaultBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.0, distribution.getLowerBound(), 0.0);
        assertEquals(1.0, distribution.getUpperBound(), 0.0);
    }

    @Test
    public void testUniformRealDistributionConstructorWithCustomBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(2.0, 5.0);
        assertEquals(2.0, distribution.getLowerBound(), 0.0);
        assertEquals(5.0, distribution.getUpperBound(), 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionConstructorWithInvalidBounds() {
        new UniformRealDistribution(5.0, 2.0);
    }

    @Test
    public void testUniformRealDistributionDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        for (double x = 0.0; x <= 1.0; x++) {
            double density = distribution.density(x);
            if (x >= 0.0 && x <= 1.0) {
                assertEquals(1.0 / (1.0 - 0.0), density, 1e-6);
            } else {
                assertEquals(0.0, density, 1e-6);
            }
        }
    }

    @Test
    public void testUniformRealDistributionCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        for (double x = 0.0; x <= 1.0; x += 0.1) {
            double cumProb = distribution.cumulativeProbability(x);
            assertEquals(x, cumProb, 1e-6);
        }
    }

    @Test
    public void testUniformRealDistributionSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= 0.0 && sample < 1.0);
        }
    }

    @Test
    public void testUniformRealDistributionGetSolverAbsoluteAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, distribution.getSolverAbsoluteAccuracy(), 1e-6);
    }

    @Test
    public void testUniformRealDistributionGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double mean = distribution.getNumericalMean();
        assertEquals(0.5, mean, 1e-6);
    }

    @Test
    public void testUniformRealDistributionGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double variance = distribution.getNumericalVariance();
        assertEquals(0.000246223621964, variance, 1e-6);
    }

    @Test
    public void testUniformRealDistributionGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double supportLowerBound = distribution.getSupportLowerBound();
        assertEquals(0.0, supportLowerBound, 0.0);
    }

    @Test
    public void testUniformRealDistributionGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        double supportUpperBound = distribution.getSupportUpperBound();
        assertEquals(1.0, supportUpperBound, 0.0);
    }

    @Test
    public void testUniformRealDistributionIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        boolean isInclusive = distribution.isSupportLowerBoundInclusive();
        assertTrue(isInclusive);
    }

    @Test
    public void testUniformRealDistributionIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        boolean isExclusive = distribution.isSupportUpperBoundInclusive();
        assertFalse(isExclusive);
    }

    @Test
    public void testUniformRealDistributionIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        boolean isConnected = distribution.isSupportConnected();
        assertTrue(isConnected);
    }

    @Test
    public void testUniformRealDistributionWithCustomRandomGenerator() {
        Well19937c rng = new Well19937c();
        UniformRealDistribution distribution = new UniformRealDistribution(rng, 0.0, 1.0);
        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= 0.0 && sample < 1.0);
        }
    }
}