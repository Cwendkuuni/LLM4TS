package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.solvers.UnivariateRealSolver;
import org.apache.commons.math3.solvers.UnivariateRealSolverFactory;

import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;

/**
 * Test class for UnivariateRealSolverUtils.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * with default solver.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testSolveDefaultSolver() throws Exception {
        UnivariateRealFunction function = new MyFunction();
        double x0 = 1.0;
        double x1 = 2.0;
        double expectedResult = 1.5;
        
        double actualResult = UnivariateRealSolverUtils.solve(function, x0, x1);
        
        assertEquals(expectedResult, actualResult, 0.001);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * with default solver and specified accuracy.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testSolveDefaultSolverWithAccuracy() throws Exception {
        UnivariateRealFunction function = new MyFunction();
        double x0 = 1.0;
        double x1 = 2.0;
        double absoluteAccuracy = 0.001;
        double expectedResult = 1.5;

        double actualResult = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);

        assertEquals(expectedResult, actualResult, absoluteAccuracy);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)}
     * with default solver and maximum iterations.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testBracketDefaultSolverMaxIterations() throws Exception {
        UnivariateRealFunction function = new MyFunction();
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        double[] expectedResult = new double[]{0.5, 1.5};

        double[] actualResult = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);

        assertArrayEquals(expectedResult, actualResult, 0.001);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with default solver and specified maximum iterations.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testBracketDefaultSolverSpecifiedMaxIterations() throws Exception {
        UnivariateRealFunction function = new MyFunction();
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maximumIterations = 10;
        double[] expectedResult = new double[]{0.5, 1.5};

        double[] actualResult = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);

        assertArrayEquals(expectedResult, actualResult, 0.001);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * with custom solver.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testSolveCustomSolver() throws Exception {
        UnivariateRealFunction function = new MyFunction();
        double x0 = 1.0;
        double x1 = 2.0;
        UnivariateRealSolver solver = new BisectionSolver();

        double expectedResult = 1.5;

        double actualResult = UnivariateRealSolverUtils.solve(function, x0, x1, solver);

        assertEquals(expectedResult, actualResult, 0.001);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)}
     * with custom solver and maximum iterations.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testBracketCustomSolverMaxIterations() throws Exception {
        UnivariateRealFunction function = new MyFunction();
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        UnivariateRealSolver solver = new BisectionSolver();
        int maximumIterations = 10;
        double[] expectedResult = new double[]{0.5, 1.5};

        double[] actualResult = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, solver, maximumIterations);

        assertArrayEquals(expectedResult, actualResult, 0.001);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * with function throwing {@link FunctionEvaluationException}.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testSolveFunctionEvaluationException() throws Exception {
        UnivariateRealFunction function = new ThrowingFunction();
        double x0 = 1.0;
        double x1 = 2.0;

        assertThrows(FunctionEvaluationException.class, () -> {
            UnivariateRealSolverUtils.solve(function, x0, x1);
        });
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * with function throwing {@link ConvergenceException}.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testSolveConvergenceException() throws Exception {
        UnivariateRealFunction function = new ThrowingConvergenceExceptionFunction();
        double x0 = 1.0;
        double x1 = 2.0;

        assertThrows(ConvergenceException.class, () -> {
            UnivariateRealSolverUtils.solve(function, x0, x1);
        });
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)}
     * with function throwing {@link FunctionEvaluationException}.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testBracketFunctionEvaluationException() throws Exception {
        UnivariateRealFunction function = new ThrowingFunction();
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;

        assertThrows(FunctionEvaluationException.class, () -> {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        });
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with function throwing {@link ConvergenceException}.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testBracketConvergenceException() throws Exception {
        UnivariateRealFunction function = new ThrowingConvergenceExceptionFunction();
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maximumIterations = 10;

        assertThrows(ConvergenceException.class, () -> {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);
        });
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with function throwing {@link IllegalArgumentException}.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testBracketIllegalArgumentException() throws Exception {
        UnivariateRealFunction function = new ThrowingIllegalArgumentExceptionFunction();
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maximumIterations = 10;

        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);
        });
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#setup(UnivariateRealFunction)}
     * with null function.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testSetupNullFunction() throws Exception {
        UnivariateRealFunction function = null;

        assertThrows(MathRuntimeException.class, () -> {
            UnivariateRealSolverUtils.setup(function);
        });
    }

    private static class MyFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return x * x - 4.0;
        }
    }

    private static class BisectionSolver implements UnivariateRealSolver {
        @Override
        public double solve(UnivariateRealFunction f, double x0, double x1)
                throws ConvergenceException, FunctionEvaluationException {
            double x2;
            do {
                x2 = UnivariateRealSolverUtils.midpoint(x0, x1);
                if (f.value(x0) * f.value(x2) < 0.0) {
                    x1 = x2;
                } else {
                    x0 = x2;
                }
            } while (Math.abs(x1 - x0) > 1.0E-6);
            return x2;
        }
    }

    private static class ThrowingFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            throw new FunctionEvaluationException("FunctionEvaluationException thrown");
        }
    }

    private static class ThrowingConvergenceExceptionFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) throws ConvergenceException {
            throw new ConvergenceException("ConvergenceException thrown");
        }
    }

    private static class ThrowingIllegalArgumentExceptionFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) throws IllegalArgumentException {
            throw new IllegalArgumentException("IllegalArgumentException thrown");
        }
    }
}