package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.Arrays;

import org.apache.commons.math3.analysis.BesselFunction;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMultiStart;
import org.apache.commons.math3.random.RandomValuesGenerator;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

import org.apache.commons.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.commons.math.optimization.univariate.UnivariateRealFunction;
import org.apache.commons.commons.math.optimization.univariate.UnivariateRealPointValuePair;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartUnivariateRealOptimizer() {
        // Prepare inputs
        UnivariateRealFunction f = new BesselFunction(BesselFunction.B0);
        GoalType goal = GoalType.MINIMIZE;
        double min = -2.0;
        double max = 2.0;
        int maxEvaluations = 10;
        int starts = 3;
        RandomValuesGenerator rng = new RandomValuesGenerator();

        // Test optimization
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new FibonacciSearchOptimizer<>(), starts, rng);
        optimizer.setConvergenceChecker(new MaxEval(maxEvaluations));

        try {
            UnivariateRealPointValuePair result = optimizer.optimize(f, goal, min, max);
            UnivariateRealPointValuePair[] optima = optimizer.getOptima();

            assertEquals(goal, result.getGoalType());
            assertEquals(min, result.getX(), 0.001);
            assertEquals(FunctionUtils.min(f, new SimpleBounds(min, max)), result.getValue(), 0.001);

            assertArrayEquals(new UnivariateRealPointValuePair[] {result, null, null, null}, optima);
            assertNull(optimizer.getConvergenceChecker());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test optimization with custom optimizer
        UnivariateOptimizer<UnivariateFunction> singleStartOptimizer = new GoldenSectionSearch();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(
                singleStartOptimizer, starts, rng);
        multiStartOptimizer.setConvergenceChecker(new GoalTypeMultiStart(goal, starts, 1));

        try {
            UnivariateRealPointValuePair result = multiStartOptimizer.optimize(f, goal, min, max);
            UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();

            assertEquals(goal, result.getGoalType());
            assertEquals(min, result.getX(), 0.001);
            assertEquals(FunctionUtils.min(f, new SimpleBounds(min, max)), result.getValue(), 0.001);

            assertArrayEquals(optima, new UnivariateRealPointValuePair[] {result, null, null, null});
            assertEquals(goal, multiStartOptimizer.getConvergenceChecker().getGoalType());
            assertEquals(starts, multiStartOptimizer.getConvergenceChecker().getMaxIterations());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test optimization with custom optimizer and custom convergence checker
        UnivariateOptimizer<UnivariateFunction> singleStartOptimizer2 = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer2 = new MultiStartUnivariateRealOptimizer<>(
                singleStartOptimizer2, starts, rng);
        multiStartOptimizer2.setConvergenceChecker(new MyConvergenceChecker());

        try {
            UnivariateRealPointValuePair result = multiStartOptimizer2.optimize(f, goal, min, max);
            UnivariateRealPointValuePair[] optima = multiStartOptimizer2.getOptima();

            assertEquals(goal, result.getGoalType());
            assertEquals(min, result.getX(), 0.001);
            assertEquals(FunctionUtils.min(f, new SimpleBounds(min, max)), result.getValue(), 0.001);

            assertArrayEquals(optima, new UnivariateRealPointValuePair[] {result, null, null, null});
            assertEquals(MyConvergenceChecker.class, multiStartOptimizer2.getConvergenceChecker().getClass());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<UnivariateRealPointValuePair> {
        public GoalType getGoalType() {
            return GoalType.MINIMIZE;
        }

        public int getMaxIterations() {
            return 1;
        }

        public boolean isConverged(UnivariateRealPointValuePair current,
                                   UnivariateRealPointValuePair previous) {
            return current.getValue() == previous.getValue();
        }

        public boolean isSatisfied(UnivariateRealPointValuePair value) {
            return value != null && value.getValue() < 1.0;
        }
    }
}