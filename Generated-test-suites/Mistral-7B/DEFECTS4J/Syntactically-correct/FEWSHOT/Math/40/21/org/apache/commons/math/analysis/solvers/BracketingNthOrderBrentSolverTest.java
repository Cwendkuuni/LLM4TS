package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolvePolynomialFunction() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, -1, 1});
        double[] bounds = {-3.0, 3.0};
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        int maxEval = 1000;

        double root = BracketingNthOrderBrentSolver.getInstance().solve(maxEval, f, bounds[0], bounds[1], allowedSolution);
        assertEquals(0.0, root, 1e-12);
    }

    @Test
    public void testSolvePolynomialFunctionWithStartValue() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, -1, 1});
        double[] bounds = {-3.0, 3.0};
        double startValue = 1.5;
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        int maxEval = 1000;

        double root = BracketingNthOrderBrentSolver.getInstance().solve(maxEval, f, bounds[0], bounds[1], startValue, allowedSolution);
        assertEquals(0.0, root, 1e-12);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, -1, 1});
        double[] bounds = {3.0, 3.0};
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        int maxEval = 1000;

        BracketingNthOrderBrentSolver.getInstance().solve(maxEval, f, bounds[0], bounds[1], allowedSolution);
    }

    @Test
    public void testSolveFunctionWithMinAndMaxOutOfOrder() {
        UnivariateFunction f = new FunctionUtils.SINE();
        double[] bounds = {3.0, 1.0};
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        int maxEval = 1000;

        double root = BracketingNthOrderBrentSolver.getInstance().solve(maxEval, f, bounds[1], bounds[0], allowedSolution);
        assertTrue(Math.abs(Math.PI - root) < 1e-12);
    }

    @Test
    public void testSolveFunctionWithFunctionValueAccuracy() {
        UnivariateFunction f = new FunctionUtils.EXPONENTIAL();
        double[] bounds = {0.0, 1.0};
        double relativeAccuracy = 1e-6;
        double absoluteAccuracy = 1e-9;
        double functionValueAccuracy = 1e-8;
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        int maxEval = 1000;

        double root = BracketingNthOrderBrentSolver.getInstance().solve(
                maxEval, f, bounds[0], bounds[1], relativeAccuracy, absoluteAccuracy, functionValueAccuracy, allowedSolution);
        assertEquals(1.718281828, root, 1e-12);
    }

    @Test
    public void testSolveFunctionWithNegativeMaximalOrder() {
        UnivariateFunction f = new FunctionUtils.SIN();
        double[] bounds = {0.0, Math.PI};
        double relativeAccuracy = 1e-6;
        double absoluteAccuracy = 1e-9;
        int maximalOrder = -5;
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        int maxEval = 1000;

        try {
            BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(relativeAccuracy, absoluteAccuracy, maximalOrder);
            solver.solve(maxEval, f, bounds[0], bounds[1], allowedSolution);
            fail("Expected NumberIsTooSmallException with negative maximal order.");
        } catch (NumberIsTooSmallException e) {
            // expected
        }
    }

    @Test
    public void testSolveFunctionWithZeroMaximalOrder() {
        UnivariateFunction f = new FunctionUtils.SIN();
        double[] bounds = {0.0, Math.PI};
        double relativeAccuracy = 1e-6;
        double absoluteAccuracy = 1e-9;
        int maximalOrder = 0;
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        int maxEval = 1000;

        try {
            BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(relativeAccuracy, absoluteAccuracy, maximalOrder);
            solver.solve(maxEval, f, bounds[0], bounds[1], allowedSolution);
            fail("Expected NumberIsTooSmallException with zero maximal order.");
        } catch (NumberIsTooSmallException e) {
            // expected
        }
    }

    @Test
    public void testSolveFunctionWithNonNumberAllowedSolution() {
        UnivariateFunction f = new FunctionUtils.SINE();
        double[] bounds = {0.0, Math.PI};
        double relativeAccuracy = 1e-6;
        double absoluteAccuracy = 1e-9;
        int maximalOrder = 5;
        Object allowedSolution = new Object();
        int maxEval = 1000;

        try {
            BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(relativeAccuracy, absoluteAccuracy, maximalOrder, allowedSolution);
            solver.solve(maxEval, f, bounds[0], bounds[1]);
            fail("Expected MathInternalError.");
        } catch (MathInternalError e) {
            // expected
        }
    }

    @Test
    public void testSolveFunctionWithNonBracketingBounds() {
        UnivariateFunction f = new FunctionUtils.SINE();
        double[] bounds = {3.0, 1.0};
        double relativeAccuracy = 1e-6;
        double absoluteAccuracy = 1e-9;
        int maximalOrder = 5;
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        int maxEval = 1000;

        try {
            BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(relativeAccuracy, absoluteAccuracy, maximalOrder);
            solver.solve(maxEval, f, bounds[0], bounds[1], allowedSolution);
            fail("Expected NoBracketingException.");
        } catch (NoBracketingException e) {
            // expected
        }
    }

    @Test
    public void testSolveFunctionWithConvergedInterval() {
        UnivariateFunction f = new FunctionUtils.EXPONENTIAL();
        double[] bounds = {0.0, 1.0};
        double relativeAccuracy = 1e-12;
        double absoluteAccuracy = 1e-15;
        int maximalOrder = 5;
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        int maxEval = 1000;

        double root = BracketingNthOrderBrentSolver.getInstance().solve(maxEval, f, bounds[0], bounds[1], allowedSolution);
        assertEquals(1.718281828, root, 1e-14);
    }
}