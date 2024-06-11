package org.apache.commons.math.optimization.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.MultiStartUnivariateRealOptimizer;

import org.junit.Before;
import org.junit.Test;

/**
 * Test for MultiStartUnivariateRealOptimizer class
 */
public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateOptimizer optimizer;
    private MultiStartUnivariateRealOptimizer multiStartOptimizer;

    @Before
    public void setUp() {
        optimizer = new SimpleUnivariateCalculator();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwisterFast());
    }

    /**
     * Test optimization with no constraints
     */
    @Test
    public void testOptimizeNoConstraints() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0}, 1);
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;

        double result = multiStartOptimizer.optimize(function, goalType, min, max);

        PointValuePair expected = new PointValuePair(2.0, -3.0);
        assertEquals(expected.getValue(), result, 1e-12);
        assertEquals(expected.getPoint(), multiStartOptimizer.getResult(), 1e-12);
    }

    /**
     * Test optimization with constraints
     */
    @Test
    public void testOptimizeWithConstraints() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0}, 1);
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 4.0;
        double startValue = 2.0;

        double result = multiStartOptimizer.optimize(function, goalType, min, max, startValue);

        PointValuePair expected = new PointValuePair(1.0, 1.0);
        assertEquals(expected.getValue(), result, 1e-12);
        assertEquals(expected.getPoint(), multiStartOptimizer.getResult(), 1e-12);
    }

    /**
     * Test optimization when all restarts fail to converge
     */
    @Test(expected = OptimizationException.class)
    public void testOptimizeNoConvergence() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{0.0, 1.0, 0.0}, 1);
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;

        multiStartOptimizer.optimize(function, goalType, min, max);

        // should throw ConvergenceException
    }

    /**
     * Test optimization when optimizer throws FunctionEvaluationException
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeFunctionEvaluationException() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, -1.0}, 1);
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;

        multiStartOptimizer.optimize(function, goalType, min, max);

        // should throw FunctionEvaluationException
    }

    /**
     * Test optimization when optimizer throws ConvergenceException
     */
    @Test(expected = ConvergenceException.class)
    public void testOptimizeConvergenceException() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0}, 1);
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        double startValue = 10.0;

        multiStartOptimizer.optimize(function, goalType, min, max, startValue);

        // should throw ConvergenceException
    }
}