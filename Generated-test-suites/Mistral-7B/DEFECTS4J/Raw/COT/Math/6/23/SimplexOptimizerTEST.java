import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariateFunctionAdapter;
import org.apache.commons.math3.optimization.direct.AbstractSimplex;
import org.apache.commons.math3.optimization.direct.SimplexOptimizer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class SimplexOptimizerTest {

    @Test
    public void testNullConstructor() {
        try {
            new SimplexOptimizer();
            Assert.fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorWithDefaultChecker() {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        Assert.assertSame(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testConstructorWithRelativeAndAbsoluteTolerance() {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1.0e-5, 1.0e-5);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        Assert.assertSame(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testSetSimplex() {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        AbstractSimplex simplex = new SimpleX();
        SimplexOptimizer optimizer = new SimplexOptimizer();

        optimizer.setSimplex(simplex);
        Assert.assertSame(simplex, optimizer.getSimplex());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetSimplexNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(null);
    }

    @Test
    public void testOptimizeMinimize() throws Exception {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        MultivariateFunction f = new MultivariateFunctionAdapter(new SineFunction());

        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair opt = optimizer.optimize(100, f, goalType, startPoint);
        Assert.assertEquals(0.0, opt.getValue(), 1e-5);
    }

    @Test
    public void testOptimizeMaximize() throws Exception {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        MultivariateFunction f = new MultivariateFunctionAdapter(new SineFunction());

        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        GoalType goalType = GoalType.MAXIMIZE;
        PointValuePair opt = optimizer.optimize(100, f, goalType, startPoint);
        Assert.assertEquals(Math.PI, opt.getValue(), 1e-5);
    }

    @Test
    public void testOptimizeWithInitialGuess() throws Exception {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        MultivariateFunction f = new MultivariateFunctionAdapter(new SineFunction());

        double[] startPoint = new double[]{Math.PI / 4, Math.PI / 4, Math.PI / 4};
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData optimizationData = new OptimizationData();
        optimizationData.setInitialGuess(startPoint);
        PointValuePair opt = optimizer.optimize(100, f, goalType, optimizationData);
        Assert.assertEquals(0.0, opt.getValue(), 1e-5);
    }

    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullFunction() throws Exception {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        MultivariateFunction f = null;

        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair opt = optimizer.optimize(100, f, goalType, startPoint);
        Assert.fail("Expected NullArgumentException");
    }

    @Test
    public void testOptimizeNullInitialGuess() throws Exception {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        MultivariateFunction f = new MultivariateFunctionAdapter(new SineFunction());

        double[] startPoint = null;
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair opt = optimizer.optimize(100, f, goalType, startPoint);
        Assert.fail("Expected NullArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeInvalidGoalType() throws Exception {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        MultivariateFunction f = new MultivariateFunctionAdapter(new SineFunction());

        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        GoalType goalType = GoalType.INDEFINITE;
        PointValuePair opt = optimizer.optimize(100, f, goalType, startPoint);
        Assert.fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithInvalidMaxEval() throws Exception {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        MultivariateFunction f = new MultivariateFunctionAdapter(new SineFunction());

        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        GoalType goalType = GoalType.MINIMIZE;
        int maxEval = 0;
        PointValuePair opt = optimizer.optimize(maxEval, f, goalType, startPoint);
        Assert.fail("Expected IllegalArgumentException");
    }

    private static class SimpleX extends AbstractSimplex {
        @Override
        public void build(double[] point) {
            // Do nothing.
        }

        @Override
        public void evaluate(MultivariateFunction function, Comparator<PointValuePair> comparator) {
            // Do nothing.
        }

        @Override
        public void iterate(MultivariateFunction function, Comparator<PointValuePair> comparator) {
            // Do nothing.
        }
    }

    private static class SineFunction implements MultivariateFunction {
        public double value(double[] point) {
            double sum = 0.0;
            for (double x : point) {
                sum += Math.sin(x);
            }
            return sum;
        }
    }
}