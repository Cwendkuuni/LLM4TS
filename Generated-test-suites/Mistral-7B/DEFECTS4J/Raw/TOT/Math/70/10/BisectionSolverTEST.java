package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.UnivariateRealSolverUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link BisectionSolver}.
 */
public class BisectionSolverTest {

    /**
     * Test solver with a simple function.
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testBisectionSolverWithSimpleFunction() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double expectedResult = 0.0;
        double result = solver.solve(f, -1.0, 1.0);
        Assert.assertEquals(expectedResult, Precision.round(result, 12), 0.0);
    }

    /**
     * Test solver with a function with roots at boundary of the interval.
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testBisectionSolverWithFunctionRootsAtBoundary() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 0.001;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double expectedResult = 1.0;
        double result = solver.solve(f, 0.0, 2.0);
        Assert.assertEquals(expectedResult, Precision.round(result, 12), 0.0);
    }

    /**
     * Test solver when the interval does not bracket a root.
     * @throws Exception if an error occurs during the test.
     */
    @Test(expected = NoRootsException.class)
    public void testBisectionSolverWithIntervalNotBracketingRoot() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, 1.0, 1.0);
    }

    /**
     * Test solver with a function that is not continuous.
     * @throws Exception if an error occurs during the test.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testBisectionSolverWithFunctionNotContinuous() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x > 0.0) {
                    throw new UnsupportedOperationException("Function is not defined for positive x");
                }
                return Math.pow(x, 2) - 1;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -1.0, 1.0);
    }

    /**
     * Test solver with a function that throws an exception.
     * @throws Exception if an error occurs during the test.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testBisectionSolverWithFunctionThrowingException() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws Exception {
                // for this test, we don't use the exception message
                // to check the result, since it's not specified.
                throw new NumberIsTooLargeException();
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -1.0, 1.0);
    }
}