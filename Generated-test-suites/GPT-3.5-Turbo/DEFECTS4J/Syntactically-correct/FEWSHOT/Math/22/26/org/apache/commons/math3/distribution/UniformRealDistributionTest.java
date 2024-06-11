/**
 * JUnit test cases for UniformRealDistribution class
 */
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class UniformRealDistributionTest {

    private final double EPSILON = 0.000001;

    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0, distribution.getSupportLowerBound(), EPSILON);
        assertEquals(1, distribution.getSupportUpperBound(), EPSILON);
    }

    @Test
    public void testConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution(2.5, 5.5);
        assertEquals(2.5, distribution.getSupportLowerBound(), EPSILON);
        assertEquals(5.5, distribution.getSupportUpperBound(), EPSILON);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidBounds() {
        new UniformRealDistribution(5.5, 2.5);
    }

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(2, 5);
        assertEquals(0, distribution.density(1), EPSILON);
        assertEquals(0.25, distribution.density(2.5), EPSILON);
        assertEquals(0.25, distribution.density(3), EPSILON);
        assertEquals(0.25, distribution.density(4), EPSILON);
        assertEquals(0.25, distribution.density(4.5), EPSILON);
        assertEquals(0, distribution.density(5), EPSILON);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(3, 7);
        assertEquals(0, distribution.cumulativeProbability(2.5), EPSILON);
        assertEquals(0, distribution.cumulativeProbability(3), EPSILON);
        assertEquals(0.25, distribution.cumulativeProbability(4), EPSILON);
        assertEquals(0.5, distribution.cumulativeProbability(5), EPSILON);
        assertEquals(0.75, distribution.cumulativeProbability(6), EPSILON);
        assertEquals(1, distribution.cumulativeProbability(7), EPSILON);
        assertEquals(1, distribution.cumulativeProbability(8), EPSILON);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(1, 5);
        assertEquals(3, distribution.getNumericalMean(), EPSILON);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);
        assertEquals(8.333333333333334, distribution.getNumericalVariance(), EPSILON);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(-2, 8);
        assertEquals(-2, distribution.getSupportLowerBound(), EPSILON);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(-5, 5);
        assertEquals(5, distribution.getSupportUpperBound(), EPSILON);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 5);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 5);
        assertTrue(!distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 5);
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        RandomGenerator generator = new Well19937c();
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10, 0.001);

        for (int i = 0; i < 100; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= 0 && sample <= 10);
        }
    }
}