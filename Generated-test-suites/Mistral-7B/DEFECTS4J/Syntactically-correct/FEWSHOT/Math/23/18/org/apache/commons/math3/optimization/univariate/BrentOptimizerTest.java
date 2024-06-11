package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;

import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstruction() {
        assertNull(new BrentOptimizer(0d, 0d));
        assertThrows(NotStrictlyPositiveException.class, () -> new BrentOptimizer(-1d, 1d));
        assertThrows(NumberIsTooSmallException.class, () -> new BrentOptimizer(0.5d, 0d));
        assertThrows(NotStrictlyPositiveException.class, () -> new BrentOptimizer(1d, -1d));
        assertThrows(NumberIsTooSmallException.class, () -> new BrentOptimizer(1d, 0d));

        BrentOptimizer optimizer1 = new BrentOptimizer(1d, 1d);
        BrentOptimizer optimizer2 = new BrentOptimizer(2d, 1d);
        BrentOptimizer optimizer3 = new BrentOptimizer(1d, 2d);
        BrentOptimizer optimizer4 = new BrentOptimizer(1d, 3d);

        assertEquals(1d, optimizer1.getRelativeThreshold(), 0.0);
        assertEquals(1d, optimizer1.getAbsoluteThreshold(), 0.0);

        assertEquals(2d, optimizer2.getRelativeThreshold(), 0.0);
        assertEquals(1d, optimizer2.getAbsoluteThreshold(), 0.0);

        assertEquals(1d, optimizer3.getRelativeThreshold(), 0.0);
        assertEquals(2d, optimizer3.getAbsoluteThreshold(), 0.0);

        assertEquals(1d, optimizer4.getRelativeThreshold(), 0.0);
        assertEquals(3d, optimizer4.getAbsoluteThreshold(), 0.0);
    }

    @Test
    public void testBrentOptimizerEmptyConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertNull(optimizer.getRelativeThreshold());
        assertNull(optimizer.getAbsoluteThreshold());
    }

    @Test
    public void testBrentOptimizerWithCheckers() {
        ConvergenceChecker<UnivariatePointValuePair> checker = (iter, a, b) -> {
            assertArrayEquals(new double[] { a.getX(), b.getX() }, new double[] { a.getX() + 0.001d, b.getX() - 0.001d });
            return true;
        };

        BrentOptimizer optimizer = new BrentOptimizer(1d, 1d, checker);
        UnivariatePointValuePair result = optimizer.optimize(x -> Math.pow(x, 2.0), -5d, 5d, null);

        assertEquals(-2.44948974278317, result.getX(), 0.001);
        assertEquals(-2.44948974278317, result.getValue(), 0.001);

        assertArrayEquals(new double[] { result.getX(), result.getX() + 0.001d }, new double[] { result.getX() + 0.001d, result.getX() - 0.001d });
    }

    @Test
    public void testBrentOptimizerMinimization() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01d, 1d);

        double[] testData = { 1.2, 2.8, 3.5, 4.1, 4.2, 4.5, 5.0, 5.5, 6.0, 6.5 };
        double lowestValue = 4.1;
        double lowestPoint = 4.1;

        for (double point : testData) {
            UnivariatePointValuePair result = optimizer.optimize(x -> Math.pow(x, 2.0) + 2 * x + 1, -10d, 10d, null);
            double computedValue = result.getValue();

            if (computedValue < lowestValue) {
                lowestValue = computedValue;
                lowestPoint = result.getX();
            }
        }

        assertEquals(lowestPoint, optimizer.findMin(x -> Math.pow(x, 2.0) + 2 * x + 1), 0.001);
    }

    @Test
    public void testBrentOptimizerMaximization() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01d, 1d);

        double[] testData = { 1.2, 2.8, 3.5, 4.1, 4.2, 4.5, 5.0, 5.5, 6.0, 6.5 };
        double highestValue = 6.5;
        double highestPoint = 6.5;

        for (double point : testData) {
            UnivariatePointValuePair result = optimizer.optimize(x -> Math.pow(x, 2.0) - 2 * x + 1, -10d, 10d, null);
            double computedValue = result.getValue();

            if (computedValue > highestValue) {
                highestValue = computedValue;
                highestPoint = result.getX();
            }
        }

        assertEquals(highestPoint, optimizer.findMax(x -> Math.pow(x, 2.0) - 2 * x + 1), 0.001);
    }

    @Test
    public void testBrentOptimizerWithGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01d, 1d);

        double[] testData = { 1.2, 2.8, 3.5, 4.1, 4.2, 4.5, 5.0, 5.5, 6.0, 6.5 };
        double lowestValue = 4.1;
        double lowestPoint = 4.1;

        for (double point : testData) {
            UnivariatePointValuePair result = optimizer.optimize(x -> Math.pow(x, 2.0) + 2 * x + 1, -10d, 10d, GoalType.MINIMIZE);
            double computedValue = result.getValue();

            if (computedValue < lowestValue) {
                lowestValue = computedValue;
                lowestPoint = result.getX();
            }
        }

        assertEquals(lowestPoint, optimizer.findMin(x -> Math.pow(x, 2.0) + 2 * x + 1), 0.001);

        double highestValue = 6.5;
        double highestPoint = 6.5;

        for (double point : testData) {
            UnivariatePointValuePair result = optimizer.optimize(x -> Math.pow(x, 2.0) - 2 * x + 1, -10d, 10d, GoalType.MAXIMIZE);
            double computedValue = result.getValue();

            if (computedValue > highestValue) {
                highestValue = computedValue;
                highestPoint = result.getX();
            }
        }

        assertEquals(highestPoint, optimizer.findMax(x -> Math.pow(x, 2.0) - 2 * x + 1), 0.001);
    }

    @Test
    public void testBrentOptimizerWithCustomConvergenceChecker() {
        ConvergenceChecker<UnivariatePointValuePair> checker = (iter, a, b) -> {
            double tolerance = 1e-6;
            if (Math.abs(a.getValue() - b.getValue()) < tolerance) {
                return true;
            }
            return false;
        };

        BrentOptimizer optimizer = new BrentOptimizer(1d, 1d, checker);

        UnivariatePointValuePair result = optimizer.optimize(x -> Math.sin(x), -10d, 10d, null);

        assertEquals(Math.PI / 2d, result.getX(), 1e-5);
        assertEquals(0d, result.getValue(), 1e-5);
    }
}