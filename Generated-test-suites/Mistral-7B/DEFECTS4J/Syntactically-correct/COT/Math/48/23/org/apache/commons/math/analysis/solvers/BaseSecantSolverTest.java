package org.apache.commons.math.analysis.solvers.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;

public class BaseSecantSolverTest {
    

    @Test
    public void testConstructorWithAbsoluteAccuracyAndMethod() {
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 1e-11);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    @Test
    public void testConstructorWithRelativeAndAbsoluteAccuracyAndMethod() {
        double relativeAccuracy = 1e-5;
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 1e-11);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 1e-11);
        assertEquals(BaseSecantSolver.Method.ILLINOIS, solver.getMethod());
    }

    @Test
    public void testConstructorWithRelativeAndAbsoluteAndFunctionValueAccuracyAndMethod() {
        double relativeAccuracy = 1e-5;
        double absoluteAccuracy = 1e-6;
        double functionValueAccuracy = 1e-7;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, BaseSecantSolver.Method.PEGASUS);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 1e-11);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 1e-11);
        assertEquals(functionValueAccuracy, solver.getFunctionValueAccuracy(), 1e-11);
        assertEquals(BaseSecantSolver.Method.PEGASUS, solver.getMethod());
    }

    @Test
    public void testSolveWithMinAndMaxAndFunctionAndMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 4);
        double min = -3.0;
        double max = 3.0;

        try {
            double result = solver.solve(50, function, min, max, 0.0);
            assertEquals(-2.0, result, 1e-9);
        } catch (MaxEvalException e) {
            // Expected
        }
    }

    @Test
    public void testSolveWithMinAndMaxAndFunctionAndMethodWithStartValue() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 4);
        double min = -3.0;
        double max = 3.0;
        double startValue = 1.0;

        try {
            double result = solver.solve(50, function, min, max, startValue);
            assertEquals(-2.0, result, 1e-9);
        } catch (MaxEvalException e) {
            // Expected
        }
    }

    @Test
    public void testSolveWithMinAndMaxAndFunctionAndMethodWithStartValueOutsideBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 4);
        double min = -3.0;
        double max = 3.0;
        double startValue = 6.0;

        assertThrows(IllegalArgumentException.class, () -> solver.solve(50, function, min, max, startValue));
    }

    @Test
    public void testSolveWithMinAndMaxAndFunctionAndMethodWithStartValueInsideBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 4);
        double min = -3.0;
        double max = 3.0;
        double startValue = -1.0;

        try {
            double result = solver.solve(50, function, min, max, startValue);
            assertEquals(-2.0, result, 1e-9);
        } catch (MaxEvalException e) {
            // Expected
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithMinAndMaxAndFunctionAndMethodWithStartValueConvergingTooSlowly() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-14, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 4);
        double min = -3.0;
        double max = 3.0;
        double startValue = 0.0;

        solver.solve(50, function, min, max, startValue);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveWithMinAndMaxAndFunctionAndMethodWithFunctionValueTooSmall() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-10, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.exp(-100 * x));
        double min = -10.0;
        double max = 10.0;

        solver.solve(50, function, min, max, 0.0);
    }

    
}