import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }

    @Test
    public void testOptimize() {
        // Test case 1
        double expectedOptimum = 0.0;
        double expectedValue = 0.0;
        double actualOptimum = optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        }, GoalType.MINIMIZE, -10, 10);
        double actualValue = optimizer.computeObjectiveValue(new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        }, actualOptimum);
        Assert.assertEquals(expectedOptimum, actualOptimum, 1E-10);
        Assert.assertEquals(expectedValue, actualValue, 1E-10);

        // Test case 2
        expectedOptimum = 0.0;
        expectedValue = 0.0;
        actualOptimum = optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        }, GoalType.MINIMIZE, -10, 10);
        actualValue = optimizer.computeObjectiveValue(new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        }, actualOptimum);
        Assert.assertEquals(expectedOptimum, actualOptimum, 1E-10);
        Assert.assertEquals(expectedValue, actualValue, 1E-10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinInvalidInput() {
        optimizer.localMin(true, new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        }, GoalType.MINIMIZE, -10, 0, 10, -1, 0);
    }

    @Test
    public void testLocalMin() {
        double expectedMinimum = 0.0;
        double actualMinimum = optimizer.localMin(true, new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        }, GoalType.MINIMIZE, -10, 0, 10, 1E-10, 1E-10);
        Assert.assertEquals(expectedMinimum, actualMinimum, 1E-10);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testLocalMinMaxIterationsExceeded() {
        optimizer.localMin(true, new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        }, GoalType.MINIMIZE, -10, 0, 10, 1E-10, 1E-10);
    }

    @Test
    public void testLocalMinMaxIterationsNotExceeded() {
        double expectedMinimum = 0.0;
        double actualMinimum = optimizer.localMin(true, new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        }, GoalType.MINIMIZE, -10, 0, 10, 1E-10, 1E-10);
        Assert.assertEquals(expectedMinimum, actualMinimum, 1E-10);
    }
}