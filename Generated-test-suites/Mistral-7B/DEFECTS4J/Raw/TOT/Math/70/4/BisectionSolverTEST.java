package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testSolveSimpleFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            double result = solver.solve(f, -2.0, 2.0);
            Assert.assertEquals(0.0, Precision.round(result, 5));
        } catch (MaxIterationsExceededException e) {
            // Should not happen
            Assert.fail("Max iterations should not be exceeded for simple function");
        }
    }

    @Test
    public void testSolveFunctionWithNoRoot() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(f, -1.0, 1.0);
            Assert.fail("Bisection algorithm should not find root for a function without root");
        } catch (NoRootsException e) {
            // Expected
        }
    }

    @Test
    public void testSolveFunctionWithNegativeInterval() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(f, -1.0, 0.0);
            Assert.fail("Bisection algorithm should not accept interval with negative length");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testSolveFunctionWithTooSmallInterval() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0) - 0.01;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(f, 0.0, 1.0e-6);
            Assert.fail("Bisection algorithm should not accept interval with too small length");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }
}