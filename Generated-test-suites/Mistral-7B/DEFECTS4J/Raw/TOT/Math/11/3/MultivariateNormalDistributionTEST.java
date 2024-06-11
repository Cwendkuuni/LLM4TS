package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSpectralDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for MultivariateNormalDistribution.
 */
public class MultivariateNormalDistributionTest {

    /**
     * Test constructor with valid input.
     */
    @Test
    public void testValidConstruction() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {
            {1.0, 0.5},
            {0.5, 1.0}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        Assert.assertEquals(means.length, dist.getDimension());
        Assert.assertArrayEquals(means, dist.getMeans());
        RealMatrix covarianceMatrix = dist.getCovariances();
        Assert.assertEquals(means.length, covarianceMatrix.getRowDimension());
        Assert.assertEquals(means.length, covarianceMatrix.getColumnDimension());
        Assert.assertTrue(covarianceMatrix.isSymmetric());
    }

    /**
     * Test constructor with invalid input.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testInvalidConstruction() {
        double[] means = {0.0};
        double[][] covariances = {
            {1.0},
            {0.5}
        };
        new MultivariateNormalDistribution(means, covariances);
    }

    /**
     * Test density calculation with valid input.
     */
    @Test
    public void testDensityValidInput() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {
            {1.0, 0.5},
            {0.5, 1.0}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] values = {1.0, 1.5};
        double density = dist.density(values);
        double expValue = FastMath.pow(2 * FastMath.PI, -2.0) * FastMath.pow(Math.E, -1.0) *
            (1.0 / (2.0 * Math.PI) * Math.sqrt(Math.PI)) * Math.exp(-0.5 * ((0.5 * Math.pow(values[0] - means[0], 2.0)) + (1.0 * Math.pow(values[1] - means[1], 2.0))));
        Assert.assertEquals(expValue, density, 1e-6);
    }

    /**
     * Test density calculation with invalid input.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testDensityInvalidInput() {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), new double[0], new double[0][0]);
        double[] values = new double[1];
        dist.density(values);
    }

    /**
     * Test sampling with valid input.
     */
    @Test
    public void testSamplingValidInput() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {
            {1.0, 0.5},
            {0.5, 1.0}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] sample = dist.sample();
        Assert.assertEquals(means.length, sample.length);
        for (int i = 0; i < sample.length; i++) {
            Assert.assertTrue(Double.isFinite(sample[i]));
        }
    }

    /**
     * Test sampling with invalid input.
     */
    @Test(expected = IllegalStateException.class)
    public void testSamplingInvalidInput() {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), new double[0], new double[0][0]);
        dist.sample();
    }

    /**
     * Test standard deviations calculation with valid input.
     */
    @Test
    public void testStandardDeviationsValidInput() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {
            {1.0, 0.5},
            {0.5, 1.0}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] standardDeviations = dist.getStandardDeviations();
        Assert.assertEquals(means.length, standardDeviations.length);
        for (int i = 0; i < standardDeviations.length; i++) {
            Assert.assertTrue(standardDeviations[i] > 0.0);
        }
    }

    /**
     * Test standard deviations calculation with invalid input.
     */
    @Test(expected = IllegalStateException.class)
    public void testStandardDeviationsInvalidInput() {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), new double[0], new double[0][0]);
        dist.getStandardDeviations();
    }

    /**
     * Test eigenvalue decomposition of covariance matrix.
     */
    @Test
    public void testEigenvalueDecomposition() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {
            {1.0, 0.5},
            {0.5, 1.0}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        RealMatrix covarianceMatrix = dist.getCovariances();
        RealArray2DSpectralDecomposition eigDecomposition = new RealArray2DSpectralDecomposition(covarianceMatrix);
        double[] eigenvalues = eigDecomposition.getEigenvalues();
        for (int i = 0; i < eigenvalues.length; i++) {
            Assert.assertTrue(eigenvalues[i] > 0.0);
        }
    }

    /**
     * Test eigenvector calculation with valid input.
     */
    @Test
    public void testEigenvectorsValidInput() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {
            {1.0, 0.5},
            {0.5, 1.0}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        RealMatrix covarianceMatrix = dist.getCovariances();
        RealArray2DSpectralDecomposition eigDecomposition = new RealArray2DSpectralDecomposition(covarianceMatrix);
        RealMatrix eigenvectors = eigDecomposition.getV();
        for (int i = 0; i < eigenvectors.getRowDimension(); i++) {
            RealVector eigenvector = eigenvectors.getColumnVector(i);
            double sum = 0.0;
            for (int j = 0; j < eigenvector.getDimension(); j++) {
                sum += eigenvector.getEntry(j) * eigenvector.getEntry(j);
            }
            Assert.assertEquals(1.0, sum, 1e-6);
        }
    }

    /**
     * Test mean vector calculation with valid input.
     */
    @Test
    public void testMeansValidInput() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {
            {1.0, 0.5},
            {0.5, 1.0}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        Assert.assertArrayEquals(means, dist.getMeans());
    }

    /**
     * Test mean vector calculation with invalid input.
     */
    @Test(expected = IllegalStateException.class)
    public void testMeansInvalidInput() {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), new double[0], new double[0][0]);
        dist.getMeans();
    }

    /**
     * Test covariance matrix calculation with valid input.
     */
    @Test
    public void testCovarianceMatrixValidInput() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {
            {1.0, 0.5},
            {0.5, 1.0}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        RealMatrix covarianceMatrix = dist.getCovariances();
        Assert.assertTrue(covarianceMatrix.isSymmetric());
    }

    /**
     * Test covariance matrix calculation with invalid input.
     */
    @Test(expected = IllegalStateException.class)
    public void testCovarianceMatrixInvalidInput() {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), new double[0], new double[0][0]);
        dist.getCovariances();
    }

    /**
     * Test inverse covariance matrix calculation with valid input.
     */
    @Test
    public void testCovarianceMatrixInverseValidInput() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {
            {1.0, 0.5},
            {0.5, 1.0}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        RealMatrix covarianceMatrix = dist.getCovariances();
        RealMatrix inverse = covarianceMatrix.inverse();
        RealMatrix product = covarianceMatrix.multiply(inverse);
        Assert.assertEquals(1.0, StatUtils.determinant(covarianceMatrix), 1e-6);
        Assert.assertTrue(product.isIdentityMatrix());
    }

    /**
     * Test inverse covariance matrix calculation with invalid input.
     */
    @Test(expected = SingularMatrixException.class)
    public void testCovarianceMatrixInverseInvalidInput() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {
            {0.0, 0.5},
            {0.5, 0.0}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        dist.getCovariances().inverse();
    }

    /**
     * Test determinant calculation with valid input.
     */
    @Test
    public void testDeterminantValidInput() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {
            {1.0, 0.5},
            {0.5, 1.0}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        Assert.assertEquals(1.0, StatUtils.determinant(dist.getCovariances()), 1e-6);
    }

    /**
     * Test determinant calculation with invalid input.
     */
    @Test(expected = IllegalStateException.class)
    public void testDeterminantInvalidInput() {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), new double[0], new double[0][0]);
        StatUtils.determinant(dist.getCovariances());
    }
}