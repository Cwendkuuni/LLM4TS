package org.apache.commons.math.optimization.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.special.E;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartUnivariateRealOptimizer() throws Exception {
         final UnivariateRealFunction f = new PolynomialFunction(1, 3, -1.0, 1.0);
         MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                 new FibonacciSearchUnivariateOptimizer(new AroundMinimumInitializer(1)), 10, new MersenneTwister());
         GoalType goalType = GoalType.MINIMIZE;
         double min = -2.0;
         double max = 2.0;
         double optimum = optimizer.optimize(f, goalType, min, max);
         double[] optima = optimizer.getOptima();
         double[] optimaValues = optimizer.getOptimaValues();

         assertNotNull(optima);
         assertNotNull(optimaValues);
         assertArrayEquals(new double[]{optimum, optimum, optimum, optimum, optimum, optimum, optimum, optimum, optimum, optimum}, optima, 0.00001);
         assertArrayEquals(new double[]{f.value(optima[0]), f.value(optima[1]), f.value(optima[2]), f.value(optima[3]), f.value(optima[4]), f.value(optima[5]), f.value(optima[6]), f.value(optima[7]), f.value(optima[8]), f.value(optima[9])}, optimaValues, 0.00001);
         assertEquals(10, optimizer.getIterationCount());
         assertEquals(MaxEval.MAX_EVALS, optimizer.getEvaluations());
         assertEquals(goalType, optimizer.getGoalType());
         assertEquals(min, optimizer.getMin());
         assertEquals(max, optimizer.getMax());
         assertEquals(optimum, optimizer.getResult(), 0.00001);
         assertEquals(optimum, StatUtils.mean(optima), 0.00001);
         assertEquals(goalType, optimizer.getGoalType());
         assertEquals(min, optimizer.getMin(), 0.00001);
         assertEquals(max, optimizer.getMax(), 0.00001);
         assertEquals(optimum, optimizer.getFunctionValue(), 0.00001);
         assertEquals(goalType, optimizer.getGoalType());
         assertEquals(min, optimizer.getMin(), 0.00001);
         assertEquals(max, optimizer.getMax(), 0.00001);
         PointValuePair bestPoint = new PointValuePair(optima[0], optimaValues[0]);
         UnivariateOptimizer singleStartOptimizer = new FibonacciSearchUnivariateOptimizer(new AroundMinimumInitializer(1));
         double singleStartOptimum = singleStartOptimizer.optimize(f, goalType, min, max).getValue();
         assertEquals(goalType, singleStartOptimizer.getGoalType());
         assertEquals(min, singleStartOptimizer.getMin(), 0.00001);
         assertEquals(max, singleStartOptimizer.getMax(), 0.00001);
         assertEquals(singleStartOptimum, bestPoint.getValue(), 0.00001);
         assertEquals(goalType, bestPoint.getGoalType());
         assertEquals(min, bestPoint.getMin(), 0.00001);
         assertEquals(max, bestPoint.getMax(), 0.00001);
         assertEquals(optima[0], bestPoint.getPoint(), 0.00001);
         assertEquals(optimaValues[0], bestPoint.getValue(), 0.00001);
         assertEquals(1, optimizer.getStarts());
         optimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
         optima = optimizer.getOptima();
         optimaValues = optimizer.getOptimaValues();
         assertEquals(15, optimizer.getIterationCount());
         assertEquals(MaxEval.MAX_EVALS, optimizer.getEvaluations());
         assertEquals(goalType, optimizer.getGoalType());
         assertEquals(min, optimizer.getMin(), 0.00001);
         assertEquals(max, optimizer.getMax(), 0.00001);
         assertEquals(optimum, optimizer.getResult(), 0.00001);
         assertEquals(goalType, optimizer.getGoalType());
         assertEquals(min, optimizer.getMin(), 0.00001);
         assertEquals(max, optimizer.getMax(), 0.00001);
         assertEquals(optimum, optimizer.getFunctionValue(), 0.00001);
         assertEquals(goalType, optimizer.getGoalType());
         assertEquals(min, optimizer.getMin(), 0.00001);
         assertEquals(max, optimizer.getMax(), 0.00001);
         assertArrayEquals(new double[]{optimum, optimum, optimum, optimum, optimum}, StatUtils.mode(optima), 0.00001);
         assertArrayEquals(new double[]{f.value(optima[0]), f.value(optima[1]), f.value(optima[2]), f.value(optima[3]), f.value(optima[4])}, StatUtils.mode(optimaValues), 0.00001);
         assertEquals(5, optimizer.getStarts());
         optimizer = new MultiStartUnivariateRealOptimizer(optimizer, 1, new MersenneTwister());
         optima = optimizer.getOptima();
         optimaValues = optimizer.getOptimaValues();
         assertEquals(15, optimizer.getIterationCount());
         assertEquals(MaxEval.MAX_EVALS, optimizer.getEvaluations());
         assertEquals(goalType, optimizer.getGoalType());
         assertEquals(min, optimizer.getMin(), 0.00001);
         assertEquals(max, optimizer.getMax(), 0.00001);
         assertEquals(optimum, optimizer.getResult(), 0.00001);
         assertEquals(goalType, optimizer.getGoalType());
         assertEquals(min, optimizer.getMin(), 0.00001);
         assertEquals(max, optimizer.getMax(), 0.00001);
         assertEquals(optimum, optimizer.getFunctionValue(), 0.00001);
         assertEquals(goalType, optimizer.getGoalType());
         assertEquals(min, optimizer.getMin(), 0.00001);
         assertEquals(max, optimizer.getMax(), 0.00001);
         assertEquals(new double[]{optimum}, optima, 0.00001);
         assertEquals(new double[]{f.value(optimum)}, optimaValues, 0.00001);
         assertEquals(1, optimizer.getStarts());
    }

    @Test(expected = IllegalStateException.class)
    public void testNoOptimumComputedYet() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciSearchUnivariateOptimizer(new AroundMinimumInitializer(1)), 10, new MersenneTwister());
        optimizer.getOptima();
    }

}