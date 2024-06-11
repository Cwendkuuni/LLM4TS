package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolveExample1() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        double root = solver.solve(100, f, -2, 2);
        assertEquals("The square root of 2 should be approximately 1.41421356", 1.41421356d, root, 1e-8);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.sin(x);
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        solver.solve(100, f, -Math.PI, Math.PI);
        fail("A NoBracketingException should have been thrown");
    }

    @Test
    public void testMaxEvalExceededException() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.log(1 / (1 - x));
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        try {
            solver.solve(0, f, 0, 1);
            fail("A MaxEvalExceededException should have been thrown");
        } catch (MaxEvalExceededException e) {
            // expected
        }
    }

    @Test
    public void testAllowedSolution() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5, AllowedSolution.LEFT_SIDE);
        double root = solver.solve(100, f, -5, 0);
        assertEquals("The left root of x^2 - 4 should be approximately -2", -2, root, 1e-8);
    }

}