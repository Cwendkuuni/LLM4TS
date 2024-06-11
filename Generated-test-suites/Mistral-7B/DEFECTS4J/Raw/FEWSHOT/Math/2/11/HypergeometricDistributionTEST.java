package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

public class HypergeometricDistributionTest {
    @Test
    public void testConstructors() {
        try {
            new HypergeometricDistribution(-1, 2, 3);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(0, 2, 3);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(1, -3, 3);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(1, 3, -3);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(1, 3, 0);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(1, 3, 4);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(1, 5, 4);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        HypergeometricDistribution hd = new HypergeometricDistribution(5, 3, 3);
        assertEquals(3, hd.getNumberOfSuccesses());
        assertEquals(5, hd.getPopulationSize());
        assertEquals(3, hd.getSampleSize());
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution hd = new HypergeometricDistribution(5, 3, 3);

        int[] domain = hd.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double cumProb = hd.cumulativeProbability(x);
            double prob = hd.probability(x);
            assertEquals(cumProb, 1.0 - Math.pow(1.0 - prob, x + 1), 0.00001);
        }
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution hd = new HypergeometricDistribution(5, 3, 3);
        for (int x = 0; x <= 5; x++) {
            double prob = hd.probability(x);
            assertEquals(hd.cumulativeProbability(x + 1) - hd.cumulativeProbability(x), prob, 0.00001);
        }
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution hd = new HypergeometricDistribution(5, 3, 3);

        int[] domain = hd.getDomain();
        for (int x = 0; x <= domain[1]; x++) {
            double upperProb = hd.upperCumulativeProbability(x);
            double cumProb = hd.cumulativeProbability(domain[1]);
            assertEquals(1.0 - cumProb, upperProb, 0.00001);
        }
    }

    @Test
    public void testGetDomain() {
        HypergeometricDistribution hd = new HypergeometricDistribution(5, 3, 3);
        int[] domain = hd.getDomain();
        assertEquals(3, domain[0]);
        assertEquals(4, domain[1]);
    }

    @Test
    public void testGetLowerDomain() {
        HypergeometricDistribution hd = new HypergeometricDistribution(5, 3, 3);
        int lowerDomain = hd.getLowerDomain();
        assertEquals(3, lowerDomain);
    }

    @Test
    public void testGetUpperDomain() {
        HypergeometricDistribution hd = new HypergeometricDistribution(5, 3, 3);
        int upperDomain = hd.getUpperDomain();
        assertEquals(4, upperDomain);
    }

    @Test
    public void testNumericalMean() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);
        double numericalMean = hd.getNumericalMean();
        double populationMean = (double) hd.getNumberOfSuccesses() / (double) hd.getPopulationSize();
        assertEquals(populationMean, numericalMean, 0.00001);
    }

    @Test
    public void testNumericalVariance() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);
        double numericalVariance = hd.getNumericalVariance();
        double populationVariance = (double) (hd.getNumberOfSuccesses() * (hd.getPopulationSize() - hd.getNumberOfSuccesses()) / (double) (hd.getPopulationSize() * (hd.getPopulationSize() - 1)));
        assertEquals(populationVariance, numericalVariance, 0.00001);
    }

    @Test
    public void testSupportLowerBound() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);
        int supportLowerBound = hd.getSupportLowerBound();
        assertEquals(5, supportLowerBound);
    }

    @Test
    public void testSupportUpperBound() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);
        int supportUpperBound = hd.getSupportUpperBound();
        assertEquals(5, supportUpperBound);
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);
        assertTrue(hd.isSupportConnected());
    }
}