import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.BaseUnivariateRealOptimizer;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.NonSmartPointEvaluator;
import org.apache.commons.math3.optim.PointValuePairList;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class MultiStartUnivariateRealOptimizerTest<FUNC extends UnivariateRealFunction> {

    private MultiStartUnivariateRealOptimizer<FUNC> optimizer;
    private BaseUnivariateRealOptimizer<FUNC> wrappedOptimizer;
    private FUNC testFunction;
    private UnivariateRealPointValuePair expectedOptima;
    private UnivariateRealPointValuePair[] actualOptima;
    private RandomGenerator random;

    @Before
    public void setUp() {
        testFunction = new MyTestFunction();
        wrappedOptimizer = new FakeUnivariateRealOptimizer();
        optimizer = new MultiStartUnivariateRealOptimizer<>(wrappedOptimizer, 10, new MersenneTwister());
        optimizer.setConvergenceChecker(new MaxEval(1000));
    }

    @Test
    public void testBasicFunctionality() throws FunctionEvaluationException, ConvergenceException {
        GoalType goal = GoalType.MINIMIZE;
        double min = 0;
        double max = 10;

        UnivariateRealPointValuePair result = optimizer.optimize(testFunction, goal, min, max);
        Assert.assertNotNull(result);
        Assert.assertEquals(goal, result.getGoalType());
        Assert.assertTrue(result.getValue() >= 0);

        actualOptima = optimizer.getOptima();
        Assert.assertEquals(10, actualOptima.length);
        for (UnivariateRealPointValuePair optima : actualOptima) {
            Assert.assertNotNull(optima);
            Assert.assertEquals(goal, optima.getGoalType());
            Assert.assertTrue(optima.getValue() >= 0);
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testNoConvergenceWithAnyStartPoint() throws FunctionEvaluationException, ConvergenceException {
        GoalType goal = GoalType.MINIMIZE;
        double min = 0;
        double max = 10;

        optimizer = new MultiStartUnivariateRealOptimizer<>(wrappedOptimizer, 1, new MersenneTwister());
        optimizer.optimize(testFunction, goal, min, max);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws FunctionEvaluationException, ConvergenceException {
        GoalType goal = GoalType.MINIMIZE;
        double min = 0;
        double max = 10;

        MyThrowingTestFunction testFunction = new MyThrowingTestFunction();
        optimizer = new MultiStartUnivariateRealOptimizer<>(wrappedOptimizer, 10, new MersenneTwister());
        optimizer.setConvergenceChecker(new MaxEval(100));

        optimizer.optimize(testFunction, goal, min, max);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testNoOptimizationCallYet() throws FunctionEvaluationException, ConvergenceException {
        optimizer.getOptima();
    }

    static class FakeUnivariateRealOptimizer implements BaseUnivariateRealOptimizer<FUNC> {
        @Override
        public UnivariateRealPointValuePair optimize(FUNC f, GoalType goal, double min, double max)
                throws FunctionEvaluationException, ConvergenceException {
            return new PointValuePair(0.5, 0.5);
        }

        @Override
        public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {}

        @Override
        public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
            return null;
        }

        @Override
        public int getMaxEvaluations() {
            return 100;
        }

        @Override
        public int getEvaluations() {
            return 0;
        }

        @Override
        public void setMaxEvaluations(int maxEvaluations) {}
    }

    static class MyTestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2);
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }
    }

    static class MyThrowingTestFunction extends MyTestFunction {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            if (x > 5) {
                throw new FunctionEvaluationException(LocalizedFormats.FUNCTION_EVALUATION_FAILED, x);
            }
            return super.value(x);
        }
    }
}