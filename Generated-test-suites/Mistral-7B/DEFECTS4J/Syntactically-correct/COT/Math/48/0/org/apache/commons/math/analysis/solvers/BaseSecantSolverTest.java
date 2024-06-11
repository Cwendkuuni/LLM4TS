package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    // Test methods and their edge cases with different allowed solutions go here.

    // Test for a function with no root in the given interval.
    @Test(expected = ConvergenceException.class)
    public void testNoRoot() throws ConvergenceException {
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1.0e-6);
        UnivariateRealFunction function = x -> Math.sin(x);
        solver.solve(10, function, -1.0, 1.0);
    }

    // Test for a non-monotonic function.
    @Test(expected = ConvergenceException.class)
    public void testNonMonotonic() throws ConvergenceException {
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1.0e-6);
        UnivariateRealFunction function = x -> x * x * (x - 1);
        solver.solve(10, function, -1.0, 1.0);
    }

    // Test for a function that is too steep to converge.
    @Test(expected = ConvergenceException.class)
    public void testTooSteep() throws ConvergenceException {
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1.0e-6);
        UnivariateRealFunction function = x -> Math.pow(Math.E, Math.abs(x));
        solver.solve(10, function, -1.0, 1.0);
    }

    // Test for a function with zero at the minimum.
    @Test(expected = ConvergenceException.class)
    public void testZeroAtMin() throws ConvergenceException {
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1.0e-6);
        UnivariateRealFunction function = x -> Math.pow(Math.E, x) * Math.sin(1.0 / x);
        solver.solve(10, function, 0.001, 0.1);
    }

    // Test for a function with zero at the maximum.
    @Test(expected = ConvergenceException.class)
    public void testZeroAtMax() throws ConvergenceException {
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1.0e-6);
        UnivariateRealFunction function = x -> Math.pow(Math.E, x) * Math.sin(1.0 / x);
        solver.solve(10, function, 0.9, 1.0);
    }
}