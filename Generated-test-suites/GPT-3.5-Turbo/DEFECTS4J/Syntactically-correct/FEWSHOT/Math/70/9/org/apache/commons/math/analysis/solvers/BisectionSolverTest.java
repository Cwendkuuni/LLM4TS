package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

public class BisectionSolverTest {
    private static final double EPSILON = 1e-6;

    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x*x - 25;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        double result = solver.solve(f, 0, 10);
        assertEquals(5.0, result, EPSILON);

        result = solver.solve(f, -10, 0);
        assertEquals(-5.0, result, EPSILON);

        result = solver.solve(f, -10, 10);
        assertEquals(-5.0, result, EPSILON);

        result = solver.solve(f, -10, -5.1);
        assertEquals(-5.0, result, EPSILON);

        result = solver.solve(f, -5.0, 10);
        assertEquals(5.0, result, EPSILON);

        result = solver.solve(f, -5.1, 5.1);
        assertEquals(5.0, result, EPSILON);
    }
}