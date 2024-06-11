package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.solvers.BrentSolver;
import org.apache.commons.math3.solvers.BisectionSolver;
import org.apache.commons.math3.solvers.PowellSolver;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        double root = UnivariateRealSolverUtils.solve(func, -1.0, 1.0);
        assertEquals(Math.PI / 4.0, root, 1e-8);
    }

    @Test
    public void testSolveWithDefaultSolver() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        double root = UnivariateRealSolverUtils.solve(func, -1.0, 1.0);
        assertEquals(-sqrt(2.0), root, 1e-8);
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };

        double absAccuracy = 1e-6;
        double root = UnivariateRealSolverUtils.solve(func, -1.0, 1.0, absAccuracy);
        assertEquals(Math.E - 1.0, root, absAccuracy);
    }

    @Test
    public void testSolveWithNullFunction() {
        assertThrows(MathIllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.solve(null, 0.0, 1.0);
        });
    }

    @Test
    public void testSolveWithInvalidInterval() {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };

        assertThrows(MathIllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.solve(func, 1.0, 1.0);
        });
    }

    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        double[] brackets = UnivariateRealSolverUtils.bracket(func, 0.0, -1.0, 1.0);
        assertArrayEquals(new double[]{ -sqrt(2.0), 0.0 }, brackets, 1e-8);
    }

    @Test
    public void testBracketWithDefaultSolver() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        double[] brackets = UnivariateRealSolverUtils.bracket(func, 0.0, -1.0, 1.0);
        assertArrayEquals(new double[]{ Math.PI / 4.0, Math.PI / 2.0 }, brackets, 1e-8);
    }

    @Test
    public void testBracketWithMaxIterations() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
            };

            double[] brackets = UnivariateRealSolverUtils.bracket(func, 0.0, -1.0, 1.0, 5);
            assertArrayEquals(new double[]{ 1.0, 1.5708 }, brackets, 1e-8);
        }

    @Test
    public void testBracketWithInvalidFunction() {
        assertThrows(MathIllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.bracket(null, 0.0, -1.0, 1.0);
        });
    }

    @Test
    public void testBracketWithInvalidMaxIterations() {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };

        assertThrows(MathIllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.bracket(func, 0.0, -1.0, 1.0, -1);
        });
    }

    @Test
    public void testBracketWithInvalidInitial() {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };

        assertThrows(MathIllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.bracket(func, -1.0, 0.0, 1.0);
        });
    }

    @Test
    public void testBracketWithConvergenceException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x > 0.5) {
                    throw new ConvergenceException("Reached maximum iterations");
                }
                return x * x - 1;
            }
        };

        assertThrows(ConvergenceException.class, () -> {
            UnivariateRealSolverUtils.bracket(func, 0.0, -1.0, 1.0);
        });
    }

    @Test
    public void testMidpoint() {
        assertEquals(5.5, UnivariateRealSolverUtils.midpoint(2.5, 8.5), 1e-8);
    }

    @Test
    public void testSetup() {
        UnivariateRealFunction func = null;

        assertThrows(MathIllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.setup(func);
        });
    }

    @Test
    public void testLazyHolder() {
        assertEquals(UnivariateRealSolverFactory.class,
                UnivariateRealSolverUtils.LazyHolder.FACTORY.getClass());
    }
}