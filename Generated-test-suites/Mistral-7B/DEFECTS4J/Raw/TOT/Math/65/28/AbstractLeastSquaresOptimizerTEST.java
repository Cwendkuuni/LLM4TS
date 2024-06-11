package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoSolutionException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DRowAccessor;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnconstrainedMinimizer;
import org.junit.Assert;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testLevenbergMarquardtOptimizer() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();

        // Test with a simple 2D problem
        double[][] data = {
                {1.0, 1.0},
                {1.5, -0.5},
                {2.0, 0.5}
        };
        RealVector x = new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}}, 1, 2).transpose();
        RealVector y = new Array2DRowRealMatrix(data, 3, 2).transpose();
        UnconstrainedMinimizer minimizer = new LevenbergMarquardtOptimizer();
        PointValuePair solution = minimizer.optimize(new QuadraticFunction(2), y.toArray(), x.toArray());

        // Check solution
        RealVector expected = new Array2DRowRealMatrix(new double[][]{{-0.0797, 0.997}}, 1, 2).transpose();
        Assert.assertArrayEquals(expected.toArray(), solution.getPoint().toArray(), 0.001);
        Assert.assertEquals(1.1945e-11, solution.getValue(), 1.0e-11);

        // Test with a ill-conditioned problem
        double[][] data2 = {
                {1.0, 1.0, 1.0},
                {1.5, -0.5, 2.0},
                {2.0, 0.5, 3.0},
                {3.5, 1.5, 4.5}
        };
        RealVector x2 = new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0, 1.0}}, 1, 3).transpose();
        RealVector y2 = new Array2DRowRealMatrix(data2, 4, 3).transpose();
        UnconstrainedMinimizer minimizer2 = new LevenbergMarquardtOptimizer();
        try {
            PointValuePair solution2 = minimizer2.optimize(new QuadraticFunction(3), y2.toArray(), x2.toArray());
            Assert.fail();
        } catch (SingularMatrixException e) {
            // expected
        }

        // Test with a non-convex problem
        double[][] data3 = {
                {1.0, 1.0},
                {1.5, -0.5},
                {2.0, 0.5},
                {3.0, -1.0}
        };
        RealVector x3 = new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}}, 1, 2).transpose();
        RealVector y3 = new Array2DRowRealMatrix(data3, 4, 2).transpose();
        UnconstrainedMinimizer minimizer3 = new LevenbergMarquardtOptimizer();
        try {
            PointValuePair solution3 = minimizer3.optimize(new QuadraticFunction(2), y3.toArray(), x3.toArray());
            Assert.fail();
        } catch (NoSolutionException e) {
            // expected
        }

        // Test with a stopping condition
        double[][] data4 = {
                {1.0, 1.0},
                {1.5, -0.5},
                {2.0, 0.5},
                {3.0, -1.5}
        };
        RealVector x4 = new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}}, 1, 2).transpose();
        RealVector y4 = new Array2DRowRealMatrix(data4, 4, 2).transpose();
        UnconstrainedMinimizer minimizer4 = new LevenbergMarquardtOptimizer();
        minimizer4.setMaxIterations(100);
        PointValuePair solution4 = minimizer4.optimize(new QuadraticFunction(2), y4.toArray(), x4.toArray());
        RealVector expected4 = new Array2DRowRealMatrix(new double[][]{{-0.1556, 0.995}}, 1, 2).transpose();
        Assert.assertArrayEquals(expected4.toArray(), solution4.getPoint().toArray(), 0.001);
        Assert.assertEquals(1.7820e-08, solution4.getValue(), 1.0e-08);
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testLevenbergMarquardtOptimizerMaxEval() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        double[][] data = {
                {1.0, 1.0},
                {1.5, -0.5},
                {2.0, 0.5},
                {3.0, -1.5},
                {4.0, -2.5},
                {5.0, -3.5},
                {6.0, -4.5},
                {7.0, -5.5},
                {8.0, -6.5},
                {9.0, -7.5},
                {10.0, -8.5}
        };
        RealVector x = new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}}, 1, 2).transpose();
        RealVector y = new Array2DRowRealMatrix(data, 11, 2).transpose();
        optimizer.setMaxEvaluations(10);
        optimizer.optimize(new QuadraticFunction(2), y.toArray(), x.toArray());
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testLevenbergMarquardtOptimizerMaxIter() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        double[][] data = {
                {1.0, 1.0},
                {1.5, -0.5},
                {2.0, 0.5},
                {3.0, -1.5},
                {4.0, -2.5},
                {5.0, -3.5},
                {6.0, -4.5},
                {7.0, -5.5},
                {8.0, -6.5},
                {9.0, -7.5},
                {10.0, -8.5}
        };
        RealVector x = new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}}, 1, 2).transpose();
        RealVector y = new Array2DRowRealMatrix(data, 11, 2).transpose();
        optimizer.setMaxIterations(1);
        optimizer.optimize(new QuadraticFunction(2), y.toArray(), x.toArray());
    }

}

class QuadraticFunction implements DifferentiableMultivariateVectorialFunction {

    private int dim;

    public QuadraticFunction(int dimension) {
        dim = dimension;
    }

    @Override
    public double value(double[] x) throws FunctionEvaluationException {
        double sum = 0.0;
        for (int i = 0; i < dim; i++) {
            sum += x[i] * x[i];
        }
        return sum;
    }

    @Override
    public double[] gradient(double[] x) throws FunctionEvaluationException {
        double[] grad = new double[dim];
        for (int i = 0; i < dim; i++) {
            grad[i] = 2 * x[i];
        }
        return grad;
    }

    @Override
    public MultivariateMatrixFunction jacobian() throws FunctionEvaluationException {
        return new MultivariateMatrixFunction() {
            @Override
            public double[][] value(double[] x) throws FunctionEvaluationException {
                double[][] jacobian = new double[dim][dim];
                for (int i = 0; i < dim; i++) {
                    for (int j = 0; j < dim; j++) {
                        jacobian[i][j] = 2 * x[i] * (i == j ? 1 : 0);
                    }
                }
                return jacobian;
            }
        };
    }

    @Override
    public String toString() {
        return "Quadratic Function";
    }

}