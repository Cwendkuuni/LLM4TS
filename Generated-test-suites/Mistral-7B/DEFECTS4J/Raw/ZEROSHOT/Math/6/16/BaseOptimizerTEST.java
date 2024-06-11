import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.convergence.ConvergenceChecker;
import org.apache.commons.math3.optimization.convergence.ConvergenceTest;
import org.apache.commons.math3.optimization.max.MaxEvaluator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link BaseOptimizer}.
 */
public class BaseOptimizerTest<PAIR> {

    private BaseOptimizer<PAIR> optimizer;
    private ConvergenceChecker<PAIR> convergenceChecker;
    private MaxEvaluator<Double> maxEvaluator;

    @Before
    public void setUp() {
        optimizer = new DummyBaseOptimizer<>();
        convergenceChecker = new ConvergenceTest(1.0E-6, 50);
        maxEvaluator = new MaxEvaluator<>(new DummyFunction(), new DummyVectorFunction(), new DummyVectorFunctionGradient());
    }

    /**
     * Test getMaxEvaluations method.
     */
    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals("Maximum number of function evaluations should be set", 0, maxEvaluations);

        optimizer = new DummyBaseOptimizer<>(50);
        maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals("Maximum number of function evaluations should be set to 50", 50, maxEvaluations);
    }

    /**
     * Test getEvaluations method.
     */
    @Test
    public void testGetEvaluations() {
        int evaluations = optimizer.getEvaluations();
        assertEquals("Number of evaluations should be zero before optimization", 0, evaluations);

        optimizer.optimize(convergenceChecker, maxEvaluator);
        evaluations = optimizer.getEvaluations();
        int expectedEvaluations = convergenceChecker.getMaxIterations();
        assertEquals("Number of evaluations should be equal to the maximum number of iterations", expectedEvaluations, evaluations);
    }

    /**
     * Test getConvergenceChecker method.
     */
    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
        assertNull("Convergence checker should be null by default", convergenceChecker);

        optimizer = new DummyBaseOptimizer<>(new MyConvergenceChecker<>());
        convergenceChecker = optimizer.getConvergenceChecker();
        assertEquals("Convergence checker should be an instance of MyConvergenceChecker", MyConvergenceChecker.class, convergenceChecker.getClass());
    }

    /**
     * Test optimization with no maximum number of evaluations.
     */
    @Test(expected = IllegalStateException.class)
    public void testOptimizeNoMaxEvaluations() {
        optimizer.optimize(convergenceChecker, maxEvaluator);
    }

    /**
     * Test optimization with maximum number of evaluations.
     */
    @Test
    public void testOptimizeMaxEvaluations() {
        optimizer = new DummyBaseOptimizer<>(MaxEval.UNLIMITED);
        optimizer.optimize(convergenceChecker, maxEvaluator);

        int evaluations = optimizer.getEvaluations();
        int expectedEvaluations = convergenceChecker.getMaxIterations();
        assertEquals("Number of evaluations should be equal to the maximum number of iterations", expectedEvaluations, evaluations);
    }

    /**
     * Test optimization with maximum number of evaluations reached.
     */
    @Test
    public void testOptimizeMaxEvaluationsReached() {
        int maxEvaluations = 10;
        optimizer = new DummyBaseOptimizer<>(maxEvaluations);

        optimizer.optimize(convergenceChecker, maxEvaluator);
        int evaluations = optimizer.getEvaluations();
        int expectedEvaluations = convergenceChecker.getMaxIterations();

        assertTrue("Number of evaluations should be less than or equal to the maximum number of iterations", evaluations <= expectedEvaluations);
    }

    /**
     * Test optimization with convergence.
     */
    @Test
    public void testOptimizeConvergence() {
        optimizer = new DummyBaseOptimizer<>(new MyConvergenceCheckerPassing());
        optimizer.optimize(convergenceChecker, maxEvaluator);
        assertTrue("Optimization should have converged", convergenceChecker.isConverged());
    }

    /**
     * Test optimization without convergence.
     */
    @Test
    public void testOptimizeNoConvergence() {
        optimizer = new DummyBaseOptimizer<>(new MyConvergenceCheckerFailing());
        optimizer.optimize(convergenceChecker, maxEvaluator);
        assertFalse("Optimization should not have converged", convergenceChecker.isConverged());
    }

    /**
     * Dummy implementation of {@link BaseOptimizer}.
     */
    private static class DummyBaseOptimizer<PAIR> implements BaseOptimizer<PAIR> {
        private int maxEvaluations;

        public DummyBaseOptimizer() {
            this(0);
        }

        public DummyBaseOptimizer(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        @Override
        public int getMaxEvaluations() {
            return maxEvaluations;
        }

        @Override
        public ConvergenceChecker<PAIR> getConvergenceChecker() {
            return null;
        }

        @Override
        public void optimize(ConvergenceChecker<PAIR> convergenceChecker, MaxEval<PAIR> maxEvaluator) {
            // TODO Implement optimization
        }
    }

    /**
     * Dummy implementation of {@link ConvergenceChecker}.
     */
    private static class MyConvergenceCheckerPassing implements ConvergenceChecker<PAIR> {
        @Override
        public boolean isConverged() {
            return true;
        }

        @Override
        public int getMaxIterations() {
            return 10;
        }
    }

    /**
     * Dummy implementation of {@link ConvergenceChecker}.
     */
    private static class MyConvergenceCheckerFailing implements ConvergenceChecker<PAIR> {
        @Override
        public boolean isConverged() {
            return false;
        }

        @Override
        public int getMaxIterations() {
            return 10;
        }
    }
}