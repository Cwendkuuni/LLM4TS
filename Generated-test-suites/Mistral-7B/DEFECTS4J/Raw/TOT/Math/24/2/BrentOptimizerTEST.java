package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.BrentOptimizer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    private UnivariateFunction testFunction;
    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        testFunction = new TestFunction();
        optimizer = new BrentOptimizer(1e-6, 1e-6);
    }

    @Test
    public void testBasic() {
        PointValuePair result = optimizer.optimize(testFunction, new SimpleBounds(0, 10));
        assertEquals(3.0, result.getPoint(), 1e-6);
        assertEquals(-4.54262359e-14, result.getValue(), 1e-15);
    }

    @Test(expected = MaxEvalException.class)
    public void testMaxEval() {
        SimpleBounds bounds = new SimpleBounds(0, 1);
        optimizer.optimize(new TestFunctionWithMaxEval(50), bounds);
    }

    @Test
    public void testOutOfBounds() {
        SimpleBounds bounds = new SimpleBounds(10, 0);
        PointValuePair result = optimizer.optimize(testFunction, bounds);
        assertEquals(9.0, result.getPoint(), 1e-6);
        assertEquals(9.984373229504942, result.getValue(), 1e-6);
    }

    @Test
    public void testNotStrictlyPositiveAbs() {
        try {
            new BrentOptimizer(-1e-6, 1e-6);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testNotStrictlyPositiveRel() {
        try {
            new BrentOptimizer(0.5, 1e-6);
            fail();
        } catch (NumberIsTooSmallException e) {
            // expected
        }
    }

    @Test
    public void testConvergenceCheck() {
        BrentOptimizer optimizerWithCheck = new BrentOptimizer(1e-6, 1e-6, new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                double previousValue = previous.getValue();
                double currentValue = current.getValue();
                double tolerance = 0.01;
                return Math.abs(currentValue - previousValue) < tolerance;
            }
        });

        PointValuePair result = optimizerWithCheck.optimize(testFunction, new SimpleBounds(0, 10));
        assertTrue(optimizerWithCheck.getIterations() > 0);
        assertEquals(3.0, result.getPoint(), 1e-6);
        assertEquals(-4.54262359e-14, result.getValue(), 1e-15);
    }

    @Test
    public void testGoalTypeMinimize() {
        optimizer = new BrentOptimizer(1e-6, 1e-6, GoalType.MINIMIZE);
        PointValuePair result = optimizer.optimize(testFunction, new SimpleBounds(0, 10));
        assertEquals(3.0, result.getPoint(), 1e-6);
        assertEquals(-4.54262359e-14, result.getValue(), 1e-15);
    }

    @Test
    public void testGoalTypeMaximize() {
        optimizer = new BrentOptimizer(1e-6, 1e-6, GoalType.MAXIMIZE);
        PointValuePair result = optimizer.optimize(new TestFunctionMaximize(), new SimpleBounds(0, 10));
        assertEquals(7.0, result.getPoint(), 1e-6);
        assertEquals(18.0, result.getValue(), 1e-6);
    }
}

class TestFunction implements UnivariateFunction {

    @Override
    public double value(double x) {
        return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
    }
}

class TestFunctionWithMaxEval implements UnivariateFunction {

    private int maxEval;

    public TestFunctionWithMaxEval(int maxEval) {
        this.maxEval = maxEval;
    }

    @Override
    public double value(double x) throws MaxEvalException {
        if (maxEval-- > 0) {
            return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
        } else {
            throw new MaxEvalException("Maximum number of evaluations reached.");
        }
    }
}

class TestFunctionMaximize implements UnivariateFunction {

    @Override
    public double value(double x) {
        return Math.pow(Math.sin(x), 2) - Math.pow(Math.cos(x), 2);
    }
}