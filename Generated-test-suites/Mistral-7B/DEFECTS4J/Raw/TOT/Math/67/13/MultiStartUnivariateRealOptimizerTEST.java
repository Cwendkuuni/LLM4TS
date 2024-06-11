package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.NonUnaryOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the {@link MultiStartUnivariateRealOptimizer} class.
 */
public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateRealFunction function;
    private UnivariateRealOptimizer optimizer;
    private MultiStartUnivariateRealOptimizer multiStartOptimizer;

    @Before
    public void setUp() {
        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) - 2 * Math.cos(x) + 1;
            }
        };
        optimizer = new NelderMeadSimplexSearch();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new RandomDataGenerator());
    }

    /**
     * Tests the multi-start optimizer with an easy function which has a
     * global minimum at x = 0.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        double min = 0.1;
        double max = 10.0;
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = multiStartOptimizer.optimize(function, goalType, min, max);
        Assert.assertNotNull(result);
        Assert.assertEquals(0.0, Precision.round(result.getPoint(), 6), 1e-6);
        Assert.assertEquals(Precision.round(function.value(0.0), 6), Precision.round(result.getValue(), 6), 1e-6);
    }

    /**
     * Tests the multi-start optimizer with a function which has multiple
     * local minimums and a global minimum at x = 1.
     */
    @Test
    public void testOptimizeComplexFunction() {
        double min = -5.0;
        double max = 5.0;
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = multiStartOptimizer.optimize(function, goalType, min, max);
        Assert.assertNotNull(result);
        Assert.assertEquals(1.0, Precision.round(result.getPoint(), 6), 1e-6);
        Assert.assertEquals(Precision.round(function.value(1.0), 6), Precision.round(result.getValue(), 6), 1e-6);
    }

    /**
     * Tests the multi-start optimizer with a function which has no real root
     * between given bounds.
     */
    @Test(expected = ConvergenceException.class)
    public void testOptimizeNoSolutionFunction() {
        double min = -1.0;
        double max = 1.0;
        GoalType goalType = GoalType.MINIMIZE;
        multiStartOptimizer.optimize(function, goalType, min, max);
    }

    /**
     * Tests the multi-start optimizer with a function which throws an
     * exception during evaluation.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeExceptionFunction() {
        class ExceptionFunction implements UnivariateRealFunction {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Evaluation exception");
            }
        }

        double min = 0.0;
        double max = 1.0;
        GoalType goalType = GoalType.MINIMIZE;
        UnivariateRealOptimizer optimizer = new MinimizerJansen(new SimpleBounds(min, max), new MaxEval(100));
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new RandomDataGenerator());

        ExceptionFunction function = new ExceptionFunction();
        multiStartOptimizer.optimize(function, goalType, min, max);
    }
}