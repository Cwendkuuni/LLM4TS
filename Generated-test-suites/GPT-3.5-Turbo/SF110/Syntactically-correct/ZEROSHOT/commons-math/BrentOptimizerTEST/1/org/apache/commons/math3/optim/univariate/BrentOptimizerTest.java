package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testConstructor() {
        double rel = 0.1;
        double abs = 0.01;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        assertEquals(rel, optimizer.relativeThreshold, 0.0);
        assertEquals(abs, optimizer.absoluteThreshold, 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorInvalidRel() {
        double rel = -0.1;
        double abs = 0.01;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidAbs() {
        double rel = 0.1;
        double abs = 0.0;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }

    @Test
    public void testDoOptimize() {
        double rel = 0.1;
        double abs = 0.01;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        double minValue = optimizer.doOptimize();
        // perform assertion based on expected result
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 2.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 4.0);
        boolean isMinim = true;
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
        assertEquals(b, result);
    }
}