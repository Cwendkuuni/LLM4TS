package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.SimpleGoalType;
import org.apache.commons.math3.optimization.UnconstrainedOptimizationProblem;
import org.apache.commons.math3.optimization.convergence.ConvergenceChecker;
import org.apache.commons.math3.optimization.simple.SimpleValueChecker;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the BaseOptimizer interface.
 */
public class BaseOptimizerTest {

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = new BaseMockOptimizer();
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals("Maximum number of evaluations should be set", MaxEval.UNLIMITED, maxEvaluations);

        optimizer = new BaseMockOptimizer(10);
        maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals("Maximum number of evaluations should be set", 10, maxEvaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Object> optimizer = new BaseMockOptimizer();
        int evaluations = optimizer.getEvaluations();
        assertEquals("Number of evaluations should be zero", 0, evaluations);

        optimizer.optimize(new UnconstrainedOptimizationProblem(new SimpleGoalType(), new SimpleFunction(), new SimplePoint()));
        evaluations = optimizer.getEvaluations();
        assertEquals("Number of evaluations should be one", 1, evaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}.
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Object> optimizer = new BaseMockOptimizer();
        ConvergenceChecker<Object> convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull("Convergence checker should not be null", convergenceChecker);

        optimizer = new BaseMockOptimizer(new SimpleValueChecker(1.0E-6));
        convergenceChecker = optimizer.getConvergenceChecker();
        assertSame("Convergence checker should be the one set", convergenceChecker.getInstance(), new SimpleValueChecker(1.0E-6));
    }

    /**
     * Test case for {@link BaseOptimizer#getMaxEvaluations()} with negative value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxEvaluations() {
        BaseOptimizer<Object> optimizer = new BaseMockOptimizer(-1);
        optimizer.getMaxEvaluations();
    }

    /**
     * Test case for {@link BaseOptimizer#getEvaluations()} before call to optimize method.
     */
    @Test
    public void testGetEvaluationsBeforeOptimize() {
        BaseOptimizer<Object> optimizer = new BaseMockOptimizer();
        int evaluations = optimizer.getEvaluations();
        assertEquals("Number of evaluations should be zero", 0, evaluations);
    }

    /**
     * Test case for {@link BaseOptimizer#getEvaluations()} after call to optimize method multiple times.
     */
    @Test
    public void testGetEvaluationsAfterMultipleOptimizes() {
        BaseOptimizer<Object> optimizer = new BaseMockOptimizer();
        int evaluations1 = optimizer.getEvaluations();
        optimizer.optimize(new UnconstrainedOptimizationProblem(new SimpleGoalType(), new SimpleFunction(), new SimplePoint()));
        int evaluations2 = optimizer.getEvaluations();
        optimizer.optimize(new UnconstrainedOptimizationProblem(new SimpleGoalType(), new SimpleFunction(), new SimplePoint()));
        int evaluations3 = optimizer.getEvaluations();

        assertEquals("Number of evaluations should increase after each call to optimize", evaluations1 + evaluations2 + evaluations3, optimizer.getEvaluations());
    }
}

class BaseMockOptimizer extends BaseOptimizer<Object> {
    private int maxEvaluations;
    private ConvergenceChecker<Object> convergenceChecker;

    /**
     * Constructor with no maximum number of evaluations.
     */
    public BaseMockOptimizer() {
        this(MaxEval.UNLIMITED);
    }

    /**
     * Constructor with maximum number of evaluations.
     *
     * @param maxEvaluations The maximum number of evaluations.
     */
    public BaseMockOptimizer(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    /**
     * Constructor with convergence checker.
     *
     * @param convergenceChecker The convergence checker.
     */
    public BaseMockOptimizer(ConvergenceChecker<Object> convergenceChecker) {
        this.convergenceChecker = convergenceChecker;
    }

    @Override
    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    @Override
    public ConvergenceChecker<Object> getConvergenceChecker() {
        return convergenceChecker;
    }
}

class SimpleFunction implements org.apache.commons.math3.optim.NonSmoothedFunction<Object, Double> {
    @Override
    public Double value(Object point) {
        return 0.0;
    }

    @Override
    public double value(double[] point) {
        return 0.0;
    }

    @Override
    public void gradient(Object point, double[] grad) throws UnsupportedOperationException {
        // Not implemented
    }

    @Override
    public void gradient(double[] point, double[] grad) throws UnsupportedOperationException {
        // Not implemented
    }

    @Override
    public void hessian(Object point, double[][] hess) throws UnsupportedOperationException {
        // Not implemented
    }

    @Override
    public void hessian(double[] point, double[][] hess) throws UnsupportedOperationException {
        // Not implemented
    }
}

class SimplePoint implements PointValuePair<Object, Double> {
    private Object point;
    private Double value;

    /**
     * Constructor.
     *
     * @param point The point.
     * @param value The value.
     */
    public SimplePoint(Object point, Double value) {
        this.point = point;
        this.value = value;
    }

    @Override
    public Object getPoint() {
        return point;
    }

    @Override
    public Double getValue() {
        return value;
    }
}

class SimpleValueChecker implements ConvergenceChecker<Object> {
    private double tolerance;

    /**
     * Constructor.
     *
     * @param tolerance The tolerance.
     */
    public SimpleValueChecker(double tolerance) {
        this.tolerance = tolerance;
    }

    @Override
    public boolean isConverged(Object optimizationData, Object currentValue) {
        Double currentValueAsDouble = (Double) currentValue;
        Double lastValue = ((PointValuePair<Object, Double>) optimizationData).getValue();
        return Math.abs(currentValueAsDouble - lastValue.doubleValue()) <= tolerance;
    }

    @Override
    public ConvergenceChecker<Object> getInstance() {
        return this;
    }
}