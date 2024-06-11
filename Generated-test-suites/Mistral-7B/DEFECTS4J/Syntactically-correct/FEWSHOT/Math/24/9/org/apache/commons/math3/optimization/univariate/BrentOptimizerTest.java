package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.guess.LinearInterpolationGuess;
import org.apache.commons.math3.optim.nonlinear.simple.RiddersMethod;
import org.apache.commons.math3.optim.nonlinear.simple.SecantMethod;
import org.junit.Test;

import org.apache.commons.commons.math3.optimization.univariate.BrentOptimizer;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructor() {
        // Check if constructor with all parameters is working
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-4, new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return current.getValue() <= 1e-8;
            }
        });
        assertEquals(1e-6, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(1e-4, optimizer.getAbsoluteThreshold(), 0.0);
        assertNull(optimizer.getConvergenceChecker());

        // Check if constructor with only rel and abs is working
        optimizer = new BrentOptimizer(1e-6, 1e-4);
        assertEquals(1e-6, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(1e-4, optimizer.getAbsoluteThreshold(), 0.0);
        assertNull(optimizer.getConvergenceChecker());

        // Check if constructor with only rel is working
        optimizer = new BrentOptimizer(1e-6);
        assertEquals(1e-6, optimizer.getRelativeThreshold(), 0.0);
        assertNull(optimizer.getAbsoluteThreshold());
        assertNull(optimizer.getConvergenceChecker());

        // Check if constructor with only abs is working
        optimizer = new BrentOptimizer(1e-4);
        assertEquals(2 * Double.ULP, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(1e-4, optimizer.getAbsoluteThreshold(), 0.0);
        assertNull(optimizer.getConvergenceChecker());

        // Check if constructor with negative abs or rel is throwing exception
        try {
            optimizer = new BrentOptimizer(-1e-4, 1e-6);
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
        try {
            optimizer = new BrentOptimizer(1e-6, -1e-4);
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
        try {
            optimizer = new BrentOptimizer(-1e-6);
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
        try {
            optimizer = new BrentOptimizer(1e-6, -1e-4, null);
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
        try {
            optimizer = new BrentOptimizer(1e-6, -1e-4, new ConvergenceChecker<PointValuePair>() {
                @Override
                public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                    return false;
                }
            });
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizerWithFunctionAndInterval() {
        // Define the function
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2) - 1;
            }

            @Override
            public double derivative(double x) {
                return 2 * Math.sin(x) * Math.cos(x) - 2 * Math.cos(x);
            }
        };

        // Define the interval
        double min = -3.0;
        double max = 3.0;

        // Define the initial guess
        double initialGuess = 0.0;

        // Define the maximum number of evaluations
        int maxEvaluations = 100;

        // Define a convergence checker
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return Math.abs(current.getValue()) < 1e-8;
            }
        };

        // Initialize optimizer
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-4, convergenceChecker);

        // Perform optimization
        PointValuePair result = optimizer.optimize(function, new Bracket(min, max, 1e-8), new InitialGuess(initialGuess), new MaxEval(maxEvaluations));

        // Check if optimization was successful
        assertEquals(-1.1752011936438015, result.getValue(), 1e-8);
        assertEquals(-1.1752011936438015, result.getPoint(), 1e-8);

        // Check if convergence was detected
        assertTrue(convergenceChecker.wasConverged());

        // Check if maximum number of evaluations was reached
        assertTrue(optimizer.getNumberOfFunctionEvaluations() >= maxEvaluations);
    }

    @Test
    public void testBrentOptimizerWithFunctionAndInitialGuess() {
        // Define the function
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2) - 1;
            }

            @Override
            public double derivative(double x) {
                return 2 * Math.sin(x) * Math.cos(x) - 2 * Math.cos(x);
            }
        };

        // Define the initial guess
        double initialGuess = 0.0;

        // Define the maximum number of evaluations
        int maxEvaluations = 100;

        // Define a convergence checker
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return Math.abs(current.getValue()) < 1e-8;
            }
        };

        // Initialize optimizer
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-4, convergenceChecker);

        // Perform optimization
        PointValuePair result = optimizer.optimize(function, new InitialGuess(initialGuess), new MaxEval(maxEvaluations));

        // Check if optimization was successful
        assertEquals(-1.1752011936438015, result.getValue(), 1e-8);
        assertEquals(-1.1752011936438015, result.getPoint(), 1e-8);

        // Check if convergence was detected
        assertTrue(convergenceChecker.wasConverged());

        // Check if maximum number of evaluations was reached
        assertTrue(optimizer.getNumberOfFunctionEvaluations() >= maxEvaluations);
    }

    @Test
    public void testBrentOptimizerWithLinearInterpolationGuess() {
        // Define the function
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * x + 1;
            }

            @Override
            public double derivative(double x) {
                return 3 * Math.pow(x, 2) - 2;
            }
        };

        // Define the interval
        double min = -2.0;
        double max = 2.0;

        // Define the initial guess
        double initialGuess = 0.0;

        // Define the maximum number of evaluations
        int maxEvaluations = 100;

        // Define a convergence checker
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return Math.abs(current.getValue()) < 1e-8;
            }
        };

        // Initialize optimizer
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-4, convergenceChecker);

        // Perform optimization with LinearInterpolationGuess
        PointValuePair result = optimizer.optimize(function, new Bracket(min, max, 1e-8), new LinearInterpolationGuess(initialGuess), new MaxEval(maxEvaluations));

        // Check if optimization was successful
        assertEquals(0.5, result.getValue(), 1e-8);
        assertEquals(0.5, result.getPoint(), 1e-8);

        // Check if convergence was detected
        assertTrue(convergenceChecker.wasConverged());

        // Check if maximum number of evaluations was reached
        assertTrue(optimizer.getNumberOfFunctionEvaluations() >= maxEvaluations);
    }

    @Test
    public void testBrentOptimizerWithSecantMethod() {
        // Define the function
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * x + 1;
            }

            @Override
            public double derivative(double x) {
                return 3 * Math.pow(x, 2) - 2;
            }
        };

        // Define the interval
        double min = -2.0;
        double max = 2.0;

        // Define the initial guess
        double initialGuess1 = -1.0;
        double initialGuess2 = 1.0;

        // Define the maximum number of evaluations
        int maxEvaluations = 100;

        // Define a convergence checker
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return Math.abs(current.getValue()) < 1e-8;
            }
        };

        // Initialize optimizer
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-4, convergenceChecker);

        // Perform optimization with SecantMethod
        PointValuePair result = optimizer.optimize(function, new SecantMethod(initialGuess1, initialGuess2), new MaxEval(maxEvaluations));

        // Check if optimization was successful
        assertEquals(0.5, result.getValue(), 1e-8);
        assertEquals(0.5, result.getPoint(), 1e-8);

        // Check if convergence was detected
        assertTrue(convergenceChecker.wasConverged());

        // Check if maximum number of evaluations was reached
        assertTrue(optimizer.getNumberOfFunctionEvaluations() >= maxEvaluations);
    }

    @Test
    public void testBrentOptimizerWithRiddersMethod() {
        // Define the function
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * x + 1;
            }

            @Override
            public double derivative(double x) {
                return 3 * Math.pow(x, 2) - 2;
            }
        };

        // Define the interval
        double min = -2.0;
        double max = 2.0;

        // Define the maximum number of evaluations
        int maxEvaluations = 100;

        // Define a convergence checker
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return Math.abs(current.getValue()) < 1e-8;
            }
        };

        // Initialize optimizer
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-4, convergenceChecker);

        // Perform optimization with RiddersMethod
        PointValuePair result = optimizer.optimize(function, new RiddersMethod(), new MaxEval(maxEvaluations));

        // Check if optimization was successful
        assertEquals(0.5, result.getValue(), 1e-8);
        assertEquals(0.5, result.getPoint(), 1e-8);

        // Check if convergence was detected
        assertTrue(convergenceChecker.wasConverged());

        // Check if maximum number of evaluations was reached
        assertTrue(optimizer.getNumberOfFunctionEvaluations() >= maxEvaluations);
    }
}