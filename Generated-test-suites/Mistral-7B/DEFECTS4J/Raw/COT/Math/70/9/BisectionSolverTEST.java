package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.junit.Test;

/**
 * Test class for BisectionSolver.
 */
public class BisectionSolverTest {

    /**
     * Test constructor with UnivariateRealFunction.
     */
    @Test
    public void testConstructorWithFunction() {
        UnivariateFunction f = new PolynomialFunction(x -> x * x - 4);
        BisectionSolver solver = new BisectionSolver(f);
    }

    /**
     * Test default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        BisectionSolver solver = new BisectionSolver();
    }

    /**
     * Test solve(double, double) with valid arguments.
     */
    @Test
    public void testSolveValidArguments() {
        UnivariateFunction function = new PolynomialFunction(x -> x * x - 4);

        BisectionSolver solver = new BisectionSolver(function);

        double result = solver.solve(1.0, 3.0);

        assertEquals(2.0, result, 1e-6);
    }

    /**
     * Test solve(double, double) with invalid arguments: min > max.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidArgumentsMinGreaterThanMax() {
        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1);
        BisectionSolver solver = new BisectionSolver(function);
        solver.solve(3.0, 2.0);
    }

    /**
     * Test solve(UnivariateRealFunction, double, double) with valid arguments.
     */
    @Test
    public void testSolveValidArgumentsFunction() {
        UnivariateFunction function = new PolynomialFunction(x -> x * x - 4);

        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, 1.0, 3.0);

        assertEquals(2.0, result, 1e-6);
    }

    /**
     * Test solve(UnivariateRealFunction, double, double) with invalid arguments: min > max.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidArgumentsFunctionMinGreaterThanMax() {
        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1);
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, 3.0, 2.0);
    }

    /**
     * Test solve(UnivariateRealFunction, double, double) with invalid function.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveInvalidFunction() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, 1.0, 3.0);
    }

    /**
     * Test MaxIterationsExceededException.
     */
    @Test
    public void testMaxIterationsExceededException() {
        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1);
        BisectionSolver solver = new BisectionSolver();
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(function, 3.0, 2.0));
    }

    /**
     * Test FunctionEvaluationException.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException();
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, 1.0, 3.0);
    }

    /**
     * Test NumberIsTooSmallException.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmallException() {
        UnivariateFunction function = new PolynomialFunction(x -> x * x - 1);
        BisectionSolver solver = new BisectionSolver();
        solver.setAbsoluteAccuracy(0.0);
        solver.solve(function, 1.0, 3.0);
    }
}