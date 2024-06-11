package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.UnbracketedException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.AllowedSolution;

public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}.
     * @throws Exception if test fails
     */
    @Test
    public void testSolve() throws Exception {
        // Test with a simple polynomial function
        UnivariateFunction func = new PolynomialFunction(new double[]{-1.0, 2.0, 1.0});
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(10, func, -1.0, 1.0, 0.0);
        assertEquals(0.0, root, 1.0e-8);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     * @throws Exception if test fails
     */
    @Test
    public void testSolveWithAllowedSolution() throws Exception {
        // Test with a simple polynomial function and allowed solution
        UnivariateFunction func = new PolynomialFunction(new double[]{-1.0, 2.0, 1.0});
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.REGULA_FALSI, AllowedSolution.RIGHT_SIDE);
        double root = solver.solve(10, func, -1.0, 1.0, 0.0);
        assertEquals(0.0, root, 1.0e-8);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)} with {@link NullArgumentException}.
     * @throws Exception if test fails
     */
    @Test(expected = NullArgumentException.class)
    public void testSolveNullFunction() throws Exception {
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(10, null, -1.0, 1.0, 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)} with {@link UnbracketedException}.
     * @throws Exception if test fails
     */
    @Test(expected = UnbracketedException.class)
    public void testSolveUnbracketed() throws Exception {
        UnivariateFunction func = new PolynomialFunction(new double[]{1.0, 0.0});
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(10, func, -1.0, -1.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)} with {@link MaxEvalExceededException}.
     * @throws Exception if test fails
     */
    @Test(expected = MaxEvalExceededException.class)
    public void testSolveMaxEvalExceeded() throws Exception {
        UnivariateFunction func = new FunctionUtils.SINE();
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(0, func, 0.0, PrecisionUtils.PI_2, 0.0);
    }

}