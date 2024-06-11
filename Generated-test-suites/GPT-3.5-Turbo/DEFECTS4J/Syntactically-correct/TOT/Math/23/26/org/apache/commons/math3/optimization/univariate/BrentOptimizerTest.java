import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    // Test the constructor with valid input
    @Test
    public void testConstructorValidInput() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.01, 0.001);
        assertNotNull(brentOptimizer);
    }

    // Test the constructor with invalid relative threshold
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorInvalidRelativeThreshold() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.0001, 0.001);
    }

    // Test the constructor with invalid absolute threshold
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidAbsoluteThreshold() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.01, -0.001);
    }

    // Test the doOptimize method when getMin() < getMax() and getGoalType() is MINIMIZE
    @Test
    public void testDoOptimizeMinimize() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.01, 0.001);
        brentOptimizer.setMin(0);
        brentOptimizer.setMax(10);
        brentOptimizer.setStartValue(5);
        brentOptimizer.setGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair result = brentOptimizer.doOptimize();

        assertNotNull(result);
        assertEquals(0, result.getPoint(), 0.001);
        assertEquals(0, result.getValue(), 0.001);
    }

    // Test the doOptimize method when getMin() > getMax() and getGoalType() is MINIMIZE
    @Test
    public void testDoOptimizeMinimizeReverse() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.01, 0.001);
        brentOptimizer.setMin(10);
        brentOptimizer.setMax(0);
        brentOptimizer.setStartValue(5);
        brentOptimizer.setGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair result = brentOptimizer.doOptimize();

        assertNotNull(result);
        assertEquals(0, result.getPoint(), 0.001);
        assertEquals(0, result.getValue(), 0.001);
    }

    // Test the doOptimize method when getMin() < getMax() and getGoalType() is MAXIMIZE
    @Test
    public void testDoOptimizeMaximize() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.01, 0.001);
        brentOptimizer.setMin(0);
        brentOptimizer.setMax(10);
        brentOptimizer.setStartValue(5);
        brentOptimizer.setGoalType(GoalType.MAXIMIZE);

        UnivariatePointValuePair result = brentOptimizer.doOptimize();

        assertNotNull(result);
        assertEquals(10, result.getPoint(), 0.001);
        assertEquals(10, result.getValue(), 0.001);
    }

    // Test the doOptimize method when getMin() > getMax() and getGoalType() is MAXIMIZE
    @Test
    public void testDoOptimizeMaximizeReverse() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.01, 0.001);
        brentOptimizer.setMin(10);
        brentOptimizer.setMax(0);
        brentOptimizer.setStartValue(5);
        brentOptimizer.setGoalType(GoalType.MAXIMIZE);

        UnivariatePointValuePair result = brentOptimizer.doOptimize();

        assertNotNull(result);
        assertEquals(10, result.getPoint(), 0.001);
        assertEquals(10, result.getValue(), 0.001);
    }

    // Test the best method when both points are null
    @Test
    public void testBestBothNull() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.01, 0.001);
        UnivariatePointValuePair result = brentOptimizer.best(null, null, true);
        assertNull(result);
    }

    // Test the best method when point a is null
    @Test
    public void testBestPointANull() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.01, 0.001);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 1);
        UnivariatePointValuePair result = brentOptimizer.best(null, b, true);
        assertEquals(b, result);
    }

    // Test the best method when point b is null
    @Test
    public void testBestPointBNull() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.01, 0.001);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 0);
        UnivariatePointValuePair result = brentOptimizer.best(a, null, true);
        assertEquals(a, result);
    }

    // Test the best method when point a has lower value and goal type is MINIMIZE
    @Test
    public void testBestMinimizeALowerValue() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.01, 0.001);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 1);
        UnivariatePointValuePair result = brentOptimizer.best(a, b, true);
        assertEquals(a, result);
    }

    // Test the best method when point b has lower value and goal type is MINIMIZE
    @Test
    public void testBestMinimizeBLowerValue() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.01, 0.001);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 1);
        UnivariatePointValuePair b = new UnivariatePointValuePair(0, 0);
        UnivariatePointValuePair result = brentOptimizer.best(a, b, true);
        assertEquals(b, result);
    }

    // Test the best method when point a has higher value and goal type is MAXIMIZE
    @Test
    public void testBestMaximizeAHigherValue() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.01, 0.001);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 1);
        UnivariatePointValuePair result = brentOptimizer.best(a, b, false);
        assertEquals(a, result);
    }

    // Test the best method when point b has higher value and goal type is MAXIMIZE
    @Test
    public void testBestMaximizeBHigherValue() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.01, 0.001);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 1);
        UnivariatePointValuePair b = new UnivariatePointValuePair(0, 0);
        UnivariatePointValuePair result = brentOptimizer.best(a, b, false);
        assertEquals(b, result);
    }

}