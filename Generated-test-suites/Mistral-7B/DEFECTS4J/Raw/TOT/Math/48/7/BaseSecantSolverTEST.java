package org.apache.commons.math.analysis.solvers.tests;

import junit.framework.Assert;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.junit.Test;

public class BaseSecantSolverTest {

    /**
     * Test Solver with a simple quadratic function.
     */
    @Test
    public void testSolverQuadraticFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(50, f, -3.0, 3.0, 2.5);
        Assert.assertEquals(0.0, f.value(root), 1.0e-6);
    }

    /**
     * Test Solver with a function that has no root in the given interval.
     */
    @Test(expected = ConvergenceException.class)
    public void testSolverNoRootInInterval() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.PEGASUS);
        solver.solve(50, f, 0.0, 2.0 * Math.PI);
    }

    /**
     * Test Solver with a function that has multiple roots.
     */
    @Test
    public void testSolverMultipleRoots() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return (x - 1.0) * (x - 2.0) * (x - 3.0);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.PEGASUS);
        double root1 = solver.solve(50, f, -2.0, 1.0);
        Assert.assertEquals(1.0, f.value(root1), 1.0e-6);

        double root2 = solver.solve(50, f, 1.0, 3.0);
        Assert.assertEquals(2.0, f.value(root2), 1.0e-6);

        double root3 = solver.solve(50, f, -3.0, -1.0);
        Assert.assertEquals(3.0, f.value(root3), 1.0e-6);
    }

    /**
     * Test Solver with a function with a vertical asymptote.
     */
    @Test(expected = MathInternalError.class)
    public void testSolverVerticalAsymptote() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x == 0.0) {
                    throw new MathInternalError("Function has a vertical asymptote at x = 0");
                }
                return 1.0 / x;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.PEGASUS);
        solver.solve(50, f, -1.0, 1.0);
    }
}