import org.junit.Test;
import org.junit.Assert;

public class BrentOptimizerTest {

    @Test
    public void testOptimize() {
        // Create an instance of BrentOptimizer
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);

        // Set the goal type and the interval
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(-10);
        optimizer.setMax(10);

        // Set the convergence checker
        optimizer.setConvergenceChecker(new ConvergenceChecker<UnivariatePointValuePair>() {
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                if (iteration >= 100) {
                    return true;
                }
                double p = previous.getValue();
                double c = current.getValue();
                return Math.abs(p - c) < 0.001;
            }
        });

        // Call the optimize method and get the result
        UnivariatePointValuePair result = optimizer.doOptimize();

        // Check the result
        Assert.assertEquals(0.0, result.getPoint(), 0.001);
        Assert.assertEquals(0.0, result.getValue(), 0.001);
    }

    @Test
    public void testBest() {
        // Create two UnivariatePointValuePair instances
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 2.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 1.0);

        // Create an instance of BrentOptimizer
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);

        // Test the best method
        UnivariatePointValuePair result1 = optimizer.best(a, b, true);
        Assert.assertEquals(b, result1);

        UnivariatePointValuePair result2 = optimizer.best(a, b, false);
        Assert.assertEquals(a, result2);
    }
}