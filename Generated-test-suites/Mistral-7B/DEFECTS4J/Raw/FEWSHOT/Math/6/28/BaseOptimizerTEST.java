package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.nonlinear.SimpleBoundingMethod;
import org.apache.commons.math3.optim.nonlinear.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.convergence.ConvergenceCheckerUtils;
import org.apache.commons.math3.optim.nonlinear.convergence.RStoppingValue;
import org.apache.commons.math3.optim.point.PointValuePairCollection;
import org.junit.Test;

public class BaseOptimizerTest<PAIR extends PointValuePair> {
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new SimpleBoundingMethod(new AroundMinimumInitializer(1.0), new RStoppingValue(100));
        assertEquals(100, optimizer.getMaxEvaluations());

        optimizer = new SimpleBoundingMethod(new AroundMinimumInitializer(1.0), null);
        assertNull(optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = new SimpleBoundingMethod(new AroundMinimumInitializer(1.0), new RStoppingValue(100));
        optimizer.optimize(new Object[]{}, new double[]{}, new String[0]);
        assertEquals(1, optimizer.getEvaluations());

        optimizer.optimize(new Object[]{}, new double[]{}, new String[0]);
        assertEquals(2, optimizer.getEvaluations());

        optimizer = new SimpleBoundingMethod(new AroundMinimumInitializer(1.0), null);
        optimizer.optimize(new Object[]{}, new double[]{}, new String[0]);
        assertNull(optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new SimpleBoundingMethod(new AroundMinimumInitializer(1.0), new RStoppingValue(100));
        ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
        assertNull(convergenceChecker);

        optimizer = new SimpleBoundingMethod(new AroundMinimumInitializer(1.0), new ConvergenceCheckerUtils.SimpleCheckpoint(1.0));
        convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull(convergenceChecker);
    }
}