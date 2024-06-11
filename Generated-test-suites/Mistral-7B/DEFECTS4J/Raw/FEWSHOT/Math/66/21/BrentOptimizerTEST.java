package org.apache.commons.cli.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstruction() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertNotNull(optimizer);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeParameters() {
        new BrentOptimizer()
                .setMaxEvaluations(-1)
                .setMaximalIterationCount(-1)
                .setAbsoluteAccuracy(-1.0)
                .setRelativeAccuracy(-1.0e-14);
    }

    @Test
    public void testBrentOptimizerGettersAndSetters() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(5);
        optimizer.setMaximalIterationCount(20);
        optimizer.setAbsoluteAccuracy(1.0e-12);
        optimizer.setRelativeAccuracy(1.0e-15);
        assertEquals(5, optimizer.getMaxEvaluations());
        assertEquals(20, optimizer.getMaximalIterationCount());
        assertEquals(1.0e-12, optimizer.getAbsoluteAccuracy(), 0);
        assertEquals(1.0e-15, optimizer.getRelativeAccuracy(), 0);
    }

    @Test
    public void testBrentOptimizerOptimizeMin() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
        assertEquals(-1.2732364850688336, result, 0.001);
    }

    @Test
    public void testBrentOptimizerOptimizeMax() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(function, GoalType.MAXIMIZE, -5.0, 5.0, 0.0);
        assertEquals(1.2732364850688336, result, 0.001);
    }

    @Test
    public void testBrentOptimizerOptimizeBounded() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 2.0, 1.0);
        assertEquals(1.2732364850688336, result, 0.001);
    }

    @Test
    public void testBrentOptimizerMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(0);
        try {
            optimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
            fail();
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizerFunctionEvaluationException() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                if (x < 0) {
                    throw new FunctionEvaluationException("Function evaluation failed");
                }
                return Math.pow(Math.sin(x), 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
            fail();
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizerNotStrictlyPositiveException() {
        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.setMaximalIterationCount(-5);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            optimizer.setMaxEvaluations(-5);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            optimizer.setAbsoluteAccuracy(-0.1);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            optimizer.setRelativeAccuracy(-0.00000001);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }
}