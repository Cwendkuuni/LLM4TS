import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(10.0, 20.0);
        assertEquals(0.0, fDistribution.density(-1.0), 1e-9);
        assertEquals(0.00979814235709332, fDistribution.density(2.0), 1e-9);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(10.0, 20.0);
        assertEquals(0.0, fDistribution.cumulativeProbability(-1.0), 1e-9);
        assertEquals(0.309690711437121, fDistribution.cumulativeProbability(2.0), 1e-9);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(10.0, 20.0);
        assertEquals(10.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(10.0, 20.0);
        assertEquals(20.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution fDistribution = new FDistribution(10.0, 20.0, 1e-9);
        assertEquals(1e-9, fDistribution.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(10.0, 20.0);
        assertEquals(1.33333333333333, fDistribution.getNumericalMean(), 1e-9);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(10.0, 20.0);
        assertEquals(2.10416666666667, fDistribution.getNumericalVariance(), 1e-9);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(10.0, 20.0);
        assertEquals(0.0, fDistribution.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(10.0, 20.0);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(10.0, 20.0);
        assertTrue(fDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(10.0, 20.0);
        assertFalse(fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(10.0, 20.0);
        assertTrue(fDistribution.isSupportConnected());
    }
}