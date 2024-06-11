package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathInternalError;
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
    public void testDefaultConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        assertNotNull(solver);
    }

    /**
     * Test constructor with custom arguments.
     */
    @Test
    public void testCustomConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-10, BaseSecantSolver.Method.ILLINOIS);
        assertNotNull(solver);
    }

    /**
     * Test constructor with custom arguments (max iterations).
     */
    @Test
    public void testCustomConstructorMaxIter() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-10, 100, BaseSecantSolver.Method.ILLINOIS);
        assertNotNull(solver);
    }

    /**
     * Test constructor with custom arguments (relative and absolute accuracy,
     * function value accuracy, and method).
     */
    @Test
    public void testCustomConstructorAccuracyFunctionValue() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-10, 1.0e-15, 1.0e-12, BaseSecantSolver.Method.PEGASUS);
        assertNotNull(solver);
    }

    /**
     * Test method solve with default arguments.
     * @throws Exception
     */
    @Test
    public void testSolveDefaultArguments() throws Exception {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);
        double root = solver.solve(f, -5.0, 5.0);
        double expected = 2.0;
        assertEquals(expected, Precision.round(root, 5), 0.0);
    }

    /**
     * Test method solve with custom arguments.
     * @throws Exception
     */
    @Test
    public void testSolveCustomArguments() throws Exception {
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-10, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction f = new PolynomialFunction(x -> Math.pow(x, 3) - 2.0);
        double root = solver.solve(f, -5.0, 5.0);
        double expected = -1.0;
        assertEquals(expected, Precision.round(root, 5), 0.0);
    }

    /**
     * Test method solve with custom arguments (max iterations).
     * @throws Exception
     */
    @Test
    public void testSolveCustomArgumentsMaxIter() throws Exception {
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-10, 100, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new PolynomialFunction(x -> Math.pow(x, 4) - 2.0);
        double root = solver.solve(f, -5.0, 5.0);
        double expected = 0.5;
        assertEquals(expected, Precision.round(root, 5), 0.0);
    }

    /**
     * Test method solve with custom arguments (relative and absolute accuracy,
     * function value accuracy, and method).
     * @throws Exception
     */
    @Test
    public void testSolveCustomArgumentsAccuracyFunctionValue() throws Exception {
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-10, 1.0e-15, 1.0e-12, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new PolynomialFunction(x -> Math.exp(x) - 2.0);
        double root = solver.solve(f, -5.0, 5.0);
        double expected = 0.69314718056;
        assertEquals(expected, Precision.round(root, 10), 0.0);
    }

    /**
     * Test method solve with incorrect function.
     * @throws Exception
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveIncorrectFunction() throws Exception {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = null;
        solver.solve(f, -5.0, 5.0);
    }

    /**
     * Test method solve with incorrect minimum.
     * @throws Exception
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveIncorrectMinimum() throws Exception {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);
        solver.solve(f, Double.NaN, 5.0);
    }

    /**
     * Test method solve with incorrect maximum.
     * @throws Exception
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveIncorrectMaximum() throws Exception {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);
        solver.solve(f, -5.0, Double.NaN);
    }

    /**
     * Test method solve with incorrect allowed solution.
     * @throws Exception
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveIncorrectAllowedSolution() throws Exception {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);
        solver.solve(f, -5.0, 5.0, AllowedSolution.ABOVE_SIDE);
    }

    /**
     * Test method solve with function value outside of bounds.
     * @throws Exception
     */
    @Test(expected = ConvergenceException.class)
    public void testSolveFunctionValueOutsideOfBounds() throws Exception {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new PolynomialFunction(x -> Math.pow(x, 2) - 10.0);
        solver.solve(f, -5.0, 5.0);
    }

    /**
     * Test method solve with function value too close to bounds.
     * @throws Exception
     */
    @Test(expected = ConvergenceException.class)
    public void testSolveFunctionValueTooCloseToBounds() throws Exception {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new PolynomialFunction(x -> Math.pow(x, 2) - 1e-15);
        solver.solve(f, -5.0, 5.0);
    }

    /**
     * Test method solve with maximum iterations exceeded.
     * @throws Exception
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() throws Exception {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS, 1);
        UnivariateRealFunction f = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);
        solver.solve(f, -5.0, 5.0);
    }

    /**
     * Test method solve with incorrect method.
     * @throws Exception
     */
    @Test(expected = MathInternalError.class)
    public void testSolveIncorrectMethod() throws Exception {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);
        solver.solve(f, -5.0, 5.0);
    }

}