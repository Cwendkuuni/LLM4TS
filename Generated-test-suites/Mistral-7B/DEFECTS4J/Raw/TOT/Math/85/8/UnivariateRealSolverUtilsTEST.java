package org.apache.commons.math.analysis.solvers.utils;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.exceptions.MaxIterationsExceededException;
import org.apache.commons.math3.analysis.exceptions.NoRootsException;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.BracketedBrentSolver;
import org.apache.commons.math3.analysis.solvers.PowellSolver;
import org.apache.commons.math3.analysis.solvers.SecantSolver;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolveDefaultSolver() {
        UnivariateFunction f = x -> Math.pow(x, 2) - 2;
        double result = UnivariateRealSolverUtils.solve(f, 1.0, 3.0);
        Assert.assertEquals(1.5, result, 1e-8);
    }

    @Test
    public void testSolveDefaultSolverWithAbsoluteAccuracy() {
        UnivariateFunction f = x -> Math.pow(x, 2) - 2;
        double result = UnivariateRealSolverUtils.solve(f, 1.0, 3.0, 1e-5);
        Assert.assertEquals(1.5, result, 5e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveDefaultSolverWithConvergenceException() {
        UnivariateFunction f = x -> Math.sin(x) / x;
        UnivariateRealSolverUtils.solve(f, 0.0, 1.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveDefaultSolverWithFunctionEvaluationException() {
        UnivariateFunction f = x -> { throw new FunctionEvaluationException("Test"); };
        UnivariateRealSolverUtils.solve(f, 0.0, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveDefaultSolverWithIllegalArgumentException() {
        UnivariateFunction f = null;
        UnivariateRealSolverUtils.solve(f, 0.0, 1.0);
    }

    @Test
    public void testBracket() {
        UnivariateFunction f = x -> Math.pow(x, 2) - 3;
        double[] result = UnivariateRealSolverUtils.bracket(f, 1.5, -2.0, 3.0);
        Assert.assertArrayEquals(new double[]{1.0, 2.5}, result, 1e-8);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketWithConvergenceException() {
        UnivariateFunction f = x -> Math.sin(x) / x;
        UnivariateRealSolverUtils.bracket(f, 0.0, 1.0, 2.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracketWithFunctionEvaluationException() {
        UnivariateFunction f = x -> { throw new FunctionEvaluationException("Test"); };
        UnivariateRealSolverUtils.bracket(f, 0.0, 1.0, 2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithIllegalArgumentException() {
        UnivariateFunction f = null;
        UnivariateRealSolverUtils.bracket(f, 0.0, 1.0, 2.0);
    }

    @Test
    public void testSolveCustomSolverBrent() {
        UnivariateFunction f = x -> Math.pow(x, 2) - 2;
        BrentSolver solver = new BrentSolver();
        double result = UnivariateRealSolverUtils.solve(f, 1.0, 3.0, solver);
        Assert.assertEquals(1.5, result, 1e-8);
    }

    @Test
    public void testSolveCustomSolverSecant() {
        UnivariateFunction f = x -> Math.pow(x, 2) - 2;
        SecantSolver solver = new SecantSolver();
        double result = UnivariateRealSolverUtils.solve(f, 1.0, 3.0, solver);
        Assert.assertEquals(1.5, result, 1e-8);
    }

    @Test
    public void testSolveCustomSolverPowell() {
        UnivariateFunction f = x -> Math.pow(x, 2) - 2;
        PowellSolver solver = new PowellSolver();
        double result = UnivariateRealSolverUtils.solve(f, 1.0, 3.0, solver);
        Assert.assertEquals(1.5, result, 1e-8);
    }

    @Test
    public void testSolveCustomSolverBracketedBrent() {
        UnivariateFunction f = x -> Math.pow(x, 2) - 2;
        BracketedBrentSolver solver = new BracketedBrentSolver();
        double[] result = UnivariateRealSolverUtils.bracket(f, 1.0, 3.0, solver);
        Assert.assertArrayEquals(new double[]{1.0, 2.5}, result, 1e-8);
    }
}