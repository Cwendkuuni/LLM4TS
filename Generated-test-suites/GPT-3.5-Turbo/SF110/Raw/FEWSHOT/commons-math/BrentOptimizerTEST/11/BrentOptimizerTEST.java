import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BrentOptimizerTest {
    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerConstructor1() {
        new BrentOptimizer(-1, 1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerConstructor2() {
        new BrentOptimizer(1, -1);
    }

    @Test
    public void testBrentOptimizerConstructor3() {
        BrentOptimizer optimizer = new BrentOptimizer(1, 1);
        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1, 1, null);
        optimizer.setMin(0);
        optimizer.setStartValue(1);
        optimizer.setMax(2);
        optimizer.setGoalType(GoalType.MINIMIZE);
        PointValuePair result = optimizer.optimize(100, new ObjectiveFunction(x -> x * x));
        assertNotNull(result);
        assertEquals(0, Precision.compareTo(result.getPoint(), 0), 0);
        assertEquals(0, Precision.compareTo(result.getValue(), 0), 0);
    }

    @Test
    public void testDoOptimizeWithNullConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1, 1);
        optimizer.setMin(0);
        optimizer.setStartValue(1);
        optimizer.setMax(2);
        optimizer.setGoalType(GoalType.MINIMIZE);
        PointValuePair result = optimizer.optimize(100, new ObjectiveFunction(x -> x * x));
        assertNotNull(result);
        assertEquals(0, Precision.compareTo(result.getPoint(), 0), 0);
        assertEquals(0, Precision.compareTo(result.getValue(), 0), 0);
    }

    @Test
    public void testDoOptimizeWithGoalTypeMaximize() {
        BrentOptimizer optimizer = new BrentOptimizer(1, 1, null);
        optimizer.setMin(0);
        optimizer.setStartValue(1);
        optimizer.setMax(2);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        PointValuePair result = optimizer.optimize(100, new ObjectiveFunction(x -> x * x));
        assertNotNull(result);
        assertEquals(0, Precision.compareTo(result.getPoint(), 2), 0);
        assertEquals(0, Precision.compareTo(result.getValue(), 4), 0);
    }

    @Test
    public void testDoOptimizeWithLargeMaxEvaluations() {
        BrentOptimizer optimizer = new BrentOptimizer(1, 1, null);
        optimizer.setMin(0);
        optimizer.setStartValue(1);
        optimizer.setMax(2);
        optimizer.setGoalType(GoalType.MINIMIZE);
        PointValuePair result = optimizer.optimize(1000000, new ObjectiveFunction(x -> x * x));
        assertNotNull(result);
        assertEquals(0, Precision.compareTo(result.getPoint(), 0), 0);
        assertEquals(0, Precision.compareTo(result.getValue(), 0), 0);
    }

    @Test
    public void testBestWithMinimization() {
        BrentOptimizer optimizer = new BrentOptimizer(1, 1, null);
        PointValuePair a = new PointValuePair(1, 1);
        PointValuePair b = new PointValuePair(2, 2);
        PointValuePair result = optimizer.best(a, b, true);
        assertNotNull(result);
        assertEquals(0, Precision.compareTo(result.getPoint(), 1), 0);
        assertEquals(0, Precision.compareTo(result.getValue(), 1), 0);
    }

    @Test
    public void testBestWithMaximization() {
        BrentOptimizer optimizer = new BrentOptimizer(1, 1, null);
        PointValuePair a = new PointValuePair(1, 1);
        PointValuePair b = new PointValuePair(2, 2);
        PointValuePair result = optimizer.best(a, b, false);
        assertNotNull(result);
        assertEquals(0, Precision.compareTo(result.getPoint(), 2), 0);
        assertEquals(0, Precision.compareTo(result.getValue(), 2), 0);
    }
}