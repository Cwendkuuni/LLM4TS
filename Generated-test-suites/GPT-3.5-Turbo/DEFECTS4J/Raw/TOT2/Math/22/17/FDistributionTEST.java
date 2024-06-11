import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDist = new FDistribution(10, 15);
        assertEquals(0.0, fDist.density(0.0), 0.0001);
        assertEquals(0.0337, fDist.density(0.5), 0.0001);
        // Add more test scenarios for density method
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDist = new FDistribution(10, 15);
        assertEquals(0.0, fDist.cumulativeProbability(0.0), 0.0001);
        assertEquals(0.0742, fDist.cumulativeProbability(0.5), 0.0001);
        // Add more test scenarios for cumulativeProbability method
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDist = new FDistribution(10, 15);
        assertEquals(Double.NaN, fDist.getNumericalMean(), 0.0001);
        // Add more test scenarios for getNumericalMean method
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDist = new FDistribution(10, 15);
        assertEquals(Double.NaN, fDist.getNumericalVariance(), 0.0001);
        // Add more test scenarios for getNumericalVariance method
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDist = new FDistribution(10, 15);
        assertEquals(0, fDist.getSupportLowerBound(), 0.0001);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDist = new FDistribution(10, 15);
        assertEquals(Double.POSITIVE_INFINITY, fDist.getSupportUpperBound(), 0.0001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDist = new FDistribution(10, 15);
        assertTrue(fDist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDist = new FDistribution(10, 15);
        assertFalse(fDist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fDist = new FDistribution(10, 15);
        assertTrue(fDist.isSupportConnected());
        // Add more test scenarios for isSupportConnected method
    }
}