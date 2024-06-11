
package org.apache.commons.math3.distribution;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    private static final double EPSILON = 1e-9;

    private HypergeometricDistribution distribution;

    @Before
    public void setup() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        distribution = new HypergeometricDistribution(100, 50, 10);
    }

    @Test
    public void testConstructor() {
        assertEquals(100, distribution.getPopulationSize());
        assertEquals(50, distribution.getNumberOfSuccesses());
        assertEquals(10, distribution.getSampleSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(100, -50, 10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroNumberOfSuccesses() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(100, 0, 10);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeSampleSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(100, 50, -10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroSampleSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(100, 50, 0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesGreaterThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(100, 110, 10);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeGreaterThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(100, 50, 110);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.0, distribution.cumulativeProbability(-1), EPSILON);
        assertEquals(0.0, distribution.cumulativeProbability(0), EPSILON);
        assertEquals(0.5085862090924918, distribution.cumulativeProbability(1), EPSILON);
        assertEquals(1.0, distribution.cumulativeProbability(10), EPSILON);
        assertEquals(1.0, distribution.cumulativeProbability(11), EPSILON);
    }

    @Test
    public void testProbability() {
        assertEquals(0.0, distribution.probability(-1), EPSILON);
        assertEquals(0.4003685995133792, distribution.probability(1), EPSILON);
        assertEquals(0.0012628002219918706, distribution.probability(10), EPSILON);
        assertEquals(0.0, distribution.probability(11), EPSILON);
    }

    @Test
    public void testUpperCumulativeProbability() {
        assertEquals(1.0, distribution.upperCumulativeProbability(-1), EPSILON);
        assertEquals(1.0, distribution.upperCumulativeProbability(0), EPSILON);
        assertEquals(0.4914137909075082, distribution.upperCumulativeProbability(1), EPSILON);
        assertEquals(0.0, distribution.upperCumulativeProbability(10), EPSILON);
        assertEquals(0.0, distribution.upperCumulativeProbability(11), EPSILON);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(5.0, distribution.getNumericalMean(), EPSILON);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(1.263157894736842, distribution.getNumericalVariance(), EPSILON);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(40, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(10, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }
}
