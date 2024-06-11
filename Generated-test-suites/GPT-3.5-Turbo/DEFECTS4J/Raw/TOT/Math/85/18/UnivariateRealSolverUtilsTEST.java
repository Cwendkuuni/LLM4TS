package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        // Typical Use Case
        double solution1 = UnivariateRealSolverUtils.solve(x -> x - 2, 1, 3);
        assertEquals(2, solution1, 0);

        // Edge Cases
        double solution2 = UnivariateRealSolverUtils.solve(x -> x - 2, 2, 3);
        assertEquals(2, solution2, 0);

        double solution3 = UnivariateRealSolverUtils.solve(x -> x - 2, 1, 2);
        assertEquals(2, solution3, 0);

        // Error Scenarios
        try {
            UnivariateRealSolverUtils.solve(null, 1, 3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("function is null", e.getMessage());
        }

        try {
            UnivariateRealSolverUtils.solve(x -> x - 2, 3, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the endpoints do not specify a valid interval", e.getMessage());
        }
    }

    @Test
    public void testSolveWithAccuracy() {
        // Typical Use Case
        double solution1 = UnivariateRealSolverUtils.solve(x -> x - 2, 1, 3, 0.1);
        assertEquals(2, solution1, 0.1);

        // Edge Cases
        double solution2 = UnivariateRealSolverUtils.solve(x -> x - 2, 2, 3, 0.1);
        assertEquals(2, solution2, 0.1);

        double solution3 = UnivariateRealSolverUtils.solve(x -> x - 2, 1, 2, 0.1);
        assertEquals(2, solution3, 0.1);

        // Error Scenarios
        try {
            UnivariateRealSolverUtils.solve(null, 1, 3, 0.1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("function is null", e.getMessage());
        }

        try {
            UnivariateRealSolverUtils.solve(x -> x - 2, 3, 1, 0.1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("the endpoints do not specify a valid interval", e.getMessage());
        }

    }

    @Test
    public void testBracket() {
        // Typical Use Case
        double[] bracket1 = UnivariateRealSolverUtils.bracket(x -> x - 2, 3, 1, 4);
        assertEquals(2, bracket1[0], 0);
        assertEquals(4, bracket1[1], 0);

        // Edge Cases
        double[] bracket2 = UnivariateRealSolverUtils.bracket(x -> x - 2, 2, 1, 4);
        assertEquals(2, bracket2[0], 0);
        assertEquals(3, bracket2[1], 0);

        double[] bracket3 = UnivariateRealSolverUtils.bracket(x -> x - 2, 4, 1, 4);
        assertEquals(3, bracket3[0], 0);
        assertEquals(4, bracket3[1], 0);

        // Error Scenarios
        try {
            UnivariateRealSolverUtils.bracket(null, 1, 3, 4);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("function is null", e.getMessage());
        }

        try {
            UnivariateRealSolverUtils.bracket(x -> x - 2, 3, 4, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("invalid bracketing parameters:  lower bound=4.0,  initial=3.0, upper bound=1.0", e.getMessage());
        }

        try {
            UnivariateRealSolverUtils.bracket(x -> x - 2, 3, 1, 4, -1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("bad value for maximum iterations number: -1", e.getMessage());
        }
    }

    @Test
    public void testMidpoint() {
        assertEquals(2, UnivariateRealSolverUtils.midpoint(1, 3), 0);
        assertEquals(0, UnivariateRealSolverUtils.midpoint(-1, 1), 0);
        assertEquals(-2.5, UnivariateRealSolverUtils.midpoint(-1, -4), 0);
    }
}