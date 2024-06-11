package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BisectionSolverTest {

    private BisectionSolver solver;
    private UnivariateRealFunction func;

    @Before
    public void setUp() {
        solver = new BisectionSolver();
        func = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 2;
            }
        };
    }

    @Test
    public void testSolveWithInitialGuess() throws MaxIterationsExceededException, FunctionEvaluationException {
        double result = solver.solve(func, 0, 2, 1.5);
        assertEquals("Incorrect result", 1.414213562373095, result, 1E-6);
    }

    @Test
    public void testSolveWithoutInitialGuess() throws MaxIterationsExceededException, FunctionEvaluationException {
        double result = solver.solve(func, 0, 2);
        assertEquals("Incorrect result", 1.414213562373095, result, 1E-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        solver.solve(func, 0, 2, 3, 20);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction badFunc = new UnivariateRealFunction() {
            public double value(double x) {
                throw new ArithmeticException("Function Evaluation Exception");
            }
        };
        solver.solve(badFunc, 0, 2);
    }
}