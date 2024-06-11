package org.apache.commons.math.optimization.direct;

import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;
    private ObjectiveFunction function;

    @Before
    public void setup() {
        optimizer = new MultiDirectional(2.0, 0.5);
        function = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + 2 * x[1] * x[1] - 4 * x[0] - 4 * x[1] + 4;
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };
    }

    @Test
    public void testFindMinimum() {
        SimpleBounds bounds = new SimpleBounds(-5.12, 5.12, -5.12, 5.12);
        PointValuePair result = optimizer.optimize(function, bounds);
        assertEquals(-3.0, result.getPoint().length > 0 ? result.getPoint()[0] : result.getPoint(), 1e-10);
        assertEquals(-3.0, result.getValue(), 1e-10);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testMaxEvalExceeded() {
        SimpleBounds bounds = new SimpleBounds(-5.12, 5.12, -5.12, 5.12);
        optimizer.setMaxEvaluations(1);
        optimizer.optimize(function, bounds);
    }

    @Test
    public void testNullFunction() {
        assertNull(optimizer.optimize(null, new SimpleBounds(-5.12, 5.12, -5.12, 5.12)));
    }

    @Test
    public void testNullBounds() {
        assertNull(optimizer.optimize(function, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBoundsOutsideDomain() {
        SimpleBounds bounds = new SimpleBounds(-10, 5.12, -10, 5.12);
        optimizer.optimize(function, bounds);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        ObjectiveFunction function = new ObjectiveFunction() {
            @Override
            public double value(double[] x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException("Test exception");
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };
        SimpleBounds bounds = new SimpleBounds(-5.12, 5.12, -5.12, 5.12);
        optimizer.optimize(function, bounds);
    }

}