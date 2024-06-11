package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution.CovarianceMatrixType;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistribution() throws Exception {
        final int dim = 3;

        // Test with identity covariance matrix.
        double[] means = new double[dim];
        double[][] covariances = new double[dim][dim];
        for (int i = 0; i < dim; i++) {
            means[i] = 0;
            for (int j = 0; j < dim; j++) {
                covariances[i][j] = (i == j) ? 1 : 0;
            }
        }
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        assertEquals(dim, dist.getDimension());
        assertArrayEquals(means, dist.getMeans());

        RealMatrix covMat = dist.getCovariances();
        RealMatrix covMatInv = dist.getCovarianceMatrixInverse();
        assertEquals(covMat.getRowDimension(), covMat.getColumnDimension());
        assertEquals(covMat.getRowDimension(), covMatInv.getRowDimension());
        assertEquals(covMat.getColumnDimension(), covMatInv.getColumnDimension());
        RealMatrix idMat = new Array2DRowRealMatrix(new double[dim][dim]);
        idMat.setIdentityMatrix(dim);
        assertEquals(idMat, covMat.multiply(covMatInv));

        double[] sample = dist.sample();
        assertEquals(dim, sample.length);
        for (int i = 0; i < dim; i++) {
            assertEquals(0, i, sample[i]);
        }

        double density = dist.density(sample);
        assertTrue(density > 0);

        // Test with covariance matrix "A".
        means = new double[]{1, 2, 3};
        covariances = new double[][]{{1, 0.5, 0}, {0.5, 2, 1}, {0, 1, 3}};
        MultivariateNormalDistribution distA = new MultivariateNormalDistribution(means, covariances);
        assertEquals(dim, distA.getDimension());
        assertArrayEquals(means, distA.getMeans());

        covMat = distA.getCovariances();
        covMatInv = distA.getCovarianceMatrixInverse();
        assertArrayEquals(new double[][]{
            {3, -1.5, -0.5},
            {-1.5, 2.5, 1.5},
            {-0.5, 1.5, 3.5}}, covMat.getData(), 0.001);
        assertArrayEquals(new double[][]{
            {0.3333333333333333, -0.1666666666666667, -0.1},
            {-0.1666666666666667, 0.4166666666666667, 0.2333333333333334},
            {-0.1, 0.2333333333333334, 0.1666666666666667}}, covMatInv.getData(), 0.001);

        sample = distA.sample();
        assertArrayEquals(new double[]{3.412, 1.234, -1.567}, sample, 0.001);

        density = distA.density(sample);
        assertTrue(density > 0);

        // Test with covariance matrix "B".
        means = new double[]{1, 2, 3};
        covariances = new double[][]{{1, 0, 0}, {0, 2, 0}, {0, 0, 4}};
        MultivariateNormalDistribution distB = new MultivariateNormalDistribution(means, covariances);
        assertEquals(dim, distB.getDimension());
        assertArrayEquals(means, distB.getMeans());

        covMat = distB.getCovariances();
        covMatInv = distB.getCovarianceMatrixInverse();
        assertArrayEquals(new double[][]{
            {0.1, 0, 0},
            {0, 0.5, 0},
            {0, 0, 0.2}}, covMatInv.getData(), 0.001);

        sample = distB.sample();
        assertArrayEquals(new double[]{1.51, 2.52, 3.01}, sample, 0.001);

        density = distB.density(sample);
        assertTrue(density > 0);

        // Test with covariance matrix "C".
        means = new double[]{1, 2, 3};
        covariances = new double[][]{{1, 2, 3}, {2, 4, 6}, {3, 6, 9}};
        MultivariateNormalDistribution distC = new MultivariateNormalDistribution(means, covariances);
        assertEquals(dim, distC.getDimension());
        assertArrayEquals(means, distC.getMeans());

        covMat = distC.getCovariances();
        covMatInv = distC.getCovarianceMatrixInverse();
        RealArray2D eigVals = distC.getCovarianceMatrixEigenvalues();
        RealVector eigenvectors = distC.getCovarianceMatrixEigenvectors().getColumnVector(0);
        double determinant = distC.getCovarianceMatrixDeterminant();
        // Manual check of eigenvalues and eigenvectors.
        double[] eigenvalues = eigVals.toArray();
        double[] eigenvector = eigenvectors.toArray();
        assertEquals(2, eigenvalues.length);
        assertArrayEquals(new double[]{3.315114250211879, 7.87501655958223}, eigenvalues, 0.001);
        assertArrayEquals(new double[]{0.6324555329876434, -0.7568024987944662, 0.6154237335499326}, eigenvector, 0.001);

        sample = distC.sample();
        assertArrayEquals(new double[]{-0.7004636305964996, 1.6227654945319333, 4.141063320299582}, sample, 0.001);

        density = distC.density(sample);
        assertTrue(density > 0);

        // Test with covariance matrix "D" (singular).
        means = new double[]{1, 2, 3};
        covariances = new double[][]{{1, 2, 3}, {2, 4, 6}, {3, 6, 0}};
        try {
            new MultivariateNormalDistribution(means, covariances);
            fail("Expected SingularMatrixException");
        } catch (SingularMatrixException e) {
            // Expected
        }

        // Test with covariance matrix "E" (non-positive definite).
        means = new double[]{1, 2, 3};
        covariances = new double[][]{{1, 2, 3}, {2, -2, 6}, {3, 6, -3}};
        try {
            new MultivariateNormalDistribution(means, covariances);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }

        // Test with invalid input.
        means = null;
        try {
            new MultivariateNormalDistribution(means, covariances);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        covariances = null;
        try {
            new MultivariateNormalDistribution(means, covariances);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        covariances = new double[][]{{1}};
        try {
            new MultivariateNormalDistribution(means, covariances);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        covariances = new double[][]{{1, 2}, {2, 1}};
        try {
            new MultivariateNormalDistribution(means, covariances);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        covariances = new double[][]{{1, 2}, {2, 3}};
        try {
            new MultivariateNormalDistribution(means, covariances);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        covariances = new double[][]{{1, 2, 3}, {2, 4, 6}, {3, 6, 9}, {4, 6, 9}};
        try {
            new MultivariateNormalDistribution(means, covariances);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        covariances = new double[][]{{1, 2, 3}, {2, 4, 6}, {3, 6, 9}, {4, 6, 9}};
        try {
            new MultivariateNormalDistribution(means, covariances, CovarianceMatrixType.CHOLESKY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}