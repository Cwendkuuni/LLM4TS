package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.initializerequired.NoInitialGuess;
import org.apache.commons.math3.optim.nonlinear.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.convergence.ConvergenceCriteria;
import org.apache.commons.math3.optim.nonlinear.convergence.SimpleLineSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunctionGrad;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunctionNDeriv;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BaseOptimizerTest<PAIR extends PointValuePair> {

    private int maxEvaluations;
    private int evaluations;
    private ConvergenceChecker<PAIR> convergenceChecker;

    public BaseOptimizerTest(int maxEvaluations, ConvergenceChecker<PAIR> convergenceChecker) {
        this.maxEvaluations = maxEvaluations;
        this.convergenceChecker = convergenceChecker;
    }

    @Parameterized.Parameters(name = "{index}: BaseOptimizer with maxEvaluations={0} and ConvergenceChecker={1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {100, new SimpleLineSearch()},
                {50, new ConvergenceCriteria(1e-6, 1e-6, 100)},
                {10, new NoInitialGuess()}
        });
    }

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = createMockOptimizer();
        assertEquals(maxEvaluations, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = createMockOptimizer();
        evaluations = optimizer.getEvaluations();
        optimizer.optimize(objectiveFunction, initialGuess, bounds, null, maxEvaluations, convergenceChecker);
        assertEquals(maxEvaluations + evaluations, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = createMockOptimizer();
        assertNull(optimizer.getConvergenceChecker());
        optimizer.setConvergenceChecker(convergenceChecker);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIllegalMethodCall() {
        BaseOptimizer<PAIR> optimizer = createMockOptimizer();
        optimizer.illegalMethodCall();
    }

    @Test
    public void testSetConvergenceCheckerNull() {
        BaseOptimizer<PAIR> optimizer = createMockOptimizer();
        try {
            optimizer.setConvergenceChecker(null);
        } catch (NullPointerException e) {
            // expected
        }
    }

    private BaseOptimizer<PAIR> createMockOptimizer() {
        return new MockBaseOptimizer<PAIR>(maxEvaluations, convergenceChecker);
    }

    private ObjectiveFunction<Double> objectiveFunction = new MockObjectiveFunction();
    private InitialGuess<Double> initialGuess = new MockInitialGuess();
    private SimpleBounds<Double> bounds = new SimpleBounds<>(-1.0, 1.0);
}

class MockBaseOptimizer<PAIR> implements BaseOptimizer<PAIR> {
    private int maxEvaluations;
    private int evaluations;
    private ConvergenceChecker<PAIR> convergenceChecker;

    public MockBaseOptimizer(int maxEvaluations, ConvergenceChecker<PAIR> convergenceChecker) {
        this.maxEvaluations = maxEvaluations;
        this.convergenceChecker = convergenceChecker;
    }

    @Override
    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    @Override
    public int getEvaluations() {
        return evaluations;
    }

    @Override
    public ConvergenceChecker<PAIR> getConvergenceChecker() {
        return convergenceChecker;
    }

    @Override
    public void setConvergenceChecker(ConvergenceChecker<PAIR> convergenceChecker) {
        this.convergenceChecker = convergenceChecker;
    }

    public void illegalMethodCall() {
        // method does not exist
    }
}

class MockObjectiveFunction implements ObjectiveFunction<Double>, ObjectiveFunctionGrad<Double>, ObjectiveFunctionNDeriv<Double> {
    @Override
    public Double value(Double point) {
        return 0.0;
    }

    @Override
    public Double value(Double point, double[] gradient) {
        return 0.0;
    }

    @Override
    public Double value(Double point, double[][] hessian) {
        return 0.0;
    }
}

class MockInitialGuess implements InitialGuess<Double> {
    @Override
    public Double getInitialGuess() {
        return null;
    }
}