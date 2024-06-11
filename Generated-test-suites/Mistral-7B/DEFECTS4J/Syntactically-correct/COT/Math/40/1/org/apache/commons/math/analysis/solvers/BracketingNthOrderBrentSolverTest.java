package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;

import org.apache.commons.math.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math.analysis.solvers.BracketingNthOrderBrentSolver.AllowedSolution;

/**
 * Test class for BracketingNthOrderBrentSolver.
 */
public class BracketingNthOrderBrentSolverTest {

    /**
     * Test constructor with default arguments.
     */
    @org.junit.Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-10);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.001, 4);
        assertEquals(0.001, solver.getAbsoluteAccuracy(), 1e-10);
        assertEquals(4, solver.getMaximalOrder());
    }

    /**
     * Test constructor with invalid maximal order.
     */
    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testInvalidConstructor() {
        new BracketingNthOrderBrentSolver(0.001, 1);
    }

    /**
     * Test getMaximalOrder method.
     */
    @org.junit.Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.001, 4);
        assertEquals(4, solver.getMaximalOrder());
    }

    /**
     * Test solve method with a simple function and default arguments.
     */
    @org.junit.Test
    public void testSimpleSolve() {
        UnivariateFunction f = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2 * x;
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(-1.0, solver.solve(f, -3.0, 1.0), 1e-8);
    }

    /**
     * Test solve method with a simple function and custom arguments.
     */
    @org.junit.Test
    public void testCustomSolve() {
        UnivariateFunction f = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2 * x;
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.001, 4);
        assertEquals(-1.0, solver.solve(f, -3.0, 1.0), 1e-8);
    }

    /**
     * Test solve method with a function that has no real roots.
     */
    @org.junit.Test
    public void testSolveNoRoots() {
        UnivariateFunction f = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(Math.abs(x)) - 1;
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.001, 4);
        assertThrows(MathIllegalArgumentException.class, () -> solver.solve(f, -1.0, 1.0));
    }

    /**
     * Test solve method with a function that has multiple real roots.
     */
    @org.junit.Test
    public void testSolveMultipleRoots() {
        UnivariateFunction f = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1.0, 3) - 1;
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.001, 4);
        assertEquals(1.0, solver.solve(f, -2.0, 3.0), 1e-8);
        assertEquals(1.0, solver.solve(f, -1.5, 1.5), 1e-8);
        assertEquals(1.0, solver.solve(f, 0.0, 2.0), 1e-8);
    }

    /**
     * Test solve method with a function that has roots of different signs.
     */
    @org.junit.Test
    public void testSolveRootsDifferentSigns() {
        UnivariateFunction f = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1.0, 3) - 1;
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.001, 4);
        assertEquals(1.0, solver.solve(f, -2.5, -0.5), 1e-8); // left root
        assertEquals(1.0, solver.solve(f, 0.5, 2.5), 1e-8);   // right root
    }

    /**
     * Test solve method with a function that has roots of the same sign.
     */
    @org.junit.Test
    public void testSolveRootsSameSign() {
        UnivariateFunction f = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1.0, 3) + 1;
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.001, 4);
        assertEquals(2.0, solver.solve(f, -1.0, 3.0), 1e-8);
    }

    /**
     * Test solve method with a function with very small roots.
     */
    @org.junit.Test
    public void testSolveSmallRoots() {
        UnivariateFunction f = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(Math.log(x) * 2) - x;
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-12, 10);
        assertEquals(Math.E, solver.solve(f, 1.0, 2.0), 1e-10);
    }

    /**
     * Test solve method with a function with very large roots.
     */
    @org.junit.Test
    public void testSolveLargeRoots() {
        UnivariateFunction f = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - Math.pow(1000.0, 2);
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 10);
        assertEquals(1000.0, solver.solve(f, 999.0, 1001.0), 1e-8);
    }

    /**
     * Test solve method with a function with roots of different orders.
     */
    @org.junit.Test
    public void testSolveRootsDifferentOrders() {
        UnivariateFunction f = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1.0, 3) - Math.pow(x - 1.5, 2);
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.001, 4);
        assertEquals(1.0, solver.solve(f, -2.0, 1.5), 1e-8);
        assertEquals(1.5, solver.solve(f, 1.2, 2.2), 1e-8);
    }

}