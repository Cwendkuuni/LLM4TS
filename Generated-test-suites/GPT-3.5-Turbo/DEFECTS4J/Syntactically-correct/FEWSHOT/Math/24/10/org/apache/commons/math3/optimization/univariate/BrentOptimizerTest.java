package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Test
    public void testBrentOptimizer() {
        optimizer = new BrentOptimizer(0.001, 0.0);
        assertNotNull(optimizer);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithNegativeAbsoluteThreshold() {
        optimizer = new BrentOptimizer(0.001, -1.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithZeroAbsoluteThreshold() {
        optimizer = new BrentOptimizer(0.001, 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithSmallRelativeThreshold() {
        optimizer = new BrentOptimizer(0.0001, 1.0);
    }

    @Test
    public void testDoOptimize() {
        optimizer = new BrentOptimizer(0.001, 0.0);
        double result = optimizer.doOptimize();
        assertEquals(0, result, 0.001);
    }

    @Test
    public void testDoOptimizeWithNegativeAbsoluteThreshold() {
        optimizer = new BrentOptimizer(0.001, -1.0);
        double result = optimizer.doOptimize();
        assertEquals(0, result, 0.001);
    }

    @Test
    public void testDoOptimizeWithZeroAbsoluteThreshold() {
        optimizer = new BrentOptimizer(0.001, 0.0);
        double result = optimizer.doOptimize();
        assertEquals(0, result, 0.001);
    }

    @Test
    public void testDoOptimizeWithSmallRelativeThreshold() {
        optimizer = new BrentOptimizer(0.0001, 1.0);
        double result = optimizer.doOptimize();
        assertEquals(0, result, 0.001);
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair point1 = new UnivariatePointValuePair(0, 0);
        UnivariatePointValuePair point2 = new UnivariatePointValuePair(1, 1);
        boolean isMinim = true;

        UnivariatePointValuePair result = optimizer.best(point1, point2, isMinim);
        assertEquals(point1, result);

        isMinim = false;
        result = optimizer.best(point1, point2, isMinim);
        assertEquals(point2, result);
    }

}