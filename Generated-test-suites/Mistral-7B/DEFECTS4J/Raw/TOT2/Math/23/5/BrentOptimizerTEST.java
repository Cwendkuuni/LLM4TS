import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.SimpleBounds;
import org.junit.Test;
import org.junit.Assert;

import static org.apache.commons.math3.optim.univariate.BrentOptimizer.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMinimum() {
        UnivariateFunction f = new MyFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        PointValuePair result = optimizer.optimize(f, new SimpleBounds(0, 1), new InitialGuess(0.5));
        Assert.assertEquals(0.36269, result.getPoint(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMaximum() {
        UnivariateFunction f = new MyFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        PointValuePair result = optimizer.optimize(f.negate(), new SimpleBounds(-1, 1), new InitialGuess(0.5));
        Assert.assertEquals(-0.36269, result.getPoint(), 1e-6);
    }

    @Test
    public void testBrentOptimizerOutsideBounds() {
        UnivariateFunction f = new MyFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        try {
            optimizer.optimize(f, new SimpleBounds(-2, 0), new InitialGuess(0.5));
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizerThrowsExceptionForSmallThresholds() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-9, 1e-9);
        try {
            optimizer.optimize(new MyFunction(), new SimpleBounds(0, 1), new InitialGuess(0.5));
            Assert.fail();
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizerThrowsExceptionForNegativeThresholds() {
        BrentOptimizer optimizer = new BrentOptimizer(-1, 1e-6);
        try {
            optimizer.optimize(new MyFunction(), new SimpleBounds(0, 1), new InitialGuess(0.5));
            Assert.fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizerConvergenceChecker() {
        UnivariateFunction f = new MyFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new MyConvergenceChecker());

        PointValuePair result = optimizer.optimize(f, new SimpleBounds(0, 1), new InitialGuess(0.5));
        Assert.assertEquals(0.36269, result.getPoint(), 1e-6);
    }

    @Test
    public void testBrentOptimizerCustomGoalType() {
        UnivariateFunction f = new MyFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null, GoalType.MAXIMIZE);

        PointValuePair result = optimizer.optimize(f, new SimpleBounds(-1, 1), new InitialGuess(0.5));
        Assert.assertEquals(-0.36269, result.getPoint(), 1e-6);
    }

    private static class MyFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2) - 0.5 * x * x;
        }
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iterationNumber, PointValuePair current, PointValuePair previous) {
            double currentFunctionValue = current.getValue();
            double previousFunctionValue = previous.getValue();

            return Math.abs(currentFunctionValue - previousFunctionValue) < 1e-8;
        }
    }
}