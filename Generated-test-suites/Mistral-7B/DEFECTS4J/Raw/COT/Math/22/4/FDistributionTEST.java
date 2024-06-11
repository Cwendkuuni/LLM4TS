package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomGenerator;
import org.apache.commons.math3.util.Well19937c;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link FDistribution}.
 */
public class FDistributionTest {

    private static final double DELTA = 1e-10;
    private static final double EPS = 1e-13;
    private static final double EPS_CUMULATIVE = 1e-6;
    private static final int NUM_Trials = 10000;

    private double numeratorDegreesOfFreedom;
    private double denominatorDegreesOfFreedom;
    private FDistribution fd;

    @Before
    public void setUp() throws NotStrictlyPositiveException {
        numeratorDegreesOfFreedom = 2.5;
        denominatorDegreesOfFreedom = 3.0;
        fd = new FDistribution(new Well19937c(), numeratorDegreesOfFreedom,
                denominatorDegreesOfFreedom, DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
    }

    @Test
    public void testFDistribution_constructor() throws NotStrictlyPositiveException {
        FDistribution fd1 = new FDistribution(numeratorDegreesOfFreedom,
                denominatorDegreesOfFreedom);
        Assert.assertEquals(numeratorDegreesOfFreedom, fd1.getNumeratorDegreesOfFreedom(), EPS);
        Assert.assertEquals(denominatorDegreesOfFreedom, fd1.getDenominatorDegreesOfFreedom(), EPS);

        FDistribution fd2 = new FDistribution(numeratorDegreesOfFreedom,
                denominatorDegreesOfFreedom, EPS_CUMULATIVE);
        Assert.assertEquals(numeratorDegreesOfFreedom, fd2.getNumeratorDegreesOfFreedom(), EPS);
        Assert.assertEquals(denominatorDegreesOfFreedom, fd2.getDenominatorDegreesOfFreedom(), EPS);
    }

    @Test
    public void testFDistribution_density() {
        double max = 5.0;
        for (double x = 0; x <= max; x += 0.01) {
            double density = fd.density(x);
            double expectedDensity = densityFromFormula(x, numeratorDegreesOfFreedom,
                    denominatorDegreesOfFreedom);
            Assert.assertEquals(expectedDensity, density, EPS);
        }
    }

    @Test
    public void testFDistribution_cumulativeProbability() {
        double[] xValues = {0.0, 1.0, 2.0, 2.5, 3.0, 5.0, 10.0};
        double[] expectedCumulativeProbabilities = {0.0, 0.3989423, 0.5221471, 0.7517344,
                0.8862269, 1.0, 1.0};
        for (int i = 0; i < xValues.length; i++) {
            double cumulativeProbability = fd.cumulativeProbability(xValues[i]);
            Assert.assertEquals(expectedCumulativeProbabilities[i], cumulativeProbability, EPS_CUMULATIVE);
        }
    }

    @Test
    public void testFDistribution_supportBounds() {
        Assert.assertEquals(0.0, fd.getSupportLowerBound(), DELTA);
        Assert.assertTrue(Double.isInfinite(fd.getSupportUpperBound()));
    }

    @Test
    public void testFDistribution_isSupportLowerBoundInclusive() {
        Assert.assertTrue(fd.isSupportLowerBoundInclusive());
    }

    @Test
    public void testFDistribution_isSupportUpperBoundInclusive() {
        Assert.assertFalse(fd.isSupportUpperBoundInclusive());
    }

    @Test
    public void testFDistribution_isSupportConnected() {
        Assert.assertTrue(fd.isSupportConnected());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistribution_constructorNegativeNumeratorDOF()
            throws NotStrictlyPositiveException {
        new FDistribution(-1.0, numeratorDegreesOfFreedom);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistribution_constructorNegativeDenominatorDOF()
            throws NotStrictlyPositiveException {
        new FDistribution(numeratorDegreesOfFreedom, -1.0);
    }

    @Test
    public void testFDistribution_numericalVariance() {
        Assert.assertFalse(fd.isNumericalVarianceCalculated());
        double variance = fd.getNumericalVariance();
        Assert.assertTrue(fd.isNumericalVarianceCalculated());
        Assert.assertEquals(expectedVariance(numeratorDegreesOfFreedom,
                denominatorDegreesOfFreedom), variance, EPS);
    }

    @Test
    public void testFDistribution_randomSample() {
        RandomGenerator rg = new Well19937c();
        FDistribution fd = new FDistribution(rg, numeratorDegreesOfFreedom,
                denominatorDegreesOfFreedom, DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
        double[] samples = new double[NUM_Trials];
        for (int i = 0; i < NUM_Trials; i++) {
            samples[i] = fd.inverseCumulativeProbability(rg.nextUniform());
        }
        double mean = getNumericalMean(samples);
        double variance = getNumericalVariance(samples);
        Assert.assertEquals(getNumericalMean(), mean, EPS);
        Assert.assertEquals(getNumericalVariance(), variance, EPS);
    }

    private double densityFromFormula(double x, double numeratorDegreesOfFreedom,
            double denominatorDegreesOfFreedom) {
        final double nhalf = numeratorDegreesOfFreedom / 2;
        final double mhalf = denominatorDegreesOfFreedom / 2;
        final double logx = FastMath.log(x);
        final double logn = FastMath.log(numeratorDegreesOfFreedom);
        final double logm = FastMath.log(denominatorDegreesOfFreedom);
        final double lognxm = FastMath.log(numeratorDegreesOfFreedom * x +
                denominatorDegreesOfFreedom);
        return FastMath.exp(nhalf * logn + nhalf * logx - logx +
                mhalf * logm - nhalf * lognxm - mhalf * lognxm -
                Beta.logBeta(nhalf, mhalf));
    }

    private double expectedVariance(double numeratorDegreesOfFreedom,
            double denominatorDegreesOfFreedom) {
        if (denominatorDegreesOfFreedom > 4) {
            return (2 * (denominatorDegreesOfFreedom * denominatorDegreesOfFreedom) *
                    (numeratorDegreesOfFreedom + denominatorDegreesOfFreedom - 2)) /
                   ((numeratorDegreesOfFreedom * (denominatorDegreesOfFreedom - 2) *
                           (denominatorDegreesOfFreedom - 4)));
        }

        return Double.NaN;
    }

    private double getNumericalMean(double[] samples) {
        double sum = 0.0;
        for (double sample : samples) {
            sum += sample;
        }
        return sum / samples.length;
    }

    private double getNumericalVariance(double[] samples) {
        double mean = getNumericalMean(samples);
        double sum = 0.0;
        for (double sample : samples) {
            sum += Precision.pow(sample - mean, 2);
        }
        return sum / (samples.length - 1);
    }
}