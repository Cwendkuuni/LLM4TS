import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.BaseUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateRealPointValuePair;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MultiStartUnivariateRealOptimizerTest<FUNC extends UnivariateRealFunction> {

    @Test
    public void testConstructor() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = mockOptimizer();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MockRandomGenerator());
        Assert.assertNotNull(multiStartOptimizer.getOptima());
    }

    @Test
    public void testSetConvergenceChecker() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = mockOptimizer();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MockRandomGenerator());
        ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker = mockConvergenceChecker();
        multiStartOptimizer.setConvergenceChecker(convergenceChecker);
        Assert.assertSame(convergenceChecker, multiStartOptimizer.getConvergenceChecker());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = mockOptimizer();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MockRandomGenerator());
        ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker = mockConvergenceChecker();
        multiStartOptimizer.setConvergenceChecker(convergenceChecker);
        Assert.assertSame(convergenceChecker, multiStartOptimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = mockOptimizer();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MockRandomGenerator());
        Assert.assertEquals(0, multiStartOptimizer.getMaxEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = mockOptimizer();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MockRandomGenerator());
        int maxEvaluations = 10;
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        Assert.assertEquals(maxEvaluations, multiStartOptimizer.getMaxEvaluations());
        optimizer.setMaxEvaluations(maxEvaluations);
        Assert.assertEquals(maxEvaluations, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = mockOptimizer();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MockRandomGenerator());
        Assert.assertEquals(0, multiStartOptimizer.getEvaluations());
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaBeforeOptimize() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = mockOptimizer();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MockRandomGenerator());
        multiStartOptimizer.getOptima();
    }

    @Test
    public void testOptimizeWithMinimizationGoalType() throws FunctionEvaluationException {
        BaseUnivariateRealOptimizer<FUNC> optimizer = mockOptimizer();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MockRandomGenerator());
        UnivariateRealFunction function = new MockFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(function, goal, min, max);
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getPoint());
        Assert.assertFalse(Double.isNaN(result.getValue()));
        Assert.assertEquals(goal, result.getGoalType());
        Assert.assertFalse(Arrays.stream(multiStartOptimizer.getOptima()).allMatch(optima -> optima == null));
    }

    @Test
    public void testOptimizeWithMaximizationGoalType() throws FunctionEvaluationException {
        BaseUnivariateRealOptimizer<FUNC> optimizer = mockOptimizer();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MockRandomGenerator());
        UnivariateRealFunction function = new MockFunction();
        GoalType goal = GoalType.MAXIMIZE;
        double min = 0.0;
        double max = 10.0;
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(function, goal, min, max);
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getPoint());
        Assert.assertTrue(Double.isNaN(result.getValue()));
        Assert.assertEquals(goal, result.getGoalType());
        Assert.assertFalse(Arrays.stream(multiStartOptimizer.getOptima()).allMatch(optima -> optima == null));
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithoutConvergence() throws FunctionEvaluationException {
        BaseUnivariateRealOptimizer<FUNC> optimizer = mockOptimizerThrowingConvergenceException();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MockRandomGenerator());
        UnivariateRealFunction function = mockFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        multiStartOptimizer.optimize(function, goal, min, max);
    }

    @Test
    public void testOptimizeWithMultipleStarts() throws FunctionEvaluationException {
        BaseUnivariateRealOptimizer<Func> optimizer = mockOptimizer();
        MultiStartUnivariateRealOptimizer<Func> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new MockRandomGenerator());
        UnivariateRealFunction function = new MockFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.optimize(function, goal, min, max, 0);
        Assert.assertNotNull(optima);
        Assert.assertArrayEquals(10, optima.length);
        for (UnivariateRealPointValuePair pair : optima) {
            Assert.assertNotNull(pair);
            Assert.assertNotNull(pair.getPoint());
            Assert.assertFalse(Double.isNaN(pair.getValue()));
            Assert.assertEquals(goal, pair.getGoalType());
        }
        Assert.assertFalse(Arrays.stream(multiStartOptimizer.getOptima()).allMatch(optima -> optima == null));
    }

    @Test
    public void testOptimizeWithMaxEvaluationsReached() throws FunctionEvaluationException {
        BaseUnivariateRealOptimizer<FUNC> optimizer = mockOptimizer();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MockRandomGenerator());
        UnivariateRealFunction function = mockFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        multiStartOptimizer.setMaxEvaluations(5);
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.optimize(function, goal, min, max, 0);
        Assert.assertNotNull(optima);
        Assert.assertArrayEquals(5, optima.length);
        Assert.assertTrue(Arrays.stream(multiStartOptimizer.getOptima()).anyMatch(optima -> optima != null));
    }

    private BaseUnivariateRealOptimizer<FUNC> mockOptimizer() {
        return mock(BaseUnivariateRealOptimizer.class);
    }

    private BaseUnivariateRealOptimizer<FUNC> mockOptimizerThrowingConvergenceException() {
        return mock(BaseUnivariateRealOptimizer.class, new Answer<UnivariateRealPointValuePair>() {
            @Override
            public UnivariateRealPointValuePair answer(InvocationOnMock invocation) throws Throwable {
                throw new ConvergenceException("Mock Convergence Exception");
            }
        });
    }

    private ConvergenceChecker<UnivariateRealPointValuePair> mockConvergenceChecker() {
        return mock(ConvergenceChecker.class);
    }

    private UnivariateRealFunction mockFunction() {
        return mock(UnivariateRealFunction.class);
    }
}

class MockRandomGenerator implements RandomGenerator {
    @Override
    public double nextDouble() {
        return 0.5;
    }
}

class MockFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.pow(x, 2);
    }

    @Override
    public double derivative(double x) {
        return 2.0 * x;
    }

    @Override
    public void checkDerivative(double x, double fx, double tolerance) {
        // Do nothing
    }
}