import org.apache.commons.math3.distribution.*;  
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(3, 4);
        assertEquals(0.2173034653107022, fDistribution.density(0), 1e-9);
        assertEquals(0.1202229551967992, fDistribution.density(1), 1e-9);
        assertEquals(0.05579677981610561, fDistribution.density(2), 1e-9);
        assertEquals(0.02569352935023578, fDistribution.density(3), 1e-9);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(3, 4);
        assertEquals(0, fDistribution.cumulativeProbability(0), 1e-9);
        assertEquals(0.162434537867217, fDistribution.cumulativeProbability(1), 1e-9);
        assertEquals(0.3041290479567007, fDistribution.cumulativeProbability(2), 1e-9);
        assertEquals(0.431106982712888, fDistribution.cumulativeProbability(3), 1e-9);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(3, 4);
        assertEquals(3, fDistribution.getNumeratorDegreesOfFreedom(), 1e-9);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(3, 4);
        assertEquals(4, fDistribution.getDenominatorDegreesOfFreedom(), 1e-9);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(3, 4);
        assertEquals(2.6666666666666665, fDistribution.getNumericalMean(), 1e-9);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(3, 4);
        assertEquals(10.285714285714286, fDistribution.getNumericalVariance(), 1e-9);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(3, 4);
        assertEquals(0, fDistribution.getSupportLowerBound(), 1e-9);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(3, 4);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 1e-9);
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