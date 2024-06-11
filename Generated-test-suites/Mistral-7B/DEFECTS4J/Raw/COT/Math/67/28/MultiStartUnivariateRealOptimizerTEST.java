package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.univariate.UnivariateRealOptimizer;
import org.apache.commons.math3.random.MersenneTwisterFast;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartUnivariateRealOptimizer() {
        UnivariateRealFunction testFunction = new TestFunction();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new NelderMeadSimplexOptimizer(), 10, new MersenneTwisterFast());
        try {
            double result = optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0);
            Assert.assertNotEquals(Double.NaN, result);
            double optimalValue = testFunction.value(result);
            Assert.assertEquals(0.5, optimalValue, 0.001);
            double[] optima = optimizer.getOptima();
            double[] optimaValues = optimizer.getOptimaValues();
            List<PointValuePair> actualOptima = new ArrayList<>();
            for (int i = 0; i < optima.length; i++) {
                actualOptima.add(new PointValuePair(optima[i], optimaValues[i]));
            }
            List<PointValuePair> expectedOptima = List.of(
                    new PointValuePair(0.20959586195650393, 0.4332683335657578),
                    new PointValuePair(0.2537028134414334, 0.460329611437429),
                    new PointValuePair(0.3288616652061115, 0.46666313012407),
                    new PointValuePair(0.3719837702993544, 0.46721229355939),
                    new PointValuePair(0.4649399728606798, 0.467300021281803),
                    new PointValuePair(0.5, 0.4666666666666666),
                    new PointValueValuePair(0.5350600271393201, 0.46666572262353),
                    new PointValuePair(0.5744325551135002, 0.4666657323478),
                    new PointValuePair(0.6127941382606899, 0.4666657327281),
                    new PointValuePair(0.6511557213078797, 0.4666657327281)
            );
            Assert.assertEquals(expectedOptima, actualOptima);
        } catch (ConvergenceException e) {
            Assert.fail("Should not throw ConvergenceException");
        } catch (FunctionEvaluationException e) {
            Assert.fail("Should not throw FunctionEvaluationException");
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testNoOptimaComputedYet() {
        UnivariateRealFunction testFunction = new TestFunction();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new NelderMeadSimplexOptimizer(), 10, new MersenneTwisterFast());
        optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0);
        optimizer.getOptima();
    }

    static class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 0.5 * Math.pow(x, 3) + 2 * x - 1;
        }

        @Override
        public double derivative(double x) {
            return 2 * x - x * x - 0.5 * Math.pow(x, 2) + 2;
        }

        @Override
        public double derivative2(double x) {
            return 2 * (1 - x) - 2 * x;
        }

        @Override
        public double[] gradient(double[] x) {
            return new double[]{2 * x[0] - x[0] * x[0] - 0.5 * Math.pow(x[0], 2) + 2,
                                2 * (1 - x[1]) - 2 * x[1]};
        }

        @Override
        public SimpleBounds getRange() {
            return new SimpleBounds(Precision.SAFE_MIN, Precision.SAFE_MAX);
        }
    }
}