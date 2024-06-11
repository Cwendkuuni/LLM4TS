import static org.junit.Assert.*;
import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(2, 3);
        assertEquals(0.104387981, fDistribution.density(0.5), 1e-9);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(2, 3);
        assertEquals(0.359632716, fDistribution.cumulativeProbability(0.5), 1e-9);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(2, 3);
        assertEquals(3, fDistribution.getNumericalMean(), 1e-9);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(2, 4);
        assertEquals(52, fDistribution.getNumericalVariance(), 1e-9);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(2, 3);
        assertEquals(0, fDistribution.getSupportLowerBound(), 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(2, 3);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(2, 3);
        assertTrue(fDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(2, 3);
        assertFalse(fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(2, 3);
        assertTrue(fDistribution.isSupportConnected());
    }
}