package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.TestUtils;
import org.junit.Test;

/**
 * Test suite for {@link MultivariateNormalDistribution}.
 */
public class MultivariateNormalDistributionTest {
    /**
     * Test for constructor with a vector of means and a covariance matrix.
     *
     * @throws Exception if an error occurred during the test.
     */
    @Test
    public void testMultivariateNormalDistribution() throws Exception {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {
            {3.0, 1.5},
            {1.5, 3.0}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        assertEquals(2, dist.getDimension());
        assertArrayEquals(means, dist.getMeans());

        RealMatrix covarianceMatrix = dist.getCovariances();
        assertEquals(2, covarianceMatrix.getRowDimension());
        assertEquals(2, covarianceMatrix.getColumnDimension());
        assertArrayEquals(covariances[0], covarianceMatrix.getColumnVector(0).toArray());
        assertArrayEquals(covariances[1], covarianceMatrix.getColumnVector(1).toArray());
    }

    /**
     * Test for constructor with a random number generator, a vector of means,
     * and a covariance matrix.
     *
     * @throws Exception if an error occurred during the test.
     */
    @Test
    public void testMultivariateNormalDistributionRandomGenerator() throws Exception {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {
            {3.0, 1.5},
            {1.5, 3.0}
        };
        final RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(rng, means, covariances);
        assertEquals(2, dist.getDimension());
        assertArrayEquals(means, dist.getMeans());

        RealMatrix covarianceMatrix = dist.getCovariances();
        assertEquals(2, covarianceMatrix.getRowDimension());
        assertEquals(2, covarianceMatrix.getColumnDimension());
        assertArrayEquals(covariances[0], covarianceMatrix.getColumnVector(0).toArray());
        assertArrayEquals(covariances[1], covarianceMatrix.getColumnVector(1).toArray());
    }

    /**
     * Test for constructor with a vector of means and a covariance matrix,
     * where the covariance matrix is not square.
     *
     * @throws Exception if an error occurred during the test.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistributionDimensionMismatch() throws Exception {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {
            {3.0},
            {1.5, 3.0}
        };
        new MultivariateNormalDistribution(covariances);
    }

    /**
     * Test for constructor with a vector of means and a covariance matrix,
     * where the covariance matrix is not positive definite.
     *
     * @throws Exception if an error occurred during the test.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testMultivariateNormalDistributionNonPositiveDefiniteMatrix() throws Exception {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {
            {-3.0, 1.5},
            {1.5, -3.0}
        };
        new MultivariateNormalDistribution(covariances);
    }

    /**
     * Test for constructor with a random number generator, a vector of means,
     * and a covariance matrix, where the covariance matrix is not square.
     *
     * @throws Exception if an error occurred during the test.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistributionRandomGeneratorDimensionMismatch() throws Exception {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {
            {3.0},
            {1.5, 3.0}
        };
        new MultivariateNormalDistribution(new Well19937c(), covariances);
    }

    /**
     * Test for constructor with a random number generator, a vector of means,
     * and a covariance matrix, where the covariance matrix is not positive definite.
     *
     * @throws Exception if an error occurred during the test.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testMultivariateNormalDistributionRandomGeneratorNonPositiveDefiniteMatrix() throws Exception {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {
            {-3.0, 1.5},
            {1.5, -3.0}
        };
        new MultivariateNormalDistribution(new Well19937c(), covariances);
    }

    /**
     * Test for density calculation.
     */
    @Test
    public void testDensity() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {
            {3.0, 1.5},
            {1.5, 3.0}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] testValues = new double[] {1.5, 2.5};
        double expected = 0.10557264; // see MathWorld for the formula
        double calculated = dist.density(testValues);
        assertEquals(expected, calculated, 1e-5);
    }

    /**
     * Test for density calculation with negative values.
     */
    @Test
    public void testDensityNegativeValues() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {
            {3.0, 1.5},
            {1.5, 3.0}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] testValues = new double[] {-1.5, -2.5};
        double calculated = dist.density(testValues);
        assertFalse(Double.isFinite(calculated));
    }

    /**
     * Test for density calculation with zero values.
     */
    @Test
    public void testDensityZeroValues() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {
            {3.0, 1.5},
            {1.5, 3.0}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] testValues = new double[] {0.0, 0.0};
        double calculated = dist.density(testValues);
        assertEquals(0.0, calculated, 1e-15);
    }

    /**
     * Test for standard deviations calculation.
     */
    @Test
    public void testStandardDeviations() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {
            {3.0, 1.5},
            {1.5, 3.0}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] stdDev = dist.getStandardDeviations();
        double[] expected = new double[] {FastMath.sqrt(3.0), FastMath.sqrt(3.0)};
        assertArrayEquals(expected, stdDev, 1e-5);
    }

    /**
     * Test for sample method.
     */
    @Test
    public void testSample() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {
            {3.0, 1.5},
            {1.5, 3.0}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] sample = dist.sample();
        double[] expected = new double[] {2.02348531, 3.26171394};
        assertArrayEquals(expected, sample, 1e-5);
    }

    /**
     * Test for sample method with a different random number generator.
     */
    @Test
    public void testSampleDifferentRandomNumberGenerator() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {
            {3.0, 1.5},
            {1.5, 3.0}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        RandomGenerator rng = new Well19937c();
        double[] sample = dist.sample(rng);
        double[] expected = new double[] {-0.67343146, 0.3547733};
        assertArrayEquals(expected, sample, 1e-5);
    }
}