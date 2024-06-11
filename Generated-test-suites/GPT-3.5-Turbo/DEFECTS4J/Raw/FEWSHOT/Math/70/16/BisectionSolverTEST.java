
package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BisectionSolverTest {
    
    private static final double EPSILON = 1e-6;

    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        double result = solver.solve(f, 0, 10);

        double expected = 2;
        assertEquals(expected, result, EPSILON);
    }

    @Test
    public void testSolveWithInitial() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        double result = solver.solve(f, 0, 10, 5);

        double expected = 2;
        assertEquals(expected, result, EPSILON);
    }
}
