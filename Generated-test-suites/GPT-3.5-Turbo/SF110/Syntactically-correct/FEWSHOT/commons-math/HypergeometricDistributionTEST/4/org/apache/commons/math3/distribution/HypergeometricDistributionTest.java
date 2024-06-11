package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

public class HypergeometricDistributionTest {
    private HypergeometricDistribution distribution;
    
    @Before
    public void setUp() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        distribution = new HypergeometricDistribution(10, 5, 2);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(10, distribution.getPopulationSize());
        assertEquals(5, distribution.getNumberOfSuccesses());
        assertEquals(2, distribution.getSampleSize());
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(0, 5, 2);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidNumberOfSuccesses() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(10, -1, 2);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidSampleSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(10, 5, -2);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesGreaterThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(10, 15, 2);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeGreaterThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        new HypergeometricDistribution(10, 5, 20);
    }
    
    @Test
    public void testCumulativeProbability() {
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.76923, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(3), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(4), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(5), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(6), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(7), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(8), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(9), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(10), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(11), 0.0001);
    }
    
    @Test
    public void testUpperCumulativeProbability() {
        assertEquals(1.0, distribution.upperCumulativeProbability(-1), 0.0001);
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0001);
        assertEquals(1.0, distribution.upperCumulativeProbability(1), 0.0001);
        assertEquals(1.0, distribution.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.23077, distribution.upperCumulativeProbability(3), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(4), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(5), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(6), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(7), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(8), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(9), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(10), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(11), 0.0001);
    }
    
    @Test
    public void testProbability() {
        assertEquals(0.0, distribution.probability(-1), 0.0001);
        assertEquals(0.0, distribution.probability(0), 0.0001);
        assertEquals(0.38462, distribution.probability(1), 0.0001);
        assertEquals(0.61538, distribution.probability(2), 0.0001);
        assertEquals(0.0, distribution.probability(3), 0.0001);
        assertEquals(0.0, distribution.probability(4), 0.0001);
        assertEquals(0.0, distribution.probability(5), 0.0001);
        assertEquals(0.0, distribution.probability(6), 0.0001);
        assertEquals(0.0, distribution.probability(7), 0.0001);
        assertEquals(0.0, distribution.probability(8), 0.0001);
        assertEquals(0.0, distribution.probability(9), 0.0001);
        assertEquals(0.0, distribution.probability(10), 0.0001);
        assertEquals(0.0, distribution.probability(11), 0.0001);
    }
    
    @Test
    public void testGetNumericalMean() {
        assertEquals(1.0, distribution.getNumericalMean(), 0.0001);
    }
    
    @Test
    public void testGetNumericalVariance() {
        assertEquals(0.09489, distribution.getNumericalVariance(), 0.0001);
    }
    
    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, distribution.getSupportLowerBound());
    }
    
    @Test
    public void testGetSupportUpperBound() {
        assertEquals(2, distribution.getSupportUpperBound());
    }
    
    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }
}