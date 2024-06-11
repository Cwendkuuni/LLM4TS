package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HypergeometricDistributionTest {
    private HypergeometricDistribution distribution;

    @Before
    public void setUp() throws Exception {
        distribution = new HypergeometricDistribution(10, 3, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativePopulationSize() throws Exception {
        new HypergeometricDistribution(-10, 3, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroPopulationSize() throws Exception {
        new HypergeometricDistribution(0, 3, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() throws Exception {
        new HypergeometricDistribution(10, -3, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeSampleSize() throws Exception {
        new HypergeometricDistribution(10, 3, -5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() throws Exception {
        new HypergeometricDistribution(10, 12, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeLargerThanPopulationSize() throws Exception {
        new HypergeometricDistribution(10, 3, 12);
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        assertEquals(0.5, distribution.cumulativeProbability(2), 0.01);
        assertEquals(1.0, distribution.cumulativeProbability(3), 0.01);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.01);
        assertEquals(0.0, distribution.cumulativeProbability(10), 0.01);
    }

    @Test
    public void testProbability() throws Exception {
        assertEquals(0.35, distribution.probability(2), 0.01);
        assertEquals(0.0, distribution.probability(4), 0.01);
        assertEquals(0.0, distribution.probability(0), 0.01);
        assertEquals(0.0, distribution.probability(10), 0.01);
    }

    @Test
    public void testUpperCumulativeProbability() throws Exception {
        assertEquals(0.0, distribution.upperCumulativeProbability(2), 0.01);
        assertEquals(0.0, distribution.upperCumulativeProbability(3), 0.01);
        assertEquals(0.5, distribution.upperCumulativeProbability(0), 0.01);
        assertEquals(1.0, distribution.upperCumulativeProbability(10), 0.01);
    }

    @Test
    public void testGetNumericalMean() throws Exception {
        assertEquals(1.5, distribution.getNumericalMean(), 0.01);
    }

    @Test
    public void testGetNumericalVariance() throws Exception {
        assertEquals(0.75, distribution.getNumericalVariance(), 0.01);
    }

    @Test
    public void testGetSupportLowerBound() throws Exception {
        assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() throws Exception {
        assertEquals(3, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() throws Exception {
        assertTrue(distribution.isSupportConnected());
    }
}