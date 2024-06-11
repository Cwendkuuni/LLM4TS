import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.AbstractUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.UnivariateRealValuedFunction;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer();
        Assert.assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        Assert.assertEquals(100, optimizer.getMaximalIterationCount());
        Assert.assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0);
        Assert.assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0);
    }

    @Test
    public void testDoOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(1);
        optimizer.setMaximalIterationCount(1);
        optimizer.setAbsoluteAccuracy(1E-5);
        optimizer.setRelativeAccuracy(1E-3);

        UnivariateRealFunction function = f -> Math.pow(Math.sin(f), 2);
        double result = optimizer.optimize(function, GoalType.MINIMIZE, -5, 5, 0);

        Assert.assertEquals(Math.PI / 2, result, 1E-3);
    }

    @Test
    public void testOptimizeMinimum() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = f -> Math.pow(f, 3) - 2 * f + 1;

        double result = optimizer.optimize(function, GoalType.MINIMIZE, -10, 10, 1);
        Assert.assertEquals(-1.0, result, 1E-5);
    }

    @Test
    public void testOptimizeMaximum() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = f -> Math.pow(f, 3) + 2 * f + 1;

        double result = optimizer.optimize(function, GoalType.MAXIMIZE, -10, 10, 1);
        Assert.assertEquals(2.0, result, 1E-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAccuracy() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(-1E-5);
        optimizer.optimize(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        }, GoalType.MINIMIZE, 0, 0, 0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeStepSize() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        }, GoalType.MINIMIZE, 0, 0, -1);
    }

    @Test
    public void testMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(1);
        optimizer.setMaximalIterationCount(0);

        UnivariateRealFunction function = f -> Math.pow(Math.sin(f), 2);
        try {
            optimizer.optimize(function, GoalType.MINIMIZE, -5, 5, 0);
            Assert.fail();
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    @Test
    public void testFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = f -> {
            throw new FunctionEvaluationException("Test Exception");
        };

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, -10, 10, 1);
            Assert.fail();
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test
    public void testOutsideBounds() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = f -> Math.log(f);

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, -1, 0, 0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, 0, 1, 0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBounds() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = f -> Math.abs(f - 2);

        SimpleBounds bounds = new SimpleBounds(0, 4);
        double result = optimizer.optimize(function, GoalType.MINIMIZE, bounds.getMin(), bounds.getMax(), 0.1);
        Assert.assertEquals(2.0, result, 1E-5);
    }

    @Test
    public void testFunctionWithDiscontinuity() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = f -> {
            if (f > 0) {
                return Math.pow(f, 0.25);
            } else {
                throw new FunctionEvaluationException("Function has a discontinuity at x = 0");
            }
        };

        double result = optimizer.optimize(function, GoalType.MINIMIZE, -10, 10, 1);
        Assert.assertEquals(0.0, result, 1E-5);
    }
}