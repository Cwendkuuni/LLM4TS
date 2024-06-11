package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.solvers.BrentSolver;
import org.apache.commons.math3.solvers.BisectionSolver;
import org.apache.commons.math3.solvers.SecantSolver;
import org.apache.commons.math3.solvers.UnivariateSolver;
import org.apache.commons.math3.util.Precision;

public class UnivariateRealSolverUtilsTest {

    @org.junit.Test
    public void testSolve() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, 1.0, -1.0}, new int[]{3, 2, 1});
        double x0 = 0.0;
        double x1 = 1.0;
        double root = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(0.5, root, Precision.EPSILON);
    }

    @org.junit.Test(expected = FunctionEvaluationException.class)
    public void testSolveWithFunctionEvaluationException() throws Exception {
        UnivariateRealFunction f = new ThrowingFunction();
        double x0 = 0.0;
        double x1 = 1.0;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @org.junit.Test(expected = ConvergenceException.class)
    public void testSolveWithConvergenceException() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0}, new int[]{6, 5, 4, 3, 2, 1});
        double x0 = 0.0;
        double x1 = 1.0;
        UnivariateRealSolverUtils.solve(f, x0, x1, 0.0, UnivariateRealSolverUtils.DEFAULT_MAX_ITERATIONS);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSolveWithIllegalArgumentException() throws Exception {
        UnivariateRealFunction f = null;
        double x0 = 0.0;
        double x1 = 1.0;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @org.junit.Test
    public void testSolveWithAbsoluteAccuracy() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, 1.0, -1.0}, new int[]{3, 2, 1});
        double x0 = 0.0;
        double x1 = 1.0;
        double root = UnivariateRealSolverUtils.solve(f, x0, x1, 0.001, UnivariateRealSolverUtils.DEFAULT_MAX_ITERATIONS);
        assertEquals(0.5, root, 0.001);
    }

    @org.junit.Test
    public void testBracket() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, 1.0, -1.0}, new int[]{3, 2, 1});
        double initial = 0.0;
        double lowerBound = 0.0;
        double upperBound = 1.0;
        double[] bracketingValues = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        double a = bracketingValues[0];
        double b = bracketingValues[1];
        assertEquals(0.5, midpoint(a, b), Precision.EPSILON);
    }

    @org.junit.Test(expected = ConvergenceException.class)
    public void testBracketWithConvergenceException() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0}, new int[]{6, 5, 4, 3, 2, 1});
        double initial = 0.0;
        double lowerBound = 0.0;
        double upperBound = 1.0;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, UnivariateRealSolverUtils.DEFAULT_MAX_ITERATIONS);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBracketWithIllegalArgumentException() throws Exception {
        UnivariateRealFunction f = null;
        double initial = 0.0;
        double lowerBound = 0.0;
        double upperBound = 1.0;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    @org.junit.Test
    public void testBracketWithMaxIterations() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, 1.0, -1.0}, new int[]{3, 2, 1});
        double initial = 0.0;
        double lowerBound = 0.0;
        double upperBound = 1.0;
        double[] bracketingValues = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, 1);
        double a = bracketingValues[0];
        double b = bracketingValues[1];
        assertEquals(0.5, midpoint(a, b), Precision.EPSILON);
    }

    @org.junit.Test
    public void testMidpoint() throws Exception {
        double a = 2.0;
        double b = 4.0;
        double mid = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(3.0, mid, Precision.EPSILON);
    }

    @org.junit.Test
    public void testSetup() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, 1.0, -1.0}, new int[]{3, 2, 1});
        try {
            UnivariateRealSolverUtils.setup(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    private static class ThrowingFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) throws Exception {
            throw new FunctionEvaluationException("Test");
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }
    }
}