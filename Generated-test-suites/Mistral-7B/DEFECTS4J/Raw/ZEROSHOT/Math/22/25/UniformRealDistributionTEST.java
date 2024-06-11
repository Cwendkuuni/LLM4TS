import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(1.0, dist.getSupportUpperBound(), 0.0);
        assertFalse(dist.isSupportUpperBoundInclusive());
        assertTrue(dist.isSupportLowerBoundInclusive());
        assertEquals(0.5, dist.getNumericalMean(), 0.001);
        assertEquals(0.25, dist.getNumericalVariance(), 0.001);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionInvalidConstructor() {
        new UniformRealDistribution(2.0, 1.0);
    }

    @Test
    public void testUniformRealDistributionCustomConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution(0.5, 1.5);
        assertEquals(0.5, dist.getSupportLowerBound(), 0.0);
        assertEquals(1.5, dist.getSupportUpperBound(), 0.0);
        assertFalse(dist.isSupportUpperBoundInclusive());
        assertTrue(dist.isSupportLowerBoundInclusive());
        assertEquals(1.0, dist.getNumericalMean(), 0.001);
        assertEquals(0.225, dist.getNumericalVariance(), 0.001);
    }

    @Test
    public void testUniformRealDistributionCustomConstructorWithInverseCumulativeAccuracy() {
        UniformRealDistribution dist = new UniformRealDistribution(0.5, 1.5, 1e-6);
        assertEquals(0.5, dist.getSupportLowerBound(), 0.0);
        assertEquals(1.5, dist.getSupportUpperBound(), 0.0);
        assertFalse(dist.isSupportUpperBoundInclusive());
        assertTrue(dist.isSupportLowerBoundInclusive());
        assertEquals(1.0, dist.getNumericalMean(), 0.001);
        assertEquals(0.225, dist.getNumericalVariance(), 0.001);
    }

    @Test
    public void testUniformRealDistributionCustomConstructorWithRandomGenerator() {
        Well19937c rng = new Well19937c();
        UniformRealDistribution dist = new UniformRealDistribution(rng, 0.5, 1.5);
        assertEquals(0.5, dist.getSupportLowerBound(), 0.0);
        assertEquals(1.5, dist.getSupportUpperBound(), 0.0);
        assertFalse(dist.isSupportUpperBoundInclusive());
        assertTrue(dist.isSupportLowerBoundInclusive());
        assertEquals(1.0, dist.getNumericalMean(), 0.001);
        assertEquals(0.225, dist.getNumericalVariance(), 0.001);
    }

    @Test
    public void testUniformRealDistributionMethods() {
        UniformRealDistribution dist = new UniformRealDistribution(0.5, 1.5);
        double sample;

        // density
        for (double x = 0.1; x <= 1.6; x += 0.1) {
            double density = dist.density(x);
            if (x >= 0.5 && x <= 1.5) {
                assertEquals(1.0 / (1.5 - 0.5), density, 0.001);
            } else {
                assertEquals(0.0, density, 0.001);
            }
        }

        // cumulativeProbability
        for (double x = 0.0; x <= 1.6; x += 0.1) {
            double cdf = dist.cumulativeProbability(x);
            if (x <= 0.5) {
                assertEquals(0.0, cdf, 0.001);
            } else if (x >= 1.5) {
                assertEquals(1.0, cdf, 0.001);
            } else {
                assertEquals((x - 0.5) / (1.5 - 0.5), cdf, 0.001);
            }
        }

        // sample
        for (int i = 0; i < 1000; i++) {
            sample = dist.sample();
            assertTrue(sample >= 0.5 && sample < 1.5);
        }
    }
}