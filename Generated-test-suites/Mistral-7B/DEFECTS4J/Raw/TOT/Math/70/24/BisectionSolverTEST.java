package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.UnivariateRealSolverUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolver_simple() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BisectionSolver solver = new BisectionSolver();

        double root = solver.solve(f, -1.0, 1.0);
        assertNotEquals(-1.0, root);
        assertNotEquals(1.0, root);
        assertEquals(Math.PI / 4, root, 1e-5);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBisectionSolver_noFunctionGiven() {
        BisectionSolver solver = new BisectionSolver();
        solver.solve(1.0, 2.0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testBisectionSolver_maxIterationsExceeded() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(1 / x);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.setMaxIterations(1);
        solver.solve(f, -1.0, 1.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBisectionSolver_functionEvaluationException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new ArithmeticException("Divide by zero");
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -1.0, 1.0);
    }

    @Test
    public void testBisectionSolver_edgeCases() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };
        BisectionSolver solver = new BisectionSolver();

        // test zero root
        assertEquals(0.0, solver.solve(f, -1.0, 1.0), 1e-9);

        // test negative root
        assertEquals(-1.0, solver.solve(f, -2.0, 0.0), 1e-9);

        // test positive root
        assertEquals(1.0, solver.solve(f, 0.0, 2.0), 1e-9);

        // test roots close to interval bounds
        assertEquals(0.5, solver.solve(f, -1.1, 1.1), 1e-9);
        assertEquals(-0.5, solver.solve(f, -1.1, 1.1), 1e-9);

        // test roots very close to each other
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 1;
            }
        };
        assertEquals(0.0, solver.solve(f2, -1.0, 1.0), 1e-9);
    }
}