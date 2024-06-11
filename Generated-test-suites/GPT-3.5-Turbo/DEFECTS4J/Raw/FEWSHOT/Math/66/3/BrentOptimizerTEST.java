package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BrentOptimizerTest {

    // Test optimize(UnivariateRealFunction f, GoalType goalType, double min, double max, double startValue)
    @Test
    public void testOptimizeWithStartValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return (x - 3) * (x - 3);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();

        double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 5, 2.5);

        assertEquals(3.0, result, 0.0001);
    }

    @Test
    public void testOptimizeWithStartValue2() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return (x - 3) * (x - 3);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();

        double result = optimizer.optimize(f, GoalType.MAXIMIZE, 0, 5, 4.5);

        assertEquals(3.0, result, 0.0001);
    }

    // Test optimize(UnivariateRealFunction f, GoalType goalType, double min, double max)
    @Test
    public void testOptimizeWithoutStartValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return (x - 3) * (x - 3);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();

        double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 5);

        assertEquals(3.0, result, 0.0001);
    }

    @Test
    public void testOptimizeWithoutStartValue2() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return (x - 3) * (x - 3);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();

        double result = optimizer.optimize(f, GoalType.MAXIMIZE, 0, 5);

        assertEquals(3.0, result, 0.0001);
    }

    // Test localMin(boolean isMinim, UnivariateRealFunction f, GoalType goalType, double lo, double mid, double hi, double eps, double t)

    @Test
    public void testLocalMin(){
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return (x - 3) * (x - 3);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        boolean isMinim = true;
        GoalType goalType = GoalType.MINIMIZE;
        double lo = 0;
        double mid = 2.5;
        double hi = 5;
        double eps = 1E-10;
        double t = 1E-10;

        try {
            double result = optimizer.localMin(isMinim, f, goalType, lo, mid, hi, eps, t);

            assertEquals(3.0, result, 0.0001);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testLocalMin2(){
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return (x - 3) * (x - 3);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        boolean isMinim = true;
        GoalType goalType = GoalType.MAXIMIZE;
        double lo = 0;
        double mid = 4.5;
        double hi = 5;
        double eps = 1E-10;
        double t = 1E-10;

        try {
            double result = optimizer.localMin(isMinim, f, goalType, lo, mid, hi, eps, t);

            assertEquals(3.0, result, 0.0001);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}