import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Create instance of BrentOptimizer
        double rel = 0.1;
        double abs = 0.01;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        // Set parameters for optimization
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);

        // Call optimization method
        UnivariatePointValuePair result = optimizer.doOptimize();

        // Check if result is not null
        assertNotNull(result);

        // Check if result is within the interval
        assertTrue(result.getPoint() >= optimizer.getMin());
        assertTrue(result.getPoint() <= optimizer.getMax());

        // Check if result is the expected minimum value
        assertEquals(0.0, result.getValue(), 0.0001);
    }

    @Test
    public void testDoOptimizeWithNegativeInterval() {
        // Create instance of BrentOptimizer
        double rel = 0.1;
        double abs = 0.01;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        // Set parameters for optimization
        optimizer.setMin(-10);
        optimizer.setMax(-5);
        optimizer.setStartValue(-7);
        optimizer.setGoalType(GoalType.MAXIMIZE);

        // Call optimization method
        UnivariatePointValuePair result = optimizer.doOptimize();

        // Check if result is not null
        assertNotNull(result);

        // Check if result is within the interval
        assertTrue(result.getPoint() >= optimizer.getMin());
        assertTrue(result.getPoint() <= optimizer.getMax());

        // Check if result is the expected maximum value
        assertEquals(0.0, result.getValue(), 0.0001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeAbsoluteThreshold() {
        // Create instance of BrentOptimizer with negative absolute threshold
        double rel = 0.1;
        double abs = -0.01;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithSmallRelativeThreshold() {
        // Create instance of BrentOptimizer with small relative threshold
        double rel = 0.000001;
        double abs = 0.01;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }
}