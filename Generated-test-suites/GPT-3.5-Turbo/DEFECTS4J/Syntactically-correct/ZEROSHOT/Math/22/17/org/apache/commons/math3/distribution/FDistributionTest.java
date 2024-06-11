import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class FDistributionTest {

    private FDistribution fDistribution;
    private static final double DELTA = 1e-15;

    @Before
    public void setUp() throws Exception {
        fDistribution = new FDistribution(5, 10);
    }

    @Test
    public void testDensity() {
        double density = fDistribution.density(1);
        assertEquals(0.5787037037037037, density, DELTA);
    }

    @Test
    public void testCumulativeProbability() {
        double cumulativeProbability = fDistribution.cumulativeProbability(1);
        assertEquals(0.7341389749860326, cumulativeProbability, DELTA);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = fDistribution.getNumeratorDegreesOfFreedom();
        assertEquals(5, numeratorDegreesOfFreedom, DELTA);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        double denominatorDegreesOfFreedom = fDistribution.getDenominatorDegreesOfFreedom();
        assertEquals(10, denominatorDegreesOfFreedom, DELTA);
    }

    @Test
    public void testGetNumericalMean() {
        double numericalMean = fDistribution.getNumericalMean();
        assertEquals(1.25, numericalMean, DELTA);
    }

    @Test
    public void testGetNumericalVariance() {
        double numericalVariance = fDistribution.getNumericalVariance();
        assertEquals(5.82051282051282, numericalVariance, DELTA);
    }

    @Test
    public void testGetSupportLowerBound() {
        double supportLowerBound = fDistribution.getSupportLowerBound();
        assertEquals(0, supportLowerBound, DELTA);
    }

    @Test
    public void testGetSupportUpperBound() {
        double supportUpperBound = fDistribution.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, supportUpperBound, DELTA);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        boolean isSupportLowerBoundInclusive = fDistribution.isSupportLowerBoundInclusive();
        assertEquals(true, isSupportLowerBoundInclusive);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        boolean isSupportUpperBoundInclusive = fDistribution.isSupportUpperBoundInclusive();
        assertEquals(false, isSupportUpperBoundInclusive);
    }

    @Test
    public void testIsSupportConnected() {
        boolean isSupportConnected = fDistribution.isSupportConnected();
        assertEquals(true, isSupportConnected);
    }
}