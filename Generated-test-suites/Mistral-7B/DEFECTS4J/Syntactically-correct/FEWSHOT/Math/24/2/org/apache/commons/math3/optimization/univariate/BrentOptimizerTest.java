package org.apache.commons.cli;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertNull(optimizer.getConvergenceChecker());
        Assert.assertNotNull(optimizer.doOptimize());
    }

    @Test
    public void testBrentOptimizerWithConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return current.getValue() == 0.0;
            }
        });
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertNull(optimizer.getConvergenceChecker());
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(0.0, result.getValue(), 1e-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithNegativeAbsoluteThreshold() {
        new BrentOptimizer(-1e-5, 1e-5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithSmallRelativeThreshold() {
        new BrentOptimizer(1e-8, 1e-5);
    }

    @Test
    public void testBrentOptimizerMinimization() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);

        // Function f(x) = x^3 - 2x^2 + x - 1
        FunctionTestable function = new FunctionTestable() {
            @Override
            public double value(double x) {
                return x * x * x - 2 * x * x + x - 1;
            }

            @Override
            public double derivative(double x) {
                return 3 * x * x - 4 * x + 1;
            }
        };

        optimizer.setFunction(function);
        optimizer.setMin(1.0);
        optimizer.setMax(3.0);
        optimizer.setStartValue(2.0);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertEquals(1.0, result.getValue(), 1e-5);
        assertEquals(1.0, result.getX(), 1e-5);
    }

    @Test
    public void testBrentOptimizerMaximization() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);

        // Function f(x) = x^3 + 2x^2 + x + 1
        FunctionTestable function = new FunctionTestable() {
            @Override
            public double value(double x) {
                return x * x * x + 2 * x * x + x + 1;
            }

            @Override
            public double derivative(double x) {
                return 3 * x * x + 4 * x + 1;
            }
        };

        optimizer.setFunction(function);
        optimizer.setMin(1.0);
        optimizer.setMax(3.0);
        optimizer.setStartValue(2.0);
        optimizer.setGoalType(GoalType.MAXIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertEquals(6.0, result.getValue(), 1e-5);
        assertEquals(3.0, result.getX(), 1e-5);
    }

    @Test
    public void testBrentOptimizerWithCustomConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                double error = Math.abs(current.getValue() - previous.getValue());
                return error < 1e-5 && iter > 10;
            }
        });

        // Function f(x) = x^2
        FunctionTestable function = new FunctionTestable() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        optimizer.setFunction(function);
        optimizer.setMin(-5.0);
        optimizer.setMax(5.0);
        optimizer.setStartValue(3.0);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertEquals(9.0, result.getValue(), 1e-5);
        assertEquals(3.0, result.getX(), 1e-5);
    }
}

class FunctionTestable {
    FunctionTestable() {
    }

    double value(double x) {
        throw new UnsupportedOperationException();
    }

    double derivative(double x) {
        throw new UnsupportedOperationException();
    }

    void setFunction(FunctionTestable function) {
        this.function = function;
    }
}