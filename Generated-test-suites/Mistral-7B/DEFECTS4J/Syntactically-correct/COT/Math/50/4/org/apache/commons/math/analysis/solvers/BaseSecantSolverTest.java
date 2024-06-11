package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /** The tested {@link BaseSecantSolver} instance. */
    private BaseSecantSolver solver;

    /** A test function for the solver. */
    private UnivariateRealFunction testFunction;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        this.solver = new BaseSecantSolver(1.0e-6, Method.PEGASUS);
        this.testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method.
     */
    @Test
    public void testSolve() {
        final double root = Math.sqrt(2.0);
        try {
            final double solution = this.solver.solve(100, this.testFunction, 1.0, 5.0);
            Assert.assertEquals(Precision.round(root, 7), solution, 1e-7);
        } catch (MaxEvalException e) {
            // This exception is expected since the solution was not found within the given max evaluations.
        } catch (NumberIsTooSmallException e) {
            // This exception is not expected since the function value accuracy is set to 1e-6.
            Assert.fail("Unexpected NumberIsTooSmallException: " + e.getMessage());
        } catch (NoBracketingException e) {
            // This exception is not expected since the function is defined for the given interval.
            Assert.fail("Unexpected NoBracketingException: " + e.getMessage());
        } catch (UnsupportedOperationException e) {
            // This exception is not expected since the method is implemented.
            Assert.fail("Unexpected UnsupportedOperationException: " + e.getMessage());
        }
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)} method with no bracketing.
     */
    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() {
        this.solver = new BaseSecantSolver(1.0e-6, Method.PEGASUS);
        this.solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return PrecisionUtils.epsilon();
            }

            @Override
            public double derivative(double x) {
                return 0;
            }
        }, 1.0, 1.0);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with a relative accuracy that is too small.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testRelativeAccuracyTooSmall() {
        this.solver = new BaseSecantSolver(1.0e-10, Method.PEGASUS);
        this.solver.solve(100, this.testFunction, 1.0, 5.0);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} method.
     */
    @Test
    public void testSolveWithAllowedSolution() {
        final double root = Math.sqrt(2.0);
        try {
            final double solution = this.solver.solve(100, this.testFunction, 1.0, 5.0, 3.0, AllowedSolution.ABOVE_SIDE);
            Assert.assertEquals(Precision.round(root, 7), solution, 1e-7);
        } catch (MaxEvalException e) {
            // This exception is expected since the solution was not found within the given max evaluations.
        } catch (NumberIsTooSmallException e) {
            // This exception is not expected since the function value accuracy is set to 1e-6.
            Assert.fail("Unexpected NumberIsTooSmallException: " + e.getMessage());
        } catch (NoBracketingException e) {
            // This exception is not expected since the function is defined for the given interval.
            Assert.fail("Unexpected NoBracketingException: " + e.getMessage());
        } catch (UnsupportedOperationException e) {
            // This exception is not expected since the method is implemented.
            Assert.fail("Unexpected UnsupportedOperationException: " + e.getMessage());
        }
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} method with a relative accuracy that is too small and a specific solution allowed.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testRelativeAccuracyTooSmallWithAllowedSolution() {
        this.solver = new BaseSecantSolver(1.0e-10, Method.PEGASUS);
        this.solver.solve(100, this.testFunction, 1.0, 5.0, 3.0, AllowedSolution.ABOVE_SIDE);
    }
}