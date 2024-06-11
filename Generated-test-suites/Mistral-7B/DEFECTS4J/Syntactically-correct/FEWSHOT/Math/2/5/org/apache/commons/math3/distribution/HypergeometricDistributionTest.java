package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstruction() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(1, 1, 1);
            assertNotNull(dist);
        } catch (NotStrictlyPositiveException | NotPositiveException | NumberIsTooLargeException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(0, 1, 1);
            assertNull(dist);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(1, 2, 3);
            assertNull(dist);
        } catch (NotPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (NumberIsTooLargeException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(1, 1, 0);
            assertNull(dist);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(1, 1, -1);
            assertNull(dist);
        } catch (NotPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(5, 2, 3);
        int[] domain = dist.getDomain();
        assertEquals(2, domain[0]);
        assertEquals(3, domain[1]);
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        double cumulativeProbability;

        cumulativeProbability = dist.cumulativeProbability(0);
        assertEquals(0.0, cumulativeProbability, 0.001);

        cumulativeProbability = dist.cumulativeProbability(5);
        assertEquals(0.4394937415134787, cumulativeProbability, 0.001);

        cumulativeProbability = dist.cumulativeProbability(10);
        assertEquals(1.0, cumulativeProbability, 0.001);

        cumulativeProbability = dist.cumulativeProbability(11);
        assertEquals(1.0, cumulativeProbability, 0.001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 3);
        double probability;

        probability = dist.probability(0);
        assertEquals(0.03046835382364221, probability, 0.001);

        probability = dist.probability(3);
        assertEquals(0.27066315789473687, probability, 0.001);

        probability = dist.probability(5);
        assertEquals(0.42857142857142856, probability, 0.001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(13, 7, 5);
        double upperCumulativeProbability;

        upperCumulativeProbability = dist.upperCumulativeProbability(0);
        assertEquals(1.0, upperCumulativeProbability, 0.001);

        upperCumulativeProbability = dist.upperCumulativeProbability(7);
        assertEquals(0.5, upperCumulativeProbability, 0.001);

        upperCumulativeProbability = dist.upperCumulativeProbability(13);
        assertEquals(1.0, upperCumulativeProbability, 0.001);
    }

    @Test
    public void testNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(5, 2, 3);
        double numericalMean = dist.getNumericalMean();
        assertEquals(2.6, numericalMean, 0.001);
    }

    @Test
    public void testNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        double numericalVariance = dist.getNumericalVariance();
        assertEquals(1.587493938393116, numericalVariance, 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        int supportLowerBound = dist.getSupportLowerBound();
        assertEquals(3, supportLowerBound);
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        int supportUpperBound = dist.getSupportUpperBound();
        assertEquals(5, supportUpperBound);
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        boolean isSupportConnected = dist.isSupportConnected();
        assertTrue(isSupportConnected);
    }
}