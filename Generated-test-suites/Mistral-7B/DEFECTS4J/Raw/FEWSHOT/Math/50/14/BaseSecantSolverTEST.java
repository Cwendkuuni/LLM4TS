package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.solvers.BracketedRealSolver;
import org.apache.commons.math3.solvers.BrentSolver;
import org.apache.commons.math3.solvers.BrentSolver.BrentMethod;
import org.apache.commons.math3.solvers.LaguerreSolver;
import org.apache.commons.math3.solvers.SecantSolver;

import org.apache.commons.cli.BaseSecantSolver;
import org.apache.commons.cli.BracketedUnivariateRealSolver;
import org.apache.commons.cli.UnivariateRealFunction;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_RegulaFalsi() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected BracketedRealSolver<UnivariateFunction> getBracketSolver() {
                return new BrentSolver(1e-6, 1e-6, 1e-6, BrentMethod.QUADRATIC);
            }
        };
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});
        try {
            double root = solver.solve(10, f, -10, 10, 0);
            assertEquals(0.0, root, 1e-7);
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded");
        }
    }

    @Test
    public void testSolve_Illinois() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected BracketedRealSolver<UnivariateFunction> getBracketSolver() {
                return new BrentSolver(1e-6, 1e-6, 1e-6, BrentMethod.QUADRATIC);
            }
        };
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});
        try {
            double root = solver.solve(10, f, -10, 10, 0);
            assertEquals(0.0, root, 1e-7);
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded");
        }
    }

    @Test
    public void testSolve_Pegasus() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS) {
            @Override
            protected BracketedRealSolver<UnivariateFunction> getBracketSolver() {
                return new LaguerreSolver();
            }
        };
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});
        try {
            double root = solver.solve(10, f, -10, 10, 0);
            assertEquals(0.0, root, 1e-7);
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded");
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolve_Secant() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.SECANT);
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});
        solver.solve(10, f, -10, 10, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolve_NullFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = null;
        solver.solve(10, f, -10, 10, 0);
    }
}