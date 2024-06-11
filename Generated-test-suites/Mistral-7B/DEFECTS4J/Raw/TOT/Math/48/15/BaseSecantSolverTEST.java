package org.apache.commons.math.analysis.solvers.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math.analysis.solvers.IlinoisSolver;
import org.apache.commons.math.analysis.solvers.PegasusSolver;

/**
 * Tests for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test solver with a polynomial function.
     * @throws Exception if an error occurs
     */
    @Test
    public void testPolynomialFunction() throws Exception {
        final double tol = 1e-12;

        final UnivariateFunction f = new PolynomialFunction(3,
                                                          new double[] {1.0, 1.0, -2.0});
        final BracketedUnivariateRealSolver<UnivariateFunction> solver =
                new RegulaFalsiSolver(tol);

        final double root = 1.0;
        assertEquals(root, solver.solve(f, 0.0, 2.0), tol);
    }

    /**
     * Test solver with a function that has two real roots.
     * @throws Exception if an error occurs
     */
    @Test
    public void testFunctionWithTwoRealRoots() throws Exception {
        final double tol = 1e-12;

        final UnivariateFunction f = new PolynomialFunction(3,
                                                          new double[] {1.0, 1.0, 2.0});
        final BracketedUnivariateRealSolver<UnivariateFunction> solver =
                new IllinoisSolver(tol);

        final double root1 = 1.0;
        final double root2 = 2.0;
        assertEquals(root1, solver.solve(f, 0.0, 3.0), tol);
        assertEquals(root2, solver.solve(f, 2.0, 4.0), tol);
    }

    /**
     * Test solver with a function that has no real roots.
     * @throws Exception if an error occurs
     */
    @Test
    public void testFunctionWithNoRealRoots() throws Exception {
        final double tol = 1e-12;

        final UnivariateFunction f = new PolynomialFunction(3,
                                                          new double[] {1.0, 1.0, -1.0, 1.0});
        final BracketedUnivariateRealSolver<UnivariateFunction> solver =
                new PegasusSolver(tol);

        assertThrows(ZeroException.class, () -> solver.solve(f, 0.0, 2.0));
    }

    /**
     * Test solver with a function that has a complex root.
     * @throws Exception if an error occurs
     */
    @Test
    public void testFunctionWithComplexRoot() throws Exception {
        final double tol = 1e-12;

        final UnivariateFunction f = new PolynomialFunction(4,
                                                          new double[] {1.0, 1.0, -2.0, 3.0, 4.0});
        final BracketedUnivariateRealSolver<UnivariateFunction> solver =
                new RegulaFalsiSolver(tol);

        final double root = MathUtils.sqrt(2.0) / 2.0;
        assertThrows(ComplexNumberException.class, () -> {
            assertEquals(root, solver.solve(f, 0.0, 2.0), tol);
        });
    }

    /**
     * Test solver with a function that has a singularity.
     * @throws Exception if an error occurs
     */
    @Test
    public void testFunctionWithSingularity() throws Exception {
        final double tol = 1e-12;

        final UnivariateFunction f = new PolynomialFunction(3,
                                                          new double[] {1.0, 1.0, -1.0});
        final BracketedUnivariateRealSolver<UnivariateFunction> solver =
                new RegulaFalsiSolver(tol);

        final double root = 0.0;
        assertThrows(ConvergenceException.class, () -> solver.solve(f, -1.0, 0.0));
    }

    /**
     * Test solver with a function that has a local maximum.
     * @throws Exception if an error occurs
     */
    @Test
    public void testFunctionWithLocalMaximum() throws Exception {
        final double tol = 1e-12;

        final UnivariateFunction f = new PolynomialFunction(3,
                                                          new double[] {1.0, 1.0, 0.1});
        final BracketedUnivariateRealSolver<UnivariateFunction> solver =
                new IllinoisSolver(tol);

        final double root = 1.5;
        assertThrows(MaxIterationsExceededException.class, () -> {
            assertEquals(root, solver.solve(f, 0.0, 2.0), tol);
        });
    }

    /**
     * Test solver with a function that has a local minimum.
     * @throws Exception if an error occurs
     */
    @Test
    public void testFunctionWithLocalMinimum() throws Exception {
        final double tol = 1e-12;

        final UnivariateFunction f = new PolynomialFunction(3,
                                                          new double[] {1.0, 1.0, -0.1});
        final BracketedUnivariateRealSolver<UnivariateFunction> solver =
                new PegasusSolver(tol);

        final double root = 1.0;
        assertEquals(root, solver.solve(f, 0.0, 2.0), tol);
    }

    /**
     * Test solver with a function that has a vertical asymptote.
     * @throws Exception if an error occurs
     */
    @Test
    public void testFunctionWithVerticalAsymptote() throws Exception {
        final double tol = 1e-12;

        final UnivariateFunction f = new PolynomialFunction(3,
                                                          new double[] {1.0, 1.0, 0.0});
        final BracketedUnivariateRealSolver<UnivariateFunction> solver =
                new PegasusSolver(tol);

        assertThrows(PrecisionException.class, () -> solver.solve(f, 0.0, 1.0));
    }
}