package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BrentOptimizerTest {
    
    @Test
    public void testDoOptimize() {
        double rel = 1e-10;
        double abs = 1e-10;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);

        // Test when the function is unimodal and has a minimum
        UnivariateFunction function1 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2, 2);
            }
        };
        GoalType goalType1 = GoalType.MINIMIZE;
        double min1 = -10;
        double max1 = 10;
        double expectedMinValue1 = 0;
        double expectedMinPoint1 = 2;
        UnivariatePointValuePair expectedMinResult1 = new UnivariatePointValuePair(expectedMinPoint1, expectedMinValue1);

        UnivariatePointValuePair result1 = optimizer.doOptimize(function1, goalType1, min1, max1);
        assertEquals(expectedMinResult1, result1);
        
        // Test when the function is unimodal and has a maximum
        UnivariateFunction function2 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(x - 2, 2);
            }
        };
        GoalType goalType2 = GoalType.MAXIMIZE;
        double min2 = -10;
        double max2 = 10;
        double expectedMaxValue2 = 0;
        double expectedMaxPoint2 = 2;
        UnivariatePointValuePair expectedMaxResult2 = new UnivariatePointValuePair(expectedMaxPoint2, expectedMaxValue2);

        UnivariatePointValuePair result2 = optimizer.doOptimize(function2, goalType2, min2, max2);
        assertEquals(expectedMaxResult2, result2);

        // Test when the function is multimodal and has multiple minima
        UnivariateFunction function3 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2, 2) + Math.pow(x + 2, 2);
            }
        };
        GoalType goalType3 = GoalType.MINIMIZE;
        double min3 = -10;
        double max3 = 10;
        double expectedMinValue3 = 0;
        double expectedMinPoint3 = -2;
        UnivariatePointValuePair expectedMinResult3 = new UnivariatePointValuePair(expectedMinPoint3, expectedMinValue3);

        UnivariatePointValuePair result3 = optimizer.doOptimize(function3, goalType3, min3, max3);
        assertEquals(expectedMinResult3, result3);
        
        // Test when the function is unbounded
        UnivariateFunction function4 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        GoalType goalType4 = GoalType.MINIMIZE;
        double min4 = Double.NEGATIVE_INFINITY;
        double max4 = Double.POSITIVE_INFINITY;
        double expectedMinValue4 = Double.NEGATIVE_INFINITY;
        double expectedMinPoint4 = 0;
        UnivariatePointValuePair expectedMinResult4 = new UnivariatePointValuePair(expectedMinPoint4, expectedMinValue4);

        UnivariatePointValuePair result4 = optimizer.doOptimize(function4, goalType4, min4, max4);
        assertEquals(expectedMinResult4, result4);

        // Test when the function is constant
        UnivariateFunction function5 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 1;
            }
        };
        GoalType goalType5 = GoalType.MINIMIZE;
        double min5 = -10;
        double max5 = 10;
        double expectedMinValue5 = 1;
        double expectedMinPoint5 = 0;
        UnivariatePointValuePair expectedMinResult5 = new UnivariatePointValuePair(expectedMinPoint5, expectedMinValue5);

        UnivariatePointValuePair result5 = optimizer.doOptimize(function5, goalType5, min5, max5);
        assertEquals(expectedMinResult5, result5);
    }
}