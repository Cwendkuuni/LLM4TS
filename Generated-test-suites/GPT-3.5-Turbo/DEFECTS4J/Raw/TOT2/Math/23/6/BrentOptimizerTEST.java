import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Create BrentOptimizer instance
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-12);

        // Set optimization parameters
        optimizer.setMin(0.0);
        optimizer.setMax(10.0);
        optimizer.setGoalType(GoalType.MINIMIZE);

        // Perform optimization
        UnivariatePointValuePair result = optimizer.optimize(100, (double x) -> x * x);

        // Check the result
        Assert.assertEquals("Optimal value should be 0.0", 0.0, result.getValue(), 1e-10);
        Assert.assertEquals("Optimal point should be 0.0", 0.0, result.getPoint(), 1e-10);
    }

    // Add more test cases for other public methods if needed

}