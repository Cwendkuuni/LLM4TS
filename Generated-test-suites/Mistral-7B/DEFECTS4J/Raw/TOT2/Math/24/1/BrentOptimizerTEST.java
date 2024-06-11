import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.initializer.ValueSelector;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraint;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerWithSimpleFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        Function function = new Function() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        PointValuePair result = optimizer.optimize(function, new ValueSelector(0.0), null, GoalType.MINIMIZE);
        assertEquals(0.0, result.getPoint(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithNegativeAbsoluteThreshold() {
        new BrentOptimizer(-1e-5, 1e-5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithRelativeThresholdTooSmall() {
        new BrentOptimizer(0.5 * FastMath.ulp(1d), 1e-5);
    }

    @Test
    public void testBrentOptimizerWithLinearConstraint() {
        Function function = new Function() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        LinearConstraint constraint = new LinearConstraint(1.0, 1.0, -1.0, true);
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        PointValuePair result = optimizer.optimize(function, null, constraint, GoalType.MINIMIZE);
        assertTrue(result.getPoint() > 1.0);
    }
}

class Function {
    public abstract double value(double x);
}