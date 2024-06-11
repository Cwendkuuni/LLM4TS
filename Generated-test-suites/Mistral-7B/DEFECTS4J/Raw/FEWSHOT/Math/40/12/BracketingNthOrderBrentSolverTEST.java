package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(1, null, 0, 1, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(1, x -> x * x, 0, 1, AllowedSolution.ANY_SIDE, -1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testZeroMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(1, x -> x * x, 0, 1, AllowedSolution.ANY_SIDE, 0);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = x -> x * x - 4;
        solver.solve(1, f, 2, 3, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testBracketingSolve() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        UnivariateFunction f = x -> x * x - 4;
        double root = solver.solve(10, f, -2, 2, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-8);
    }

    @Test
    public void testBracketingSolveWithStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        UnivariateFunction f = x -> x * x - 4;
        double root = solver.solve(10, f, -2, 2, 1.5, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-8);
    }

    @Test
    public void testBracketingSolveWithRelativeAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.01, 5);
        UnivariateFunction f = x -> Math.sqrt(x) - 2;
        double root = solver.solve(10, f, 0, 10, AllowedSolution.ANY_SIDE);
        assertEquals(2.0, root, 1e-4);
    }

    @Test
    public void testBracketingSolveWithAbsoluteAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.0, 5);
        UnivariateFunction f = x -> Math.sqrt(x) - 2;
        double root = solver.solve(10, f, 0, 10, AllowedSolution.ANY_SIDE, 0.001);
        assertEquals(2.0, root, 1e-4);
    }

    @Test
    public void testBracketingSolveWithFunctionValueAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.01, 5);
        UnivariateFunction f = new PolynomialFunction(new double[] {1, 2, -3}, new double[] {0, 0, 1});
        double root = solver.solve(10, f, -5, 5, AllowedSolution.ANY_SIDE, 1e-6);
        assertEquals(1.0, root, 1e-6);
    }

}