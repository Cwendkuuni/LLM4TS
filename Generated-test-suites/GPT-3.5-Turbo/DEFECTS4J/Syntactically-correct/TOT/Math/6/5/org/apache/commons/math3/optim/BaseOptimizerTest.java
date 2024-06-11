import static org.junit.Assert.*;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.junit.Before;
import org.junit.Test;

public class BaseOptimizerTest {

    private BaseOptimizer<Double> optimizer;
    
    @Before
    public void setUp() {
        optimizer = new BaseOptimizer<Double>() {
            public int getMaxEvaluations() {
                return 100;
            }
            
            public int getEvaluations() {
                return 50;
            }
            
            public ConvergenceChecker<Double> getConvergenceChecker() {
                return null;
            }
        };
    }
    
    @Test
    public void testGetMaxEvaluations() {
        assertEquals(100, optimizer.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        assertEquals(50, optimizer.getEvaluations());
    }
    
    @Test
    public void testGetConvergenceChecker() {
        assertNull(optimizer.getConvergenceChecker());
    }
    
}