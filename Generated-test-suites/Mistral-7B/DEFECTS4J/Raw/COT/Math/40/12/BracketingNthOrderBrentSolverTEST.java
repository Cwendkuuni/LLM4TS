package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit test cases for the BracketingNthOrderBrentSolver class
 */
public class BracketingNthOrderBrentSolverTest {

    private BracketingNthOrderBrentSolver solver;
    private UnivariateFunction function;

    @Before
    public void setUp() {
        solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        function = new PolynomialFunction(new double[]{-1, 4, -1}, new double[]{-1, 6, 1});
    }

    /**
     * Test the constructor with default arguments.
     */
    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-8);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test the constructor with specified arguments.
     */
    @Test
    public void testSpecifiedConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 1e-8);
        assertEquals(7, solver.getMaximalOrder());
    }

    /**
     * Test the solve method with a simple polynomial function.
     */
    @Test
    public void testSolveSimplePolynomial() {
        double expectedResult = -1;
        double result = solver.solve(MaxCountExceededException.MAX_COUNT, function, -2, 2);
        assertEquals(expectedResult, result, 1e-8);
    }

    /**
     * Test the solve method with a polynomial function with roots at the endpoints.
     */
    @Test
    public void testSolvePolynomialWithRootsAtEndpoints() {
        double expectedResult1 = -1.3162277660168379;
        double expectedResult2 = 1.3162277660168379;
        double[] expectedRoots = {expectedResult1, expectedResult2};
        double[] roots = new double[2];
        solver.solve(MaxCountExceededException.MAX_COUNT, function, -3, 3, roots);
        assertArrayEquals(expectedRoots, roots, 1e-8);
    }

    /**
     * Test the solve method with a polynomial function with roots between the endpoints.
     */
    @Test
    public void testSolvePolynomialWithRootsBetweenEndpoints() {
        double expectedResult = -0.6264150992104975;
        double result = solver.solve(MaxCountExceededException.MAX_COUNT, function, -2, 2);
        assertEquals(expectedResult, result, 1e-8);
    }

    /**
     * Test the solve method with a polynomial function with roots at the start value.
     */
    @Test
    public void testSolvePolynomialWithRootAtStartValue() {
        double startValue = 0;
        double expectedResult = 0;
        double result = solver.solve(MaxCountExceededException.MAX_COUNT, function, startValue, startValue);
        assertEquals(expectedResult, result, 1e-8);
    }

    /**
     * Test the solve method with a polynomial function with roots at the min value.
     */
    @Test
    public void testSolvePolynomialWithRootAtMinValue() {
        double min = -2;
        double expectedResult = -2;
        double result = solver.solve(MaxCountExceededException.MAX_COUNT, function, min, min);
        assertEquals(expectedResult, result, 1e-8);
    }

    /**
     * Test the solve method with a polynomial function with roots at the max value.
     */
    @Test
    public void testSolvePolynomialWithRootAtMaxValue() {
        double max = 2;
        double expectedResult = 2;
        double result = solver.solve(MaxCountExceededException.MAX_COUNT, function, max, max);
        assertEquals(expectedResult, result, 1e-8);
    }

    /**
     * Test the solve method with a non-bracketing interval.
     */
    @Test(expected = NoBracketingException.class)
    public void testSolveNonBracketingInterval() {
        double min = -2;
        double max = -1;
        solver.solve(MaxCountExceededException.MAX_COUNT, function, min, max);
    }

    /**
     * Test the solve method with a function that is not differentiable at one of the roots.
     */
    @Test
    public void testSolveNonDifferentiableFunction() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.abs(x - 0.5), 0.5);
            }

            @Override
            public double derivative(double x) {
                throw new UnsupportedOperationException();
            }
        };
        double expectedResult = 0.5;
        double result = solver.solve(MaxCountExceededException.MAX_COUNT, function, -1, 1);
        assertEquals(expectedResult, result, 1e-8);
    }

}