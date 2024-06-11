package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeConvergenceCriterion;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleBounds;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartOptimizerMinimization() {
        UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new FminBrent(), 5, new Random());
        double[] functionValues = { 1.0, 0.5, 0.2, 0.1, 0.05 };
        PolynomialFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0, 1.0, -1.0}, 5);

        double x = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        Assert.assertEquals(0.0, x, 1e-6);
        double fval = optimizer.getFunctionValue();
        Assert.assertEquals(0.0, fval, 1e-6);
        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();

        for (int i = 0; i < 5; ++i) {
            Assert.assertEquals(optimaValues[i], functionValues[i], 1e-6);
        }
    }

    @Test
    public void testMultiStartOptimizerMaximization() {
        UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new FminBrent(), 5, new Random());
        double[] functionValues = { 1.0, 1.5, 2.0, 2.5, 3.0 };
        PolynomialFunction f = new PolynomialFunction(new double[]{-1.0, -1.0, 1.0, 1.0, 1.0}, 5);

        double x = optimizer.optimize(f, GoalType.MAXIMIZE, -10.0, 10.0);
        Assert.assertEquals(3.0, x, 1e-6);
        double fval = optimizer.getFunctionValue();
        Assert.assertEquals(3.0, fval, 1e-6);
        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();

        for (int i = 0; i < 5; ++i) {
            Assert.assertEquals(optimaValues[i], functionValues[i], 1e-6);
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testMultiStartOptimizerNoConvergence() {
        UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new FminBrent(), 5, new Random());
        PolynomialFunction f = new PolynomialFunction(new double[]{1.0, -1.0, 1.0, -1.0, 1.0}, 5);

        optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testMultiStartOptimizerFunctionEvaluationException() {
        UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new FminBrent(), 5, new Random()) {
            @Override
            protected void initializeSearch() throws FunctionEvaluationException {
                throw new FunctionEvaluationException("Test FunctionEvaluationException");
            }
        };
        PolynomialFunction f = new PolynomialFunction(new double[]{1.0, 1.0, 1.0, 1.0, 1.0}, 5);

        optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
    }

    @Test(expected = MathRuntimeException.class)
    public void testMultiStartOptimizerIllegalArgumentException() {
        UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 5, new Random());
        PolynomialFunction f = new PolynomialFunction(new double[]{1.0, 1.0, 1.0, 1.0, 1.0}, 5);

        optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
    }

    @Test
    public void testMultiStartOptimizerIterationCount() {
        UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new FminBrent(), 5, new Random());
        PolynomialFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0, 1.0, -1.0}, 5);

        optimizer.setMaxIterations(5);
        optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);

        int iterations = optimizer.getIterationCount();
        Assert.assertEquals(10 * 5, iterations);
    }

    @Test
    public void testMultiStartOptimizerEvaluations() {
        UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new FminBrent(), 5, new Random());
        PolynomialFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0, 1.0, -1.0}, 5);

        optimizer.setMaxEvaluations(5);
        optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);

        int evaluations = optimizer.getEvaluations();
        Assert.assertEquals(10 * 5, evaluations);
    }

    @Test
    public void testMultiStartOptimizerGoalType() {
        UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new FminBrent(), 5, new Random());

        optimizer.optimize(new PolynomialFunction(new double[]{1.0, -1.0, 1.0, -1.0, 1.0}, 5), GoalType.MINIMIZE, -10.0, 10.0);
        optimizer.optimize(new PolynomialFunction(new double[]{-1.0, 1.0, -1.0, 1.0, -1.0}, 5), GoalType.MAXIMIZE, -10.0, 10.0);
    }

    @Test
    public void testMultiStartOptimizerCustomGoalType() {
        UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new FminBrent(), 5, new Random());

        GoalTypeConvergenceCriterion gtc = new GoalTypeConvergenceCriterion(new SimpleBounds(0.01, 0.01), 1e-10, 50);
        optimizer.optimize(new PolynomialFunction(new double[]{1.0, -1.0, 1.0, -1.0, 1.0}, 5), gtc, -10.0, 10.0);
    }

    @Test
    public void testMultiStartOptimizerCustomInitializer() {
        UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new FminBrent(), 5, new Random());

        AroundMinimumInitializer initializer = new AroundMinimumInitializer(new PolynomialFunction(new double[]{1.0, -1.0, 1.0, -1.0, 1.0}, 5), 1.0);
        optimizer.setInitializer(initializer);
        optimizer.optimize(new PolynomialFunction(new double[]{1.0, -1.0, 1.0, -1.0, 1.0}, 5), GoalType.MINIMIZE, -10.0, 10.0);

        double[] initialGuesses = initializer.getInitialPoints();
        for (double initialGuess : initialGuesses) {
            Assert.assertFalse(Double.isNaN(initialGuess));
        }
    }
}