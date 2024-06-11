package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;

public class BracketingNthOrderBrentSolverTest {

    @org.junit.Test
    public void testConstructorDefault() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(5, solver.getMaximalOrder());
    }

    @org.junit.Test
    public void testConstructorWithParameters() {
        double relativeAccuracy = 1e-4;
        double absoluteAccuracy = 1e-7;
        int maximalOrder = 8;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(relativeAccuracy, absoluteAccuracy, maximalOrder);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 1e-9);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(maximalOrder, solver.getMaximalOrder());
    }

    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidMaximalOrder() {
        double relativeAccuracy = 1e-4;
        double absoluteAccuracy = 1e-7;
        int maximalOrder = 1;
        new BracketingNthOrderBrentSolver(relativeAccuracy, absoluteAccuracy, maximalOrder);
    }

    @org.junit.Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        int maximalOrder = solver.getMaximalOrder();
        assertEquals(5, maximalOrder);

        BracketingNthOrderBrentSolver solver2 = new BracketingNthOrderBrentSolver(3);
        maximalOrder = solver2.getMaximalOrder();
        assertEquals(3, maximalOrder);
    }

    @org.junit.Test
    public void testSetAndGetAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        AllowedSolution allowed = AllowedSolution.ANY_SIDE;
        solver.setAllowedSolution(allowed);
        AllowedSolution allowedFromSolver = solver.getAllowedSolution();
        assertEquals(allowed, allowedFromSolver);

        AllowedSolution anotherAllowed = AllowedSolution.LEFT_SIDE;
        solver.setAllowedSolution(anotherAllowed);
        AllowedSolution allowedFromSolver2 = solver.getAllowedSolution();
        assertEquals(anotherAllowed, allowedFromSolver2);
    }

    @org.junit.Test
    public void testSolveSimpleExample() throws NoBracketingException {
        double functionValue = Math.pow(2, 0.5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(2, x) - functionValue;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, f, -1.0, 1.0);
        assertEquals(0.5, result, 1e-6);
    }

    @org.junit.Test
    public void testSolveWithLowerAllowedSolution() throws NoBracketingException {
        double functionValue = Math.pow(2, 1.5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(2, x) - functionValue;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.setAllowedSolution(AllowedSolution.LEFT_SIDE);
        double result = solver.solve(100, f, -1.0, 1.0);
        assertEquals(1.5, result, 1e-6);
    }

    @org.junit.Test
    public void testSolveWithUpperAllowedSolution() throws NoBracketingException {
        double functionValue = Math.pow(2, 0.5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(2, x) - functionValue;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.setAllowedSolution(AllowedSolution.RIGHT_SIDE);
        double result = solver.solve(100, f, -1.0, 1.0);
        assertEquals(0.5, result, 1e-6);
    }

    @org.junit.Test
    public void testSolveNoBracketing() throws NoBracketingException {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        try {
            solver.solve(100, f, -Math.PI, Math.PI);
            assertFalse(true);
        } catch (NoBracketingException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testSolveWithFunctionValueAccuracy() throws NoBracketingException {
        double functionValue = Math.pow(2, 0.5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(2, x) - functionValue;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 1e-9, 1e-10);
        double result = solver.solve(100, f, -1.0, 1.0);
        assertEquals(0.5, result, 1e-10);
    }

    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testSolveWithNegativeAllowedAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        try {
            solver.solve(1, null, -1.0, 1.0, AllowedSolution.ANY_SIDE, -1e-6);
            assertFalse(true);
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testSolveWithNullFunction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        try {
            solver.solve(1, null, -1.0, 1.0, AllowedSolution.ANY_SIDE);
            assertFalse(true);
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }
}