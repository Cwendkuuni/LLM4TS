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
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolverConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver.getMaximalIterationCount());
        assertNotNull(solver.getAbsoluteAccuracy());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBisectionSolverNullFunction() {
        new BisectionSolver(null);
    }

    @Test
    public void testBisectionSolverWithDefaults() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0, 1.0}, new double[]{1.0, 0.0, 0.0, 1.0});
        double expectedResult = 0.0;
        double result = solver.solve(f, -1.0, 1.0);
        assertEquals(expectedResult, result, 1e-14);
    }

    @Test
    public void testBisectionSolverWithCustomParams() throws Exception {
        BisectionSolver solver = new BisectionSolver(50, 1e-8);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0, 1.0}, new double[]{1.0, 0.0, 0.0, 1.0});
        double expectedResult = 0.0;
        double result = solver.solve(f, -1.0, 1.0);
        assertEquals(expectedResult, result, 1e-14);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testBisectionSolverMaxIterationsExceeded() throws Exception {
        BisectionSolver solver = new BisectionSolver(1);
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, 0.0, 0.0, 1.0}, new double[]{1.0, 0.0, 0.0, 1.0});
        double min = -1.0;
        double max = 1.0;
        solver.solve(f, min, max);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBisectionSolverFunctionEvaluationException() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = (x) -> { throw new FunctionEvaluationException("Test Exception"); };
        double min = 0.0;
        double max = 1.0;
        solver.solve(f, min, max);
    }

    @Test
    public void testBisectionSolverWrongRoot() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 1.0}, new double[]{1.0, 0.0, 1.0, 1.0});
        double min = -1.0;
        double max = 1.0;
        assertFalse(FunctionUtils.checkEqual(solver.solve(f, min, max), 1.0));
    }

    @Test
    public void testBisectionSolverInitialValue() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, 0.0, 1.0, 1.0}, new double[]{1.0, 0.0, 1.0, 1.0});
        double min = -1.0;
        double max = 1.0;
        double initial = 0.5;
        double expectedResult = 0.0;
        double result = solver.solve(f, min, max, initial);
        assertEquals(expectedResult, result, 1e-14);
    }

    @Test
    public void testBisectionSolverCustomFunction() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = (x) -> Math.log(1 + Math.pow(x, 2.0));
        double min = 0.0;
        double max = 2.0;
        double expectedResult = 1.0;
        double result = solver.solve(f, min, max);
        assertEquals(expectedResult, result, 1e-14);
    }

    @Test
    public void testBisectionSolverCustomFunctionWithInitialValue() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = (x) -> Math.log(1 + Math.pow(x, 2.0));
        double min = 0.0;
        double max = 2.0;
        double initial = 1.5;
        double expectedResult = 1.0;
        double result = solver.solve(f, min, max, initial);
        assertEquals(expectedResult, result, 1e-14);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBisectionSolverSmallInterval() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, 0.0, 1.0, 1.0}, new double[]{1.0, 0.0, 1.0, 1.0});
        double min = 0.0;
        double max = 0.01;
        solver.solve(f, min, max);
    }
}