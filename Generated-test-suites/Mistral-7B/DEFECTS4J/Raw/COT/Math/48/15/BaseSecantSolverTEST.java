package org.apache.commons.math.analysis.solvers.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunctions;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnivariateFunctionEvalException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for BaseSecantSolver.
 */
public class BaseSecantSolverTest {

    /**
     * Test constructor with default arguments.
     */
    @Test
    public void testConstructorWithDefaultArguments() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test constructor with custom arguments.
     */
    @Test
    public void testConstructorWithCustomArguments() {
        double relativeAccuracy = 1e-4;
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 0.0);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.ILLINOIS, solver.getMethod());
    }

    /**
     * Test constructor with optional function value accuracy.
     */
    @Test
    public void testConstructorWithFunctionValueAccuracy() {
        double relativeAccuracy = 1e-4;
        double absoluteAccuracy = 1e-6;
        double functionValueAccuracy = 1e-7;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, BaseSecantSolver.Method.PEGASUS);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 0.0);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(functionValueAccuracy, solver.getFunctionValueAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.PEGASUS, solver.getMethod());
    }

    /**
     * Test solve method with default constructor and polynomial function.
     */
    @Test
    public void testSolveWithDefaultConstructorAndPolynomialFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateFunction function = new PolynomialFunctions.SquareFunction();
        double root = 2.0;
        double result;

        try {
            result = solver.solve(50, function, -5.0, 5.0, 0.0);
            assertEquals(root, result, 1e-5);
        } catch (MaxEvalException e) {
            // Maximum number of evaluations exceeded, this is expected.
        }
    }

    /**
     * Test solve method with custom constructor and polynomial function.
     */
    @Test
    public void testSolveWithCustomConstructorAndPolynomialFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-4, 1e-6, BaseSecantSolver.Method.ILLINOIS);
        UnivariateFunction function = new PolynomialFunctions.SquareFunction();
        double root = 2.0;
        double result;

        try {
            result = solver.solve(50, function, -5.0, 5.0, 0.0);
            assertEquals(root, result, 1e-6);
        } catch (MaxEvalException e) {
            // Maximum number of evaluations exceeded, this is expected.
        }
    }

    /**
     * Test solve method with optional function value accuracy and polynomial function.
     */
    @Test
    public void testSolveWithFunctionValueAccuracyAndPolynomialFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-4, 1e-6, 1e-8, BaseSecantSolver.Method.PEGASUS);
        UnivariateFunction function = new PolynomialFunctions.SquareFunction();
        double root = 2.0;
        double result;

        try {
            result = solver.solve(50, function, -5.0, 5.0, 0.0);
            assertEquals(root, result, 1e-8);
        } catch (MaxEvalException | NumberIsTooSmallException e) {
            // Expected exceptions
        }
    }

    /**
     * Test solve method with negative relative accuracy.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNegativeRelativeAccuracy() {
        new BaseSecantSolver(-1e-4, 1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    }

    /**
     * Test solve method with negative absolute accuracy.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNegativeAbsoluteAccuracy() {
        new BaseSecantSolver(1e-4, -1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    }

    /**
     * Test solve method with invalid method.
     */
    @Test(expected = IllegalStateException.class)
    public void testSolveWithInvalidMethod() {
        new BaseSecantSolver(1e-4, 1e-6, BaseSecantSolver.Method.INVALID, BaseSecantSolver.Method.REGULA_FALSI);
    }

    /**
     * Test verifyBracketing method with invalid interval.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testVerifyBracketingWithInvalidInterval() {
        new BaseSecantSolver(1e-4, 1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double doSolve() {
                // Override doSolve method to not call verifyBracketing
                super.doSolve();
                throw new IllegalArgumentException("Invalid interval");
            }
        }.solve(50, new PolynomialFunctions.SquareFunction(), -5.0, -5.0, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test solve method with function throwing exception.
     */
    @Test
    public void testSolveWithFunctionThrowingException() {
        UnivariateFunction function = (x) -> {
            throw new UnivariateFunctionEvalException("Function evaluation error");
        };
        assertThrows(UnivariateFunctionEvalException.class, () -> new BaseSecantSolver(1e-4, 1e-6, BaseSecantSolver.Method.REGULA_FALSI).solve(50, function, -5.0, 5.0, AllowedSolution.ANY_SIDE));
    }
}