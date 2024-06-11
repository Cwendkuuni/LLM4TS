import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testCumulativeProbability() {
        // Typical use case
        assertEquals(0.5982515982515983, new HypergeometricDistribution(10, 3, 5).cumulativeProbability(2), 0.0001);

        // Edge case
        assertEquals(0.0, new HypergeometricDistribution(10, 3, 5).cumulativeProbability(-1), 0.0);
    }

    @Test
    public void testUpperCumulativeProbability() {
        // Typical use case
        assertEquals(0.3333333333333333, new HypergeometricDistribution(10, 3, 5).upperCumulativeProbability(1), 0.0001);

        // Edge case
        assertEquals(1.0, new HypergeometricDistribution(10, 3, 5).upperCumulativeProbability(6), 0.0);
    }

    @Test
    public void testProbability() {
        // Typical use case
        assertEquals(0.20147420147420147, new HypergeometricDistribution(10, 3, 5).probability(2), 0.0001);

        // Edge case
        assertEquals(0.0, new HypergeometricDistribution(10, 3, 5).probability(-1), 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        // Typical use case
        assertEquals(1.5, new HypergeometricDistribution(10, 3, 5).getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        // Typical use case
        assertEquals(0.32142857142857145, new HypergeometricDistribution(10, 3, 5).getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        // Typical use case
        assertEquals(0, new HypergeometricDistribution(10, 3, 5).getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        // Typical use case
        assertEquals(3, new HypergeometricDistribution(10, 3, 5).getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        // Typical use case
        assertTrue(new HypergeometricDistribution(10, 3, 5).isSupportConnected());
    }
}