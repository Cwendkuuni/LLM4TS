package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        double result = solver.solve(f, 0, 3);
        assertEquals(2.0, result, 0.0001);

        result = solver.solve(f, -3, 0);
        assertEquals(-2.0, result, 0.0001);

        result = solver.solve(f, 1, 2);
        assertEquals(2.0, result, 0.0001);

        result = solver.solve(f, -2, -1);
        assertEquals(-2.0, result, 0.0001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolve_maxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.setMaximumIterationCount(5);

        solver.solve(f, -10, 10);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolve_functionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                throw new UnsupportedOperationException("Function evaluation exception");
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, 0, 3);
    }
}