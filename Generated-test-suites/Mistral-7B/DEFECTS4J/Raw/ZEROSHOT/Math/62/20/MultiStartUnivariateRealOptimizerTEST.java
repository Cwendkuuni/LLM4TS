import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.BaseUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateRealPointValuePair;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartUnivariateRealOptimizer() {
        UnivariateRealFunction function = new MyTestFunction();
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FibonacciSearchOptimizer(function, 0.0, 1.0, 0.01);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwisterFastRandom());

        // Test with no max evaluations
        try {
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("maxEvaluations must be set before optimization is initiated", e.getMessage());
        }

        // Test with max evaluations set to a value less than the optimization requires
        multiStartOptimizer.setMaxEvaluations(5);
        try {
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
            Assert.fail();
        } catch (ConvergenceException e) {
            Assert.assertEquals("Reached maximum number of evaluations allowed", e.getMessage());
        }

        // Test with max evaluations set to a value greater than the optimization requires
        multiStartOptimizer.setMaxEvaluations(100);
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);

        // Check that optimization was performed correctly
        Assert.assertNotNull(result);
        Assert.assertFalse("Optimization should have converged", multiStartOptimizer.isDiverging());
        Assert.assertEquals(5, multiStartOptimizer.getOptima().length);
        for (UnivariateRealPointValuePair pair : multiStartOptimizer.getOptima()) {
            Assert.assertNotNull(pair);
            Assert.assertFalse("Optima should not be null", pair.isDiverging());
            Assert.assertTrue("Optima should have been found", pair.getValue() >= 0.0 && pair.getValue() <= 1.0);
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        UnivariateRealFunction function = new MyTestFunctionWithException();
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FibonacciSearchOptimizer(function, 0.0, 1.0, 0.01);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwisterFastRandom());

        multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() {
        UnivariateRealFunction function = new MyTestFunctionWithNoSolution();
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FibonacciSearchOptimizer(function, 0.0, 1.0, 0.01);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwisterFastRandom());

        multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
    }

    private static class MyTestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2);
        }

        @Override
        public double derive(double x) {
            return 2.0 * x;
        }
    }

    private static class MyTestFunctionWithException implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            if (x > 0.5) {
                throw new FunctionEvaluationException("Exception in function evaluation at x = " + x);
            }
            return Math.pow(x, 2);
        }

        @Override
        public double derive(double x) {
            return 2.0 * x;
        }
    }

    private static class MyTestFunctionWithNoSolution implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.sin(Math.PI * x) / x;
        }

        @Override
        public double derive(double x) {
            return (Math.cos(Math.PI * x) / x - Math.PI * Math.cos(Math.PI * x)) - (Math.sin(Math.PI * x) / (x * x));
        }
    }
}