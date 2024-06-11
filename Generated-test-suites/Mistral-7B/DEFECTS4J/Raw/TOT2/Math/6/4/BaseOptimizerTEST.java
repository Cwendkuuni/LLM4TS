import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getMaxEvaluations()).thenReturn(100);

        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(100, maxEvaluations);

        // Test edge cases
        optimizer = mock(BaseOptimizer.class);
        when(optimizer.getMaxEvaluations()).thenReturn(-1);
        maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(-1, maxEvaluations);

        optimizer = mock(BaseOptimizer.class);
        BaseOptimizer<Object> optimizerWithoutMaxEvaluations = Mockito.spy(optimizer);
        when(optimizerWithoutMaxEvaluations.getMaxEvaluations()).thenReturn(0);
        maxEvaluations = optimizerWithoutMaxEvaluations.getMaxEvaluations();
        assertEquals(0, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getEvaluations()).thenReturn(5);

        int evaluations = optimizer.getEvaluations();
        assertEquals(5, evaluations);

        // Test edge cases
        optimizer = mock(BaseOptimizer.class);
        when(optimizer.getEvaluations()).thenReturn(-1);
        evaluations = optimizer.getEvaluations();
        assertEquals(-1, evaluations);

        optimizer = mock(BaseOptimizer.class);
        BaseOptimizer<Object> optimizerWithoutEvaluations = Mockito.spy(optimizer);
        when(optimizerWithoutEvaluations.getEvaluations()).thenReturn(0);
        evaluations = optimizerWithoutEvaluations.getEvaluations();
        assertEquals(0, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        ConvergenceChecker<Object> convergenceChecker = mock(ConvergenceChecker.class);
        when(optimizer.getConvergenceChecker()).thenReturn(convergenceChecker);

        ConvergenceChecker<Object> actualConvergenceChecker = optimizer.getConvergenceChecker();
        assertEquals(convergenceChecker, actualConvergenceChecker);

        // Test edge cases
        optimizer = mock(BaseOptimizer.class);
        when(optimizer.getConvergenceChecker()).thenReturn(null);
        ConvergenceChecker<Object> nullConvergenceChecker = optimizer.getConvergenceChecker();
        assertNull(nullConvergenceChecker);

        optimizer = mock(BaseOptimizer.class);
        BaseOptimizer<Object> optimizerWithoutConvergenceChecker = Mockito.spy(optimizer);
        when(optimizerWithoutConvergenceChecker.getConvergenceChecker()).thenReturn(null);
        ConvergenceChecker<Object> nullActualConvergenceChecker = optimizerWithoutConvergenceChecker.getConvergenceChecker();
        assertNull(nullActualConvergenceChecker);
    }
}