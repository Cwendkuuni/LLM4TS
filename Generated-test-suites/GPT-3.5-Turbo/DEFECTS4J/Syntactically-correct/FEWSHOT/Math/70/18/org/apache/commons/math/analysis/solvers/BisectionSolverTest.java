package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Before;
import org.junit.Test;

public class BisectionSolverTest {
    private BisectionSolver solver;
    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        solver = new BisectionSolver();
        function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
    }

    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        double result = solver.solve(function, -10, 10);
        assertEquals(2, result, 0.0001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        solver.solve(function, -10, 10, 1);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction exceptionFunction = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };
        solver.solve(exceptionFunction, -10, 10);
    }

    @Test
    public void testSolveWithInitialValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        double result = solver.solve(function, -10, 10, 1);
        assertEquals(2, result, 0.0001);
    }

    @Test
    public void testSolveWithNegativeInterval() throws MaxIterationsExceededException, FunctionEvaluationException {
        double result = solver.solve(function, -10, -1);
        assertTrue(result < 0);
    }

    @Test
    public void testSolveWithPositiveInterval() throws MaxIterationsExceededException, FunctionEvaluationException {
        double result = solver.solve(function, 1, 10);
        assertTrue(result > 0);
    }
}