package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testBracketingNthOrderBrentSolver() throws Exception {
        // Test default constructor
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertNotNull(solver);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-10);
        assertEquals(5, solver.getMaximalOrder());

        // Test constructor with specified accuracy and maximal order
        solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        assertNotNull(solver);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 1e-12);
        assertEquals(7, solver.getMaximalOrder());

        // Test constructor with specified relative and absolute accuracy, maximal order
        solver = new BracketingNthOrderBrentSolver(1e-3, 1e-5, 6);
        assertNotNull(solver);
        assertEquals(1e-3, solver.getRelativeAccuracy(), 1e-6);
        assertEquals(1e-5, solver.getAbsoluteAccuracy(), 1e-10);
        assertEquals(6, solver.getMaximalOrder());

        // Test constructor with specified relative, absolute accuracy, function value accuracy, and maximal order
        solver = new BracketingNthOrderBrentSolver(1e-4, 1e-7, 1e-9, 5);
        assertNotNull(solver);
        assertEquals(1e-4, solver.getRelativeAccuracy(), 1e-6);
        assertEquals(1e-7, solver.getAbsoluteAccuracy(), 1e-11);
        assertEquals(1e-9, solver.getFunctionValueAccuracy(), 1e-12);
        assertEquals(5, solver.getMaximalOrder());

        // Test method for getting maximal order
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, -2, 1}, new double[]{1, 0, 0, 1});
        BracketingNthOrderBrentSolver solver1 = new BracketingNthOrderBrentSolver(1e-4, 1e-7, 5);
        assertEquals(5, solver1.getMaximalOrder());

        // Test solve method with empty interval
        try {
            double root = solver1.solve(100000, f, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, null);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected exception
        }

        // Test solve method with interval that does not bracket the root
        double min = -2.5;
        double max = -1.5;
        try {
            double root = solver1.solve(100000, f, min, max, null);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected exception
        }

        // Test solve method with interval that brackets the root
        min = -1.8;
        max = -1.2;
        UnivariateFunction fTest = new PolynomialFunction(new double[]{1, -2, 1}, new double[]{0, 1, 0});
        double rootTest = solver1.solve(100000, fTest, min, max, null);
        assertEquals(-1.5, rootTest, 1e-6);

        // Test solve method with start value that is the root
        double startValue = -1.5;
        double rootSolved = solver1.solve(100000, fTest, min, max, startValue, null);
        assertEquals(-1.5, rootSolved, 1e-6);

        // Test solve method with maxEval exceeded
        solver1 = new BracketingNthOrderBrentSolver(1e-4, 1e-7, 5);
        try {
            double root = solver1.solve(5, f, min, max, null);
            fail("Expected MaxEvalExceededException");
        } catch (MaxEvalExceededException e) {
            // Expected exception
        }

        // Test solve method with allowed solution
        BracketingNthOrderBrentSolver solver2 = new BracketingNthOrderBrentSolver(1e-4, 1e-7, 5, AllowedSolution.BELOW_SIDE);
        double rootBelow = solver2.solve(100000, f, min, max, null);
        assertEquals(-1.8, rootBelow, 1e-6);

        BracketingNthOrderBrentSolver solver3 = new BracketingNthOrderBrentSolver(1e-4, 1e-7, 5, AllowedSolution.ABOVE_SIDE);
        double rootAbove = solver3.solve(100000, f, min, max, null);
        assertEquals(-1.2, rootAbove, 1e-6);

        // Test solve method with allowed solution and start value
        double startValueBelow = -1.7;
        double rootBelowStart = solver2.solve(100000, f, min, max, startValueBelow, AllowedSolution.BELOW_SIDE);
        assertEquals(-1.8, rootBelowStart, 1e-6);

        double startValueAbove = -1.3;
        double rootAboveStart = solver3.solve(100000, f, min, max, startValueAbove, AllowedSolution.ABOVE_SIDE);
        assertEquals(-1.2, rootAboveStart, 1e-6);

        // Test solve method with allowed solution, start value, and maxEval
        int maxEvalBelow = 10;
        double rootBelowMaxEval = solver2.solve(maxEvalBelow, f, min, max, startValueBelow, AllowedSolution.BELOW_SIDE);
        assertEquals(-1.8, rootBelowMaxEval, 1e-6);

        int maxEvalAbove = 10;
        double rootAboveMaxEval = solver3.solve(maxEvalAbove, f, min, max, startValueAbove, AllowedSolution.ABOVE_SIDE);
        assertEquals(-1.2, rootAboveMaxEval, 1e-6);

        // Test solve method with default allowed solution
        double rootDefault = solver1.solve(100000, f, min, max, null);
        assertEquals(-1.5, rootDefault, 1e-6);
    }
}