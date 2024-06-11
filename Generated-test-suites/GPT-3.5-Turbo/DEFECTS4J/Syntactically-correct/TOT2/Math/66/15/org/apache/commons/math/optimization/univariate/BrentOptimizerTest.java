import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;

public class BrentOptimizerTest {

    @Test
    public void testOptimizeWithStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, 1.0, 3.0, 1.5);
            Assert.assertEquals(2.0, result, 1e-10);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            Assert.fail("Exception thrown");
        }
    }

    @Test
    public void testOptimizeWithoutStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, 1.0, 3.0);
            Assert.assertEquals(2.0, result, 1e-10);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            Assert.fail("Exception thrown");
        }
    }

    @Test
    public void testLocalMin() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        try {
            double result = optimizer.localMin(true, f, GoalType.MINIMIZE, 1.0, 2.0, 3.0, 1e-14, 1e-10);
            Assert.assertEquals(2.0, result, 1e-10);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            Assert.fail("Exception thrown");
        }
    }

    @Test
    public void testOutOfBoundsLoHi() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, 3.0, 1.0);
            Assert.fail("Expected NotStrictlyPositiveException was not thrown");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            Assert.fail("Wrong exception was thrown");
        }
    }

    @Test
    public void testInvalidTolerance() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        try {
            double result = optimizer.localMin(true, f, GoalType.MINIMIZE, 1.0, 2.0, 3.0, 0, 1e-10);
            Assert.fail("Expected NotStrictlyPositiveException was not thrown");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            Assert.fail("Wrong exception was thrown");
        }
    }

}