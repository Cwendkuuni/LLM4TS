package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;

public class BracketingNthOrderBrentSolverTest {

    private static final double ABSOLUTE_ACCURACY = 1e-6;
    private static final int MAXIMAL_ORDER = 5;

    @org.junit.Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(MAXIMAL_ORDER, solver.getMaximalOrder());
    }

    @org.junit.Test
    public void testConstructorWithAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(ABSOLUTE_ACCURACY);
        assertEquals(ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(MAXIMAL_ORDER, solver.getMaximalOrder());
    }

    @org.junit.Test
    public void testConstructorWithOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(MAXIMAL_ORDER);
        assertEquals(ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(MAXIMAL_ORDER, solver.getMaximalOrder());
    }

    @org.junit.Test
    public void testConstructorWithAccuracyAndOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(ABSOLUTE_ACCURACY, MAXIMAL_ORDER);
        assertEquals(ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(MAXIMAL_ORDER, solver.getMaximalOrder());
    }

    @org.junit.Test
    public void testConstructorWithRelativeAccuracy() {
        double relativeAccuracy = 1e-4;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(relativeAccuracy, ABSOLUTE_ACCURACY);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 0.0);
        assertEquals(MAXIMAL_ORDER, solver.getMaximalOrder());
    }

    @org.junit.Test
    public void testConstructorWithRelativeAccuracyAndOrder() {
        double relativeAccuracy = 1e-4;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(relativeAccuracy, ABSOLUTE_ACCURACY, MAXIMAL_ORDER);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 0.0);
        assertEquals(MAXIMAL_ORDER, solver.getMaximalOrder());
    }

    @org.junit.Test
    public void testConstructorWithFunctionValueAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(ABSOLUTE_ACCURACY, ABSOLUTE_ACCURACY, 1e-8);
        assertEquals(ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(MAXIMAL_ORDER, solver.getMaximalOrder());
    }

    @org.junit.Test
    public void testConstructorWithFunctionValueAccuracyAndOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(ABSOLUTE_ACCURACY, ABSOLUTE_ACCURACY, 1e-8, MAXIMAL_ORDER);
        assertEquals(ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(MAXIMAL_ORDER, solver.getMaximalOrder());
    }

    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testInvalidMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(ABSOLUTE_ACCURACY, MAXIMAL_ORDER - 1);
    }

    @org.junit.Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(MAXIMAL_ORDER, solver.getMaximalOrder());
    }

    @org.junit.Test
    public void testSolveWithFunction() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = solver.solve(f, -2.0, 2.0);
        assertEquals(0.0, root, 1e-6);
    }

    @org.junit.Test
    public void testSolveWithFunctionAndStartValue() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = solver.solve(f, -2.0, 2.0, 1.5);
        assertEquals(0.0, root, 1e-6);
    }

    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testSolveWithFunctionAndInvalidMin() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(f, -Double.MAX_VALUE, 2.0);
    }

    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testSolveWithFunctionAndInvalidMax() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(f, 2.0, -Double.MAX_VALUE);
    }

    @org.junit.Test
    public void testSolveWithFunctionAndStartValueAndAllowedSolutionLeft() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(AllowedSolution.LEFT_SIDE);
        double root = solver.solve(f, -2.0, 2.0, 1.5);
        assertEquals(0.0, root, 1e-6);
    }

    @org.junit.Test
    public void testSolveWithFunctionAndStartValueAndAllowedSolutionRight() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(AllowedSolution.RIGHT_SIDE);
        double root = solver.solve(f, -2.0, 2.0, 1.5);
        assertEquals(0.0, root, 1e-6);
    }

    @org.junit.Test
    public void testSolveWithFunctionAndStartValueAndAllowedSolutionBelow() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(AllowedSolution.BELOW_SIDE);
        double root = solver.solve(f, -2.0, 2.0, 1.5);
        assertTrue(root < 0.0);
    }

    @org.junit.Test
    public void testSolveWithFunctionAndStartValueAndAllowedSolutionAbove() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(AllowedSolution.ABOVE_SIDE);
        double root = solver.solve(f, -2.0, 2.0, 1.5);
        assertTrue(root > 0.0);
    }

    @org.junit.Test(expected = NoBracketingException.class)
    public void testSolveWithFunctionAndInvalidBracketing() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(f, -10.0, 10.0);
    }

}