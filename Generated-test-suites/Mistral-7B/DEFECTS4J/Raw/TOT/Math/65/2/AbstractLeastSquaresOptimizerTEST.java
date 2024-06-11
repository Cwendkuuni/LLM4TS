package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnconstrainedMinimizer;
import org.apache.commons.math3.optim.nonlinear.gradient.LeastSquaresOptimizer;
import org.apache.commons.math3.optim.nonlinear.gradient.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.nonlinear.linear.GaussNewtonOptimizer;
import org.apache.commons.math3.optim.nonlinear.linear.QNOptimizer;
import org.apache.commons.math3.optim.nonlinear.linear.SVDQROptimizer;
import org.apache.commons.math3.optim.nonlinear.linear.SimpleGradientDescentOptimizer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link AbstractLeastSquaresOptimizer} abstract class.
 */
public class AbstractLeastSquaresOptimizerTest {

    /**
     * Test for the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        Assert.assertSame(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        Assert.assertSame(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getIterations());
        Assert.assertSame(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        Assert.assertSame(Integer.MAX_VALUE, optimizer.getEvaluations());
        Assert.assertSame(Integer.MAX_VALUE, optimizer.getJacobianEvaluations());
        Assert.assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test for the constructor with custom convergence checker and maximal number of iterations.
     */
    @Test
    public void testCustomConstructor() {
        VectorialConvergenceChecker customChecker = new VectorialConvergenceChecker() {
            @Override
            public boolean isConverged(double[] currentPoint, double[] currentCost, double tolerance) {
                return Math.abs(currentCost[0]) < 1e-12;
            }
        };
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialConvergenceChecker getConvergenceChecker() {
                return customChecker;
            }

            @Override
            protected int getMaxIterations() {
                return 10;
            }
        };
        Assert.assertSame(customChecker, optimizer.getConvergenceChecker());
        Assert.assertSame(10, optimizer.getMaxIterations());
    }

    /**
     * Test the optimization with a simple quadratic function.
     */
    @Test
    public void testQuadraticFunction() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        DifferentiableMultivariateVectorialFunction function = new DifferentiableMultivariateVectorialFunction() {
            @Override
            public double[] value(double[] x) throws FunctionEvaluationException {
                double result = 0;
                for (int i = 0; i < 2; i++) {
                    result += x[i] * x[i];
                }
                return new double[]{result};
            }

            @Override
            public double[] gradientValue(double[] x) throws FunctionEvaluationException {
                double[] result = new double[2];
                for (int i = 0; i < 2; i++) {
                    result[i] = 2 * x[i];
                }
                return result;
            }
        };
        double[] target = new double[]{3.0, 4.0};
        double[] weights = new double[]{1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        PointValuePair optimalPointValuePair = optimizer.optimize(function, target, weights, startPoint);
        Assert.assertEquals(3.0, optimalPointValuePair.getPoint()[0], 1e-12);
        Assert.assertEquals(4.0, optimalPointValuePair.getPoint()[1], 1e-12);
        Assert.assertEquals(3.0, optimalPointValuePair.getValue()[0], 1e-12);
    }

    /**
     * Test the optimization with a function that has no roots.
     */
    @Test(expected = OptimizationException.class)
    public void testNoRootFunction() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        DifferentiableMultivariateVectorialFunction function = new DifferentiableMultivariateVectorialFunction() {
            @Override
            public double[] value(double[] x) throws FunctionEvaluationException {
                return new double[]{Math.sin(Math.PI)};
            }

            @Override
            public double[] gradientValue(double[] x) throws FunctionEvaluationException {
                return new double[]{Math.cos(Math.PI)};
            }
        };
        double[] target = new double[]{0.0};
        double[] weights = new double[]{1.0};
        double[] startPoint = new double[]{0.0};
        AbstractLeastSquaresOptimizer optimizer = new SimpleGradientDescentOptimizer();
        optimizer.optimize(function, target, weights, startPoint);
    }

    /**
     * Test the optimization with a function that throws an exception.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testExceptionFunction() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        DifferentiableMultivariateVectorialFunction function = new DifferentiableMultivariateVectorialFunction() {
            @Override
            public double[] value(double[] x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException("Exception");
            }

            @Override
            public double[] gradientValue(double[] x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException("Exception");
            }
        };
        double[] target = new double[]{0.0};
        double[] weights = new double[]{1.0};
        double[] startPoint = new double[]{0.0};
        AbstractLeastSquaresOptimizer optimizer = new QNOptimizer();
        optimizer.optimize(function, target, weights, startPoint);
    }

    /**
     * Test the optimization with a function with a wrong number of dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWrongNumberOfDimensionsFunction() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        DifferentiableMultivariateVectorialFunction function = new DifferentiableMultivariateVectorialFunction() {
            @Override
            public double[] value(double[] x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException("Exception");
            }

            @Override
            public double[] gradientValue(double[] x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException("Exception");
            }
        };
        double[] target = new double[]{0.0};
        double[] weights = new double[]{1.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        AbstractLeastSquaresOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.optimize(function, target, weights, startPoint);
    }

    /**
     * Test the optimization with a function with a wrong number of target values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWrongNumberOfTargetValues() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        DifferentiableMultivariateVectorialFunction function = new DifferentiableMultivariateVectorialFunction() {
            @Override
            public double[] value(double[] x) throws FunctionEvaluationException {
                return new double[]{0.0};
            }

            @Override
            public double[] gradientValue(double[] x) throws FunctionEvaluationException {
                return new double[]{0.0};
            }
        };
        double[] target = new double[]{0.0, 1.0};
        double[] weights = new double[]{1.0};
        double[] startPoint = new double[]{0.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.optimize(function, target, weights, startPoint);
    }

    /**
     * Test the optimization with a function with a wrong number of weights.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWrongNumberOfWeights() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        DifferentiableMultivariateVectorialFunction function = new DifferentiableMultivariateVectorialFunction() {
            @Override
            public double[] value(double[] x) throws FunctionEvaluationException {
                return new double[]{0.0};
            }

            @Override
            public double[] gradientValue(double[] x) throws FunctionEvaluationException {
                return new double[]{0.0};
            }
        };
        double[] target = new double[]{0.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        double[] startPoint = new double[]{0.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.optimize(function, target, weights, startPoint);
    }

    /**
     * Test the optimization with a function with a wrong number of initial points.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWrongNumberOfInitialPoints() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        DifferentiableMultivariateVectorialFunction function = new DifferentiableMultivariateVectorialFunction() {
            @Override
            public double[] value(double[] x) throws FunctionEvaluationException {
                return new double[]{0.0};
            }

            @Override
            public double[] gradientValue(double[] x) throws FunctionEvaluationException {
                return new double[]{0.0};
            }
        };
        double[] target = new double[]{0.0};
        double[] weights = new double[]{1.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0, 0.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.optimize(function, target, weights, startPoint);
    }

    /**
     * Test the optimization with a function that is not differentiable.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testUndifferentiablenFunction() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        DifferentiableMultivariateVectorialFunction function = new DifferentiableMultivariateVectorialFunction() {
            @Override
            public double[] value(double[] x) throws FunctionEvaluationException {
                return new double[]{Math.sqrt(Math.abs(x[0]))};
            }

            @Override
            public double[] gradientValue(double[] x) throws FunctionEvaluationException {
                throw new UnsupportedOperationException("Function is not differentiable");
            }
        };
        double[] target = new double[]{0.0};
        double[] weights = new double[]{1.0};
        double[] startPoint = new double[]{1.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        optimizer.optimize(function, target, weights, startPoint);
    }
}