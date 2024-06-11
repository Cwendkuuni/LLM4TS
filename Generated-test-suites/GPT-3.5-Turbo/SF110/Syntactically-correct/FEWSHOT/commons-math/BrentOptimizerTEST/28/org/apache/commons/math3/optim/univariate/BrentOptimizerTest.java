package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {
    private BrentOptimizer optimizer;

    @Before
    public void setup() {
        optimizer = new BrentOptimizer(1e-9, 1e-6);
    }

    @Test
    public void testConstructor() {
        try {
            optimizer = new BrentOptimizer(0.0, 1e-6);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // expected behavior
        }

        try {
            optimizer = new BrentOptimizer(1e-9, 0.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected behavior
        }
    }

    @Test
    public void testDoOptimize() {
        optimizer.withMaxIterations(100);

        UnivariatePointValuePair result = optimizer.optimize(100, (x) -> Math.pow(x - 2, 2));
        assertEquals(2.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);

        result = optimizer.optimize(100, (x) -> -Math.pow(x - 2, 2));
        assertEquals(2.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }
}