package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

/**
 * Test case for {@link BracketingNthOrderBrentSolver}.
 */
public class BracketingNthOrderBrentSolverTest {

    /**
     * Tests that the solver returns the expected value for a simple polynomial.
     * @throws UnsupportedOperationException if the solver does not support the provided function.
     * @throws NoBracketingException if the solver fails to bracket the root.
     * @throws MaxCountExceededException if the solver fails to converge within the maximum number of evaluations.
     */
    @Test
    public void testSimplePolynomial() throws UnsupportedOperationException, NoBracketingException, MaxCountExceededException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0}, 3);
        double root = solver.solve(100, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(1.0, Precision.round(root, 5), 1e-9);
    }

    /**
     * Tests that the solver returns the expected value for a function with multiple roots.
     * @throws UnsupportedOperationException if the solver does not support the provided function.
     * @throws NoBracketingException if the solver fails to bracket the root.
     * @throws MaxCountExceededException if the solver fails to converge within the maximum number of evaluations.
     */
    @Test
    public void testFunctionWithMultipleRoots() throws UnsupportedOperationException, NoBracketingException, MaxCountExceededException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 2.0, 1.0, 2.0, -1.0}, 5);
        double root1 = solver.solve(100, f, -3.0, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, Precision.round(root1, 5), 1e-9); // Root at x = 0
        double root2 = solver.solve(100, f, 1.0, 3.0, AllowedSolution.ANY_SIDE);
        assertEquals(2.0, Precision.round(root2, 5), 1e-9); // Root at x = 2
    }

    /**
     * Tests that the solver returns the expected value for a function with no bracketing interval.
     * @throws UnsupportedOperationException if the solver does not support the provided function.
     * @throws NoBracketingException if the solver fails to bracket the root.
     * @throws MaxCountExceededException if the solver fails to converge within the maximum number of evaluations.
     */
    @Test(expected = NoBracketingException.class)
    public void testFunctionWithNoBracketingInterval() throws UnsupportedOperationException, NoBracketingException, MaxCountExceededException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 2.0, 1.0}, 2);
        solver.solve(100, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
    }

    /**
     * Tests that the solver returns the expected value for a function with a discontinuity.
     * @throws UnsupportedOperationException if the solver does not support the provided function.
     * @throws NoBracketingException if the solver fails to bracket the root.
     * @throws MaxCountExceededException if the solver fails to converge within the maximum number of evaluations.
     */
    @Test
    public void testFunctionWithDiscontinuity() throws UnsupportedOperationException, NoBracketingException, MaxCountExceededException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new FunctionWithDiscontinuity();
        double root = solver.solve(100, f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.25, Precision.round(root, 5), 1e-9);
    }

    /**
     * Test helper for a function with a discontinuity.
     */
    private static class FunctionWithDiscontinuity implements UnivariateFunction {

        /**
         * {@inheritDoc}
         */
        @Override
        public double value(double x) {
            if (x > 0.25) {
                return x * x;
            } else {
                throw new UnsupportedOperationException();
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double derivative(double x) {
            if (x > 0.25) {
                return 2 * x;
            } else {
                throw new UnsupportedOperationException();
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double derivative(int n, double x) {
            if (n > 1) {
                throw new UnsupportedOperationException();
            }
            if (x > 0.25) {
                return 2;
            } else {
                throw new UnsupportedOperationException();
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double[] derivatives(double x, int n) {
            if (n > 1) {
                throw new UnsupportedOperationException();
            }
            if (x > 0.25) {
                return new double[]{2};
            } else {
                throw new UnsupportedOperationException();
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public RealMatrix jacobian(double x) {
            if (x > 0.25) {
                return new Array2DRowRealMatrix(new double[][]{new double[]{2}});
            } else {
                throw new UnsupportedOperationException();
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public RealMatrix valueMatrix(double x) {
            if (x > 0.25) {
                return new Array2DRowRealMatrix(new double[][]{new double[]{x*x}});
            } else {
                throw new UnsupportedOperationException();
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double root(int multiplicity, double... values) {
            if (multiplicity != 1) {
                throw new UnsupportedOperationException();
            }
            return FunctionUtils.root(this, values[0]);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double root(int multiplicity, double x, double direction) {
            if (multiplicity != 1) {
                throw new UnsupportedOperationException();
            }
            if (x > 0.25) {
                return x;
            } else {
                throw new UnsupportedOperationException();
            }
        }
    }

}