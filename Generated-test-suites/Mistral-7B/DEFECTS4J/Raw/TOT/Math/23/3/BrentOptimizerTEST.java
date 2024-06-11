package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optim.initializer.ValueInitializer;
import org.apache.commons.math3.test.AbstractTestCase;
import org.junit.Test;

public class BrentOptimizerTest extends AbstractTestCase {

    /**
     * Test optimization of simple function with known minimum.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new AbsFunction(1);
        UnivariatePointValuePair result = optimizer.optimize(function, 2.0, null);
        assertEquals("Optimization failed.", new UnivariatePointValuePair(1.0, 0.0), result);
    }

    /**
     * Test optimization of simple function with known maximum.
     */
    @Test
    public void testOptimizeSimpleFunctionMax() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new AbsFunction(-1);
        UnivariatePointValuePair result = optimizer.optimize(function, 2.0, null);
        assertEquals("Optimization failed.", new UnivariatePointValuePair(3.0, 3.0), result);
    }

    /**
     * Test optimization of function with infinite minimum.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testOptimizeFunctionWithInfMin() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-9, 1e-6);
        UnivariateFunction function = new ExpFunction(-1);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, null);
        fail("Optimization should not have returned a result.");
    }

    /**
     * Test optimization of function with infinite maximum.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testOptimizeFunctionWithInfMax() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-9, 1e-6);
        UnivariateFunction function = new ExpFunction(1);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, null);
        fail("Optimization should not have returned a result.");
    }

    /**
     * Test optimization of function with a non-strictly positive threshold.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeFunctionWithNonPositiveThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0, 1e-6);
    }

    /**
     * Test optimization with user-defined convergence checking procedure.
     */
    @Test
    public void testOptimizeWithCustomChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new MyConvergenceChecker());
        UnivariateFunction function = new SinFunction();
        UnivariatePointValuePair result = optimizer.optimize(function, 1.0, null);
        assertEquals("Optimization failed.", new UnivariatePointValuePair(Math.PI / 2.0, 0.0), result);
    }

    /**
     * Test optimization of function with initial guess outside the function's
     * domain.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeOutsideDomain() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new LogFunction(2.0);
        optimizer.optimize(function, 0.0, null);
    }

    /**
     * Test optimization of function with a large interval.
     */
    @Test
    public void testOptimizeLargeInterval() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new LinearFunction(-10.0, 10.0);
        UnivariatePointValuePair result = optimizer.optimize(function, -15.0, null);
        assertEquals("Optimization failed.", new UnivariatePointValuePair(-2.0, 4.0 * (-2.0)), result);
    }

    /**
     * Test optimization of function with multiple local minima.
     */
    @Test
    public void testOptimizeFunctionWithMultipleLocalMinima() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new SinFunctionWithTwoMinima();
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, null);
        assertEquals("Optimization failed.", new UnivariatePointValuePair(-1.0, 1.0), result);
    }

    /**
     * Test optimization of function with a very flat minimum.
     */
    @Test
    public void testOptimizeFlatMinimum() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-12, 1e-12);
        UnivariateFunction function = new FlatMinimumFunction();
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, null);
        assertEquals("Optimization failed.", new UnivariatePointValuePair(1.0, 1.0e-12), result);
    }

    /**
     * Test optimization of function with initial guess equal to minimum.
     */
    @Test
    public void testOptimizeInitialGuessIsMinimum() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new AbsFunction(0.5);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.5, null);
        assertEquals("Optimization failed.", new UnivariatePointValuePair(0.5, 0.0), result);
    }

    /**
     * Test optimization of function with initial guess equal to maximum.
     */
    @Test
    public void testOptimizeInitialGuessIsMaximum() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new AbsFunction(5.0);
        UnivariatePointValuePair result = optimizer.optimize(function, 5.0, null);
        assertEquals("Optimization failed.", new UnivariatePointValuePair(5.0, 0.0), result);
    }

    /**
     * Test optimization of function with a custom initializer.
     */
    @Test
    public void testOptimizeWithCustomInitializer() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new SinFunction();
        ValueInitializer<UnivariateOptimizer> initializer = new ValueInitializer<UnivariateOptimizer>() {
            @Override
            public double getInitialValue(UnivariateOptimizer optimizer) {
                return 1.5;
            }
        };
        UnivariatePointValuePair result = optimizer.optimize(function, 1.0, initializer);
        assertEquals("Optimization failed.", new UnivariatePointValuePair(Math.PI / 4.0, Math.sqrt(2.0) / 2.0), result);
    }

    /**
     * Convergence checking procedure that always considers a single iteration
     * sufficient, regardless of the current point and the previous one.
     */
    private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return true;
        }
    }

}

class AbsFunction implements UnivariateFunction {

    private final double a;

    public AbsFunction(final double a) {
        this.a = a;
    }

    public double value(double x) {
        return Math.abs(a - x);
    }

}

class ExpFunction implements UnivariateFunction {

    private final double a;

    public ExpFunction(final double a) {
        this.a = a;
    }

    public double value(double x) {
        return Math.exp(a * x);
    }

}

class LinearFunction implements UnivariateFunction {

    private final double a;
    private final double b;

    public LinearFunction(final double a, final double b) {
        this.a = a;
        this.b = b;
    }

    public double value(double x) {
        return a * x + b;
    }

}

class LogFunction implements UnivariateFunction {

    private final double a;

    public LogFunction(final double a) {
        this.a = a;
    }

    public double value(double x) {
        return Math.log(a / x);
    }

}

class SinFunction implements UnivariateFunction {

    public double value(double x) {
        return Math.sin(x);
    }

}

class SinFunctionWithTwoMinima implements UnivariateFunction {

    public double value(double x) {
        return Math.sin(x) * Math.sin(x) * (x > 0 ? -1.0 : 1.0);
    }

}

class FlatMinimumFunction implements UnivariateFunction {

    public double value(double x) {
        return 1.0 / (1.0 + x * x);
    }

}