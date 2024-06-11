import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(5, 10);
        double result = fDistribution.density(2.0);
        assertEquals(0.1326115092966054, result, 1e-15);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(5, 10);
        double result = fDistribution.cumulativeProbability(2.0);
        assertEquals(0.8183884907033946, result, 1e-15);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(5, 10);
        double result = fDistribution.getNumericalMean();
        assertEquals(Double.NaN, result, 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(5, 10);
        double result = fDistribution.getNumericalVariance();
        assertEquals(Double.NaN, result, 0.0);
    }

    @Test
    public void testSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(5, 10);
        double result = fDistribution.getSupportLowerBound();
        assertEquals(0, result, 0.0);
    }

    @Test
    public void testSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(5, 10);
        double result = fDistribution.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(5, 10);
        boolean result = fDistribution.isSupportLowerBoundInclusive();
        assertTrue(result);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(5, 10);
        boolean result = fDistribution.isSupportUpperBoundInclusive();
        assertFalse(result);
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(5, 10);
        boolean result = fDistribution.isSupportConnected();
        assertTrue(result);
    }

}