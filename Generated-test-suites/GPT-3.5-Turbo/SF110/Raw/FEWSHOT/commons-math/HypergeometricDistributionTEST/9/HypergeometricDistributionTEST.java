package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertNotNull(distribution);
    }

    @Test(expected = org.apache.commons.math3.exception.NotPositiveException.class)
    public void testConstructor_InvalidPopulationSize() {
        new HypergeometricDistribution(0, 5, 3);
    }

    @Test(expected = org.apache.commons.math3.exception.NotPositiveException.class)
    public void testConstructor_InvalidNumberOfSuccesses() {
        new HypergeometricDistribution(10, -1, 3);
    }

    @Test(expected = org.apache.commons.math3.exception.NotPositiveException.class)
    public void testConstructor_InvalidSampleSize() {
        new HypergeometricDistribution(10, 5, -1);
    }

    @Test(expected = org.apache.commons.math3.exception.NumberIsTooLargeException.class)
    public void testConstructor_NumberOfSuccessesGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 15, 3);
    }

    @Test(expected = org.apache.commons.math3.exception.NumberIsTooLargeException.class)
    public void testConstructor_SampleSizeGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 5, 12);
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.0909, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.3636, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(0.8182, distribution.cumulativeProbability(3), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(4), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(5), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(6), 0.0001);
    }

    @Test
    public void testGetDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        int[] domain = distribution.getDomain(10, 5, 3);
        assertNotNull(domain);
        assertEquals(0, domain[0]);
        assertEquals(3, domain[1]);
    }

    @Test
    public void testGetLowerDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        int lowerDomain = distribution.getLowerDomain(10, 5, 3);
        assertEquals(2, lowerDomain);

        lowerDomain = distribution.getLowerDomain(5, 5, 3);
        assertEquals(0, lowerDomain);

        lowerDomain = distribution.getLowerDomain(10, 0, 3);
        assertEquals(0, lowerDomain);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(5, distribution.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, distribution.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, distribution.getSampleSize());
    }

    @Test
    public void testGetUpperDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        int upperDomain = distribution.getUpperDomain(5, 3);
        assertEquals(3, upperDomain);

        upperDomain = distribution.getUpperDomain(10, 5);
        assertEquals(5, upperDomain);

        upperDomain = distribution.getUpperDomain(10, 0);
        assertEquals(0, upperDomain);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, distribution.probability(-1), 0.0001);
        assertEquals(0.0, distribution.probability(0), 0.0001);
        assertEquals(0.0909, distribution.probability(1), 0.0001);
        assertEquals(0.2727, distribution.probability(2), 0.0001);
        assertEquals(0.4545, distribution.probability(3), 0.0001);
        assertEquals(0.2727, distribution.probability(4), 0.0001);
        assertEquals(0.0, distribution.probability(5), 0.0001);
        assertEquals(0.0, distribution.probability(6), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.0, distribution.upperCumulativeProbability(7), 0.0001);
        assertEquals(1.0, distribution.upperCumulativeProbability(6), 0.0001);
        assertEquals(1.0, distribution.upperCumulativeProbability(5), 0.0001);
        assertEquals(0.8182, distribution.upperCumulativeProbability(4), 0.0001);
        assertEquals(0.4545, distribution.upperCumulativeProbability(3), 0.0001);
        assertEquals(0.1818, distribution.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.0909, distribution.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(0), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(-1), 0.0001);
    }

    @Test
    public void testInnerCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double innerCumulativeProbability = distribution.innerCumulativeProbability(1, 4, 1);
        assertEquals(0.7273, innerCumulativeProbability, 0.0001);

        innerCumulativeProbability = distribution.innerCumulativeProbability(4, 1, -1);
        assertEquals(0.7273, innerCumulativeProbability, 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double numericalVariance = distribution.getNumericalVariance();
        assertTrue(Double.isNaN(numericalVariance));

        distribution.cumulativeProbability(2);
        numericalVariance = distribution.getNumericalVariance();
        assertEquals(1.2, numericalVariance, 0.0001);
    }

    @Test
    public void testCalculateNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        double numericalVariance = distribution.calculateNumericalVariance();
        assertEquals(1.2, numericalVariance, 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1, distribution.getSupportLowerBound());

        distribution = new HypergeometricDistribution(10, 10, 5);
        assertEquals(5, distribution.getSupportLowerBound());

        distribution = new HypergeometricDistribution(5, 0, 3);
        assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, distribution.getSupportUpperBound());

        distribution = new HypergeometricDistribution(10, 10, 5);
        assertEquals(5, distribution.getSupportUpperBound());

        distribution = new HypergeometricDistribution(5, 0, 3);
        assertEquals(0, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);
        assertTrue(distribution.isSupportConnected());
    }
}