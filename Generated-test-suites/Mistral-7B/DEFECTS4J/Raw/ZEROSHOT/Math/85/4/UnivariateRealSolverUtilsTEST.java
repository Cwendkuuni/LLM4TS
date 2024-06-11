package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.BisectionSolver;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;

public class UnivariateRealSolverUtilsTest {

    @org.junit.Test
    public void testSolve() throws Exception {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };

        double root = UnivariateRealSolverUtils.solve(f, -3.0, 3.0);
        assertEquals(0.0, root, 1e-7);

        // Test with absolute accuracy
        root = UnivariateRealSolverUtils.solve(f, -3.0, 3.0, 1e-5);
        assertEquals(0.0, root, 1e-5);

        // Test with ConvergenceException
        assertThrows(ConvergenceException.class, () -> {
            UnivariateRealSolverUtils.solve(f, 0.0, 0.0);
        });

        // Test with FunctionEvaluationException
        UnivariateRealFunction exceptionThrowingFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws Exception {
                throw new FunctionEvaluationException("Exception!");
            }
        };
        assertThrows(FunctionEvaluationException.class, () -> {
            UnivariateRealSolverUtils.solve(exceptionThrowingFunction, -3.0, 3.0);
        });

        // Test with IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.solve(null, -3.0, 3.0);
        });

        // Test with IllegalArgumentException (endpoints not valid)
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.solve(f, 3.0, -3.0);
        });
    }

    @org.junit.Test
    public void testBracket() throws Exception {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };

        double[] brackets = UnivariateRealSolverUtils.bracket(f, 0.0, -3.0, 3.0);
        assertEquals(-2.0, brackets[0], 1e-7);
        assertEquals(0.0, brackets[1], 1e-7);

        brackets = UnivariateRealSolverUtils.bracket(f, 0.0, -3.0, 3.0, 1000);
        assertEquals(-2.0, brackets[0], 1e-7);
        assertEquals(0.0, brackets[1], 1e-7);

        UnivariateRealFunction exceptionThrowingFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws Exception {
                throw new FunctionEvaluationException("Exception!");
            }
        };
        assertThrows(FunctionEvaluationException.class, () -> {
            UnivariateRealSolverUtils.bracket(exceptionThrowingFunction, 0.0, -3.0, 3.0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.bracket(null, 0.0, -3.0, 3.0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.bracket(f, 3.0, 0.0, 3.0);
        });

        assertThrows(NoBracketingException.class, () -> {
            UnivariateRealSolverUtils.bracket(f, 0.0, -3.0, 3.0, 0);
        });

        assertThrows(MaxIterationsExceededException.class, () -> {
            UnivariateRealSolverUtils.bracket(f, 0.0, -3.0, 3.0, 0);
        });
    }

    @org.junit.Test
    public void testBisectionSolver() throws Exception {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };

        double root = UnivariateRealSolverUtils.solve(new BisectionSolver(), f, -3.0, 3.0);
        assertEquals(0.0, root, 1e-7);
    }
}