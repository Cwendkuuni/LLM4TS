package org.apache.commons.math3.optimization;

import static org.junit.Assert.*;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleBounds;
import org.apache.commons.math3.optimization.UnconstrainedOptimizationProblem;
import org.apache.commons.math3.optimization.convergence.SimpleConvergenceChecker;
import org.apache.commons.math3.optimization.nonlinear.scalar.GoalTypeTest;
import org.junit.Test;

/**
 * Test cases for BaseOptimizer interface.
 */
public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        // Create a test instance of BaseOptimizer
        BaseOptimizer<PointValuePair> optimizer = new DummyBaseOptimizer<>();

        // Test default value
        assertEquals(MaxEval.UNLIMITED, optimizer.getMaxEvaluations());

        // Test custom value
        optimizer = new DummyBaseOptimizer<>(10);
        assertEquals(10, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        // Create a test instance of BaseOptimizer
        BaseOptimizer<PointValuePair> optimizer = new DummyBaseOptimizer<>();

        // Test default value
        assertEquals(0, optimizer.getEvaluations());

        // Test value after optimization
        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(f -> 0.1 * Math.pow(f, 2), new SimpleBounds(0, 1));
        optimizer.optimize(problem, new SimpleConvergenceChecker(1e-6, 1e-6, GoalType.MINIMIZE));
        assertEquals(2, optimizer.getEvaluations()); // One for the problem constructor, another for optimization
    }

    @Test
    public void testGetConvergenceChecker() {
        // Create a test instance of BaseOptimizer
        BaseOptimizer<PointValuePair> optimizer = new DummyBaseOptimizer<>();

        // Test default value
        assertNull(optimizer.getConvergenceChecker());

        // Test custom value
        optimizer = new DummyBaseOptimizer<>(new SimpleConvergenceChecker(1e-6, 1e-6, GoalTypeTest.MINIMIZE));
        assertEquals(optimizer.getConvergenceChecker().getAbsoluteTolerance(), 1e-6, 0.001);
        assertEquals(optimizer.getConvergenceChecker().getRelativeTolerance(), 1e-6, 0.001);
        assertEquals(optimizer.getConvergenceChecker().getMaximumIterations(), GoalTypeTest.MAX_ITERATIONS);
        assertEquals(optimizer.getConvergenceChecker().getGoalType(), GoalTypeTest.GOAL_TYPE);
    }
}

class DummyBaseOptimizer<PAIR> implements BaseOptimizer<PAIR> {
    private int maxEvaluations;
    private ConvergenceChecker<PAIR> convergenceChecker;

    public DummyBaseOptimizer() {
        this.maxEvaluations = MaxEval.UNLIMITED;
    }

    public DummyBaseOptimizer(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    public DummyBaseOptimizer(ConvergenceChecker<PAIR> convergenceChecker) {
        this.convergenceChecker = convergenceChecker;
    }

    @Override
    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    @Override
    public int getEvaluations() {
        return 0;
    }

    @Override
    public ConvergenceChecker<PAIR> getConvergenceChecker() {
        return convergenceChecker;
    }
}