
package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    private HypergeometricDistribution distribution;

    @Before
    public void setUp() throws Exception {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test
    public void testCumulativeProbability() {
        double cumulativeProbability = distribution.cumulativeProbability(2);
        assertEquals(0.42857142857142855, cumulativeProbability, 0.0001);
    }

    @Test
    public void testGetLowerDomain() {
        int lowerDomain = distribution.getLowerDomain(10, 5, 3);
        assertEquals(2, lowerDomain);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        int numberOfSuccesses = distribution.getNumberOfSuccesses();
        assertEquals(5, numberOfSuccesses);
    }

    @Test
    public void testGetPopulationSize() {
        int populationSize = distribution.getPopulationSize();
        assertEquals(10, populationSize);
    }

    @Test
    public void testGetSampleSize() {
        int sampleSize = distribution.getSampleSize();
        assertEquals(3, sampleSize);
    }

    @Test
    public void testGetUpperDomain() {
        int upperDomain = distribution.getUpperDomain(5, 3);
        assertEquals(3, upperDomain);
    }

    @Test
    public void testProbability() {
        double probability = distribution.probability(2);
        assertEquals(0.5714285714285714, probability, 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        double upperCumulativeProbability = distribution.upperCumulativeProbability(2);
        assertEquals(0.5714285714285714, upperCumulativeProbability, 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        double numericalMean = distribution.getNumericalMean();
        assertEquals(1.5, numericalMean, 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        double numericalVariance = distribution.getNumericalVariance();
        assertEquals(0.375, numericalVariance, 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        int supportLowerBound = distribution.getSupportLowerBound();
        assertEquals(2, supportLowerBound);
    }

    @Test
    public void testGetSupportUpperBound() {
        int supportUpperBound = distribution.getSupportUpperBound();
        assertEquals(3, supportUpperBound);
    }

    @Test
    public void testIsSupportConnected() {
        boolean supportConnected = distribution.isSupportConnected();
        assertTrue(supportConnected);
    }
}
