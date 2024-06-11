package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.interpolators.PolynomialInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testBracketingNthOrderBrentSolver() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);

        assertEquals(5, solver.getMaximalOrder());
        assertEquals(AllowedSolution.ANY_SIDE, solver.getAllowedSolution());

        UnivariateFunction testFunction = new PolynomialFunction(new double[]{-1, 2, -1, 1, 0});
        double min = -5;
        double max = 5;
        double[] roots = {-3.0, 1.0};

        double[] results;
        try {
            results = solver.solve(100, testFunction, min, max);
        } catch (MaxEvalException e) {
            results = e.getSolutions();
        }

        assertArrayEquals(roots.length, results.length);
        for (int i = 0; i < roots.length; i++) {
            assertEquals(roots[i], results[i], 1e-7);
        }

        // Test allowed solution
        solver = new BracketingNthOrderBrentSolver(1e-6, 5, AllowedSolution.RIGHT_SIDE);
        results = solver.solve(100, testFunction, min, max);
        for (int i = 0; i < roots.length; i++) {
            assertTrue(results[i] > max);
        }

        // Test with no bracketing
        solver = new BracketingNthOrderBrentSolver(1e-6, 5, AllowedSolution.ANY_SIDE);
        testFunction = new PolynomialFunction(new double[]{0, 0, 1, 0, 0});
        min = 1;
        max = 1;
        assertThrows(NoBracketingException.class, () -> solver.solve(100, testFunction, min, max));

        // Test with invalid order
        solver = new BracketingNthOrderBrentSolver(-1, 5, AllowedSolution.ANY_SIDE);
        assertThrows(NumberIsTooSmallException.class, () -> solver);
    }

    @Test
    public void testBrentSolverWithFunctionValueAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5, 1e-8);

        UnivariateFunction testFunction = new PolynomialFunction(new double[]{-1, 2, -1, 1, 0});
        double min = -5;
        double max = 5;

        double[] results = solver.solve(100, testFunction, min, max);

        assertArrayEquals(roots.length, results.length);
        for (int i = 0; i < roots.length; i++) {
            assertEquals(roots[i], results[i], 1e-8);
        }
    }

    @Test
    public void testBrentSolverWithRelativeAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.01, 5, 1e-6);

        UnivariateFunction testFunction = new PolynomialFunction(new double[]{-1, 2, -1, 1, 0});
        double min = -5;
        double max = 5;

        double[] results = solver.solve(100, testFunction, min, max);

        assertArrayEquals(roots.length, results.length);
        for (int i = 0; i < roots.length; i++) {
            assertEquals(roots[i], results[i], 1.01 * Math.abs(roots[i]));
        }
    }

    @Test
    public void testBrentSolverWithAbsoluteAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5, 1e-6);

        UnivariateFunction testFunction = new PolynomialFunction(new double[]{-1, 2, -1, 1, 0});
        double min = -5;
        double max = 5;

        double[] results = solver.solve(100, testFunction, min, max);

        assertArrayEquals(roots.length, results.length);
        for (int i = 0; i < roots.length; i++) {
            assertEquals(roots[i], results[i], 1e-6);
        }
    }
}