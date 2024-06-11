import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;

    @Before
    public void setUp() {
        BaseUnivariateRealOptimizer singleStartOptimizer = new DummyUnivariateRealOptimizer();
        int starts = 3;
        RandomGenerator generator = new DummyRandomGenerator();
        optimizer = new MultiStartUnivariateRealOptimizer(singleStartOptimizer, starts, generator);
    }

    @Test
    public void testOptimize_typical() {
        UnivariateRealFunction function = new DummyUnivariateRealFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;

        UnivariateRealPointValuePair result = optimizer.optimize(function, goal, min, max);

        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), 0.000001);
    }

    @Test
    public void testOptimize_edge() {
        UnivariateRealFunction function = new DummyUnivariateRealFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = Double.NEGATIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;

        UnivariateRealPointValuePair result = optimizer.optimize(function, goal, min, max);

        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), 0.000001);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimize_error() {
        UnivariateRealFunction function = new DummyUnivariateRealFunctionWithError();
        GoalType goal = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;

        optimizer.optimize(function, goal, min, max);
    }

    private static class DummyUnivariateRealOptimizer implements BaseUnivariateRealOptimizer<UnivariateRealFunction> {

        private ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker;
        private int maxEvaluations;
        private int evaluations;

        @Override
        public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
            this.convergenceChecker = checker;
        }

        @Override
        public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
            return convergenceChecker;
        }

        @Override
        public int getMaxEvaluations() {
            return maxEvaluations;
        }

        @Override
        public int getEvaluations() {
            return evaluations;
        }

        @Override
        public void setMaxEvaluations(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        @Override
        public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException {
            return new UnivariateRealPointValuePair(0.0, f.value(0.0));
        }
    }

    private static class DummyRandomGenerator implements RandomGenerator {

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

    private static class DummyUnivariateRealFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) throws FunctionEvaluationException {
            return x * x;
        }
    }

    private static class DummyUnivariateRealFunctionWithError implements UnivariateRealFunction {

        @Override
        public double value(double x) throws FunctionEvaluationException {
            throw new FunctionEvaluationException(x);
        }
    }
}