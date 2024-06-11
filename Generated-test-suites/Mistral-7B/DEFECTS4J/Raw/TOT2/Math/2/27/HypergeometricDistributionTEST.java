import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistributionTest;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest extends HypergeometricDistribution {

    @Test
    public void testConstructWithValidArguments() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, dist.getPopulationSize());
        assertEquals(5, dist.getNumberOfSuccesses());
        assertEquals(3, dist.getSampleSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructWithInvalidPopulationSize() {
        new HypergeometricDistribution(-1, 5, 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructWithInvalidPopulationSizeOrNumberOfSuccesses() {
        new HypergeometricDistribution(0, 5, 3);
        new HypergeometricDistribution(10, 0, 3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructWithInvalidSampleSize() {
        new HypergeometricDistribution(10, 5, 11);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3.0, dist.getNumericalMean(), 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3.75, dist.getNumericalVariance(), 0.001);
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.2734375, dist.cumulativeProbability(2), 0.001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.1295625, dist.probability(2), 0.001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.8704375, dist.upperCumulativeProbability(2), 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(5, dist.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, dist.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testSampling() {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3, rng);
        int x;
        for (int i = 0; i < 1000; i++) {
            x = dist.nextInt();
            assertTrue(x >= dist.getSupportLowerBound() && x <= dist.getSupportUpperBound());
        }
    }
}