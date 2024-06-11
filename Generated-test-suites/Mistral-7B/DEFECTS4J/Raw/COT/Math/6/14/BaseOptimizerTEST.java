package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MaxEval;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for BaseOptimizer interface.
 */
public class BaseOptimizerTest {

    /**
     * Test getMaxEvaluations method.
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = createMockOptimizer();
        int maxEvaluations = optimizer.getMaxEvaluations();
        int expectedMaxEvaluations = 100;
        assertEquals("Unexpected maximal number of function evaluations", expectedMaxEvaluations, maxEvaluations);
    }

    /**
     * Test getEvaluations method.
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Object> optimizer = createMockOptimizer();
        int evaluations = optimizer.getEvaluations();
        int expectedEvaluations = 5;
        assertEquals("Unexpected number of evaluations", expectedEvaluations, evaluations);
    }

    /**
     * Test getConvergenceChecker method.
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Object> optimizer = createMockOptimizer();
        ConvergenceChecker<Object> convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull("Convertergence checker should not be null", convergenceChecker);
    }

    /**
     * Test method getMaxEvaluations with illegal argument.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMaxEvaluationsIllegalArgument() {
        BaseOptimizer<Object> optimizer = createMockOptimizer();
        optimizer.setMaxEvaluations(-1);
        int maxEvaluations = optimizer.getMaxEvaluations();
    }

    /**
     * Test method getEvaluations with illegal argument.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetEvaluationsIllegalArgument() {
        BaseOptimizer<Object> optimizer = createMockOptimizer();
        optimizer.setEvaluations(-1);
        int evaluations = optimizer.getEvaluations();
    }

    /**
     * Test method getConvergenceChecker with illegal argument.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetConvergenceCheckerIllegalArgument() {
        BaseOptimizer<Object> optimizer = createMockOptimizer();
        optimizer.setConvergenceChecker(null);
        ConvergenceChecker<Object> convergenceChecker = optimizer.getConvergenceChecker();
    }

    /**
     * Creates a mock optimizer for testing.
     *
     * @return a mock optimizer.
     */
    private BaseOptimizer<Object> createMockOptimizer() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getMaxEvaluations()).thenReturn(new MaxEval(100));
        when(optimizer.getEvaluations()).thenReturn(new MaxEval(5));
        when(optimizer.getConvergenceChecker()).thenReturn(mock(ConvergenceChecker.class));
        return optimizer;
    }
}