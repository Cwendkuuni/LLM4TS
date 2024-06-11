package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link MultivariateNormalDistribution}.
 */
public class MultivariateNormalDistributionTest {

    private static final double EPSILON = 1e-12;
    private MultivariateNormalDistribution dist;
    private RandomGenerator rng;

    @Before
    public void setUp() {
        rng = new Well19937c();
        dist = new MultivariateNormalDistribution(rng, new double[]{-1, 2},
                new double[][]{{1, 1}, {1, 1}});
    }

    /**
     * Tests the distribution mean.
     */
    @Test
    public void testMean() {
        final double[] means = dist.getMeans();
        final double[] sample = new double[means.length];
        for (int i = 0; i < sample.length; i++) {
            sample[i] = dist.sample()[i];
        }
        Assert.assertArrayEquals(means, sample, EPSILON);
    }

    /**
     * Tests the distribution covariance matrix.
     */
    @Test
    public void testCovarianceMatrix() {
        final RealMatrix covariances = dist.getCovariances();
        final double[][] sample = new double[100][];
        for (int i = 0; i < sample.length; i++) {
            sample[i] = dist.sample();
        }
        final RealMatrix covarianceSample = new Array2DRowRealMatrix(sample).covariance();
        Assert.assertEquals(covariances, covarianceSample);
    }

    /**
     * Tests the density function with a valid input.
     */
    @Test
    public void testDensityValidInput() {
        final double[] values = new double[]{-1, 1};
        final double density = dist.density(values);
        final double expected = 0.11274048; // calculated with MathWorld's calculator
        Assert.assertEquals(expected, density, EPSILON);
    }

    /**
     * Tests the density function with an invalid input (wrong dimensions).
     */
    @Test(expected = DimensionMismatchException.class)
    public void testDensityInvalidInput() {
        dist.density(new double[]{1, 1, 2});
    }

    /**
     * Tests the sample function with a valid input.
     */
    @Test
    public void testSampleValidInput() {
        final double[] sample = dist.sample();
        Assert.assertArrayEquals(dist.getDimension(), sample.length);
        for (int i = 0; i < sample.length; i++) {
            Assert.assertTrue("Sample value is out of range",
                    Precision.isClose(dist.getMeans()[i], sample[i], EPSILON));
        }
    }

    /**
     * Tests the sample function with an invalid input (wrong dimensions).
     */
    @Test(expected = DimensionMismatchException.class)
    public void testSampleInvalidInput() {
        dist.sample(2);
    }

    /**
     * Tests the getExponentTerm function with a valid input.
     */
    @Test
    public void testGetExponentTermValidInput() {
        final double[] values = new double[]{-1, 1};
        final double exponentTerm = dist.getExponentTerm(values);
        final double expected = 0.76159415; // calculated with MathWorld's calculator
        Assert.assertEquals(expected, exponentTerm, EPSILON);
    }

    /**
     * Tests the getExponentTerm function with an invalid input (wrong dimensions).
     */
    @Test(expected = DimensionMismatchException.class)
    public void testGetExponentTermInvalidInput() {
        dist.getExponentTerm(new double[]{1, 1, 2});
    }
}