package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit 4 test cases for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {
    private static final double DEVIATION_TOLERANCE = 1e-9;

    @Before
    public void setUp() {
        // Set the default method to RegulaFalsi for the tests
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
    }

    /**
     * Test the 'solve' method with a simple polynomial function.
     *
     * @throws ConvergenceException         If the method did not converge.
     * @throws MaxIterationsExceededException If the maximum number of iterations was exceeded.
     * @throws PrecisionException           If the precision could not be met.
     */
    @Test
    public void testSimplePolynomialFunction() throws ConvergenceException,
            MaxIterationsExceededException, PrecisionException {
        UnivariateRealFunction function = new PolynomialFunction(3, -2);
        double root = 1.0;
        double result = solver.solve(50, function, -5.0, 5.0, root);
        assertEquals(root, result, DEVIATION_TOLERANCE);
    }

    /**
     * Test the 'solve' method with a function that has roots on both sides.
     *
     * @throws ConvergenceException         If the method did not converge.
     * @throws MaxIterationsExceededException If the maximum number of iterations was exceeded.
     * @throws PrecisionException           If the precision could not be met.
     */
    @Test
    public void testFunctionWithRootsOnBothSides() throws ConvergenceException,
            MaxIterationsExceededException, PrecisionException {
        UnivariateRealFunction function = new PolynomialFunction(2, 4, 1);
        double root1 = 1.0;
        double root2 = 3.0;
        solver.setAllowedSolution(AllowedSolution.BOTH_SIDES);
        double result1 = solver.solve(50, function, -5.0, 5.0, Precision.SAFE_MIN);
        double result2 = solver.solve(50, function, -5.0, 5.0, Precision.SAFE_MAX);
        assertEquals(root1, result1, DEVIATION_TOLERANCE);
        assertEquals(root2, result2, DEVIATION_TOLERANCE);
    }

    /**
     * Test the 'solve' method with a function that has no roots in the given interval.
     *
     * @throws ConvergenceException         If the method did not converge.
     * @throws MaxIterationsExceededException If the maximum number of iterations was exceeded.
     * @throws PrecisionException           If the precision could not be met.
     */
    @Test(expected = ConvergenceException.class)
    public void testFunctionWithNoRoots() throws ConvergenceException,
            MaxIterationsExceededException, PrecisionException {
        UnivariateRealFunction function = new PolynomialFunction(1, 1);
        solver.solve(50, function, -5.0, 5.0, Precision.SAFE_MIN);
    }

    /**
     * Test the 'solve' method with a function that throws an exception.
     *
     * @throws ConvergenceException         If the method did not converge.
     * @throws MaxIterationsExceededException If the maximum number of iterations was exceeded.
     * @throws PrecisionException           If the precision could not be met.
     */
    @Test(expected = MathInternalError.class)
    public void testFunctionWithException() throws ConvergenceException,
            MaxIterationsExceededException, PrecisionException {
        UnivariateRealFunction function = new FunctionWithException();
        solver.solve(50, function, -5.0, 5.0, Precision.SAFE_MIN);
    }

    /**
     * Test the 'solve' method with the Illinois method.
     *
     * @throws ConvergenceException         If the method did not converge.
     * @throws MaxIterationsExceededException If the maximum number of iterations was exceeded.
     * @throws PrecisionException           If the precision could not be met.
     */
    @Test
    public void testIllinoisMethod() throws ConvergenceException,
            MaxIterationsExceededException, PrecisionException {
        UnivariateRealFunction function = new PolynomialFunction(3, -2);
        solver.setMethod(Method.ILLINOIS);
        double root = 1.0;
        double result = solver.solve(50, function, -5.0, 5.0, root);
        assertEquals(root, result, DEVIATION_TOLERANCE);
    }
}

class FunctionWithException implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        throw new RuntimeException("Test exception");
    }
}

enum AllowedSolution {
    LEFT_SIDE,
    RIGHT_SIDE,
    BELOW_SIDE,
    ABOVE_SIDE,
    ANY_SIDE,
    BOTH_SIDES;
}