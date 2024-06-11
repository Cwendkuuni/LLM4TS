
package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(1, 0.1);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 10);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, 20);
        UnivariatePointValuePair c = new UnivariatePointValuePair(3, 5);

        assertEquals(a, optimizer.best(a, null, true));
        assertEquals(b, optimizer.best(null, b, true));
        assertEquals(a, optimizer.best(a, b, true));
        assertEquals(c, optimizer.best(a, c, true));
        assertEquals(a, optimizer.best(c, a, true));
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1, 0.1);

        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0);
        optimizer.setStartValue(1);
        optimizer.setMax(2);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertEquals(0, Precision.compareTo(1.0, result.getPoint(), 0.001));
        assertEquals(0, Precision.compareTo(0, result.getValue(), 0.001));
    }

    @Test
    public void testConstructor() {
        double relative = 1;
        double absolute = 0.1;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;

        BrentOptimizer optimizer = new BrentOptimizer(relative, absolute, checker);

        assertEquals(relative, optimizer.getRelativeThreshold(), 0.001);
        assertEquals(absolute, optimizer.getAbsoluteThreshold(), 0.001);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorException1() {
        double relative = 0.99;
        double absolute = 0.1;

        new BrentOptimizer(relative, absolute);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorException2() {
        double relative = 1;
        double absolute = 0;

        new BrentOptimizer(relative, absolute);
    }
}
