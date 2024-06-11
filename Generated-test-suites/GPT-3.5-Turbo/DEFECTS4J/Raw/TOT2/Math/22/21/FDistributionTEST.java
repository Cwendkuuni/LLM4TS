import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDist = new FDistribution(5, 10);
        assertEquals(0.045656197570680926, fDist.density(3.0), 1e-9);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDist = new FDistribution(5, 10);
        assertEquals(0.9345972891752496, fDist.cumulativeProbability(3.0), 1e-9);
    }

    @Test
    public void testMean() {
        FDistribution fDist = new FDistribution(5, 10);
        assertEquals(Double.NaN, fDist.getNumericalMean(), 0);
        fDist = new FDistribution(6, 10);
        assertEquals(1.6666666666666667, fDist.getNumericalMean(), 1e-9);
    }

    @Test
    public void testVariance() {
        FDistribution fDist = new FDistribution(5, 10);
        assertEquals(Double.NaN, fDist.getNumericalVariance(), 0);
        fDist = new FDistribution(7, 10);
        assertEquals(0.9210526315789474, fDist.getNumericalVariance(), 1e-9);
    }

    @Test
    public void testSupportBoundaries() {
        FDistribution fDist = new FDistribution(5, 10);
        assertTrue(fDist.isSupportLowerBoundInclusive());
        assertFalse(fDist.isSupportUpperBoundInclusive());
        assertTrue(fDist.isSupportConnected());
    }

}