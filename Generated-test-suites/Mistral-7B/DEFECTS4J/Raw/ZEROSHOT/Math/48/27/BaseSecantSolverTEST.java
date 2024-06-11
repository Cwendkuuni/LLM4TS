package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.util.Precision;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver.Method;

public class BaseSecantSolverTest {

    @org.junit.Test
    public void testBaseSecantSolver() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6, function.getValue(0.0));
        double root = solver.solve(100, function, -10.0, 10.0, 0.0);
        assertEquals("Regula Falsi Solver failed to find root", 0.0, root, 1e-6);

        solver = new BaseSecantSolver.IllinoisSolver(1e-6, function.getValue(0.0));
        root = solver.solve(100, function, -10.0, 10.0, 0.0);
        assertEquals("Illinois Solver failed to find root", 0.0, root, 1e-6);

        solver = new BaseSecantSolver.PegasusSolver(1e-6, function.getValue(0.0));
        root = solver.solve(100, function, -10.0, 10.0, 0.0);
        assertEquals("Pegasus Solver failed to find root", 0.0, root, 1e-6);

        try {
            solver = new BaseSecantSolver.BaseSecantSolver(1e-6, function.getValue(0.0), Method.REGULA_FALSI, new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    throw new UnsupportedOperationException();
                }
            });
            fail("Expected an IllegalArgumentException when passing an UnsupportedOperationException function to the constructor");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            solver = new BaseSecantSolver.BaseSecantSolver(1e-6, function.getValue(0.0), Method.REGULA_FALSI, null);
            fail("Expected a NullPointerException when passing a null function to the constructor");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @org.junit.Test(expected = ConvergenceException.class)
    public void testConvergenceExceptionThrown() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
            @Override
            public double derive(double x) {
                return 2 * x;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1e-12, function.getValue(0.0), 100);
        try {
            solver.solve(100, function, -10.0, 10.0, 0.0);
            fail("Expected a ConvergenceException when using a very small maximum number of iterations");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testAllowedSolutions() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6, function.getValue(0.0));

        try {
            solver.solve(100, function, -10.0, 10.0, 0.0, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
            fail("Expected an IllegalArgumentException when attempting to find a root below the function");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            solver.solve(100, function, -10.0, 10.0, 0.0, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
            fail("Expected an IllegalArgumentException when attempting to find a root above the function");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            solver.solve(100, function, -10.0, 10.0, 0.0, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
            double root = solver.solve(100, function, -10.0, 10.0, 0.0);
            assertEquals("Regula Falsi Solver failed to find root on the left side", -1.0, root, 1e-6);
        } catch (IllegalArgumentException e) {
            fail("Expected a root to be found on the left side");
        }

        try {
            solver.solve(100, function, -10.0, 10.0, 0.0, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
            double root = solver.solve(100, function, -10.0, 10.0, 0.0);
            assertEquals("Regula Falsi Solver failed to find root on the right side", 1.0, root, 1e-6);
        } catch (IllegalArgumentException e) {
            fail("Expected a root to be found on the right side");
        }

        try {
            solver.solve(100, function, -10.0, 10.0, 0.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
            double root = solver.solve(100, function, -10.0, 10.0, 0.0);
            assertEquals("Regula Falsi Solver failed to find root", 0.0, root, 1e-6);
        } catch (IllegalArgumentException e) {
            fail("Expected any side to work");
        }

        try {
            solver.solve(100, function, -10.0, 10.0, 0.0, null);
            fail("Expected a NullPointerException when passing a null AllowedSolution to the constructor");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}