import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(100, -10, 50);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Pass
        }

        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(0, 10, 50);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Pass
        }

        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(100, 110, 50);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Pass
        }

        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(100, 10, 150);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Pass
        }

        assertDoesNotThrow(() -> {
            HypergeometricDistribution dist = new HypergeometricDistribution(100, 10, 50);
        });
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 10, 50);
        assertEquals(5.0, dist.getNumericalMean(), 0.0001);

        dist = new HypergeometricDistribution(1000, 500, 100);
        assertEquals(50.0, dist.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 10, 50);
        assertEquals(2.7755, dist.getNumericalVariance(), 0.0001);

        dist = new HypergeometricDistribution(1000, 500, 100);
        assertEquals(4.5918, dist.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 10, 50);
        assertEquals(0, dist.getSupportLowerBound());

        dist = new HypergeometricDistribution(1000, 500, 100);
        assertEquals(0, dist.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 10, 50);
        assertEquals(10, dist.getSupportUpperBound());

        dist = new HypergeometricDistribution(1000, 500, 100);
        assertEquals(100, dist.getSupportUpperBound());
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 10, 50);
        assertEquals(0.523366, dist.probability(5), 0.0001);
        assertEquals(0.855302, dist.probability(8), 0.0001);
        assertEquals(0.0, dist.probability(15), 0.0001);

        dist = new HypergeometricDistribution(1000, 500, 100);
        assertEquals(0.078235, dist.probability(40), 0.0001);
        assertEquals(0.001078, dist.probability(80), 0.0001);
        assertEquals(0.0, dist.probability(200), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 10, 50);
        assertEquals(0.000015, dist.cumulativeProbability(5), 0.0001);
        assertEquals(0.03855, dist.cumulativeProbability(8), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(15), 0.0001);

        dist = new HypergeometricDistribution(1000, 500, 100);
        assertEquals(0.999999, dist.cumulativeProbability(40), 0.0001);
        assertEquals(0.998043, dist.cumulativeProbability(80), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(200), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 10, 50);
        assertEquals(1.0, dist.upperCumulativeProbability(5), 0.0001);
        assertEquals(0.961449, dist.upperCumulativeProbability(8), 0.0001);
        assertEquals(0.0, dist.upperCumulativeProbability(15), 0.0001);

        dist = new HypergeometricDistribution(1000, 500, 100);
        assertEquals(1.0, dist.upperCumulativeProbability(40), 0.0001);
        assertEquals(0.001956, dist.upperCumulativeProbability(80), 0.0001);
        assertEquals(0.0, dist.upperCumulativeProbability(200), 0.0001);
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(100, 10, 50);
        assertTrue(dist.isSupportConnected());

        dist = new HypergeometricDistribution(1000, 500, 100);
        assertTrue(dist.isSupportConnected());
    }

}