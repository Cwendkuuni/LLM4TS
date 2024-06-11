import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        double result = fDistribution.density(1.5);
        assertEquals(0.15120038985629778, result, 1e-15);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        double result = fDistribution.cumulativeProbability(1.5);
        assertEquals(0.3216330192606023, result, 1e-15);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        double result = fDistribution.getNumericalMean();
        assertEquals(5.0 / 3.0, result, 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        double result = fDistribution.getNumericalVariance();
        assertEquals(72.0 / 7.0, result, 0.0);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        double result = fDistribution.getSupportLowerBound();
        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        double result = fDistribution.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        assertTrue(fDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        assertFalse(fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(3.0, 5.0);
        assertTrue(fDistribution.isSupportConnected());
    }
}