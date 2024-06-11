package org.apache.commons.math.analysis.solvers.tests;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.UnbracketedException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.TestConstants;
import org.apache.commons.math3.util.test.OpenMathAssert;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.Method;

/**
 * Test for BaseSecantSolver class
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, Method)}
     */
    @Test
    public void testBaseSecantSolverConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(TestConstants.DOUBLE_EPSILON, Method.PEGASUS);
        assertNotNull(solver);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, Method)}
     */
    @Test
    public void testBaseSecantSolverConstructorWithRelativeAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(TestConstants.REAL_TOLERANCE, TestConstants.ABSOLUTE_TOLERANCE, Method.PEGASUS);
        assertNotNull(solver);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double, Method)}
     */
    @Test
    public void testBaseSecantSolverConstructorWithFunctionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(TestConstants.REAL_TOLERANCE, TestConstants.ABSOLUTE_TOLERANCE, TestConstants.FUNCTION_VALUE_TOLERANCE, Method.PEGASUS);
        assertNotNull(solver);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     */
    @Test
    public void testSolve() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(TestConstants.ABSOLUTE_TOLERANCE, Method.PEGASUS);
        double solution = solver.solve(100, f, -TestConstants.PI / 2, TestConstants.PI / 2);
        OpenMathAssert.assertEquals(Math.PI / 2, solution, TestConstants.PI / 4);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     */
    @Test
    public void testSolveWithAllowedSolution() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(TestConstants.ABSOLUTE_TOLERANCE, Method.PEGASUS, AllowedSolution.BELOW_SIDE);
        double solution = solver.solve(100, f, -TestConstants.PI / 2, TestConstants.PI / 2, -TestConstants.PI / 2);
        OpenMathAssert.assertEquals(Math.PI / 2, solution, TestConstants.PI / 4);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}
     */
    @Test(expected = UnbracketedException.class)
    public void testUnbracketedException() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(TestConstants.ABSOLUTE_TOLERANCE, Method.PEGASUS);
        solver.solve(100, f, 0, 0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with inverted interval
     */
    @Test
    public void testSolveWithInvertedInterval() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) + 0.5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(TestConstants.ABSOLUTE_TOLERANCE, Method.PEGASUS);
        double solution
            = solver.solve(100, f, TestConstants.PI / 2, TestConstants.PI / 2);
        OpenMathAssert.assertEquals(TestConstants.PI / 2, solution, TestConstants.PI / 4);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with inverted interval
     */
    @Test
    public void testSolveWithAllowedSolutionAndInvertedInterval() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) + 0.5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(TestConstants.ABSOLUTE_TOLERANCE, Method.PEGASUS, AllowedSolution.BELOW_SIDE);
        double solution
            = solver.solve(100, f, TestConstants.PI / 2, TestConstants.PI / 2, TestConstants.PI / 2);
        OpenMathAssert.assertEquals(TestConstants.PI / 2, solution, TestConstants.PI / 4);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with maxEval exception
     */
    @Test(expected = MaxEvalException.class)
    public void testMaxEvalException() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new RuntimeException();
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(TestConstants.ABSOLUTE_TOLERANCE, Method.PEGASUS);
        solver.solve(1, f, -1, 1);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with maxEval exception
     */
    @Test(expected = MaxEvalException.class)
    public void testMaxEvalExceptionWithAllowedSolution() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new RuntimeException();
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(TestConstants.ABSOLUTE_TOLERANCE, Method.PEGASUS, AllowedSolution.BELOW_SIDE);
        solver.solve(1, f, -1, 1, -1);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with
     * null argument
     */
    @Test(expected = NullArgumentException.class)
    public void testNullArgumentException() {
        BaseSecantSolver solver = new BaseSecantSolver(TestConstants.ABSOLUTE_TOLERANCE, Method.PEGASUS);
        solver.solve(100, null, -TestConstants.PI / 2, TestConstants.PI / 2);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with null argument
     */
    @Test(expected = NullArgumentException.class)
    public void testNullArgumentExceptionWithAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(TestConstants.ABSOLUTE_TOLERANCE, Method.PEGASUS, AllowedSolution.BELOW_SIDE);
        solver.solve(100, null, -TestConstants.PI / 2, TestConstants.PI / 2, -TestConstants.PI / 2);
    }
}