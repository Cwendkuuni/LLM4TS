import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.ValueInitializer;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.linear.LinearFunction;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer_SimpleFunctionMinimization() {
        UnivariateFunction function = new LinearFunction(new ValueInitializer(-10.0), 2.0);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function);
        assertEquals(-1.0, result.getPoint(), 1e-6);
    }

    @Test
    public void testBrentOptimizer_SimpleFunctionMaximization() {
        UnivariateFunction function = new LinearFunction(new ValueInitializer(10.0), -2.0);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        PointValuePair result = optimizer.optimize(function);
        assertEquals(1.0, result.getPoint(), 1e-6);
    }

    @Test
    public void testBrentOptimizer_NonZeroStartingPoint() {
        UnivariateFunction function = new LinearFunction(new ValueInitializer(0.0), 1.0);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function, 2.0);
        assertEquals(-1.0, result.getPoint(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizer_InvalidAbsoluteThreshold() {
        new BrentOptimizer(-1.0, 1e-6, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizer_InvalidRelativeThreshold() {
        new BrentOptimizer(1e-6, 0.0, null);
    }

    @Test(expected = NullPointerException.class)
    public void testBrentOptimizer_NullFunction() {
        new BrentOptimizer(1e-6, 1e-6, null).optimize(null);
    }

    @Test
    public void testBrentOptimizer_FunctionWithInflectionPoint() {
        UnivariateFunction function = new LinearFunction(new ValueInitializer(0.0), 0.0);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function);
        assertEquals(0.0, result.getPoint(), 1e-6);
    }

    @Test
    public void testBrentOptimizer_FunctionWithMultipleRoots() {
        UnivariateFunction function = new LinearFunction(new ValueInitializer(0.0), x -> x * (x - 1.0));
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function);
        assertEquals(0.5, result.getPoint(), 1e-6);
    }
}