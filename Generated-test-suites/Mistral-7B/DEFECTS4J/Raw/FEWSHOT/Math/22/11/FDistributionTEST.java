package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testFDistribution() throws Exception {
        double numeratorDegreesOfFreedom = 2.5;
        double denominatorDegreesOfFreedom = 4.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals(numeratorDegreesOfFreedom, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeDegreesOfFreedom() throws Exception {
        new FDistribution(-1.0, 2.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionZeroDegreesOfFreedom() throws Exception {
        new FDistribution(0.0, 2.0);
    }

    @Test
    public void testDensity() {
        double numeratorDegreesOfFreedom = 2.5;
        double denominatorDegreesOfFreedom = 4.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double x = 1.5;
        double density = fd.density(x);
        double expectedDensity = FastMath.exp(
                numeratorDegreesOfFreedom * FastMath.log(x) -
                FastMath.log(x) +
                denominatorDegreesOfFreedom * FastMath.log(denominatorDegreesOfFreedom) -
                numeratorDegreesOfFreedom * FastMath.log(numeratorDegreesOfFreedom * x +
                        denominatorDegreesOfFreedom) -
                denominatorDegreesOfFreedom * FastMath.log(numeratorDegreesOfFreedom * x +
                        denominatorDegreesOfFreedom) -
                Beta.logBeta(numeratorDegreesOfFreedom / 2, denominatorDegreesOfFreedom / 2));
        assertEquals(expectedDensity, density, 1e-9);
    }

    @Test
    public void testCumulativeProbability() {
        double numeratorDegreesOfFreedom = 2.5;
        double denominatorDegreesOfFreedom = 4.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double x = 1.5;
        double cdf = fd.cumulativeProbability(x);
        double expectedCDF = Beta.regularizedBeta(
                (numeratorDegreesOfFreedom * x) / (denominatorDegreesOfFreedom + numeratorDegreesOfFreedom * x),
                0.5 * numeratorDegreesOfFreedom, 0.5 * denominatorDegreesOfFreedom);
        assertEquals(expectedCDF, cdf, 1e-9);
    }

    @Test
    public void testGetNumericalMean() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double mean = fd.getNumericalMean();
        assertEquals(denominatorDegreesOfFreedom / (denominatorDegreesOfFreedom - 2), mean, 1e-9);
    }

    @Test
    public void testGetNumericalVariance() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double variance = fd.getNumericalVariance();
        if (denominatorDegreesOfFreedom > 4) {
            double expectedVariance = (2 * (denominatorDegreesOfFreedom * denominatorDegreesOfFreedom) *
                    (numeratorDegreesOfFreedom + denominatorDegreesOfFreedom - 2)) /
                    ((numeratorDegreesOfFreedom * (denominatorDegreesOfFreedom - 2) *
                            (denominatorDegreesOfFreedom - 4)));
            assertEquals(expectedVariance, variance, 1e-9);
        } else {
            assertTrue(Double.isNaN(variance));
        }
    }

    @Test
    public void testGetSupportBounds() {
        double numeratorDegreesOfFreedom = 2.5;
        double denominatorDegreesOfFreedom = 4.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertTrue(fd.getSupportLowerBound() == 0.0);
        assertTrue(fd.getSupportUpperBound() == Double.POSITIVE_INFINITY);
        assertTrue(fd.isSupportLowerBoundInclusive());
        assertTrue(fd.isSupportUpperBoundExclusive());
    }

    @Test
    public void testGenerateRandomValues() {
        double numeratorDegreesOfFreedom = 2.5;
        double denominatorDegreesOfFreedom = 4.0;
        RandomGenerator rng = new Well19937c();
        FDistribution fd = new FDistribution(rng, numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double[] values = new double[1000];
        for (int i = 0; i < values.length; i++) {
            values[i] = fd.inverseCumulativeProbability(rng.nextDouble());
        }
        assertArrayEquals(values, fd.sample(values.length), 0.01);
    }
}