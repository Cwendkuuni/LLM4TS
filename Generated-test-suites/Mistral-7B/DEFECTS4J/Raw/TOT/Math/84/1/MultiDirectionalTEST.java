package org.apache.commons.math.optimization.direct;

import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.SimpleOptimizationProblem;
import org.apache.commons.math3.optim.nonlinear.DiffusivityFunction;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalOptimizer() {
        // Test with default coefficients
        MultiDirectional optimizer = new MultiDirectional();

        // Test with simple function
        SimpleOptimizationProblem problem = new SimpleOptimizationProblem(new SimpleFunction(), new SimpleBounds(0.0, 10.0));
        optimizer.optimize(problem);
        PointValuePair solution = problem.getOptimum();
        assertEquals(3.0, solution.getPoint()[0], 0.001);
        assertEquals(2.0, solution.getValue(), 0.001);

        // Test with complex function
        problem = new SimpleOptimizationProblem(new ComplexFunction(), new SimpleBounds(-5.0, 5.0), new DiffusivityFunction() {
            @Override
            public double value(PointValuePair point) {
                return 0.1;
            }
        });
        optimizer.optimize(problem);
        solution = problem.getOptimum();
        assertEquals(-1.5, solution.getPoint()[0], 0.001);
        assertEquals(3.0, solution.getValue(), 0.001);

        // Test with initial guess outside bounds
        problem = new SimpleOptimizationProblem(new SimpleFunction(), new SimpleBounds(0.0, 1.0), new InitialGuess() {
            @Override
            public PointValuePair getPointValuePair() {
                return new PointValuePair(new double[]{2.0}, Double.NaN);
            }
        });
        try {
            optimizer.optimize(problem);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with function that doesn't have a minimum
        problem = new SimpleOptimizationProblem(new NoMinFunction(), new SimpleBounds(0.0, 10.0));
        try {
            optimizer.optimize(problem);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }

        // Test with function that doesn't allow more evaluations
        problem = new SimpleOptimizationProblem(new SimpleFunction(), new SimpleBounds(0.0, 1.0), new MaxEval(1));
        try {
            optimizer.optimize(problem);
            fail("Expected MaxEvalExceededException");
        } catch (MaxEvalExceededException e) {
            // Expected
        }

        // Test with unsupported operation
        problem = new SimpleOptimizationProblem(new UnsupportedFunction(), new SimpleBounds(0.0, 1.0));
        try {
            optimizer.optimize(problem);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testMultiDirectionalOptimizerWithCustomCoefficients() {
        // Test with custom coefficients
        MultiDirectional optimizer = new MultiDirectional(1.5, 0.7);

        // Test with simple function
        SimpleOptimizationProblem problem = new SimpleOptimizationProblem(new SimpleFunction(), new SimpleBounds(0.0, 10.0));
        optimizer.optimize(problem);
        PointValuePair solution = problem.getOptimum();
        assertEquals(3.0, solution.getPoint()[0], 0.001);
        assertEquals(1.8, solution.getValue(), 0.001);
    }

    // Custom functions used for testing
    private static class SimpleFunction implements org.apache.commons.math3.optim.NonlinearFunction {
        @Override
        public double value(double x) {
            return x * x - 4.0 * x + 3.0;
        }

        @Override
        public double value(double[] x) {
            return value(x[0]);
        }

        @Override
        public double[] gradient(double[] x) {
            return new double[]{2.0 * x - 4.0};
        }

        @Override
        public double[] gradient(double x) {
            return new double[]{2.0 * x - 4.0};
        }
    }

    private static class ComplexFunction implements org.apache.commons.math3.optim.NonlinearFunction {
        @Override
        public double value(double[] x) {
            return Math.sin(Math.PI * x[0]) * Math.exp(-Math.pow(x[0], 2.0)) + Math.pow(x[0], 2.0);
        }

        @Override
        public double[] gradient(double[] x) {
            double[] result = new double[x.length];
            result[0] = Math.cos(Math.PI * x[0]) * Math.exp(-Math.pow(x[0], 2.0)) - 2.0 * x[0] * Math.exp(-Math.pow(x[0], 2.0));
            return result;
        }

        @Override
        public double[] gradient(double x) {
            throw new UnsupportedOperationException();
        }
    }

    private static class NoMinFunction implements org.apache.commons.math3.optim.NonlinearFunction {
        @Override
        public double value(double[] x) {
            return Math.sin(Math.PI * x[0]);
        }

        @Override
        public double[] gradient(double[] x) {
            return new double[]{Math.cos(Math.PI * x[0])};
        }

        @Override
        public double[] gradient(double x) {
            throw new UnsupportedOperationException();
        }
    }

    private static class UnsupportedFunction implements org.apache.commons.math3.optim.NonlinearFunction {
        @Override
        public double value(double[] x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double value(double x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double[] gradient(double[] x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double[] gradient(double x) {
            throw new UnsupportedOperationException();
        }
    }
}