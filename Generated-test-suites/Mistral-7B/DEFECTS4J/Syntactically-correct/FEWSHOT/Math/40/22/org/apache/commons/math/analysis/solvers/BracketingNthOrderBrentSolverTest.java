package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        new BracketingNthOrderBrentSolver().solve(1, null, 0.0, 1.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaximalOrder() {
        new BracketingNthOrderBrentSolver(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testMinimalOrder() {
        new BracketingNthOrderBrentSolver(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeAllowedSolution() {
        new BracketingNthOrderBrentSolver(1.0, -1);
    }

    @Test
    public void testBasicExample() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 5.0, -1.0}, 3);
        double root = 1.0;
        assertEquals(root, solver.solve(10, function, -2.0, 4.0, AllowedSolution.ANY_SIDE), 1e-6);
        assertEquals(root, solver.solve(10, function, -2.0, 4.0, root, AllowedSolution.ANY_SIDE), 1e-6);
    }

    @Test
    public void testFunctionValueAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5, 1e-8, 5);
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 5.0, -1.0}, 3);
        double root = 1.0;
        assertEquals(root, solver.solve(10, function, -2.0, 4.0, AllowedSolution.ANY_SIDE), 1e-6);
        assertEquals(root, solver.solve(10, function, -2.0, 4.0, root, AllowedSolution.ANY_SIDE), 1e-6);
        assertEquals(Precision.round(FunctionUtils.value(function, root), 14), PrecisionUtils.round(solver.getFunctionValue(), 14), 1e-8);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction function = new PolynomialFunction(new double[]{1.0, 0.0}, 1);
        double root = 0.0;
        solver.solve(10, function, -1.0, 1.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxEvalReached() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 5.0, -1.0}, 3);
        double root = 1.0;
        UnsupportedOperationException exception = null;
        try {
            solver.solve(0, function, -2.0, 4.0, AllowedSolution.ANY_SIDE);
        } catch (Exception e) {
            exception = (UnsupportedOperationException) e;
        }
        assertTrue(exception.getMessage().contains("maxEval"));
    }
}