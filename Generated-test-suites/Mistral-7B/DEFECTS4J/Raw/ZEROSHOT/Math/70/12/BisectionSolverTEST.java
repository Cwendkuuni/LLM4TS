package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.PrecisionRequiredException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.util.Precision;

/**
 * Test file for the BisectionSolver class.
 */
public class BisectionSolverTest {

    /**
     * Test solver with a simple polynomial function.
     *
     * @throws Exception if an error occurs during the test.
     */
    @org.junit.Test
    public void testBisectionSolverPolynomial() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{"0.0", "1.0", "-1.0", "1.0", "-1.0", "1.0", "-1.0", "1.0"}, 8);
        BisectionSolver solver = new BisectionSolver();

        // test with initial guess inside the interval
        double root = 0.5;
        double[] result = solver.solve(f, -1.0, 1.0);
        assertNotNull(result);
        assertEquals(root, result[0], Precision.SAFE_EPSILON);
        assertEquals(result.length, 2);
        assertEquals(solver.getIterations(), result[1]);

        // test with initial guess outside the interval
        root = -1.2;
        result = solver.solve(f, -1.0, 1.0, root);
        assertNotNull(result);
        assertEquals(root, result[0], Precision.SAFE_EPSILON);
        assertEquals(result.length, 2);
        assertEquals(solver.getIterations(), result[1]);

        // test with function that has no real root in the interval
        f = new PolynomialFunction(new double[]{"0.0", "1.0", "1.0", "1.0", "1.0", "1.0", "1.0", "1.0"}, 8);
        solver = new BisectionSolver();
        try {
            solver.solve(f, -1.0, 1.0);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // expected
        }

        // test with function that has a root on the boundary
        f = new PolynomialFunction(new double[]{"0.0", "1.0", "-1.0", "1.0", "-1.0", "1.0", "-1.0", "1.0"}, 8);
        solver = new BisectionSolver();
        result = solver.solve(f, -1.0, 1.0);
        assertNotNull(result);
        assertEquals(preciseSubtraction(1.0, result[0], 1E-6), 0.0, Precision.SAFE_EPSILON);
        assertEquals(result.length, 2);
        assertEquals(solver.getIterations(), result[1]);

        // test with function that has a root close to one boundary
        f = new PolynomialFunction(new double[]{"0.0", "1.0", "-1.0", "1.0", "-1.0", "1.0", "-1.0", "1.0"}, 8);
        solver = new BisectionSolver();
        result = solver.solve(f, -1.0, 0.0);
        assertNotNull(result);
        assertEquals(preciseSubtraction(0.0, result[0], 1E-6), 1.0, Precision.SAFE_EPSILON);
        assertEquals(result.length, 2);
        assertEquals(solver.getIterations(), result[1]);

        // test with function that has a root close to the other boundary
        f = new PolynomialFunction(new double[]{"0.0", "1.0", "-1.0", "1.0", "-1.0", "1.0", "-1.0", "1.0"}, 8);
        solver = new BisectionSolver();
        result = solver.solve(f, 0.0, 1.0);
        assertNotNull(result);
        assertEquals(preciseSubtraction(0.0, result[0], 1E-6), -1.0, Precision.SAFE_EPSILON);
        assertEquals(result.length, 2);
        assertEquals(solver.getIterations(), result[1]);

        // test with function that has two roots in the interval
        f = new PolynomialFunction(new double[]{"0.0", "1.0", "-2.0", "2.0", "-2.0", "2.0", "-2.0", "2.0"}, 8);
        solver = new BisectionSolver();
        try {
            solver.solve(f, -1.0, 1.0);
            fail("Expected NumberIsTooSmallException or PrecisionRequiredException");
        } catch (NumberIsTooSmallException | PrecisionRequiredException e) {
            // expected
        }

        // test with function that has a root very close to the boundary
        f = new PolynomialFunction(new double[]{"0.0", "1.0", "-1.0e-9", "1.0", "-1.0", "1.0", "-1.0", "1.0"}, 8);
        solver = new BisectionSolver();
        result = solver.solve(f, -1.0, 1.0);
        assertNotNull(result);
        assertEquals(preciseSubtraction(1.0, result[0], 1E-9), 0.0, Precision.SAFE_EPSILON);
        assertEquals(result.length, 2);
        assertEquals(solver.getIterations(), result[1]);
    }

    /**
     * Test solver with a function that has a root outside the interval.
     *
     * @throws Exception if an error occurs during the test.
     */
    @org.junit.Test
    public void testBisectionSolverOutsideInterval() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{"0.0", "1.0", "-1.0", "1.0", "-1.0", "1.0", "-1.0", "1.0"}, 8);
        BisectionSolver solver = new BisectionSolver();

        // test with interval that does not contain the root
        try {
            solver.solve(f, -2.0, 2.0);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // expected
        }

        // test with interval that contains the root but initial guess outside
        double root = 0.5;
        try {
            solver.solve(f, -2.0, 2.0, root);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // expected
        }
    }

    /**
     * Test solver with a function that has an infinite number of roots.
     *
     * @throws Exception if an error occurs during the test.
     */
    @org.junit.Test
    public void testBisectionSolverInfiniteRoots() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{"0.0", "1.0", "1.0", "1.0", "1.0", "1.0", "1.0", "1.0"}, 8);
        BisectionSolver solver = new BisectionSolver();

        try {
            solver.solve(f, -1.0, 1.0);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // expected
        }
    }

    /**
     * Helper method for subtracting two doubles with a given precision.
     *
     * @param a the first double
     * @param b the second double
     * @param epsilon the precision
     * @return the result of {@code a - b} with the specified precision
     */
    private double preciseSubtraction(double a, double b, double epsilon) {
        return a - b + Math.signum(a - b) * epsilon;
    }
}