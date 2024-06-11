package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testConstructorWithDefaultValues() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-10);
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testConstructorWithCustomValues() {
        double relativeAccuracy = 1e-4;
        double absoluteAccuracy = 1e-7;
        double functionValueAccuracy = 1e-8;
        int maximalOrder = 8;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(relativeAccuracy, absoluteAccuracy,
                functionValueAccuracy, maximalOrder);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 1e-10);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 1e-10);
        assertEquals(functionValueAccuracy, solver.getFunctionValueAccuracy(), 1e-10);
        assertEquals(maximalOrder, solver.getMaximalOrder());
    }

    @Test
    public void testConstructorWithLowerMaximalOrder() {
        assertThrows(NumberIsTooSmallException.class, () -> new BracketingNthOrderBrentSolver(1));
    }

    @Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(5, solver.getMaximalOrder());

        BracketingNthOrderBrentSolver solverWithCustomMaximalOrder = new BracketingNthOrderBrentSolver(1e-4, 1e-7, 1e-8, 8);
        assertEquals(8, solverWithCustomMaximalOrder.getMaximalOrder());
    }

    @Test
    public void testSolveWithFunctionBracketingTheRoot() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };
        double root = solver.solve(100, function, -2, 2).getResult();
        assertEquals(0, root, 1e-6);
    }

    @Test
    public void testSolveWithFunctionNotBracketingTheRoot() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        assertThrows(NoBracketingException.class, () -> solver.solve(100, function, -5, 5));
    }

    @Test
    public void testSolveWithFunctionBracketingTheRootOnDifferentSides() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2, 3) - 2;
            }
        };
        double root = solver.solve(100, function, -5, 0).getResult();
        assertEquals(-2, root, 1e-6);

        root = solver.solve(100, function, 0, 5).getResult();
        assertEquals(2, root, 1e-6);
    }

    @Test
    public void testSolveWithFunctionBracketingTheRootAboveBelow() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1, 2) + 1;
            }
        };
        double root = solver.solve(100, function, -5, 5, AllowedSolution.ABOVE_SIDE).getResult();
        assertEquals(1, root, 1e-6);

        root = solver.solve(100, function, -5, 5, AllowedSolution.BELOW_SIDE).getResult();
        assertEquals(1, root, 1e-6);
    }

    @Test
    public void testSolveWithFunctionBracketingTheRootOnDifferentSidesWithCustomAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2, 3) - 2;
            }
        };
        double root = solver.solve(100, function, -5, 0, AllowedSolution.RIGHT_SIDE).getResult();
        assertEquals(2, root, 1e-6);

        root = solver.solve(100, function, 0, 5, AllowedSolution.LEFT_SIDE).getResult();
        assertEquals(-2, root, 1e-6);
    }

    @Test
    public void testSolveWithFunctionBracketingTheRootOnLeftSideWithCustomAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 3, 2) + 1;
            }
        };
        double root = solver.solve(100, function, -10, 2, AllowedSolution.LEFT_SIDE).getResult();
        assertEquals(3, root, 1e-6);
    }

    @Test
    public void testSolveWithFunctionBracketingTheRootOnRightSideWithCustomAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 4, 2) + 1;
            }
        };
        double root = solver.solve(100, function, 2, 10, AllowedSolution.RIGHT_SIDE).getResult();
        assertEquals(4, root, 1e-6);
    }

    @Test
    public void testSolveWithFunctionBracketingTheRootOnBothSidesWithCustomAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 5, 2) - 1;
            }
        };
        double root = solver.solve(100, function, -10, 10, AllowedSolution.ANY_SIDE).getResult();
        assertEquals(5, root, 1e-6);
    }

    @Test
    public void testSolveWithFunctionWithInversePolynomialInterpolation() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 6, 2) + Math.sin(x) - 1;
            }
        };
        double root = solver.solve(100, function, -10, 10).getResult();
        assertEquals(6, root, 1e-6);
    }

    @Test
    public void testSolveWithFunctionWithInversePolynomialInterpolationWithCustomAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 7, 2) + Math.sin(x) - 1;
            }
        };
        double root = solver.solve(100, function, -10, 10, AllowedSolution.ANY_SIDE).getResult();
        assertEquals(7, root, 1e-6);

        root = solver.solve(100, function, -10, 10, AllowedSolution.BELOW_SIDE).getResult();
        assertTrue(root > 7);

        root = solver.solve(100, function, -10, 10, AllowedSolution.ABOVE_SIDE).getResult();
        assertTrue(root < 7);
    }

    @Test
    public void testGuessXWithTwoPoints() {
        double x0 = 0;
        double y0 = 1;
        double x1 = 2;
        double y1 = 4;
        double targetY = 3;

        double[] x = new double[3];
        double[] y = new double[3];
        x[0] = x0;
        x[1] = x1;
        y[0] = y0;
        y[1] = y1;

        double guessedX = BracketingNthOrderBrentSolver.guessX(targetY, x, y, 0, 2);
        assertEquals(1.5, guessedX, 1e-6);
    }

    @Test
    public void testGuessXWithThreePoints() {
        double x0 = 0;
        double y0 = 1;
        double x1 = 2;
        double y1 = 4;
        double x2 = 5;
        double y2 = 7;
        double targetY = 5;

        double[] x = new double[4];
        double[] y = new double[4];
        x[0] = x0;
        x[1] = x1;
        x[2] = x2;
        y[0] = y0;
        y[1] = y1;
        y[2] = y2;

        double guessedX = BracketingNthOrderBrentSolver.guessX(targetY, x, y, 0, 3);
        assertEquals(3, guessedX, 1e-6);
    }

    @Test
    public void testGuessXWithPointsWithSameY() {
        double x0 = 0;
        double y0 = 1;
        double x1 = 2;
        double y1 = 1;
        double x2 = 4;
        double y2 = 1;
        double targetY = 1;

        double[] x = new double[4];
        double[] y = new double[4];
        x[0] = x0;
        x[1] = x1;
        x[2] = x2;
        y[0] = y0;
        y[1] = y1;
        y[2] = y2;

        assertThrows(IllegalArgumentException.class, () -> BracketingNthOrderBrentSolver.guessX(targetY, x, y, 0, 3));
    }

}