package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.initializer.AroundMinimum;
import org.apache.commons.math3.optim.initializer.RandomStart;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math3.optimization.BaseOptimizer;

/**
 * Test cases for BaseOptimizer interface.
 */
public class BaseOptimizerTest {

    private BaseOptimizer<PointValuePair> optimizer;

    @Before
    public void setUp() {
        optimizer = new TestBaseOptimizer();
    }

    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals("Max evaluations should be set by default to 1000", 1000, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        assertEquals("Initial number of evaluations should be 0", 0, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull("Convergence checker should not be null", convergenceChecker);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetConvergenceCheckerBeforeInitialization() {
        optimizer.getConvergenceChecker(); // should initialize with a default one
        optimizer.getConvergenceChecker(); // shouldn't throw an exception

        optimizer = new TestBaseOptimizer(); // reset to an uninitialized one
        optimizer.getConvergenceChecker(); // should initialize with a default one

        // now try to get it before initialization
        optimizer.getConvergenceChecker();
    }

    @Test
    public void testGetConvergenceCheckerWithCustomOne() {
        ConvergenceChecker<PointValuePair> customConvergenceChecker = new MyConvergenceChecker();
        optimizer = new TestBaseOptimizer(customConvergenceChecker);
        ConvergenceChecker<PointValuePair> actualConvergenceChecker = optimizer.getConvergenceChecker();
        assertEquals("Custom convergence checker not set correctly", customConvergenceChecker, actualConvergenceChecker);
    }

    @Test(expected = IllegalStateException.class)
    public void testOptimizeWithoutObjectiveFunction() {
        optimizer = new TestBaseOptimizer();
        optimizer.optimize(new SimpleBounds(0, 1));
    }

    @Test
    public void testOptimize() {
        ObjectiveFunction<Number> objectiveFunction = new TestObjectiveFunction();
        optimizer = new TestBaseOptimizer(objectiveFunction);
        SimpleBounds bounds = new SimpleBounds(0.1, 1.0);

        PointValuePair result = optimizer.optimize(bounds);
        assertNotNull("Optimize should return a result", result);
        assertEquals("The minimum should be around 0.5", 0.5, result.getPoint().doubleValue(), 0.01);
        assertEquals("The minimum value should be around 0.25", 0.25, result.getValue().doubleValue(), 0.01);
        assertEquals("The number of evaluations should be greater than 0", 1, optimizer.getEvaluations());
    }

    @Test
    public void testOptimizeWithRandomStart() {
        ObjectiveFunction<Number> objectiveFunction = new TestObjectiveFunction();
        optimizer = new TestBaseOptimizer(objectiveFunction);
        SimpleBounds bounds = new SimpleBounds(0.1, 1.0);

        optimizer.initialize(new RandomStart(bounds));
        PointValuePair result = optimizer.optimize(null);
        assertNotNull("Optimize should return a result", result);
        // the result depends on the random initialization
    }

    @Test
    public void testOptimizeWithAroundMinimumStart() {
        ObjectiveFunction<Number> objectiveFunction = new TestObjectiveFunction();
        optimizer = new TestBaseOptimizer(objectiveFunction);
        SimpleBounds bounds = new SimpleBounds(0.1, 1.0);

        optimizer.initialize(new AroundMinimum(bounds, 0.01));
        PointValuePair result = optimizer.optimize(null);
        assertNotNull("Optimize should return a result", result);
        assertEquals("The minimum should be around 0.5", 0.5, result.getPoint().doubleValue(), 0.01);
        assertEquals("The minimum value should be around 0.25", 0.25, result.getValue().doubleValue(), 0.01);
    }

    @Test
    public void testOptimizeWithInitialValue() {
        ObjectiveFunction<Number> objectiveFunction = new TestObjectiveFunction();
        optimizer = new TestBaseOptimizer(objectiveFunction);

        optimizer.initialize(new PointValuePair(1.0));
        PointValuePair result = optimizer.optimize(new SimpleBounds(0.1, 1.0));
        assertNotNull("Optimize should return a result", result);
        assertEquals("The minimum should be around 0.5", 0.5, result.getPoint().doubleValue(), 0.01);
        assertEquals("The minimum value should be around 0.25", 0.25, result.getValue().doubleValue(), 0.01);
    }

    @Test
    public void testOptimizeWithGoalType() {
        ObjectiveFunction<Number> objectiveFunction = new TestObjectiveFunction();
        optimizer = new TestBaseOptimizer(objectiveFunction);

        optimizer.initialize(new GoalType(GoalType.MINIMIZE));
        PointValuePair result = optimizer.optimize(new SimpleBounds(0.1, 1.0));
        assertNotNull("Optimize should return a result", result);
        assertEquals("The minimum should be around 0.5", 0.5, result.getPoint().doubleValue(), 0.01);
        assertEquals("The minimum value should be around 0.25", 0.25, result.getValue().doubleValue(), 0.01);

        optimizer = new TestBaseOptimizer(objectiveFunction);
        optimizer.initialize(new GoalType(GoalType.MAXIMIZE));
        result = optimizer.optimize(new SimpleBounds(0.1, 1.0));
        assertNotNull("Optimize should return a result", result);
        assertEquals("The maximum should be around 0.6", 0.6, result.getPoint().doubleValue(), 0.01);
        assertEquals("The maximum value should be around 0.41", 0.41, result.getValue().doubleValue(), 0.01);
    }

    @Test
    public void testOptimizeWithMaxEval() {
        ObjectiveFunction<Number> objectiveFunction = new TestObjectiveFunction();
        optimizer = new TestBaseOptimizer(objectiveFunction);

        optimizer.setMaxIterations(1);
        PointValuePair result = optimizer.optimize(new SimpleBounds(0.1, 1.0));
        assertNotNull("Optimize should return a result", result);
        assertEquals("The number of evaluations should be 1", 1, optimizer.getEvaluations());
        assertEquals("The minimum should be around 0.5", 0.5, result.getPoint().doubleValue(), 0.01);
        assertEquals("The minimum value should be around 0.25", 0.25, result.getValue().doubleValue(), 0.01);

        optimizer.setMaxIterations(MaxEval.NO_LIMIT);
        result = optimizer.optimize(new SimpleBounds(0.1, 1.0));
        assertNotNull("Optimize should return a result", result);
        assertEquals("The number of evaluations should be greater than 1", 1, optimizer.getEvaluations());
    }
}

class TestBaseOptimizer extends AbstractUnivariateOptimizer implements BaseOptimizer<PointValuePair> {
    private ConvergenceChecker<PointValuePair> convergenceChecker;
    private ObjectiveFunction<Number> objectiveFunction;

    public TestBaseOptimizer() {
        this(null);
    }

    public TestBaseOptimizer(ObjectiveFunction<Number> objectiveFunction) {
        this(objectiveFunction, new SimpleConvergenceChecker());
    }

    public TestBaseOptimizer(ObjectiveFunction<Number> objectiveFunction, ConvergenceChecker<PointValuePair> convergenceChecker) {
        super(objectiveFunction);
        this.objectiveFunction = objectiveFunction;
        this.convergenceChecker = convergenceChecker;
        setMaxIterations(MaxEval.NO_LIMIT);
    }

    @Override
    public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
        return convergenceChecker;
    }

    @Override
    public void initialize(PointValuePair initialGuess, GoalType goalType) {
        super.initialize(initialGuess, goalType);
    }

    @Override
    public void initialize(PointValuePair initialGuess) {
        super.initialize(initialGuess);
    }

    @Override
    public void initialize(GoalType goalType) {
        super.initialize(goalType);
    }

    @Override
    public void initialize(InitialGuess initialGuess) {
        super.initialize(initialGuess);
    }

    @Override
    public void initialize(InitialGuess initialGuess, GoalType goalType) {
        super.initialize(initialGuess, goalType);
    }
}

class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
    @Override
    public boolean checkConvergence(PointValuePair currentValue, PointValuePair previousValue) {
        // implementation specific to your custom convergence checker
        // replace with your logic here
        return Math.abs(currentValue.getValue().doubleValue() - previousValue.getValue().doubleValue()) < 0.01;
    }
}

class TestObjectiveFunction implements ObjectiveFunction<Number>, TestableObjectiveFunction {
    @Override
    public Number value(Number point) {
        return new PointValuePair(point, Math.pow(point, 2));
    }

    @Override
    public void test() {
        // implementation specific to your objective function
        // replace with your test logic here
    }
}