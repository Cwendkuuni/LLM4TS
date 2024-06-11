package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

/**
 * Test class for BisectionSolver.
 */
public class BisectionSolverTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        // Check default constructor
        BisectionSolver solver = new BisectionSolver();
        assertTrue(solver instanceof BisectionSolver);
        assertEquals(100, solver.getMaxIterations());
        assertEquals(1E-6, solver.getRelativeAccuracy(), 1E-8);

        // Check constructor with arguments
        BisectionSolver solverWithArgs = new BisectionSolver(100, 1E-6);
        assertEquals(100, solverWithArgs.getMaxIterations());
        assertEquals(1E-6, solverWithArgs.getRelativeAccuracy(), 1E-8);
    }

    /**
     * Test solve method with valid function, min, max and initial guess.
     * @throws Exception
     */
    @Test
    public void testSolveValidFunction() throws Exception {
        // Prepare test data
        UnivariateRealFunction f = new Function("f(x) = x^3 - 2", x -> Math.pow(x, 3) - 2);
        double min = -2.0;
        double max = 2.0;
        double initialGuess = 0.0;
        double expectedResult = -1.0;

        // Test BisectionSolver
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, min, max, initialGuess);

        // Check test result
        assertTrue(result > -1E-6 && result < 1E-6);
        assertEquals(expectedResult, result, 1E-6);
    }

    /**
     * Test solve method with valid function, min and max only.
     * @throws Exception
     */
    @Test
    public void testSolveValidFunctionMinMax() throws Exception {
        // Prepare test data
        UnivariateRealFunction f = new Function("f(x) = x^3 - 2", x -> Math.pow(x, 3) - 2);
        double min = -2.0;
        double max = 2.0;
        double initialGuess = 0.0;
        double expectedResult = -1.0;

        // Test BisectionSolver
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, min, max);

        // Check test result
        assertTrue(result > -1E-6 && result < 1E-6);
        assertEquals(expectedResult, result, 1E-6);
    }

    /**
     * Test solve method with exception when max and min are the same.
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveMinMaxSameException() throws Exception {
        // Prepare test data
        UnivariateRealFunction f = new Function("f(x) = x^2", x -> Math.pow(x, 2));
        double min = 2.0;
        double max = 2.0;

        // Test BisectionSolver
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, min, max);
    }

    /**
     * Test solve method with exception when function evaluation fails.
     * @throws Exception
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() throws Exception {
        // Prepare test data
        UnivariateRealFunction f = new Function("f(x)", x -> { throw new UnsupportedOperationException(); });
        double min = 0.0;
        double max = 0.0;

        // Test BisectionSolver
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, min, max);
    }

    /**
     * Test solve method with exception when maximal number of iterations is exceeded.
     * @throws Exception
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceededException() throws Exception {
        // Prepare test data
        UnivariateRealFunction f = new Function("f(x) = x^3 - 2", x -> Math.pow(x, 3) - 2);
        double min = -2.0;
        double max = 2.0;
        double initialGuess = 0.0;
        BisectionSolver solver = new BisectionSolver(1);

        // Test BisectionSolver
        solver.solve(f, min, max, initialGuess);
    }

    /**
     * Test solve method with invalid function.
     * @throws Exception
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveInvalidFunction() throws Exception {
        // Prepare test data
        UnivariateRealFunction f = new Function("f", x -> { throw new UnsupportedOperationException(); });
        double min = 0.0;
        double max = 0.0;

        // Test BisectionSolver
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, min, max);
    }

    /**
     * Utility method to create a UnivariateRealFunction with a given name and lambda function.
     * @param name Name of the UnivariateRealFunction.
     * @param function Lambda function for the UnivariateRealFunction.
     * @return The created UnivariateRealFunction.
     */
    private UnivariateRealFunction newFunction(String name, UnivariateRealFunction function) {
        return new FunctionWrapper(name, function);
    }

    /**
     * Utility method to create a UnivariateRealFunction wrapping a lambda function.
     * @param name Name of the UnivariateRealFunction.
     * @param function Lambda function for the UnivariateRealFunction.
     * @return The created UnivariateRealFunction.
     */
    private UnivariateRealFunction newFunction(String name, DoubleFunction function) {
        return new FunctionWrapper(name, x -> function.apply(x));
    }

    /**
     * Utility function interface for functions that can be applied to a double.
     */
    @FunctionalInterface
    private interface DoubleFunction {
        double apply(double x);
    }

    /**
     * Utility class for wrapping a UnivariateRealFunction or a DoubleFunction as a UnivariateRealFunction.
     */
    private static class FunctionWrapper implements UnivariateRealFunction {
        private final String name;
        private final UnivariateRealFunction function;

        public FunctionWrapper(String name, UnivariateRealFunction function) {
            this.name = name;
            this.function = function;
        }

        public FunctionWrapper(String name, DoubleFunction function) {
            this.name = name;
            this.function = new FunctionAdapter(function);
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public double value(double x) throws FunctionEvaluationException {
            return function.value(x);
        }
    }

    /**
     * Utility class for adapting a DoubleFunction to a UnivariateRealFunction.
     */
    private static class FunctionAdapter implements UnivariateRealFunction {
        private final DoubleFunction function;

        public FunctionAdapter(DoubleFunction function) {
            this.function = function;
        }

        @Override
        public String getName() {
            throw new UnsupportedOperationException();
        }

        @Override
        public double value(double x) throws FunctionEvaluationException {
            return function.apply(x);
        }
    }
}