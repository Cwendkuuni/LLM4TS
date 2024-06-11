package org.apache.commons.math3.optimization.general;

import org.junit.Assert;
import org.junit.Test;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationProblem;

/**
 * Tests for {@link GaussNewtonOptimizer}.
 */
public class GaussNewtonOptimizerTest {

    /**
     * Test the case where the optimization problem has a unique solution.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testUniqueSolution() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Define the optimization problem.
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem() {
            @Override
            public double value(double[] x) {
                return 0.1 * Math.pow(Math.sin(x[0]), 2) + Math.pow(Math.cos(x[0]) - 1, 2);
            }

            @Override
            public double[] gradient(double[] x) {
                double[] gradient = new double[1];
                gradient[0] = -0.2 * Math.sin(x[0]) * Math.cos(x[0]) - 2 * (Math.cos(x[0]) - 1);
                return gradient;
            }

            @Override
            public double[] startPoint() {
                return new double[]{0.0};
            }
        };

        // Define the convergence checker.
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker(1e-6, 1e-6);

        // Define the weight matrix.
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
            {1.0, 0.0}, {0.0, 1.0}
        });

        // Run the optimization.
        PointValuePair result = optimizer.optimize(problem, weightMatrix, checker);

        // Check the result.
        Assert.assertArrayEquals(new double[]{Math.PI / 2}, result.getPoint(), 1e-6);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Test the case where the optimization problem has no solution.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test(expected = ConvergenceException.class)
    public void testNoSolution() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Define the optimization problem.
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem() {
            @Override
            public double value(double[] x) {
                return Math.sin(Math.PI) + Math.cos(Math.PI);
            }

            @Override
            public double[] gradient(double[] x) {
                throw new UnsupportedOperationException();
            }

            @Override
            public double[] startPoint() {
                return new double[]{0.0};
            }
        };

        // Define the convergence checker.
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker(1e-6, 1e-6);

        // Define the weight matrix.
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
            {1.0, 0.0}, {0.0, 1.0}
        });

        // Run the optimization.
        optimizer.optimize(problem, weightMatrix, checker);
    }

    /**
     * Test the case where the optimization problem has multiple solutions.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testMultipleSolutions() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Define the optimization problem.
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]), 2) + Math.pow(Math.cos(x[0]) - 1, 2);
            }

            @Override
            public double[] gradient(double[] x) {
                double[] gradient = new double[1];
                gradient[0] = -2 * Math.sin(x[0]) * Math.cos(x[0]) - 2 * (Math.cos(x[0]) - 1);
                return gradient;
            }

            @Override
            public double[] startPoint() {
                return new double[]{0.0};
            }
        };

        // Define the convergence checker.
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker(1e-6, 1e-6);

        // Define the weight matrix.
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
            {1.0, 0.0}, {0.0, 1.0}
        });

        // Run the optimization with LU decomposition.
        PointValuePair result1 = optimizer.optimize(problem, weightMatrix, checker, false);
        Assert.assertArrayEquals(new double[]{Math.PI / 2}, result1.getPoint(), 1e-6);
        Assert.assertEquals(0.0, result1.getValue(), 1e-6);

        // Run the optimization with QR decomposition.
        PointValuePair result2 = optimizer.optimize(problem, weightMatrix, checker, true);
        Assert.assertArrayEquals(new double[]{-Math.PI / 2}, result2.getPoint(), 1e-6);
        Assert.assertEquals(0.0, result2.getValue(), 1e-6);
    }

    /**
     * Test the case where the optimization problem is ill-conditioned.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testIllConditioned() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Define the optimization problem.
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]), 2) / (Math.pow(1e-16, 2) + Math.pow(Math.cos(x[0]) - 1, 2));
            }

            @Override
            public double[] gradient(double[] x) {
                double[] gradient = new double[1];
                double sinX = Math.sin(x[0]);
                double cosX = Math.cos(x[0]);
                double term1 = 1.0 / (Math.pow(1e-16, 2) + Math.pow(cosX - 1, 2));
                double term2 = -2 * (cosX - 1) * (sinX * term1);
                gradient[0] = term2;
                return gradient;
            }

            @Override
            public double[] startPoint() {
                return new double[]{0.0};
            }
        };

        // Define the convergence checker.
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker(1e-6, 1e-6);

        // Define the weight matrix.
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
            {1.0, 0.0}, {0.0, 1.0}
        });

        // Run the optimization.
        try {
            optimizer.optimize(problem, weightMatrix, checker, false);
            Assert.fail();
        } catch (ConvergenceException e) {
            // Expected.
        }

        // Check that the singular value decomposition of the gradient matrix
        // indicates an ill-conditioned problem.
        RealMatrix jacobian = optimizer.computeWeightedJacobian(new double[]{0.0});
        SingularValueDecomposition svd = new SingularValueDecomposition(jacobian);
        double smallestSingularValue = svd.getSingularValues()[0];
        Assert.assertTrue(Math.abs(smallestSingularValue) < 1e-12);

        // Run the optimization with QR decomposition.
        optimizer = new GaussNewtonOptimizer(true, checker);
        PointValuePair result = optimizer.optimize(problem, weightMatrix, checker, true);
        Assert.assertArrayEquals(new double[]{Math.PI / 2}, result.getPoint(), 1e-6);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
    }
}