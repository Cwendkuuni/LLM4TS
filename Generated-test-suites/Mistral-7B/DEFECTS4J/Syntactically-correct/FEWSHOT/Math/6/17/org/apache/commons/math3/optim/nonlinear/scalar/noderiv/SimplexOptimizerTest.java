package org.apache.commons.cli.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initialization.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.simple.*;
import org.apache.commons.math3.optim.points.*;
import org.apache.commons.math3.optim.stats.DescriptiveStatistics;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexOptimizer;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testSimpleExample() {
        ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]), 2) + Math.pow(Math.sin(x[1]), 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        PointValuePair optimum = new SimplexOptimizer(new SimpleValueChecker(1e-5, 1e-5)).optimize(
                100, f, GoalType.MINIMIZE, new RealPointValuePair[]{
                        new RealPointValuePair(new RealVector(2, 0.0), Double.NaN),
                        new RealPointValuePair(new RealVector(2, 1.5), Double.NaN)
                });

        assertEquals(0.0, optimum.getValue(), 1e-5);
        assertEquals(new RealVector(2, 0.0), optimum.getPoint(), 1e-5);
    }

    @Test
    public void testConvergence() {
        ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return FunctionUtils.pow(Math.sin(x[0]), 2) + FunctionUtils.pow(Math.sin(x[1]), 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        PointValuePair[] start = new RealPointValuePair[]{
            new RealPointValuePair(new RealVector(2, 0.0), Double.NaN),
            new RealPointValuePair(new RealVector(2, 1.5), Double.NaN),
            new RealPointValuePair(new RealVector(2, 3.0), Double.NaN),
            new RealPointValuePair(new RealVector(2, 4.5), Double.NaN)
        };

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-5, 1e-5));
        PointValuePair result = optimizer.optimize(10, f, GoalType.MINIMIZE, start);

        assertEquals(0.0, result.getValue(), 1e-5);
        assertEquals(new RealVector(2, 0.0), result.getPoint(), 1e-5);

        PointValuePair[] previous = optimizer.getPreviousBest();
        double[] previousValues = new double[previous.length];
        for (int i = 0; i < previous.length; i++) {
            previousValues[i] = previous[i].getValue();
        }

        PointValuePair[] current = optimizer.getCurrentBest();
        double[] currentValues = new double[current.length];
        for (int i = 0; i < current.length; i++) {
            currentValues[i] = current[i].getValue();
        }

        SimpleValueChecker checker = (SimpleValueChecker) optimizer.getConvergenceChecker();
        boolean convergence = checker.converged(1, previous, current);
        assertTrue(convergence);

        PointValuePair[] previousIteration = optimizer.getPreviousBest(1);
        double[] previousIterationValues = new double[previousIteration.length];
        for (int i = 0; i < previousIteration.length; i++) {
            previousIterationValues[i] = previousIteration[i].getValue();
        }

        assertFalse(checker.converged(1, previous, previousIteration));

        DescriptiveStatistics statistics = new DescriptiveStatistics();
        for (int i = 0; i < previousValues.length; i++) {
            statistics.addValue(previousValues[i]);
        }
        double mean = statistics.getMean();
        double std = statistics.getStandardDeviation();

        assertTrue(Math.abs(mean - result.getValue()) < 1e-5);
        assertTrue(Math.abs(std) < 1e-5);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullOptimizationData() {
        new SimplexOptimizer().optimize(1, null, GoalType.MINIMIZE, null);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        new SimplexOptimizer().optimize(1, null, GoalType.MINIMIZE, new RealPointValuePair[]{});
    }

    @Test(expected = NullArgumentException.class)
    public void testNullGoalType() {
        new SimplexOptimizer().optimize(1, new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return 0.0;
            }

            @Override
            public int getNumberOfVariables() {
                return 0;
            }
        }, null, new RealPointValuePair[]{});
    }
}