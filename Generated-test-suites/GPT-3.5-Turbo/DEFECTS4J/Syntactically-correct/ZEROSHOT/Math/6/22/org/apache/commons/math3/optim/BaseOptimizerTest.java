import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BaseOptimizerTest {
    
    private BaseOptimizer optimizer;
    
    @Before
    public void setUp() {
        optimizer = new BaseOptimizer();
    }
    
    @Test
    public void testGetMaxEvaluations() {
        int result = optimizer.getMaxEvaluations();
        assertEquals(0, result);
    }
    
    @Test
    public void testGetEvaluations() {
        int result = optimizer.getEvaluations();
        assertEquals(0, result);
    }
    
    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<PAIR> result = optimizer.getConvergenceChecker();
        assertNotNull(result);
    }
}