package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.analysis.UnivariateFunction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BracketingNthOrderBrentSolverTest {

    private BracketingNthOrderBrentSolver solver;

    @Before
    public void setUp() {
        solver = new BracketingNthOrderBrentSolver();
    }

    @Test
    public void testSolve() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        double result = solver.solve(100, function, 1, 10);
        assertTrue("result = " + result, result > 2.0 && result < 3.0);
    }

    @Test
    public void testSolveWithStartValue() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        double result = solver.solve(100, function, 1, 10, 2);
        assertEquals("result = " + result, 2.0, result, 1e-6);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolveWithNoBracketing() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x * x + 4;
            }
        };

        solver.solve(100, function, 1, 10);
    }

}