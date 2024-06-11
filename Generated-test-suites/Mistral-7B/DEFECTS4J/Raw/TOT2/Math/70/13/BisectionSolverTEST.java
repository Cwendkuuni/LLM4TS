package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolverConstructor() {
        new BisectionSolver();
    }

    @Test
    public void testSolveEdgeCases() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        assertFalse(solver.solve(f, -2, 2) instanceof Double);
        assertEquals(-2.0, solver.solve(f, -2, 0), 1e-6);
        assertEquals(0.0, solver.solve(f, 0, 2), 1e-6);
        assertEquals(2.0, solver.solve(f, 2, 4), 1e-6);

        UnivariateRealFunction functionWithNoRoot = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        assertThrows(FunctionEvaluationException.class, () -> solver.solve(functionWithNoRoot, -1, 1));

        UnivariateRealFunction functionWithMaxIterationsExceeded = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(0.5, x) - Math.pow(0.5, x * 2);
            }
        };
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(functionWithMaxIterationsExceeded, -1, 1));
    }

    // Add test cases for other methods

}