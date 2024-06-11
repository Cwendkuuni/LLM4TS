import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.UnivariateRealPointValuePair;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.util.FastMath;
import java.util.Random;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.optimization.ConvergenceChecker;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer<FUNC> optimizer;
    private MockOptimizer<FUNC> mockOptimizer;
    private RandomGenerator generator;

    @Before
    public void setUp() {
        mockOptimizer = new MockOptimizer<FUNC>();
        generator = new RandomGenerator() {
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
            public void nextBytes(byte[] bytes) {}
            public int nextInt() { return 0; }
            public int nextInt(int n) { return 0; }
            public long nextLong() { return 0; }
            public boolean nextBoolean() { return false; }
            public float nextFloat() { return 0; }
            public double nextDouble() { return 0; }
            public double nextGaussian() { return 0; }
        };
        optimizer = new MultiStartUnivariateRealOptimizer<FUNC>(mockOptimizer, 3, generator);
    }

    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checker = new MockConvergenceChecker<UnivariateRealPointValuePair>();
        optimizer.setConvergenceChecker(checker);
        assertEquals(checker, mockOptimizer.getConvergenceChecker());
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checker = new MockConvergenceChecker<UnivariateRealPointValuePair>();
        mockOptimizer.setConvergenceChecker(checker);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = 100;
        mockOptimizer.setMaxEvaluations(maxEvaluations);
        assertEquals(maxEvaluations, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        int evaluations = 50;
        mockOptimizer.setEvaluations(evaluations);
        assertEquals(evaluations, optimizer.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 100;
        optimizer.setMaxEvaluations(maxEvaluations);
        assertEquals(maxEvaluations, mockOptimizer.getMaxEvaluations());
    }

    @Test
    public void testGetOptima() {
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertNull(optima);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaWithoutOptimize() {
        optimizer.getOptima();
    }

    @Test
    public void testOptimize() throws FunctionEvaluationException {
        double min = 0.0;
        double max = 1.0;
        GoalType goal = GoalType.MINIMIZE;
        mockOptimizer.setOptimum(new UnivariateRealPointValuePair(0.5, 1.0));
        UnivariateRealPointValuePair result = optimizer.optimize(null, goal, min, max);
        assertEquals(0.5, result.getPoint(), 0.0);
        assertEquals(1.0, result.getValue(), 0.0);
    }

    @Test
    public void testOptimizeWithStartValue() throws FunctionEvaluationException {
        double min = 0.0;
        double max = 1.0;
        GoalType goal = GoalType.MAXIMIZE;
        double startValue = 0.1;
        mockOptimizer.setOptimum(new UnivariateRealPointValuePair(0.5, 1.0));
        UnivariateRealPointValuePair result = optimizer.optimize(null, goal, min, max, startValue);
        assertEquals(0.5, result.getPoint(), 0.0);
        assertEquals(1.0, result.getValue(), 0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithNoConvergence() throws FunctionEvaluationException {
        double min = 0.0;
        double max = 1.0;
        GoalType goal = GoalType.MINIMIZE;
        mockOptimizer.setException(new ConvergenceException(LocalizedFormats.NO_CONVERGENCE, 100));
        optimizer.optimize(null, goal, min, max);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithNoConvergenceAndStartValue() throws FunctionEvaluationException {
        double min = 0.0;
        double max = 1.0;
        GoalType goal = GoalType.MINIMIZE;
        double startValue = 0.1;
        mockOptimizer.setException(new ConvergenceException(LocalizedFormats.NO_CONVERGENCE, 100));
        optimizer.optimize(null, goal, min, max, startValue);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeWithFunctionEvaluationException() throws FunctionEvaluationException {
        double min = 0.0;
        double max = 1.0;
        GoalType goal = GoalType.MINIMIZE;
        mockOptimizer.setException(new FunctionEvaluationException(1.0));
        optimizer.optimize(null, goal, min, max);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeWithFunctionEvaluationExceptionAndStartValue() throws FunctionEvaluationException {
        double min = 0.0;
        double max = 1.0;
        GoalType goal = GoalType.MINIMIZE;
        double startValue = 0.1;
        mockOptimizer.setException(new FunctionEvaluationException(1.0));
        optimizer.optimize(null, goal, min, max, startValue);
    }

    @Test
    public void testSortPairsMinimize() {
        GoalType goal = GoalType.MINIMIZE;
        UnivariateRealPointValuePair p1 = new UnivariateRealPointValuePair(1.0, 2.0);
        UnivariateRealPointValuePair p2 = new UnivariateRealPointValuePair(2.0, 1.0);
        UnivariateRealPointValuePair p3 = new UnivariateRealPointValuePair(3.0, 3.0);
        UnivariateRealPointValuePair[] pairs = {p3, p1, p2};
        optimizer.sortPairs(goal);
        assertArrayEquals(new UnivariateRealPointValuePair[]{p2, p1, p3}, pairs);
    }

    @Test
    public void testSortPairsMaximize() {
        GoalType goal = GoalType.MAXIMIZE;
        UnivariateRealPointValuePair p1 = new UnivariateRealPointValuePair(1.0, 2.0);
        UnivariateRealPointValuePair p2 = new UnivariateRealPointValuePair(2.0, 1.0);
        UnivariateRealPointValuePair p3 = new UnivariateRealPointValuePair(3.0, 3.0);
        UnivariateRealPointValuePair[] pairs = {p3, p1, p2};
        optimizer.sortPairs(goal);
        assertArrayEquals(new UnivariateRealPointValuePair[]{p3, p1, p2}, pairs);
    }

    private class MockOptimizer<FUNC extends UnivariateRealFunction>
            implements BaseUnivariateRealOptimizer<FUNC> {

        private ConvergenceChecker<UnivariateRealPointValuePair> checker;
        private int maxEvaluations;
        private int evaluations;
        private UnivariateRealPointValuePair optimum;
        private Exception exception;

        public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
            this.checker = checker;
        }

        public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
            return checker;
        }

        public int getMaxEvaluations() {
            return maxEvaluations;
        }

        public void setMaxEvaluations(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        public int getEvaluations() {
            return evaluations;
        }

        public void setEvaluations(int evaluations) {
            this.evaluations = evaluations;
        }

        public UnivariateRealPointValuePair optimize(UnivariateRealFunction f,
                                                     GoalType goal,
                                                     double min,
                                                     double max)
                throws FunctionEvaluationException, ConvergenceException {
            if (exception != null) {
                if (exception instanceof FunctionEvaluationException) {
                    throw (FunctionEvaluationException) exception;
                } else if (exception instanceof ConvergenceException) {
                    throw (ConvergenceException) exception;
                }
            }
            return optimum;
        }

        public void setOptimum(UnivariateRealPointValuePair optimum) {
            this.optimum = optimum;
        }

        public void setException(Exception exception) {
            this.exception = exception;
        }
    }

    private class MockConvergenceChecker<V> implements ConvergenceChecker<V> {
        public boolean converged(int iteration,
                                 V previous,
                                 V current) {
            return false;
        }
    }

}