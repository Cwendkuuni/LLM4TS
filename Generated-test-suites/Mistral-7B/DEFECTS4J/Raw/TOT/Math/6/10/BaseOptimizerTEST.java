package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.GaussNewtonMinimizer;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointInitializer;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.GaussNewtonMinimizer;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointInitializer;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.GaussNewtonMinimizer;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointInitializer;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.GaussNewtonMinimizer;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointInitializer;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.GaussNewtonMinimizer;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointInitializer;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class BaseOptimizerTest<PAIR> {

    private BaseOptimizer<PAIR> optimizer;
    private ObjectiveFunction objectiveFunction;
    private ConvergenceChecker<PAIR> convergenceChecker;

    @Before
    public void setUp() {
        optimizer = new GaussNewtonMinimizer<>(new SimpleBounds(-10.0, 10.0), new AveragedStartingPointInitializer<>(10));
        objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }

            @Override
            public double value(double[] x) {
                return Math.pow(x[0], 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };
        convergenceChecker = new ConvergenceChecker<PAIR>() {
            @Override
            public boolean check(PAIR parameter, double value, int iteration) {
                return (value < 0.001);
            }
        };
    }

    @Test
    public void testGetMaxEvaluations() {
        assertFalse(optimizer instanceof BaseOptimizer);
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(MaxEval.UNLIMITED, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        assertFalse(optimizer instanceof BaseOptimizer);
        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        assertFalse(optimizer instanceof BaseOptimizer);
        ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull(convergenceChecker);
    }

    @Test
    public void testOptimizeWithSimpleBounds() {
        optimizer.setObjectiveFunction(objectiveFunction);
        optimizer.setConvergenceChecker(convergenceChecker);
        optimizer.optimize(new SimpleBounds(-5.0, 5.0));

        int evaluations = optimizer.getEvaluations();
        PointValuePair result = optimizer.getOptimum();
        assertEquals(3, evaluations);
        assertFalse(Double.isNaN(result.getValue()));
        assertEquals(0.0, result.getValue(), 0.01);
        assertEquals(-1.0, result.getPoint()[0], 0.01);
    }

    @Test(expected = UnconstrainedOptimizationException.class)
    public void testOptimizeWithInvalidBounds() {
        optimizer.setObjectiveFunction(objectiveFunction);
        optimizer.setConvergenceChecker(convergenceChecker);
        optimizer.optimize(new SimpleBounds(5.0, -5.0));
    }
}