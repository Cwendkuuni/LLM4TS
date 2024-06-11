package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.1);
        assertEquals(optimizer.getRelativeThreshold(), 0.001, Precision.EPSILON);
        assertEquals(optimizer.getAbsoluteThreshold(), 0.1, Precision.EPSILON);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerInvalidRelativeThreshold() {
        new BrentOptimizer(0.0005, 0.1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerInvalidAbsoluteThreshold() {
        new BrentOptimizer(0.001, -0.1);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.1);
        optimizer.setGoalType(GoalType.MINIMIZE);
        double result = optimizer.doOptimize().getPoint();
        assertEquals(result, 0.0, Precision.EPSILON);
    }
}