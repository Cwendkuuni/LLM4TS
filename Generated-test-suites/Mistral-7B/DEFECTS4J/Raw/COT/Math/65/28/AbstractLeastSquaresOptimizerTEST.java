package org.apache.commons.math.optimization.general; // Import necessary packages

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.vector.linear.*;
import org.apache.commons.math3.optim.points.PointValuePair;
import org.apache.commons.math3.optim.test.DummyVectorFunction;
import org.junit.Test;

/**
 * Test class for AbstractLeastSquaresOptimizer.
 */
public class AbstractLeastSquaresOptimizerTest {

    /**
     * Test the constructor with default settings.
     */
    @Test
    public void testConstructorWithDefaultSettings() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertNotNull(optimizer);
        assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        assertEquals(new SimpleVectorialValueChecker(), optimizer.getConvergenceChecker());
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
    }

    /**
     * Test the setMaxIterations method.
     */
    @Test
    public void testSetMaxIterations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(5);
        assertEquals(5, optimizer.getMaxIterations());
    }

    /**
     * Test the getIterations method.
     */
    @Test
    public void testGetIterations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertNull(optimizer.getIterations());

        optimizer.incrementIterationsCounter();
        optimizer.incrementIterationsCounter();
        assertEquals(2, optimizer.getIterations());
    }

    /**
     * Test the setMaxEvaluations method.
     */
    @Test
    public void testSetMaxEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxEvaluations(3);
        assertEquals(3, optimizer.getMaxEvaluations());
    }

    /**
     * Test the getMaxEvaluations method.
     */
    @Test
    public void testGetMaxEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertNull(optimizer.getMaxEvaluations());

        optimizer.incrementObjectiveEvaluations();
        optimizer.incrementObjectiveEvaluations();
        optimizer.incrementJacobianEvaluations();
        optimizer.incrementJacobianEvaluations();
        assertEquals(4, optimizer.getMaxEvaluations());
    }

    /**
     * Test the setConvergenceChecker method.
     */
    @Test
    public void testSetConvergenceChecker() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        VectorialConvergenceChecker newChecker = new SimpleVectorialValueChecker();
        optimizer.setConvergenceChecker(newChecker);
        assertEquals(newChecker, optimizer.getConvergenceChecker());
    }

    /**
     * Test the incrementIterationsCounter method.
     * @throws OptimizationException if the maximal number of iterations is exceeded
     */
    @Test(expected = OptimizationException.class)
    public void testIncrementIterationsCounterWithMaxIterationsExceeded()
            throws OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(1);
        optimizer.incrementIterationsCounter();
    }

    /**
     * Test the updateJacobian method.
     * @throws FunctionEvaluationException if the function jacobian cannot be evaluated
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testUpdateJacobianWithFunctionEvaluationException()
            throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        OptimizationTestFunctions functions = new OptimizationTestFunctions();
        DifferentiableMultivariateVectorialFunction function = functions.FIVE_HILBERT;
        optimizer.setFunction(function);
        optimizer.updateJacobian();
    }

    /**
     * Test the updateResidualsAndCost method.
     * @throws FunctionEvaluationException if the function cannot be evaluated
     * or its dimension doesn't match problem dimension or maximal number of
     * of evaluations is exceeded
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testUpdateResidualsAndCostWithFunctionEvaluationException()
            throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        OptimizationTestFunctions functions = new OptimizationTestFunctions();
        DifferentiableMultivariateVectorialFunction function = functions.FIVE_HILBERT;
        optimizer.setFunction(function);
        optimizer.setMaxEvaluations(0);
        optimizer.updateResidualsAndCost();
    }

    /**
     * Test the getRMS method.
     */
    @Test
    public void testGetRMS() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        OptimizationTestFunctions functions = new OptimizationTestFunctions();
        DifferentiableMultivariateVectorialFunction function = functions.SIX_HILBERT;
        double[] target = new double[]{1.0, 1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        optimizer.optimize(function, target, weights, startPoint);
        double rms = optimizer.getRMS();
        double expectedRMS = Math.sqrt(3.0 / 3.0);
        assertEquals(expectedRMS, rms, 1e-6);
    }

    /**
     * Test the getChiSquare method.
     */
    @Test
    public void testGetChiSquare() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        OptimizationTestFunctions functions = new OptimizationTestFunctions();
        DifferentiableMultivariateVectorialFunction function = functions.SIX_HILBERT;
        double[] target = new double[]{1.0, 1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        optimizer.optimize(function, target, weights, startPoint);
        double chiSquare = optimizer.getChiSquare();
        double expectedChiSquare = 3.0;
        assertEquals(expectedChiSquare, chiSquare, 1e-6);
    }

    /**
     * Test the getCovariances method.
     * @throws FunctionEvaluationException if the function jacobian cannot be evaluated
     * @throws OptimizationException if the covariance matrix cannot be computed
     */
    @Test
    public void testGetCovariances()
            throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        OptimizationTestFunctions functions = new OptimizationTestFunctions();
        DifferentiableMultivariateVectorialFunction function = functions.SIX_HILBERT;
        double[] target = new double[]{1.0, 1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        optimizer.optimize(function, target, weights, startPoint);
        double[][] covariances = optimizer.getCovariances();
        double[][] expectedCovariances = {{0.003926996209425, 0.0, 0.0},
                {0.0, 0.003926996209425, 0.0},
                {0.0, 0.0, 0.003926996209425}};
        assertArrayEquals(expectedCovariances, covariances, 1e-6);
    }

    /**
     * Test the guessParametersErrors method.
     * @throws FunctionEvaluationException if the function jacobian cannot be evaluated
     * @throws OptimizationException if the covariances matrix cannot be computed
     */
    @Test
    public void testGuessParametersErrors()
            throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        OptimizationTestFunctions functions = new OptimizationTestFunctions();
        DifferentiableMultivariateVectorialFunction function = functions.SIX_HILBERT;
        double[] target = new double[]{1.0, 1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        optimizer.optimize(function, target, weights, startPoint);
        double[] errors = optimizer.guessParametersErrors();
        double[] expectedErrors = {0.123846124273916, 0.123846124273916, 0.123846124273916};
        assertArrayEquals(expectedErrors, errors, 1e-6);
    }

    /**
     * Test the optimize method.
     * @throws FunctionEvaluationException if the objective function throws one during
     * the search
     * @throws OptimizationException if the algorithm failed to converge
     * @throws IllegalArgumentException if the start point dimension is wrong
     */
    @Test
    public void testOptimize()
            throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        OptimizationTestFunctions functions = new OptimizationTestFunctions();
        DifferentiableMultivariateVectorialFunction function = functions.FIVE_HILBERT;
        double[] target = new double[]{1.0, 1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        PointValuePair result = optimizer.optimize(function, target, weights, startPoint);
        double[] expectedResult = {1.0, 1.0, 1.0};
        assertArrayEquals(expectedResult, result.getPoint(), 1e-6);
        double expectedValue = 0.0;
        assertEquals(expectedValue, result.getValue(), 1e-6);
    }

    /**
     * Test the doOptimize method (should be abstract and not callable).
     * @throws FunctionEvaluationException if the objective function throws one during
     * the search
     * @throws OptimizationException if the algorithm failed to converge
     * @throws IllegalArgumentException if the start point dimension is wrong
     */
    @Test(expected = AbstractMethodError.class)
    public void testDoOptimize()
            throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return new PointValuePair(new double[]{0.0}, 0.0);
            }
        };
        OptimizationTestFunctions functions = new OptimizationTestFunctions();
        DifferentiableMultivariateVectorialFunction function = functions.FIVE_HILBERT;
        double[] target = new double[]{1.0, 1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        optimizer.optimize(function, target, weights, startPoint);
    }

    /**
     * Test the optimize method with a function that always returns an empty vector.
     * @throws FunctionEvaluationException if the objective function throws one during
     * the search
     * @throws OptimizationException if the algorithm failed to converge
     * @throws IllegalArgumentException if the start point dimension is wrong
     */
    @Test
    public void testOptimizeWithEmptyFunction()
            throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        OptimizationTestFunctions functions = new OptimizationTestFunctions();
        DifferentiableMultivariateVectorialFunction function = new DummyVectorFunction(new double[0]);
        double[] target = new double[]{1.0, 1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};

        OptimizationProblem problem = new OptimizationProblem(
                new LinearConstrainedNonLinearFunction(GoalType.MINIMIZE, function),
                null);
        Optimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.optimize(problem, startPoint);

        assertNull(optimizer.getIterations());
        assertEquals(0.0, optimizer.getCost(), 1e-6);
    }

}