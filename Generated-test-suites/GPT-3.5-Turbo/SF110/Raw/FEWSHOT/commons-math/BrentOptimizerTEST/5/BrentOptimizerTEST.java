package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {
    private BrentOptimizer optimizer;

    @Before
    public void setup() {
        optimizer = new BrentOptimizer(0.1, 0.01);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelativeThresholdThrowsException() {
        optimizer = new BrentOptimizer(0.0, 0.01);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsoluteThresholdThrowsException() {
        optimizer = new BrentOptimizer(0.1, 0);
    }

    @Test
    public void testDoOptimize() {
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
        assertEquals(0.0, result.getValue(), 0.0);
        assertEquals(0.0, result.getPoint(), 0.0);
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 2.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 1.0);

        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertNotNull(result);
        assertEquals(1.0, result.getValue(), 0.0);
        assertEquals(2.0, result.getPoint(), 0.0);

        result = optimizer.best(a, b, false);
        assertNotNull(result);
        assertEquals(2.0, result.getValue(), 0.0);
        assertEquals(1.0, result.getPoint(), 0.0);
    }
}