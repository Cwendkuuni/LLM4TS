package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.SimpleConvergenceChecker;
import org.apache.commons.math3.optim.SimpleOptimizationProblem;
import org.junit.Test;

public class BaseOptimizerTest<PAIR extends PointValuePair> {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = mock(BaseOptimizer.class);
        assertEquals(0, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = mock(BaseOptimizer.class);
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = mock(BaseOptimizer.class);
        ConvergenceChecker<PAIR> convergenceChecker = mock(ConvergenceChecker.class);
        optimizer = (BaseOptimizer<PAIR>) ((Object) optimizer)[0]; // Set the field programmatically
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetConvergenceCheckerNull() {
        BaseOptimizer<PAIR> optimizer = mock(BaseOptimizer.class);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testOptimizeWithDefaults() {
        BaseOptimizer<PAIR> optimizer = mock(BaseOptimizer.class);
        SimpleOptimizationProblem<Double, Double> problem = new SimpleOptimizationProblem<>(x -> x * x, new SimpleBounds(-10.0, 10.0));
        optimizer.optimize(problem);
        ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
        assertEquals(convergenceChecker.getClass(), SimpleConvergenceChecker.class);
    }

    @Test
    public void testOptimizeWithCustomConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = mock(BaseOptimizer.class);
        ConvergenceChecker<PAIR> customConvergenceChecker = mock(ConvergenceChecker.class);
        optimizer = (BaseOptimizer<PAIR>) ((Object) optimizer)[0]; // Set the field programmatically
        optimizer.setConvergenceChecker(customConvergenceChecker);
        SimpleOptimizationProblem<Double, Double> problem = new SimpleOptimizationProblem<>(x -> x * x, new SimpleBounds(-10.0, 10.0));
        optimizer.optimize(problem);
        assertEquals(customConvergenceChecker, optimizer.getConvergenceChecker());
    }
}