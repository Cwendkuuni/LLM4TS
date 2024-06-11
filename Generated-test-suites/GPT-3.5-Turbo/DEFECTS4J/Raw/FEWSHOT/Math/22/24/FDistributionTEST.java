import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.distribution.FDistribution;
import org.junit.BeforeClass;
import org.junit.Test;

public class FDistributionTest {

    private static final double EPSILON = 1e-9;
    private static FDistribution distribution;

    @BeforeClass
    public static void setUp() {
        distribution = new FDistribution(5, 10);
    }

    @Test
    public void testDensity() {
        assertEquals(0, distribution.density(0), EPSILON);
        assertEquals(0, distribution.density(3), EPSILON);
        assertEquals(0, distribution.density(10), EPSILON);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0, distribution.cumulativeProbability(0), EPSILON);
        assertEquals(0, distribution.cumulativeProbability(-1), EPSILON);
        assertEquals(1, distribution.cumulativeProbability(Double.POSITIVE_INFINITY), EPSILON);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        assertEquals(5, distribution.getNumeratorDegreesOfFreedom(), EPSILON);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        assertEquals(10, distribution.getDenominatorDegreesOfFreedom(), EPSILON);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(2.5, distribution.getNumericalMean(), EPSILON);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(3.75, distribution.getNumericalVariance(), EPSILON);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, distribution.getSupportLowerBound(), EPSILON);
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(Double.POSITIVE_INFINITY, distribution.getSupportUpperBound(), EPSILON);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertEquals(true, distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertEquals(false, distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        assertEquals(true, distribution.isSupportConnected());
    }
}