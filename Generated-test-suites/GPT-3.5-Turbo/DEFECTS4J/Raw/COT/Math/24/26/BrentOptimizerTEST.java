import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        double rel = 0.01;
        double abs = 0.01;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);

        // Test case 1
        double min = -10;
        double max = 10;
        double startValue = 0;
        GoalType goalType = GoalType.MINIMIZE;

        double expected = optimizer.optimize(Integer.MAX_VALUE, goalType, min, max, startValue).getValue(); 
        double actual = optimizer.doOptimize().getValue();
        assertEquals(expected, actual, 0);

        // Test case 2
        min = -5;
        max = 5;
        startValue = -2.5;
        goalType = GoalType.MINIMIZE;

        expected = optimizer.optimize(Integer.MAX_VALUE, goalType, min, max, startValue).getValue(); 
        actual = optimizer.doOptimize().getValue();
        assertEquals(expected, actual, 0);

        // Test case 3
        min = -1;
        max = 1;
        startValue = 0;
        goalType = GoalType.MAXIMIZE;

        expected = optimizer.optimize(Integer.MAX_VALUE, goalType, min, max, startValue).getValue(); 
        actual = optimizer.doOptimize().getValue();
        assertEquals(expected, actual, 0);

        // Test case 4
        min = 0;
        max = 5;
        startValue = 2.5;
        goalType = GoalType.MAXIMIZE;

        expected = optimizer.optimize(Integer.MAX_VALUE, goalType, min, max, startValue).getValue(); 
        actual = optimizer.doOptimize().getValue();
        assertEquals(expected, actual, 0);
    }
}