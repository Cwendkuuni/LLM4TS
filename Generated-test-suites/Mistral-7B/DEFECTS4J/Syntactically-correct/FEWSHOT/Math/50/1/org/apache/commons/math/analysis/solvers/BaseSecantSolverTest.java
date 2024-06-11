package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.commons.math.analysis.FunctionUtils;
import org.apache.commons.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.commons.math.exception.MathInternalError;
import org.junit.Test;

import org.apache.commons.cli.BaseSecantSolver;
import org.apache.commons.cli.BracketedUnivariateRealSolver;
import org.apache.commons.cli.BracketedUnivariateRealSolver.AllowedSolution;
import org.apache.commons.cli.RegulaFalsiSolver;
import org.apache.commons.commons.math.analysis.solvers.AbstractUnivariateRealSolver;

public class BaseSecantSolverTest {

    @Test
    public void testConstructors() {
        // Test default constructor
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());

        // Test constructor with absolute accuracy
        BaseSecantSolver solver1 = new BaseSecantSolver(0.001, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(0.001, solver1.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver1.getMethod());

        // Test constructor with relative accuracy and absolute accuracy
        BaseSecantSolver solver2 = new BaseSecantSolver(1e-5, 1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(1e-5, solver2.getRelativeAccuracy(), 0.0);
        assertEquals(1e-6, solver2.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver2.getMethod());

        // Test constructor with relative accuracy, absolute accuracy, and function value accuracy
        BaseSecantSolver solver3 = new BaseSecantSolver(1e-5, 1e-6, 1e-7, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(1e-5, solver3.getRelativeAccuracy(), 0.0);
        assertEquals(1e-6, solver3.getAbsoluteAccuracy(), 0.0);
        assertEquals(1e-7, solver3.getFunctionValueAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver3.getMethod());
    }

    @Test
    public void testSolverWithDefaultMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5);
        assertNull(solver.solve(10, new SimpleFunction(0.0), -1.0, 1.0, 0.0));
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolverWithNullFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5);
        solver.solve(10, null, -1.0, 1.0, 0.0);
    }

    @Test(expected = MathInternalError.class)
    public void testSolverWithUnbracketedBounds() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5);
        solver.solve(10, new SimpleFunction(0.0), 0.0, 0.0, AllowedSolution.LEFT_SIDE);
    }

    @Test
    public void testSolverWithAllowedLeftSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5);
        double result = solver.solve(10, new SimpleFunction(0.0), -1.0, 0.0, AllowedSolution.LEFT_SIDE);
        assertTrue(result >= -1.0);
    }

    @Test
    public void testSolverWithAllowedRightSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5);
        double result = solver.solve(10, new SimpleFunction(0.0), 0.0, 1.0, AllowedSolution.RIGHT_SIDE);
        assertTrue(result <= 1.0);
    }

    @Test
    public void testSolverWithAllowedBelowSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5);
        double result = solver.solve(10, new SimpleFunction(0.0), -1.0, 0.0, AllowedSolution.BELOW_SIDE);
        assertTrue(result >= -1.0);
    }

    @Test
    public void testSolverWithAllowedAboveSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5);
        double result = solver.solve(10, new SimpleFunction(0.0), 0.0, 1.0, AllowedSolution.ABOVE_SIDE);
        assertTrue(result <= 1.0);
    }

    @Test
    public void testSolverWithRegulaFalsiMethod() {
        RegulaFalsiSolver solver = new RegulaFalsiSolver(1e-5);
        double result = solver.solve(10, new SimpleFunction(0.0), -1.0, 1.0);
        assertTrue(result >= -1.0 && result <= 1.0);
    }

    @Test(expected = MathInternalError.class)
    public void testSolverWithInvalidMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5);
        solver.setMethod(BaseSecantSolver.Method.INVALID);
        solver.solve(10, new SimpleFunction(0.0), -1.0, 1.0);
    }

    @Test
    public void testVerifyBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5);
        double[] bounds = {-1.0, 1.0};
        double x0 = bounds[0];
        double x1 = bounds[1];
        double f0 = computeObjectiveValue(x0);
        double f1 = computeObjectiveValue(x1);
        solver.verifyBracketing(x0, x1);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testVerifyBracketingWithInvalidBounds() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5);
        double x0 = 1.0;
        double x1 = 0.0;
        double f0 = computeObjectiveValue(x0);
        double f1 = computeObjectiveValue(x1);
        solver.verifyBracketing(x0, x1);
    }

    @Test
    public void testVerifyBracketingWithZeroFunctionValueAtMin() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5);
        double x0 = 0.0;
        double x1 = 1.0;
        double f0 = 0.0;
        double f1 = computeObjectiveValue(x1);
        solver.verifyBracketing(x0, x1);
    }

    @Test
    public void testVerifyBracketingWithZeroFunctionValueAtMax() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5);
        double x0 = -1.0;
        double x1 = 0.0;
        double f0 = computeObjectiveValue(x0);
        double f1 = 0.0;
        solver.verifyBracketing(x0, x1);
    }

    @Test
    public void testVerifyBracketingWithNegativeFunctionValueAtMin() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5);
        double x0 = -1.0;
        double x1 = 0.0;
        double f0 = -1.0;
        double f1 = 0.0;
        solver.verifyBracketing(x0, x1);
    }

    @Test
    public void testVerifyBracketingWithPositiveFunctionValueAtMax() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5);
        double x0 = -1.0;
        double x1 = 0.0;
        double f0 = -1.0;
        double f1 = 1.0;
        solver.verifyBracketing(x0, x1);
    }

    @Test
    public void testVerifyBracketingWithNegativeFunctionValueAtBoth() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5);
        double x0 = -1.0;
        double x1 = -2.0;
        double f0 = -1.0;
        double f1 = -2.0;
        solver.verifyBracketing(x0, x1);
    }

    @Test
    public void testVerifyBracketingWithPositiveFunctionValueAtBoth() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5);
        double x0 = 1.0;
        double x1 = 2.0;
        double f0 = 1.0;
        double f1 = 2.0;
        solver.verifyBracketing(x0, x1);
    }

    @Test
    public void testVerifyBracketingWithFunctionValueZeroAtMid() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5);
        double x0 = -1.0;
        double x1 = 0.0;
        double f0 = -1.0;
        double f1 = 0.0;
        solver.verifyBracketing(x0, x1);
    }

    private UnivariateRealFunction newSimpleFunction(double a) {
        return new UnivariateRealFunction() {

            @Override
            public double value(double x) {
                return x * x - a;
            }

            @Override
            public int compare(double x, double y) {
                return Double.compare(value(x), value(y));
            }
        };
    }

    private double computeObjectiveValue(double x) {
        return newSimpleFunction(1.0).value(x);
    }

    private static class SimpleFunction implements UnivariateRealFunction {
        private final double a;

        public SimpleFunction(double a) {
            this.a = a;
        }

        @Override
        public double value(double x) {
            return x * x - a;
        }

        @Override
        public int compare(double x, double y) {
            return Double.compare(value(x), value(y));
        }
    }
}