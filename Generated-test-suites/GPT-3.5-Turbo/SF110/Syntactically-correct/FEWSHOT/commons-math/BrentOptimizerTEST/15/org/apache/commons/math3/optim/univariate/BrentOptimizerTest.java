
package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class BrentOptimizerTest {

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRel() {
        new BrentOptimizer(-0.1, 1.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbs() {
        new BrentOptimizer(0.5, -1.0);
    }

    @Test
    public void testConstructorWithValidRelAndAbs() {
        BrentOptimizer optimizer = new BrentOptimizer(0.5, 1.0);
        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimize() {
        // Test when goal type is MINIMIZE
        BrentOptimizer optimizer = new BrentOptimizer(0.5, 1.0, null);
        optimizer.setMin(0.0);
        optimizer.setStartValue(2.0);
        optimizer.setMax(4.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(1.0, result.getPoint(), 1e-5);
        assertEquals(-1.0, result.getValue(), 1e-5);

        // Test when goal type is MAXIMIZE
        optimizer.setGoalType(GoalType.MAXIMIZE);
        result = optimizer.doOptimize();
        assertEquals(3.0, result.getPoint(), 1e-5);
        assertEquals(1.0, result.getValue(), 1e-5);
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.5, 1.0, null);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 2.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 3.0);

        boolean isMinim = true;
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
        assertEquals(a, result);

        isMinim = false;
        result = optimizer.best(a, b, isMinim);
        assertEquals(b, result);
    }
}
