package org.apache.commons.math3.optimization;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.initializer.GradientDescentInitializer;
import org.apache.commons.math3.optim.nonlinear.ScaledGradientDescent;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.strategy.OnePointStrategy;
import org.apache.commons.math3.optim.strategy.SimpleValueChecker;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.junit.Test;

public class BaseOptimizerTest<PAIR extends PointValuePair<Point, Double>> {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new ScaledGradientDescent<>(1.0E-6, new SimpleValueChecker(1.0E-6), new GradientDescentInitializer(0.1), new OnePointStrategy());
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(Integer.MAX_VALUE, maxEvaluations);

        optimizer = new ScaledGradientDescent<>(1.0E-6, new SimpleValueChecker(1.0E-6), new AroundMinimumInitializer(0.1), new OnePointStrategy());
        maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(Integer.MAX_VALUE, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = new ScaledGradientDescent<>(1.0E-6, new SimpleValueChecker(1.0E-6), new GradientDescentInitializer(0.1), new OnePointStrategy());
        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations);

        optimizer.optimize(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }

            @Override
            public double derivative(double x) {
                return 2.0 * x;
            }
        });

        evaluations = optimizer.getEvaluations();
        assertEquals(1, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new ScaledGradientDescent<>(1.0E-6, new SimpleValueChecker(1.0E-6), new GradientDescentInitializer(0.1), new OnePointStrategy());
        ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull(convergenceChecker);

        optimizer = new ScaledGradientDescent<>(1.0E-6, new SimpleValueChecker(1.0E-6), new AroundMinimumInitializer(0.1), new OnePointStrategy());
        convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull(convergenceChecker);
    }
}