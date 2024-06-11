import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fdistribution = new FDistribution(3, 4);

        // Typical case
        assertEquals(0.1806722689, fdistribution.density(1), 1e-9);

        // Edge case
        assertEquals(0.0, fdistribution.density(0), 1e-9);

        // Error scenario
        try {
            fdistribution.density(-1);
        } catch (IllegalArgumentException e) {
            assertEquals("x must be >= 0", e.getMessage());
        }
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fdistribution = new FDistribution(3, 4);

        // Typical case
        assertEquals(0.4755821906, fdistribution.cumulativeProbability(1), 1e-9);

        // Edge case
        assertEquals(0.0, fdistribution.cumulativeProbability(0), 1e-9);

        // Error scenario
        try {
            fdistribution.cumulativeProbability(-1);
        } catch (IllegalArgumentException e) {
            assertEquals("x must be >= 0", e.getMessage());
        }
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fdistribution = new FDistribution(3, 4);

        // Typical case
        assertEquals(3, fdistribution.getNumeratorDegreesOfFreedom(), 1e-9);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fdistribution = new FDistribution(3, 4);

        // Typical case
        assertEquals(4, fdistribution.getDenominatorDegreesOfFreedom(), 1e-9);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fdistribution = new FDistribution(3, 4);

        // Typical case
        assertEquals(2, fdistribution.getNumericalMean(), 1e-9);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fdistribution = new FDistribution(3, 4);

        // Typical case
        assertEquals(24, fdistribution.getNumericalVariance(), 1e-9);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fdistribution = new FDistribution(3, 4);

        // Typical case
        assertEquals(0, fdistribution.getSupportLowerBound(), 1e-9);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fdistribution = new FDistribution(3, 4);

        // Typical case
        assertEquals(Double.POSITIVE_INFINITY, fdistribution.getSupportUpperBound(), 1e-9);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fdistribution = new FDistribution(3, 4);

        // Typical case
        assertTrue(fdistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fdistribution = new FDistribution(3, 4);

        // Typical case
        assertFalse(fdistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fdistribution = new FDistribution(3, 4);

        // Typical case
        assertTrue(fdistribution.isSupportConnected());
    }
}