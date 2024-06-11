import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getLowerBound(), 0.0);
        assertEquals(1.0, dist.getUpperBound(), 0.0);

        UniformRealDistribution dist1 = new UniformRealDistribution(0.1, 0.2);
        assertEquals(0.1, dist1.getLowerBound(), 0.0);
        assertEquals(0.2, dist1.getUpperBound(), 0.0);

        UniformRealDistribution dist2 = new UniformRealDistribution(0.1, 0.2, 0.001);
        assertEquals(0.1, dist2.getLowerBound(), 0.0);
        assertEquals(0.2, dist2.getUpperBound(), 0.0);
        assertEquals(0.001, dist2.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionConstructorWithInvalidBounds() {
        new UniformRealDistribution(1.0, 1.0);
    }

    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double density = dist.density(x);
            if (x >= dist.getLowerBound() && x < dist.getUpperBound()) {
                assertEquals(1.0 / (1.0 - 0.0), density, 0.001);
            } else {
                assertEquals(0.0, density, 0.0);
            }
        }
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (double x = dist.getLowerBound(); x <= dist.getUpperBound(); x += 0.01) {
            double cdf = dist.cumulativeProbability(x);
            if (x >= dist.getLowerBound()) {
                assertEquals(x / (dist.getUpperBound() - dist.getLowerBound()), cdf, 0.01);
            } else {
                assertEquals(0.0, cdf, 0.0);
            }
        }
    }

    @Test
    public void testSolverAbsoluteAccuracy() {
        UniformRealDistribution dist = new UniformRealDistribution();
        double x = dist.inverseCumulativeProbability(0.5);
        assertEquals(0.5, x, dist.getSolverAbsoluteAccuracy());
    }

    @Test
    public void testNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.5, dist.getNumericalMean(), 0.0);
    }

    @Test
    public void testNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution();
        double variance = dist.getNumericalVariance();
        assertEquals(0.008333333333333333, variance, 0.00001);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(1.0, dist.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (int i = 0; i < 10000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= dist.getLowerBound());
            assertTrue(sample < dist.getUpperBound());
        }
    }
}