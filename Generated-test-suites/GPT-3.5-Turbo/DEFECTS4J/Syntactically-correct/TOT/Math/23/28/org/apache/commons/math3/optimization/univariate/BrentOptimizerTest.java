import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize_minimize() {
        // Create test instance of BrentOptimizer
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8);

        // Set up input values
        double min = 0;
        double max = 10;
        double startValue = 4;

        // Set up Expected results
        double expectedMinValue = 0;

        // Perform the optimization
        UnivariatePointValuePair result = optimizer.optimize(100,
            new ObjectiveFunction(new BrentOptimizerTestFunction()),
            GoalType.MINIMIZE,
            new SearchInterval(min, max, startValue),
            new MaxEvaluations(1000),
            new AbsoluteThreshold(1e-8),
            new RelativeThreshold(1e-6)
        );

        // Check the solution
        Assert.assertEquals(expectedMinValue, result.getValue(), 1e-8);
    }

    @Test
    public void testDoOptimize_maximize() {
        // Create test instance of BrentOptimizer
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8);

        // Set up input values
        double min = 0;
        double max = 10;
        double startValue = 4;

        // Set up Expected results
        double expectedMaxValue = 100;

        // Perform the optimization
        UnivariatePointValuePair result = optimizer.optimize(100,
            new ObjectiveFunction(new BrentOptimizerTestFunction()),
            GoalType.MAXIMIZE,
            new SearchInterval(min, max, startValue),
            new MaxEvaluations(1000),
            new AbsoluteThreshold(1e-8),
            new RelativeThreshold(1e-6)
        );

        // Check the solution
        Assert.assertEquals(expectedMaxValue, result.getValue(), 1e-8);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_RelativeThresholdNonPositive() {
        BrentOptimizer optimizer = new BrentOptimizer(-1e-6, 1e-8);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_AbsoluteThresholdNonPositive() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, -1e-8);
    }

    @Test
    public void testConstructor_WithoutConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8);
    }

}

class BrentOptimizerTestFunction implements UnivariateFunction {

    public double value(double x) {
        return x * x;
    }
}