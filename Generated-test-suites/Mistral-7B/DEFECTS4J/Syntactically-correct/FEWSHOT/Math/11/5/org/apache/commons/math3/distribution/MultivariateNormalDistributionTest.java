package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution.CovarianceType;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    private static RandomGenerator rng = new Well19937c();

    @Test
    public void testSimpleConstructor() throws Exception {
        double[] means = new double[]{0.5};
        double[][] covariances = new double[][]{{0.5, 0.5}, {0.5, 0.5}};

        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        assertEquals(means.length, dist.getDimension());
        assertArrayEquals(means, dist.getMeans());

        RealMatrix covMat = dist.getCovariances();
        assertArrayEquals(covariances, covMat.getData());

        double[] stdDev = dist.getStandardDeviations();
        assertEquals(1.0, FastMath.sqrt(covMat.getEntry(0, 0)), 1e-6);
        assertEquals(1.0, FastMath.sqrt(covMat.getEntry(1, 1)), 1e-6);

        double[] sample = dist.sample();
        assertEquals(means.length, sample.length);
        for (int i = 0; i < sample.length; i++) {
            assertTrue(sample[i] >= -3.0 * FastMath.SQRT_2 * FastMath.sqrt(covMat.getEntry(i, i)));
            assertTrue(sample[i] <= 3.0 * FastMath.SQRT_2 * FastMath.sqrt(covMat.getEntry(i, i)));
        }

        double[] values = new double[]{0.5, 0.5};
        double density = dist.density(values);
        double expectedDensity = 1.0 / (2 * FastMath.PI) / FastMath.SQRT_2
                * FastMath.exp(-0.5);
        assertEquals(expectedDensity, density, 1e-5);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithMismatchedMeansAndCovariances() throws Exception {
        double[] means = new double[]{0.5};
        double[][] covariances = new double[][]{{0.5, 0.5}, {0.5, 0.5, 0.5}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithMismatchedCovariances() throws Exception {
        double[] means = new double[]{0.5};
        double[][] covariances = new double[][]{{0.5, 0.5}, {0.5, 0.5, 0.5, 0.5}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testConstructorWithSingularCovariances() throws Exception {
        double[] means = new double[]{0.5};
        double[][] covariances = new double[][]{{0.5, 0.5}, {0.5, 0.5}, {0.5, 0.5}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNegativeEigenvalues() throws Exception {
        double[] means = new double[]{0.5};
        double[][] covariances = new double[][]{{-1.0, 0.5}, {0.5, -1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testDensity() throws Exception {
        double[] means = new double[]{0.5};
        double[][] covariances = new double[][]{{0.5, 0.5}, {0.5, 0.5}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] values = new double[]{0.5, 0.5};
        double density = dist.density(values);
        double expectedDensity = 1.0 / (2 * FastMath.PI) / FastMath.SQRT_2 * FastMath.exp(-0.5);
        assertEquals(expectedDensity, density, 1e-5);
    }

    @Test
    public void testSample() throws Exception {
        double[] means = new double[]{0.5};
        double[][] covariances = new double[][]{{0.5, 0.5}, {0.5, 0.5}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] sample = dist.sample();
        for (int i = 0; i < sample.length; i++) {
            assertEquals(means[i], sample[i], 1e-5);
        }
    }

    @Test
    public void testSampleCovarianceTypeIdentity() throws Exception {
        double[] means = new double[]{0.5};
        double[][] covariances = new double[][]{{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances, CovarianceType.IDENTITY);
        double[] sample = dist.sample();
        for (int i = 0; i < sample.length; i++) {
            assertEquals(means[i], sample[i], 1e-5);
        }
    }

    @Test
    public void testSampleCovarianceTypeCholesky() throws Exception {
        double[] means = new double[]{0.5};
        double[][] covariances = new double[][]{{1.0, 1.0}, {1.0, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances, CovarianceType.CHOLESKY);
        double[] sample = dist.sample();
        for (int i = 0; i < sample.length; i++) {
            assertEquals(means[i], sample[i], 1e-5);
        }
    }

    @Test
    public void testSampleCovarianceTypeDense() throws Exception {
        double[] means = new double[]{0.5};
        double[][] covariances = new double[][]{{1.0, 1.0}, {1.0, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances, CovarianceType.DENSE);
        double[] sample = dist.sample();
        for (int i = 0; i < sample.length; i++) {
            assertEquals(means[i], sample[i], 1e-5);
        }
    }

    @Test
    public void testGetCovariances() throws Exception {
        double[] means = new double[]{0.5};
        double[][] covariances = new double[][]{{0.5, 0.5}, {0.5, 0.5}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        RealMatrix covMatrix = dist.getCovariances();
        assertArrayEquals(covMat.getData(), covMatrix.getData());
    }

    @Test
    public void testGetStandardDeviations() throws Exception {
        double[] means = new double[]{0.5};
        double[][] covariances = new double[][]{{0.5, 0.5}, {0.5, 0.5}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] stdDev = dist.getStandardDeviations();
        assertEquals(1.0, stdDev[0], 1e-5);
        assertEquals(1.0, stdDev[1], 1e-5);
    }
}