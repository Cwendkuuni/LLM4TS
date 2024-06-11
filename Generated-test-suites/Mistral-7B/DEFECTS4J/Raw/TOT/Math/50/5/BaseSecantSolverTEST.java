package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.Assert.*;

public class BaseSecantSolverTest {

    /**
     * Test the solver with a simple function.
     *
     * @throws UnsupportedOperationException if the solver is not bracketing
     * @throws NoBracketingException        if the solver cannot find a bracketing
     * @throws MaxEvalExceededException      if the solver fails to find a root within
     *                                       the specified maximum number of evaluations
     */
    @Test
    public void testSimpleFunction() {
        UnivariateRealFunction function = f -> Math.pow(f, 2) - 1;
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, function);
        double root = solver.solve(100, function, -2.0, 2.0);
        assertEquals(0.0, root, 1e-8);
    }

    /**
     * Test the solver with a function having no roots.
     *
     * @throws UnsupportedOperationException if the solver is not bracketing
     * @throws NoBracketingException        if the solver cannot find a bracketing
     * @throws MaxEvalExceededException      if the solver fails to find a root within
     *                                       the specified maximum number of evaluations
     */
    @Test(expected = NoBracketingException.class)
    public void testFunctionWithNoRoots() {
        UnivariateRealFunction function = f -> Math.sin(f) / f;
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, function);
        solver.solve(100, function, -10.0, 10.0);
    }

    /**
     * Test the solver with a function having multiple roots.
     *
     * @throws UnsupportedOperationException if the solver is not bracketing
     * @throws NoBracketingException        if the solver cannot find a bracketing
     * @throws MaxEvalExceededException      if the solver fails to find a root within
     *                                       the specified maximum number of evaluations
     */
    @Test
    public void testFunctionWithMultipleRoots() {
        UnivariateRealFunction function = f -> Math.pow(f, 2) - 4;
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, function);
        double root1 = solver.solve(100, function, -2.0, 2.0);
        double root2 = solver.solve(100, function, 2.0, 4.0);
        assertEquals(2.0, root1, 1e-8);
        assertEquals(-2.0, root2, 1e-8);
    }

    /**
     * Test the solver with a function having a vertical asymptote.
     *
     * @throws UnsupportedOperationException if the solver is not bracketing
     * @throws NoBracketingException        if the solver cannot find a bracketing
     * @throws MaxEvalExceededException      if the solver fails to find a root within
     *                                       the specified maximum number of evaluations
     */
    @Test(expected = NoBracketingException.class)
    public void testFunctionWithVerticalAsymptote() {
        UnivariateRealFunction function = f -> 1.0 / (f - 2);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, function);
        solver.solve(100, function, -5.0, 5.0);
    }

    /**
     * Test the solver with a function having a horizontal asymptote.
     *
     * @throws UnsupportedOperationException if the solver is not bracketing
     * @throws NoBracketingException        if the solver cannot find a bracketing
     * @throws MaxEvalExceededException      if the solver fails to find a root within
     *                                       the specified maximum number of evaluations
     */
    @Test(expected = NoBracketingException.class)
    public void testFunctionWithHorizontalAsymptote() {
        UnivariateRealFunction function = f -> Math.cos(f) / f;
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, function);
        solver.solve(100, function, -10.0, 10.0);
    }

    /**
     * Test the solver with a function having a local maximum.
     *
     * @throws UnsupportedOperationException if the solver is not bracketing
     * @throws NoBracketingException        if the solver cannot find a bracketing
     * @throws MaxEvalExceededException      if the solver fails to find a root within
     *                                       the specified maximum number of evaluations
     */
    @Test
    public void testFunctionWithLocalMaximum() {
        UnivariateRealFunction function = f -> Math.pow(f, 2) + 2;
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, function);
        double root = solver.solve(100, function, -3.0, 3.0);
        assertEquals(-1.0, root, 1e-8);
    }

    /**
     * Test the solver with a function having a local minimum.
     *
     * @throws UnsupportedOperationException if the solver is not bracketing
     * @throws NoBracketingException        if the solver cannot find a bracketing
     * @throws MaxEvalExceededException      if the solver fails to find a root within
     *                                       the specified maximum number of evaluations
     */
    @Test
    public void testFunctionWithLocalMinimum() {
        UnivariateRealFunction function = f -> Math.pow(f, 2) - 1;
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, function);
        double root = solver.solve(100, function, -2.0, 2.0);
        assertEquals(0.0, root, 1e-8);
    }

    /**
     * Test the solver with a function having a root at a boundary.
     *
     * @throws UnsupportedOperationException if the solver is not bracketing
     * @throws NoBracketingException        if the solver cannot find a bracketing
     * @throws MaxEvalExceededException      if the solver fails to find a root within
     *                                       the specified maximum number of evaluations
     */
    @Test
    public void testFunctionWithRootAtBoundary() {
        UnivariateRealFunction function = f -> Math.pow(f, 2) - 1;
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, function);
        double root = solver.solve(100, function, -1.0, 1.0);
        assertEquals(-1.0, root, 1e-8);
    }

    /**
     * Test the solver with a function having a root at a specified accuracy.
     *
     * @throws UnsupportedOperationException if the solver is not bracketing
     * @throws NoBracketingException        if the solver cannot find a bracketing
     * @throws MaxEvalExceededException      if the solver fails to find a root within
     *                                       the specified maximum number of evaluations
     */
    @Test
    public void testFunctionWithRootAtGivenAccuracy() {
        UnivariateRealFunction function = f -> Math.pow(f, 2) - 1;
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-9, function);
        double root = solver.solve(100, function, -1.0, 1.0);
        assertEquals(-1.0, root, 1e-10);
    }
}