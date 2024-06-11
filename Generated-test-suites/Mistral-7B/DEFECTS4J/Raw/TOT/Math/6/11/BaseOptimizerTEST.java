package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.initializer.AroundMinimum;
import org.apache.commons.math3.optim.point.PointValuePairCollection;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.convergers.FibonacciSearch;
import org.apache.commons.math3.optim.univariate.convergers.SimpleBounds;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BaseOptimizerTest<PAIR extends PointValuePair> {

    private final UnivariateFunction testFunction;
    private final int maxEvaluations;

    public BaseOptimizerTest(UnivariateFunction testFunction, int maxEvaluations) {
        this.testFunction = testFunction;
        this.maxEvaluations = maxEvaluations;
    }

    @Parameterized.Parameters(name = "{index}: Testing function {0} with {1} max evaluations")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new SinTestFunction(), 100},
                {new QuadraticTestFunction(), 50},
                {new StepTestFunction(), 5},
                {new NegativeTestFunction(), 20}
        });
    }

    @Test
    public void testOptimize() {
        BaseOptimizer<PAIR> optimizer = new FibonacciSearch(new SimpleBounds(testFunction.getMin(), testFunction.getMax()), new AroundMinimum(), new MaxEval(maxEvaluations), new FibonacciSearch.DefaultObjectiveChecker(1e-12));
        ConvergenceChecker<PAIR> convergenceChecker = new ConvergenceChecker<PAIR>() {
            @Override
            public boolean checkConvergence(PAIR result) {
                return Math.abs(result.getValue().getValue()) <= 1e-12;
            }

            @Override
            public String getCheckMessage() {
                return "convergence not reached";
            }
        };
        optimizer.setConvergenceChecker(convergenceChecker);
        PointValuePair solution = optimizer.optimize(testFunction, null);
        assertEquals("Optimization did not find the minimum", testFunction.getValue(solution.getPoint()), solution.getValue().getValue(), 1e-12);
        assertEquals("Number of function evaluations did not match", maxEvaluations, optimizer.getEvaluations());
    }

    private static class SinTestFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.sin(x);
        }

        @Override
        public double getMin() {
            return -1;
        }

        @Override
        public double getMax() {
            return 1;
        }
    }

    private static class QuadraticTestFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return x * x;
        }

        @Override
        public double getMin() {
            return 0;
        }

        @Override
        public double getMax() {
            return 1;
        }
    }

    private static class StepTestFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return x >= 0 ? 0 : Double.POSITIVE_INFINITY;
        }

        @Override
        public double getMin() {
            return Double.NEGATIVE_INFINITY;
        }

        @Override
        public double getMax() {
            return 0;
        }
    }

    private static class NegativeTestFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return -x;
        }

        @Override
        public double getMin() {
            return 0;
        }

        @Override
        public double getMax() {
            return Double.POSITIVE_INFINITY;
        }
    }
}