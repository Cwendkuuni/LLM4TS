import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerInitialization() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        assertNotNull(optimizer);
        assertEquals(1e-6, optimizer.getRelativeThreshold(), 0);
        assertEquals(1e-6, optimizer.getAbsoluteThreshold(), 0);
    }

    @Test
    public void testOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair result = optimizer.optimize(100, x -> x * x, BrentOptimizer.GoalType.MINIMIZE);
        assertEquals(0, result.getPoint(), 1e-6);
        assertEquals(0, result.getValue(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAbsoluteThreshold() {
        new BrentOptimizer(1e-6, -1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSmallRelativeThreshold() {
        new BrentOptimizer(1e-12, 1e-6);
    }

    @Test
    public void testGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        assertEquals(BrentOptimizer.GoalType.MINIMIZE, optimizer.getGoalType());
        optimizer.setGoalType(BrentOptimizer.GoalType.MAXIMIZE);
        assertEquals(BrentOptimizer.GoalType.MAXIMIZE, optimizer.getGoalType());
    }

    // Add more test cases as needed

}