package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BrentOptimizerTest {
    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 1e-10);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(0, result.getPoint(), 1e-10);
        // Add more test cases here to comprehensively test the method
    }
}