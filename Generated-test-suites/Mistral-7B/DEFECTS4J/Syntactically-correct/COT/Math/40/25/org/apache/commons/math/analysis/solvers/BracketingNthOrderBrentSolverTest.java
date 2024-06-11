package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithLowerMaximalOrder() {
        new BracketingNthOrderBrentSolver(1);
    }

    @Test
    public void testSolverWithDefaultParameters() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double solution = solver.solve(100, function, -2.5, 2.5);
        assertEquals(0, solution, 1e-6);
    }

    @Test
    public void testSolverWithCustomParameters() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 3);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        double solution = solver.solve(100, function, -5, 5);
        assertEquals(0, solution, 1e-8);
    }

    @Test
    public void testSolverWithCustomStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 3);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };
        double solution = solver.solve(100, function, -3, 3, -1);
        assertEquals(0, solution, 1e-8);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolverWithNoBracketing() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 3);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.cos(x);
            }
        };
        solver.solve(100, function, -1, 1);
    }

    @Test
    public void testAllowedSolutionRightSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 3, AllowedSolution.RIGHT_SIDE);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x - 2;
            }
        };
        double solution = solver.solve(100, function, -3, 1);
        assertEquals(1, solution, 1e-8);
    }

    @Test
    public void testAllowedSolutionLeftSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 3, AllowedSolution.LEFT_SIDE);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 2 - x;
            }
        };
        double solution = solver.solve(100, function, -3, 1);
        assertEquals(-1, solution, 1e-8);
    }

    @Test
    public void testAllowedSolutionBelowSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 3, AllowedSolution.BELOW_SIDE);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double solution = solver.solve(100, function, -3, 1);
        assertEquals(-2, solution, 1e-8);
    }

    @Test
    public void testAllowedSolutionAboveSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 3, AllowedSolution.ABOVE_SIDE);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 4 - x * x;
            }
        };
        double solution = solver.solve(100, function, -3, 1);
        assertEquals(2, solution, 1e-8);
    }

}