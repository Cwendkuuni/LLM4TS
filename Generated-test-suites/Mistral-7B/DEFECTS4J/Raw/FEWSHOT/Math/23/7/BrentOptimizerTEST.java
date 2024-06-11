package org.apache.commons.cli;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new MockUnivariateFunction();
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);
        assertEquals(0.5, result.getX(), 1e-6);
        assertEquals(-0.25, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMinimumRelativeThresholdSmallerThanMacheps() {
        try {
            new BrentOptimizer(0.5 * FastMath.ulp(1d), 1e-6, null);
            fail("NumberIsTooSmallException expected");
        } catch (NumberIsTooSmallException e) {
            // expected
        }
    }

    @Test
    public void testBrentOptimizerAbsoluteThresholdZero() {
        try {
            new BrentOptimizer(1e-6, 0, null);
            fail("NotStrictlyPositiveException expected");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testBrentOptimizerConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new MockConvergenceChecker());
        UnivariateFunction function = new MockUnivariateFunction();
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);
        assertEquals(0.5, result.getX(), 1e-6);
        assertEquals(-0.25, result.getValue(), 1e-6);
        assertTrue(optimizer.getConvergenceChecker().isConverged(0, null, result));
    }

    @Test
    public void testBrentOptimizerGoalTypeMinimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new MockUnivariateFunctionMinimize();
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 2.0);
        assertEquals(1.0, result.getX(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerGoalTypeMaximize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new MockUnivariateFunctionMaximize();
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 2.0);
        assertEquals(2.0, result.getX(), 1e-6);
        assertEquals(5.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerInterpolation() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new MockUnivariateFunctionInterpolation();
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);
        assertEquals(0.25, result.getX(), 1e-6);
        assertEquals(0.75, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerGoldenSection() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new MockUnivariateFunctionGoldenSection();
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);
        assertEquals(0.5, result.getX(), 1e-6);
        assertEquals(-0.25, result.getValue(), 1e-6);
    }
}

class MockUnivariateFunction implements UnivariateFunction {
    @Override
    public double value(double x) {
        return Math.pow(x, 2) - 2 * x;
    }
}

class MockUnivariateFunctionMinimize implements UnivariateFunction {
    @Override
    public double value(double x) {
        return Math.pow(x, 2) + 2 * x;
    }
}

class MockUnivariateFunctionMaximize implements UnivariateFunction {
    @Override
    public double value(double x) {
        return Math.pow(x, 2) - 2 * x;
    }
}

class MockUnivariateFunctionInterpolation implements UnivariateFunction {
    @Override
    public double value(double x) {
        return Math.pow(x, 2) - 0.5 * x;
    }
}

class MockUnivariateFunctionGoldenSection implements UnivariateFunction {
    @Override
    public double value(double x) {
        if (x < 0.3) {
            return Math.pow(x, 2) + 0.2 * x;
        } else {
            return Math.pow(x, 2) - 0.2 * x;
        }
    }
}

class MockConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
    @Override
    public boolean isConverged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
        return previous == null && current.getValue() <= 0;
    }
}