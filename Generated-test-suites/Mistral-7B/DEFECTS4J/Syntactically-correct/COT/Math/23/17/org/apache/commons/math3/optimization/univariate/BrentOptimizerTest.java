package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.maximization.BrentOptimizer;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01, null);
        assertNotNull(optimizer);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeAbsoluteThreshold() {
        new BrentOptimizer(0.1, -0.01, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerRelativeThresholdBelowMinRelativeTolerance() {
        new BrentOptimizer(0, 0.01, null);
    }

    @Test
    public void testEmptyConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertNull(optimizer.getMin());
        assertNull(optimizer.getMax());
        assertNull(optimizer.getStartValue());
        assertNull(optimizer.getGoalType());
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testGettersAndSetters() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMin(1.0);
        optimizer.setMax(2.0);
        optimizer.setStartValue(1.5);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setConvergenceChecker(null);
        assertEquals(1.0, optimizer.getMin(), 0.0);
        assertEquals(2.0, optimizer.getMax(), 0.0);
        assertEquals(1.5, optimizer.getStartValue(), 0.0);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testDoOptimizeBasic() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateFunction function = f -> Math.pow(f, 2);
        UnivariatePointValuePair optimum = optimizer.optimize(function);
        assertEquals(0.0, optimum.getPoint(), 0.01);
    }

    @Test
    public void testDoOptimizeWithCustomConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.0001, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int i, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return current.getValue() < 0.001 && previous.getValue() > 0.001;
            }
        });
        UnivariateFunction function = f -> Math.pow(f, 2);
        UnivariatePointValuePair optimum = optimizer.optimize(function);
        assertEquals(0.0, optimum.getPoint(), 0.01);
    }

    @Test
    public void testDoOptimizeWithCustomStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateFunction function = f -> Math.pow(f, 2);
        optimizer.setStartValue(2.0);
        UnivariatePointValuePair optimum = optimizer.optimize(function);
        assertEquals(-1.0, optimum.getPoint(), 0.01);
    }

    @Test
    public void testDoOptimizeWithCustomMinimum() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateFunction function = f -> Math.pow(f, 2);
        optimizer.setMin(-2.0);
        UnivariatePointValuePair optimum = optimizer.optimize(function);
        assertEquals(-2.0, optimum.getPoint(), 0.01);
    }

    @Test
    public void testDoOptimizeWithCustomMaximum() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateFunction function = f -> Math.pow(f, 2);
        optimizer.setMax(1.0);
        UnivariatePointValuePair optimum = optimizer.optimize(function);
        assertEquals(1.0, optimum.getPoint(), 0.01);
    }

    @Test
    public void testDoOptimizeWithCustomGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateFunction function = f -> -Math.pow(f, 2);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair optimum = optimizer.optimize(function);
        assertEquals(0.0, optimum.getPoint(), 0.01);
    }
}

class ConvergenceChecker<T extends PointValuePair> implements ConvergenceChecker<T> {
    @Override
    public boolean converged(int iter, T previous, T current) {
        return false;
    }
}