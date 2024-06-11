import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testConstructor() {
        UnivariateRealFunction function = new SimpleFunction();
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FibonacciSearch();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapped = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new RandomGenerator() {
            @Override
            public double nextDouble() {
                return 0.5;
            }
        });
        Assert.assertNotNull(optimizerWrapped);
    }

    @Test
    public void testSettersAndGetters() {
        UnivariateRealFunction function = new SimpleFunction();
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FibonacciSearch();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapped = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new RandomGenerator() {
            @Override
            public double nextDouble() {
                return 0.5;
            }
        });

        Assert.assertSame(optimizer, optimizerWrapped.getOptimizer());
        optimizerWrapped.setMaxEvaluations(10);
        Assert.assertEquals(10, optimizerWrapped.getMaxEvaluations());
        optimizerWrapped.setEvaluations(5);
        Assert.assertEquals(5, optimizerWrapped.getEvaluations());
    }

    @Test
    public void testGetOptima_NoConvergence() throws ConvergenceException {
        UnivariateRealFunction function = new SimpleFunction();
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FibonacciSearch();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapped = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new RandomGenerator() {
            @Override
            public double nextDouble() {
                return 0.5;
            }
        });
        PointValuePair[] optima = optimizerWrapped.optimize(function, GoalType.MINIMIZE, -10.0, 10.0).getOptima();
        Assert.assertArrayEquals(new PointValuePair[]{null, null, null, null, null}, optima);
    }

    @Test
    public void testGetOptima_ConvergenceWithSingleStart() throws ConvergenceException {
        UnivariateRealFunction function = new SimpleFunction();
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FibonacciSearch();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapped = new MultiStartUnivariateRealOptimizer<>(optimizer, 1, new RandomGenerator() {
            @Override
            public double nextDouble() {
                return 0.5;
            }
        });
        PointValuePair[] optima = optimizerWrapped.optimize(function, GoalType.MINIMIZE, -10.0, 10.0).getOptima();
        Assert.assertNotNull(optima[0]);
    }

    @Test
    public void testGetOptima_ConvergenceWithMultipleStarts() throws ConvergenceException {
        UnivariateRealFunction function = new SimpleFunction();
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FibonacciSearch();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapped = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomGenerator() {
            @Override
            public double nextDouble() {
                return 0.5;
            }
        });
        PointValuePair[] optima = optimizerWrapped.optimize(function, GoalType.MINIMIZE, -10.0, 10.0).getOptima();
        Assert.assertNotNull(optima);
        Assert.assertEquals(10, optima.length);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptima_NotOptimized() throws ConvergenceException {
        UnivariateRealFunction function = new SimpleFunction();
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FibonacciSearch();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapped = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomGenerator() {
            @Override
            public double nextDouble() {
                return 0.5;
            }
        });
        PointValuePair[] optima = optimizerWrapped.getOptima();
        Assert.fail("Expected MathIllegalStateException");
    }
}

class SimpleFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.pow(x, 2);
    }
}