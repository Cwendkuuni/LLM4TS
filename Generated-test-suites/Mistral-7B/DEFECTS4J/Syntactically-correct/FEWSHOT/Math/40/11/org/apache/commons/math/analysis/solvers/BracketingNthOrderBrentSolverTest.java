package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.exception.NoBracketingException;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testBracketingNthOrderBrentSolver() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();

        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2) - 0.5;
        double min = -3.0;
        double max = 3.0;

        try {
            double result = solver.solve(100, function, min, max, AllowedSolution.ANY_SIDE);
            assertEquals(Math.PI / 2, result, 0.001);
        } catch (NoBracketingException e) {
            fail("Bracketing not found.");
        }

        function = f -> f * f - 2;
        min = -2.0;
        max = 2.0;

        try {
            double result = solver.solve(100, function, min, max, AllowedSolution.RIGHT_SIDE);
            assertEquals(0.0, result, 0.001);
        } catch (NoBracketingException e) {
            fail("Bracketing not found.");
        }

        function = f -> Math.log(f) - Math.log(2);
        min = 0.0;
        max = 4.0;

        try {
            double result = solver.solve(100, function, min, max, AllowedSolution.LEFT_SIDE);
            assertEquals(1.0, result, 0.1);
        } catch (NoBracketingException e) {
            fail("Bracketing not found.");
        }

        function = f -> 1 / (f * f + 1);
        min = -2.0;
        max = 2.0;

        try {
            double result = solver.solve(100, function, min, max, AllowedSolution.BELOW_SIDE);
            assertEquals(-1.0, result, 0.01);
        } catch (NoBracketingException e) {
            fail("Bracketing not found.");
        }

        function = f -> 1 / (f * f + 1);
        min = -2.0;
        max = 2.0;

        try {
            double result = solver.solve(100, function, min, max, AllowedSolution.ABOVE_SIDE);
            assertEquals(1.0, result, 0.01);
        } catch (NoBracketingException e) {
            fail("Bracketing not found.");
        }
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmallException() {
        new BracketingNthOrderBrentSolver(0, 1);
    }
}