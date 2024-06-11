package org.apache.commons.math3.distribution;

import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    private HypergeometricDistribution distribution;

    @Before
    public void setUp() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        distribution = new HypergeometricDistribution(10, 5, 3);
    }

    @Test
    public void testConstructorWithValidArguments() {
        assertEquals(10, distribution.getPopulationSize());
        assertEquals(5, distribution.getNumberOfSuccesses());
        assertEquals(3, distribution.getSampleSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        distribution = new HypergeometricDistribution(-10, 5, 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidNumberOfSuccesses() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        distribution = new HypergeometricDistribution(10, -5, 3);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidSampleSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        distribution = new HypergeometricDistribution(10, 5, -3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        distribution = new HypergeometricDistribution(10, 15, 3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeLargerThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        distribution = new HypergeometricDistribution(10, 5, 15);
    }

    @Test
    public void testCumulativeProbability() {
        // Edge cases
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(10), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(11), 0.0001);

        // Typical use cases
        assertEquals(0.34375, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.59375, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(0.875, distribution.cumulativeProbability(3), 0.0001);
    }

    @Test
    public void testProbability() {
        // Edge cases
        assertEquals(0.0, distribution.probability(-1), 0.0001);
        assertEquals(0.0, distribution.probability(0), 0.0001);
        assertEquals(0.0, distribution.probability(11), 0.0001);

        // Typical use case
        assertEquals(0.359375, distribution.probability(2), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        // Edge cases
        assertEquals(0.0, distribution.upperCumulativeProbability(-1), 0.0001);
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0001);
        assertEquals(1.0, distribution.upperCumulativeProbability(10), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(11), 0.0001);

        // Typical use cases
        assertEquals(0.0, distribution.upperCumulativeProbability(3), 0.0001);
        assertEquals(0.12499999999999989, distribution.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.40624999999999994, distribution.upperCumulativeProbability(1), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(1.5, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(0.975, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(3, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(3, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }

}