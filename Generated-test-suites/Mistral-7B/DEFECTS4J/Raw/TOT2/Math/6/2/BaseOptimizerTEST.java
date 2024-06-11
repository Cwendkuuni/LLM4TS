package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = new BaseOptimizerImpl<>();
        int maxEvaluations = optimizer.getMaxEvaluations();
        int expectedMaxEvaluations = 100;
        assertEquals(expectedMaxEvaluations, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Object> optimizer = new BaseOptimizerImpl<>();
        int evaluations = optimizer.getEvaluations();
        int expectedEvaluations = 0;
        assertEquals(expectedEvaluations, evaluations);

        optimizer.optimize(new PointValuePair());
        evaluations = optimizer.getEvaluations();
        expectedEvaluations = 1;
        assertEquals(expectedEvaluations, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Object> optimizer = new BaseOptimizerImpl<>();
        ConvergenceChecker<Object> convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull(convergenceChecker);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetConvergenceCheckerUninitialized() {
        BaseOptimizer<Object> optimizer = new BaseOptimizerImpl<>();
        ConvergenceChecker<Object> convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull(convergenceChecker);

        // We need to call optimize() before getConvergenceChecker() to check if it's initialized
        optimizer.optimize(new PointValuePair());

        convergenceChecker = optimizer.getConvergenceChecker();
        assertNull(convergenceChecker);
    }

    @Test
    public void testGetMaxEvaluationsNegative() {
        BaseOptimizer<Object> optimizer = new BaseOptimizerImpl<>();
        int maxEvaluations = optimizer.getMaxEvaluations();
        int expectedMaxEvaluations = 100;
        assertEquals(expectedMaxEvaluations, maxEvaluations);

        optimizer.setMaxEvaluations(-1); // Assuming setMaxEvaluations() is a valid method
        maxEvaluations = optimizer.getMaxEvaluations();
        int expectedNegativeMaxEvaluations = -1;
        assertEquals(expectedNegativeMaxEvaluations, maxEvaluations);
    }

    @Test
    public void testGetEvaluationsMultipleCalls() {
        BaseOptimizer<Object> optimizer = new BaseOptimizerImpl<>();
        int evaluations = optimizer.getEvaluations();
        int expectedEvaluations = 0;
        assertEquals(expectedEvaluations, evaluations);

        optimizer.optimize(new PointValuePair());
        evaluations = optimizer.getEvaluations();
        expectedEvaluations = 1;
        assertEquals(expectedEvaluations, evaluations);

        optimizer.optimize(new PointValuePair());
        evaluations = optimizer.getEvaluations();
        expectedEvaluations = 2;
        assertEquals(expectedEvaluations, evaluations);
    }
}