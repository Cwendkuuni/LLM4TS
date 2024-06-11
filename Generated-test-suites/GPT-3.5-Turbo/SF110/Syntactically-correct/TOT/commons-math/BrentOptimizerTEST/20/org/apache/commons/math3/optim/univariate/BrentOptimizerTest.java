package org.apache.commons.math3.optim.univariate;

import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testConstructor() {
        // Typical use case
        BrentOptimizer optimizer1 = new BrentOptimizer(0.01, 0.1);
        assertNotNull(optimizer1);

        // Edge case - minimum valid relative threshold
        BrentOptimizer optimizer2 = new BrentOptimizer(2.0 * Math.ulp(1.0), 0.1);
        assertNotNull(optimizer2);

        // Edge case - minimum valid absolute threshold
        BrentOptimizer optimizer3 = new BrentOptimizer(0.01, Double.MIN_VALUE);
        assertNotNull(optimizer3);

        // Error scenario - relative threshold below minimum allowed value
        try {
            BrentOptimizer optimizer4 = new BrentOptimizer(-0.01, 0.1);
            fail("Expected NumberIsTooSmallException to be thrown");
        } catch (NumberIsTooSmallException e) {
            // Expected exception
        }

        // Error scenario - absolute threshold below minimum allowed value
        try {
            BrentOptimizer optimizer5 = new BrentOptimizer(0.01, -0.1);
            fail("Expected NotStrictlyPositiveException to be thrown");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testDoOptimize() {
        // Typical use case
        BrentOptimizer optimizer1 = new BrentOptimizer(0.01, 0.1);
        optimizer1.setMin(0.0);
        optimizer1.setMax(10.0);
        optimizer1.setStartValue(5.0);
        GoalType goalType1 = GoalType.MINIMIZE;
        UnivariatePointValuePair result1 = optimizer1.doOptimize();
        assertNotNull(result1);
        assertEquals(7.853981633974483, result1.getValue(), 1e-10);

        // Edge case - min equals max
        BrentOptimizer optimizer2 = new BrentOptimizer(0.01, 0.1);
        optimizer2.setMin(0.0);
        optimizer2.setMax(0.0);
        optimizer2.setStartValue(0.0);
        GoalType goalType2 = GoalType.MINIMIZE;
        UnivariatePointValuePair result2 = optimizer2.doOptimize();
        assertNotNull(result2);
        assertEquals(0.0, result2.getValue(), 1e-10);

        // Error scenario - goal type not set
        BrentOptimizer optimizer3 = new BrentOptimizer(0.01, 0.1);
        optimizer3.setMin(0.0);
        optimizer3.setMax(10.0);
        optimizer3.setStartValue(5.0);
        try {
            UnivariatePointValuePair result3 = optimizer3.doOptimize();
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testBest() {
        // Typical use case - a is better
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
        boolean isMinim = true;
        BrentOptimizer optimizer1 = new BrentOptimizer(0.01, 0.1);
        UnivariatePointValuePair result1 = optimizer1.best(a, b, isMinim);
        assertNotNull(result1);
        assertEquals(a, result1);

        // Typical use case - b is better
        UnivariatePointValuePair c = new UnivariatePointValuePair(3.0, 2.0);
        UnivariatePointValuePair d = new UnivariatePointValuePair(4.0, 4.0);
        BrentOptimizer optimizer2 = new BrentOptimizer(0.01, 0.1);
        UnivariatePointValuePair result2 = optimizer2.best(c, d, isMinim);
        assertNotNull(result2);
        assertEquals(c, result2);

        // Error scenario - a and b have same value, a should be returned
        UnivariatePointValuePair e = new UnivariatePointValuePair(5.0, 5.0);
        UnivariatePointValuePair f = new UnivariatePointValuePair(5.0, 5.0);
        BrentOptimizer optimizer3 = new BrentOptimizer(0.01, 0.1);
        UnivariatePointValuePair result3 = optimizer3.best(e, f, isMinim);
        assertNotNull(result3);
        assertEquals(e, result3);
    }

}