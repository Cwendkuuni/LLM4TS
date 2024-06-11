import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.goal.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeChecker;
import org.apache.commons.math3.optim.nonlinear.vector.function.AbstractMultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.vector.function.UnivariateFunctionAdapter;
import org.apache.commons.math3.optim.point.PointValuePairCollection;
import org.apache.commons.math3.optim.restart.RestartStrategy;
import org.apache.commons.math3.optim.restart.ExponentialRestart;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.optim.PointValueType.MINIMIZE;

/**
 * Test class for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {

    private static final double DELTA = 1.0e-6;

    /**
     * Test function for SimplexOptimizer.
     */
    private static class TestFunction extends AbstractMultivariateFunction {
        @Override
        public double value(double[] x) {
            double sum = 0.0;
            for (int i = 0; i < x.length; i++) {
                sum += Math.pow(x[i] - 1., 2.);
            }
            return sum;
        }

        @Override
        public int getNumberOfVariables() {
            return x.length;
        }
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[])}.
     */
    @Test
    public void testOptimize() {
        TestFunction testFunction = new TestFunction();
        RestartStrategy strategy = new ExponentialRestart(new int[]{10, 100, 1000});

        PointValuePairCollection solutions = new PointValuePairCollection();

        // Test minimize
        SimplexOptimizer optimizer = new SimplexOptimizer(MINIMIZE, DELTA, DELTA);
        optimizer.setMaxIterations(10);
        optimizer.setConvergenceChecker(new SimpleValueChecker(DELTA, DELTA));
        optimizer.setRestartStrategy(strategy);

        double[] startPoint = new double[]{1.5, 1.5, 1.5};
        PointValuePair solution = optimizer.optimize(testFunction, startPoint);
        solutions.add(solution);

        optimizer.setMaxIterations(5);
        solution = optimizer.optimize(testFunction, startPoint);
        solutions.add(solution);

        optimizer.setMaxIterations(1);
        solution = optimizer.optimize(testFunction, startPoint);
        solutions.add(solution);

        // Test maximize
        SimplexOptimizer optimizerMax = new SimplexOptimizer(GoalType.MAXIMIZE, DELTA, DELTA);
        optimizerMax.setMaxIterations(10);
        optimizerMax.setConvergenceChecker(new SimpleValueChecker(DELTA, DELTA));
        optimizerMax.setRestartStrategy(strategy);

        PointValuePair solutionMax = optimizerMax.optimize(testFunction, startPoint);
        solutions.add(solutionMax);

        optimizerMax.setMaxIterations(5);
        solutionMax = optimizerMax.optimize(testFunction, startPoint);
        solutions.add(solutionMax);

        optimizerMax.setMaxIterations(1);
        solutionMax = optimizerMax.optimize(testFunction, startPoint);
        solutions.add(solutionMax);

        // Test goal type checker
        SimplexOptimizer optimizerGT = new SimplexOptimizer(GoalTypeChecker.CHECK, DELTA, DELTA);
        optimizerGT.setRestartStrategy(strategy);
        PointValuePair solutionGT = optimizerGT.optimize(testFunction, startPoint);
        Assert.assertNull(solutionGT);

        Assert.assertEquals(11, solutions.size());
        for (int i = 0; i < solutions.size(); i++) {
            PointValuePair solutionTest = solutions.get(i);
            Assert.assertEquals(i == 0 ? 0.0 : solutions.get(i - 1).getValue(),
                               solutionTest.getValue(), DELTA);
            Assert.assertTrue(testFunction.value(solutionTest.getPoint()) < DELTA);
        }
    }

    /**
     * Test method for {@link SimplexOptimizer#setSimplex(AbstractSimplex)}.
     */
    @Test
    public void testSetSimplex() {
        TestFunction testFunction = new TestFunction();
        SimplexOptimizer optimizer = new SimplexOptimizer(MINIMIZE, DELTA, DELTA);
        optimizer.setSimplex(new Simplex());
        optimizer.setMaxIterations(1);
        optimizer.setConvergenceChecker(new SimpleValueChecker(DELTA, DELTA));
        optimizer.setRestartStrategy(new ExponentialRestart(new int[]{10, 100, 1000}));
        PointValuePair solution = optimizer.optimize(testFunction, new double[]{1.5, 1.5, 1.5});
        Assert.assertNull(solution);
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[], double[])}.
     */
    @Test
    public void testOptimizeWithFixedStartPoint() {
        TestFunction testFunction = new TestFunction();
        SimplexOptimizer optimizer = new SimplexOptimizer(MINIMIZE, DELTA, DELTA);
        optimizer.setMaxIterations(1);
        optimizer.setConvergenceChecker(new SimpleValueChecker(DELTA, DELTA));
        optimizer.setRestartStrategy(new ExponentialRestart(new int[]{10, 100, 1000}));

        PointValuePair solution = optimizer.optimize(testFunction, new double[]{1.5, 1.5, 1.5}, 
                new double[]{1.5, 1.5, 1.5});
        Assert.assertNotNull(solution);
        Assert.assertTrue(testFunction.value(solution.getPoint()) < DELTA);
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}.
     */
    @Test
    public void testOptimizeWithFixedSimplex() {
        TestFunction testFunction = new TestFunction();
        SimplexOptimizer optimizer = new SimplexOptimizer(MINIMIZE, DELTA, DELTA);
        optimizer.setMaxIterations(1);
        optimizer.setConvergenceChecker(new SimpleValueChecker(DELTA, DELTA));
        optimizer.setRestartStrategy(new ExponentialRestart(new int[]{10, 100, 1000}));

        AbstractSimplex simplex = new Simplex();
        optimizer.parseOptimizationData(new OptimizationData[]{new OptimizationData(){
            public Object getParameter(String name) {
                if (name.equals("simplex")) {
                    return simplex;
                }
                return null;
            }
        }});

        PointValuePair solution = optimizer.optimize(testFunction, new double[]{1.5, 1.5, 1.5});
        Assert.assertNotNull(solution);
        Assert.assertTrue(testFunction.value(solution.getPoint()) < DELTA);
    }
}