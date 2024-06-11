package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.AbstractSimplex;
import org.apache.commons.math3.optim.maximumlikelihood.GaussianCovarianceModel;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeInterval;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {

    private MultivariateFunction function;
    private MultivariateOptimizer optimizer;
    private ConvergenceChecker<PointValuePair> convergenceChecker;
    private AbstractSimplex simplex;

    @Before
    public void setUp() {
        this.function = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(x[0] - 1, 2) + Math.pow(x[1] - 1, 2);
            }
        };
        this.convergenceChecker = new SimpleValueChecker(1e-6, 1e-6);
        this.simplex = new SimpleSimplex(new SimpleValueChecker());
        this.optimizer = new SimplexOptimizer(this.convergenceChecker);
    }

    /**
     * Test the optimization of a quadratic function.
     */
    @Test
    public void testQuadraticFunction() {
        double[] startPoint = {1.5, 1.5};
        PointValuePair optimum = this.optimizer.optimize(100, this.function,
                                                         GoalType.MINIMIZE,
                                                         new OptimizationData[] {
            new InitialGuess(startPoint),
            this.simplex
        });
        Assert.assertEquals(0.25, optimum.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[] {0.5, 0.5}, optimum.getPoint(), 1e-6);
    }

    /**
     * Test the optimization of a function with a local minimum and a plateau.
     */
    @Test
    public void testLocalMinimumAndPlateau() {
        double[] startPoint = {3, 3};
        this.optimizer.setSimplex(this.simplex);
        PointValuePair optimum = this.optimizer.optimize(100, this.function,
                                                         GoalType.MINIMIZE,
                                                         new OptimizationData[] {
            new InitialGuess(startPoint),
            this.simplex
        });
        Assert.assertEquals(1, optimum.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[] {1, 1}, optimum.getPoint(), 1e-6);
    }

    /**
     * Test the optimization of a function with no local minimum.
     */
    @Test(expected = NullArgumentException.class)
    public void testNoLocalMinimum() {
        double[] startPoint = null;
        PointValuePair optimum = this.optimizer.optimize(100, this.function,
                                                         GoalType.MINIMIZE,
                                                         new OptimizationData[] {
            new InitialGuess(startPoint),
            this.simplex
        });
    }

    /**
     * Test the optimization of a function with a limit.
     */
    @Test
    public void testLimit() {
        double[] startPoint = {0.1, 0.1};
        this.optimizer.setGoalType(new GoalTypeInterval(3.5));
        PointValuePair optimum = this.optimizer.optimize(100, this.function,
                                                         GoalType.MINIMIZE,
                                                         new OptimizationData[] {
            new InitialGuess(startPoint),
            this.simplex
        });
        Assert.assertEquals(3.5, optimum.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[] {3.5 / Math.sqrt(2), 3.5 / Math.sqrt(2)},
                                optimum.getPoint(), 1e-6);
    }
}