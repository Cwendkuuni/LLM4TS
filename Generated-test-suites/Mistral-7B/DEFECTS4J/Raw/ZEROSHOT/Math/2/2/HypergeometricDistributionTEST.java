import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.AbstractIntegerDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        try {
            new HypergeometricDistribution(1, 1, 1);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            fail("Expected no exceptions, but got: " + e.getMessage());
        }

        try {
            new HypergeometricDistribution(1, 1, -1);
            fail("Expected NotPositiveException but no exception was thrown.");
        } catch (NotPositiveException e) {
        }

        try {
            new HypergeometricDistribution(0, 1, 1);
            fail("Expected NotStrictlyPositiveException but no exception was thrown.");
        } catch (NotStrictlyPositiveException e) {
        }

        try {
            new HypergeometricDistribution(1, 3, 4);
            fail("Expected NumberIsTooLargeException but no exception was thrown.");
        } catch (NumberIsTooLargeException e) {
        }

        try {
            new HypergeometricDistribution(new Well19937c(), 1, 1, 1);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            fail("Expected no exceptions, but got: " + e.getMessage());
        }

        try {
            new HypergeometricDistribution(new Well19937c(), 1, 1, -1);
            fail("Expected NotPositiveException but no exception was thrown.");
        } catch (NotPositiveException e) {
        }

        try {
            new HypergeometricDistribution(new Well19937c(), 0, 1, 1);
            fail("Expected NotStrictlyPositiveException but no exception was thrown.");
        } catch (NotStrictlyPositiveException e) {
        }

        try {
            new HypergeometricDistribution(new Well19937c(), 1, 3, 4);
            fail("Expected NumberIsTooLargeException but no exception was thrown.");
        } catch (NumberIsTooLargeException e) {
        }
    }

    @Test
    public void testDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        int[] domain = dist.getDomain();
        assertEquals(0, domain[0]);
        assertEquals(5, domain[1]);
    }

    @Test
    public void testNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(5.0, dist.getNumericalMean(), 0.001);
    }

    @Test
    public void testNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(1.9832828165517067, dist.getNumericalVariance(), 0.001);
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(0.0, dist.cumulativeProbability(0), 0.001);
        assertEquals(0.2734375, dist.cumulativeProbability(2), 0.001);
        assertEquals(1.0, dist.cumulativeProbability(5), 0.001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(0.16666666666666668, dist.probability(2), 0.001);
        assertEquals(0.0, dist.probability(6), 0.001);
        assertEquals(0.36028571428571427, dist.probability(3), 0.001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(0.8333333333333333, dist.upperCumulativeProbability(2), 0.001);
        assertEquals(1.0, dist.upperCumulativeProbability(5), 0.001);
        assertEquals(0.0, dist.upperCumulativeProbability(6), 0.001);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(5, dist.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(10, dist.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(5, dist.getSampleSize());
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(5, dist.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(5, dist.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertTrue(dist.isSupportConnected());
    }
}