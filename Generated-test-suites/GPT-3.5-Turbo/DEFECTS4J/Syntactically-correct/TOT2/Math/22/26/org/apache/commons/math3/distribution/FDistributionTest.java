import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(10.0, 20.0);
        assertEquals(0.0, fDistribution.density(-1.0), 0.0001);
        assertEquals(0.0437, fDistribution.density(0.5), 0.0001);
        assertEquals(0.0, fDistribution.density(Double.NEGATIVE_INFINITY), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(10.0, 20.0);
        assertEquals(0.0, fDistribution.cumulativeProbability(-1.0), 0.0001);
        assertEquals(0.0169, fDistribution.cumulativeProbability(0.5), 0.0001);
        assertEquals(1.0, fDistribution.cumulativeProbability(Double.POSITIVE_INFINITY), 0.0001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(10.0, 20.0);
        assertEquals(10.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(10.0, 20.0);
        assertEquals(20.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(10.0, 20.0);
        assertEquals(20.0 / 18.0, fDistribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(10.0, 20.0);
        assertEquals(3.3333, fDistribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(10.0, 20.0);
        assertTrue(fDistribution.getSupportLowerBound() == 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(10.0, 20.0);
        assertTrue(fDistribution.getSupportUpperBound() == Double.POSITIVE_INFINITY);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(10.0, 20.0);
        assertTrue(fDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(10.0, 20.0);
        assertTrue(!fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(10.0, 20.0);
        assertTrue(fDistribution.isSupportConnected());
    }
}