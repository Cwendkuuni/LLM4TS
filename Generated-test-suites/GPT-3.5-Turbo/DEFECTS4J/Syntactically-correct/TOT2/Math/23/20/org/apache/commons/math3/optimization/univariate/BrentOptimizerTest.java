import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1e-6, 1e-6);
    }

    @Test
    public void testMinimize() {
        double minValue = optimizer.optimize(100, new Sin(), GoalType.MINIMIZE, 0, 6, 3);
        assertEquals(3.14159, minValue, 0.00001);
    }

    @Test
    public void testMaximize() {
        double maxValue = optimizer.optimize(100, new Sin(), GoalType.MAXIMIZE, 0, 6, 3);
        assertEquals(0, maxValue, 0.00001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAbsoluteThreshold() {
        optimizer = new BrentOptimizer(1e-6, -1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSmallRelativeThreshold() {
        optimizer = new BrentOptimizer(1e-20, 1e-6);
    }

    @Test
    public void testEdgeCaseLowerBound() {
        double value = optimizer.optimize(100, new Sin(), GoalType.MINIMIZE, 0, 6, 0);
        assertEquals(0, value, 0.00001);
    }

    @Test
    public void testEdgeCaseUpperBound() {
        double value = optimizer.optimize(100, new Sin(), GoalType.MAXIMIZE, 0, 6, 6);
        assertEquals(0, value, 0.00001);
    }

}