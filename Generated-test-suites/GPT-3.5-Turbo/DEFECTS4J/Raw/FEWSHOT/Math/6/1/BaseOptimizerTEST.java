import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.junit.Before;
import org.junit.Test;

public class BaseOptimizerTest {
    
    private BaseOptimizer<Integer> optimizer;
    
    @Before
    public void setUp() {
        optimizer = new BaseOptimizerImpl<>();
    }
    
    @Test
    public void testGetMaxEvaluations() {
        assertEquals(100, optimizer.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        assertEquals(0, optimizer.getEvaluations());
    }
    
    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<Integer> convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull(convergenceChecker);
    }
}

class BaseOptimizerImpl<Integer> implements BaseOptimizer<Integer> {
    @Override
    public int getMaxEvaluations() {
        return 100;
    }
    
    @Override
    public int getEvaluations() {
        return 0;
    }
    
    @Override
    public ConvergenceChecker<Integer> getConvergenceChecker() {
        return new ConvergenceCheckerImpl<>();
    }
}

class ConvergenceCheckerImpl<Integer> implements ConvergenceChecker<Integer> {
    @Override
    public boolean converged(int iteration, PAIR previous, PAIR current) {
        return false;
    }
}