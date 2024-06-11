package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.PrecisionRequiredException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.UnivariateRealSolverUtils;

import org.junit.Test;

public class BisectionSolverTest {

    /**
     * Test constructor with UnivariateRealFunction.
     */
    @Test
    public void testConstructorWithFunction() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        // no need to test the result as it is not used here
    }

    /**
     * Test default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        BisectionSolver solver = new BisectionSolver();
        // no need to test the result as it is not used here
    }

    /**
     * Test solve method with minimum, maximum and initial value.
     * @throws Exception
     */
    @Test
    public void testSolveWithMinMaxInitialValue() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        double result = solver.solve(0.0, 2.0);
        double expected = Precision.round(1.0, 3); // Approximate result
        assertEquals("The result is not correct", expected, result, 1e-3);
    }

    /**
     * Test solve method with minimum and maximum value.
     * @throws Exception
     */
    @Test
    public void testSolveWithMinMax() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, 0.0, 2.0);
        double expected = Precision.round(1.0, 3); // Approximate result
        assertEquals("The result is not correct", expected, result, 1e-3);
    }

    /**
     * Test exceptions when function is not bracketing the root.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 3;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        solver.solve(0.0, 2.0); // Should throw MaxIterationsExceededException
    }

    /**
     * Test exceptions when function is not continuous.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x < 0) {
                    throw new ArithmeticException("Function not defined for negative x");
                }
                return Math.pow(x, 2) - 2;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, -1.0, 1.0); // Should throw FunctionEvaluationException
    }

    /**
     * Test exceptions when initial value is too close to the root.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmallException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, 0.0, 0.0); // Should throw NumberIsTooSmallException
    }

    /**
     * Test exceptions when absolute accuracy is too small.
     */
    @Test(expected = PrecisionRequiredException.class)
    public void testPrecisionRequiredException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        solver.setAbsoluteAccuracy(1e-8);
        solver.solve(function, 0.0, 2.0); // Should throw PrecisionRequiredException
    }

    /**
     * Test exceptions when maximal iteration count is too small.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededExceptionWithSmallMaxIterations() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        solver.setMaxIterations(1);
        solver.solve(function, 0.0, 2.0); // Should throw MaxIterationsExceededException
    }

    /**
     * Test exceptions when minimal value is greater than the maximum value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, 2.0, 0.0); // Should throw IllegalArgumentException
    }
}