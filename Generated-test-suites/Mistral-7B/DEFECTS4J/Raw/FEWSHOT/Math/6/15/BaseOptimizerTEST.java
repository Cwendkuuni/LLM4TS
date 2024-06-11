package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.junit.Test;

/**
 * Test cases for BaseOptimizer interface.
 */
public class BaseOptimizerTest {

    /**
     * Test getMaxEvaluations method.
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new DummyOptimizer();
        assertEquals(MaxEval.UNLIMITED, optimizer.getMaxEvaluations());
    }

    /**
     * Test getEvaluations method.
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new DummyOptimizer();
        assertEquals(0, optimizer.getEvaluations());
    }

    /**
     * Test getConvergenceChecker method.
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new DummyOptimizer();
        assertNull(optimizer.getConvergenceChecker());

        optimizer = new DummyOptimizerWithChecker();
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
        assertEquals(0.0, convergenceChecker.getTolerance(), 0);
        assertEquals(0.0, convergenceChecker.getMaxIterations(), 0);
        assertEquals(SimpleValueChecker.class, convergenceChecker.getClass());
    }
}

class DummyOptimizer implements BaseOptimizer<PointValuePair> {
    @Override
    public int getMaxEvaluations() {
        return MaxEval.UNLIMITED;
    }

    @Override
    public int getEvaluations() {
        return 0;
    }

    @Override
    public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
        return null;
    }
}

class DummyOptimizerWithChecker extends DummyOptimizer {
    @Override
    public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
        return new SimpleValueChecker(0.0, 0.0);
    }
}