package org.apache.commons.math.analysis.solvers;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.solvers.BisectionSolver;

public class BisectionSolverTest {

    private UnivariateRealFunction function;
    private BisectionSolver solver;

    @Before
    public void setUp() {
        function = new PolynomialFunction(3, new double[]{-1.0, 1.0, 1.0});
        solver = new BisectionSolver(function);
    }

    @Test
    public void testSolve_positiveRoot() {
        // Test for a function with a positive root
        double result = solver.solve(function, -1.0, 1.0);
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testSolve_negativeRoot() {
        // Test for a function with a negative root
        double result = solver.solve(function, 1.0, 3.0);
        assertEquals(-1.0, result, 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        // Test for maximum iterations exceeded exception
        solver = new BisectionSolver(function);
        solver.solve(function, -1.0, 1.0, 0.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        // Test for function evaluation exception
        UnivariateRealFunction invalidFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };
        solver = new BisectionSolver(invalidFunction);
        solver.solve(invalidFunction, -1.0, 1.0);
    }

    @Test
    public void testSolve_functionWithZeroRoot() {
        solver = new BisectionSolver(function);
        double result = solver.solve(function, -3.0, 3.0);
        assertEquals(0.0, result, 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded_functionWithNoRoot() {
        UnivariateRealFunction functionNegative = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        solver = new BisectionSolver(functionNegative);
        solver.solve(functionNegative, -1.0, 1.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException_divideByZero() {
        UnivariateRealFunction functionDivideByZero = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 1.0 / x;
            }
        };
        solver = new BisectionSolver(functionDivideByZero);
        solver.solve(functionDivideByZero, -1.0, 1.0);
    }

    @Test
    public void testSolve_functionWithNegativeRoot() {
        UnivariateRealFunction functionNegative = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        solver = new BisectionSolver(functionNegative);
        double result = solver.solve(functionNegative, -3.0, -1.0);
        assertEquals(-1.84776e-19, result, 1e-6);
    }

    @Test
    public void testSolve_functionWithTwoRoots() {
        UnivariateRealFunction functionTwoRoots = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1.0;
            }
        };
        solver = new BisectionSolver(functionTwoRoots);
        double result = solver.solve(functionTwoRoots, -2.0, 2.0);
        assertEquals(-1.0, result, 1e-6);
        result = solver.solve(functionTwoRoots, 1.0, 2.0);
        assertEquals(1.0, result, 1e-6);
    }
}