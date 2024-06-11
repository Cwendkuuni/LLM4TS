import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    BaseOptimizer<String> optimizer;

    @Before
    public void setUp() {
        optimizer = new BaseOptimizer<>();
    }

    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(0, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<String> checker = optimizer.getConvergenceChecker();
        assertNull(checker);
    }
}