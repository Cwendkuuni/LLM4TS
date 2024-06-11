import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer;

    @Before
    public void setUp() {
        // Initialize the optimizer with a mock single-start optimizer and 3 starts
        BaseUnivariateRealOptimizer<UnivariateRealFunction> mockOptimizer = new MockOptimizer();
        RandomGenerator generator = new MockRandomGenerator();
        optimizer = new MultiStartUnivariateRealOptimizer<>(mockOptimizer, 3, generator);
    }

    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checker = new MockConvergenceChecker();
        optimizer.setConvergenceChecker(checker);
        // Assert that the convergence checker is set correctly
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(0, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        optimizer.setMaxEvaluations(100);
        // Assert that the max evaluations is set correctly
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetOptima_NoOptimumComputed() {
        try {
            optimizer.getOptima();
            // Assert that a MathIllegalStateException is thrown when no optimum is computed yet
            fail("Expected MathIllegalStateException");
        } catch (MathIllegalStateException e) {
            assertEquals(LocalizedFormats.NO_OPTIMUM_COMPUTED_YET, e.getPattern());
        }
    }

    @Test
    public void testOptimize_SingleStart() throws Exception {
        UnivariateRealPointValuePair result = optimizer.optimize(null, GoalType.MINIMIZE, 0, 1);
        // Assert that the result is not null
        assertNotNull(result);
        // Assert that the mock optimizer optimize method is called with the correct arguments
        MockOptimizer mockOptimizer = (MockOptimizer) optimizer.optimizer;
        assertEquals(null, mockOptimizer.f);
        assertEquals(GoalType.MINIMIZE, mockOptimizer.goal);
        assertEquals(0, mockOptimizer.min, 0);
        assertEquals(1, mockOptimizer.max, 0);
        assertTrue(mockOptimizer.optimizeMethodCalled);
    }

    @Test
    public void testOptimize_MultiStart() throws Exception {
        // Set up the mock optimizer to throw a FunctionEvaluationException on the first start
        BaseUnivariateRealOptimizer<UnivariateRealFunction> mockOptimizer = new MockOptimizerWithException();
        optimizer = new MultiStartUnivariateRealOptimizer<>(mockOptimizer, 3, new MockRandomGenerator());

        UnivariateRealPointValuePair result = optimizer.optimize(null, GoalType.MINIMIZE, 0, 1);
        // Assert that the result is not null
        assertNotNull(result);
        // Assert that the optima array contains the result from the second start (the first start will throw an exception)
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertNotNull(optima);
        assertEquals(3, optima.length);
        assertNull(optima[0]);
        assertNull(optima[1]);
        assertNotNull(optima[2]);
    }

    @Test
    public void testOptimize_NoConvergenceWithAnyStartPoint() {
        // Set up the mock optimizer to throw a ConvergenceException on all starts
        BaseUnivariateRealOptimizer<UnivariateRealFunction> mockOptimizer = new MockOptimizerWithConvergenceException();
        optimizer = new MultiStartUnivariateRealOptimizer<>(mockOptimizer, 3, new MockRandomGenerator());

        try {
            optimizer.optimize(null, GoalType.MINIMIZE, 0, 1);
            // Assert that a ConvergenceException is thrown when no convergence is achieved with any start point
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            assertEquals(LocalizedFormats.NO_CONVERGENCE_WITH_ANY_START_POINT, e.getPattern());
            assertEquals(3, e.getArguments()[0]);
        }
    }

    // Mock classes

    private static class MockOptimizer implements BaseUnivariateRealOptimizer<UnivariateRealFunction> {

        private UnivariateRealFunction f;
        private GoalType goal;
        private double min;
        private double max;
        private boolean optimizeMethodCalled;

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
        public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) {
            this.f = f;
            this.goal = goal;
            this.min = min;
            this.max = max;
            this.optimizeMethodCalled = true;
            return null;
        }
    }

    private static class MockConvergenceChecker implements ConvergenceChecker<UnivariateRealPointValuePair> {

        @Override
        public boolean converged(int iteration, UnivariateRealPointValuePair previous, UnivariateRealPointValuePair current) {
            return true;
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

    private static class MockOptimizerWithException extends MockOptimizer {

        @Override
        public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) {
            throw new FunctionEvaluationException(null);
        }
    }

    private static class MockOptimizerWithConvergenceException extends MockOptimizer {

        @Override
        public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) {
            throw new ConvergenceException(null);
        }
    }
}