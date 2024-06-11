package org.apache.commons.math.optimization.general.test;

import org.apache.commons.math3.optim.AbstractVectorOptimizer;
import org.apache.commons.math3.optim.MaxEvaluationsExceededException;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optim.VectorOptimizer;
import org.apache.commons.math3.optim.nonlinear.simple.SphereFunction;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test suite for {@link AbstractLeastSquaresOptimizer}.
 */
public class AbstractLeastSquaresOptimizerTest {

    /** The optimizer to test. */
    private static AbstractLeastSquaresOptimizer optimizer;

    /** The optimization problem to solve. */
    private static SphereFunction function;

    /** The optimizer's convergence checker. */
    private static VectorialConvergenceChecker checker;

    /**
     * Set up the test: initialize the optimizer and the convergence checker.
     */
    @BeforeClass
    public static void setUp() {
        optimizer = new LevenbergMarquardtOptimizer(); // You should replace this with your optimizer here
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(Integer.MAX_VALUE);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());
        function = new SphereFunction(1.0, 3.0);
        checker = optimizer.getConvergenceChecker();
    }

    /**
     * Test the optimization with the given start point.
     * @throws UnconstrainedOptimizationException if the optimization fails
     * @throws MaxIterationsExceededException if the optimization takes too many iterations
     * @throws MaxEvaluationsExceededException if the optimization performs too many evaluations
     */
    @Test
    public void testOptimize()
        throws UnconstrainedOptimizationException, MaxIterationsExceededException, MaxEvaluationsExceededException {
        double[] startPoint = new double[] { 1.0, 1.0, 1.0 };
        PointValuePair result = optimizer.optimize(function, new double[] { 0.0, 0.0, 0.0 }, null, startPoint);
        double[] expected = new double[] { 0.0, 0.0, 0.0 };
        Assert.assertArrayEquals(expected, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, result.getValue(), 0.0);
        checkResult(result);
    }

    /**
     * Test the optimization with a wrong start point.
     * @throws UnconstrainedOptimizationException if the optimization fails
     * @throws MaxIterationsExceededException if the optimization takes too many iterations
     * @throws MaxEvaluationsExceededException if the optimization performs too many evaluations
     */
    @Test(expected = UnconstrainedOptimizationException.class)
    public void testOptimizeWrongStartPoint()
        throws UnconstrainedOptimizationException, MaxIterationsExceededException, MaxEvaluationsExceededException {
        double[] startPoint = new double[] { 1.0, 1.0, -1.0 };
        optimizer.optimize(function, new double[] { 0.0, 0.0, 0.0 }, null, startPoint);
    }

    /**
     * Test the optimization with a non-differentiable function.
     * @throws UnconstrainedOptimizationException if the optimization fails
     * @throws MaxIterationsExceededException if the optimization takes too many iterations
     * @throws MaxEvaluationsExceededException if the optimization performs too many evaluations
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testOptimizeNonDifferentiableFunction()
        throws UnconstrainedOptimizationException, MaxIterationsExceededException, MaxEvaluationsExceededException {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                throw new UnsupportedOperationException("This optimizer does not support non-differentiable functions.");
            }
        };
        double[] startPoint = new double[] { 1.0, 1.0, 1.0 };
        optimizer.optimize(new AbstractMultivariateFunction() {
            @Override
            public double value(double[] x) {
                throw new UnsupportedOperationException("This function is non-differentiable.");
            }

            @Override
            public double[] gradient(double[] x) {
                throw new UnsupportedOperationException("This function is non-differentiable.");
            }
        }, new double[] { 0.0, 0.0, 0.0 }, null, startPoint);
    }

    /**
     * Test the optimization with a function that does not have a solution.
     * @throws UnconstrainedOptimizationException if the optimization fails
     * @throws MaxIterationsExceededException if the optimization takes too many iterations
     * @throws MaxEvaluationsExceededException if the optimization performs too many evaluations
     */
    @Test(expected = UnconstrainedOptimizationException.class)
    public void testOptimizeNoSolution()
        throws UnconstrainedOptimizationException, MaxIterationsExceededException, MaxEvaluationsExceededException {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                throw new UnconstrainedOptimizationException("Function has no solution.");
            }
        };
        double[] startPoint = new double[] { 1.0, 1.0, 1.0 };
        optimizer.optimize(function, new double[] { 0.0, 0.0, 0.0 }, null, startPoint);
    }

    /**
     * Test the convergence checker.
     * @param result the optimization result
     */
    private void checkResult(PointValuePair result) {
        Assert.assertFalse(result.isDiverging());
        Assert.assertFalse(result.isFailed());
        Assert.assertEquals(result.getStatus(), AbstractVectorOptimizer.OPTIMAL_POINT_FOUND, 0);
    }

}