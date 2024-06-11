import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDist = new FDistribution(5.0, 10.0);
        double x = 2.0;
        assertEquals(0.096178545, fDist.density(x), 1e-9);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDist = new FDistribution(5.0, 10.0);
        double x = 2.0;
        assertEquals(0.81263805, fDist.cumulativeProbability(x), 1e-9);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(5.0, 10.0);
        assertEquals(5.0, fDist.getNumeratorDegreesOfFreedom(), 0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(5.0, 10.0);
        assertEquals(10.0, fDist.getDenominatorDegreesOfFreedom(), 0);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDist = new FDistribution(5.0, 10.0);
        assertEquals(1.25, fDist.getNumericalMean(), 0);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDist = new FDistribution(5.0, 10.0);
        assertEquals(3.75, fDist.getNumericalVariance(), 0);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDist = new FDistribution(5.0, 10.0);
        assertEquals(0, fDist.getSupportLowerBound(), 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDist = new FDistribution(5.0, 10.0);
        assertEquals(Double.POSITIVE_INFINITY, fDist.getSupportUpperBound(), 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDist = new FDistribution(5.0, 10.0);
        assertTrue(fDist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDist = new FDistribution(5.0, 10.0);
        assertFalse(fDist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fDist = new FDistribution(5.0, 10.0);
        assertTrue(fDist.isSupportConnected());
    }
}