package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;

public class MultivariateNormalDistributionTest {

    private static final double EPSILON = 1e-14;

    @Test
    public void testMultivariateNormalDistribution() {
        // Test creation with given mean and covariance matrix (identity)
        double[] means = new double[]{1.0, 2.0, 3.0};
        double[][] covariances = new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        RealDistribution dist = new MultivariateNormalDistribution(means, covariances);
        assertEquals(3, dist.getDimension());
        assertArrayEquals(means, dist.getMeans());
        RealMatrix covarianceMatrix = dist.getCovariances();
        assertTrue(covarianceMatrix.equals(new Array2DRowRealMatrix(covariances)));

        // Test creation with RandomGenerator
        RandomGenerator rng = new Well19937c();
        means = new double[]{1.0, 2.0, 3.0};
        covariances = new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        dist = new MultivariateNormalDistribution(rng, means, covariances);
        assertEquals(3, dist.getDimension());
        assertArrayEquals(means, dist.getMeans());
        covarianceMatrix = dist.getCovariances();
        assertTrue(covarianceMatrix.equals(new Array2DRowRealMatrix(covariances)));

        // Test density
        double[] testValues = new double[]{1.0, 2.0, 3.0};
        double density = dist.density(testValues);
        double normalConstant = FastMath.pow(2 * FastMath.PI, -1.5) * FastMath.pow(covarianceMatrixDeterminant, -0.5);
        double exponentTerm = dist.getExponentTerm(testValues);
        assertEquals(density, normalConstant * exponentTerm, EPSILON);

        testValues = new double[]{-1.0, 2.0, 3.0};
        density = dist.density(testValues);
        exponentTerm = dist.getExponentTerm(testValues);
        assertEquals(density, 0, EPSILON);

        // Test sample
        double[] sample = dist.sample();
        for (int i = 0; i < sample.length; i++) {
            assertEquals(means[i] + Math.randomGauss(0, 1), sample[i], EPSILON);
        }

        sample = dist.sample();
        for (int i = 0; i < sample.length; i++) {
            assertEquals(means[i] + Math.randomGauss(0, 1), sample[i], EPSILON);
        }
    }

    @Test
    public void testSingularMatrixException() {
        try {
            double[] means = new double[]{1.0};
            double[][] covariances = new double[][]{{1.0, 0.0}, {0.0, 1.0}, {0.0, 0.0}};
            new MultivariateNormalDistribution(means, covariances);
            fail("Should throw SingularMatrixException");
        } catch (SingularMatrixException e) {
            // Expected
        }

        try {
            double[] means = new double[]{1.0};
            double[][] covariances = new double[][]{{1.0, 1.0}, {1.0, 1.0}, {1.0, 1.0}};
            new MultivariateNormalDistribution(means, covariances);
            fail("Should throw SingularMatrixException");
        } catch (SingularMatrixException e) {
            // Expected
        }

        try {
            double[] means = new double[]{1.0};
            double[][] covariances = new double[][]{{1.0, 0.0}, {0.0, 1.0}, {1.0, 1.0}};
            new MultivariateNormalDistribution(means, covariances);
            fail("Should throw SingularMatrixException");
        } catch (SingularMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrixException() {
        try {
            double[] means = new double[]{1.0};
            double[][] covariances = new double[][]{{-1.0, 0.0}, {0.0, -1.0}};
            new MultivariateNormalDistribution(means, covariances);
            fail("Should throw NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testDimensionMismatchException() {
        try {
            double[] means = new double[]{1.0, 2.0};
            double[][] covariances = new double[][]{{1.0}, {2.0, 3.0}};
            new MultivariateNormalDistribution(means, covariances);
            fail("Should throw DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }

        try {
            double[] means = new double[]{1.0};
            double[][] covariances = new double[][]{{1.0, 2.0, 3.0}};
            new MultivariateNormalDistribution(means, covariances);
            fail("Should throw DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }

        try {
            double[] means = new double[]{1.0, 2.0};
            double[][] covariances = new double[][]{{1.0, 0.0}, {0.0, 3.0}};
            new MultivariateNormalDistribution(means, covariances);
            fail("Should throw DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }
}