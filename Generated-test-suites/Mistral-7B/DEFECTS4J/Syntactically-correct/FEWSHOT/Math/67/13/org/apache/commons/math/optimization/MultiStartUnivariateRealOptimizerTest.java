package org.apache.commons.cli.optimization; // make sure to import all necessary packages

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import org.apache.commons.math.analysis.FunctionUtils;
import org.apache.commons.math.analysis.PolynomialFunction;
import org.apache.commons.math.optim.GoalType;
import org.apache.commons.math.optim.InitialGuess;
import org.apache.commons.math.optim.MaxEval;
import org.apache.commons.math.optim.NonConvergentException;
import org.apache.commons.math.optim.PointValuePair;
import org.apache.commons.math.optim.SimpleBounds;
import org.apache.commons.math.optim.UnivariateOptimizer;
import org.apache.commons.math.optim.nonlinear.AbstractUnivariateFunction;
import org.apache.commons.math.optim.nonlinear.convergence.SimpleStoppingCondition;
import org.apache.commons.math.random.MersenneTwister;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateOptimizer optimizer;
    private MultiStartUnivariateRealOptimizer multiStartOptimizer;
    private int maxIterations;
    private int maxEvaluations;
    private int starts;
    private double min;
    private double max;

    public MultiStartUnivariateRealOptimizerTest(int maxIterations, int maxEvaluations, int starts, double min, double max) {
        this.optimizer = neworg.apache.commons.math.optim.SimpleXPSOptimizer(new SimpleBounds(min, max));
        this.multiStartOptimizer = newMultiStartUnivariateRealOptimizer(optimizer, starts, new MersenneTwister(System.nanoTime()));
        this.maxIterations = maxIterations;
        this.maxEvaluations = maxEvaluations;
        this.starts = starts;
        this.min = min;
        this.max = max;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{10, 10, 3, -5.0, 5.0},
                new Object[]{10, 15, 3, -5.0, 5.0},
                new Object[]{10, 20, 3, -5.0, 5.0},
                new Object[]{10, 25, 3, -5.0, 5.0},
                new Object[]{10, 30, 3, -5.0, 5.0},
                new Object[]{100, 10, 3, -5.0, 5.0},
                new Object[]{100, 15, 3, -5.0, 5.0},
                new Object[]{100, 20, 3, -5.0, 5.0},
                new Object[]{100, 25, 3, -5.0, 5.0},
                new Object[]{100, 30, 3, -5.0, 5.0},
                new Object[]{10, 10, 5, -5.0, 5.0},
                new Object[]{10, 10, 10, -5.0, 5.0},
                new Object[]{10, 10, 15, -5.0, 5.0},
                new Object[]{10, 10, 20, -5.0, 5.0},
                new Object[]{10, 10, 25, -5.0, 5.0},
                new Object[]{10, 10, 30, -5.0, 5.0},
                new Object[]{100, 10, 5, -5.0, 5.0},
                new Object[]{100, 10, 10, -5.0, 5.0},
                new Object[]{100, 10, 15, -5.0, 5.0},
                new Object[]{100, 10, 20, -5.0, 5.0},
                new Object[]{100, 10, 25, -5.0, 5.0},
                new Object[]{100, 10, 30, -5.0, 5.0},
        });
    }

    @Test
    public void testOptimize() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = this.multiStartOptimizer;
        GoalType goalType = GoalType.MINIMIZE;
        PolynomialFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0}, 3);
        double x = optimizer.optimize(f, goalType, this.min, this.max);
        PointValuePair pvp = optimizer.getOptima();
        double[] optima = pvp.getPoint();
        double[] optimaValues = pvp.getValue();

        assertEquals(this.starts, pvp.getNumberOfPoints());
        for (int i = 0; i < pvp.getNumberOfPoints(); i++) {
            assertNotNull(optima[i]);
            assertNotNull(optimaValues[i]);
            assertEquals(goalType, pvp.getGoalType());
            assertEquals(this.min, optima[i] - FunctionUtils.value(f, optima[i], null), 1e-9);
            assertSame(goalType, pvp.getGoalType());
        }

        assertEquals(this.maxIterations, optimizer.getMaximalIterationCount());
        assertEquals(this.maxEvaluations, optimizer.getMaxEvaluations());
        assertEquals(this.maxEvaluations, optimizer.getEvaluations());
        assertEquals(this.maxIterations, optimizer.getIterationCount());
    }

    @Test(expected = ConvergenceException.class)
    public void testNoConvergence() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = this.multiStartOptimizer;
        GoalType goalType = GoalType.MINIMIZE;
        PolynomialFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 3);
        optimizer.optimize(f, goalType, this.min, this.max);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = this.multiStartOptimizer;
        GoalType goalType = GoalType.MINIMIZE;
        PolynomialFunction f = new PolynomialFunction(new double[]{Double.NaN}, 0);
        optimizer.optimize(f, goalType, this.min, this.max);
    }

    @Test(expected = IllegalStateException.class)
    public void testNoOptimaComputedYet() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = this.multiStartOptimizer;
        double[] optima = optimizer.getOptima();
    }

    @Test
    public void testGetOptimaValues() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = this.multiStartOptimizer;
        GoalType goalType = GoalType.MINIMIZE;
        PolynomialFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0}, 3);
        double x = optimizer.optimize(f, goalType, this.min, this.max);
        PointValuePair pvp = optimizer.getOptima();
        double[] optimaValues = pvp.getValue();

        for (int i = 0; i < pvp.getNumberOfPoints(); i++) {
            assertEquals(FunctionUtils.value(f, optima[i], null), optimaValues[i], 1e-9);
        }
    }

    @Test
    public void testSetMaxIterations() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = this.multiStartOptimizer;
        optimizer.setMaximalIterationCount(this.maxIterations * 2);
        assertEquals(this.maxIterations * 2, optimizer.getMaximalIterationCount());
    }

    @Test
    public void testSetMaxEvaluations() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = this.multiStartOptimizer;
        optimizer.setMaxEvaluations(this.maxEvaluations * 2);
        assertEquals(this.maxEvaluations * 2, optimizer.getMaxEvaluations());
    }

    @Test
    public void testResetMaxIterations() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = this.multiStartOptimizer;
        optimizer.setMaximalIterationCount(this.maxIterations * 2);
        optimizer.resetMaximalIterationCount();
        assertEquals(this.maxIterations, optimizer.getMaximalIterationCount());
    }

    @Test
    public void testResetMaxEvaluations() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = this.multiStartOptimizer;
        optimizer.setMaxEvaluations(this.maxEvaluations * 2);
        optimizer.resetMaxEvaluations();
        assertEquals(this.maxEvaluations, optimizer.getMaxEvaluations());
    }

    @Test
    public void testSetAbsoluteAccuracy() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = this.multiStartOptimizer;
        optimizer.setAbsoluteAccuracy(1e-5);
        assertEquals(1e-5, optimizer.getAbsoluteAccuracy(), 1e-9);
    }

    @Test
    public void testSetRelativeAccuracy() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = this.multiStartOptimizer;
        optimizer.setRelativeAccuracy(1e-5);
        assertEquals(1e-5, optimizer.getRelativeAccuracy(), 1e-9);
    }

    @Test
    public void testResetRelativeAccuracy() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = this.multiStartOptimizer;
        optimizer.setRelativeAccuracy(1e-5);
        optimizer.resetRelativeAccuracy();
        assertEquals(0.0, optimizer.getRelativeAccuracy(), 1e-9);
    }

    @Test
    public void testResetAbsoluteAccuracy() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = this.multiStartOptimizer;
        optimizer.setAbsoluteAccuracy(1e-5);
        optimizer.resetAbsoluteAccuracy();
        assertEquals(0.0, optimizer.getAbsoluteAccuracy(), 1e-9);
    }

    @Test
    public void testSetGoalType() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = this.multiStartOptimizer;
        optimizer.setGoalType(GoalType.MAXIMIZE);
        assertEquals(GoalType.MAXIMIZE, optimizer.getGoalType());
    }

    @Test
    public void testResetGoalType() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = this.multiStartOptimizer;
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.resetGoalType();
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
    }

    @Test
    public void testSetInitialGuess() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = this.multiStartOptimizer;
        InitialGuess initialGuess = new InitialGuess(new SimpleBounds(this.min, this.max));
        optimizer.setInitialGuess(initialGuess);
        assertEquals(initialGuess, optimizer.getInitialGuess());
    }

    @Test
    public void testResetInitialGuess() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = this.multiStartOptimizer;
        InitialGuess initialGuess = new InitialGuess(new SimpleBounds(this.min, this.max));
        optimizer.setInitialGuess(initialGuess);
        optimizer.resetInitialGuess();
        assertNull(optimizer.getInitialGuess());
    }

    @Test
    public void testSetStoppingCondition() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = this.multiStartOptimizer;
        SimpleStoppingCondition stoppingCondition = new SimpleStoppingCondition(10);
        optimizer.setStoppingCondition(stoppingCondition);
        assertEquals(stoppingCondition, optimizer.getStoppingCondition());
    }

    @Test
    public void testResetStoppingCondition() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = this.multiStartOptimizer;
        SimpleStoppingCondition stoppingCondition = new SimpleStoppingCondition(10);
        optimizer.setStoppingCondition(stoppingCondition);
        optimizer.resetStoppingCondition();
        assertNull(optimizer.getStoppingCondition());
    }

    @Test
    public void testSetMaxEvalPerIteration() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = this.multiStartOptimizer;
        MaxEval maxEval = new MaxEval(10);
        optimizer.setMaxEvalPerIteration(maxEval);
        assertEquals(maxEval, optimizer.getMaxEvalPerIteration());
    }

    @Test
    public void testResetMaxEvalPerIteration() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = this.multiStartOptimizer;
        MaxEval maxEval = new MaxEval(10);
        optimizer.setMaxEvalPerIteration(maxEval);
        optimizer.resetMaxEvalPerIteration();
        assertNull(optimizer.getMaxEvalPerIteration());
    }
}