package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(3, 5);
        UnivariatePointValuePair b = new UnivariatePointValuePair(4, 6);

        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        BrentOptimizer optimizer1 = new BrentOptimizer(0.01, 0.001, null);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer1.setGoalType(GoalType.MAXIMIZE);

        UnivariatePointValuePair best = optimizer.best(a, b, true);
        assertEquals(a, best);

        UnivariatePointValuePair best1 = optimizer1.best(a, b, true);
        assertEquals(b, best1);
    }

    @Test
    public void testBrentOptimizer() {
        ConvergenceChecker<UnivariatePointValuePair> checker = (iteration, previous, current) -> {
            if (current == null) {
                return false;
            }
            if (previous == null) {
                return false;
            }
            return Precision.equals(current.getValue(), previous.getValue(), 1e-6);
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, checker);
        assertNotNull(optimizer);
    }
}