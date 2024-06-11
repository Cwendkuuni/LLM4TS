import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checker = new MockConvergenceChecker();
        BaseUnivariateRealOptimizer<FUNC> optimizer = new MockBaseUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MockRandomGenerator());

        multiStartOptimizer.setConvergenceChecker(checker);
        Assert.assertEquals(checker, multiStartOptimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = new MockBaseUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MockRandomGenerator());

        Assert.assertEquals(0, multiStartOptimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = new MockBaseUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MockRandomGenerator());

        Assert.assertEquals(0, multiStartOptimizer.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = new MockBaseUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MockRandomGenerator());

        int maxEvaluations = 100;
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        Assert.assertEquals(maxEvaluations, multiStartOptimizer.getMaxEvaluations());
        Assert.assertEquals(maxEvaluations, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetOptima() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = new MockBaseUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MockRandomGenerator());

        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        Assert.assertNotNull(optima);
        Assert.assertEquals(0, optima.length);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaNoOptimumComputed() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = new MockBaseUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MockRandomGenerator());

        multiStartOptimizer.getOptima();
    }

    @Test
    public void testOptimize() throws FunctionEvaluationException {
        BaseUnivariateRealOptimizer<FUNC> optimizer = new MockBaseUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MockRandomGenerator());

        UnivariateRealFunction f = new MockUnivariateRealFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;

        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(f, goal, min, max);

        Assert.assertNotNull(result);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeNoConvergence() throws FunctionEvaluationException {
        BaseUnivariateRealOptimizer<FUNC> optimizer = new MockBaseUnivariateRealOptimizerNoConvergence();
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MockRandomGenerator());

        UnivariateRealFunction f = new MockUnivariateRealFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;

        multiStartOptimizer.optimize(f, goal, min, max);
    }

    private static class MockConvergenceChecker implements ConvergenceChecker<UnivariateRealPointValuePair> {

        @Override
        public boolean converged(int iteration, UnivariateRealPointValuePair previous, UnivariateRealPointValuePair current) {
            return true;
        }
    }

    private static class MockBaseUnivariateRealOptimizer<FUNC extends UnivariateRealFunction> implements BaseUnivariateRealOptimizer<FUNC> {

        @Override
        public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {

        }

        @Override
        public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
            return null;
        }

        @Override
        public int getMaxEvaluations() {
            return 0;
        }

        @Override
        public int getEvaluations() {
            return 0;
        }

        @Override
        public void setMaxEvaluations(int maxEvaluations) {

        }

        @Override
        public UnivariateRealPointValuePair optimize(FUNC f, GoalType goal, double min, double max) throws FunctionEvaluationException, ConvergenceException {
            return null;
        }
    }

    private static class MockRandomGenerator implements RandomGenerator {

        @Override
        public void setSeed(int seed) {

        }

        @Override
        public void setSeed(int[] seed) {

        }

        @Override
        public void setSeed(long seed) {

        }

        @Override
        public void nextBytes(byte[] bytes) {

        }

        @Override
        public int nextInt() {
            return 0;
        }

        @Override
        public int nextInt(int n) {
            return 0;
        }

        @Override
        public long nextLong() {
            return 0;
        }

        @Override
        public boolean nextBoolean() {
            return false;
        }

        @Override
        public float nextFloat() {
            return 0;
        }

        @Override
        public double nextDouble() {
            return 0;
        }

        @Override
        public double nextGaussian() {
            return 0;
        }
    }

    private static class MockUnivariateRealFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) throws FunctionEvaluationException {
            return 0;
        }
    }

    private static class MockBaseUnivariateRealOptimizerNoConvergence<FUNC extends UnivariateRealFunction> extends MockBaseUnivariateRealOptimizer<FUNC> {

        @Override
        public UnivariateRealPointValuePair optimize(FUNC f, GoalType goal, double min, double max) throws FunctionEvaluationException, ConvergenceException {
            throw new ConvergenceException(LocalizedFormats.NO_CONVERGENCE_WITH_ANY_START_POINT, 5);
        }
    }
}