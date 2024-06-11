package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

/**
 * Test class for BrentOptimizer
 */
public class BrentOptimizerTest {

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer()}.
     */
    @Test
    public void testBrentOptimizerConstructor() {
        BrentOptimizer opt = new BrentOptimizer();
        assertNotNull(opt);
        assertEquals(Integer.MAX_VALUE, opt.getMaxEvaluations());
        assertEquals(100, opt.getMaximalIterationCount());
        assertEquals(1E-10, opt.getAbsoluteAccuracy(), 1E-15);
        assertEquals(1.0e-14, opt.getRelativeAccuracy(), 1E-15);
    }

    /**
     * Test method for {@link BrentOptimizer#doOptimize()}.
     * This method is not meant to be called directly, an UnsupportedOperationException
     * should be thrown instead.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testDoOptimize() {
        BrentOptimizer opt = new BrentOptimizer();
        opt.doOptimize();
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     */
    @Test
    public void testOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer opt = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };
        double min = 0.0;
        double max = 3.14159265359;
        double startValue = 1.57079632679;
        double expectedResult = 0.0;
        double result = opt.optimize(f, GoalType.MINIMIZE, min, max, startValue);
        assertEquals(expectedResult, result, 1E-9);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test
    public void testOptimizeWithStartingPointInInterval() throws MaxIterationsExceededException, FunctionEvaluationException, NumberIsTooSmallException {
        BrentOptimizer opt = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };
        double min = 0.0;
        double max = 3.14159265359;
        double startValue = 1.0;
        double expectedResult = 0.0;
        double result = opt.optimize(f, GoalType.MINIMIZE, min, max, startValue);
        assertEquals(expectedResult, result, 1E-9);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Test case with the bounds in wrong order.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithBoundsInWrongOrder() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer opt = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };
        double min = 3.14159265359;
        double max = 0.0;
        double startValue = 1.57079632679;
        opt.optimize(f, GoalType.MINIMIZE, min, max, startValue);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Test case with the function throwing an exception on evaluation.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeWithFunctionThrowingException() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer opt = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException("Function evaluation error");
            }
        };
        double min = 0.0;
        double max = 3.14159265359;
        double startValue = 1.57079632679;
        opt.optimize(f, GoalType.MINIMIZE, min, max, startValue);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Test case with the function not having a minimum in the interval.
     */
    @Test
    public void testOptimizeWithFunctionNotHavingMinimum() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer opt = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;
        double result = opt.optimize(f, GoalType.MINIMIZE, min, max, startValue);
        assertTrue(Double.isNaN(result));
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Test case with the function not having a minimum in the interval and the function evaluations exceeding the maximal iteration count.
     */
    @Test
    public void testOptimizeWithFunctionNotHavingMinimumAndMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer opt = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;
        opt.setMaximalIterationCount(0);
        try {
            opt.optimize(f, GoalType.MINIMIZE, min, max, startValue);
            fail("MaxIterationsExceededException expected");
        } catch (MaxIterationsExceededException e) {
            // expected
        }
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Test case with the function not having a minimum in the interval and the tolerance being too small.
     */
    @Test
    public void testOptimizeWithFunctionNotHavingMinimumAndToleranceTooSmall() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer opt = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;
        opt.setAbsoluteAccuracy(0.0);
        try {
            opt.optimize(f, GoalType.MINIMIZE, min, max, startValue);
            fail("FunctionEvaluationException expected");
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double)}.
     */
    @Test
    public void testOptimizeWithBoundsAndStartingPoint() throws MaxIterationsExceededException, FunctionEvaluationException, NumberIsTooSmallException {
        BrentOptimizer opt = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };
        double min = 0.0;
        double max = 3.14159265359;
        double startValue = 1.57079632679;
        double expectedResult = 0.0;
        double result = opt.optimize(f, GoalType.MINIMIZE, min, startValue);
        assertEquals(expectedResult, result, 1E-9);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double)}.
     * Test case with the function throwing an exception on evaluation.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeWithFunctionThrowingExceptionWithOneBound() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer opt = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException("Function evaluation error");
            }
        };
        double min = 0.0;
        double startValue = 1.57079632679;
        opt.optimize(f, GoalType.MINIMIZE, min, startValue);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double)}.
     * Test case with the function not having a minimum in the interval.
     */
    @Test
    public void testOptimizeWithFunctionNotHavingMinimumAndOneBound() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer opt = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        double min = 0.0;
        double startValue = 0.5;
        double result = opt.optimize(f, GoalType.MINIMIZE, min, startValue);
        assertTrue(Double.isNaN(result));
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double)}.
     * Test case with the function not having a minimum in the interval and the tolerance being too small.
     */
    @Test
    public void testOptimizeWithFunctionNotHavingMinimumAndToleranceTooSmallWithOneBound() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer opt = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {

            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        double min = 0.0;
        double startValue = 0.5;
        opt.setAbsoluteAccuracy(0.0);
        try {
            opt.optimize(f, GoalType.MINIMIZE, min, startValue);
            fail("FunctionEvaluationException expected");
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }
}