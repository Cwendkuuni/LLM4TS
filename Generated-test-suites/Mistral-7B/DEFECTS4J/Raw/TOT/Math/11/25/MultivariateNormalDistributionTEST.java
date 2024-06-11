package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the {@link MultivariateNormalDistribution} class.
 */
public class MultivariateNormalDistributionTest {

    /**
     * Test the MultivariateNormalDistribution constructor with valid means and
     * covariance matrix.
     * @throws DimensionMismatchException when means and covariances arrays have
     *                                  different length.
     * @throws SingularMatrixException when covariance matrix is singular.
     * @throws NonPositiveDefiniteMatrixException when any eigenvalue is negative.
     */
    @Test
    public void testValidInstanciation() throws DimensionMismatchException,
            SingularMatrixException, NonPositiveDefiniteMatrixException {
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        Assert.assertArrayEquals(means, dist.getMeans(), 0.001);
        RealMatrix covarianceMatrix = dist.getCovariances();
        Assert.assertEquals(covariances[0].length, covarianceMatrix.getColumnDimension());
        Assert.assertEquals(covariances.length, covarianceMatrix.getRowDimension());
        Assert.assertEquals(covariances.length, covarianceMatrix.getDimension());
        Assert.assertArrayEquals(covariances, covarianceMatrix.getData(), 0.001);
    }

    /**
     * Test the MultivariateNormalDistribution constructor with invalid means and
     * covariance matrix dimensions.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testInvalidInstanciation() {
        double[] means = new double[]{0.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}, {0.5, 0.5}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    /**
     * Test the getStandardDeviations() method.
     */
    @Test
    public void testGetStandardDeviations() {
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        double[] stdDeviations = dist.getStandardDeviations();
        Assert.assertArrayEquals(new double[]{1.0, 0.5}, stdDeviations, 0.001);
    }

    /**
     * Test the sample() method with default random number generator.
     */
    @Test
    public void testSampleDefaultGenerator() {
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] sample = dist.sample();
        Assert.assertArrayEquals(means, sample, 0.001);
        for (int i = 0; i < sample.length; i++) {
            Assert.assertTrue(Precision.isClose(0.0, sample[i] - means[i], 0.1));
            Assert.assertTrue(Precision.isClose(0.0, means[i], 0.1));
        }
    }

    /**
     * Test the sample() method with custom random number generator.
     */
    @Test
    public void testSampleCustomGenerator() {
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        RandomGenerator rng = new Well19937c();
        double[] sample = dist.sample(rng);
        Assert.assertArrayEquals(means, sample, 0.001);
        for (int i = 0; i < sample.length; i++) {
            Assert.assertTrue(Precision.isClose(0.0, sample[i] - means[i], 0.1));
            Assert.assertTrue(Precision.isClose(0.0, means[i], 0.1));
        }
    }

    /**
     * Test the density() method with valid values.
     */
    @Test
    public void testDensityValidValues() {
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        double[] testValues = new double[]{-1.0, -1.0};
        double density = dist.density(testValues);
        Assert.assertTrue(density < 1e-10);

        testValues = new double[]{0.0, 0.0};
        Assert.assertEquals(1.0, dist.density(testValues), 1e-8);

        testValues = new double[]{1.0, 1.0};
        density = dist.density(testValues);
        Assert.assertEquals(1.0 / (Math.sqrt(2.0 * Math.PI) * Math.pow(2.0, 2.0) * Math.exp(-1.0)), density, 1e-8);
    }

    /**
     * Test the density() method with invalid dimensions.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testDensityInvalidDimensions() {
        double[] means = new double[]{0.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}, {0.5, 0.5}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        double[] testValues = new double[]{0.0, 0.0, 0.0};
        dist.density(testValues);
    }

    /**
     * Test the density() method with non-positive definite covariance matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testDensityNonPositiveDefiniteCovarianceMatrix() {
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = new double[][]{{1.0, -1.0}, {-1.0, 1.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    /**
     * Test the density() method with singular covariance matrix.
     */
    @Test(expected = SingularMatrixException.class)
    public void testDensitySingularCovarianceMatrix() {
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = new double[][]{{0.0, 1.0}, {1.0, 0.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }
}