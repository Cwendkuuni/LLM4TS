import org.apache.commons.math3.optim.MaxEvalException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnconvergedException;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.math3.optim.PointValuePair.makePair;

public class BaseOptimizerTest<PAIR extends PointValuePair<Object, Number>> {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getMaxEvaluations()).thenReturn(100);

        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(100, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getEvaluations()).thenReturn(0);

        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations);

        optimizer.optimize(anyDouble(), anyDoubleArray());
        evaluations = optimizer.getEvaluations();
        assertEquals(1, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = mock(BaseOptimizer.class);
        ConvergenceChecker<PAIR> convergenceChecker = mock(ConvergenceChecker.class);
        when(optimizer.getConvergenceChecker()).thenReturn(convergenceChecker);

        ConvergenceChecker<PAIR> checker = optimizer.getConvergenceChecker();
        assertEquals(convergenceChecker, checker);
    }

    @Test
    public void testMaxEvalException() {
        BaseOptimizer<PAIR> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getMaxEvaluations()).thenReturn(1);

        try {
            optimizer.optimize(anyDouble(), anyDoubleArray());
            fail("Should have thrown MaxEvalException");
        } catch (MaxEvalException e) {
            // Expected exception
        }
    }

    @Test
    public void testUnconvergedException() {
        BaseOptimizer<PAIR> optimizer = mock(BaseOptimizer.class);
        ConvergenceChecker<PAIR> convergenceChecker = mock(ConvergenceChecker.class);
        when(convergenceChecker.isConverged(any(PAIR.class))).thenReturn(false);
        when(optimizer.getConvergenceChecker()).thenReturn(convergenceChecker);

        try {
            optimizer.optimize(anyDouble(), anyDoubleArray());
            fail("Should have thrown UnconvergedException");
        } catch (UnconvergedException e) {
            // Expected exception
        }
    }
}