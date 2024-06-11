package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test cases for the methods of the {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /**
     * Test constructor with default arguments.
     */
    @Test
    public void testDefaultConstructor() {
        final BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test constructor with custom arguments.
     */
    @Test
    public void testCustomConstructor() {
        final double relativeAccuracy = 0.01;
        final double absoluteAccuracy = 0.001;
        final double functionValueAccuracy = 0.0001;
        final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy,
                                                            functionValueAccuracy, BaseSecantSolver.Method.PEGASUS);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 0.0);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(functionValueAccuracy, solver.getFunctionValueAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.PEGASUS, solver.getMethod());
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with a simple function.
     */
    @Test
    public void testSolveSimpleFunction() {
        final UnivariateFunction function = new PolynomialFunction(x -> x * x - 4.0);
        final double min = -2.0;
        final double max = 2.0;
        final double expectedSolution = 1.0;

        final BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        final double actualSolution = solver.solve(function, min, max);
        assertEquals(expectedSolution, actualSolution, Precision.SAFE_EPSILON);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} with a simple function.
     */
    @Test
    public void testSolveSimpleFunctionAllowedSolution() {
        final UnivariateFunction function = new PolynomialFunction(x -> x * x - 4.0);
        final double min = -2.0;
        final double max = 2.0;
        final double startValue = -1.0;
        final AllowedSolution allowedSolution = AllowedSolution.LEFT_SIDE;

        final BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        final double actualSolution = solver.solve(function, min, max, startValue, allowedSolution);
        assertEquals(1.0, actualSolution, Precision.SAFE_EPSILON);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with a simple function that throws an exception.
     */
    @Test
    public void testSolveSimpleFunctionException() {
        final UnivariateFunction function = x -> {
            throw new ZeroException("Function has a zero in the given interval");
        };
        final double min = -2.0;
        final double max = 2.0;

        final BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(function, min, max));
    }

    /**
     * Test edge case with a function where the roots are very close to each other.
     */
    @Test
    public void testEdgeCaseWithCloseRoots() {
        final UnivariateFunction function = x -> x * x - 0.0001;
        final double min = -1.5;
        final double max = 1.5;
        final double expectedSolution = -1.0;

        final BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        final double actualSolution = solver.solve(function, min, max);
        assertEquals(expectedSolution, actualSolution, Precision.SAFE_EPSILON);
    }

    /**
     * Test edge case with a function where the roots are on opposite sides of the interval.
     */
    @Test
    public void testEdgeCaseWithRootsOnOppositeSides() {
        final UnivariateFunction function = x -> Math.pow(x, 3) - 2;
        final double min = -2.0;
        final double max = 2.0;
        final double expectedSolution1 = -1.0;
        final double expectedSolution2 = 1.0;

        final BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        assertThrows(IllegalArgumentException.class, () -> solver.solve(function, min, max));

        final BaseSecantSolver solver1 = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI, AllowedSolution.LEFT_SIDE);
        final double actualSolution1 = solver1.solve(function, min, max);
        assertEquals(expectedSolution1, actualSolution1, Precision.SAFE_EPSILON);

        final BaseSecantSolver solver2 = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI, AllowedSolution.RIGHT_SIDE);
        final double actualSolution2 = solver2.solve(function, min, max);
        assertEquals(expectedSolution2, actualSolution2, Precision.SAFE_EPSILON);
    }

    /**
     * Test edge case with a function where the roots are at the endpoints of the interval.
     */
    @Test
    public void testEdgeCaseWithRootsAtEndpoints() {
        final UnivariateFunction function = x -> x * x - 4.0;
        final double min = -2.0;
        final double max = 2.0;
        final double expectedSolution1 = -2.0;
        final double expectedSolution2 = 2.0;

        final BaseSecantSolver solver1 = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS, AllowedSolution.LEFT_SIDE);
        final double actualSolution1 = solver1.solve(function, min, max);
        assertEquals(expectedSolution1, actualSolution1, Precision.SAFE_EPSILON);

        final BaseSecantSolver solver2 = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS, AllowedSolution.RIGHT_SIDE);
        final double actualSolution2 = solver2.solve(function, min, max);
        assertEquals(expectedSolution2, actualSolution2, Precision.SAFE_EPSILON);
    }

    /**
     * Test edge case with a function that has a root at the midpoint of the interval.
     */
    @Test
    public void testEdgeCaseWithRootAtMidpoint() {
        final UnivariateFunction function = x -> x * x - 4.0;
        final double min = -2.0;
        final double max = 2.0;
        final double expectedSolution = 0.0;

        final BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        final double actualSolution = solver.solve(function, min, max);
        assertEquals(expectedSolution, actualSolution, Precision.SAFE_EPSILON);
    }

}