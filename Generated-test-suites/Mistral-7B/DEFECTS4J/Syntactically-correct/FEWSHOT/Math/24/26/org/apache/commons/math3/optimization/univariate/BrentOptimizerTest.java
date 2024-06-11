package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.UnivariatePointValuePair;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        assertEquals(BrentOptimizer.class, optimizer.getClass());
        assertEquals(1e-5, optimizer.relativeThreshold, 0);
        assertEquals(1e-5, optimizer.absoluteThreshold, 0);
        assertNull(optimizer.getConvergenceChecker());

        BrentOptimizer optimizer1 = new BrentOptimizer(1e-5, 1e-5, null);
        assertEquals(1e-5, optimizer1.relativeThreshold, 0);
        assertEquals(1e-5, optimizer1.absoluteThreshold, 0);
        assertNull(optimizer1.getConvergenceChecker());

        BrentOptimizer optimizer2 = new BrentOptimizer(1e-5, 1e-6);
        assertEquals(1e-5, optimizer2.relativeThreshold, 0);
        assertEquals(1e-6, optimizer2.absoluteThreshold, 0);
        assertNull(optimizer2.getConvergenceChecker());

        BrentOptimizer optimizer3 = new BrentOptimizer(1e-6, 1e-5);
        assertEquals(1e-6, optimizer3.relativeThreshold, 0);
        assertEquals(1e-5, optimizer3.absoluteThreshold, 0);
        assertNull(optimizer3.getConvergenceChecker());

        BrentOptimizer optimizer4 = new BrentOptimizer(-1e-5, 1e-5);
        assertThrows(NotStrictlyPositiveException.class, () -> optimizer4);

        BrentOptimizer optimizer5 = new BrentOptimizer(1e-5, -1e-5);
        assertThrows(NotStrictlyPositiveException.class, () -> optimizer5);

        BrentOptimizer optimizer6 = new BrentOptimizer(1e-5, 1e-5, new MyConvergenceChecker());
        assertEquals(1e-5, optimizer6.relativeThreshold, 0);
        assertEquals(1e-5, optimizer6.absoluteThreshold, 0);
        assertNotNull(optimizer6.getConvergenceChecker());
    }

    @Test
    public void testDoOptimize() {
        FunctionTest functionTest = new FunctionTest();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);

        UnivariatePointValuePair result = optimizer.optimize(functionTest, 0, 10);
        assertNotNull(result);
        assertEquals(0.5, result.getPoint(), 0.00001);
        assertEquals(-0.25, result.getValue(), 0.00001);

        result = optimizer.optimize(functionTest, 1, 2);
        assertNotNull(result);
        assertEquals(1, result.getPoint(), 0.00001);
        assertEquals(0, result.getValue(), 0.00001);

        result = optimizer.optimize(functionTest, 2, 3);
        assertEquals(Double.NaN, result.getPoint(), 0.0);
        assertEquals(Double.NaN, result.getValue(), 0.0);
    }

    @Test
    public void testConvergenceChecker() {
        FunctionTest functionTest = new FunctionTest();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new MyConvergenceChecker());

        UnivariatePointValuePair result = optimizer.optimize(functionTest, 0, 10);
        assertNotNull(result);
        assertEquals(0.5, result.getPoint(), 0.00001);
        assertEquals(-0.25, result.getValue(), 0.00001);

        MyConvergenceChecker convergenceChecker = (iter, previous, current) -> true;
        optimizer = new BrentOptimizer(1e-6, 1e-6, convergenceChecker);

        result = optimizer.optimize(functionTest, 0, 10);
        assertNotNull(result);
        assertEquals(0.5, result.getPoint(), 0.00001);
        assertEquals(-0.25, result.getValue(), 0.00001);

        convergenceChecker = (iter, previous, current) -> false;
        optimizer = new BrentOptimizer(1e-6, 1e-6, convergenceChecker);

        assertThrows(IllegalStateException.class, () -> optimizer.optimize(functionTest, 0, 10));
    }

    @Test
    public void testMinimize() {
        FunctionTest functionTest = new FunctionTest();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);

        assertEquals(0.5, optimizer.minimize(functionTest, 0, 10).getPoint(), 0.00001);
        assertEquals(-0.25, optimizer.minimize(functionTest, 0, 10).getValue(), 0.00001);

        functionTest = new FunctionTestWithMinimum(1);
        assertEquals(1, optimizer.minimize(functionTest, 0, 10).getPoint(), 0.00001);
        assertEquals(0, optimizer.minimize(functionTest, 0, 10).getValue(), 0.00001);
    }

    @Test
    public void testMaximize() {
        FunctionTest functionTest = new FunctionMaximizer(1);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);

        assertEquals(1, optimizer.maximize(functionTest, 0, 10).getPoint(), 0.00001);
        assertEquals(1, optimizer.maximize(functionTest, 0, 10).getValue(), 0.00001);

        functionTest = new FunctionTestWithMaximum(1);
        assertEquals(1, optimizer.maximize(functionTest, 0, 10).getPoint(), 0.00001);
        assertEquals(1, optimizer.maximize(functionTest, 0, 10).getValue(), 0.00001);
    }

    private static class FunctionTest implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 2 * x + 1;
        }
    }

    private static class FunctionTestWithMinimum implements UnivariateFunction {
        private final double minimum;

        public FunctionTestWithMinimum(double minimum) {
            this.minimum = minimum;
        }

        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 2 * x + minimum;
        }
    }

    private static class FunctionTestWithMaximum implements UnivariateFunction {
        private final double maximum;

        public FunctionTestWithMaximum(double maximum) {
            this.maximum = maximum;
        }

        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 2 * x + maximum;
        }
    }

    private static class FunctionMaximizer implements UnivariateFunction {
        private final double maximum;

        public FunctionMaximizer(double maximum) {
            this.maximum = maximum;
        }

        @Override
        public double value(double x) {
            return -(Math.pow(x, 2) - 2 * x + maximum);
        }
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return true;
        }
    }
}