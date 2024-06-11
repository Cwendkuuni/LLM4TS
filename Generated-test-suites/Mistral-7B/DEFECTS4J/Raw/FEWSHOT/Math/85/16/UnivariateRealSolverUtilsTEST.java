package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertDoubleEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.function.Functions;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MathRuntimeException;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = Functions.sinFunction(0);
        double x0 = 0.0;
        double x1 = Math.PI;

        double root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertDoubleEquals(Math.PI / 2.0, root, 1e-6);
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() throws ConvergenceException,
            FunctionEvaluationException {
        UnivariateRealFunction f = Functions.sinFunction(0);
        double x0 = 0.0;
        double x1 = Math.PI;
        double absoluteAccuracy = 1e-6;

        double root = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        assertDoubleEquals(Math.PI / 2.0, root, absoluteAccuracy);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveWithNullFunction() throws FunctionEvaluationException {
        UnivariateRealFunction f = null;
        double x0 = 0.0;
        double x1 = 1.0;

        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidInterval() throws IllegalArgumentException {
        UnivariateRealFunction f = Functions.sinFunction(0);
        double x0 = Double.POSITIVE_INFINITY;
        double x1 = Double.NEGATIVE_INFINITY;

        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithMaxIterations() throws ConvergenceException,
            FunctionEvaluationException {
        UnivariateRealFunction f = Functions.sinFunction(0);
        double x0 = 0.0;
        double x1 = Math.PI;

        UnivariateRealSolverUtils.solve(f, x0, x1, 0);
    }

    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = Functions.sinFunction(0);
        double initial = 0.0;
        double lowerBound = -Math.PI;
        double upperBound = Math.PI;

        double[] brackets = UnivariateRealSolverUtils.bracket(f, initial, lowerBound,
                upperBound);
        assertArrayEquals(new double[]{lowerBound, upperBound}, brackets, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketWithMaxIterations() throws ConvergenceException,
            FunctionEvaluationException {
        UnivariateRealFunction f = Functions.sinFunction(0);
        double initial = 0.0;
        double lowerBound = -Math.PI;
        double upperBound = Math.PI;
        int maximumIterations = 0;

        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound,
                maximumIterations);
    }

    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 2.0;

        double midpoint = UnivariateRealSolverUtils.midpoint(a, b);
        assertDoubleEquals((a + b) / 2.0, midpoint, 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetupWithNullFunction() throws IllegalArgumentException {
        UnivariateRealFunction f = null;

        UnivariateRealSolverUtils.setup(f);
    }
}