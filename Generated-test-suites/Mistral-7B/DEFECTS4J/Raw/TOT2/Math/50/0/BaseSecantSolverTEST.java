package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * with {@link BaseSecantSolver#REGULA_FALSI} method.
     */
    @Test
    public void testRegulaFalsiSolver() {
        final double epsilon = 1e-9;
        final UnivariateFunction f = new AbstractUnivariateFunction() {
            @Override
            public double value(final double x) {
                return x * x - 2;
            }
        };
        final BaseSecantSolver solver = new RegulaFalsiSolver(epsilon);
        final double root = solver.solve(f, -1.5, 1.5);
        Assert.assertEquals(0.0, Precision.abs(root - 0.5), epsilon);
    }

    /**
     * Test for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * with {@link BaseSecantSolver#ILLINOIS} method.
     */
    @Test
    public void testIllinoisSolver() {
        final double epsilon = 1e-9;
        final UnivariateFunction f = new AbstractUnivariateFunction() {
            @Override
            public double value(final double x) {
                return Math.sin(x) - x;
            }
        };
        final BaseSecantSolver solver = new IllinoisSolver(epsilon);
        final double root = solver.solve(f, -1.5, 1.5);
        Assert.assertEquals(0.0, Precision.abs(Math.PI / 2 - root), epsilon);
    }

    /**
     * Test for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * with {@link BaseSecantSolver#PEGASUS} method.
     */
    @Test
    public void testPegasusSolver() {
        final double epsilon = 1e-9;
        final UnivariateFunction f = new AbstractUnivariateFunction() {
            @Override
            public double value(final double x) {
                return Math.pow(x, 3) - 1;
            }
        };
        final BaseSecantSolver solver = new PegasusSolver(epsilon);
        final double root = solver.solve(f, -2.0, 1.0);
        Assert.assertEquals(0.0, Precision.abs(Math.cbrt(1.0) - root), epsilon);
    }

    /**
     * Test for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * with incorrect input.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIncorrectInput() {
        final BaseSecantSolver solver = new BaseSecantSolver(-1.0, Method.REGULA_FALSI);
    }

    /**
     * Test for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * when the function does not change sign in the given interval.
     */
    @Test
    public void testNoSignChange() {
        final double epsilon = 1e-9;
        final UnivariateFunction f = new AbstractUnivariateFunction() {
            @Override
            public double value(final double x) {
                return x * x - 1;
            }
        };
        final BaseSecantSolver solver = new RegulaFalsiSolver(epsilon);
        final double root = solver.solve(f, -1.5, 1.5);
        Assert.assertEquals(Double.NaN, root, epsilon);
    }
}