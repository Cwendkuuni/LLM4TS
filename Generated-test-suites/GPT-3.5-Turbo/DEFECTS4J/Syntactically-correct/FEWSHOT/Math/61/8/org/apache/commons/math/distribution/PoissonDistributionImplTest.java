import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.junit.Test;

public class PoissonDistributionImplTest {
    private static final double DEFAULT_EPSILON = 1E-12;
    private static final int DEFAULT_MAX_ITERATIONS = 10000000;

    @Test
    public void testConstructor() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        assertEquals(1.5, distribution.getMean(), DEFAULT_EPSILON);
        assertEquals(DEFAULT_MAX_ITERATIONS, distribution.getMaxIterations());
        assertEquals(DEFAULT_EPSILON, distribution.getEpsilon(), DEFAULT_EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeMean() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithZeroMean() {
        new PoissonDistributionImpl(0);
    }

    @Test
    public void testConstructorWithCustomMaxIterations() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5, 0.01, 500000);
        assertEquals(1.5, distribution.getMean(), DEFAULT_EPSILON);
        assertEquals(500000, distribution.getMaxIterations());
        assertEquals(0.01, distribution.getEpsilon(), DEFAULT_EPSILON);
    }

    @Test
    public void testConstructorWithCustomEpsilon() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5, 0.01);
        assertEquals(1.5, distribution.getMean(), DEFAULT_EPSILON);
        assertEquals(DEFAULT_MAX_ITERATIONS, distribution.getMaxIterations());
        assertEquals(0.01, distribution.getEpsilon(), DEFAULT_EPSILON);
    }

    @Test
    public void testConstructorWithCustomEpsilonAndMaxIterations() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5, 0.01, 500000);
        assertEquals(1.5, distribution.getMean(), DEFAULT_EPSILON);
        assertEquals(500000, distribution.getMaxIterations());
        assertEquals(0.01, distribution.getEpsilon(), DEFAULT_EPSILON);
    }

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, distribution.getMean(), DEFAULT_EPSILON);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.0820849986238988, distribution.probability(2), DEFAULT_EPSILON);
        assertEquals(0.0230599363338986, distribution.probability(4), DEFAULT_EPSILON);
        assertEquals(0.0667327756591666, distribution.probability(3), DEFAULT_EPSILON);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.543813456262795, distribution.cumulativeProbability(3), DEFAULT_EPSILON);
        assertEquals(0.543813456262795, distribution.cumulativeProbability(3.0), DEFAULT_EPSILON);
        assertEquals(0.830214849834692, distribution.cumulativeProbability(5), DEFAULT_EPSILON);
        assertEquals(1.0, distribution.cumulativeProbability(Integer.MAX_VALUE), DEFAULT_EPSILON);
    }

    @Test
    public void testCumulativeProbabilityWithNegativeX() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.0, distribution.cumulativeProbability(-5), DEFAULT_EPSILON);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.8871465590186347, distribution.normalApproximateProbability(3), DEFAULT_EPSILON);
        assertEquals(0.6990710144415972, distribution.normalApproximateProbability(5), DEFAULT_EPSILON);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        for (int i = 0; i < 100000; i++) {
            int value = distribution.sample();
            assertTrue(value >= 0 && value <= Integer.MAX_VALUE);
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0, distribution.getDomainLowerBound(0.0), DEFAULT_EPSILON);
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.0), DEFAULT_EPSILON);
    }
}