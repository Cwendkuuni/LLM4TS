package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.optim.UnboundedOptimizationException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link AbstractLeastSquaresOptimizer}.
 */
public class AbstractLeastSquaresOptimizerTest {

    /**
     * Test the default constructor with default settings.
     */
    @Test
    public void testDefaultConstructorWithDefaultSettings() {
        final AbstractLeastSquaresOptimizer testee = new AbstractLeastSquaresOptimizer();
        assertEquals(100, testee.getMaxIterations());
        assertEquals(0.0, testee.getCost(), 0.0);
    }

    /**
     * Test the constructor with custom settings.
     */
    @Test
    public void testCustomConstructor() {
        final int maxIterations = 5;
        final AbstractLeastSquaresOptimizer testee = new AbstractLeastSquaresOptimizer() {
            @Override
            protected int getMaxIterations() {
                return maxIterations;
            }
        };
        assertEquals(maxIterations, testee.getMaxIterations());
    }

    /**
     * Test the optimization algorithm for a simple linear regression problem.
     */
    @Test
    public void testOptimizeSimpleLinearRegression()
            throws FunctionEvaluationException, OptimizationException {

        final DifferentiableMultivariateVectorialFunction function =
                new DifferentiableMultivariateVectorialFunction() {
                    @Override
                    public double[] value(double[] point)
                            throws FunctionEvaluationException {
                        final double[] x = point;
                        return new double[]{x[0] * x[1] - 2.0 * x[0] + 3.0};
                    }

                    @Override
                    public double[] jacobianValue(double[] point)
                            throws FunctionEvaluationException {
                        final double[] x = point;
                        return new double[][]{
                            {x[0], x[1]},
                            {x[0], 0.0},
                            {0.0, 1.0}
                        };
                    }
                };

        final double[] target = new double[]{1.0, 2.0, 3.0};
        final double[] weights = new double[]{1.0, 1.0, 1.0};
        final double[] startPoint = new double[]{0.0, 0.0};

        final PointValuePair result =
                new AbstractLeastSquaresOptimizer().optimize(
                        function, target, weights, startPoint);

        final double[] expectedPoint = new double[]{1.5, 1.5};
        assertArrayEquals(expectedPoint, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);

    }

    /**
     * Test the optimization algorithm for a complex non-linear regression problem.
     */
    @Test
    public void testOptimizeComplexNonLinearRegression()
            throws FunctionEvaluationException, OptimizationException {

        final DifferentiableMultivariateVectorialFunction function =
                new DifferentiableMultivariateVectorialFunction() {
                    @Override
                    public double[] value(double[] point)
                            throws FunctionEvaluationException {
                        final double[] x = point;
                        final double sum = x[0] * x[0] + x[1] * x[1];
                        return new double[]{Math.exp(sum) - sum - 1.0};
                    }

                    @Override
                    public double[] jacobianValue(double[] point)
                            throws FunctionEvaluationException {
                        final double[] x = point;
                        final double sum = x[0] * x[0] + x[1] * x[1];
                        return new double[][]{
                            {2.0 * x[0], 2.0 * x[1]},
                            {2.0 * x[0], 2.0 * x[1]}
                        };
                    }
                };

        final double[] target = new double[]{1.0, 1.0, 1.0};
        final double[] weights = new double[]{1.0, 1.0, 1.0};
        final double[] startPoint = new double[]{1.0, 1.0};

        final PointValuePair result =
                new AbstractLeastSquaresOptimizer().optimize(
                        function, target, weights, startPoint);

        final double[] expectedPoint = new double[]{0.5, 0.5};
        assertArrayEquals(expectedPoint, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);

    }

    /**
     * Test the optimization algorithm with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeInvalidInput() {
        final DifferentiableMultivariateVectorialFunction function =
                new DifferentiableMultivariateVectorialFunction() {};
        final double[] target = new double[]{1.0};
        final double[] weights = new double[]{1.0};
        final double[] startPoint = new double[]{1.0, 1.0};
        new AbstractLeastSquaresOptimizer().optimize(function, target, weights, startPoint);
    }

    /**
     * Test the optimization algorithm with incorrect method signatures.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeIncorrectMethodSignatures()
            throws FunctionEvaluationException, OptimizationException {

        final DifferentiableMultivariateVectorialFunction function =
                new DifferentiableMultivariateVectorialFunction() {
                    @Override
                    public double value(double[] point)
                            throws FunctionEvaluationException {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public double[] jacobianValue(double[] point)
                            throws FunctionEvaluationException {
                        return new double[0][0];
                    }
                };

        final double[] target = new double[]{1.0, 2.0, 3.0};
        final double[] weights = new double[]{1.0, 1.0, 1.0};
        final double[] startPoint = new double[]{0.0, 0.0};

        new AbstractLeastSquaresOptimizer().optimize(function, target, weights, startPoint);

    }

    /**
     * Test the optimization algorithm with a function that doesn't meet the
     * requirements.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeFunctionThatDoesntMeetRequirements()
            throws FunctionEvaluationException, OptimizationException {

        final DifferentiableMultivariateVectorialFunction function =
                new DifferentiableMultivariateVectorialFunction() {
                    @Override
                    public double[] value(double[] point)
                            throws FunctionEvaluationException {
                        throw new MaxIterationsExceededException(1);
                    }

                    @Override
                    public double[] jacobianValue(double[] point)
                            throws FunctionEvaluationException {
                        throw new MaxEvaluationsExceededException(1);
                    }
                };

        final double[] target = new double[]{1.0, 2.0, 3.0};
        final double[] weights = new double[]{1.0, 1.0, 1.0};
        final double[] startPoint = new double[]{0.0, 0.0};

        new AbstractLeastSquaresOptimizer().optimize(function, target, weights, startPoint);

    }

    /**
     * Test the optimization algorithm with a convergence checker that doesn't
     * pass.
     */
    @Test(expected = OptimizationException.class)
    public void testOptimizeConvergenceCheckerThatDoesntPass()
            throws FunctionEvaluationException, OptimizationException {

        final DifferentiableMultivariateVectorialFunction function =
                new DifferentiableMultivariateVectorialFunction() {
                    @Override
                    public double[] value(double[] point)
                            throws FunctionEvaluationException {
                        return new double[]{0.0};
                    }

                    @Override
                    public double[] jacobianValue(double[] point)
                            throws FunctionEvaluationException {
                        return new double[0][0];
                    }
                };

        final VectorialConvergenceChecker checker =
                new VectorialConvergenceChecker() {
                    @Override
                    public boolean isConverged(double[] previousPoint, double[] currentPoint,
                                              double currentValue, double previousValue) {
                        return false;
                    }
                };

        final AbstractLeastSquaresOptimizer testee =
                new AbstractLeastSquaresOptimizer() {
                    @Override
                    protected VectorialConvergenceChecker getConvergenceChecker() {
                        return checker;
                    }
                };

        final double[] target = new double[]{1.0, 2.0, 3.0};
        final double[] weights = new double[]{1.0, 1.0, 1.0};
        final double[] startPoint = new double[]{0.0, 0.0};

        testee.optimize(function, target, weights, startPoint);

    }

}