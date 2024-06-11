import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well1024a;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class UniformRealDistributionTest {

    private static final double DEFAULT_EPSILON = 1e-9;

    private UniformRealDistribution distribution;
    private RandomGenerator rng;

    @Before
    public void setUp() {
        rng = new Well1024a();
    }

    // Testing constructor with default parameters

    @Test
    public void testConstructorDefault() {
        distribution = new UniformRealDistribution();
        assertEquals(0, distribution.getSupportLowerBound(), DEFAULT_EPSILON);
        assertEquals(1, distribution.getSupportUpperBound(), DEFAULT_EPSILON);
        assertEquals(0.5, distribution.getNumericalMean(), DEFAULT_EPSILON);
        assertEquals(1.0 / 12, distribution.getNumericalVariance(), DEFAULT_EPSILON);
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertTrue(!distribution.isSupportUpperBoundInclusive());
        assertTrue(distribution.isSupportConnected());
    }

    // Testing constructor with user-defined parameters

    @Test
    public void testConstructorWithBounds() {
        distribution = new UniformRealDistribution(-1, 1);
        assertEquals(-1, distribution.getSupportLowerBound(), DEFAULT_EPSILON);
        assertEquals(1, distribution.getSupportUpperBound(), DEFAULT_EPSILON);
        assertEquals(0, distribution.getNumericalMean(), DEFAULT_EPSILON);
        assertEquals(1.0 / 3, distribution.getNumericalVariance(), DEFAULT_EPSILON);
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertTrue(!distribution.isSupportUpperBoundInclusive());
        assertTrue(distribution.isSupportConnected());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithBoundsThrowsException() {
        distribution = new UniformRealDistribution(1, -1);
    }

    // Testing Random Number Generation

    @Test
    public void testSample() {
        distribution = new UniformRealDistribution(-5, 5);

        for (int i = 0; i < 1000; i++) {
            double value = distribution.sample();
            assertTrue(value >= -5 && value <= 5);
        }
    }

    // Testing Probability Density Function

    @Test
    public void testDensity() {
        distribution = new UniformRealDistribution(0, 1);

        for (int i = 0; i <= 10; i++) {
            double value = i / 10.0;
            double expectedDensity = 1;
            assertEquals(expectedDensity, distribution.density(value), DEFAULT_EPSILON);
        }

        assertEquals(0, distribution.density(-0.1), DEFAULT_EPSILON);
        assertEquals(0, distribution.density(1.1), DEFAULT_EPSILON);
    }

    // Testing Cumulative Probability Function

    @Test
    public void testCumulativeProbability() {
        distribution = new UniformRealDistribution(0, 1);

        assertEquals(0, distribution.cumulativeProbability(-1), DEFAULT_EPSILON);
        assertEquals(0, distribution.cumulativeProbability(0), DEFAULT_EPSILON);
        assertEquals(0.2, distribution.cumulativeProbability(0.2), DEFAULT_EPSILON);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), DEFAULT_EPSILON);
        assertEquals(0.8, distribution.cumulativeProbability(0.8), DEFAULT_EPSILON);
        assertEquals(1, distribution.cumulativeProbability(1), DEFAULT_EPSILON);
        assertEquals(1, distribution.cumulativeProbability(2), DEFAULT_EPSILON);
    }

    // Testing Mean

    @Test
    public void testMean() {
        distribution = new UniformRealDistribution(-10, 10);

        assertEquals(0, distribution.getNumericalMean(), DEFAULT_EPSILON);
    }

    // Testing Variance

    @Test
    public void testVariance() {
        distribution = new UniformRealDistribution(-10, 10);
        double expectedVariance = (10 - (-10)) * (10 - (-10)) / 12;

        assertEquals(expectedVariance, distribution.getNumericalVariance(), DEFAULT_EPSILON);
    }

    // Testing Support Bounds

    @Test
    public void testSupportBounds() {
        distribution = new UniformRealDistribution(-10, 10);

        assertEquals(-10, distribution.getSupportLowerBound(), DEFAULT_EPSILON);
        assertEquals(10, distribution.getSupportUpperBound(), DEFAULT_EPSILON);
    }

    @Test
    public void testSupportInclusive() {
        distribution = new UniformRealDistribution(-10, 10);

        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertTrue(!distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testSupportConnected() {
        distribution = new UniformRealDistribution(-10, 10);

        assertTrue(distribution.isSupportConnected());
    }
}