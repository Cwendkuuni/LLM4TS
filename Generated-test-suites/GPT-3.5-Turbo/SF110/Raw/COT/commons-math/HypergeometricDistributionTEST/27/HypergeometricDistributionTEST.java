
package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstructor() {
        try {
            new HypergeometricDistribution(-1, 1, 1);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Success
        } catch (Exception e) {
            fail("Unexpected Exception");
        }

        try {
            new HypergeometricDistribution(1, -1, 1);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Success
        } catch (Exception e) {
            fail("Unexpected Exception");
        }

        try {
            new HypergeometricDistribution(1, 1, -1);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Success
        } catch (Exception e) {
            fail("Unexpected Exception");
        }

        try {
            new HypergeometricDistribution(1, 2, 1);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Success
        } catch (Exception e) {
            fail("Unexpected Exception");
        }

        try {
            new HypergeometricDistribution(1, 1, 2);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Success
        } catch (Exception e) {
            fail("Unexpected Exception");
        }
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.0714, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.2857, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(0.6428, distribution.cumulativeProbability(3), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(4), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(5), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(6), 0.0001);
    }

    @Test
    public void testHypergeometricDistributionProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(1.0, distribution.probability(0), 0.0001);
        assertEquals(0.2381, distribution.probability(1), 0.0001);
        assertEquals(0.5714, distribution.probability(2), 0.0001);
        assertEquals(0.1904, distribution.probability(3), 0.0001);
        assertEquals(0.0, distribution.probability(4), 0.0001);
        assertEquals(0.0, distribution.probability(5), 0.0001);
        assertEquals(0.0, distribution.probability(6), 0.0001);
    }

    @Test
    public void testHypergeometricDistributionUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(1.0, distribution.upperCumulativeProbability(-1), 0.0001);
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.9285, distribution.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.6428, distribution.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.3571, distribution.upperCumulativeProbability(3), 0.0001);
        assertEquals(0.0714, distribution.upperCumulativeProbability(4), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(5), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(6), 0.0001);
    }

    @Test
    public void testHypergeometricDistributionMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(1.5, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testHypergeometricDistributionVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(0.6429, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testHypergeometricDistributionSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testHypergeometricDistributionSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(3, distribution.getSupportUpperBound());
    }

    @Test
    public void testHypergeometricDistributionIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(true, distribution.isSupportConnected());
    }

}

