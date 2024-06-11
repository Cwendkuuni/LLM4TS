package org.apache.commons.cli.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;

public class BaseSecantSolverTest {

    @Test
    public void testBaseSecantSolver() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };

        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(f, -3.0, 3.0);
        assertEquals(0.0, Precision.round(root, 6));

        solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.ILLINOIS);
        root = solver.solve(f, -3.0, 3.0);
        assertEquals(0.0, Precision.round(root, 6));

        solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.PEGASUS);
        root = solver.solve(f, -3.0, 3.0);
        assertEquals(0.0, Precision.round(root, 6));

        // Test with different accuracies
        solver = new BaseSecantSolver(1.0e-8, BaseSecantSolver.Method.REGULA_FALSI);
        root = solver.solve(f, -3.0, 3.0);
        assertEquals(0.0, Precision.round(root, 8));

        solver = new BaseSecantSolver(1.0e-12, BaseSecantSolver.Method.ILLINOIS);
        root = solver.solve(f, -3.0, 3.0);
        assertEquals(0.0, Precision.round(root, 12));

        // Test with convergence exception
        UnivariateRealFunction fExp = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new ConvergenceException();
            }
        };

        solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.REGULA_FALSI);
        try {
            solver.solve(fExp, -3.0, 3.0);
            // This line should not be reached
            assertEquals(0.0, 1);
        } catch (ConvergenceException ce) {
            // Expected
        }

        // Test with MathInternalError
        UnivariateRealFunction fError = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new MathInternalError();
            }
        };

        solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.REGULA_FALSI);
        try {
            solver.solve(fError, -3.0, 3.0);
            // This line should not be reached
            assertEquals(0.0, 1);
        } catch (MathInternalError mi) {
            // Expected
        }

        // Test with Brent's method, check if it finds the same root
        UnivariateRealFunction fBrent = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };

        BracketedUnivariateRealSolver<UnivariateRealFunction> solverBrent = new BrentSolver(1.0e-6);
        root = solverBrent.solve(fBrent, -3.0, 3.0);
        assertEquals(0.0, Precision.round(root, 6));

        // Test with bisection method, check if it finds the same root
        UnivariateRealFunction fBisection = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };

        BracketedUnivariateRealSolver<UnivariateRealFunction> solverBisection = new BisectionSolver(1.0e-6);
        root = solverBisection.solve(fBisection, -3.0, 3.0);
        assertEquals(0.0, Precision.round(root, 6));
    }
}