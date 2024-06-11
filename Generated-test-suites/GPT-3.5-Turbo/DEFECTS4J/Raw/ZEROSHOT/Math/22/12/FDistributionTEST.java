import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution f = new FDistribution(3, 5);
        assertEquals(0.02955609078941051, f.density(2.0), 1e-15);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution f = new FDistribution(3, 5);
        assertEquals(0.9044274361266087, f.cumulativeProbability(2.0), 1e-15);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution f = new FDistribution(3, 5);
        assertEquals(3.0, f.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution f = new FDistribution(3, 5);
        assertEquals(5.0, f.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution f = new FDistribution(3, 5);
        assertEquals(3.3333333333333335, f.getNumericalMean(), 1e-15);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution f = new FDistribution(3, 5);
        assertEquals(4.166666666666667, f.getNumericalVariance(), 1e-15);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution f = new FDistribution(3, 5);
        assertEquals(0.0, f.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution f = new FDistribution(3, 5);
        assertEquals(Double.POSITIVE_INFINITY, f.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution f = new FDistribution(3, 5);
        assertTrue(f.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution f = new FDistribution(3, 5);
        assertFalse(f.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution f = new FDistribution(3, 5);
        assertTrue(f.isSupportConnected());
    }
}