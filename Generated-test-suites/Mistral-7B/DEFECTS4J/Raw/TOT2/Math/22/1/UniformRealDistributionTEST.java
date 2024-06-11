import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionConstruction() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0, dist.getSupportLowerBound(), 0.001);
        assertEquals(1, dist.getSupportUpperBound(), 0.001);

        UniformRealDistribution distCustom = new UniformRealDistribution(1, 2);
        assertEquals(1, distCustom.getSupportLowerBound(), 0.001);
        assertEquals(2, distCustom.getSupportUpperBound(), 0.001);
    }

    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution();

        for (double x = dist.getSupportLowerBound(); x < dist.getSupportUpperBound(); x += 0.1) {
            assertEquals(1.0 / (dist.getSupportUpperBound() - dist.getSupportLowerBound()), dist.density(x), 0.01);
        }
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution();

        for (double x = dist.getSupportLowerBound(); x < dist.getSupportUpperBound(); x += 0.1) {
            double cumProb = dist.cumulativeProbability(x);
            double expected = x / (dist.getSupportUpperBound() - dist.getSupportLowerBound());
            assertEquals(expected, cumProb, 0.01);
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberIsTooLargeException() {
        try {
            new UniformRealDistribution(-1, 0);
        } catch (NumberIsTooLargeException e) {
            assertEquals("Lower bound (-1.0) must be less than upper bound (0.0)", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution();

        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= dist.getSupportLowerBound());
            assertTrue(sample < dist.getSupportUpperBound());
        }
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.5, dist.getNumericalMean(), 0.01);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.00088888888888888888, dist.getNumericalVariance(), 0.0001);
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
    public void testCustomUniformRealDistribution() {
        UniformRealDistribution dist = new UniformRealDistribution(new Well19937c(), 1, 2);

        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 1);
            assertTrue(sample < 2);
        }
    }
}