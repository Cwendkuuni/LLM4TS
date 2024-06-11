package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    private BrentOptimizer brentOptimizer;

    @Before
    public void setUp() {
        brentOptimizer = new BrentOptimizer(1e-6, 1e-6);
    }

    @Test
    public void testDoOptimize() {
        double min = -10.0;
        double max = 10.0;
        UnivariatePointValuePair result = brentOptimizer.doOptimize();
        double solution = result.getPoint();
        assertTrue(solution >= min && solution <= max);
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
        boolean isMinim = true;

        UnivariatePointValuePair result = brentOptimizer.best(a, b, isMinim);
        assertEquals(a, result);
    }

    @Test
    public void testBestWithNull() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = null;
        boolean isMinim = true;

        UnivariatePointValuePair result = brentOptimizer.best(a, b, isMinim);
        assertEquals(a, result);

        result = brentOptimizer.best(b, a, isMinim);
        assertEquals(a, result);
    }
}