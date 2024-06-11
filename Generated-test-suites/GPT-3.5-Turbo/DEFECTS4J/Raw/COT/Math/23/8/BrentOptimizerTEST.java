
import org.apache.commons.math3.optimization.univariate.*;
import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnit4.class)
public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Create BrentOptimizer object
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);

        // Set up test inputs
        double lo = 0.0;
        double hi = 10.0;
        GoalType goal = GoalType.MINIMIZE;
        
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x * x; // Quadratic function
            }
        };
        
        UnivariateOptimizer.Optimum optimum = optimizer.optimize(new MaxEval(100),
                                                                new UnivariateObjectiveFunction(function),
                                                                goal,
                                                                new SearchInterval(lo, hi));

        // Perform assertions
        Assert.assertEquals(0.0, optimum.getPoint(), 0.0001);
        Assert.assertEquals(0.0, optimum.getValue(), 0.0001);
    }

    @Test
    public void testConstructorWithRelAndAbs() {
        // Create BrentOptimizer object with custom relative and absolute thresholds
        BrentOptimizer optimizer = new BrentOptimizer(0.5, 0.1);

        // Perform assertions
        Assert.assertEquals(0.5, optimizer.getRelativeThreshold(), 0.0001);
        Assert.assertEquals(0.1, optimizer.getAbsoluteThreshold(), 0.0001);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithRelTooSmall() {
        // Attempt to create BrentOptimizer object with relative threshold smaller than the minimum
        BrentOptimizer optimizer = new BrentOptimizer(1e-20, 0.1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithAbsNegative() {
        // Attempt to create BrentOptimizer object with negative absolute threshold
        BrentOptimizer optimizer = new BrentOptimizer(0.5, -0.1);
    }

    @Test
    public void testBest() {
        // Create BrentOptimizer object
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);

        // Create two UnivariatePointValuePair objects with different values
        UnivariatePointValuePair a = new UnivariatePointValuePair(0.0, 0.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 1.0);

        // Check the best point
        UnivariatePointValuePair best = optimizer.best(a, b, true);
        Assert.assertSame(b, best);
    }
}
