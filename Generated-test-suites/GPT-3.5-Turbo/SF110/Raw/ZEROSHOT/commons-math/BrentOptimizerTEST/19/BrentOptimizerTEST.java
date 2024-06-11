package org.apache.commons.math3.optim.univariate;

import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        double rel = 1e-6;
        double abs = 1e-6;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        optimizer.setMin(-10);
        optimizer.setMax(10);
        optimizer.setStartValue(0);
        optimizer.setGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 2.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 3.0);
        UnivariatePointValuePair c = new UnivariatePointValuePair(2.0, 1.0);

        UnivariatePointValuePair result1 = optimizer.best(a, b, true);
        UnivariatePointValuePair result2 = optimizer.best(a, c, true);

        assertSame(b, result1);
        assertSame(a, result2);
    }
}