import org.apache.commons.math3.optim.BaseUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.UnivariateRealPointValuePair;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest<FUNC extends UnivariateRealFunction> {

    private BaseUnivariateRealOptimizer<FUNC> optimizer;
    private MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer;
    private FUNC testFunction;
    private RandomDataGenerator random;

    @Before
    public void setUp() {
        optimizer = new FooUnivariateOptimizer<>(); // Assuming FooUnivariateOptimizer is the optimizer being wrapped
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new RandomDataGenerator());
        testFunction = new MyTestFunction();
    }

    @Test
    public void testBasicFunctionality() throws Exception {
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, -10.0, 10.0);
        Assert.assertNotNull(result);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationExceptionThrown() throws Exception {
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(new ThrowingFunction(), GoalType.MINIMIZE, -10.0, 10.0);
        Assert.fail("A FunctionEvaluationException should have been thrown.");
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceExceptionThrown() throws Exception {
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(new NonConvergingFunction(), GoalType.MINIMIZE, -10.0, 10.0);
        Assert.fail("A ConvergenceException should have been thrown.");
    }

    @Test
    public void testGetOptima() throws Exception {
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        Assert.assertArrayNotNull(optima);
        Assert.assertEquals(5, optima.length);
    }

    @Test
    public void testGetOptimaEmpty() throws Exception {
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 0, new RandomDataGenerator());
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        Assert.assertArrayNotNull(optima);
        Assert.assertEquals(0, optima.length);
    }

    @Test
    public void testMaxEvaluationsExceeded() throws Exception {
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new RandomDataGenerator());
        multiStartOptimizer.setMaxEvaluations(MaxEval.ONE);
        try {
            multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, -10.0, 10.0);
            Assert.fail("A ConvergenceException should have been thrown.");
        } catch (ConvergenceException ce) {
            Assert.assertTrue(ce.getMessage().contains("Maximum number of evaluations exceeded"));
        }
    }
}

private static class MyTestFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.sin(x);
    }
}

private static class FooUnivariateOptimizer<FUNC extends UnivariateRealFunction>
        implements BaseUnivariateRealOptimizer<FUNC> {
    @Override
    public void optimize(FUNC f, GoalType goal, double start, double end)
            throws FunctionEvaluationException {
        // Implementation of the optimizer to be wrapped
    }
}

private static class ThrowingFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) throws FunctionEvaluationException {
        throw new FunctionEvaluationException("Test FunctionEvaluationException");
    }
}

private static class NonConvergingFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        // Non-converging function implementation
    }
}