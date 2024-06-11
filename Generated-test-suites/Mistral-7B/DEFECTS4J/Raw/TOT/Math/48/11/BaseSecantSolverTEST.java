package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private UnivariateRealFunction function;
    private BaseSecantSolver<UnivariateRealFunction> solver;

    @Before
    public void setUp() {
        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        solver = new RegulaFalsiSolver<>(1e-6, RegulaFalsiSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolveSimple() {
        double root = solver.solve(10, function, -5.0, 5.0);
        assertEquals("Root found incorrectly", 2.0, root, 1e-6);
    }

    @Test(expected = MaxEvalException.class)
    public void testSolveMaxEval() {
        solver = new RegulaFalsiSolver<>(1e-6, RegulaFalsiSolver.Method.REGULA_FALSI);
        solver.solve(0, function, -5.0, 5.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSolveNumberTooLarge() {
        solver = new RegulaFalsiSolver<>(1e-6, RegulaFalsiSolver.Method.REGULA_FALSI);
        solver.solve(10, function, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolveNullFunction() {
        solver = new RegulaFalsiSolver<>(1e-6, RegulaFalsiSolver.Method.REGULA_FALSI);
        solver.solve(10, null, -5.0, 5.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidBounds() {
        solver = new RegulaFalsiSolver<>(1e-6, RegulaFalsiSolver.Method.REGULA_FALSI);
        solver.solve(10, function, Double.NEGATIVE_INFINITY, -5.0);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        BaseSecantSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver<>(1e-6, RegulaFalsiSolver.Method.REGULA_FALSI, AllowedSolution.LEFT_SIDE);
        double root = solver.solve(10, function, -5.0, 5.0);
        assertEquals("Root found incorrectly", 2.0, root, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveConvergenceException() {
        BaseSecantSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver<>(1e-16, RegulaFalsiSolver.Method.REGULA_FALSI);
        solver.solve(10, function, -5.0, 5.0);
    }
}