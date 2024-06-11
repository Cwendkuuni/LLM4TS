import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(2, 3);
        double result = fDistribution.density(1.5);
        assertEquals(0.7357588823428847, result, 1e-15);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(2, 3);
        double result = fDistribution.cumulativeProbability(1.5);
        assertEquals(0.6645648216667677, result, 1e-15);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(2, 3);
        assertEquals(2, fDistribution.getNumeratorDegreesOfFreedom(), 0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(2, 3);
        assertEquals(3, fDistribution.getDenominatorDegreesOfFreedom(), 0);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(4, 5);
        double result = fDistribution.getNumericalMean();
        assertEquals(5, result, 0);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(4, 6);
        double result = fDistribution.getNumericalVariance();
        assertEquals(8.0, result, 0);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(2, 3);
        assertEquals(0, fDistribution.getSupportLowerBound(), 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(2, 3);
        assertTrue(Double.isInfinite(fDistribution.getSupportUpperBound()));
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(2, 3);
        assertTrue(fDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(2, 3);
        assertTrue(!fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(2, 3);
        assertTrue(fDistribution.isSupportConnected());
    }
}