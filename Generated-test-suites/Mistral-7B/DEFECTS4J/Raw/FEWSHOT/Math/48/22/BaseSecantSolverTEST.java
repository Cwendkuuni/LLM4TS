package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.apache.commons.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.commons.math.exception.ConvergenceException;
import org.apache.commons.commons.math.util.Precision;
import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.AllowedSolution;
import org.apache.commons.commons.math.analysis.solvers.Method;

public class BaseSecantSolverTest {

    @Test
    public void testBaseSecantSolver() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        try {
            double root = solver.solve(10, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
            assertEquals(2.0, Precision.round(root, 3));
        } catch (ConvergenceException e) {
            fail("Solver did not converge.");
        }

        solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        try {
            double root = solver.solve(10, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
            assertEquals(2.0, Precision.round(root, 3));
        } catch (ConvergenceException e) {
            fail("Solver did not converge.");
        }

        solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        try {
            double root = solver.solve(10, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
            assertEquals(2.0, Precision.round(root, 3));
        } catch (ConvergenceException e) {
            fail("Solver did not converge.");
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testBaseSecantSolverDivergence() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };

        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        solver.solve(10, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MathInternalError.class)
    public void testBaseSecantSolverInvalidMethod() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-6, Method.INVALID);
        solver.solve(10, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
    }
}