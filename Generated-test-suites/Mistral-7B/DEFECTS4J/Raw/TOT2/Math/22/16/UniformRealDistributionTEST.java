import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for UniformRealDistribution.
 */
public class UniformRealDistributionTest {

    /**
     * Test constructor with default parameters.
     */
    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getLowerBound(), 0.0);
        assertEquals(1.0, dist.getUpperBound(), 0.0);
        assertEquals(0.5, dist.getNumericalMean(), 0.0);
        assertEquals(0.25, dist.getNumericalVariance(), 0.0);
    }

    /**
     * Test constructor with custom parameters.
     */
    @Test
    public void testCustomConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 3.0);
        assertEquals(1.0, dist.getLowerBound(), 0.0);
        assertEquals(3.0, dist.getUpperBound(), 0.0);
        assertEquals(2.0, dist.getNumericalMean(), 0.0);
        assertEquals(0.361111, dist.getNumericalVariance(), 0.001);
    }

    /**
     * Test constructor with invalid parameters.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidConstructor() {
        new UniformRealDistribution(3.0, 2.0);
    }

    /**
     * Test density method.
     */
    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution();
        Well19937c rng = new Well19937c();
        for (double x = 0; x <= 1; x += 0.01) {
            double density = dist.density(x);
            double expected = 1 / (1 - 0);
            assertEquals(expected, density, 0.0001);
            UniformRealDistribution dist2 = new UniformRealDistribution(rng, x, x + 0.01, 5e-6);
            density = dist2.density(x);
            expected *= rng.nextDouble();
            assertEquals(expected, density, 0.0001);
        }
    }

    /**
     * Test cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (double x = 0; x <= 1; x += 0.01) {
            double p = dist.cumulativeProbability(x);
            double expected = x;
            assertEquals(expected, p, 0.0001);
        }
    }

    /**
     * Test sample method with default parameters.
     */
    @Test
    public void testSampleDefault() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= dist.getLowerBound());
            assertTrue(sample < dist.getUpperBound());
        }
    }

    /**
     * Test sample method with custom parameters.
     */
    @Test
    public void testSampleCustom() {
        UniformRealDistribution dist = new UniformRealDistribution(new Well19937c(), 1.5, 2.5, 5e-6);
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= dist.getLowerBound());
            assertTrue(sample < dist.getUpperBound());
        }
    }

    /**
     * Test getSupportLowerBound and getSupportUpperBound methods.
     */
    @Test
    public void testGetSupportBounds() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(1.0, dist.getSupportUpperBound(), 0.0);

        UniformRealDistribution dist2 = new UniformRealDistribution(1.0, 3.0);
        assertEquals(1.0, dist2.getSupportLowerBound(), 0.0);
        assertEquals(3.0, dist2.getSupportUpperBound(), 0.0);
    }
}