package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;
    private ConvergenceChecker<UnivariatePointValuePair> checker;

    @Before
    public void setup() {
        optimizer = new BrentOptimizer(1e-5, 1e-6);
        checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return previous != null && current != null && previous.getValue() == current.getValue();
            }
        };
    }

    @Test
    public void testOptimizerWithDefaultValues() {
        optimizer = new BrentOptimizer(null, null, checker);
        UnivariatePointValuePair result = optimizer.optimize(x -> Math.pow(x, 2), -5.0, 5.0);
        assertEquals(-2.44948974278317, result.getValue(), 1e-6);
        assertEquals(-2.44948974278317, result.getPoint(), 1e-6);
    }

    @Test
    public void testOptimizerWithPredefinedValues() {
        optimizer = new BrentOptimizer(1e-5, 1e-6, checker);
        UnivariatePointValuePair result = optimizer.optimize(x -> Math.pow(x, 2), -5.0, 5.0);
        assertEquals(-2.44948974278317, result.getValue(), 1e-6);
        assertEquals(-2.44948974278317, result.getPoint(), 1e-6);
    }

    @Test
    public void testOptimizerWithPredefinedValuesAndCustomChecker() {
        optimizer = new BrentOptimizer(1e-5, 1e-6, checker);
        UnivariatePointValuePair result = optimizer.optimize(x -> Math.pow(x, 2), -5.0, 5.0);
        assertEquals(-2.44948974278317, result.getValue(), 1e-6);
        assertEquals(-2.44948974278317, result.getPoint(), 1e-6);
        assertTrue(checker.converged(optimizer.getIterations(), null, result));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidAbsoluteThreshold() {
        optimizer = new BrentOptimizer(-1e-6, 1e-5, checker);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testInvalidRelativeThreshold() {
        optimizer = new BrentOptimizer(1e-7, 1e-6, checker);
    }

    @Test
    public void testOptimizerWithGoalType() {
        optimizer = new BrentOptimizer(1e-5, 1e-6, checker);
        UnivariatePointValuePair result = optimizer.optimize(x -> -Math.pow(x, 2), -5.0, 5.0);
        assertEquals(2.44948974278317, result.getValue(), 1e-6);
        assertEquals(2.44948974278317, result.getPoint(), 1e-6);
    }

    @Test
    public void testOptimizerWithDifferentIntervals() {
        optimizer = new BrentOptimizer(1e-5, 1e-6, checker);
        UnivariatePointValuePair result = optimizer.optimize(x -> Math.sin(x), 1.0, 2.0);
        assertEquals(1.27323954473580, result.getValue(), 1e-5);
        assertEquals(1.27323954473580, result.getPoint(), 1e-5);
    }

    @Test
    public void testOptimizerWithConvergedPoints() {
        optimizer = new BrentOptimizer(1e-5, 1e-6, checker);
        List<UnivariatePointValuePair> points = new ArrayList<>();
        points.add(new UnivariatePointValuePair(1.5, 1.25));
        points.add(new UnivariatePointValuePair(1.6, 1.24));
        points.add(new UnivariatePointValuePair(1.7, 1.23));
        points.add(new UnivariatePointValuePair(1.8, 1.22));
        UnivariatePointValuePair result = optimizer.optimize(x -> Math.pow(x, 2), 1.0, 2.0, points);
        assertEquals(1.5, result.getPoint(), 1e-6);
        assertEquals(1.25, result.getValue(), 1e-6);
    }
}