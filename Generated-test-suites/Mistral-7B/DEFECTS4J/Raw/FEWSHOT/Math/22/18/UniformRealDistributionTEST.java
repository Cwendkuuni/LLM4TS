package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionDefaultConstructor() {
        UniformRealDistribution urd = new UniformRealDistribution();
        assertEquals(0, urd.getNumericalMean(), 0.0001);
        assertEquals(0, urd.getNumericalVariance(), 0.0001);
        assertEquals(0, urd.getSupportLowerBound(), 0.0001);
        assertEquals(1, urd.getSupportUpperBound(), 0.0001);
        assertTrue(urd.isSupportLowerBoundInclusive());
        assertTrue(urd.isSupportUpperBoundExclusive());
        assertTrue(urd.isSupportConnected());
    }

    @Test
    public void testUniformRealDistributionConstructor() throws NumberIsTooLargeException {
        UniformRealDistribution urd = new UniformRealDistribution(1, 5);
        assertEquals(3, urd.getNumericalMean(), 0.0001);
        assertEquals(8.333333333333333, urd.getNumericalVariance(), 0.0001);
        assertEquals(1, urd.getSupportLowerBound(), 0.0001);
        assertEquals(5, urd.getSupportUpperBound(), 0.0001);
        assertTrue(urd.isSupportLowerBoundInclusive());
        assertFalse(urd.isSupportUpperBoundInclusive());
        assertTrue(urd.isSupportConnected());
    }

    @Test
    public void testUniformRealDistributionConstructorWithInverseCumulativeProbabilityAccuracy()
            throws NumberIsTooLargeException {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1, 0.0001);
        assertEquals(0.5, urd.getNumericalMean(), 0.0001);
        assertEquals(0.00025, urd.getNumericalVariance(), 0.0001);
        assertEquals(0, urd.getSupportLowerBound(), 0.0001);
        assertEquals(1, urd.getSupportUpperBound(), 0.0001);
        assertTrue(urd.isSupportLowerBoundInclusive());
        assertFalse(urd.isSupportUpperBoundInclusive());
        assertTrue(urd.isSupportConnected());
    }

    @Test
    public void testUniformRealDistributionConstructorWithRandomGenerator()
            throws NumberIsTooLargeException {
        RandomGenerator rg = new Well19937c();
        UniformRealDistribution urd = new UniformRealDistribution(rg, 0, 1, 0.0001);
        assertEquals(0.5, urd.getNumericalMean(), 0.0001);
        assertEquals(0.00025, urd.getNumericalVariance(), 0.0001);
        assertEquals(0, urd.getSupportLowerBound(), 0.0001);
        assertEquals(1, urd.getSupportUpperBound(), 0.0001);
        assertTrue(urd.isSupportLowerBoundInclusive());
        assertFalse(urd.isSupportUpperBoundInclusive());
        assertTrue(urd.isSupportConnected());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionConstructorWithInvalidBounds()
            throws NumberIsTooLargeException {
        new UniformRealDistribution(new Well19937c(), 5, 4);
    }

    @Test
    public void testDensity() {
        UniformRealDistribution urd = new UniformRealDistribution();
        double[] xValues = { 0, 0.25, 0.5, 0.75, 1 };
        double[] expectedDensities = { 1 / (1 - 0), 1 / (1 - 0.25), 1 / (1 - 0.5), 1 / (1 - 0.75), 1 / (1 - 1) };
        for (int i = 0; i < xValues.length; i++) {
            assertEquals(expectedDensities[i], urd.density(xValues[i]), 0.0001);
        }
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution urd = new UniformRealDistribution();
        double[] xValues = { 0, 0.25, 0.5, 0.75, 1 };
        double[] expectedProbabilities = { 0, 0.25, 0.5, 0.75, 1 };
        for (int i = 0; i < xValues.length; i++) {
            assertEquals(expectedProbabilities[i], urd.cumulativeProbability(xValues[i]), 0.0001);
        }
    }

    @Test
    public void testSample() {
        UniformRealDistribution urd = new UniformRealDistribution();
        for (int i = 0; i < 1000; i++) {
            double sample = urd.sample();
            assertTrue(sample >= urd.getSupportLowerBound());
            assertTrue(sample < urd.getSupportUpperBound());
        }
    }
}