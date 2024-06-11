package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.StoppingCriteria;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optim.univariate.LinearConstraint;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.greedy.ConjugateGradient;
import org.apache.commons.math3.optim.univariate.greedy.SimpleBoundsMinimizer;
import org.apache.commons.math3.optim.univariate.greedy.UnivariateOptimizationProblem;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math3.optim.initializers.Averaged;
import org.apache.commons.math3.optim.initializers.ExtendedRealValuedRandomGenerator;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveValue;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.point.RealVector;
import org.apache.commons.math3.optim.point.RealVectorValue;
import org.apache.commons.math3.optim.point.SingularValueDecomposition;
import org.apache.commons.math3.optim.point.ValueVector;
import org.apache.commons.math3.optim.termination.TerminationCondition;

import java.util.Random;

/**
 * Test class for the {@link BaseOptimizer} interface. This class is intended
 * to be a comprehensive test for the BaseOptimizer interface, checking all
 * methods and validating their behaviors.
 *
 * @see BaseOptimizer
 */
public class BaseOptimizerTest {

    /**
     * Test method for the {@link BaseOptimizer#getMaxEvaluations()} method.
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new SimpleBoundsMinimizer<>();
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(MaxEval.UNLIMITED, maxEvaluations);

        optimizer = new ConjugateGradient<>();
        maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(Integer.MAX_VALUE, maxEvaluations);
    }

    /**
     * Test method for the {@link BaseOptimizer#getEvaluations()} method.
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new SimpleBoundsMinimizer<>();
        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations);

        optimizer.optimize(new UnivariateOptimizationProblem(new SineFunction(), new SimpleBounds(0, Math.PI)));
        evaluations = optimizer.getEvaluations();
        assertNotNull(optimizer.getCurrentValue());
        assertNotNull(optimizer.getCurrentPoint());
        assertEquals(1, evaluations);

        optimizer.optimize(new UnivariateOptimizationProblem(new SineFunction(), new SimpleBounds(0, Math.PI)));
        evaluations = optimizer.getEvaluations();
        assertEquals(2, evaluations);
    }

    /**
     * Test method for the {@link BaseOptimizer#getConvergenceChecker()} method.
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new SimpleBoundsMinimizer<>();
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
        assertNull(convergenceChecker);

        optimizer = new ConjugateGradient<>();
        convergenceChecker = optimizer.getConvergenceChecker();
        assertInstanceOf(ConvergenceChecker.class, convergenceChecker);
    }

    /**
     * Test method for the {@link BaseOptimizer#optimize(UnivariateOptimizationProblem)} method
     * with a univariate optimization problem using a simple bounds constraint.
     */
    @Test
    public void testOptimizeSimpleBounds() {
        BaseOptimizer<PointValuePair> optimizer = new SimpleBoundsMinimizer<>();

        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(new SimpleFunction(), new SimpleBounds(0, 1));

        try {
            optimizer.optimize(problem);
            fail("Optimization should have failed due to non-differentiable function.");
        } catch (UnconstrainedOptimizationException e) {
            // Pass
        }

        // Test optimization with different initial guesses
        for (int i = 0; i < 5; i++) {
            PointValuePair solution = optimizer.optimize(problem);
            double x = solution.getPoint().getEntry(0);
            double fx = solution.getValue();
            double expected = Math.sqrt(2.0) / 2.0;
            assertEquals(expected, x, 1e-5);
            assertEquals(expected * expected, fx, 1e-5);
        }
    }

    /**
     * Test method for the {@link BaseOptimizer#optimize(UnivariateOptimizationProblem)} method
     * with a univariate optimization problem using a linear constraint.
     */
    @Test
    public void testOptimizeLinearConstraint() {
        BaseOptimizer<PointValuePair> optimizer = new SimpleBoundsMinimizer<>();

        UnivariateFunction function = new LinearFunction(1, 3);
        ConvergenceCriterion convergenceCriterion = new SimpleStoppingCriterion(1e-6);
        TerminationCondition terminationCondition = new TerminationCondition(convergenceCriterion);

        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(function, new SimpleBounds(0, 2), new LinearConstraint(1, 1));

        optimizer.setInitialStep(0.1);
        optimizer.setInitialSeed(new Random());
        optimizer.setInitialGuess(new Averaged(new ExtendedRealValuedRandomGenerator()));

        try {
            optimizer.optimize(problem, terminationCondition);
            fail("Optimization should have failed due to linear constraint violation.");
        } catch (UnconstrainedOptimizationException e) {
            // Pass
        }

        // Test optimization with different initial guesses that satisfy the linear constraint
        for (int i = 0; i < 5; i++) {
            optimizer.setInitialGuess(new RealVectorValue(new SingularValueDecomposition(new ValueVector(new double[]{0.5, 1.5})).getSolution().getColumnVector(0)));
            PointValuePair solution = optimizer.optimize(problem, terminationCondition);
            double x = solution.getPoint().getEntry(0);
            double fx = solution.getValue();
            double expected = 1.0 / 3.0;
            assertEquals(expected, x, 1e-5);
            assertEquals(expected * expected, fx, 1e-5);
        }
    }

    /**
     * Test method for the {@link BaseOptimizer#optimize(UnivariateOptimizationProblem)} method
     * with a univariate optimization problem using a custom convergence criterion.
     */
    @Test
    public void testOptimizeCustomConvergenceCriterion() {
        BaseOptimizer<PointValuePair> optimizer = new ConjugateGradient<>();

        UnivariateFunction function = new SimpleFunction();
        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(function, new SimpleBounds(0, 1), new SimpleGoalType(GoalType.MINIMIZE));

        ConvergenceCriterion convergenceCriterion = new CustomConvergenceCriterion(0.0001, 10);

        terminateOnConvergence(optimizer, problem, convergenceCriterion);

        PointValuePair solution = optimizer.getCurrentValue();
        assertEquals(Math.sqrt(2.0) / 2.0, solution.getPoint().getEntry(0), 1e-5);
        assertEquals(Math.pow(Math.sqrt(2.0) / 2.0, 2), solution.getValue(), 1e-5);
    }

    /**
     * Helper method to perform the optimization using a given optimizer, optimization problem, and convergence criterion.
     *
     * @param optimizer The optimizer to use for the test.
     * @param problem   The optimization problem to solve.
     * @param criterion The convergence criterion to use.
     */
    private void terminateOnConvergence(BaseOptimizer<PointValuePair> optimizer, UnivariateOptimizationProblem problem, ConvergenceCriterion criterion) {
        optimizer.optimize(problem, criterion);
        assertFalse("Optimization should have converged", criterion.isSatisfied());
    }
}

/**
 * Custom convergence criterion for the test.
 */
class CustomConvergenceCriterion implements ConvergenceCriterion {

    private final double tolerance;
    private int maxIterations;

    public CustomConvergenceCriterion(double tolerance, int maxIterations) {
        this.tolerance = tolerance;
        this.maxIterations = maxIterations;
    }

    @Override
    public boolean isSatisfied(UnivariateOptimizer<? extends PointValuePair> optimizer, PointValuePair currentPointValuePair, int iteration) {
        PointValuePair currentPointValue = currentPointValuePair;
        double currentValue = currentPointValue.getValue();
        double currentPoint = currentPointValue.getPoint().getEntry(0);

        double previousValue = optimizer.getCurrentValue().getValue();
        double previousPoint = optimizer.getCurrentValue().getPoint().getEntry(0);

        return Math.abs(currentValue - previousValue) < tolerance || iteration >= maxIterations;
    }
}

/**
 * Univariate function for testing.
 */
class SimpleFunction implements UnivariateFunction {
    @Override
    public double value(double x) {
        return Math.pow(Math.sin(x), 2);
    }

    @Override
    public RealVector gradient(double x) {
        return new RealVectorValue(new double[]{-2 * Math.sin(x) * Math.cos(x)});
    }
}

/**
 * Univariate optimization problem for testing.
 */
class SimpleOptimizationProblem implements UnivariateOptimizationProblem {
    private UnivariateFunction function;
    private GoalType goalType;
    private Bounds bounds;

    public SimpleOptimizationProblem(UnivariateFunction function, GoalType goalType, Bounds bounds) {
        this.function = function;
        this.goalType = goalType;
        this.bounds = bounds;
    }

    @Override
    public UnivariateFunction getObjectiveFunction() {
        return function;
    }

    @Override
    public GoalType getGoalType() {
        return goalType;
    }

    @Override
    public Bounds getBounds() {
        return bounds;
    }
}

/**
 * Bounds for the univariate optimization problem.
 */
class SimpleBounds implements Bounds {
    private double lowerBound;
    private double upperBound;

    public SimpleBounds(double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public double getLowerBound() {
        return lowerBound;
    }

    @Override
    public double getUpperBound() {
        return upperBound;
    }

    @Override
    public boolean includesPoint(double point) {
        return point >= lowerBound && point <= upperBound;
    }
}

/**
 * Linear constraint for the univariate optimization problem.
 */
class LinearConstraint implements Constraint {
    private double constant;
    private double coefficient;

    public LinearConstraint(double constant, double coefficient) {
        this.constant = constant;
        this.coefficient = coefficient;
    }

    @Override
    public boolean isSatisfied(RealVector x) {
        return x.getEntry(0) >= constant + coefficient * x.getEntry(0);
    }
}

/**
 * Goal type for the univariate optimization problem.
 */
enum SimpleGoalType implements GoalType {
    MINIMIZE;

    @Override
    public boolean isMinimization() {
        return this == MINIMIZE;
    }
}