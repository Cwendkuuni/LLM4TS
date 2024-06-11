import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution f = new FDistribution(2, 3);
        assertEquals(0.1495107851940024, f.density(1.0), 0.0001);  // Typical case
        assertEquals(0.0, f.density(0.0), 0.0001);  // Edge case
        assertEquals(0.0, f.density(-1.0), 0.0001);  // Error case
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution f = new FDistribution(2, 3);
        assertEquals(0.7578292352857844, f.cumulativeProbability(1.0), 0.0001);  // Typical case
        assertEquals(0.0, f.cumulativeProbability(0.0), 0.0001);  // Edge case
        assertEquals(0.0, f.cumulativeProbability(-1.0), 0.0001);  // Error case
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution f = new FDistribution(2, 3);
        assertEquals(3.0, f.getNumericalMean(), 0.0001);  // Typical case
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution f = new FDistribution(2, 3);
        assertEquals(18.0, f.getNumericalVariance(), 0.0001);  // Typical case
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution f = new FDistribution(2, 3);
        assertEquals(0.0, f.getSupportLowerBound(), 0.0001);  // Typical case
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution f = new FDistribution(2, 3);
        assertEquals(Double.POSITIVE_INFINITY, f.getSupportUpperBound(), 0.0001);  // Typical case
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution f = new FDistribution(2, 3);
        assertTrue(f.isSupportLowerBoundInclusive());  // Typical case
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution f = new FDistribution(2, 3);
        assertFalse(f.isSupportUpperBoundInclusive());  // Typical case
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution f = new FDistribution(2, 3);
        assertTrue(f.isSupportConnected());  // Typical case
    }

}