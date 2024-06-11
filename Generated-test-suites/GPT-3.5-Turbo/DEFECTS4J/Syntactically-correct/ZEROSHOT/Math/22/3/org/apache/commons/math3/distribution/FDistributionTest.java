import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution f = new FDistribution(2, 5);
        assertEquals(0.030692, f.density(0.5), 1e-6);
        assertEquals(0.0407899, f.density(1.0), 1e-6);
        assertEquals(0.0214286, f.density(2.0), 1e-6);
        assertEquals(0.0109727, f.density(3.0), 1e-6);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution f = new FDistribution(2, 5);
        assertEquals(0.050000, f.cumulativeProbability(0.1), 1e-6);
        assertEquals(0.772531, f.cumulativeProbability(1.0), 1e-6);
        assertEquals(0.950000, f.cumulativeProbability(5.0), 1e-6);
        assertEquals(0.980000, f.cumulativeProbability(10.0), 1e-6);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution f = new FDistribution(2, 5);
        assertEquals(2.0, f.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution f = new FDistribution(2, 5);
        assertEquals(5.0, f.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution f = new FDistribution(2, 5);
        assertEquals(1.25, f.getNumericalMean(), 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution f = new FDistribution(2, 5);
        assertEquals(0.625, f.getNumericalVariance(), 0.0);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution f = new FDistribution(2, 5);
        assertEquals(0.0, f.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution f = new FDistribution(2, 5);
        assertEquals(Double.POSITIVE_INFINITY, f.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution f = new FDistribution(2, 5);
        assertTrue(f.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution f = new FDistribution(2, 5);
        assertFalse(f.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution f = new FDistribution(2, 5);
        assertTrue(f.isSupportConnected());
    }
}