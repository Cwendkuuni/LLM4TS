import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

public class HypergeometricDistributionTest {
    @Test
    public void testConstructor() {
        // Test IllegalArgumentException for invalid input values
        try {
            new HypergeometricDistribution(-1, 1, 1);
            fail("Constructor should throw NotStrictlyPositiveException for negative population size");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
        try {
            new HypergeometricDistribution(1, -1, 1);
            fail("Constructor should throw NotPositiveException for negative numberOfSuccesses");
        } catch (NotPositiveException e) {
            // Expected exception
        }
        try {
            new HypergeometricDistribution(1, 1, -1);
            fail("Constructor should throw NotPositiveException for negative sampleSize");
        } catch (NotPositiveException e) {
            // Expected exception
        }
        try {
            new HypergeometricDistribution(10, 11, 1);
            fail("Constructor should throw NumberIsTooLargeException for numberOfSuccesses > populationSize");
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        }
        try {
            new HypergeometricDistribution(10, 1, 11);
            fail("Constructor should throw NumberIsTooLargeException for sampleSize > populationSize");
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        }
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 4);
        assertEquals(0.0, dist.cumulativeProbability(0), 0.00001);
        assertEquals(0.19048, dist.cumulativeProbability(1), 0.00001);
        assertEquals(0.61904, dist.cumulativeProbability(2), 0.00001);
        assertEquals(0.95238, dist.cumulativeProbability(3), 0.00001);
        assertEquals(1.0, dist.cumulativeProbability(4), 0.00001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 4);
        assertEquals(0.0, dist.probability(0), 0.00001);
        assertEquals(0.17143, dist.probability(1), 0.00001);
        assertEquals(0.42143, dist.probability(2), 0.00001);
        assertEquals(0.30857, dist.probability(3), 0.00001);
        assertEquals(0.09854, dist.probability(4), 0.00001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 4);
        assertEquals(1.0, dist.upperCumulativeProbability(0), 0.00001);
        assertEquals(0.90476, dist.upperCumulativeProbability(1), 0.00001);
        assertEquals(0.28571, dist.upperCumulativeProbability(2), 0.00001);
        assertEquals(0.07619, dist.upperCumulativeProbability(3), 0.00001);
        assertEquals(0.0, dist.upperCumulativeProbability(4), 0.00001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 4);
        assertEquals(2.0, dist.getNumericalMean(), 0.00001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 4);
        assertEquals(1.05263, dist.getNumericalVariance(), 0.00001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 4);
        assertEquals(0, dist.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 4);
        assertEquals(4, dist.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 4);
        assertTrue(dist.isSupportConnected());
    }
}