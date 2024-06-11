import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BaseOptimizerTest {

    private BaseOptimizer baseOptimizer;

    @Before
    public void setUp() {
        baseOptimizer = new MockBaseOptimizer();
    }

    @After
    public void tearDown() {
        baseOptimizer = null;
    }

    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = baseOptimizer.getMaxEvaluations();
        assertEquals(0, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        int evaluations = baseOptimizer.getEvaluations();
        assertEquals(0, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker convergenceChecker = baseOptimizer.getConvergenceChecker();
        assertNotNull(convergenceChecker);
    }

    // Edge case: maxEvaluations is set to a positive value
    @Test
    public void testGetMaxEvaluationsWithPositiveValue() {
        int maxEvaluations = 100;
        baseOptimizer.setMaxEvaluations(maxEvaluations);
        assertEquals(maxEvaluations, baseOptimizer.getMaxEvaluations());
    }

    // Error scenario: maxEvaluations is set to a negative value
    @Test(expected = IllegalArgumentException.class)
    public void testGetMaxEvaluationsWithNegativeValue() {
        int maxEvaluations = -100;
        baseOptimizer.setMaxEvaluations(maxEvaluations);
    }

    // Typical use case: Some evaluations have been performed
    @Test
    public void testGetEvaluationsAfterOptimize() {
        int evaluations = baseOptimizer.getEvaluations();
        assertEquals(0, evaluations);
        
        baseOptimizer.optimize();
        
        evaluations = baseOptimizer.getEvaluations();
        assertEquals(1, evaluations);
    }

    // Edge case: No evaluations have been performed
    @Test
    public void testGetEvaluationsBeforeOptimize() {
        int evaluations = baseOptimizer.getEvaluations();
        assertEquals(0, evaluations);
    }

    // Edge case: Object initialized without a convergence checker
    @Test
    public void testGetConvergenceCheckerWithoutInitialization() {
        baseOptimizer = new BaseOptimizerWithoutConvergenceChecker();
        ConvergenceChecker convergenceChecker = baseOptimizer.getConvergenceChecker();
        assertNotNull(convergenceChecker);
    }

    private class MockBaseOptimizer implements BaseOptimizer {

        private int maxEvaluations;
        private int evaluations;

        public MockBaseOptimizer() {
            this.maxEvaluations = 0;
            this.evaluations = 0;
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
        public ConvergenceChecker<PAIR> getConvergenceChecker() {
            return new MockConvergenceChecker();
        }

        public void setMaxEvaluations(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        public void optimize() {
            evaluations++;
        }
    }

    private class BaseOptimizerWithoutConvergenceChecker implements BaseOptimizer {

        @Override
        public int getMaxEvaluations() {
            return 0;
        }

        @Override
        public int getEvaluations() {
            return 0;
        }

        @Override
        public ConvergenceChecker<PAIR> getConvergenceChecker() {
            return null;
        }
    }

    private class MockConvergenceChecker implements ConvergenceChecker {

        @Override
        public boolean converged(int iteration, PAIR previous, PAIR current) {
            return false;
        }
    }

}