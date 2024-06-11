package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testSolveSimpleFunction() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, -2.0, 2.0);
        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testSolveFunctionWithInitialGuess() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, 1.0, 2.0);
        assertTrue(Math.abs(Math.PI / 2.0 - root) < 1e-6);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionWithNonBracketingInterval() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, 0.0, 0.0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveFunctionWithMaxIterationsExceeded() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        BisectionSolver solver = new BisectionSolver(1);
        solver.solve(function, 1.0, 2.0);
    }
}