import org.junit.Test;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.distribution.*;

import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstructor() {
        // Typical use case
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            assertNotNull(dist);
        } catch (Exception e) {
            fail("Unexpected exception in constructor: " + e.getMessage());
        }

        // Edge case: populationSize = 0
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(0, 5, 3);
            fail("Expected NotStrictlyPositiveException for populationSize = 0");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception in constructor: " + e.getMessage());
        }

        // Edge case: numberOfSuccesses < 0
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, -1, 3);
            fail("Expected NotPositiveException for numberOfSuccesses < 0");
        } catch (NotPositiveException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception in constructor: " + e.getMessage());
        }

        // Edge case: sampleSize < 0
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, -1);
            fail("Expected NotPositiveException for sampleSize < 0");
        } catch (NotPositiveException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception in constructor: " + e.getMessage());
        }

        // Edge case: numberOfSuccesses > populationSize
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 11, 3);
            fail("Expected NumberIsTooLargeException for numberOfSuccesses > populationSize");
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception in constructor: " + e.getMessage());
        }

        // Edge case: sampleSize > populationSize
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 11);
            fail("Expected NumberIsTooLargeException for sampleSize > populationSize");
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception in constructor: " + e.getMessage());
        }
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        // Typical use case
        assertEquals(0.588, dist.cumulativeProbability(2), 0.001);

        // Edge case: x < lower domain
        assertEquals(0.0, dist.cumulativeProbability(-1), 0.001);

        // Edge case: x = lower domain
        assertEquals(0.0, dist.cumulativeProbability(0), 0.001);

        // Edge case: x = upper domain
        assertEquals(1.0, dist.cumulativeProbability(3), 0.001);

        // Edge case: x > upper domain
        assertEquals(1.0, dist.cumulativeProbability(4), 0.001);
    }

    @Test
    public void testHypergeometricDistributionProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        // Typical use case
        assertEquals(0.343, dist.probability(2), 0.001);

        // Edge case: x < lower domain
        assertEquals(0.0, dist.probability(-1), 0.001);

        // Edge case: x = lower domain
        assertEquals(0.0, dist.probability(0), 0.001);

        // Edge case: x = upper domain
        assertEquals(0.294, dist.probability(3), 0.001);

        // Edge case: x > upper domain
        assertEquals(0.0, dist.probability(4), 0.001);
    }

    @Test
    public void testHypergeometricDistributionUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        // Typical use case
        assertEquals(0.408, dist.upperCumulativeProbability(2), 0.001);

        // Edge case: x <= lower domain
        assertEquals(1.0, dist.upperCumulativeProbability(-1), 0.001);

        // Edge case: x = lower domain
        assertEquals(1.0, dist.upperCumulativeProbability(0), 0.001);

        // Edge case: x = upper domain
        assertEquals(0.0, dist.upperCumulativeProbability(3), 0.001);

        // Edge case: x > upper domain
        assertEquals(0.0, dist.upperCumulativeProbability(4), 0.001);
    }

    @Test
    public void testHypergeometricDistributionMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        // Typical use case
        assertEquals(1.5, dist.getNumericalMean(), 0.001);

        // Edge case: populationSize = 0
        HypergeometricDistribution distZeroPopulation = new HypergeometricDistribution(0, 5, 3);
        assertEquals(0.0, distZeroPopulation.getNumericalMean(), 0.001);
    }

    @Test
    public void testHypergeometricDistributionVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        // Typical use case
        assertEquals(0.225, dist.getNumericalVariance(), 0.001);

        // Edge case: populationSize = 0
        HypergeometricDistribution distZeroPopulation = new HypergeometricDistribution(0, 5, 3);
        assertEquals(0.0, distZeroPopulation.getNumericalVariance(), 0.001);
    }

    @Test
    public void testHypergeometricDistributionSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        // Typical use case
        assertEquals(3, dist.getSupportLowerBound());

        // Edge case: sampleSize + numberOfSuccesses - populationSize
        HypergeometricDistribution distZeroSupport = new HypergeometricDistribution(10, 5, 8);
        assertEquals(2, distZeroSupport.getSupportLowerBound());

        // Edge case: populationSize = 0
        HypergeometricDistribution distZeroPopulation = new HypergeometricDistribution(0, 5, 3);
        assertEquals(8, distZeroPopulation.getSupportLowerBound());
    }

    @Test
    public void testHypergeometricDistributionSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        // Typical use case
        assertEquals(3, dist.getSupportUpperBound());

        // Edge case: numberOfSuccesses
        HypergeometricDistribution distZeroSupport = new HypergeometricDistribution(10, 3, 3);
        assertEquals(3, distZeroSupport.getSupportUpperBound());

        // Edge case: sampleSize + numberOfSuccesses - populationSize
        HypergeometricDistribution distMaxSupport = new HypergeometricDistribution(10, 7, 8);
        assertEquals(5, distMaxSupport.getSupportUpperBound());
    }

    @Test
    public void testHypergeometricDistributionIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        // Typical use case
        assertTrue(dist.isSupportConnected());
    }
}