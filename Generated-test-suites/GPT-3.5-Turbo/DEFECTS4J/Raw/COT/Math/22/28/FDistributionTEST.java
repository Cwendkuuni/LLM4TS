import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {
    private FDistribution fDistribution;

    @Before
    public void setUp() {
        fDistribution = new FDistribution(3, 4);
    }

    @Test
    public void testDensity() {
        assertEquals(0.097360, fDistribution.density(1.5), 1e-6);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.700414, fDistribution.cumulativeProbability(1.5), 1e-6);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        assertEquals(3, fDistribution.getNumeratorDegreesOfFreedom(), 0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        assertEquals(4, fDistribution.getDenominatorDegreesOfFreedom(), 0);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(4, fDistribution.getNumericalMean(), 0);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(32, fDistribution.getNumericalVariance(), 0);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, fDistribution.getSupportLowerBound(), 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue(fDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertFalse(fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(fDistribution.isSupportConnected());
    }
}