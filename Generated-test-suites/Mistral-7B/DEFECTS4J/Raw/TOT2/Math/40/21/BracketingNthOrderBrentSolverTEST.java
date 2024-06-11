package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testBracketingNthOrderBrentSolver_basic() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        double root = solver.solve(100, function, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals("Root not found", 0.0, root, 1e-7);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBracketingNthOrderBrentSolver_orderTooSmall() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 0);
        solver.solve(100, function, -10.0, 10.0, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testBracketingNthOrderBrentSolver_allowedSolutions() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);

        double root = solver.solve(100, function, -2.0, 2.0, AllowedSolution.LEFT_SIDE);
        assertEquals("Root not found", 0.0, root, 1e-7);

        root = solver.solve(100, function, -2.0, 2.0, AllowedSolution.RIGHT_SIDE);
        assertEquals("Root not found", 0.0, root, 1e-7);

        root = solver.solve(100, function, -2.0, 2.0, AllowedSolution.BELOW_SIDE);
        assertTrue("Root is above expected", root < 0.0);

        root = solver.solve(100, function, -2.0, 2.0, AllowedSolution.ABOVE_SIDE);
        assertTrue("Root is below expected", root > 0.0);
    }

    @Test
    public void testBracketingNthOrderBrentSolver_functionValueAccuracy() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - Math.E;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5, 1e-8, 5);
        double root = solver.solve(100, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
        assertEquals("Root not found", 0.0, root, 1e-8);
    }

}