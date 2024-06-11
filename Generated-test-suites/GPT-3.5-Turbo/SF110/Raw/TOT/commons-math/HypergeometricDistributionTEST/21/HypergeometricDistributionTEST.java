package org.apache.commons.math3.distribution;

import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructorWithPositiveValues() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertNotNull(dist);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativePopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(-10, 5, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, -5, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, -3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesGreaterThanPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 15, 3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeGreaterThanPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 15);
    }

    @Test
    public void testCumulativeProbabilityWithValidValue() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        double result = dist.cumulativeProbability(2);
        assertEquals(0.19047619047619047, result, 0);
    }

    @Test
    public void testCumulativeProbabilityWithLowerBoundValue() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        double result = dist.cumulativeProbability(0);
        assertEquals(0.0, result, 0);
    }

    @Test
    public void testCumulativeProbabilityWithUpperBoundValue() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        double result = dist.cumulativeProbability(3);
        assertEquals(1.0, result, 0);
    }

    @Test
    public void testProbabilityWithValidValue() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        double result = dist.probability(2);
        assertEquals(0.2647058823529412, result, 0);
    }

    @Test
    public void testProbabilityWithInvalidValue() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        double result = dist.probability(7);
        assertEquals(0.0, result, 0);
    }

    @Test
    public void testUpperCumulativeProbabilityWithValidValue() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        double result = dist.upperCumulativeProbability(2);
        assertEquals(0.8095238095238095, result, 0);
    }

    @Test
    public void testUpperCumulativeProbabilityWithLowerBoundValue() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        double result = dist.upperCumulativeProbability(0);
        assertEquals(1.0, result, 0);
    }

    @Test
    public void testUpperCumulativeProbabilityWithUpperBoundValue() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        double result = dist.upperCumulativeProbability(3);
        assertEquals(0.0, result, 0);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        double result = dist.getNumericalMean();
        assertEquals(1.5, result, 0);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        double result = dist.getNumericalVariance();
        assertEquals(0.7338775510204081, result, 0);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        int result = dist.getSupportLowerBound();
        assertEquals(2, result);
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        int result = dist.getSupportUpperBound();
        assertEquals(3, result);
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        boolean result = dist.isSupportConnected();
        assertTrue(result);
    }

}