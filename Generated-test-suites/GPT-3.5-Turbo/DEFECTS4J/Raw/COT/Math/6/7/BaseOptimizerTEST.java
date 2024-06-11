
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.optimization.BaseOptimizer;

public class BaseOptimizerTest {

    private BaseOptimizer<Object> optimizer;

    @Before
    public void setUp() {
        optimizer = new BaseOptimizer<Object>() {
            public int getMaxEvaluations() {
                return 0;
            }
            public int getEvaluations() {
                return 0;
            }
            public ConvergenceChecker<Object> getConvergenceChecker() {
                return null;
            }
        };
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
        ConvergenceChecker<Object> checker = optimizer.getConvergenceChecker();
        assertNull(checker);
    }
}
