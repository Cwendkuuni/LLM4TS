package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.junit.Test;

/**
 * Test suite for BracketingNthOrderBrentSolver class
 */
public class BracketingNthOrderBrentSolverTest {

    /**
     * Test constructor with default arguments
     */
    @Test
    public void testDefaultConstructor() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test constructor with specified arguments
     */
    @Test
    public void testConstructor() {
        final double absAccuracy = 1e-8;
        final int maximalOrder = 10;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(absAccuracy, maximalOrder);
        assertEquals(absAccuracy, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(maximalOrder, solver.getMaximalOrder());
    }

    /**
     * Test constructor with invalid maximal order
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidMaximalOrder() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1);
    }

    /**
     * Test solve method with a simple polynomial function
     */
    @Test
    public void testSolveWithSimplePolynomialFunction() {
        final double absoluteAccuracy = 1e-8;
        final double maxEval = 10;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(absoluteAccuracy);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0}, new double[]{0.0, 1.0, 1.0});
        double[] roots = {-sqrt(2.0), sqrt(2.0)};
        for (double root : roots) {
            double result = solver.solve(maxEval, f, -1.0, 1.0);
            assertEquals(root, result, absoluteAccuracy);
        }
    }

    /**
     * Test solve method with a function that has no roots in the given interval
     */
    @Test
    public void testSolveWithFunctionNoRoots() {
        final double absoluteAccuracy = 1e-8;
        final double maxEval = 10;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(absoluteAccuracy);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{0.0, 1.0, 1.0});
        double result = solver.solve(maxEval, f, -1.0, 1.0);
        assertEquals(Double.NaN, result, 1e-9);
    }

    /**
     * Test solve method with a function that brackets a root
     */
    @Test
    public void testSolveWithFunctionBracketsRoot() {
        final double absoluteAccuracy = 1e-8;
        final double maxEval = 10;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(absoluteAccuracy);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0}, new double[]{0.0, 1.0, 1.0});
        double[] roots = {-sqrt(2.0), sqrt(2.0)};
        for (double root : roots) {
            double[] interval = {-1.5 * root, 1.5 * root};
            double result = solver.solve(maxEval, f, interval[0], interval[1]);
            assertEquals(root, result, absoluteAccuracy);
        }
    }

    /**
     * Test solve method with a function that has roots on the edges of the interval
     */
    @Test
    public void testSolveWithFunctionRootsOnEdges() {
        final double absoluteAccuracy = 1e-8;
        final double maxEval = 10;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(absoluteAccuracy);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0}, new double[]{0.0, 1.0, 1.0});
        double[] roots = {-sqrt(2.0), sqrt(2.0)};
        for (double root : roots) {
            double[] interval = {root - 1e-6, root + 1e-6};
            double result = solver.solve(maxEval, f, interval[0], interval[1]);
            assertEquals(root, result, absoluteAccuracy);
        }
    }

    /**
     * Test solve method with a function that has roots at the end of the interval
     */
    @Test
    public void testSolveWithFunctionRootsAtEnd() {
        final double absoluteAccuracy = 1e-8;
        final double maxEval = 10;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(absoluteAccuracy);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0}, new double[]{0.0, 1.0, 1.0});
        double[] roots = {-sqrt(2.0), sqrt(2.0)};
        for (int i = 0; i < roots.length; ++i) {
            double root = roots[i];
            double[] interval = {1.0, 2.0};
            double result = solver.solve(maxEval, f, interval[0], interval[1]);
            assertEquals(root, result, absoluteAccuracy);
        }
    }

    /**
     * Test solve method with a function that has roots at the beginning of the interval
     */
    @Test
    public void testSolveWithFunctionRootsAtBeginning() {
        final double absoluteAccuracy = 1e-8;
        final double maxEval = 10;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(absoluteAccuracy);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0}, new double[]{0.0, 1.0, 1.0});
        double[] roots = {-sqrt(2.0), sqrt(2.0)};
        for (int i = 0; i < roots.length; ++i) {
            double root = roots[i];
            double[] interval = {-1.0, 0.0};
            double result = solver.solve(maxEval, f, interval[0], interval[1]);
            assertEquals(root, result, absoluteAccuracy);
        }
    }

    /**
     * Test solve method with a function that has a singularity in the interval
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveWithFunctionSingularity() {
        final double absoluteAccuracy = 1e-8;
        final double maxEval = 10;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(absoluteAccuracy);
        UnivariateFunction f = new PolynomialFunction(new double[]{0.0, 1.0, 0.0}, new double[]{0.0});
        double[] interval = {-1.0, 1.0};
        solver.solve(maxEval, f, interval[0], interval[1]);
    }

    /**
     * Test solve method with a function that has a vertical asymptote in the interval
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveWithFunctionVerticalAsymptote() {
        final double absoluteAccuracy = 1e-8;
        final double maxEval = 10;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(absoluteAccuracy);
        UnivariateFunction f = new PolynomialFunction(new double[]{0.0, 0.0, 1.0}, new double[]{0.0});
        double[] interval = {-1.0, 1.0};
        solver.solve(maxEval, f, interval[0], interval[1]);
    }
}