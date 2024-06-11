import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithConvergenceChecker() {
        // Test constructor with convergence checker
        // Given constructor parameters
        double rel = 0.01;
        double abs = 0.001;
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<>();

        // Create BrentOptimizer instance
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);

        // Check if the optimizer instance is not null
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithoutConvergenceChecker() {
        // Test constructor without convergence checker
        // Given constructor parameters
        double rel = 0.01;
        double abs = 0.001;

        // Create BrentOptimizer instance
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        // Check if the optimizer instance is not null
        assertNotNull(optimizer);
    }

    @Test
    public void testOptimizeMethod() {
        // Test optimize method
        // Given constructor parameters
        double rel = 0.01;
        double abs = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        // Define the start value, goal type, min, and max
        double startValue = 2.0;
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        optimizer.setGoalType(goalType);
        optimizer.setMin(min);
        optimizer.setMax(max);
        optimizer.setStartValue(startValue);

        // Call the optimize method
        UnivariatePointValuePair result = optimizer.optimize();

        // Check if the result is not null
        assertNotNull(result);
    }

    @Test
    public void testBestMethod() {
        // Test best method
        // Given two points with values
        UnivariatePointValuePair pointA = new UnivariatePointValuePair(1.0, 5.0);
        UnivariatePointValuePair pointB = new UnivariatePointValuePair(2.0, 3.0);
        boolean isMinim = true;

        // Call the best method
        UnivariatePointValuePair bestPoint = optimizer.best(pointA, pointB, isMinim);

        // Check if the best point is the expected one
        assertEquals(bestPoint, pointB);
    }
}