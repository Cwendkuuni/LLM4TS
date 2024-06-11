package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeError;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(5, solver.getMaximalOrder());

        BracketingNthOrderBrentSolver solver1 = new BracketingNthOrderBrentSolver(1e-5, 4);
        assertEquals(1e-5, solver1.getAbsoluteAccuracy(), 1e-9);
        assertEquals(4, solver1.getMaximalOrder());

        BracketingNthOrderBrentSolver solver2 = new BracketingNthOrderBrentSolver(0.001, 2.5, 1e-6, 3);
        assertEquals(0.001, solver2.getRelativeAccuracy(), 1e-9);
        assertEquals(1e-6, solver2.getAbsoluteAccuracy(), 1e-9);
        assertEquals(3, solver2.getMaximalOrder());
    }

    @Test
    public void testSolveWithDefaults() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{1, 2, 1});
        double root = solver.solve(100, f, -2, 2);
        assertEquals(0.0, root, 1e-9);
    }

    @Test
    public void testSolveWithStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{1, 2, 1});
        double root = solver.solve(100, f, -2, 2, 1.5);
        assertEquals(0.0, root, 1e-9);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 0);
    }

    @Test(expected = MathRuntimeError.class)
    public void testNoBracketing() {
        UnivariateFunction f = new PolynomialFunction(new double[]{1}, new double[]{0});
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 3);
        try {
            solver.solve(100, f, -1, 1);
        } catch (NoBracketingException e) {
            throw new AssertionError("Unexpected NoBracketingException", e);
        }
    }

    @Test
    public void testSolveWithDifferentAllowedSolutions() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1}, new double[]{1, 2});
        double root = solver.solve(100, f, -2, 2, AllowedSolution.LEFT_SIDE);
        assertEquals(-1.0, root, 1e-9);

        root = solver.solve(100, f, -2, 2, AllowedSolution.RIGHT_SIDE);
        assertEquals(1.0, root, 1e-9);

        root = solver.solve(100, f, -2, 2, AllowedSolution.BELOW_SIDE);
        assertEquals(-1.0, root, 1e-9);

        root = solver.solve(100, f, -2, 2, AllowedSolution.ABOVE_SIDE);
        assertEquals(1.0, root, 1e-9);

        root = solver.solve(100, f, -2, 2, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-9);
    }

    @Test
    public void testSolveWithDifferentRelativeAndAbsoluteAccuracy() {
        BracketingNthOrderBrentSolver solver1 = new BracketingNthOrderBrentSolver(1e-5, 3);
        BracketingNthOrderBrentSolver solver2 = new BracketingNthOrderBrentSolver(1e-6, 3);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1}, new double[]{1, 2});

        double root1 = solver1.solve(100, f, -2, 2);
        double root2 = solver2.solve(100, f, -2, 2);

        assertEquals(root1, root2, 1e-12);
    }
}