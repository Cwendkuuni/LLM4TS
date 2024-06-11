import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.BaseUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealPointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeCriteria;
import org.apache.commons.math3.optim.nonlinear.scalar.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.univariate.AmoebaOptimizer;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test MultiStartUnivariateRealOptimizer
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Testing a simple function: f(x) = x^2, with goal type MINIMIZE
     * @throws FunctionEvaluationException
     * @throws ConvergenceException
     */
    @Test
    public void testSimpleMinimize() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;

        BaseUnivariateRealOptimizer optimizer = new AmoebaOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister());

        multiStartOptimizer.optimize(function, goalType, min, max);

        UnivariateRealPointValuePair bestOptim = multiStartOptimizer.getOptima()[0];
        Assert.assertNotNull(bestOptim);
        Assert.assertEquals(min, bestOptim.getPoint(), 1e-8);
        Assert.assertEquals(0.0, bestOptim.getValue(), 1e-8);

        for (UnivariateRealPointValuePair pair : multiStartOptimizer.getOptima()) {
            if (pair == null) {
                continue;
            }
            Assert.assertFalse(pair.getPoint() == bestOptim.getPoint());
            Assert.assertEquals(goalType, pair.getGoalType());
            Assert.assertEquals(goalType, bestOptim.getGoalType());
            Assert.assertTrue(pair.getValue() > bestOptim.getValue());
        }

        MaxEval maxEval = optimizer.getMaxEvaluations();
        int totalEvaluations = multiStartOptimizer.getEvaluations();
        Assert.assertEquals(maxEval.getValue() * 5, totalEvaluations);
    }

    /**
     * Testing a simple function: f(x) = x^2, with goal type MAXIMIZE
     * @throws FunctionEvaluationException
     * @throws ConvergenceException
     */
    @Test
    public void testSimpleMaximize() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0);
            }
        };
        GoalType goalType = GoalType.MAXIMIZE;
        double min = -10.0;
        double max = 10.0;

        BaseUnivariateRealOptimizer optimizer = new AmoebaOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister());

        multiStartOptimizer.optimize(function, goalType, min, max);

        UnivariateRealPointValuePair bestOptim = multiStartOptimizer.getOptima()[0];
        Assert.assertNotNull(bestOptim);
        Assert.assertEquals(max, bestOptim.getPoint(), 1e-8);
        Assert.assertEquals(0.0, bestOptim.getValue(), 1e-8);

        for (UnivariateRealPointValuePair pair : multiStartOptimizer.getOptima()) {
            if (pair == null) {
                continue;
            }
            Assert.assertFalse(pair.getPoint() == bestOptim.getPoint());
            Assert.assertEquals(goalType, pair.getGoalType());
            Assert.assertEquals(goalType, bestOptim.getGoalType());
            Assert.assertTrue(pair.getValue() < bestOptim.getValue());
        }

        MaxEval maxEval = optimizer.getMaxEvaluations();
        int totalEvaluations = multiStartOptimizer.getEvaluations();
        Assert.assertEquals(maxEval.getValue() * 5, totalEvaluations);
    }

    /**
     * Testing a non-converging function: f(x) = |x|, with goal type MINIMIZE
     * @throws FunctionEvaluationException
     * @throws ConvergenceException
     */
    @Test(expected = ConvergenceException.class)
    public void testNonConvergingMinimize() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.abs(x);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;

        BaseUnivariateRealOptimizer optimizer = new AmoebaOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister());

        multiStartOptimizer.optimize(function, goalType, min, max);
    }

    /**
     * Testing a non-converging function: f(x) = |x|, with goal type MAXIMIZE
     * @throws FunctionEvaluationException
     * @throws ConvergenceException
     */
    @Test(expected = ConvergenceException.class)
    public void testNonConvergingMaximize() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.abs(x);
            }
        };
        GoalType goalType = GoalType.MAXIMIZE;
        double min = -10.0;
        double max = 10.0;

        BaseUnivariateRealOptimizer optimizer = new AmoebaOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister());

        multiStartOptimizer.optimize(function, goalType, min, max);
    }
}