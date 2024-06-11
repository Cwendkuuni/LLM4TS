import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionConstruction() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getLowerBound(), 0.0);
        assertEquals(1.0, dist.getUpperBound(), 0.0);

        try {
            UniformRealDistribution distNegativeUpper = new UniformRealDistribution(-1.0, 1.0);
            fail("Should have thrown NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }

        try {
            UniformRealDistribution distSameLowerAndUpper = new UniformRealDistribution(1.0, 1.0);
            fail("Should have thrown NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test
    public void testUniformRealDistributionMethods() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);

        // density
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double density = dist.density(x);
            double eps = 1e-6;
            assertTrue(Math.abs(density - 1 / (1.0 - 0.0)) <= eps);
        }

        // cumulativeProbability
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double cdf = dist.cumulativeProbability(x);
            double eps = 1e-6;
            if (x <= 0.0) {
                assertTrue(cdf == 0.0 || Math.abs(cdf) <= eps);
            } else if (x >= 1.0) {
                assertTrue(cdf == 1.0 || Math.abs(cdf - 1.0) <= eps);
            } else {
                assertTrue(Math.abs(cdf - x / (1.0 - 0.0)) <= eps);
            }
        }

        // getSolverAbsoluteAccuracy
        assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, dist.getSolverAbsoluteAccuracy(), 0.0);

        // getNumericalMean
        assertEquals(0.5, dist.getNumericalMean(), 0.0);

        // getNumericalVariance
        assertEquals(0.25 / 12.0, dist.getNumericalVariance(), 0.0);

        // getSupportLowerBound
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);

        // getSupportUpperBound
        assertEquals(1.0, dist.getSupportUpperBound(), 0.0);

        // isSupportLowerBoundInclusive
        assertTrue(dist.isSupportLowerBoundInclusive());

        // isSupportUpperBoundInclusive
        assertFalse(dist.isSupportUpperBoundInclusive());

        // isSupportConnected
        assertTrue(dist.isSupportConnected());

        // sample
        for (int i = 0; i < 10000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= dist.getSupportLowerBound());
            assertTrue(sample < dist.getSupportUpperBound());
        }
    }
}