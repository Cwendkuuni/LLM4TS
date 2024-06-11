package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.junit.Test;

public class BrentOptimizerTest {
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_withNegativeRelativeThreshold() {
        new BrentOptimizer(-1, 0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_withZeroAbsoluteThreshold() {
        new BrentOptimizer(1, 0);
    }

    @Test
    public void testConstructor_withValidParameters() {
        BrentOptimizer optimizer = new BrentOptimizer(1, 1);
        assertEquals(1, optimizer.getRelativeThreshold(), 0);
        assertEquals(1, optimizer.getAbsoluteThreshold(), 0);
    }
}