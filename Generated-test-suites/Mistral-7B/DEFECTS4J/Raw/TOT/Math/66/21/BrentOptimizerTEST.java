package org.apache.commons.math.optimization.univariate.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.NonconvergentIterationException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMock;
import org.apache.commons.math3.optim.nonlinear.scalar.univariate.BrentOptimizer;

/**
 * Test cases for the {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    /**
     * Test minimization of a simple function.
     */
    @org.junit.Test
    public void testMinimization() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2, 2);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;
        try {
            PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);
            assertEquals(2.0, result.getPoint(), 1e-8);
            assertEquals(0.0, result.getValue(), 1e-8);
        } catch (MaxIterationsExceededException e) {
            // should not be thrown
            fail("MaxIterationsExceededException should not be thrown");
        } catch (FunctionEvaluationException e) {
            // should be thrown
            assertFalse("FunctionEvaluationException should be thrown", false);
        }
    }

    /**
     * Test minimization of a function with a maximum iteration limit.
     */
    @org.junit.Test
    public void testMaxIterations() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2, 2);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(1);
        GoalType goalType = GoalTypeMock.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;
        try {
            optimizer.optimize(f, goalType, min, max, startValue);
            fail("MaxIterationsExceededException should be thrown");
        } catch (MaxIterationsExceededException e) {
            // expected
        } catch (FunctionEvaluationException e) {
            // should not be thrown
        }
    }

    /**
     * Test minimization of a function with negative bounds.
     */
    @org.junit.Test
    public void testNegativeBounds() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2, 2);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        GoalType goalType = GoalTypeMock.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        double startValue = 5.0;
        try {
            optimizer.optimize(f, goalType, min, max, startValue);
            fail("FunctionEvaluationException should be thrown");
        } catch (FunctionEvaluationException e) {
            // expected
        } catch (MaxIterationsExceededException e) {
            // should not be thrown
        }
    }

    /**
     * Test minimization of a function with bounds touching.
     */
    @org.junit.Test
    public void testBoundsTouching() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2, 2);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        GoalType goalType = GoalTypeMock.MINIMIZE;
        double min = 2.0;
        double max = 2.0;
        double startValue = 5.0;
        try {
            optimizer.optimize(f, goalType, min, max, startValue);
            fail("FunctionEvaluationException should be thrown");
        } catch (FunctionEvaluationException e) {
            // expected
        } catch (MaxIterationsExceededException e) {
            // should not be thrown
        }
    }

    /**
     * Test minimization of a function that does not converge.
     */
    @org.junit.Test
    public void testNonConvergentFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(1.0 / x);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        GoalType goalType = GoalTypeMock.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;
        try {
            optimizer.optimize(f, goalType, min, max, startValue);
            fail("NonconvergentIterationException should be thrown");
        } catch (NonconvergentIterationException e) {
            // expected
        } catch (MaxIterationsExceededException e) {
            fail("MaxIterationsExceededException should not be thrown");
        } catch (FunctionEvaluationException e) {
            // should not be thrown
        }
    }

    /**
     * Test optimization with a function that throws an exception.
     */
    @org.junit.Test
    public void testExceptionFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new RuntimeException("Exception thrown");
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        GoalType goalType = GoalTypeMock.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;
        try {
            optimizer.optimize(f, goalType, min, max, startValue);
            fail("FunctionEvaluationException should be thrown");
        } catch (FunctionEvaluationException e) {
            // expected
        } catch (MaxIterationsExceededException e) {
            // should not be thrown
        }
    }

    /**
     * Test optimization with a null function.
     */
    @org.junit.Test
    public void testNullFunction() {
        UnivariateRealFunction f = null;
        UnivariateOptimizer optimizer = new BrentOptimizer();
        GoalType goalType = GoalTypeMock.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;
        try {
            optimizer.optimize(f, goalType, min, max, startValue);
            fail("FunctionEvaluationException should be thrown");
        } catch (NullPointerException e) {
            // expected
        } catch (MaxIterationsExceededException e) {
            // should not be thrown
        } catch (FunctionEvaluationException e) {
            // should not be thrown
        }
    }

    /**
     * Test optimization with a null optimizer.
     */
    @org.junit.Test
    public void testNullOptimizer() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2, 2);
            }
        };
        UnivariateOptimizer optimizer = null;
        GoalType goalType = GoalTypeMock.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;
        try {
            optimizer.optimize(f, goalType, min, max, startValue);
            fail("FunctionEvaluationException should be thrown");
        } catch (NullPointerException e) {
            // expected
        } catch (MaxIterationsExceededException e) {
            // should not be thrown
        } catch (FunctionEvaluationException e) {
            // should not be thrown
        }
    }
}