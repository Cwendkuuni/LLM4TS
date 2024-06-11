import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(0.1, 0.01);
    }

    @Test
    public void testDoOptimize_Minimize() {
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
        assertTrue(result.getPoint() >= optimizer.getMin());
        assertTrue(result.getPoint() <= optimizer.getMax());
    }

    @Test
    public void testDoOptimize_Maximize() {
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MAXIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
        assertTrue(result.getPoint() >= optimizer.getMin());
        assertTrue(result.getPoint() <= optimizer.getMax());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_RelativeThresholdNotPositive() {
        new BrentOptimizer(0, 0.01);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_AbsoluteThresholdTooSmall() {
        new BrentOptimizer(0.1, 0);
    }

    @Test
    public void testBest() {
        double a = 1;
        double b = 2;
        double aValue = 3;
        double bValue = 2;

        UnivariatePointValuePair pointA = new UnivariatePointValuePair(a, aValue);
        UnivariatePointValuePair pointB = new UnivariatePointValuePair(b, bValue);

        UnivariatePointValuePair best = optimizer.best(pointA, pointB, true);

        assertNotNull(best);
        assertEquals(a, best.getPoint(), 0);
        assertEquals(aValue, best.getValue(), 0);
    }
}