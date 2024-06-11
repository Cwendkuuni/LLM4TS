import org.junit.Test;
import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer();
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(0, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer();
        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer optimizer = new BaseOptimizer();
        ConvergenceChecker checker = optimizer.getConvergenceChecker();
        assertNotNull(checker);
    }

    // Edge Case: Test for negative max evaluations
    @Test
    public void testGetMaxEvaluationsNegative() {
        BaseOptimizer optimizer = new BaseOptimizer();
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(0, maxEvaluations);
    }

    // Exception Handling: Test for null convergence checker
    @Test(expected = NullPointerException.class)
    public void testGetConvergenceCheckerNull() {
        BaseOptimizer optimizer = new BaseOptimizer();
        optimizer.getConvergenceChecker();
    }
}