package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorChecksRel() {
        new BrentOptimizer(-0.5, 1.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorChecksAbs() {
        new BrentOptimizer(0.1, 0.0);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0);
        // TODO: write test case
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer();
        // TODO: write test case
    }
}