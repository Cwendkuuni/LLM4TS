package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution.CovarianceMatrixSource;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DRowVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testConstructorWithMeansAndSymmetricCovariances() {
        RandomGenerator rng = new Well19937c();
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};

        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(rng, means, covariances);
        assertEquals(means.length, dist.getDimension());
        assertArrayEquals(means, dist.getMeans());

        RealMatrix covarianceMatrix = dist.getCovariances();
        RealMatrix expectedCovarianceMatrix = new Array2DRowRealMatrix(covariances);
        assertArrayEquals(expectedCovarianceMatrix.getData(), covarianceMatrix.getData());

        RealMatrix expectedCovarianceMatrixInverse = dist.getCovarianceMatrixInverse();
        RealMatrix eigenvectors = new Array2DRowRealMatrix(expectedCovarianceMatrix.getColumnNames(), 0, expectedCovarianceMatrix.getColumnDimension());
        RealMatrix eigenvalues = new Array2DRowRealMatrix(expectedCovarianceMatrix.getRowDimension(), 1, new double[]{1.0, 1.0});
        RealMatrix expectedCovarianceMatrixInverseEigen = new EigenDecomposition(expectedCovarianceMatrix.transpose()).getSolver();

        assertArrayEquals(expectedCovarianceMatrixInverseEigen.getInverse().getData(), expectedCovarianceMatrixInverse.getData());
    }

    @Test
    public void testConstructorWithMeansAndAsymmetricCovariances() {
        RandomGenerator rng = new Well19937c();
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 2.0}};

        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(rng, means, covariances);
        assertEquals(means.length, dist.getDimension());
        assertArrayEquals(means, dist.getMeans());

        RealMatrix covarianceMatrix = dist.getCovariances();
        RealMatrix expectedCovarianceMatrix = new Array2DRowRealMatrix(covariances);
        assertArrayEquals(expectedCovarianceMatrix.getData(), covarianceMatrix.getData());

        RealMatrix expectedCovarianceMatrixInverse = dist.getCovarianceMatrixInverse();
        CovarianceMatrixSource source = new CovarianceMatrixSource(expectedCovarianceMatrix.getColumnNames(), 0, expectedCovarianceMatrix.getColumnDimension());
        RealMatrix eigenvectors = new Array2DRowRealMatrix(source, 0, source.getRowDimension());
        RealMatrix eigenvalues = new Array2DRowRealMatrix(source, CovarianceMatrixSource.DIAGONAL, source.getRowDimension());
        RealMatrix expectedCovarianceMatrixInverseEigen = new EigenDecomposition(expectedCovarianceMatrix.transpose()).getSolver();

        assertArrayEquals(expectedCovarianceMatrixInverseEigen.getInverse().getData(), expectedCovarianceMatrixInverse.getData());
    }

    @Test
    public void testDensity() {
        RandomGenerator rng = new Well19937c();
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(rng, means, covariances);

        double[] testValues = new double[]{0.25, 0.25};
        double expectedDensity = FastMath.pow(2 * FastMath.PI, -2) * Math.pow(Math.E, -0.5 * (Math.pow(0.25 - 0.0, 2) + Math.pow(0.25 - 0.0, 2) + 2 * 0.25 * (0.25 - 0.0) * 0.5 + Math.pow(0.25 - 0.0, 2) * 1.0));
        assertEquals(expectedDensity, dist.density(testValues), 0.001);
    }

    @Test
    public void testSample() {
        RandomGenerator rng = new Well19937c();
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(rng, means, covariances);

        double[] sample = dist.sample();
        double[] expectedMeans = dist.getMeans();
        double[] standardDeviations = dist.getStandardDeviations();

        for (int i = 0; i < sample.length; i++) {
            assertEquals(expectedMeans[i], sample[i], 0.001);
        }

        for (int i = 0; i < sample.length; i++) {
            assertTrue(Math.abs(sample[i] - expectedMeans[i]) < standardDeviations[i] * 1.5);
        }
    }

    @Test
    public void testSampleWithMeansAndAsymmetricCovariances() {
        RandomGenerator rng = new Well19937c();
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(rng, means, covariances);

        double[] sample = dist.sample();
        double[] expectedMeans = dist.getMeans();
        double[] standardDeviations = dist.getStandardDeviations();

        for (int i = 0; i < sample.length; i++) {
            assertEquals(expectedMeans[i], sample[i], 0.001);
        }

        for (int i = 0; i < sample.length; i++) {
            assertTrue(Math.abs(sample[i] - expectedMeans[i]) < standardDeviations[i] * 1.5);
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityWithInvalidArguments() {
        RandomGenerator rng = new Well19937c();
        double[] means = new double[]{0.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}, {0.0, 0.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(rng, means, covariances);

        double[] invalidValues = new double[]{0.25, 0.25, 0.0};
        dist.density(invalidValues);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSampleWithInvalidArguments() {
        RandomGenerator rng = new Well19937c();
        double[] means = new double[]{0.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}, {0.0, 0.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(rng, means, covariances);

        double[] invalidValues = new double[]{0.25, 0.25, 0.0};
        dist.sample(invalidValues.length);
    }
}