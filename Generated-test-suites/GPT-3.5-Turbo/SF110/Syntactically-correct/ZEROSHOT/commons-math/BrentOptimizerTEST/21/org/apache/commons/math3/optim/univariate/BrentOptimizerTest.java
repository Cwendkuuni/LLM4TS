import org.junit.Test;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;

public class BrentOptimizerTest {

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor1() {
        new BrentOptimizer(0.0, 1.0, null);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor2() {
        new BrentOptimizer(1.0, 0.0, null);
    }

    @Test
    public void testDoOptimize() {
        double rel = 1.0;
        double abs = 1.0;
        UnivariateFunction objective = new UnivariateFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;
        UnivariateOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair result = optimizer.optimize(new MaxEval(100),
                objective, goalType, min, max, startValue);
        double expected = (goalType == GoalType.MINIMIZE) ? 0.0 : 100.0;
        assertEquals(expected, result.getValue(), 0.001);
    }

    @Test
    public void testBest() {
        boolean isMinim = true;
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 2.0);
        UnivariateOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        assertEquals(a, optimizer.best(a, b, isMinim));
    }

}