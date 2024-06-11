import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class UniformRealDistributionTest {
    private static final double EPSILON = 1e-9;

    // Test constructor
    @Test
    public void testConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0, distribution.getSupportLowerBound(), EPSILON);
        assertEquals(1, distribution.getSupportUpperBound(), EPSILON);
        assertFalse(distribution.isSupportUpperBoundInclusive());

        distribution = new UniformRealDistribution(0, 1);
        assertEquals(0, distribution.getSupportLowerBound(), EPSILON);
        assertEquals(1, distribution.getSupportUpperBound(), EPSILON);
        assertFalse(distribution.isSupportUpperBoundInclusive());

        // Test invalid input
        try {
            distribution = new UniformRealDistribution(1, 0);
            fail("Expected NumberIsTooLargeException not thrown.");
        } catch (NumberIsTooLargeException e) {
            assertEquals(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, e.getSpecificPattern());
            assertEquals(1, e.getArguments()[0]);
            assertEquals(0, e.getArguments()[1]);
        }
    }

    // Test density
    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1, distribution.density(0), EPSILON);
        assertEquals(1, distribution.density(0.5), EPSILON);
        assertEquals(1, distribution.density(1), EPSILON);
        assertEquals(0, distribution.density(-1), EPSILON);
        assertEquals(0, distribution.density(2), EPSILON);
    }

    // Test cumulativeProbability
    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0, distribution.cumulativeProbability(-1), EPSILON);
        assertEquals(0, distribution.cumulativeProbability(0), EPSILON);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), EPSILON);
        assertEquals(1, distribution.cumulativeProbability(1), EPSILON);
        assertEquals(1, distribution.cumulativeProbability(2), EPSILON);
    }

    // Test getNumericalMean
    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(0.5, distribution.getNumericalMean(), EPSILON);
    }

    // Test getNumericalVariance
    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 1);
        assertEquals(1/12.0, distribution.getNumericalVariance(), EPSILON);
    }

    // Test getSupportLowerBound
    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(2, 5);
        assertEquals(2, distribution.getSupportLowerBound(), EPSILON);
    }

    // Test getSupportUpperBound
    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(2, 5);
        assertEquals(5, distribution.getSupportUpperBound(), EPSILON);
    }

    // Test isSupportLowerBoundInclusive
    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(2, 5);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    // Test isSupportUpperBoundInclusive
    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(2, 5);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    // Test isSupportConnected
    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(2, 5);
        assertTrue(distribution.isSupportConnected());
    }

    // Test sample
    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(2, 5);
        for (int i = 0; i < 1000; i++) {
            double value = distribution.sample();
            assertTrue(value >= 2 && value < 5);
        }
    }
}