package org.apache.commons.math.analysis.solvers.tests;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RoundingMode;
import org.apache.commons.math3.util.Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math.analysis.solvers.SecantSolver;
import org.apache.commons.math.analysis.solvers.TestFunctions;
import org.apache.commons.math.analysis.solvers.allowed.AllowedSolution;
import org.apache.commons.math.analysis.solvers.allowed.AllowedSolution.AllowedSide;

import java.util.Arrays;
import java.util.Collection;

/**
 * Test class for the {@link BaseSecantSolver} class.
 */
@RunWith(Parameterized.class)
public class BaseSecantSolverTest {

    /** The test method to use. */
    private BaseSecantSolver solver;

    /** The test function to use. */
    private UnivariateRealFunction function;

    /** The maximum number of evaluations allowed. */
    private int maxEval;

    /** The minimum value of the interval. */
    private double min;

    /** The maximum value of the interval. */
    private double max;

    /** The allowed solution. */
    private AllowedSolution allowed;

    /**
     * Constructor.
     *
     * @param function The test function to use.
     * @param maxEval The maximum number of evaluations allowed.
     * @param min The minimum value of the interval.
     * @param max The maximum value of the interval.
     * @param allowed The allowed solution.
     */
    public BaseSecantSolverTest(UnivariateRealFunction function, int maxEval,
                               double min, double max, AllowedSolution allowed) {
        this.solver = new RegulaFalsiSolver(1e-6, function);
        this.function = function;
        this.maxEval = maxEval;
        this.min = min;
        this.max = max;
        this.allowed = allowed;
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method.
     *
     * @throws Exception If an error occurs.
     */
    @Test(expected = ConvergenceException.class)
    public void testSolveThrowsConvergenceException() throws Exception {
        solver.solve(maxEval, function, min, max, allowed);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} method.
     *
     * @throws Exception If an error occurs.
     */
    @Test
    public void testSolve() throws Exception {
        double root = solver.solve(maxEval, function, min, max, allowed);
        double tolerance = Precision.safeEpsilon();
       double expected = function.getValue(root);
        assertEquals("Failed to find root", 0.0, expected, tolerance);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with a function
     * that has a root between the specified bounds.
     *
     * @throws Exception If an error occurs.
     */
    @Test
    public void testSolveWithRootInside() throws Exception {
        double root = solver.solve(maxEval, function, min, max, AllowedSolution.ANY_SIDE);
        double tolerance = Precision.safeEpsilon();
        double expected = function.getValue(root);
        assertEquals("Failed to find root", 0.0, expected, tolerance);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with a function
     * that has a root outside the specified bounds.
     *
     * @throws Exception If an error occurs.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveWithRootOutside() throws Exception {
        solver.solve(maxEval, function, min, max, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} method
     * with a function that has a root on one of the specified bounds.
     *
     * @throws Exception If an error occurs.
     */
    @Test
    public void testSolveWithRootOnBound() throws Exception {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, function);
        double root = solver.solve(maxEval, function, min, max,
                min + 1e-6, AllowedSolution.LEFT_SIDE);
        double tolerance = Precision.safeEpsilon();
        double expected = function.getValue(root);
        assertEquals("Failed to find root", 0.0, expected, tolerance);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} method
     * with a function that has a root on the other specified bound.
     *
     * @throws Exception If an error occurs.
     */
    @Test
    public void testSolveWithRootOnOtherBound() throws Exception {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, function);
        double root = solver.solve(maxEval, function, min, max,
                min - 1e-6, AllowedSolution.RIGHT_SIDE);
        double tolerance = Precision.safeEpsilon();
        double expected = function.getValue(root);
        assertEquals("Failed to find root", 0.0, expected, tolerance);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} method
     * with a function that has no roots in the specified interval.
     *
     * @throws Exception If an error occurs.
     */
    @Test(expected = MaxEvalException.class)
    public void testSolveWithNoRoots() throws Exception {
        solver.solve(maxEval, function, min, max, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} method
     * with a function that is not monotonic between the specified bounds.
     *
     * @throws Exception If an error occurs.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveWithNonMonotonicFunction() throws Exception {
        UnivariateRealFunction squaredFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        double[] minMax = TestFunctions.minMax(squaredFunction, 0.0, 1.0);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, squaredFunction);
        solver.solve(maxEval, squaredFunction, minMax[0], minMax[1], AllowedSolution.ANY_SIDE);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with the
     * {@link RegulaFalsiSolver} method.
     */
    @Parameterized.Parameters(name = "{index}: function({0}) with min={1}, max={2} and allowed solution={3}")
    public static Collection<Object[]> testDataRegulaFalsi() {
        return Arrays.asList(new Object[][]{
                {new TestFunctions.SquareRootFunction(), 10, 2.0, 3.0, AllowedSolution.ANY_SIDE},
                {new TestFunctions.SquareRootFunction(), 100, 2.0, 3.0, AllowedSolution.ANY_SIDE},
                {new TestFunctions.SinFunction(), 10, -1.0, 1.0, AllowedSolution.ANY_SIDE},
                {new TestFunctions.SinFunction(), 100, -1.0, 1.0, AllowedSolution.ANY_SIDE},
                {new TestFunctions.CubicFunction(), 10, -2.0, 1.0, AllowedSolution.ANY_SIDE},
                {new TestFunctions.CubicFunction(), 100, -2.0, 1.0, AllowedSolution.ANY_SIDE},
                {new TestFunctions.LogarithmFunction(), 10, 1e-5, 0.5, AllowedSolution.LEFT_SIDE},
                {new TestFunctions.LogarithmFunction(), 100, 1e-5, 0.5, AllowedSolution.LEFT_SIDE},
                {new TestFunctions.LogarithmFunction(), 10, 1e-5, 0.5, AllowedSolution.RIGHT_SIDE},
                {new TestFunctions.LogarithmFunction(), 100, 1e-5, 0.5, AllowedSolution.RIGHT_SIDE},
                {new TestFunctions.ExpFunction(), 10, 1e-8, 1.0, AllowedSolution.ABOVE_SIDE},
                {new TestFunctions.ExpFunction(), 100, 1e-8, 1.0, AllowedSolution.ABOVE_SIDE},
                {new TestFunctions.ExpFunction(), 10, 1e-8, 1.0, AllowedSolution.BELOW_SIDE},
                {new TestFunctions.ExpFunction(), 100, 1e-8, 1.0, AllowedSolution.BELOW_SIDE}
        });
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with the
     * {@link IllinoisSolver} method.
     */
    @Parameterized.Parameters(name = "{index}: function({0}) with min={1}, max={2} and allowed solution={3}")
    public static Collection<Object[]> testDataIllinois() {
        return Arrays.asList(new Object[][]{
                {new TestFunctions.SquareRootFunction(), 100, 2.0, 3.0, AllowedSolution.ANY_SIDE},
                {new TestFunctions.SquareRootFunction(), 1000, 2.0, 3.0, AllowedSolution.ANY_SIDE},
                {new TestFunctions.SinFunction(), 100, -1.0, 1.0, AllowedSolution.ANY_SIDE},
                {new TestFunctions.SinFunction(), 1000, -1.0, 1.0, AllowedSolution.ANY_SIDE},
                {new TestFunctions.CubicFunction(), 100, -2.0, 1.0, AllowedSolution.ANY_SIDE},
                {new TestFunctions.CubicFunction(), 1000, -2.0, 1.0, AllowedSolution.ANY_SIDE},
                {new TestFunctions.LogarithmFunction(), 100, 1e-5, 0.5, AllowedSolution.LEFT_SIDE},
                {new TestFunctions.LogarithmFunction(), 1000, 1e-5, 0.5, AllowedSolution.LEFT_SIDE},
                {new TestFunctions.LogarithmFunction(), 100, 1e-5, 0.5, AllowedSolution.RIGHT_SIDE},
                {new TestFunctions.LogarithmFunction(), 1000, 1e-5, 0.5, AllowedSolution.RIGHT_SIDE},
                {new TestFunctions.ExpFunction(), 100, 1e-8, 1.0, AllowedSolution.ABOVE_SIDE},
                {new TestFunctions.ExpFunction(), 1000, 1e-8, 1.0, AllowedSolution.ABOVE_SIDE},
                {new TestFunctions.ExpFunction(), 100, 1e-8, 1.0, AllowedSolution.BELOW_SIDE},
                {new TestFunctions.ExpFunction(), 1000, 1e-8, 1.0, AllowedSolution.BELOW_SIDE}
        });
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with the
     * {@link PegasusSolver} method.
     */
    @Parameterized.Parameters(name = "{index}: function({0}) with min={1}, max={2} and allowed solution={3}")
    public static Collection<Object[]> testDataPegasus() {
        return Arrays.asList(new Object[][]{
                {new TestFunctions.SquareRootFunction(), 100, 2.0, 3.0, AllowedSolution.ANY_SIDE},
                {new TestFunctions.SquareRootFunction(), 1000, 2.0, 3.0, AllowedSolution.ANY_SIDE},
                {new TestFunctions.SinFunction(), 100, -1.0, 1.0, AllowedSolution.ANY_SIDE},
                {new TestFunctions.SinFunction(), 1000, -1.0, 1.0, AllowedSolution.ANY_SIDE},
                {new TestFunctions.CubicFunction(), 100, -2.0, 1.0, AllowedSolution.ANY_SIDE},
                {new TestFunctions.CubicFunction(), 1000, -2.0, 1.0, AllowedSolution.ANY_SIDE},
                {new TestFunctions.LogarithmFunction(), 100, 1e-5, 0.5, AllowedSolution.LEFT_SIDE},
                {new TestFunctions.LogarithmFunction(), 1000, 1e-5, 0.5, AllowedSolution.LEFT_SIDE},
                {new TestFunctions.LogarithmFunction(), 100, 1e-5, 0.5, AllowedSolution.RIGHT_SIDE},
                {new TestFunctions.LogarithmFunction(), 1000, 1e-5, 0.5, AllowedSolution.RIGHT_SIDE},
                {new TestFunctions.ExpFunction(), 100, 1e-8, 1.0, AllowedSolution.ABOVE_SIDE},
                {new TestFunctions.ExpFunction(), 1000, 1e-8, 1.0, AllowedSolution.ABOVE_SIDE},
                {new TestFunctions.ExpFunction(), 100, 1e-8, 1.0, AllowedSolution.BELOW_SIDE},
                {new TestFunctions.ExpFunction(), 1000, 1e-8, 1.0, AllowedSolution.BELOW_SIDE}
        });
    }

}