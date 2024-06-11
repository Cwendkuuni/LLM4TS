package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class BrentOptimizerTest {
    @Test
    public void testBrentOptimizer() {
        double rel = 0.1;
        double abs = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerInvalidRel() {
        double rel = 0.01;
        double abs = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerInvalidAbs() {
        double rel = 0.1;
        double abs = 0.0;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.001);
        optimizer.setMin(0.0);
        optimizer.setMax(1.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(new UnivariateObjectiveFunction() {
            public double value(double x) {
                return x * x;
            }
        });
        assertEquals(0.0, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }
}