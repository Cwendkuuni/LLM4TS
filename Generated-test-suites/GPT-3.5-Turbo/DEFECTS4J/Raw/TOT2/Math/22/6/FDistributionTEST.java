import static org.junit.Assert.*;

import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(3, 4);
        assertEquals(0.05311321868987345, fDistribution.density(2.0), 1e-15);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(3, 4);
        assertEquals(0.4406287427278285, fDistribution.cumulativeProbability(2.0), 1e-15);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(3, 4);
        assertEquals(4.0/2.0, fDistribution.getNumericalMean(), 1e-15);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(3, 4);
        assertEquals(( 2 * (4*4) * (3 + 4 - 2) ) / ( (3 * ((4 - 2) * (4 - 2)) * (4 - 4)) ), fDistribution.getNumericalVariance(), 1e-15);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(3, 4);
        assertEquals(0.0, fDistribution.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(3, 4);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(3, 4);
        assertTrue(fDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(3, 4);
        assertFalse(fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(3, 4);
        assertTrue(fDistribution.isSupportConnected());
    }
}