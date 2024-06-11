import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setup() {
        optimizer = new BrentOptimizer();
    }

    @Test
    public void testOptimizeWithStartValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3; // (x-1)(x-3)
            }
        };

        double result = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 4.0, 2.0);

        Assert.assertEquals(1.0, result, 0.01);
    }

    @Test
    public void testOptimizeWithoutStartValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3; // (x-1)(x-3)
            }
        };

        double result = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 4.0);

        Assert.assertEquals(1.0, result, 0.01);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3; // (x-1)(x-3)
            }
        };

        optimizer.setMaximalIterationCount(5);
        optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 4.0);
    }
}