package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithChecker() {
        ConvergenceChecker<UnivariatePointValuePair> checker = new MockConvergenceChecker();
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01, checker);

        assertNotNull(optimizer);
        assertEquals(0.1, optimizer.relativeThreshold, 0.001);
        assertEquals(0.01, optimizer.absoluteThreshold, 0.001);
    }

    @Test
    public void testConstructorWithoutChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);

        assertNotNull(optimizer);
        assertEquals(0.1, optimizer.relativeThreshold, 0.001);
        assertEquals(0.01, optimizer.absoluteThreshold, 0.001);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0.0);
        optimizer.setMax(1.0);
        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 0.0);
        UnivariatePointValuePair result = optimizer.best(a, b, true);

        assertNotNull(result);
        assertEquals(a, result);
    }

    class MockConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return false;
        }
    }
}