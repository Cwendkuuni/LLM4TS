import org.junit.Before;
import org.junit.Test;

public class BaseOptimizerTest {

    private BaseOptimizer<PAIR> optimizer;
    
    @Before
    public void setup() {
        optimizer = new TestableBaseOptimizer();
    }

    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(0, maxEvaluations);
    }

    @Test
    public void testGetEvaluations_NoOptimizeCalled() {
        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations);
    }

    // Edge case: Call optimize method and then check number of evaluations
    @Test
    public void testGetEvaluations_AfterOptimizeCalled() {
        // TODO: Create a mock objective function and a mock convergence checker
        // Call the optimize method
        // Check the number of evaluations
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull(convergenceChecker);
    }
}

class TestableBaseOptimizer implements BaseOptimizer<PAIR> {
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
        return new DefaultConvergenceChecker<PAIR>();
    }
}