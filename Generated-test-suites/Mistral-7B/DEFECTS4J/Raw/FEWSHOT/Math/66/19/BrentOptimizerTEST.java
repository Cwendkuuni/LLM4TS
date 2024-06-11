package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MathOptimizer;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstruction() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        assertEquals(100, optimizer.getMaximalIterationCount());
        assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0);
        assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0);
    }

    @Test
    public void testBrentOptimizerNullFunction() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(null, GoalType.MINIMIZE, 0.0, 1.0, 0.5));
    }

    @Test
    public void testBrentOptimizerInvalidGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(new PolynomialFunction(1, 1), GoalType.MAXIMIZE, 0.0, 1.0, 0.5));
    }

    @Test
    public void testBrentOptimizerNegativeInterval() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(new PolynomialFunction(1, 1), GoalType.MINIMIZE, -1.0, -0.5, 0.5));
    }

    @Test
    public void testBrentOptimizerNegativeStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(new PolynomialFunction(1, 1), GoalType.MINIMIZE, 0.5, -1.0, 0.5));
    }

    @Test
    public void testBrentOptimizerZeroInterval() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(new PolynomialFunction(1, 1), GoalType.MINIMIZE, 0.0, 0.0, 0.5));
    }

    @Test
    public void testBrentOptimizerMinimizeSimple() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new PolynomialFunction(1, -1);
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0, 0.5);
        assertEquals(0.5, result.getPoint(), 1e-12);
        assertEquals(-1.0 / 4, result.getValue(), 1e-12);
    }

    @Test
    public void testBrentOptimizerMinimizeWithInitialGuess() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new PolynomialFunction(1, -1);
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0, new SimpleBounds(0.6, 0.8));
        assertEquals(0.712738383191318, result.getPoint(), 1e-6);
        assertEquals(-1.0 / 4, result.getValue(), 1e-12);
    }

    @Test
    public void testBrentOptimizerMinimizeWithInitialGuessOutsideInterval() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new PolynomialFunction(1, -1);
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0, new SimpleBounds(0.1, 0.3)));
    }

    @Test
    public void testBrentOptimizerMinimizeWithInitialGuessInsideInterval() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new PolynomialFunction(1, -1);
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0, new SimpleBounds(0.4, 0.6));
        assertEquals(0.48686954528911, result.getPoint(), 1e-6);
        assertEquals(-1.0 / 4, result.getValue(), 1e-12);
    }

    @Test
    public void testBrentOptimizerMaximizeSimple() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new PolynomialFunction(1, 1);
        PointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, 0.0, 1.0, 0.5);
        assertEquals(1.0, result.getPoint(), 1e-12);
        assertEquals(1.0, result.getValue(), 1e-12);
    }

    @Test
    public void testBrentOptimizerMaximizeWithInitialGuess() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new PolynomialFunction(1, 1);
        PointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, 0.0, 1.0, new SimpleBounds(0.6, 0.8));
        assertEquals(0.84147098484598, result.getPoint(), 1e-6);
        assertEquals(1.0, result.getValue(), 1e-12);
    }

    @Test
    public void testBrentOptimizerMaximizeWithInitialGuessOutsideInterval() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new PolynomialFunction(1, 1);
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(f, GoalType.MAXIMIZE, 0.0, 1.0, new SimpleBounds(0.1, 0.3)));
    }

    @Test
    public void testBrentOptimizerMaximizeWithInitialGuessInsideInterval() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new PolynomialFunction(1, 1);
        PointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, 0.0, 1.0, new SimpleBounds(0.4, 0.6));
        assertEquals(0.5603813310866, result.getPoint(), 1e-6);
        assertEquals(1.0, result.getValue(), 1e-12);
    }

    @Test
    public void testBrentOptimizerFunctionEvaluationException() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = (x) -> { throw new FunctionEvaluationException("Test message"); };
        assertThrows(FunctionEvaluationException.class, () -> optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0, 0.5));
    }

    @Test
    public void testBrentOptimizerMaxIterationsExceeded() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(1);
        UnivariateRealFunction f = new PolynomialFunction(1, -1);
        try {
            optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0, 0.5);
            fail("Exception not thrown");
        } catch (MaxIterationsExceededException e) {
            // expected
        }
    }

    @Test
    public void testBrentOptimizerNotStrictlyPositiveAccuracy() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(-1.0);
        UnivariateRealFunction f = new PolynomialFunction(1, -1);
        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0, 0.5));
    }

    @Test
    public void testBrentOptimizerNotStrictlyPositiveTolerance() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(1.0);
        optimizer.setMaximalIterationCount(1);
        UnivariateRealFunction f = new PolynomialFunction(1, -1);
        try {
            optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0, 0.5);
            fail("Exception not thrown");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }
}