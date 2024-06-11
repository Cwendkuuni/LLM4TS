package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.Gradient;
import org.apache.commons.math3.optim.nonlinear.convergence.AbstractConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.convergence.SimpleLineSearch;
import org.apache.commons.math3.optim.nonlinear.convergence.StoppingCondition;
import org.apache.commons.math3.optim.nonlinear.convergence.success.SuccessCriteria;
import org.apache.commons.math3.random.GaussianRandomGenerator;
import org.apache.commons.math3.random.MersenneTwisterFast;
import org.apache.commons.math3.special.BesselFunctions;
import org.apache.commons.math3.special.Erf;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {
    private UnivariateOptimizer<UnivariateFunction> optimizer;
    private MultiStartUnivariateRealOptimizer<UnivariateFunction> multiStartOptimizer;
    private UnivariateFunction function;
    private double min, max;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
        function = new PolynomialFunction(new double[]{-1, 1, -1}, 1);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateFunction>(optimizer, 3, new MersenneTwisterFast());
        min = 0.1;
        max = 1.0;
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateFunction, GoalType, double, double)}.
     */
    @Test
    public void testOptimizeMinimum() throws Exception {
        GoalType goal = GoalType.MINIMIZE;
        double expected = -BesselFunctions.BesselI(0, 1);
        PointValuePair result = multiStartOptimizer.optimize(function, goal, min, max);
        assertEquals(expected, result.getPoint(), 1e-12);
        assertEquals(expected, result.getValue(), 1e-12);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateFunction, GoalType, double, double)}.
     */
    @Test
    public void testOptimizeMaximum() throws Exception {
        GoalType goal = GoalType.MAXIMIZE;
        double expected = Erf.erf(0.5);
        PointValuePair result = multiStartOptimizer.optimize(function, goal, min, max);
        assertEquals(expected, result.getPoint(), 1e-12);
        assertEquals(expected, result.getValue(), 1e-12);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateFunction, GoalType, double, double)}
     * with a non-converging function.
     */
    @Test(expected = ConvergenceException.class)
    public void testOptimizeNonConvergingFunction() throws Exception {
        function = new PolynomialFunction(new double[]{1, -1, 1}, 1);
        multiStartOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateFunction, GoalType, double, double)}
     * with a function with no real roots.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeNoRealRoots() throws Exception {
        function = new PolynomialFunction(new double[]{-1, 1, -1}, 1);
        multiStartOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getOptima()}
     */
    @Test
    public void testGetOptima() throws Exception {
        GoalType goal = GoalType.MINIMIZE;
        multiStartOptimizer.optimize(function, goal, min, max);
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertEquals(3, optima.length);
        for (UnivariateRealPointValuePair p : optima) {
            if (p != null) {
                PointValuePair pv = (PointValuePair) p;
                assertEquals(expected, pv.getPoint(), 1e-12);
                assertEquals(expected, pv.getValue(), 1e-12);
            }
        }
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateFunction, GoalType, double, double)}
     * with custom convergence checkers.
     */
    @Test
    public void testCustomConvergenceChecker() throws Exception {
        final double tolerance = 1e-8;
        final int maxIterations = 100;
        final double epsilon = 1e-6;
        final SuccessCriteria<Double> success = new SuccessCriteria<Double>() {
            public boolean test(final Double value) {
                return Math.abs(value - expected) <= tolerance;
            }

            public int getMaxIterations() {
                return maxIterations;
            }
        };
        final StoppingCondition<Double> stoppingCondition = new StoppingCondition<Double>() {
            public boolean test(final Double value) {
                return Math.abs(value - expected) <= epsilon;
            }
        };
        final SimpleLineSearch lineSearch = new SimpleLineSearch(new Gradient() {
            public double value(final double x) {
                return function.value(x);
            }

            public double gradient(final double x) {
                return function.derivative(x);
            }
        });
        GoalType goal = GoalType.MINIMIZE;
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateFunction>(optimizer, 3, new GaussianRandomGenerator());
        multiStartOptimizer.setConvergenceChecker(new AbstractConvergenceChecker<UnivariateRealPointValuePair>() {
            public boolean test(final UnivariateRealPointValuePair pv) {
                return success.test(pv.getValue());
            }

            public String getFailureMessage(final UnivariateRealPointValuePair pv) {
                return "value " + pv.getValue() + " is not close enough to " + expected;
            }

            public String getSuccessMessage(final UnivariateRealPointValuePair pv) {
                return "optimum found: " + pv.getPoint() + " with value " + pv.getValue();
            }

            public int getMaxIterations() {
                return maxIterations;
            }

            public StoppingCondition<UnivariateRealPointValuePair> getStoppingCondition() {
                return new StoppingCondition<UnivariateRealPointValuePair>() {
                    public boolean test(final UnivariateRealPointValuePair pv) {
                        return stoppingCondition.test(pv.getValue());
                    }
                };
            }

            public LineSearch<UnivariateRealPointValuePair> getLineSearch() {
                return lineSearch;
            }
        });
        multiStartOptimizer.optimize(function, goal, min, max);
    }
}