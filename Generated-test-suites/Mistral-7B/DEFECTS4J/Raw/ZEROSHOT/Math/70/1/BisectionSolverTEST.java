package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.UnivariateRealSolverUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for BisectionSolver.
 */
public class BisectionSolverTest {

    /**
     * Test solver with a function that has one root in the given interval.
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testSolveOneRoot() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1, 2) - 1;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, -2.0, 1.0);
        Assert.assertEquals(0.0, Precision.doubleEquals(root, 0.0, 1e-5), "The root should be 0.");
    }

    /**
     * Test solver with a function that has no roots in the given interval.
     * @throws Exception if an error occurs during the test.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveNoRoots() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, -2.0, 2.0);
    }

    /**
     * Test solver with a function that has multiple roots in the given interval.
     * @throws Exception if an error occurs during the test.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMultipleRoots() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1, 3) - 1;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, -1.5, 1.5);
    }

    /**
     * Test solver with a function that has a root at the endpoint.
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testSolveRootAtEndpoint() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, -1.0, 1.0);
        Assert.assertEquals(0.0, Precision.doubleEquals(root, 0.0, 1e-5), "The root should be 0.");
    }

    /**
     * Test solver with a function that has a root very close to an endpoint.
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testSolveRootCloseToEndpoint() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) - 0.1;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, -1.0, 0.0);
        Assert.assertEquals(0.0, Precision.doubleEquals(root, 0.05, 1e-5), "The root should be close to 0.05.");
    }
}