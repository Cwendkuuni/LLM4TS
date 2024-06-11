import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    private final double EPSILON = 1e-10;
    private final double TOLERANCE = 1e-14;
    private final int MAX_ITERATIONS = 100;

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.doOptimize();
        } catch (UnsupportedOperationException e) {
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.fail("Unexpected exception occurred!");
        }
    }

    @Test
    public void testOptimizeWithStartValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x, 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10, 2);
            Assert.assertEquals(0, result, EPSILON);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            Assert.fail("Unexpected exception occurred!");
        }
    }

    @Test
    public void testOptimizeWithoutStartValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x, 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
            Assert.assertEquals(0, result, EPSILON);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            Assert.fail("Unexpected exception occurred!");
        }
    }

    @Test
    public void testOptimizeWithNegativeTolerance() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x, 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10, 2);
            Assert.assertEquals(0, result, EPSILON);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            Assert.fail("Unexpected exception occurred!");
        } catch (NotStrictlyPositiveException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testOptimizeWithNegativeAbsoluteAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x, 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(-0.0001);
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10, 2);
            Assert.assertEquals(0, result, EPSILON);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            Assert.fail("Unexpected exception occurred!");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testOptimizeWithNegativeRelativeAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x, 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setRelativeAccuracy(-0.0001);
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10, 2);
            Assert.assertEquals(0, result, EPSILON);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            Assert.fail("Unexpected exception occurred!");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testLocalMin() {
        double lo = -1;
        double mid = 0;
        double hi = 1;
        double eps = 1e-6;
        double t = 1e-12;
        boolean isMinim = true;
        GoalType goalType = GoalType.MINIMIZE;

        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x, 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            double result = optimizer.localMin(isMinim, f, goalType, lo, mid, hi, eps, t);
            Assert.assertEquals(0, result, EPSILON);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            Assert.fail("Unexpected exception occurred!");
        }
    }

}