
package org.apache.commons.math3.distribution;

import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testNegativePopulationSize() {
        new HypergeometricDistribution(-10, 5, 2);
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(10, -5, 2);
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeSampleSize() {
        new HypergeometricDistribution(10, 5, -2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberOfSuccessesGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 15, 2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSampleSizeGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 5, 20);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(5, distribution.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(10, distribution.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(2, distribution.getSampleSize());
    }

    @Test
    public void testProbabilitySuccessfulOutcome() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.3636363636, distribution.probability(2), 0.00001);
    }

    @Test
    public void testProbabilitySuccessfulOutcomeOutsideDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.0, distribution.probability(10), 0.00001);
    }

    @Test
    public void testProbabilitySuccessfulOutcomeAtLowerDomainBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.4545454545, distribution.probability(3), 0.00001);
    }

    @Test
    public void testProbabilityZeroSuccessfulOutcome() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.4545454545, distribution.probability(0), 0.00001);
    }

    @Test
    public void testCumulativeProbabilityAllPossibleSuccessfulOutcomes() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(1.0, distribution.cumulativeProbability(2), 0.00001);
    }

    @Test
    public void testCumulativeProbabilityNoSuccessfulOutcomes() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.00001);
    }

    @Test
    public void testUpperCumulativeProbabilityNoSuccessfulOutcomes() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(1.0, distribution.upperCumulativeProbability(2), 0.00001);
    }

    @Test
    public void testUpperCumulativeProbabilityAllPossibleSuccessfulOutcomes() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.0, distribution.upperCumulativeProbability(0), 0.00001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(1.0, distribution.getNumericalMean(), 0.00001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(0.18, distribution.getNumericalVariance(), 0.01);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(3, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertEquals(2, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 2);
        assertTrue(distribution.isSupportConnected());
    }
}

