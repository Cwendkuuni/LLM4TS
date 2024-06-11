package org.apache.commons.cli.optimization; // assuming the test class is in the same package as the optimizer class

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateRealFunction function;
    private GoalType goalType;
    private double min, max, expectedOptima;
    private double[] expectedOptimaValues;

    public MultiStartUnivariateRealOptimizerTest(UnivariateRealFunction function, GoalType goalType, double min, double max, double[] expectedOptima, double[] expectedOptimaValues) {
        this.function = function;
        this.goalType = goalType;
        this.min = min;
        this.max = max;
        this.expectedOptima = expectedOptima[0];
        this.expectedOptimaValues = expectedOptimaValues;
    }

    @Parameters
    public static Collection<Object[]> data() {
        // Define test cases here, for example:
        Object[] testCase1 = new Object[]{new MyTestFunction(), GoalType.MINIMIZE, 0.0, 1.0, new double[]{0.5}, new double[]{0.25, 0.25, 0.25, 0.25, 0.25}};
        Object[] testCase2 = new Object[]{new MyTestFunction(), GoalType.MAXIMIZE, 0.0, 1.0, new double[]{1.0}, new double[]{1.0, 1.0, 1.0, 1.0, 1.0}};
        return Arrays.asList(new Object[][]{testCase1, testCase2});
    }

    @Test
    public void testMultiStartOptimization() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new SimpleUnivariateRealOptimizer(), 5, new RandomGenerator());
        double[] actualOptima = optimizer.optimize(function, goalType, min, max);
        double[] actualOptimaValues = optimizer.getOptimaValues();
        assertArrayEquals("Optima not as expected", expectedOptima, actualOptima);
        assertArrayEquals("Optima values not as expected", expectedOptimaValues, actualOptimaValues);
    }
}

class MyTestFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.pow(x - 0.5, 2);
    }
}