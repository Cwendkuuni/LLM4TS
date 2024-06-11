package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link GaussNewtonOptimizer}.
 */
public class GaussNewtonOptimizerTest {

    /**
     * Test the optimization of a simple quadratic function using LU decomposition
     * and a simple checker.
     */
    @Test
    public void testGaussNewtonOptimizerLUDecomposition() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        OptimizableFunction function = new QuadraticFunction(new double[]{-1, 0}, new double[]{-1, 1}, 1);
        double[] startPoint = new double[]{3, 3};
        PointValuePair result = optimizer.optimize(function, startPoint);
        Assert.assertEquals(new double[]{1, 1}, result.getPoint(), 1e-8);
        Assert.assertEquals(0.0, result.getValue(), 1e-8);
    }

    /**
     * Test the optimization of a simple quadratic function using QR decomposition
     * and a simple checker.
     */
    @Test
    public void testGaussNewtonOptimizerQRDecomposition() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        OptimizableFunction function = new QuadraticFunction(new double[]{-1, 0}, new double[]{-1, 1}, 1);
        double[] startPoint = new double[]{3, 3};
        PointValuePair result = optimizer.optimize(function, startPoint);
        Assert.assertEquals(new double[]{1, 1}, result.getPoint(), 1e-8);
        Assert.assertEquals(0.0, result.getValue(), 1e-8);
    }

    /**
     * Test the optimization of a non-quadratic function using LU decomposition
     * and a simple checker, which should fail due to divergence.
     */
    @Test(expected = ConvergenceException.class)
    public void testGaussNewtonOptimizerLUDivergence() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        OptimizableFunction function = new NonQuadraticFunction();
        double[] startPoint = new double[]{0, 0};
        PointValuePair result = optimizer.optimize(function, startPoint);
        // The optimization should not converge, so an exception should be thrown.
    }

    /**
     * Test the optimization of a non-quadratic function using QR decomposition
     * and a simple checker, which should fail due to divergence.
     */
    @Test(expected = ConvergenceException.class)
    public void testGaussNewtonOptimizerQRDivergence() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        OptimizableFunction function = new NonQuadraticFunction();
        double[] startPoint = new double[]{0, 0};
        PointValuePair result = optimizer.optimize(function, startPoint);
        // The optimization should not converge, so an exception should be thrown.
    }

    /**
     * Test the optimization of a quadratic function using an uninitialized
     * optimizer, which should fail due to a null pointer exception.
     */
    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizerUninitialized() {
        OptimizableFunction function = new QuadraticFunction(new double[]{-1, 0}, new double[]{-1, 1}, 1);
        double[] startPoint = new double[]{3, 3};
        GaussNewtonOptimizer optimizer = null;
        PointValuePair result = optimizer.optimize(function, startPoint);
        // The optimizer should not be initialized, so a null pointer exception
        // should be thrown.
    }

    /**
     * Test the optimization of a quadratic function using a non-initialized
     * convergence checker, which should fail due to a null pointer exception.
     */
    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizerUninitializedChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        OptimizableFunction function = new QuadraticFunction(new double[]{-1, 0}, new double[]{-1, 1}, 1);
        double[] startPoint = new double[]{3, 3};
        ConvergenceChecker<PointValuePair> checker = null;
        PointValuePair result = optimizer.optimize(function, startPoint, checker);
        // The convergence checker should not be initialized, so a null pointer
        // exception should be thrown.
    }

    /**
     * Test the optimization of a quadratic function with a weight matrix.
     */
    @Test
    public void testGaussNewtonOptimizerWeighted() {
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
                {1, 0},
                {0, 10}
        });
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        OptimizableFunction function = new QuadraticFunction(new double[]{-1, 0}, new double[]{-1, 1}, 1, weightMatrix);
        double[] startPoint = new double[]{3, 3};
        PointValuePair result = optimizer.optimize(function, startPoint);
        Assert.assertEquals(new double[]{1, 1/5}, result.getPoint(), 1e-8);
        Assert.assertEquals(0.0, result.getValue(), 1e-8);
    }

    /**
     * A quadratic function.
     */
    static class QuadraticFunction implements OptimizableFunction {

        /** The target values. */
        private final double[] targetValues;

        /** The Jacobian matrix. */
        private final RealMatrix jacobian;

        /** The cost function. */
        private final double cost;

        /**
         * Constructor for a quadratic function.
         *
         * @param startPoint The initial point.
         * @param target     The target values.
         * @param cost        The cost function.
         */
        QuadraticFunction(double[] startPoint, double[] target, double cost) {
            this.targetValues = target;
            BlockRealMatrix jacobian = new BlockRealMatrix(new double[target.length][2]);
            for (int i = 0; i < target.length; i++) {
                double[] jacobianRow = jacobian.getRow(i);
                jacobianRow[0] = 2 * startPoint[i];
                jacobianRow[1] = 2 * startPoint[i + 1];
            }
            this.jacobian = jacobian;
            this.cost = cost;
        }

        /**
         * Constructor for a quadratic function with a weight matrix.
         *
         * @param startPoint      The initial point.
         * @param target          The target values.
         * @param cost            The cost function.
         * @param weightMatrix    The weight matrix.
         */
        QuadraticFunction(double[] startPoint, double[] target, double cost, RealMatrix weightMatrix) {
            this.targetValues = target;
            BlockRealMatrix jacobian = new BlockRealMatrix(new double[target.length][2]);
            for (int i = 0; i < target.length; i++) {
                double[] jacobianRow = jacobian.getRow(i);
                jacobianRow[0] = 2 * startPoint[i];
                jacobianRow[1] = 2 * startPoint[i + 1];
                jacobianRow[0] *= weightMatrix.getEntry(i, i);
                jacobianRow[1] *= weightMatrix.getEntry(i, i);
            }
            this.jacobian = jacobian;
            this.cost = cost;
        }

        @Override
        public double value(double[] point) {
            double result = 0.0;
            for (int i = 0; i < targetValues.length; i++) {
                double error = targetValues[i] - point[i] - point[i + 1] * point[i + 1];
                result += error * error;
            }
            result += this.cost;
            return result;
        }

        @Override
        public RealMatrix gradient(double[] point) {
            return jacobian;
        }

        @Override
        public double[] getTargetValues() {
            return targetValues;
        }

        @Override
        public double getCost() {
            return cost;
        }
    }

    /**
     * A non-quadratic function.
     */
    static class NonQuadraticFunction implements OptimizableFunction {

        @Override
        public double value(double[] point) {
            return Math.pow(Math.sin(point[0]), 2) + Math.pow(Math.sin(point[1]), 2);
        }

        @Override
        public RealMatrix gradient(double[] point) {
            RealMatrix jacobian = new BlockRealMatrix(new double[][]{
                    {-2 * Math.cos(point[0]) * Math.sin(point[0]), 0},
                    {0, -2 * Math.cos(point[1]) * Math.sin(point[1])}
            });
            return jacobian;
        }

        @Override
        public double[] getTargetValues() {
            return new double[0];
        }

        @Override
        public double getCost() {
            return 0.0;
        }
    }

}