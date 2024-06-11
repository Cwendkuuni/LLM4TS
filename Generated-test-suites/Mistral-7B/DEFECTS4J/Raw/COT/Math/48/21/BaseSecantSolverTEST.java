package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {
    // Tested class under consideration
    private BaseSecantSolver baseSecantSolver;

    @Before
    public void setup() {
        baseSecantSolver = new BaseSecantSolver.RegulaFalsiSolver(1e-6);
    }

    // Testing the constructor
    @Test
    public void testConstructor() {
        assertEquals("Incorrect constructor for RegulaFalsiSolver",
                RegulaFalsiSolver.class, baseSecantSolver.getClass());
    }

    // Testing the solve method with a simple polynomial function
    @Test
    public void testSolvePolynomial() throws MaxEvalExceededException,
            UnsupportedOperationException {
        UnivariateFunction f = new PolynomialFunction(x -> Math.pow(x, 2) - 4);
        double actual = baseSecantSolver.solve(10, f, -2.0, 2.0, 0.0);
        double expected = -1.0;
        assertEquals("Incorrect solution for polynomial function", expected,
                actual, 1e-6);
    }

    // Testing the solve method with a function that has a root at the minimum
    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveMinimumRoot() throws MaxEvalExceededException {
        UnivariateFunction f = x -> Math.pow(x, 2) - 1e-12;
        baseSecantSolver.solve(10, f, -1.0, 1.0, 0.0);
    }

    // Testing the solve method with a function that has a root at the maximum
    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveMaximumRoot() throws MaxEvalExceededException {
        UnivariateFunction f = x -> Math.pow(x, 2) + 1e-12;
        baseSecantSolver.solve(10, f, -1.0, 1.0, 0.0);
    }

    // Testing the solve method with a bracketed function
    @Test
    public void testSolveBracketedFunction() throws MaxEvalExceededException,
            UnsupportedOperationException {
        UnivariateFunction f = x -> Math.pow(x, 2) - 4;
        double actual = baseSecantSolver.solve(10, f, -2.0, 2.0, 0.0);
        double expected = -1.0;
        assertEquals("Incorrect solution for bracketed function", expected,
                actual, 1e-6);
    }

    // Testing the solve method with a function that has multiple roots
    @Test
    public void testSolveMultipleRoots() throws MaxEvalExceededException,
            UnsupportedOperationException {
        UnivariateFunction f = x -> Math.pow(x, 3) - 2 * Math.pow(x, 2)
                + Math.pow(x, 1) + 1;
        double actual = baseSecantSolver.solve(10, f, -3.0, 3.0, 1.0);
        double expectedRoot1 = -1.0;
        double expectedRoot2 = 1.0;
        double tolerance = 1e-6;
        assertEquals("Incorrect solution for multiple roots (Root 1)",
                expectedRoot1, actual, tolerance);
        actual = baseSecantSolver.solve(10, f, -3.0, 3.0, 1.5);
        assertEquals("Incorrect solution for multiple roots (Root 2)",
                expectedRoot2, actual, tolerance);
    }

    // Testing the solve method with a non-bracketed function
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveNonBracketedFunction() throws MaxEvalExceededException {
        UnivariateFunction f = x -> Math.sin(x);
        baseSecantSolver.solve(10, f, 0.0, 1.0, 0.0);
    }
}