import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UniformRealDistributionTest {

    private UniformRealDistribution distribution;
    private RandomGenerator rng;

    @Before
    public void setUp() {
        rng = new Well19937c();
        distribution = new UniformRealDistribution(rng, 0, 1);
    }

    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution uniform = new UniformRealDistribution();
        assertEquals(0, uniform.getSupportLowerBound(), 0.001);
        assertEquals(1, uniform.getSupportUpperBound(), 0.001);
    }

    @Test
    public void testConstructorWithBounds() {
        UniformRealDistribution uniform = new UniformRealDistribution(0.5, 1.5);
        assertEquals(0.5, uniform.getSupportLowerBound(), 0.001);
        assertEquals(1.5, uniform.getSupportUpperBound(), 0.001);
    }

    @Test
    public void testConstructorWithInvalidBounds() {
        assertThrows(NumberIsTooLargeException.class, () -> new UniformRealDistribution(1.5, 0.5));
    }

    @Test
    public void testConstructorWithRandomGeneratorAndBounds() {
        UniformRealDistribution uniform = new UniformRealDistribution(rng, 0.5, 1.5);
        assertEquals(0.5, uniform.getSupportLowerBound(), 0.001);
        assertEquals(1.5, uniform.getSupportUpperBound(), 0.001);
    }

    @Test
    public void testConstructorWithRandomGeneratorInvalidBounds() {
        assertThrows(NumberIsTooLargeException.class, () -> new UniformRealDistribution(rng, 1.5, 0.5));
    }

    @Test
    public void testDensity() {
        double x = 0.5;
        double density = distribution.density(x);
        assertEquals(2, density, 0.001);
    }

    @Test
    public void testCumulativeProbability() {
        double x = 0.1;
        double p = distribution.cumulativeProbability(x);
        assertEquals(0.1, p, 0.001);
    }

    @Test
    public void testCumulativeProbabilityOutsideSupport() {
        double x = 1.5;
        double p = distribution.cumulativeProbability(x);
        assertEquals(1, p, 0.001);
    }

    @Test
    public void testGetNumericalMean() {
        double mean = distribution.getNumericalMean();
        assertEquals(0.5, mean, 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        double variance = distribution.getNumericalVariance();
        assertEquals(0.0625, variance, 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        double lowerBound = distribution.getSupportLowerBound();
        assertEquals(0.0, lowerBound, 0.001);
    }

    @Test
    public void testGetSupportUpperBound() {
        double upperBound = distribution.getSupportUpperBound();
        assertEquals(1.0, upperBound, 0.001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        boolean isInclusive = distribution.isSupportLowerBoundInclusive();
        assertTrue(isInclusive);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        boolean isInclusive = distribution.isSupportUpperBoundInclusive();
        assertFalse(isInclusive);
    }

    @Test
    public void testIsSupportConnected() {
        boolean isConnected = distribution.isSupportConnected();
        assertTrue(isConnected);
    }

    @Test
    public void testSample() {
        double sample = distribution.sample();
        assertEquals(0.5, sample, 0.01);
    }
}