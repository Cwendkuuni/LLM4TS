package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
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

    /**
     * Test constructor with given mean vector and covariance matrix.
     */
    @Test
    public void testConstructorWithGivenMeanVectorAndCovarianceMatrix() {
        RandomGenerator rng = new Well19937c();

        double[] means = new double[]{1.0, 2.0, 3.0};
        double[][] covariances = {
                {1.0, 0.5, 0.5},
                {0.5, 1.5, 0.5},
                {0.5, 0.5, 2.5}
        };

        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        assertEquals(means.length, dist.getDimension());
        assertArrayEquals(means, dist.getMeans());

        RealMatrix covarianceMatrix = dist.getCovariances();
        assertEquals(means.length, covarianceMatrix.getRowDimension());
        assertEquals(means.length, covarianceMatrix.getColumnDimension());
        assertArrayEquals(covariances[0], covarianceMatrix.getColumnVector(0).toArray());
        assertArrayEquals(covariances[1], covarianceMatrix.getColumnVector(1).toArray());
        assertArrayEquals(covariances[2], covarianceMatrix.getColumnVector(2).toArray());
    }

    /**
     * Test constructor with given mean vector and covariance matrix from a random source.
     */
    @Test
    public void testConstructorWithGivenMeanVectorAndCovarianceMatrixFromRandomSource() {
        RandomGenerator rng = new Well19937c();

        double[] means = new double[]{1.0, 2.0, 3.0};
        double[][] covariances = new double[3][3];
        for (int i = 0; i < covariances.length; i++) {
            for (int j = 0; j < covariances[i].length; j++) {
                covariances[i][j] = rng.nextGaussian();
            }
        }

        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(rng, means, covariances);
        assertEquals(means.length, dist.getDimension());
        assertArrayEquals(means, dist.getMeans());

        RealMatrix covarianceMatrix = dist.getCovariances();
        assertEquals(means.length, covarianceMatrix.getRowDimension());
        assertEquals(means.length, covarianceMatrix.getColumnDimension());
        for (int i = 0; i < covariances.length; i++) {
            assertArrayEquals(covariances[i], covarianceMatrix.getColumnVector(i).toArray());
        }
    }

    /**
     * Test density method with given values.
     */
    @Test
    public void testDensityMethodWithGivenValues() {
        RandomGenerator rng = new Well19937c();

        double[] means = new double[]{1.0, 2.0, 3.0};
        double[][] covariances = {
                {1.0, 0.5, 0.5},
                {0.5, 1.5, 0.5},
                {0.5, 0.5, 2.5}
        };

        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] values = new double[]{2.0, 1.5, 4.5};

        double expected = FastMath.pow(2 * FastMath.PI, -3.0 / 2.0) *
                FastMath.pow(dist.covarianceMatrixDeterminant, -0.5) *
                dist.getExponentTerm(values);

        double density = dist.density(values);
        assertEquals(expected, density, 1e-8);
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSampleMethod() {
        RandomGenerator rng = new Well19937c();

        double[] means = new double[]{1.0, 2.0, 3.0};
        double[][] covariances = {
                {1.0, 0.5, 0.5},
                {0.5, 1.5, 0.5},
                {0.5, 0.5, 2.5}
        };

        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);

        double[] sample = dist.sample();
        assertEquals(means.length, sample.length);
        for (int i = 0; i < sample.length; i++) {
            assertTrue(Double.isFinite(sample[i]));
        }
    }

    /**
     * Test constructor with inconsistent arrays length.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithInconsistentArraysLength() {
        RandomGenerator rng = new Well19937c();

        double[] means = new double[]{1.0, 2.0, 3.0};
        double[][] covariances = {
                {1.0, 0.5, 0.5},
                {0.5, 1.5}
        };

        new MultivariateNormalDistribution(means, covariances);
    }

    /**
     * Test constructor with covariance matrix that cannot be decomposed.
     */
    @Test(expected = SingularMatrixException.class)
    public void testConstructorWithSingularCovarianceMatrix() {
        RandomGenerator rng = new Well19937c();

        double[] means = new double[]{1.0, 2.0, 3.0};
        double[][] covariances = {
                {1.0, 0.5, 0.5},
                {0.5, 0.0, 0.5},
                {0.5, 0.5, 0.0}
        };

        new MultivariateNormalDistribution(means, covariances);
    }

    /**
     * Test constructor with negative eigenvalues in covariance matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNegativeEigenvaluesInCovarianceMatrix() {
        RandomGenerator rng = new Well19937c();

        double[] means = new double[]{1.0, 2.0, 3.0};
        double[][] covariances = {
                {1.0, 0.5, 0.5},
                {0.5, -1.5, 0.5},
                {0.5, 0.5, -2.5}
        };

        new MultivariateNormalDistribution(means, covariances);
    }
}