package org.apache.commons.math.optimization;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;
    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        // Create a simple function for testing
        function = new UnivariateFunctionWithMinimumAndMaximum();
        // Create an optimizer and set up some parameters
        UnivariateOptimizer optimizerUnderTest = new NelderMeadSimplexOptimizer();
        optimizer = new MultiStartUnivariateRealOptimizer(optimizerUnderTest, 5, new RandomDataGenerator());
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(200);
    }

    @Test
    public void testBasicFunctionality() {
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        double expectedResult = 0.75;
        assertEquals(expectedResult, result.getPoint(), 0.001);
        assertEquals(expectedResult, result.getValue(), 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testNoConvergence() {
        optimizer.optimize(new SineFunction(), GoalType.MINIMIZE, 0.0, 2.0 * Math.PI);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        optimizer.optimize(new FunctionWithException(), GoalType.MINIMIZE, 0.0, 1.0);
    }

    @Test(expected = MathRuntimeException.class)
    public void testIllegalArgumentException() {
        optimizer.optimize(null, GoalType.MINIMIZE, 0.0, 1.0);
    }

    @Test
    public void testMultipleStarts() {
        PointValuePair[] optima = optimizer.getOptima();
        int numberOfStarts = optimizer.getStarts();
        assertEquals(numberOfStarts, optima.length);
        double expectedMinValue = 0.75;
        double expectedMaxValue = 0.85;
        for (PointValuePair pvp : optima) {
            double point = pvp.getPoint();
            double value = pvp.getValue();
            if (numberOfStarts > 1) {
                assertTrue(point > 0.0);
                assertTrue(point < 1.0);
                assertTrue(value >= expectedMinValue);
                assertTrue(value <= expectedMaxValue);
            } else {
                assertEquals(expectedMinValue, point, 0.001);
                assertEquals(expectedMinValue, value, 0.001);
            }
        }
    }

    private static class UnivariateFunctionWithMinimumAndMaximum implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            if (x < 0.25) {
                return Math.pow(x, 2) + 0.1 * x;
            } else {
                return Math.pow(x - 0.75, 2);
            }
        }

        @Override
        public double derive(double x) {
            if (x < 0.25) {
                return 2 * x + 0.1;
            } else {
                return 2 * (x - 0.75);
            }
        }

    }

    private static class SineFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return Math.sin(Math.PI * x);
        }

        @Override
        public double derive(double x) {
            return Math.cos(Math.PI * x);
        }

    }

    private static class FunctionWithException implements UnivariateRealFunction {

        @Override
        public double value(double x) throws FunctionEvaluationException {
            throw new FunctionEvaluationException("Function evaluation error");
        }

        @Override
        public double derive(double x) {
            return 0.0;
        }

    }

}