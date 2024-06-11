import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {
    
    @Test
    public void testDoOptimize() {
        // Test case 1
        BrentOptimizer optimizer = new BrentOptimizer(1, 1);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result = optimizer.doOptimize();
        double expectedValue = 0.0;
        double expectedPoint = 10.0;
        assertEquals(expectedValue, result.getValue(), 0.0001);
        assertEquals(expectedPoint, result.getPoint(), 0.0001);

        // Test case 2
        optimizer = new BrentOptimizer(2, 2);
        optimizer.setMin(-5);
        optimizer.setMax(5);
        optimizer.setStartValue(-5);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        result = optimizer.doOptimize();
        expectedValue = 0.0;
        expectedPoint = -5.0;
        assertEquals(expectedValue, result.getValue(), 0.0001);
        assertEquals(expectedPoint, result.getPoint(), 0.0001);

        // Test case 3
        optimizer = new BrentOptimizer(0.5, 0.5);
        optimizer.setMin(0);
        optimizer.setMax(100);
        optimizer.setStartValue(50);
        optimizer.setGoalType(GoalType.MINIMIZE);
        result = optimizer.doOptimize();
        expectedValue = 0.0;
        expectedPoint = 100.0;
        assertEquals(expectedValue, result.getValue(), 0.0001);
        assertEquals(expectedPoint, result.getPoint(), 0.0001);
    }
    
    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(1, 1);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 1);

        UnivariatePointValuePair best = optimizer.best(a, b, true);
        assertEquals(0.0, best.getPoint(), 0.0001);
        assertEquals(0.0, best.getValue(), 0.0001);

        best = optimizer.best(b, a, true);
        assertEquals(0.0, best.getPoint(), 0.0001);
        assertEquals(0.0, best.getValue(), 0.0001);

        best = optimizer.best(a, b, false);
        assertEquals(1.0, best.getPoint(), 0.0001);
        assertEquals(1.0, best.getValue(), 0.0001);

        best = optimizer.best(b, a, false);
        assertEquals(1.0, best.getPoint(), 0.0001);
        assertEquals(1.0, best.getValue(), 0.0001);
    }

}