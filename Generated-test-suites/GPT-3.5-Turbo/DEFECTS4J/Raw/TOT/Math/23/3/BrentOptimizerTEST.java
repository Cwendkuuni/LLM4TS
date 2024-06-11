import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private static final double EPSILON = 1e-6;

    private BrentOptimizer optimizer;

    @Before
    public void setup() {
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                // Custom convergence criteria can be implemented here
                return false;
            }
        };
        optimizer = new BrentOptimizer(1e-8, 1e-8, convergenceChecker);
    }

    @Test
    public void testDoOptimize_minimize() {
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setGoalType(GoalType.MINIMIZE);

        // Test typical use case
        UnivariatePointValuePair result = optimizer.optimize(1, (x) -> Math.pow(x - 5, 2));
        Assert.assertEquals(5, result.getPoint(), EPSILON);

        // Test edge case
        result = optimizer.optimize(0, (x) -> Math.pow(x - 10, 2));
        Assert.assertEquals(10, result.getPoint(), EPSILON);

        // Test error scenario
        try {
            optimizer.optimize(0, (x) -> Math.pow(x - 10, 2));
            Assert.fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testDoOptimize_maximize() {
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setGoalType(GoalType.MAXIMIZE);

        // Test typical use case
        UnivariatePointValuePair result = optimizer.optimize(1, (x) -> -Math.pow(x - 5, 2));
        Assert.assertEquals(5, result.getPoint(), EPSILON);

        // Test edge case
        result = optimizer.optimize(0, (x) -> -Math.pow(x - 10, 2));
        Assert.assertEquals(10, result.getPoint(), EPSILON);

        // Test error scenario
        try {
            optimizer.optimize(0, (x) -> -Math.pow(x - 10, 2));
            Assert.fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testDoOptimize_customConvergence() {
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setGoalType(GoalType.MINIMIZE);

        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return iteration >= 10;
            }
        };

        optimizer.setConvergenceChecker(convergenceChecker);

        // Test convergence
        UnivariatePointValuePair result = optimizer.optimize(1, (x) -> Math.pow(x - 5, 2));
        Assert.assertEquals(5, result.getPoint(), EPSILON);
    }

}