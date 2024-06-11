package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.Polynomials;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketException;
import org.apache.commons.math3.exception.PrecisionExceededException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, Method)}.
     */
    @Test
    public void testConstructor() {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, -1.0});
        final BaseSecantSolver solver = new BaseSecantSolver(1e-9, BaseSecantSolver.Method.ILLINOIS);
        assertNotNull(solver);
        assertEquals(BaseSecantSolver.Method.ILLINOIS, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolve() {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, -1.0});
        final BaseSecantSolver solver = new BaseSecantSolver(1e-9, BaseSecantSolver.Method.ILLINOIS);
        final double x = solver.solve(10, f, -2.0, 2.0, 0.0);
        assertEquals(0.0, x, 1e-9);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveAllowedSolution() {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, -1.0});
        final BaseSecantSolver solver = new BaseSecantSolver(1e-9, BaseSecantSolver.Method.ILLINOIS);
        final double x = solver.solve(10, f, -2.0, 2.0, 0.0, AllowedSolution.RIGHT_SIDE);
        assertEquals(0.0, x, 1e-9);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * With invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveException1() {
        final UnivariateFunction f = f -> Math.pow(f.getValue(), 2) - 1;
        final BaseSecantSolver solver = new BaseSecantSolver(1e-9, BaseSecantSolver.Method.ILLINOIS);
        solver.solve(10, (UnivariateRealFunction) f, -2.0, 2.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * With an incorrect number of evaluations.
     */
    @Test(expected = MaxEvalExceededException.class)
    public void testSolveException2() {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, -1.0});
        final BaseSecantSolver solver = new BaseSecantSolver(1e-9, BaseSecantSolver.Method.ILLINOIS);
        solver.solve(-1, f, -2.0, 2.0, 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * With a function that doesn't bracket a root.
     */
    @Test(expected = NoBracketException.class)
    public void testSolveException3() {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, -1.0});
        final BaseSecantSolver solver = new BaseSecantSolver(1e-9, BaseSecantSolver.Method.ILLINOIS);
        solver.solve(10, f, 1.0, 1.0, 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * With a function that doesn't converge.
     */
    @Test(expected = PrecisionExceededException.class)
    public void testSolveException4() {
        final UnivariateRealFunction f = x -> Math.sin(x) / x;
        final BaseSecantSolver solver = new BaseSecantSolver(1e-9, BaseSecantSolver.Method.ILLINOIS);
        solver.solve(10, f, -3.0, 3.0, 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * With an incorrect number of evaluations.
     */
    @Test(expected = MaxEvalExceededException.class)
    public void testSolveAllowedSolutionException2() {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, -1.0});
        final BaseSecantSolver solver = new BaseSecantSolver(1e-9, BaseSecantSolver.Method.ILLINOIS);
        solver.solve(10, f, -2.0, 2.0, 0.0, AllowedSolution.LEFT_SIDE);
        solver.solve(-1, f, -2.0, 2.0, 0.0, AllowedSolution.LEFT_SIDE);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * With an incorrect function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveException5() {
        final UnivariateFunction f = f -> Math.pow(f.getValue(), 2);
        final BaseSecantSolver solver = new BaseSecantSolver(1e-9, BaseSecantSolver.Method.ILLINOIS);
        solver.solve(10, (UnivariateRealFunction) f, -2.0, 2.0, 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * With an incorrect function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveAllowedSolutionException5() {
        final UnivariateFunction f = f -> Math.pow(f.getValue(), 2);
        final BaseSecantSolver solver = new BaseSecantSolver(1e-9, BaseSecantSolver.Method.ILLINOIS);
        solver.solve(10, (UnivariateRealFunction) f, -2.0, 2.0, 0.0, AllowedSolution.LEFT_SIDE);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * With a function that doesn't bracket a root.
     */
    @Test(expected = NoBracketException.class)
    public void testSolveAllowedSolutionException3() {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, 1.0, -1.0});
        final BaseSecantSolver solver = new BaseSecantSolver(1e-9, BaseSecantSolver.Method.ILLINOIS);
        solver.solve(10, f, -1.0, 1.0, 0.0, AllowedSolution.LEFT_SIDE);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * With a function that doesn't bracket a root.
     */
    @Test(expected = NoBracketException.class)
    public void testSolveAllowedSolutionException3a() {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, -1.0});
        final BaseSecantSolver solver = new BaseSecantSolver(1e-9, BaseSecantSolver.Method.ILLINOIS);
        solver.solve(10, f, 1.0, 1.0, 0.0, AllowedSolution.LEFT_SIDE);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * With an allowed solution that's not valid.
     */
    @Test
    public void testSolveAllowedSolutionException4() {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, -1.0});
        final BaseSecantSolver solver = new BaseSecantSolver(1e-9, BaseSecantSolver.Method.ILLINOIS);
        assertThrows(IllegalArgumentException.class, () -> solver.solve(10, f, -2.0, 2.0, 0.0, AllowedSolution.INVALID));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * With the function with discontinuities.
     */
    @Test
    public void testSolveDiscontinuity() {
        final UnivariateRealFunction f = x -> {
            if (x >= 0) {
                return Math.log(Math.abs(x) + 1.0);
            } else {
                throw new MathIllegalArgumentException("x < 0");
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(1e-9, BaseSecantSolver.Method.ILLINOIS);
        double x = solver.solve(10, f, -1.0, 1.0, 0.0, AllowedSolution.LEFT_SIDE);
        assertEquals(-1.0, x, 1e-9);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * With the function that has a vertical tangent.
     */
    @Test
    public void testSolveVerticalTangent() {
        final UnivariateRealFunction f = x -> x * x * x - 2.0;
        final BaseSecantSolver solver = new BaseSecantSolver(1e-9, BaseSecantSolver.Method.ILLINOIS);
        double x = solver.solve(10, f, -2.0, 2.0, 0.0, AllowedSolution.LEFT_SIDE);
        assertEquals(-1.5, x, 1e-9);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * With the function that has a root with multiplicity greater than 1.
     */
    @Test
    public void testSolveMultiplicityGreaterThanOne() {
        final UnivariateRealFunction f = x -> Math.pow(x - 1.0, 2.0);
        final BaseSecantSolver solver = new BaseSecantSolver(1e-9, BaseSecantSolver.Method.ILLINOIS);
        double x = solver.solve(10, f, -2.0, 2.0, 0.0, AllowedSolution.LEFT_SIDE);
        assertEquals(1.0, x, 1e-9);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * With the function that has an infinite root.
     */
    @Test(expected = ConvergenceException.class)
    public void testSolveInfiniteRoot() {
        final UnivariateRealFunction f = x -> 1.0 / x;
        final BaseSecantSolver solver = new BaseSecantSolver(1e-9, BaseSecantSolver.Method.ILLINOIS);
        double x = solver.solve(10, f, -2.0, 2.0, 0.0, AllowedSolution.LEFT_SIDE);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * With the function that has a root with multiplicity less than 1.
     */
    @Test
    public void testSolveRootWithMultiplicityLessThanOne() {
        final UnivariateRealFunction f = x -> Math.exp(x) - 2.0;
        final BaseSecantSolver solver = new BaseSecantSolver(1e-9, BaseSecantSolver.Method.ILLINOIS);
        double x = solver.solve(10, f, -3.0, 1.0, 0.0, AllowedSolution.LEFT_SIDE);
        assertEquals(-0.69314718056, x, 1e-5);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * With the function that has multiple roots.
     */
    @Test
    public void testSolveMultipleRoots() {
        final UnivariateRealFunction f = x -> Math.pow(x - 1.0, 2.0) * Math.sin(x);
        final BaseSecantSolver solver = new BaseSecantSolver(1e-9, BaseSecantSolver.Method.ILLINOIS);
        double[] roots = {-1.0, 0.5236};
        double[] solutions = new double[roots.length];
        for (int i = 0; i < roots.length; i++) {
            solutions[i] = solver.solve(10, f, -2.0, 2.0, 0.0, AllowedSolution.LEFT_SIDE);
        }
        assertArrayEquals(roots, solutions, 1e-9);
    }
}