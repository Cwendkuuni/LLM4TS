package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;

/**
 * Test class for MultivariateNormalDistribution.
 */
public class MultivariateNormalDistributionTest {

    @Test
    public void testConstructors() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};

        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        assertEquals(means.length, dist.getDimension());
        assertEquals(means, dist.getMeans());

        // Check the covariance matrix is correct
        RealMatrix cov = dist.getCovariances();
        assertEquals(means.length, cov.getRowDimension());
        assertEquals(means.length, cov.getColumnDimension());
        assertEquals(covariances[0].length, cov.getColumnDimension());
        for (int i = 0; i < cov.getRowDimension(); i++) {
            for (int j = 0; j < cov.getColumnDimension(); j++) {
                assertEquals(covariances[i][j], cov.getEntry(i, j), 1e-12);
            }
        }

        RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution dist2 = new MultivariateNormalDistribution(rng, means, covariances);
        assertEquals(means, dist2.getMeans());
        assertEquals(cov, dist2.getCovariances());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithInvalidMeans() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithInvalidCovariances() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0}, {1.0, 2.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testConstructorWithSingularCovarianceMatrix() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNegativeEigenvalues() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{-1.0, 0.5}, {0.5, -2.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testDensity() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);

        double[] values = {1.5, 2.5};
        double density = dist.density(values);
        double expectedDensity = Math.exp(-0.5 * (Math.pow(values[0] - means[0], 2) + Math.pow(values[1] - means[1], 2))) / (Math.sqrt(2 * Math.PI) * Math.sqrt(Math.abs(covariances.determinant())));
        assertEquals(expectedDensity, density, 1e-5);
    }

    @Test
    public void testSample() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);

        double[] samples = dist.sample();
        for (int i = 0; i < samples.length; i++) {
            assertEquals(Precision.isClose(means[i], samples[i], 1e-5), true);
        }
    }

    @Test
    public void testSampleWithCustomRNG() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(rng, means, covariances);

        double[] samples = dist.sample();
        for (int i = 0; i < samples.length; i++) {
            assertEquals(Precision.isClose(means[i], samples[i], 1e-5), true);
        }
    }

    @Test
    public void testGetStandardDeviations() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] stdDevs = dist.getStandardDeviations();
        assertEquals(stdDevs[0], Math.sqrt(covariances[0][0]), 1e-5);
        assertEquals(stdDevs[1], Math.sqrt(covariances[1][1]), 1e-5);
    }

    @Test
    public void testGetMeans() {
        final double[] means = {1.0, 2.0};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, new double[0][]);
        assertEquals(means[0], dist.getMeans()[0]);
        assertEquals(means[1], dist.getMeans()[1]);
    }

    @Test
    public void testGetCovariances() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        RealMatrix covMatrix = dist.getCovariances();
        for (int i = 0; i < covMatrix.getRowDimension(); i++) {
            for (int j = 0; j < covMatrix.getColumnDimension(); j++) {
                assertEquals(covariances[i][j], covMatrix.getEntry(i, j), 1e-5);
            }
        }
    }
}