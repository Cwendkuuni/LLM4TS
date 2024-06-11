package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testBrentSolverWithLinearFunction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x - 1;
            }
        };
        double result = solver.solve(100, function, -5, 5);
        assertEquals(0.0, result, 1e-6);
    }

    @Test(expected = NoBracketingException.class)
    public void testBrentSolverWithFunctionOutsideBrackets() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - Math.cos(x);
            }
        };
        solver.solve(100, function, -10, 10);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentSolverWithNegativeMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, -3);
    }

    @Test
    public void testBrentSolverWithAllowedSolutionLeftSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5, AllowedSolution.LEFT_SIDE);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        double result = solver.solve(100, function, -3, 0);
        assertEquals(-1.0, result, 1e-6);
    }

    @Test
    public void testBrentSolverWithAllowedSolutionRightSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5, AllowedSolution.RIGHT_SIDE);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        double result = solver.solve(100, function, 0, 3);
        assertEquals(1.0, result, 1e-6);
    }

    @Test
    public void testBrentSolverWithAllowedSolutionBelowSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5, AllowedSolution.BELOW_SIDE);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        double result = solver.solve(100, function, -3, 0);
        assertEquals(-1.0, result, 1e-6);
    }

    @Test
    public void testBrentSolverWithAllowedSolutionAboveSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5, AllowedSolution.ABOVE_SIDE);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        double result = solver.solve(100, function, 0, 3);
        assertEquals(1.0, result, 1e-6);
    }

}