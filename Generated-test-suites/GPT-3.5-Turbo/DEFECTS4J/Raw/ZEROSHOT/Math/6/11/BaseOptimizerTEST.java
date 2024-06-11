import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.optimization.BaseOptimizer;

public class BaseOptimizerTest {

    private BaseOptimizer<Object> optimizer;

    @Before
    public void setUp() {
        optimizer = new BaseOptimizer<Object>() {
            public int getMaxEvaluations() { return 0; }
            public int getEvaluations() { return 0; }
            public ConvergenceChecker<Object> getConvergenceChecker() { return null; }
        };
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
    public void testGetConvergenceChecker() {
        assertNull(optimizer.getConvergenceChecker());
    }
}