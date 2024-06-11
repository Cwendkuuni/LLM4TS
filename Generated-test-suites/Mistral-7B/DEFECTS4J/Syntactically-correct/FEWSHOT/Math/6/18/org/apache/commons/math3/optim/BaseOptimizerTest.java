package org.apache.commons.math3.optimization;

import static org.junit.Assert.*;

import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.SimpleConstraints;
import org.apache.commons.math3.optimization.SimpleGoalType;
import org.apache.commons.math3.optimization.SimpleVectorValue;
import org.apache.commons.math3.optimization.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Test;

/**
 * Test cases for the BaseOptimizer interface.
 */
public class BaseOptimizerTest {

    /**
     * Test getMaxEvaluations method.
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new DummyBaseOptimizer();
        assertEquals(MaxEval.UNLIMITED, optimizer.getMaxEvaluations());

        optimizer = new LimitedBaseOptimizer(5);
        assertEquals(5, optimizer.getMaxEvaluations());
    }

    /**
     * Test getEvaluations method.
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new DummyBaseOptimizer();
        assertEquals(0, optimizer.getEvaluations());

        optimizer.optimize(new DummyUnivariateFunction(), new SimpleGoalType(0.0), new SimpleConstraints());
        assertEquals(1, optimizer.getEvaluations());
    }

    /**
     * Test getConvergenceChecker method.
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new DummyBaseOptimizer();
        assertNull(optimizer.getConvergenceChecker());

        optimizer = new MyConvergenceCheckerBaseOptimizer();
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
        assertTrue(convergenceChecker instanceof MyConvergenceChecker);
    }
}

/**
 * Dummy implementation of BaseOptimizer interface.
 */
class DummyBaseOptimizer implements BaseOptimizer<PointValuePair> {
    // Empty implementation
}

/**
 * Limited implementation of BaseOptimizer interface.
 */
class LimitedBaseOptimizer implements BaseOptimizer<PointValuePair> {
    private final int maxEvaluations;

    public LimitedBaseOptimizer(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    @Override
    public int getMaxEvaluations() {
        return maxEvaluations;
    }
}

/**
 * Dummy univariate function for testing.
 */
class DummyUnivariateFunction implements UnivariateFunction {
    @Override
    public double value(double x) {
        return x * x;
    }

    @Override
    public int getNumberOfVariables() {
        return 1;
    }

    @Override
    public int getNumberOfConstraints() {
        return 0;
    }

    @Override
    public void setMaxEvaluations(MaxEval evaluations) {
        // Not used
    }

    @Override
    public void setGoalType(GoalType goal) {
        // Not used
    }

    @Override
    public void setConstraints(Constraints constraints) {
        // Not used
    }

    @Override
    public void initialize(int n, MaxEval evaluations, GoalType goal, Constraints constraints) {
        // Not used
    }

    @Override
    public PointValuePair optimize(double step, double x, double stepSize) {
        return new PointValuePair(x, value(x));
    }
}

/**
 * Custom convergence checker for testing.
 */
class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
    @Override
    public boolean checkPoint(PointValuePair point) {
        // Custom check condition
        return point.getValue() < 0.1;
    }
}

/**
 * Custom BaseOptimizer implementation for testing.
 */
class MyConvergenceCheckerBaseOptimizer implements BaseOptimizer<PointValuePair> {
    private ConvergenceChecker<PointValuePair> convergenceChecker;

    @Override
    public int getMaxEvaluations() {
        return MaxEval.UNLIMITED;
    }

    @Override
    public int getEvaluations() {
        return 0;
    }

    @Override
    public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
        return convergenceChecker;
    }

    @Override
    public void setConvergenceChecker(ConvergenceChecker<PointValuePair> convergenceChecker) {
        this.convergenceChecker = convergenceChecker;
    }
}