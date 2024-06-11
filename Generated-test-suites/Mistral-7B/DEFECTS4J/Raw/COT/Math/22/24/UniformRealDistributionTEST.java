import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

public class UniformRealDistributionTest {

    private UniformRealDistribution distribution;

    @Before
    public void setUp() throws NumberIsTooLargeException {
        RandomGenerator rng = new Well19937c();
        distribution = new UniformRealDistribution(rng, 0, 1);
    }

    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution urd = new UniformRealDistribution();
        assertEquals(0, urd.getLowerBound(), 0.0);
        assertEquals(1, urd.getUpperBound(), 0.0);
    }

    @Test
    public void testConstructorWithBounds() throws NumberIsTooLargeException {
        assertEquals(0, distribution.getLowerBound(), 0.0);
        assertEquals(1, distribution.getUpperBound(), 0.0);
    }

    @Test
    public void testConstructorWithBoundsAndAccuracy() throws NumberIsTooLargeException {
        assertEquals(0, distribution.getLowerBound(), 0.0);
        assertEquals(1, distribution.getUpperBound(), 0.0);
        assertEquals(DEFAULT_INVERSE_ABSOLUTE_ACCURACY, distribution.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testConstructorWithRandomGeneratorAndBounds() throws NumberIsTooLargeException {
        assertEquals(0, distribution.getLowerBound(), 0.0);
        assertEquals(1, distribution.getUpperBound(), 0.0);
    }

    @Test
    public void testConstructorWithRandomGeneratorAndBoundsAndAccuracy() throws NumberIsTooLargeException {
        assertEquals(0, distribution.getLowerBound(), 0.0);
        assertEquals(1, distribution.getUpperBound(), 0.0);
        assertEquals(DEFAULT_INVERSE_ABSOLUTE_ACCURACY, distribution.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testDensity() {
        double x = 0.5;
        assertEquals(2, Math.round(distribution.density(x) * 1000), 2);
    }

    @Test
    public void testCumulativeProbability() {
        double x = 0.25;
        assertEquals(0.25, distribution.cumulativeProbability(x), 0.0);

        x = 0.75;
        assertEquals(0.75, distribution.cumulativeProbability(x), 0.0);

        x = 0.5;
        assertEquals(0.5, distribution.cumulativeProbability(x), 0.0);

        x = 1.1;
        assertEquals(1, distribution.cumulativeProbability(x), 0.0);

        x = -0.1;
        assertEquals(0, distribution.cumulativeProbability(x), 0.0);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        assertEquals(DEFAULT_INVERSE_ABSOLUTE_ACCURACY, distribution.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(0.5, distribution.getNumericalMean(), 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(0.008888888888888889, distribution.getNumericalVariance(), 0.000000001);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, distribution.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(1, distribution.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        for (int i = 0; i < 100; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= distribution.getLowerBound());
            assertTrue(sample < distribution.getUpperBound());
        }
    }
}