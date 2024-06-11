package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunctions;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.LBFGSBNonlinearSolver;
import org.apache.commons.math3.analysis.solvers.SimplifiedBrentSolver;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test(expected = IllegalArgumentException.class)
    public void testSolveNullFunction() {
        UnivariateRealFunction f = null;
        UnivariateRealSolverUtils.solve(f, 0.0, 1.0);
    }

    @Test
    public void testSolvePolynomialFunction() {
        UnivariateFunction f = new PolynomialFunctions.Quartic(1.0, -1.0, 1.0, -1.0, 1.0);
        double x = UnivariateRealSolverUtils.solve(f, 0.0, 1.0);
        assertArrayEquals(new double[]{1.0, 1.0}, new double[]{0.5, x}, 1e-6);
    }

    @Test
    public void testSolvePolynomialFunctionWithAbsoluteAccuracy() {
        UnivariateFunction f = new PolynomialFunctions.Quartic(1.0, -1.0, 1.0, -1.0, 1.0);
        double x = UnivariateRealSolverUtils.solve(f, 0.0, 1.0, 1e-9);
        assertArrayEquals(new double[]{1.0, 1.0}, new double[]{0.5, x}, 1e-11);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveFunctionWithMaxIterationsReached() throws Exception {
        UnivariateFunction f = new PolynomialFunctions.Quartic(1.0, -1.0, 1.0, -1.0, 1.0);
        UnivariateRealSolverUtils.solve(f, 0.0, 1.0, 0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionWithEvalError() {
        UnivariateRealFunction f = () -> { throw new ArithmeticException("Test error"); };
        UnivariateRealSolverUtils.solve(f, 0.0, 1.0);
    }

    @Test
    public void testBracketFunction() throws Exception {
        UnivariateFunction f = new PolynomialFunctions.Quartic(1.0, -1.0, 1.0, -1.0, 1.0);
        double[] brackets = UnivariateRealSolverUtils.bracket(f, 0.0, 1.0, 1e-6);
        assertArrayEquals(new double[]{1.0, 1.0}, new double[]{brackets[0], brackets[1]}, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketFunctionWithMaxIterationsReached() throws Exception {
        UnivariateFunction f = new PolynomialFunctions.Quartic(1.0, -1.0, 1.0, -1.0, 1.0);
        UnivariateRealSolverUtils.bracket(f, 0.0, 1.0, Integer.MAX_VALUE);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracketFunctionWithEvalError() throws Exception {
        UnivariateFunction f = () -> { throw new ArithmeticException("Test error"); };
        UnivariateRealSolverUtils.bracket(f, 0.0, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketFunctionNullFunction() {
        UnivariateRealFunction f = null;
        UnivariateRealSolverUtils.bracket(f, 0.0, 1.0);
    }

    @Test
    public void testBrentSolver() throws Exception {
        UnivariateFunction f = new PolynomialFunctions.Quintic(-1.0, 1.0, -1.0, 1.0, -1.0, 1.0);
        UnivariateRealSolver solver = new BrentSolver();
        double x = solver.solve(f, 0.0, 1.0);
        assertArrayEquals(new double[]{1.0, 1.0}, new double[]{0.5, x}, 1e-6);
    }

    @Test
    public void testSimplifiedBrentSolver() throws Exception {
        UnivariateFunction f = new PolynomialFunctions.Quintic(-1.0, 1.0, -1.0, 1.0, -1.0, 1.0);
        UnivariateRealSolver solver = new SimplifiedBrentSolver();
        double x = solver.solve(f, 0.0, 1.0);
        assertArrayEquals(new double[]{1.0, 1.0}, new double[]{0.5, x}, 1e-6);
    }

    @Test
    public void testLBFGSBSolver() throws Exception {
        UnivariateFunction f = new PolynomialFunctions.Quintic(-1.0, 1.0, -1.0, 1.0, -1.0, 1.0);
        UnivariateRealSolver solver = new LBFGSBNonlinearSolver();
        double x = solver.solve(f, 0.0, 1.0);
        assertArrayEquals(new double[]{1.0, 1.0}, new double[]{0.5, x}, 1e-6);
    }

}