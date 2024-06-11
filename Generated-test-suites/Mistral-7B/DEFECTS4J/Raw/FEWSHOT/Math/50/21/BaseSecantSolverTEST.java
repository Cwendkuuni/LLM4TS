package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testMethodEnumValues() {
        assertEquals(3, BaseSecantSolver.Method.values().length);
        assertTrue(BaseSecantSolver.Method.REGULA_FALSI == BaseSecantSolver.Method.values()[0]);
        assertTrue(BaseSecantSolver.Method.ILLINOIS == BaseSecantSolver.Method.values()[1]);
        assertTrue(BaseSecantSolver.Method.PEGASUS == BaseSecantSolver.Method.values()[2]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMethod() {
        new BaseSecantSolver.Method("INVALID");
    }

    @Test
    public void testCreateSolver() {
        assertEquals(Method.REGULA_FALSI, new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI).getMethod());
        assertEquals(Method.ILLINOIS, new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.ILLINOIS).getMethod());
        assertEquals(Method.PEGASUS, new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.PEGASUS).getMethod());
    }

    @Test
    public void testCreateSolverWithRelativeAndAbsoluteAccuracy() {
        assertEquals(Method.REGULA_FALSI, new BaseSecantSolver(0.01, 1e-6, Method.REGULA_FALSI).getMethod());
        assertEquals(Method.ILLINOIS, new BaseSecantSolver(0.01, 1e-6, Method.ILLINOIS).getMethod());
        assertEquals(Method.PEGASUS, new BaseSecantSolver(0.01, 1e-6, Method.PEGASUS).getMethod());
    }

    @Test
    public void testCreateSolverWithRelativeAccuracyAndFunctionValueAccuracy() {
        assertEquals(Method.REGULA_FALSI, new BaseSecantSolver(0.01, 1e-6, 1e-6, Method.REGULA_FALSI).getMethod());
        assertEquals(Method.ILLINOIS, new BaseSecantSolver(0.01, 1e-6, 1e-6, Method.ILLINOIS).getMethod());
        assertEquals(Method.PEGASUS, new BaseSecantSolver(0.01, 1e-6, 1e-6, Method.PEGASUS).getMethod());
    }

    @Test
    public void testSolveWithMinMaxAndFunction() {
        UnivariateRealFunction f = x -> x * x - 2;

        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);
        double root = solver.solve(10, f, -5, 5, 0);

        assertEquals(0, root, 1e-6);
        assertTrue(f.value(root) < 1e-6);
    }

    @Test
    public void testSolveWithMinMaxAndFunctionAndStartValue() {
        UnivariateRealFunction f = x -> x * x - 2;

        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);
        double root = solver.solve(10, f, -5, 5, 1);

        assertEquals(0, root, 1e-6);
        assertTrue(f.value(root) < 1e-6);
    }

    @Test
    public void testSolveWithMinMaxAndFunctionAndStartValueAndAllowedSolution() {
        UnivariateRealFunction f = x -> x * x - 2;

        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI, AllowedSolution.LEFT_SIDE);
        double root = solver.solve(10, f, -5, 5, 1);

        assertEquals(-2.44948974278317, root, 1e-6);
        assertTrue(f.value(root) < 0);
    }

    @Test
    public void testSolveWithMinMaxAndFunctionAndStartValueAndAllowedSolution2() {
        UnivariateRealFunction f = x -> x * x - 2;

        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI, AllowedSolution.RIGHT_SIDE);
        double root = solver.solve(10, f, -5, 5, 1);

        assertEquals(2.44948974278317, root, 1e-6);
        assertTrue(f.value(root) > 0);
    }

    @Test
    public void testSolveWithMinMaxAndFunctionAndStartValueAndAllowedSolution3() {
        UnivariateRealFunction f = x -> x * x - 2;

        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI, AllowedSolution.BELOW_SIDE);
        double root = solver.solve(10, f, -5, 5, 1);

        assertEquals(-2.44948974278317, root, 1e-6);
        assertTrue(f.value(root) < 0);
    }

    @Test
    public void testSolveWithMinMaxAndFunctionAndStartValueAndAllowedSolution4() {
        UnivariateRealFunction f = x -> x * x - 2;

        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI, AllowedSolution.ABOVE_SIDE);
        double root = solver.solve(10, f, -5, 5, 1);

        assertEquals(2.44948974278317, root, 1e-6);
        assertTrue(f.value(root) > 0);
    }

    @Test
    public void testSolveWithMinMaxAndFunctionAndStartValueAndAllowedSolution5() {
        UnivariateRealFunction f = x -> Math.pow(Math.E, -x);

        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.ILLINOIS);
        double root = solver.solve(10, f, -11, -5, 0);

        assertEquals(0, root, 1e-6);
        assertTrue(Math.abs(Math.E - Math.exp(root)) < 1e-6);
    }

    @Test
    public void testSolveWithMinMaxAndFunctionAndStartValueAndAllowedSolution6() {
        UnivariateRealFunction f = x -> Math.pow(Math.E, -x);

        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.ILLINOIS, AllowedSolution.LEFT_SIDE);
        double root = solver.solve(10, f, -11, -5, 0);

        assertEquals(-1.13757319433566, root, 1e-6);
        assertTrue(Math.abs(Math.E - Math.exp(root)) < 1e-6);
    }

    @Test
    public void testSolveWithMinMaxAndFunctionAndStartValueAndAllowedSolution7() {
        UnivariateRealFunction f = x -> Math.pow(Math.E, -x);

        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.ILLINOIS, AllowedSolution.RIGHT_SIDE);
        double root = solver.solve(10, f, -11, -5, 0);

        assertEquals(-1.13757319433566, root, 1e-6);
        assertTrue(Math.abs(Math.E - Math.exp(root)) < 1e-6);
    }

    @Test
    public void testSolveWithMinMaxAndFunctionAndStartValueAndAllowedSolution8() {
        UnivariateRealFunction f = x -> Math.pow(Math.E, -x);

        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.PEGASUS);
        double root = solver.solve(10, f, -11, -5, 0);

        assertEquals(1.13757319433566, root, 1e-6);
        assertTrue(Math.abs(Math.E - Math.exp(root)) < 1e-6);
    }

    @Test
    public void testSolveWithMinMaxAndFunctionAndStartValueAndAllowedSolution9() {
        UnivariateRealFunction f = x -> Math.pow(Math.E, -x);

        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.PEGASUS, AllowedSolution.LEFT_SIDE);
        double root = solver.solve(10, f, -11, -5, 0);

        assertEquals(-1.13757319433566, root, 1e-6);
        assertTrue(Math.abs(Math.E - Math.exp(root)) < 1e-6);
    }

    @Test
    public void testSolveWithMinMaxAndFunctionAndStartValueAndAllowedSolution10() {
        UnivariateRealFunction f = x -> Math.pow(Math.E, -x);

        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.PEGASUS, AllowedSolution.RIGHT_SIDE);
        double root = solver.solve(10, f, -11, -5, 0);

        assertEquals(-1.13757319433566, root, 1e-6);
        assertTrue(Math.abs(Math.E - Math.exp(root)) < 1e-6);
    }

    @Test
    public void testVerifyBracketing() {
        double x0 = 1, x1 = 2, f0, f1;

        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);

        f0 = solver.computeObjectiveValue(x0);
        f1 = solver.computeObjectiveValue(x1);

        solver.verifyBracketing(x0, x1);

        // If the function values have opposite sign, the interval is bracketing
        assertTrue(f0 * f1 < 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyBracketingWithEqualFunctionValues() {
        double x0 = 1, x1 = 1;

        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);

        solver.verifyBracketing(x0, x1);
    }
}