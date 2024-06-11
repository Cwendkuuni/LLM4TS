import org.apache.commons.math3.optim.BaseUnivariateRealOptimizerTests;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.goal.GoalType;
import org.apache.commons.math3.optim.nonlinear.simple.function.Pow;
import org.apache.commons.math3.optim.nonlinear.simple.function.Sine;
import org.apache.commons.math3.optim.nonlinear.simple.function.Square;
import org.apache.commons.math3.optim.univariate.GoalType;
import org.apache.commons.math3.optim.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateRealPointValuePair;
import org.apache.commons.math3.random.RandomValueGenerator;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MultiStartUnivariateRealOptimizerTest extends BaseUnivariateRealOptimizerTests {
    private static final int MAX_EVALUATIONS = 100;
    private static final int NUM_STARTS = 5;
    private static final RandomValueGenerator rng = new TestRandomGenerator();

    @Test
    public void testMinimization() throws Exception {
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new Fibonacci(), GoalType.MINIMIZE, NUM_STARTS, rng);
        UnivariateRealPointValuePair result = optimizer.optimize(new Fibonacci(), GoalType.MINIMIZE, 0.0, Double.POSITIVE_INFINITY);
        Assert.assertEquals(0.0, result.getPoint(), 1e-14);
        Assert.assertEquals(-1.0, result.getValue(), 1e-14);
        assertArrayEquals(new UnivariateRealPointValuePair[] {
                new UnivariateRealPointValuePair(2.0, -1.0),
                new UnivariateRealPointValuePair(5.0, -3.0),
                result,
                new UnivariateRealPointValuePair(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY),
                new UnivariateRealPointValuePair(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY)
        }, optimizer.getOptima());
    }

    @Test
    public void testMaximization() throws Exception {
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new Pow(2.0), GoalType.MAXIMIZE, NUM_STARTS, rng);
        UnivariateRealPointValuePair result = optimizer.optimize(new Pow(2.0), GoalType.MAXIMIZE, 0.0, Double.POSITIVE_INFINITY);
        Assert.assertEquals(Double.POSITIVE_INFINITY, result.getPoint(), 1e-14);
        Assert.assertEquals(Double.POSITIVE_INFINITY, result.getValue(), 1e-14);
        assertArrayEquals(new UnivariateRealPointValuePair[] {
                new UnivariateRealPointValuePair(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY),
                new UnivariateRealPointValuePair(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY),
                result,
                new UnivariateRealPointValuePair(0.0, 1.0),
                new UnivariateRealPointValuePair(1.0, 4.0)
        }, optimizer.getOptima());
    }

    @Test
    public void testMixed() throws Exception {
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new Square(), GoalType.MIN_MAX, NUM_STARTS, rng);
        UnivariateRealPointValuePair result = optimizer.optimize(new Square(), GoalType.MIN_MAX, 0.0, Double.POSITIVE_INFINITY);
        Assert.assertNotNull(result);
        Assert.assertEquals(3.0, result.getPoint(), 1e-14);
        Assert.assertEquals(9.0, result.getValue(), 1e-14);
        assertArrayEquals(new UnivariateRealPointValuePair[] {
                new UnivariateRealPointValuePair(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY),
                new UnivariateRealPointValuePair(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY),
                result,
                new UnivariateRealPointValuePair(1.0, 1.0),
                new UnivariateRealPointValuePair(4.0, 16.0)
        }, optimizer.getOptima());
    }

    @Test
    public void testNoConvergence() throws Exception {
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new Sine(), GoalType.MINIMIZE, NUM_STARTS, rng);
        try {
            optimizer.optimize(new Sine(), GoalType.MINIMIZE, 0.0, Double.POSITIVE_INFINITY);
        } catch (ConvergenceException e) {
            Assert.assertEquals(NUM_STARTS, e.getNumStartsFailed());
            assertArrayEquals(new UnivariateRealPointValuePair[] {}, optimizer.getOptima());
            return;
        }
        Assert.fail();
    }

    @Test
    public void testOutOfBounds() throws Exception {
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new Square(), GoalType.MINIMIZE, NUM_STARTS, rng);
        try {
            optimizer.optimize(new Square(), GoalType.MINIMIZE, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("min must be less than max", e.getMessage());
            return;
        }
        Assert.fail();
    }

    @Test
    public void testMaxEvaluations() throws Exception {
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new Pow(2.0), GoalType.MAXIMIZE, NUM_STARTS, rng);
        optimizer.setMaxEvaluations(MAX_EVALUATIONS);
        optimizer.optimize(new Pow(2.0), GoalType.MAXIMIZE, 0.0, Double.POSITIVE_INFINITY);
        Assert.assertEquals(MAX_EVALUATIONS, optimizer.getEvaluations());
    }

    @AfterClass
    public static void cleanUp() {
        rng.setSeed(System.currentTimeMillis());
    }
}

class Fibonacci implements UnivariateRealFunction {
    private int a = 1, b = 1;

    @Override
    public double value(double x) {
        int c = a + b;
        a = b;
        b = c;
        return a;
    }

    @Override
    public double derivative(double x) {
        return value(x) - value(x - 1.0);
    }
}