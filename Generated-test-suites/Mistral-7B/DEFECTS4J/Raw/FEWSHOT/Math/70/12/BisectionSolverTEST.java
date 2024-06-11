package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testConstructors() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);
    }

    @Test
    public void testSolve_SingleArgument() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0}, 3);
        double result = solver.solve(f, -1.0, 1.0);
        assertEquals(0.0, result, 1.0e-6);
    }

    @Test
    public void testSolve_TwoArguments() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, -1.0, 1.0}, 3);
        double result = solver.solve(f, -2.0, 2.0);
        assertEquals(0.0, result, 1.0e-6);
    }

    @Test
    public void testSolve_ThreeArguments() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 3.0, 2.0, 1.0}, 4);
        double result = solver.solve(f, -3.0, 3.0);
        assertEquals(1.0, result, 1.0e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolve_MaxIterationsExceeded() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0}, 3);
        solver.setMaximalIterationCount(0);
        solver.solve(f, -1.0, 1.0);
    }

    @Test
    public void testSolve_FunctionEvaluationException() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new FunctionUtils.Inverse(new PolynomialFunction(new double[]{-1.0, 1.0, 0.0}, 3), null);
        solver.solve(f, -1.0, 1.0);
    }

    @Test
    public void testVerifyInterval() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, -1.0, 1.0}, 3);
        solver.verifyInterval(-1.0, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyInterval_InvalidInterval() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, -1.0, 1.0}, 3);
        solver.verifyInterval(1.0, -1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyInterval_ZeroLengthInterval() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, -1.0, 1.0}, 3);
        solver.verifyInterval(0.0, 0.0);
    }

    @Test
    public void testMidpoint() {
        double min = -1.0;
        double max = 1.0;
        double expected = 0.0;
        double result = UnivariateRealSolverUtils.midpoint(min, max);
        assertEquals(expected, result, 1.0e-6);
    }

    @Test
    public void testMidpoint_ZeroLengthInterval() {
        double min = 0.0;
        double max = 0.0;
        double result = UnivariateRealSolverUtils.midpoint(min, max);
        assertNull(result);
    }

    @Test
    public void testMidpoint_InfAndMinusInf() {
        double min = Double.NEGATIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;
        double result = UnivariateRealSolverUtils.midpoint(min, max);
        assertNull(result);
    }

    @Test
    public void testMidpoint_ZeroAndInf() {
        double min = 0.0;
        double max = Double.POSITIVE_INFINITY;
        double result = UnivariateRealSolverUtils.midpoint(min, max);
        assertEquals(Double.POSITIVE_INFINITY, result);
    }

    @Test
    public void testMidpoint_ZeroAndMinusInf() {
        double min = 0.0;
        double max = Double.NEGATIVE_INFINITY;
        double result = UnivariateRealSolverUtils.midpoint(min, max);
        assertEquals(Double.NEGATIVE_INFINITY, result);
    }

    @Test
    public void testMidpoint_InfAndMinusInfAndZero() {
        double min = Double.NEGATIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;
        double result = UnivariateRealSolverUtils.midpoint(min, max, 0.0);
        assertEquals(0.0, result, 1.0e-6);
    }
}