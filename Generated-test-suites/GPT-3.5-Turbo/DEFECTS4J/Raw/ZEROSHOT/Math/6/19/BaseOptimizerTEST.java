import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optimization.*;

public class BaseOptimizerTest {
    
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = new BaseOptimizer<Object>() {
            public int getMaxEvaluations() {
                return 100;
            }
            public int getEvaluations() {
                return 0;
            }
            public ConvergenceChecker<Object> getConvergenceChecker() {
                return null;
            }
        };
        assertEquals(100, optimizer.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Object> optimizer = new BaseOptimizer<Object>() {
            public int getMaxEvaluations() {
                return 0;
            }
            public int getEvaluations() {
                return 10;
            }
            public ConvergenceChecker<Object> getConvergenceChecker() {
                return null;
            }
        };
        assertEquals(10, optimizer.getEvaluations());
    }
    
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Object> optimizer = new BaseOptimizer<Object>() {
            public int getMaxEvaluations() {
                return 0;
            }
            public int getEvaluations() {
                return 0;
            }
            public ConvergenceChecker<Object> getConvergenceChecker() {
                return new ConvergenceChecker<Object>() {
                    public boolean converged(int iteration, Object previous, Object current) {
                        return false;
                    }
                };
            }
        };
        assertNotNull(optimizer.getConvergenceChecker());
    }
}