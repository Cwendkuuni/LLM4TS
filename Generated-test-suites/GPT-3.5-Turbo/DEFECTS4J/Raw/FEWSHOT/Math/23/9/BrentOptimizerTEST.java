package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class BrentOptimizerTest {
    @Test
    public void testBrentOptimizerConstructor_withChecker() {
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            public boolean converged(int iteration,
                                     UnivariatePointValuePair previous,
                                     UnivariatePointValuePair current) {
                return true;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.1, checker);
        assertNotNull(optimizer);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerConstructor_withChecker_shouldThrowNumberIsTooSmallException()
    {
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            public boolean converged(int iteration,
                                     UnivariatePointValuePair previous,
                                     UnivariatePointValuePair current) {
                return true;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(0.000000001, 0.1, checker);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerConstructor_withChecker_shouldThrowNotStrictlyPositiveException() {
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            public boolean converged(int iteration,
                                     UnivariatePointValuePair previous,
                                     UnivariatePointValuePair current) {
                return true;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(0.5, -0.1, checker);
    }

    @Test
    public void testBrentOptimizerConstructor_withoutChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.1);
        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.1);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(0.5, 0.01);
        UnivariatePointValuePair b = new UnivariatePointValuePair(0.4, 0.01);
        boolean isMinim = true;
        UnivariatePointValuePair best = optimizer.best(a, b, isMinim);
        assertNotNull(best);
        assertEquals(a.getValue(), best.getValue());
    }
}