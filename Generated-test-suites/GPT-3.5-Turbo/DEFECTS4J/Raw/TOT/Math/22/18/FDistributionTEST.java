import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fd = new FDistribution(5, 10);
        
        // Typical use case
        double density1 = fd.density(2);
        assertEquals(0.0541321, density1, 0.00001);
        
        // Edge case: x = 0
        double density2 = fd.density(0);
        assertEquals(0, density2, 0);
        
        // Error scenario: x < 0
        try {
            double density3 = fd.density(-2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fd = new FDistribution(5, 10);
        
        // Typical use case
        double cumulativeProb1 = fd.cumulativeProbability(2);
        assertEquals(0.833333, cumulativeProb1, 0.000001);
        
        // Edge case: x = 0
        double cumulativeProb2 = fd.cumulativeProbability(0);
        assertEquals(0, cumulativeProb2, 0);
        
        // Error scenario: x < 0
        try {
            double cumulativeProb3 = fd.cumulativeProbability(-2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fd1 = new FDistribution(5, 10);
        assertEquals(1.25, fd1.getNumericalMean(), 0);
        
        FDistribution fd2 = new FDistribution(2, 10);
        assertEquals(Double.NaN, fd2.getNumericalMean(), 0);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fd1 = new FDistribution(5, 10);
        assertEquals(5.555555, fd1.getNumericalVariance(), 0.000001);
        
        FDistribution fd2 = new FDistribution(2, 10);
        assertEquals(Double.NaN, fd2.getNumericalVariance(), 0);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fd = new FDistribution(5, 10);
        assertEquals(0, fd.getSupportLowerBound(), 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fd = new FDistribution(5, 10);
        assertEquals(Double.POSITIVE_INFINITY, fd.getSupportUpperBound(), 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fd = new FDistribution(5, 10);
        assertTrue(fd.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fd = new FDistribution(5, 10);
        assertFalse(fd.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fd = new FDistribution(5, 10);
        assertTrue(fd.isSupportConnected());
    }
}