import org.apache.commons.math3.distribution.FDistribution;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FDistributionTest {

    private static final double EPSILON = 1e-9;

    private FDistribution fDistribution;

    @Before
    public void setup() {
        fDistribution = new FDistribution(5, 10);
    }

    @Test
    public void testDensity() {
        // Expected values calculated using R statistics
        assertEquals(0.0235481550, fDistribution.density(0.5), EPSILON);
        assertEquals(0.0555887593, fDistribution.density(1.0), EPSILON);
        assertEquals(0.0701807006, fDistribution.density(1.5), EPSILON);
    }

    @Test
    public void testCumulativeProbability() {
        // Expected values calculated using R statistics
        assertEquals(0.0130286871, fDistribution.cumulativeProbability(1.0), EPSILON);
        assertEquals(0.0322768945, fDistribution.cumulativeProbability(1.5), EPSILON);
        assertEquals(0.0508557154, fDistribution.cumulativeProbability(2.0), EPSILON);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        assertEquals(5, fDistribution.getNumeratorDegreesOfFreedom(), EPSILON);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        assertEquals(10, fDistribution.getDenominatorDegreesOfFreedom(), EPSILON);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(1.25, fDistribution.getNumericalMean(), EPSILON);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(6.9444444, fDistribution.getNumericalVariance(), EPSILON);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, fDistribution.getSupportLowerBound(), EPSILON);
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), EPSILON);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertEquals(true, fDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertEquals(false, fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        assertEquals(true, fDistribution.isSupportConnected());
    }
}