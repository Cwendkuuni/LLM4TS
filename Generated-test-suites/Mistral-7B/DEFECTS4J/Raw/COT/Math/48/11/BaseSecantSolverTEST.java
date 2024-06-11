package org.apache.commons.math.analysis.solvers.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test cases for the BaseSecantSolver class.
 */
public class BaseSecantSolverTest {

    /**
     * Test constructor with default arguments.
     */
    @Test
    public void testDefaultConstructor() {
        final BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test constructor with custom arguments.
     */
    @Test
    public void testCustomConstructor() {
        final double relativeAccuracy = 1e-4;
        final double absoluteAccuracy = 1e-6;
        final double functionValueAccuracy = 1e-7;
        final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 0);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0);
        assertEquals(functionValueAccuracy, solver.getFunctionValueAccuracy(), 0);
        assertEquals(BaseSecantSolver.Method.ILLINOIS, solver.getMethod());
    }

    /**
     * Test solve method with default arguments.
     *
     * @throws ConvergenceException when the solver fails to converge.
     */
    @Test
    public void testSolveDefaultArguments() throws ConvergenceException {
        final BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        final UnivariateRealFunction function = new FunctionTest();
        final double result = solver.solve(10, function, -1.0, 1.0, 0.0);
        assertEquals(0.75, result, Precision.EPSILON);
    }

    /**
     * Test solve method with custom arguments.
     *
     * @throws ConvergenceException when the solver fails to converge.
     */
    @Test
    public void testSolveCustomArguments() throws ConvergenceException {
        final double relativeAccuracy = 1e-4;
        final double absoluteAccuracy = 1e-6;
        final double functionValueAccuracy = 1e-7;
        final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, BaseSecantSolver.Method.ILLINOIS);
        final UnivariateRealFunction function = new FunctionTest();
        final double result = solver.solve(10, function, -1.0, 1.0, 0.0);
        assertEquals(0.75, result, Precision.EPSILON);
    }

    /**
     * Test solve method with custom start value.
     *
     * @throws ConvergenceException when the solver fails to converge.
     */
    @Test
    public void testSolveCustomStartValue() throws ConvergenceException {
        final double relativeAccuracy = 1e-4;
        final double absoluteAccuracy = 1e-6;
        final double functionValueAccuracy = 1e-7;
        final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, BaseSecantSolver.Method.PEGASUS);
        final UnivariateRealFunction function = new FunctionTest();
        final double startValue = 0.5;
        final double result = solver.solve(10, function, -1.0, 1.0, startValue);
        assertEquals(0.75, result, Precision.EPSILON);
    }

    /**
     * Test verifyBracketing method.
     */
    @Test
    public void testVerifyBracketing() {
        final BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        assertThrows(IllegalArgumentException.class, () -> solver.verifyBracketing(-1.0, 0.0));
        assertThrows(IllegalArgumentException.class, () -> solver.verifyBracketing(1.0, -1.0));
    }

    /**
     * Test getMethod method.
     */
    @Test
    public void testGetMethod() {
        final BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test setMethod method.
     */
    @Test
    public void testSetMethod() {
        final BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        solver.setMethod(BaseSecantSolver.Method.ILLINOIS);
        assertEquals(BaseSecantSolver.Method.ILLINOIS, solver.getMethod());
    }

    /**
     * Test getAllowedSolution method.
     */
    @Test
    public void testGetAllowedSolution() {
        final BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(AllowedSolution.ANY_SIDE, solver.getAllowedSolution());
    }

    /**
     * Test setAllowedSolution method.
     */
    @Test
    public void testSetAllowedSolution() {
        final BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        solver.setAllowedSolution(AllowedSolution.LEFT_SIDE);
        assertEquals(AllowedSolution.LEFT_SIDE, solver.getAllowedSolution());
    }

    /**
     * Test constructor with null function.
     */
    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullFunction() {
        final BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS, null);
    }

    /**
     * Test solve method with null function.
     */
    @Test(expected = NullArgumentException.class)
    public void testSolveWithNullFunction() {
        final BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        solver.solve(10, null, -1.0, 1.0, 0.0);
    }

    /**
     * Test solve method with invalid method.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveWithInvalidMethod() {
        final BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        solver.setMethod(BaseSecantSolver.Method.INVALID);
        solver.solve(10, new FunctionTest(), -1.0, 1.0, 0.0);
    }
}

/**
 * A simple function for testing.
 */
class FunctionTest implements UnivariateRealFunction {

    @Override
    public double value(final double x) {
        return x * x - 1.0;
    }

}

/**
 * Allowed solution types.
 */
enum AllowedSolution {

    /**
     * Any solution.
     */
    ANY_SIDE,

    /**
     * Solution on the left side of the root.
     */
    LEFT_SIDE,

    /**
     * Solution on the right side of the root.
     */
    RIGHT_SIDE,

    /**
     * Solution below the root.
     */
    BELOW_SIDE,

    /**
     * Solution above the root.
     */
    ABOVE_SIDE
}