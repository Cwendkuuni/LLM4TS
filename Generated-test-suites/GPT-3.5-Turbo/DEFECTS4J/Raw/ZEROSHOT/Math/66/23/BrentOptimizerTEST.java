import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;

public class BrentOptimizerTest {

    @Test
    public void testOptimize_single_startValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        double startValue = 5.0;
        try {
            double result = optimizer.optimize(f, goalType, min, max, startValue);
            Assert.assertEquals(0.0, result, 1e-10);
        } catch (MaxIterationsExceededException e) {
            Assert.fail("MaxIterationsExceededException");
        } catch (FunctionEvaluationException e) {
            Assert.fail("FunctionEvaluationException");
        }
    }

    @Test
    public void testOptimize_single_noStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        try {
            double result = optimizer.optimize(f, goalType, min, max);
            Assert.assertEquals(0.0, result, 1e-10);
        } catch (MaxIterationsExceededException e) {
            Assert.fail("MaxIterationsExceededException");
        } catch (FunctionEvaluationException e) {
            Assert.fail("FunctionEvaluationException");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimize_single_invalidEps() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        double startValue = 5.0;
        double eps = 0.0;
        double t = 1e-10;
        try {
            optimizer.localMin(true, f, goalType, min, startValue, max, eps, t);
        } catch (MaxIterationsExceededException e) {
            Assert.fail("MaxIterationsExceededException");
        } catch (FunctionEvaluationException e) {
            Assert.fail("FunctionEvaluationException");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimize_single_invalidT() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        double startValue = 5.0;
        double eps = 1e-10;
        double t = 0.0;
        try {
            optimizer.localMin(true, f, goalType, min, startValue, max, eps, t);
        } catch (MaxIterationsExceededException e) {
            Assert.fail("MaxIterationsExceededException");
        } catch (FunctionEvaluationException e) {
            Assert.fail("FunctionEvaluationException");
        }
    }

}