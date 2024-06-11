import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionConstruction() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0, dist.getSupportLowerBound(), 0);
        assertEquals(1, dist.getSupportUpperBound(), 0);

        try {
            UniformRealDistribution distInvalid = new UniformRealDistribution(-1, 1);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }

        try {
            UniformRealDistribution distInvalid = new UniformRealDistribution(1, 0);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }

        UniformRealDistribution distCustom = new UniformRealDistribution(0.1, 0.5, 0.001);
        assertEquals(0.1, distCustom.getSupportLowerBound(), 0.001);
        assertEquals(0.5, distCustom.getSupportUpperBound(), 0.001);
    }

    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution();

        for (double x = dist.getSupportLowerBound(); x <= dist.getSupportUpperBound(); x += 0.001) {
            double density = dist.density(x);
            double expected = 1 / (dist.getSupportUpperBound() - dist.getSupportLowerBound());
            assertEquals(expected, density, 1e-6);
        }

        double lower = dist.getSupportLowerBound() - 0.01;
        double upper = dist.getSupportUpperBound() + 0.01;
        for (double x = lower; x <= upper; x += 0.001) {
            double density = dist.density(x);
            double expected = 1 / (dist.getSupportUpperBound() - dist.getSupportLowerBound());
            double error = Math.abs(density - expected);
            if (x >= dist.getSupportLowerBound() && x <= dist.getSupportUpperBound()) {
                assertTrue(error < 1e-5);
            } else {
                assertTrue(error < 1e-9);
            }
        }
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution();

        for (double x = dist.getSupportLowerBound(); x <= dist.getSupportUpperBound(); x += 0.001) {
            double p = dist.cumulativeProbability(x);
            double expected = x - dist.getSupportLowerBound();
            assertEquals(expected, p, 1e-6);
        }

        double lower = dist.getSupportLowerBound() - 0.01;
        double upper = dist.getSupportUpperBound() + 0.01;
        for (double x = lower; x <= upper; x += 0.001) {
            double p = dist.cumulativeProbability(x);
            double expected = x - dist.getSupportLowerBound();
            double error = Math.abs(p - expected);
            if (x >= dist.getSupportLowerBound() && x <= dist.getSupportUpperBound()) {
                assertTrue(error < 1e-5);
            } else {
                assertTrue(error < 1e-9);
            }
        }
    }

    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution();

        for (int i = 0; i < 100000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= dist.getSupportLowerBound());
            assertTrue(sample < dist.getSupportUpperBound());
        }

        UniformRealDistribution distCustom = new UniformRealDistribution(new Well19937c(), 0.1, 0.5, 0.001);
        for (int i = 0; i < 100000; i++) {
            double sample = distCustom.sample();
            assertTrue(sample >= distCustom.getSupportLowerBound());
            assertTrue(sample < distCustom.getSupportUpperBound());
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSampleWithInvalidArguments() {
        UniformRealDistribution dist = new UniformRealDistribution();
        dist.sample(2);
    }

    @Test
    public void testNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution();
        double mean = dist.getNumericalMean();
        assertEquals(0.5, mean, 1e-6);

        UniformRealDistribution distCustom = new UniformRealDistribution(0.1, 0.5, 0.001);
        double meanCustom = distCustom.getNumericalMean();
        assertEquals(0.3, meanCustom, 1e-6);
    }

    @Test
    public void testNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution();
        double variance = dist.getNumericalVariance();
        double expected = (dist.getSupportUpperBound() - dist.getSupportLowerBound()) * (dist.getSupportUpperBound() - dist.getSupportLowerBound()) / 12;
        assertEquals(expected, variance, 1e-6);

        UniformRealDistribution distCustom = new UniformRealDistribution(0.1, 0.5, 0.001);
        double varianceCustom = distCustom.getNumericalVariance();
        double expectedCustom = (distCustom.getSupportUpperBound() - distCustom.getSupportLowerBound()) * (distCustom.getSupportUpperBound() - distCustom.getSupportLowerBound()) / 12;
        assertEquals(expectedCustom, varianceCustom, 1e-6);
    }

}