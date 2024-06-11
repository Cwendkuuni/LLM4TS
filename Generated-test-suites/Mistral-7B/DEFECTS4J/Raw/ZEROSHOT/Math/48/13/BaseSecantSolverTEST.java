package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnbracketedException;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.special.LogBeta;
import org.apache.commons.math3.special.LogGamma;

import org.junit.Test;

/**
 * Tests for BaseSecantSolver class.
 */
public class BaseSecantSolverTest {

    @Test
    public void testSolve_simple() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        double result = solver.solve(10, f, -2.0, 2.0, 0.0);
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testSolve_bracketing() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.log(x) - Math.log(2.0);
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double result = solver.solve(10, f, 1.0, 3.0, 2.0);
        assertEquals(1.0, result, 1e-6);
    }

    @Test
    public void testSolve_noBracketing() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - Math.cos(x);
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        assertThrows(UnbracketedException.class, () -> solver.solve(10, f, -2.0, 2.0, 0.0));
    }

    @Test
    public void testSolve_maxIterations() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Erf.erf(x);
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        assertThrows(MaxIterationsExceededException.class, () -> {
            solver.solve(0, f, -2.0, 2.0, 0.0);
        });
    }

    @Test
    public void testSolve_allowedSolution() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.log(x) - LogBeta.logBeta(1.5, 1.5);
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS,
                AllowedSolution.LEFT_SIDE);
        double result = solver.solve(10, f, 1.0, 3.0, 2.0);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolve_allowedSolution_rightSide() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.log(x) - LogBeta.logBeta(1.5, 1.5);
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS,
                AllowedSolution.RIGHT_SIDE);
        double result = solver.solve(10, f, 1.0, 3.0, 2.0);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolve_allowedSolution_belowSide() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.log(x) - LogBeta.logBeta(1.5, 1.5);
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS,
                AllowedSolution.BELOW_SIDE);
        double result = solver.solve(10, f, 1.0, 3.0, 2.0);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolve_allowedSolution_aboveSide() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.log(x) - LogBeta.logBeta(1.5, 1.5);
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS,
                AllowedSolution.ABOVE_SIDE);
        double result = solver.solve(10, f, 1.0, 3.0, 2.0);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolve_allowedSolution_noBracketing() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - Math.cos(x);
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI,
                AllowedSolution.LEFT_SIDE);
        assertThrows(UnbracketedException.class, () -> solver.solve(10, f, -2.0, 2.0, 0.0));
    }

    @Test
    public void testSolve_allowedSolution_noBracketing_rightSide() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - Math.cos(x);
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI,
                AllowedSolution.RIGHT_SIDE);
        assertThrows(UnbracketedException.class, () -> solver.solve(10, f, -2.0, 2.0, 0.0));
    }

    @Test
    public void testSolve_allowedSolution_noBracketing_belowSide() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - Math.cos(x);
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI,
                AllowedSolution.BELOW_SIDE);
        assertThrows(UnbracketedException.class, () -> solver.solve(10, f, -2.0, 2.0, 0.0));
    }

    @Test
    public void testSolve_allowedSolution_noBracketing_aboveSide() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - Math.cos(x);
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI,
                AllowedSolution.ABOVE_SIDE);
        assertThrows(UnbracketedException.class, () -> solver.solve(10, f, -2.0, 2.0, 0.0));
    }

    @Test
    public void testSolve_noBracketing_allowedBelowSide() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return LogGamma.logGamma(x + 0.5) - LogGamma.logGamma(x - 0.5);
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS,
                AllowedSolution.BELOW_SIDE);
        assertThrows(NoBracketingException.class, () -> solver.solve(10, f, 0.0, 1.0, 0.5));
    }

    @Test
    public void testSolve_noBracketing_allowedAboveSide() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return LogGamma.logGamma(x + 0.5) - LogGamma.logGamma(x - 0.5);
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS,
                AllowedSolution.ABOVE_SIDE);
        assertThrows(NoBracketingException.class, () -> solver.solve(10, f, 0.0, 1.0, 0.5));
    }

}