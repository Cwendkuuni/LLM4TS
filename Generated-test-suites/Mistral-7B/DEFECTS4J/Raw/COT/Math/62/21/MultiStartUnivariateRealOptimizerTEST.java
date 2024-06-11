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
    public void testConstructor() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = mockOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapped = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomGenerator());
        Assert.assertNotNull(optimizerWrapped);
        Assert.assertEquals(optimizer, optimizerWrapped.getOptimizer());
        Assert.assertEquals(3, optimizerWrapped.getStarts());
        Assert.assertNotNull(optimizerWrapped.getGenerator());
    }

    @Test
    public void testSetConvergenceChecker() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = mockOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapped = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomGenerator());
        ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker = mockConvergenceChecker();
        optimizerWrapped.setConvergenceChecker(convergenceChecker);
        Assert.assertEquals(convergenceChecker, optimizerWrapped.getConvergenceChecker());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = mockOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapped = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomGenerator());
        ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker = mockConvergenceChecker();
        optimizerWrapped.setConvergenceChecker(convergenceChecker);
        Assert.assertEquals(convergenceChecker, optimizerWrapped.getConvergenceChecker());
        ConvergenceChecker<UnivariateRealPointValuePair> convergenceCheckerReturned = optimizerWrapped.getConvergenceChecker();
        Assert.assertEquals(convergenceChecker, convergenceCheckerReturned);
    }

    @Test
    public void testGetMaxEvaluations() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = mockOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapped = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomGenerator());
        int maxEvaluations = optimizerWrapped.getMaxEvaluations();
        Assert.assertEquals(optimizer.getMaxEvaluations(), maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = mockOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapped = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomGenerator());
        int evaluations = optimizerWrapped.getEvaluations();
        Assert.assertEquals(optimizer.getEvaluations(), evaluations);
    }

    @Test
    public void testSetMaxEvaluations() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = mockOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapped = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomGenerator());
        optimizerWrapped.setMaxEvaluations(10);
        Assert.assertEquals(10, optimizerWrapped.getMaxEvaluations());
        optimizer.setMaxEvaluations(5);
        Assert.assertEquals(5, optimizerWrapped.getMaxEvaluations());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxEvaluationsNegative() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = mockOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapped = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomGenerator());
        optimizerWrapped.setMaxEvaluations(-1);
    }

    @Test
    public void testGetOptimaEmpty() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = mockOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapped = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomGenerator());
        Assert.assertArrayEquals(new UnivariateRealPointValuePair[0], optimizerWrapped.getOptima());
    }

    @Test
    public void testGetOptima() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = mockOptimizer();
        RandomGenerator generator = new CyclicRandomGenerator();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapped = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, generator);
        UnivariateRealFunction function = mockFunction();
        double min = -1.0;
        double max = 1.0;
        UnivariateRealPointValuePair[] optima = optimizerWrapped.optimize(function, GoalType.MINIMIZE, min, max)
                .getOptima();
        UnivariateRealPointValuePair[] expectedOptima = new UnivariateRealPointValuePair[3];
        for (int i = 0; i < 3; i++) {
            expectedOptima[i] = new UnivariateRealPointValuePair(generator.nextDouble(min, max), function.value(generator.nextDouble(min, max)));
        }
        Assert.assertArrayEquals(expectedOptima, optimizerWrapped.getOptima());
    }

    @Test
    public void testOptimizeNullFunction() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = mockOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapped = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomGenerator());
        Assert.assertThrows(NullPointerException.class, () -> optimizerWrapped.optimize(null, GoalType.MINIMIZE, 0.0, 1.0));
    }

    @Test
    public void testOptimizeNullGoalType() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = mockOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapped = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomGenerator());
        UnivariateRealFunction function = mockFunction();
        Assert.assertThrows(NullPointerException.class, () -> optimizerWrapped.optimize(function, null, 0.0, 1.0));
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeFunctionEvaluationException() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = mockOptimizerThrowingException();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapped = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomGenerator());
        UnivariateRealFunction function = mockFunction();
        optimizerWrapped.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeConvergenceException() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = mockOptimizerThrowingConvergenceException();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapped = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomGenerator());
        UnivariateRealFunction function = mockFunction();
        optimizerWrapped.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testOptimizeNoOptimumComputedYet() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = mockOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapped = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomGenerator());
        UnivariateRealFunction function = mockFunction();
        optimizerWrapped.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        optimizerWrapped.getOptima();
        Assert.assertThrows(MathIllegalStateException.class, () -> optimizerWrapped.optimize(function, GoalType.MINIMIZE, 0.0, 1.0));
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeNoConvergenceWithAnyStartPoint() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = mockOptimizerThrowingConvergenceException();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapped = new MultiStartUnivariateRealOptimizer<>(optimizer, 1, new RandomGenerator());
        UnivariateRealFunction function = mockFunction();
        optimizerWrapped.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
    }

    private BaseUnivariateRealOptimizer<UnivariateRealFunction> mockOptimizer() {
        return mock(BaseUnivariateRealOptimizer.class);
    }

    private BaseUnivariateRealOptimizer<UnivariateRealFunction> mockOptimizerThrowingException() {
        return mock(BaseUnivariateRealOptimizer.class, new ExceptionThrowingBehavior());
    }

    private BaseUnivariateRealOptimizer<UnivariateRealFunction> mockOptimizerThrowingConvergenceException() {
        return mock(BaseUnivariateRealOptimizer.class, new ConvergenceThrowingBehavior());
    }

    private ConvergenceChecker<UnivariateRealPointValuePair> mockConvergenceChecker() {
        return mock(ConvergenceChecker.class);
    }

    private UnivariateRealFunction mockFunction() {
        return mock(UnivariateRealFunction.class);
    }

    private static class CyclicRandomGenerator implements RandomGenerator {
        private double current = 0.0;

        @Override
        public double nextDouble(double min, double max) {
            double next = current;
            current = (current + 0.2) % (max - min + 1.0) + min;
            return next;
        }

        @Override
        public long nextLong() {
            throw new UnsupportedOperationException("Method not supported.");
        }
    }
}