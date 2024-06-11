import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseOptimizerTest {

    private BaseOptimizer<String> optimizer;

    @Before
    public void setUp() {
        optimizer = new BaseOptimizer<String>() {
            @Override
            public int getMaxEvaluations() {
                return 0;
            }

            @Override
            public int getEvaluations() {
                return 0;
            }

            @Override
            public ConvergenceChecker<String> getConvergenceChecker() {
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
        ConvergenceChecker<String> convergenceChecker = optimizer.getConvergenceChecker();
        assertNull(convergenceChecker);
    }

}