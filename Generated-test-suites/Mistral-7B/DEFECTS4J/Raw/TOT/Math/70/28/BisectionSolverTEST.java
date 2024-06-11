package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.special.E;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link BisectionSolver} class.
 */
public class BisectionSolverTest {

    /**
     * Test for solving simple function.
     */
    @Test
    public void testSimpleFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            double root = solver.solve(f, -2.0, 2.0);
            Assert.assertEquals("The result is not correct.", -1.0, root, 1e-7);
        } catch (MaxIterationsExceededException e) {
            Assert.fail("The bisection method should converge for this function.");
        }
    }

    /**
     * Test for solving function with root outside of the given interval.
     */
    @Test
    public void testRootOutside() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(f, 0.0, 1.0);
            Assert.fail("An exception should be thrown because the root is outside the given interval.");
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }

    /**
     * Test for solving function with root at the edge of the given interval.
     */
    @Test
    public void testRootAtEdge() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(Math.abs(x)) - 1.0;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            double root = solver.solve(f, -1.0, 1.0);
            Assert.assertEquals("The result is not correct.", 0.0, root, 1e-7);
        } catch (MaxIterationsExceededException e) {
            Assert.fail("The bisection method should converge for this function.");
        }
    }

    /**
     * Test for solving function with root very close to the edge of the given interval.
     */
    @Test
    public void testRootVeryCloseToEdge() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(Math.abs(x)) - 1.0;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double maxIterations = solver.getMaxIterations();
        try {
            solver.setMaxIterations(maxIterations * 2);
            double root = solver.solve(f, -1.01, 1.01);
            Assert.assertEquals("The result is not correct.", 0.0, root, 1e-7);
            solver.setMaxIterations(maxIterations);
        } catch (MaxIterationsExceededException e) {
            // expected
        }
    }

    /**
     * Test for solving function with root as a NaN value.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testNaNFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new ArithmeticException("Divide by zero");
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, 0.0, 1.0);
    }

    /**
     * Test for solving function with root as a negative number.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testNegativeRoot() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(x + 1) - 1.0;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(f, -1.0, 0.0);
            Assert.fail("An exception should be thrown because the root is negative.");
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }

    /**
     * Test for solving function with infinite root.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testInfiniteRoot() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 1.0 / x;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(f, 0.0, 1.0);
            Assert.fail("An exception should be thrown because the root is infinite.");
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }

    /**
     * Test for solving function with root between zero and a very small number.
     */
    @Test
    public void testSmallRoot() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - E.EULER;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            double root = solver.solve(f, 1.0e-10, 0.0);
            Assert.assertEquals("The result is not correct.", 0.0, root, 1e-10);
        } catch (MaxIterationsExceededException e) {
            Assert.fail("The bisection method should converge for this function.");
        }
    }

    /**
     * Test for solving function with root as a very small number.
     */
    @Test
    public void testVerySmallRoot() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            double root = solver.solve(f, -1.0e-6, 1.0e-6);
            Assert.assertTrue("The result is not correct.", Math.abs(root) > 1e-12);
        } catch (MaxIterationsExceededException e) {
            Assert.fail("The bisection method should converge for this function.");
        }
    }
}