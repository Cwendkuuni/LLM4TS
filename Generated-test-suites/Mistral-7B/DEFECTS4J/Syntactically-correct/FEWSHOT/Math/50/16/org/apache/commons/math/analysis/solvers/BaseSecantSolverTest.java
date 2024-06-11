package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testMethods() {
        assertEquals(Method.values().length, BaseSecantSolver.Method.values().length);
    }

    @Test
    public void testDefaultConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    @Test
    public void testConstructorWithRelativeAndAbsoluteAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(0.01, 0.001, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(0.01, solver.getRelativeAccuracy(), 0.0);
        assertEquals(0.001, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    @Test
    public void testConstructorWithAllParameters() {
        BaseSecantSolver solver = new BaseSecantSolver(0.01, 0.001, 0.0001, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(0.01, solver.getRelativeAccuracy(), 0.0);
        assertEquals(0.001, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(0.0001, solver.getFunctionValueAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    @Test
    public void testGetAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(AllowedSolution.ANY_SIDE, solver.getAllowedSolution());
    }

    @Test
    public void testSetAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        solver.setAllowedSolution(AllowedSolution.LEFT_SIDE);
        assertEquals(AllowedSolution.LEFT_SIDE, solver.getAllowedSolution());
    }

    @Test
    public void testSolveWithMinAndMax() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = f -> Math.pow(Math.E, -Math.log(2) * f);
        double min = 0.1;
        double max = 1.0;
        double solution = solver.solve(10, function, min, max, AllowedSolution.ANY_SIDE);
        assertTrue(solution > min && solution < max);
    }

    @Test
    public void testSolveWithMinAndMaxAndStartValue() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = f -> Math.pow(Math.E, -Math.log(2) * f);
        double min = 0.1;
        double max = 1.0;
        double startValue = 0.5;
        double solution = solver.solve(10, function, min, max, startValue, AllowedSolution.ANY_SIDE);
        assertTrue(solution > min && solution < max);
    }

    @Test
    public void testSolveWithMinAndMaxAndStartValueAndAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = f -> Math.pow(Math.E, -Math.log(2) * f);
        double min = 0.1;
        double max = 1.0;
        double startValue = 0.5;
        AllowedSolution allowedSolution = AllowedSolution.LEFT_SIDE;
        double solution = solver.solve(10, function, min, max, startValue, allowedSolution);
        assertTrue(solution > min && solution < max);
        assertFalse(solution <= min || solution >= max);
    }

    @Test
    public void testVerifyBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        double x0 = 1.0;
        double x1 = 2.0;
        assertTrue(solver.verifyBracketing(x0, x1));
    }

    @Test(expected = MathInternalError.class)
    public void testVerifyBracketingWithInvalidMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.INVALID);
        double x0 = 1.0;
        double x1 = 2.0;
        solver.verifyBracketing(x0, x1);
    }

    @Test
    public void testSolveWithFunctionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(0.01, 0.001, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = f -> Math.pow(Math.E, -Math.log(2) * f);
        double min = 0.1;
        double max = 1.0;
        double functionValueAccuracy = 0.0001;
        double solution = solver.solve(10, function, min, max, AllowedSolution.ANY_SIDE, functionValueAccuracy);
        assertTrue(solution > min && solution < max);
    }

    @Test
    public void testSolveWithFunctionValueAccuracyAndAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(0.01, 0.001, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = f -> Math.pow(Math.E, -Math.log(2) * f);
        double min = 0.1;
        double max = 1.0;
        double functionValueAccuracy = 0.0001;
        AllowedSolution allowedSolution = AllowedSolution.LEFT_SIDE;
        double solution = solver.solve(10, function, min, max, functionValueAccuracy, allowedSolution);
        assertTrue(solution > min && solution < max);
        assertFalse(solution <= min || solution >= max);
    }
}