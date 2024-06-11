import static org.junit.Assert.*;
import org.junit.Test;

public class FDistributionTest {
    
    private static final double EPSILON = 1e-9;

    @Test
    public void testDensity() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(0.068682, fDistribution.density(1.0), EPSILON);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(0.442719, fDistribution.cumulativeProbability(1.0), EPSILON);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(10.0 / 8.0, fDistribution.getNumericalMean(), EPSILON);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(500.0 / 188.0, fDistribution.getNumericalVariance(), EPSILON);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidDegreesOfFreedom() {
        new FDistribution(-1, 10);
    }

    @Test
    public void testSupportLowerBound() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(0.0, fDistribution.getSupportLowerBound(), EPSILON);
    }

    @Test
    public void testSupportUpperBound() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.getSupportUpperBound(), EPSILON);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertTrue(fDistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertFalse(fDistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fDistribution = new FDistribution(5, 10);
        assertTrue(fDistribution.isSupportConnected());
    }
}