import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    // Test case 1
    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(0.15461558821635294, fDistribution.density(1.5), 1e-9);
    }

    // Test case 2
    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(0.8327264401518275, fDistribution.cumulativeProbability(1.5), 1e-9);
    }

    // Test case 3
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(5, fDistribution.getNumeratorDegreesOfFreedom(), 0);
    }

    // Test case 4
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(10, fDistribution.getDenominatorDegreesOfFreedom(), 0);
    }

    // Test case 5
    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(1.25, fDistribution.getNumericalMean(), 0);
    }

    // Test case 6
    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(5.357142857142858, fDistribution.getNumericalVariance(), 1e-9);
    }

    // Test case 7
    @Test
    public void testGetSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(0, fDistribution.getSupportLowerBound(), 0);
    }

    // Test case 8
    @Test
    public void testGetSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), 0);
    }

    // Test case 9
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertTrue(fDistribution.isSupportLowerBoundInclusive());
    }

    // Test case 10
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertFalse(fDistribution.isSupportUpperBoundInclusive());
    }

    // Test case 11
    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertTrue(fDistribution.isSupportConnected());
    }
}