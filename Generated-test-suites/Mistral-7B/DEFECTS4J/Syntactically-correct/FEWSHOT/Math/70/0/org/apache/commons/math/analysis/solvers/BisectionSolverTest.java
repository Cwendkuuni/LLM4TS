package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);
        assertEquals(100, solver.getMaximalIterationCount());
        assertEquals(1E-6, solver.getAbsoluteAccuracy(), 1E-6);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeprecatedConstructorWithFunction() {
        new BisectionSolver(FunctionUtils.sin);
    }

    @Test
    public void testSolve_minMax_positiveRoot() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new MyFunction();
        double result = solver.solve(f, 0.1, 0.5);
        assertEquals(0.3, result, 1E-6);
    }

    @Test
    public void testSolve_minMax_negativeRoot() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new MyFunctionNegativeRoot();
        double result = solver.solve(f, -0.5, -0.1);
        assertEquals(-0.3, result, 1E-6);
    }

    @Test
    public void testSolve_minMax_zeroRoot() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new MyFunctionZeroRoot();
        double result = solver.solve(f, -1, 1);
        assertEquals(0.0, result, 1E-14);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolve_minMax_failToFindRoot() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new MyFunctionFail();
        solver.solve(f, 0, 1);
    }

    @Test
    public void testSolve_minMax_initialGuess() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new MyFunction();
        double result = solver.solve(f, 0.5, 0.5);
        assertEquals(0.3, result, 1E-6);
    }

    @Test
    public void testSolve_minMax_initialGuessFarFromRoot() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new MyFunction();
        double result = solver.solve(f, -1, 1);
        assertEquals(0.3, result, 1E-6);
    }

    @Test
    public void testSolve_minMax_initialGuessTooClose() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new MyFunction();
        double result = solver.solve(f, 0.2, 0.4);
        assertEquals(0.3, result, 1E-6);
    }

    @Test
    public void testSolve_minMax_initialGuessSameAsRoot() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new MyFunction();
        double result = solver.solve(f, 0.3, 0.3);
        assertEquals(0.3, result, 1E-6);
    }

    @Test
    public void testSolve_minMax_initialGuessMidpoint() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new MyFunction();
        double result = solver.solve(f, 0.1, 0.5);
        assertEquals(0.3, result, 1E-6);
    }

    private static class MyFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return x * x - 0.09;
        }
    }

    private static class MyFunctionNegativeRoot implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return Math.pow(x, 2) + 1;
        }
    }

    private static class MyFunctionZeroRoot implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 0.01;
        }
    }

    private static class MyFunctionFail implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return Math.sin(x);
        }
    }
}