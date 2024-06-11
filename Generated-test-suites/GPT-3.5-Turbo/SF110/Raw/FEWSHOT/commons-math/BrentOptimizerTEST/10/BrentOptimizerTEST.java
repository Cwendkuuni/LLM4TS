
package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.SimplePointChecker;
import org.apache.commons.math3.optim.UnivariateObjectiveFunction;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunctionGradient;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunctionValue;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunctionValuePair;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorInvalidRelativeThreshold() {
        new BrentOptimizer(-1, 1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidAbsoluteThreshold() {
        new BrentOptimizer(0.1, 0);
    }

    @Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1);
        assertNotNull(optimizer);
        assertEquals(0.1, optimizer.getRelativeThreshold(), Precision.EPSILON);
        assertEquals(1, optimizer.getAbsoluteThreshold(), Precision.EPSILON);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testDoOptimizeMinimize() {
        GoalType goal = GoalType.MINIMIZE;
        UnivariateObjectiveFunction function = new UnivariateObjectiveFunction(x -> x * x);
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8, new SimplePointChecker<>(1e-8, 1e-8));

        ObjectiveFunctionValuePair result = optimizer.optimize(function, goal, 0, 10);

        assertEquals(0, result.getPoint(), Precision.EPSILON);
        assertEquals(0, result.getValue(), Precision.EPSILON);
    }

    @Test
    public void testDoOptimizeMaximize() {
        GoalType goal = GoalType.MAXIMIZE;
        UnivariateObjectiveFunction function = new UnivariateObjectiveFunction(x -> -x * x);
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8, new SimplePointChecker<>(1e-8, 1e-8));

        ObjectiveFunctionValuePair result = optimizer.optimize(function, goal, 0, 10);

        assertEquals(0, result.getPoint(), Precision.EPSILON);
        assertEquals(0, result.getValue(), Precision.EPSILON);
    }
}
