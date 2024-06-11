import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    private static final double EPSILON = 1e-10;

    @Test
    public void testOptimizeWithInvalidEpsilon() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;
        double epsilon = -1.0; // invalid epsilon

        BrentOptimizer optimizer = new BrentOptimizer();

        try {
            optimizer.optimize(f, goalType, min, max, startValue, epsilon);
            Assert.fail("Should throw NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        } catch (MaxIterationsExceededException e) {
            // not expected
            Assert.fail("Unexpected exception");
        }
    }

    @Test
    public void testOptimizeWithInvalidTolerance() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        GoalType goalType = GoalType.MAXIMIZE; // maximize function x^2 = 100
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;
        double epsilon = 1e-4;
        double tolerance = -1; // invalid tolerance

        BrentOptimizer optimizer = new BrentOptimizer();

        try {
            optimizer.optimize(f, goalType, min, max, startValue, epsilon, tolerance);
            Assert.fail("Should throw NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        } catch (MaxIterationsExceededException e) {
            // not expected
            Assert.fail("Unexpected exception");
        }
    }

    @Test
    public void testOptimizeWithInvalidInterval() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 10.0; // invalid interval
        double max = -10.0; // invalid interval
        double startValue = 0.0;
        double epsilon = 1e-4;
        double tolerance = 1e-10;

        BrentOptimizer optimizer = new BrentOptimizer();

        try {
            optimizer.optimize(f, goalType, min, max, startValue, epsilon, tolerance);
            Assert.fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        } catch (MaxIterationsExceededException e) {
            // not expected
            Assert.fail("Unexpected exception");
        }
    }

    @Test
    public void testOptimizeWithValidInputs() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return (x - 1) * (x - 2) * (x - 3); // roots at x=1, x=2, x=3
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 4.0;
        double startValue = 1.5;
        double epsilon = 1e-4;
        double tolerance = 1e-10;

        BrentOptimizer optimizer = new BrentOptimizer();

        double result;
        try {
            result = optimizer.optimize(f, goalType, min, max, startValue, epsilon, tolerance);
            Assert.assertEquals(1.0, result, EPSILON);
        } catch (MaxIterationsExceededException e) {
            // not expected
            Assert.fail("Unexpected exception");
        }
    }

}