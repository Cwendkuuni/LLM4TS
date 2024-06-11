package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for {@link FDistribution}.
 */
public class FDistributionTest {

    /**
     * Test construction with valid arguments.
     */
    @Test
    public void testConstruction() {
        final double numeratorDegreesOfFreedom = 3;
        final double denominatorDegreesOfFreedom = 5;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals(numeratorDegreesOfFreedom, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test construction with invalid arguments.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructionInvalid() {
        new FDistribution(-1, 2);
    }

    /**
     * Test density function.
     */
    @Test
    public void testDensity() {
        final double numeratorDegreesOfFreedom = 3;
        final double denominatorDegreesOfFreedom = 5;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double x = 0.5;
        double expected = 0.007635262377912575;
        double actual = fd.density(x);
        assertEquals(expected, actual, 1e-8);
    }

    /**
     * Test cumulative probability function.
     */
    @Test
    public void testCumulativeProbability() {
        final double numeratorDegreesOfFreedom = 3;
        final double denominatorDegreesOfFreedom = 5;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double x = 1.5;
        double expected = 0.8687346938757721;
        double actual = fd.cumulativeProbability(x);
        assertEquals(expected, actual, 1e-8);
    }

    /**
     * Test inverse cumulative probability function.
     */
    @Test
    public void testInverseCumulativeProbability() {
        final double numeratorDegreesOfFreedom = 3;
        final double denominatorDegreesOfFreedom = 5;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double p = 0.95;
        double expected = 2.062563736231529;
        double actual = fd.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 1e-8);
    }

    /**
     * Test mean.
     */
    @Test
    public void testMean() {
        final double numeratorDegreesOfFreedom = 3;
        final double denominatorDegreesOfFreedom = 5;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double expected = 3.0 / (numeratorDegreesOfFreedom - 2);
        double actual = fd.getNumericalMean();
        assertEquals(expected, actual, 1e-8);
    }

    /**
     * Test variance.
     */
    @Test
    public void testVariance() {
        final double numeratorDegreesOfFreedom = 4;
        final double denominatorDegreesOfFreedom = 6;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double expected = (2 * denominatorDegreesOfFreedom *
                           (numeratorDegreesOfFreedom + denominatorDegreesOfFreedom - 2)) /
                (numeratorDegreesOfFreedom * (denominatorDegreesOfFreedom - 2) *
                 (denominatorDegreesOfFreedom - 4));
        double actual = fd.getNumericalVariance();
        assertEquals(expected, actual, 1e-8);
    }

    /**
     * Test random generation.
     */
    @Test
    public void testRandomGeneration() {
        final double numeratorDegreesOfFreedom = 3;
        final double denominatorDegreesOfFreedom = 5;
        FDistribution fd = new FDistribution(new RandomGenerator(),
                                            numeratorDegreesOfFreedom,
                                            denominatorDegreesOfFreedom,
                                            1e-9);
        double x;
        for (int i = 0; i < 100; i++) {
            x = fd.inverseCumulativeProbability(fd.randomValue());
            assertTrue(x > 0);
        }
    }
}