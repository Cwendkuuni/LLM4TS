package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with the {@link RegulaFalsiSolver} method.
     */
    @Test
    public void testRegulaFalsiSolver() {
        final UnivariateFunction f = new PolynomialFunction(2, -1, 1, 1);
        final double absTol = 1e-6;
        final double relTol = 1e-6;
        final BaseSecantSolver solver = new RegulaFalsiSolver(absTol, relTol);
        final double root = solver.solve(100, f, -10, 10, 0.0);
        assertEquals(0.0, root, absTol);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with the {@link IllinoisSolver} method.
     */
    @Test
    public void testIllinoisSolver() {
        final UnivariateFunction f = new PolynomialFunction(3, -1, 1, 1, 1);
        final double absTol = 1e-6;
        final double relTol = 1e-6;
        final BaseSecantSolver solver = new IllinoisSolver(absTol, relTol);
        final double root = solver.solve(100, f, -10, 10, 0.0);
        assertEquals(0.0, root, absTol);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with the {@link PegasusSolver} method.
     */
    @Test
    public void testPegasusSolver() {
        final UnivariateFunction f = new PolynomialFunction(4, -1, 1, 1, 1, 1);
        final double absTol = 1e-6;
        final double relTol = 1e-6;
        final BaseSecantSolver solver = new PegasusSolver(absTol, relTol);
        final double root = solver.solve(100, f, -10, 10, 0.0);
        assertEquals(0.0, root, absTol);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with no bracketing.
     */
    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() {
        final UnivariateFunction f = new PolynomialFunction(1, 1);
        final double absTol = 1e-6;
        final double relTol = 1e-6;
        final BaseSecantSolver solver = new RegulaFalsiSolver(absTol, relTol);
        solver.solve(100, f, -1, 1, 0.0);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with a function that returns infinite values.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInfiniteFunction() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Double.POSITIVE_INFINITY;
            }
        };
        final double absTol = 1e-6;
        final double relTol = 1e-6;
        final BaseSecantSolver solver = new RegulaFalsiSolver(absTol, relTol);
        solver.solve(100, f, -10, 10, 0.0);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with a function that throws an exception.
     */
    @Test
    public void testExceptionFunction() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new RuntimeException("Exception");
            }
        };
        final double absTol = 1e-6;
        final double relTol = 1e-6;
        final BaseSecantSolver solver = new RegulaFalsiSolver(absTol, relTol);
        assertThrows(MaxEvalException.class, () -> solver.solve(100, f, -10, 10, 0.0));
    }
}