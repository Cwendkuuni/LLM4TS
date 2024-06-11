import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class FDistributionTest {
    private static final double EPSILON = 1e-9;

    @Test
    public void testDensity() {
        FDistribution distribution = new FDistribution(5, 10);

        assertEquals(0.0521998435, distribution.density(0.2), EPSILON);
        assertEquals(0.0857193034, distribution.density(0.4), EPSILON);
        assertEquals(0.119644426, distribution.density(0.6), EPSILON);
        assertEquals(0.154105296, distribution.density(0.8), EPSILON);
        assertEquals(0.189282161, distribution.density(1.0), EPSILON);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution distribution = new FDistribution(5, 10);

        assertEquals(0.0304858127, distribution.cumulativeProbability(0.2), EPSILON);
        assertEquals(0.108483863, distribution.cumulativeProbability(0.4), EPSILON);
        assertEquals(0.255787964, distribution.cumulativeProbability(0.6), EPSILON);
        assertEquals(0.50475205, distribution.cumulativeProbability(0.8), EPSILON);
        assertEquals(0.76033981, distribution.cumulativeProbability(1.0), EPSILON);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(5, 10);

        assertEquals(5, distribution.getNumeratorDegreesOfFreedom(), EPSILON);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(5, 10);

        assertEquals(10, distribution.getDenominatorDegreesOfFreedom(), EPSILON);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution distribution = new FDistribution(5, 10);

        assertEquals(Double.NaN, distribution.getNumericalMean(), EPSILON);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution distribution = new FDistribution(5, 10);

        assertEquals(0.147058823529412, distribution.getNumericalVariance(), EPSILON);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution distribution = new FDistribution(5, 10);

        assertEquals(0, distribution.getSupportLowerBound(), EPSILON);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution distribution = new FDistribution(5, 10);

        assertEquals(Double.POSITIVE_INFINITY, distribution.getSupportUpperBound(), EPSILON);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution distribution = new FDistribution(5, 10);

        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution distribution = new FDistribution(5, 10);

        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution distribution = new FDistribution(5, 10);

        assertTrue(distribution.isSupportConnected());
    }
}