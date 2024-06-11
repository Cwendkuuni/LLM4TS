package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Test;

public class BaseSecantSolverTest {

    private static UnivariateRealFunction f(double[] coefficients) {
        return new PolynomialFunction(coefficients);
    }

    @Test
    public void testSolverWithRegulaFalsiMethod() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, new RegulaFalsiSolver.RegulaFalsiMethod());
        double[] coefficients = {1, -2, 1};
        UnivariateRealFunction function = f(coefficients);
        double min = -2.0;
        double max = 2.0;
        double expectedRoot = 1.0;

        double root = solver.solve(100, function, min, max, 0.0);
        assertTrue(FastMath.isClose(expectedRoot, root, 1e-9));
    }

    @Test(expected = NoBracketingException.class)
    public void testSolverWithoutBracketing() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, new RegulaFalsiSolver.RegulaFalsiMethod());
        double[] coefficients = {1, 1};
        UnivariateRealFunction function = f(coefficients);
        double min = 0.0;
        double max = 0.0;

        solver.solve(100, function, min, max, 0.0);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolverWithMaxEvalExceeded() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, new RegulaFalsiSolver.RegulaFalsiMethod());
        double[] coefficients = {1, -2, 1};
        UnivariateRealFunction function = f(coefficients);
        double min = -2.0;
        double max = 2.0;

        UnivariateRealFunction mockedFunction = mock(UnivariateRealFunction.class);
        when(mockedFunction.value(0.0)).thenThrow(new UnsupportedOperationException());
        when(mockedFunction.value(1.0)).thenReturn(Double.NaN);
        when(mockedFunction.value(2.0)).thenReturn(Double.NaN);

        solver.solve(0, mockedFunction, min, max, 0.0);
    }

    @Test
    public void testSolverAllowedSolutionLeftSide() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, new RegulaFalsiSolver.RegulaFalsiMethod());
        double[] coefficients = {1, -2, 1};
        UnivariateRealFunction function = f(coefficients);
        double min = -2.0;
        double max = 0.0;
        double expectedRoot = -1.0;

        double root = solver.solve(100, function, min, max, 0.0, AllowedSolution.LEFT_SIDE);
        assertTrue(FastMath.isClose(expectedRoot, root, 1e-9));
    }

    @Test
    public void testSolverAllowedSolutionRightSide() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, new RegulaFalsiSolver.RegulaFalsiMethod());
        double[] coefficients = {1, -2, 1};
        UnivariateRealFunction function = f(coefficients);
        double min = 0.0;
        double max = 2.0;
        double expectedRoot = 1.0;

        double root = solver.solve(100, function, min, max, 0.0, AllowedSolution.RIGHT_SIDE);
        assertTrue(FastMath.isClose(expectedRoot, root, 1e-9));
    }

    @Test
    public void testSolverAllowedSolutionBelowSide() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, new RegulaFalsiSolver.RegulaFalsiMethod());
        double[] coefficients = {1, -2, 1};
        UnivariateRealFunction function = f(coefficients);
        double min = -2.0;
        double max = 2.0;
        double expectedRoot = -1.0;

        double root = solver.solve(100, function, min, max, 0.0, AllowedSolution.BELOW_SIDE);
        assertTrue(FastMath.isClose(expectedRoot, root, 1e-9));
    }

    @Test
    public void testSolverAllowedSolutionAboveSide() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, new RegulaFalsiSolver.RegulaFalsiMethod());
        double[] coefficients = {1, -2, 1};
        UnivariateRealFunction function = f(coefficients);
        double min = -2.0;
        double max = 2.0;
        double expectedRoot = 1.0;

        double root = solver.solve(100, function, min, max, 0.0, AllowedSolution.ABOVE_SIDE);
        assertTrue(FastMath.isClose(expectedRoot, root, 1e-9));
    }

    @Test
    public void testSolverWithIllinoisMethod() {
        BaseSecantSolver solver = new IllinoisSolver(1e-6, new IllinoisSolver.IllinoisMethod());
        double[] coefficients = {1, -2, 1};
        UnivariateRealFunction function = f(coefficients);
        double min = -2.0;
        double max = 2.0;
        double expectedRoot = 1.0;

        double root = solver.solve(100, function, min, max, 0.0);
        assertTrue(FastMath.isClose(expectedRoot, root, 1e-9));
    }

    @Test
    public void testSolverWithPegasusMethod() {
        BaseSecantSolver solver = new PegasusSolver(1e-6, new PegasusSolver.PegasusMethod());
        double[] coefficients = {1, -2, 1};
        UnivariateRealFunction function = f(coefficients);
        double min = -2.0;
        double max = 2.0;
        double expectedRoot = 1.0;

        double root = solver.solve(100, function, min, max, 0.0);
        assertTrue(FastMath.isClose(expectedRoot, root, 1e-9));
    }

    @Test
    public void testSolverWithThreeRoots() {
        BaseSecantSolver solver = new PegasusSolver(1e-6, new PegasusSolver.PegasusMethod());
        double[] coefficients = {1, -3, 3, -2};
        UnivariateRealFunction function = f(coefficients);
        double min = -2.0;
        double max = 2.0;
        double[] expectedRoots = {-1.5, 0.5, 1.5};

        double[] roots = new double[3];
        for (int i = 0; i < 3; i++) {
            roots[i] = solver.solve(100, function, min, max, 0.0, AllowedSolution.ANY_SIDE);
        }

        assertArrayEquals(expectedRoots, roots, 1e-9);
    }

    @Test
    public void testSolverWithNegativeFunctionValues() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, new RegulaFalsiSolver.RegulaFalsiMethod());
        double[] coefficients = {-1, 2};
        UnivariateRealFunction function = f(coefficients);
        double min = -1.0;
        double max = 1.0;
        double expectedRoot = 0.0;

        double root = solver.solve(100, function, min, max, 0.0);
        assertTrue(FastMath.isClose(expectedRoot, root, 1e-9));
    }

    @Test
    public void testSolverWithZeroFunctionValue() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, new RegulaFalsiSolver.RegulaFalsiMethod());
        double[] coefficients = {1, 1, -2};
        UnivariateRealFunction function = f(coefficients);
        double min = -2.0;
        double max = 2.0;

        try {
            solver.solve(100, function, min, max, 0.0);
            assertFalse(true);
        } catch (NoBracketingException e) {
            // Expected
        }

        double root = solver.solve(100, function, min, max, 1.0);
        assertTrue(FastMath.isClose(1.0, root, 1e-9));
    }

    @Test
    public void testSolverWithInvertedInterval() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, new RegulaFalsiSolver.RegulaFalsiMethod());
        double[] coefficients = {1, -2, 1};
        UnivariateRealFunction function = f(coefficients);
        double min = 2.0;
        double max = -2.0;

        try {
            solver.solve(100, function, min, max, 0.0);
            assertFalse(true);
        } catch (NoBracketingException e) {
            // Expected
        }

        double root = solver.solve(100, function, min, max, 1.0, AllowedSolution.RIGHT_SIDE);
        assertTrue(FastMath.isClose(-1.0, root, 1e-9));
    }
}