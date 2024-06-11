import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.BaseUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.noderiv.BrentOptimizer;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

public class MultiStartUnivariateRealOptimizerTest {

    @SuppressWarnings("unchecked")
    private static final UnivariateRealFunction FUNC = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return Math.sin(x) * Math.exp(-x * x / 2.0);
        }

        @Override
        public int getNumberOfParameters() {
            return 1;
        }
    };

    @Test
    public void testMultiStartUnivariateRealOptimizer() throws Exception {
        // Test with a single start.
        UnivariateRealOptimizer<UnivariateRealFunction> singleStartOptimizer =
                new BrentOptimizer();
        PointValuePair bestPoint = singleStartOptimizer.optimize(FUNC, GoalType.MINIMIZE, -3.14, 3.14);
        Assert.assertNotNull(bestPoint);

        // Test with multiple starts.
        UnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new BrentOptimizer(), 10, new RandomDataGenerator());

        UnivariateRealPointValuePair[] optima = optimizer.optimize(FUNC, GoalType.MINIMIZE, -3.14, 3.14, 0.0);
        Assert.assertNotNull(optima);
        Assert.assertEquals(10, optima.length);

        for (UnivariateRealPointValuePair pair : optima) {
            if (pair != null) {
                Assert.assertNotNull(pair.getPoint());
                Assert.assertNotNull(pair.getValue());
                Assert.assertTrue(pair.getValue() >= -1.0);
            } else {
                Assert.assertNull(pair);
            }
        }

        // Test with invalid arguments.
        try {
            optimizer = new MultiStartUnivariateRealOptimizer<>(null, 10, new RandomDataGenerator());
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            optimizer = new MultiStartUnivariateRealOptimizer<>(new BrentOptimizer(), 0, new RandomDataGenerator());
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            optimizer = new MultiStartUnivariateRealOptimizer<>(new BrentOptimizer(), 10, null);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        // Test with convergence checker.
        ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker =
                new MaxEval(MaxEval.RELATIVE, 1e-6);
        optimizer = new MultiStartUnivariateRealOptimizer<>(new BrentOptimizer(), 10, new RandomDataGenerator());
        optimizer.setConvergenceChecker(convergenceChecker);

        optima = optimizer.optimize(FUNC, GoalType.MINIMIZE, -3.14, 3.14, 0.0);
        Assert.assertNotNull(optima);
        Assert.assertEquals(10, optima.length);

        for (UnivariateRealPointValuePair pair : optima) {
            if (pair != null) {
                Assert.assertNotNull(pair.getPoint());
                Assert.assertNotNull(pair.getValue());
                Assert.assertTrue(pair.getValue() >= -1.0);
            } else {
                Assert.assertNull(pair);
            }
        }

        // Test with custom initial guess.
        optimizer = new MultiStartUnivariateRealOptimizer<>(new BrentOptimizer(), 10, new RandomDataGenerator());
        UnivariateRealPointValuePair[] customGuesses = new UnivariateRealPointValuePair[10];

        for (int i = 0; i < 10; ++i) {
            customGuesses[i] = new UnivariateRealPointValuePair(
                    new InitialGuess(SimpleBounds.open(0.0, 10.0)), Double.POSITIVE_INFINITY);
        }

        optimizer.setInitialGuesses(customGuesses);
        optima = optimizer.optimize(FUNC, GoalType.MINIMIZE, -3.14, 3.14, 0.0);
        Assert.assertNotNull(optima);
        Assert.assertEquals(10, optima.length);

        for (UnivariateRealPointValuePair pair : optima) {
            if (pair != null) {
                Assert.assertNotNull(pair.getPoint());
                Assert.assertNotNull(pair.getValue());
                Assert.assertTrue(pair.getValue() >= -1.0);
            } else {
                Assert.assertNull(pair);
            }
        }
    }
}