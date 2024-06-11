import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(10, 10);
        assertEquals(1.3906347382039722E-4, fDistribution.density(0.1), 0.000001);
        assertEquals(0.04137991014736145, fDistribution.density(1), 0.000001);
        assertEquals(0.10583087343591185, fDistribution.density(2), 0.000001);
        assertEquals(0.09015714567786636, fDistribution.density(3), 0.000001);
        assertEquals(0.070540085058909, fDistribution.density(4), 0.000001);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(10, 10);
        assertEquals(0, fDistribution.cumulativeProbability(0), 0.000001);
        assertEquals(2.646652780447267E-4, fDistribution.cumulativeProbability(0.1), 0.000001);
        assertEquals(0.0012875597267129167, fDistribution.cumulativeProbability(1), 0.000001);
        assertEquals(0.0021034336015947925, fDistribution.cumulativeProbability(2), 0.000001);
        assertEquals(0.0028249493767481203, fDistribution.cumulativeProbability(3), 0.000001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(10, 10);
        assertEquals(10, fDistribution.getNumeratorDegreesOfFreedom(), 0.000001);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(10, 10);
        assertEquals(10, fDistribution.getDenominatorDegreesOfFreedom(), 0.000001);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(10, 10);
        assertEquals(1, fDistribution.getNumericalMean(), 0.000001);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(10, 10);
        assertEquals(10, fDistribution.getNumericalVariance(), 0.000001);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(10, 10);
        assertEquals(0, fDistribution.getSupportLowerBound(), 0.000001);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(10, 10);
        assertNotEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(10, 10);
        assertEquals(true, fDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(10, 10);
        assertEquals(false, fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(10, 10);
        assertEquals(true, fDistribution.isSupportConnected());
    }
}