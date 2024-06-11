package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedsSystemException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.UnivariateFunctionUtils;
import org.junit.Test;

import java.util.function.Function;

/**
 * Test class for {@link BisectionSolver}
 */
public class BisectionSolverTest {

    /**
     * Test solver with simple function
     */
    @Test
    public void testSimpleFunction() {
        UnivariateFunction f = new PolynomialFunction(3.0, -2.0, 1.0);
        BisectionSolver solver = new BisectionSolver();
        double result;

        result = solver.solve(f, -5.0, 5.0);
        assertEquals(0.0, result, 1e-8);

        try {
            result = solver.solve(f, -10.0, 10.0);
            fail();
        } catch (MaxIterationsExceededException e) {
            // expected
        }
    }

    /**
     * Test solver with function that is always positive
     */
    @Test(expected = UnsupportedsSystemException.class)
    public void testAlwaysPositiveFunction() {
        UnivariateFunction f = new PolynomialFunction(1.0, 1.0, 0.0);
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -1.0, 1.0);
    }

    /**
     * Test solver with function that is always negative
     */
    @Test(expected = UnsupportedsSystemException.class)
    public void testAlwaysNegativeFunction() {
        UnivariateFunction f = new PolynomialFunction(-1.0, 1.0, 0.0);
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -1.0, 1.0);
    }

    /**
     * Test solver with function that has no roots
     */
    @Test
    public void testFunctionWithNoRoots() {
        UnivariateFunction f = new PolynomialFunction(-1.0, 1.0, 2.0);
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(f, -10.0, 10.0);
            fail();
        } catch (MaxIterationsExceededException e) {
            // expected
        }
    }

    /**
     * Test solver with function that has a very small root
     */
    @Test
    public void testSmallRootFunction() {
        UnivariateFunction f = new PolynomialFunction(1.0E-10, 0.0, 1.0);
        BisectionSolver solver = new BisectionSolver();
        try {
            double result = solver.solve(f, -1.0, 1.0);
            fail();
        } catch (MaxIterationsExceededException e) {
            try {
                double result = solver.solve(f, -1.0e-10, 1.0e-10);
                assertEquals(0.0, result, 1e-15);
            } catch (NumberIsTooSmallException ex) {
                // expected
            }
        }
    }

    /**
     * Test solver with function that has a root close to maximum iterations
     */
    @Test
    public void testRootCloseToMaxIterations() {
        UnivariateFunction f = new PolynomialFunction(1.0, 1.0, 0.0);
        BisectionSolver solver = new BisectionSolver(300);
        double result = solver.solve(f, -1.0, 1.0);
        assertEquals(0.0, result, 1e-8);
    }

    /**
     * Test solver with function that has a root close to absolute accuracy
     */
    @Test
    public void testRootCloseToAbsAccuracy() {
        UnivariateFunction f = new PolynomialFunction(1.0E-6, 1.0, 0.0);
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, -1.0, 1.0);
        assertEquals(0.0, result, 1e-12);
    }

    /**
     * Test solver with function that has a root close to initial guess
     */
    @Test
    public void testRootCloseToInitialGuess() {
        UnivariateFunction f = new PolynomialFunction(1.0, 0.5, 0.0);
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, -1.0, 1.0);
        assertEquals(0.5, result, 1e-6);
    }

    /**
     * Test solver with function that is discontinuous at the root
     */
    @Test
    public void testDiscontinuousFunction() {
        UnivariateFunction f = new FunctionUtils.Pow(x -> Math.pow(Math.abs(x), 0.5), 2);
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, -1.0, 1.0);
        assertEquals(0.0, result, 1e-6);
    }

    /**
     * Test solver with function that is discontinuous at the interval boundaries
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testDiscontinuousFunctionAtBoundaries() {
        UnivariateFunction f = new FunctionUtils.Pow(x -> Math.pow(Math.abs(x), 0.5), 2);
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -1.0, 0.0);
    }

    /**
     * Test solver with very large interval
     */
    @Test
    public void testLargeInterval() {
        UnivariateFunction f = new PolynomialFunction(1.0, 1.0, 0.0);
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, -1e10, 1e10);
        assertEquals(0.0, result, 1e-8);
    }

    /**
     * Test solver with very small interval
     */
    @Test(expected = UnsupportedsSystemException.class)
    public void testSmallInterval() {
        UnivariateFunction f = new PolynomialFunction(1.0, 1.0, 0.0);
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, 0.0, 1e-8);
    }
}