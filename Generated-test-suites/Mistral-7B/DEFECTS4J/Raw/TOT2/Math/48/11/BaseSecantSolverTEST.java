package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.special.E;
import org.junit.Test;

/**
 * Test cases for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test solver with a simple function (x^2 - 1).
     */
    @Test
    public void testSimpleFunction() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(0.001, Method.ILLINOIS);
        final double root = solver.solve(100, f, -2.0, 2.0, 1.0);
        assertEquals(Math.sqrt(2.0) - 1.0, root, 1e-5);
    }

    /**
     * Test solver with a function that has no root within the interval.
     */
    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - Math.sin(Math.PI);
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(0.001, Method.ILLINOIS);
        solver.solve(100, f, -10.0, 10.0, 0.0);
    }

    /**
     * Test solver with a function that throws an exception.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testExceptionFunction() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x < 0) {
                    throw new MathIllegalArgumentException("x must be non-negative.");
                }
                return x * x;
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(0.001, Method.ILLINOIS);
        solver.solve(100, f, -1.0, 1.0, 0.0);
    }

    /**
     * Test solver with a function that does not converge.
     */
    @Test(expected = ConvergenceException.class)
    public void testNonConvergentFunction() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(E.PI_DIVIDED_BY_FOUR.doubleValue()) / x;
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(0.001, Method.ILLINOIS);
        solver.solve(100, f, -1.0, 1.0, 0.0);
    }

    /**
     * Test solver with a function that has multiple roots.
     */
    @Test
    public void testMultipleRootsFunction() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return (x - 1.0) * (x - 2.0) * (x - 3.0);
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(0.001, Method.ILLINOIS);
        final double root1 = solver.solve(100, f, -5.0, 1.0);
        assertEquals(1.0, root1, 1e-5);

        final double root2 = solver.solve(100, f, 1.0, 5.0);
        assertEquals(2.0, root2, 1e-5);

        final double root3 = solver.solve(100, f, 4.0, 6.0);
        assertEquals(3.0, root3, 1e-5);
    }

}