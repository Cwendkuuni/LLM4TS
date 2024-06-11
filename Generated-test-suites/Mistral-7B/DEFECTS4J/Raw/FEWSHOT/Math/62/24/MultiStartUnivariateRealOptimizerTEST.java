package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.univariate.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointGenerator;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciGoldenSectionSearch;
import org.apache.commons.math3.random.MersenneTwister;

public class MultiStartUnivariateRealOptimizerTest {

    @SuppressWarnings("unchecked")
    private static <T extends UnivariateRealFunction> UnivariateRealOptimizer<T> createOptimizer(
            int starts, int maxEvaluations, double min, double max) {
        final ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + 0.5 * Math.pow(x - 1.5, 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };

        UnivariateRealOptimizer<T> optimizer = new FibonacciGoldenSectionSearch(
                new AveragedStartingPointGenerator(new MersenneTwister(), starts),
                GoalType.MINIMIZE);
        optimizer.setMaxEvaluations(maxEvaluations);

        return (UnivariateRealOptimizer<T>) optimizer;
    }

    @Test
    public void testOptimize() throws Exception {
        final int starts = 10;
        final int maxEvaluations = 100;
        final double min = -5.0;
        final double max = 5.0;

        MultiStartUnivariateRealOptimizer<ObjectiveFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(createOptimizer(starts, maxEvaluations, min, max), starts,
                        new MersenneTwister());

        PointValuePair best = optimizer.optimize(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + 0.5 * Math.pow(x - 1.5, 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        }, GoalType.MINIMIZE, min, max);

        System.out.println("Best solution: x = " + best.getPoint() + ", f(x) = " + best.getValue());

        assertFalse(Double.isNaN(best.getPoint()));
        assertTrue(best.getValue() > -10.0);

        PointValuePair[] optima = optimizer.getOptima();
        assertArrayEquals(new PointValuePair[]{best}, optima);
    }

    @Test
    public void testNoConvergence() throws Exception {
        final int starts = 1;
        final int maxEvaluations = 100;
        final double min = 0.0;
        final double max = 1.0;

        MultiStartUnivariateRealOptimizer<ObjectiveFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(new FibonacciGoldenSectionSearch(null, GoalType.MINIMIZE),
                        starts, new MersenneTwister());
        optimizer.setMaxEvaluations(maxEvaluations);

        try {
            optimizer.optimize(new ObjectiveFunction() {
                @Override
                public double value(double x) {
                    throw new RuntimeException();
                }

                @Override
                public int getNumberOfVariables() {
                    return 1;
                }
            }, GoalType.MINIMIZE, min, max);
            fail("Should have thrown ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        PointValuePair[] optima = optimizer.getOptima();
        assertArrayEquals(new PointValuePair[starts], optima);
        for (PointValuePair p : optima) {
            assertNull(p);
        }
    }

    @Test(expected = MathIllegalStateException.class)
    public void testNoOptimizationCall() {
        final int starts = 1;
        final int maxEvaluations = 100;
        final double min = 0.0;
        final double max = 1.0;

        MultiStartUnivariateRealOptimizer<ObjectiveFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(new FibonacciGoldenSectionSearch(null, GoalType.MINIMIZE),
                        starts, new MersenneTwister());
        optimizer.setMaxEvaluations(maxEvaluations);

        optimizer.getOptima();
    }
}