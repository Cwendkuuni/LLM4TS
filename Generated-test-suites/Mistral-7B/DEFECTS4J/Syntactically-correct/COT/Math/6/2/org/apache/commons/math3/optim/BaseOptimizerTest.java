package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.junit.Test;

/**
 * Test class for {@link BaseOptimizer}.
 *
 * @author John Smith
 */
public class BaseOptimizerTest {

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = new DummyBaseOptimizer();
        int maxEvaluations = optimizer.getMaxEvaluations();
        int expected = MaxEval.MAX_EVAL; // Max value for MaxEval constant
        assertEquals("Max evaluations should match expected value", expected, maxEvaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Object> optimizer = new DummyBaseOptimizer();
        int evaluations = optimizer.getEvaluations();
        int expected = 0;
        assertEquals("Evaluations should match expected value", expected, evaluations);

        optimizer.optimize(new DummyObjectiveFunction(), new DummyPoint());
        evaluations = optimizer.getEvaluations();
        expected = 1;
        assertEquals("Evaluations should match expected value", expected, evaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}.
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Object> optimizer = new DummyBaseOptimizer();
        ConvergenceChecker<Object> convergenceChecker = optimizer.getConvergenceChecker();
        assertTrue("Convergence checker should not be null", convergenceChecker != null);
        assertTrue("Convergence checker should be an instance of SimpleValueChecker", convergenceChecker instanceof SimpleValueChecker);
    }

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()} with negative value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxEvaluations() {
        BaseOptimizer<Object> optimizer = new DummyBaseOptimizer();
        optimizer.setMaxEvaluations(-1);
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()} with negative value.
     */
    @Test(expected = IllegalStateException.class)
    public void testNegativeEvaluations() {
        BaseOptimizer<Object> optimizer = new DummyBaseOptimizer();
        optimizer.setEvaluations(-1);
    }
}

class DummyBaseOptimizer implements BaseOptimizer<Object> {

    private int maxEvaluations = MaxEval.MAX_EVAL;

    @Override
    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    @Override
    public void setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    private int evaluations = 0;

    @Override
    public int getEvaluations() {
        return evaluations;
    }

    @Override
    public ConvergenceChecker<Object> getConvergenceChecker() {
        return new SimpleValueChecker();
    }

    @Override
    public Object optimize(ObjectiveFunction objectiveFunction, Point startPoint) {
        // Method body not relevant for testing
        return null;
    }
}

class DummyObjectiveFunction implements ObjectiveFunction {

    @Override
    public double value(Point point) {
        // Method body not relevant for testing
        return 0.0;
    }

    @Override
    public Point gradient(Point point) {
        // Method body not relevant for testing
        return null;
    }
}

class DummyPoint implements Point {

    @Override
    public double[] getCoordinates() {
        // Method body not relevant for testing
        return new double[0];
    }

    @Override
    public String toString() {
        // Method body not relevant for testing
        return "";
    }
}