package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.intervals.Interval;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BrentOptimizerTest {

    private double relTol;
    private double absTol;
    private UnivariateFunction function;
    private double expectedMin;
    private Interval interval;

    public BrentOptimizerTest(double relTol, double absTol, UnivariateFunction function, double expectedMin, Interval interval) {
        this.relTol = relTol;
        this.absTol = absTol;
        this.function = function;
        this.expectedMin = expectedMin;
        this.interval = interval;
    }

    @Parameterized.Parameters(name = "relTol={0}, absTol={1}, function={2}, interval={3}, expectedMin={4}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {1e-6, 1e-6, new SimpleFunction(), new Interval(0, 1), 0.5},
                {1e-4, 1e-8, new SimpleFunctionWithInflectionPoint(), new Interval(0, 1), 0.25},
                {1e-3, 1e-3, new SimpleFunctionWithRoots(), new Interval(-1, 1), -0.5},
                {1e-2, 1e-2, new SimpleFunctionWithRootsAndInflectionPoint(), new Interval(-1, 1), -0.75},
                {1e-5, 1e-5, new SimpleFunctionWithSingularity(), new Interval(-1, 1), 0},
                {1e-4, 1e-7, new SimpleFunctionWithSingularity(), new Interval(-1, 1), 0}
        });
    }

    @Test
    public void testBrentOptimizer() throws NotStrictlyPositiveException, NumberIsTooSmallException {
        BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, null);
        UnivariatePointValuePair result = optimizer.optimize(function, interval, null);
        double actualMin = result.getValue();
        assertEquals("Expected minimum not found", expectedMin, actualMin, 1e-8);
    }

    private static class SimpleFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2);
        }
    }

    private static class SimpleFunctionWithInflectionPoint implements UnivariateFunction {
        @Override
        public double value(double x) {
            if (x < 0.25) {
                return Math.pow(x, 2) - 0.25;
            } else {
                return Math.pow(x - 0.25, 2);
            }
        }
    }

    private static class SimpleFunctionWithRoots implements UnivariateFunction {
        @Override
        public double value(double x) {
            if (x == 0.5) {
                throw new UnsupportedOperationException();
            }
            if (x < 0.5) {
                return Math.sin(Math.PI * x) - Math.sin(Math.PI);
            } else {
                return -(Math.sin(Math.PI * (x - 1)) - Math.sin(Math.PI));
            }
        }
    }

    private static class SimpleFunctionWithRootsAndInflectionPoint implements UnivariateFunction {
        @Override
        public double value(double x) {
            if (x < -0.75) {
                return Math.pow(x, 2) + 0.5;
            } else if (x < -0.5) {
                return Math.pow(x + 0.25, 2) - 0.25;
            } else if (x < 0.5) {
                return -Math.pow(x - 0.25, 2) + 0.25;
            } else {
                return Math.pow(x - 0.75, 2) + 0.5;
            }
        }
    }

    private static class SimpleFunctionWithSingularity implements UnivariateFunction {
        @Override
        public double value(double x) {
            if (x == 0) {
                throw new ArithmeticException("Divide by zero");
            }
            return 1 / x;
        }
    }
}