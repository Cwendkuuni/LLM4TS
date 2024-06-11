package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.optim.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BrentOptimizerTest {

    // test constructor with rel, abs, and checker
    @Test
    public void testConstructor1() {
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return iteration >= 100;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01, checker);
        // assert statements
    }

    // test constructor with rel and abs
    @Test
    public void testConstructor2() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        // assert statements
    }

    // test doOptimize method
    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        // assert statements
    }

    // test best method
    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        // assert statements
    }

    // test stripLeadingAndTrailingQuotes method
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        // assert statements
    }

    // test stripLeadingHyphens method
    @Test
    public void testStripLeadingHyphens() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        // assert statements
    }
}