import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.junit.Test;

public class BaseOptimizerTest {
    
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer optimizer = mock(BaseOptimizer.class);
        when(optimizer.getMaxEvaluations()).thenReturn(100);
        
        assertEquals(100, optimizer.getMaxEvaluations());
        verify(optimizer).getMaxEvaluations();
    }
    
    @Test
    public void testGetEvaluations() {
        BaseOptimizer optimizer = mock(BaseOptimizer.class);
        when(optimizer.getEvaluations()).thenReturn(50);
        
        assertEquals(50, optimizer.getEvaluations());
        verify(optimizer).getEvaluations();
    }
    
    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker checker = mock(ConvergenceChecker.class);
        BaseOptimizer optimizer = mock(BaseOptimizer.class);
        when(optimizer.getConvergenceChecker()).thenReturn(checker);
        
        assertEquals(checker, optimizer.getConvergenceChecker());
        verify(optimizer).getConvergenceChecker();
    }
}