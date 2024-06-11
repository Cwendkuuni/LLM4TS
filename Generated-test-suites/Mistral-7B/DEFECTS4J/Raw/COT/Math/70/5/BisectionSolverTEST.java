package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolverConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeprecatedConstructor() {
        new BisectionSolver(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        });
    }

    @Test
    public void testSolveSimpleFunction() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1.0;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, -2.0, 2.0);
        assertEquals(-1.0, result, 1e-6);
    }

    @Test
    public void testSolveFunctionWithInitialValue() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, -3.0, 3.0, 1.0);
        assertEquals(Math.PI / 4.0, result, 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.setMaxIterations(1);
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(function, -3.0, 3.0));
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Testing exception");
            }
        };

        BisectionSolver solver = new BisectionSolver();
        assertThrows(FunctionEvaluationException.class, () -> solver.solve(function, -3.0, 3.0));
    }

    @Test
    public void testVerifyInterval() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1.0;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        assertThrows(IllegalArgumentException.class, () -> solver.solve(function, -1.0, -2.0));
        assertThrows(IllegalArgumentException.class, () -> solver.solve(function, 2.0, -1.0));
    }
}