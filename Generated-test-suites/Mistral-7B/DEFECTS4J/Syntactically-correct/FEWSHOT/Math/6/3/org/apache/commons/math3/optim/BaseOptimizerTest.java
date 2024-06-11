package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MaxEval;
import org.junit.Test;

public class BaseOptimizerTest<PAIR> {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new DummyBaseOptimizer<>();
        assertEquals(0, optimizer.getMaxEvaluations());

        optimizer = new DummyBaseOptimizer<>(10);
        assertEquals(10, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = new DummyBaseOptimizer<>();
        assertEquals(0, optimizer.getEvaluations());

        optimizer.optimize(new DummyObjectiveFunction(), new DummyPoint(), new DummyInitializer());
        assertEquals(1, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new DummyBaseOptimizer<>();
        assertNull(optimizer.getConvergenceChecker());

        optimizer = new DummyBaseOptimizer<>(new MaxEval(10));
        assertNull(optimizer.getConvergenceChecker());

        optimizer = new DummyBaseOptimizer<>(new ConvergenceChecker<PAIR>() {
            @Override
            public boolean isConverged(PAIR currentValue, PAIR previousValue) {
                return false;
            }
        });
        assertNotNull(optimizer.getConvergenceChecker());
    }
}

abstract class DummyBaseOptimizer<PAIR> implements BaseOptimizer<PAIR> {
    private int maxEvaluations = 0;
    private ConvergenceChecker<PAIR> convergenceChecker;

    public DummyBaseOptimizer() {
        this(null);
    }

    public DummyBaseOptimizer(int maxEvaluations) {
        this(maxEvaluations, null);
    }

    public DummyBaseOptimizer(ConvergenceChecker<PAIR> convergenceChecker) {
        this(0, convergenceChecker);
    }

    public DummyBaseOptimizer(int maxEvaluations, ConvergenceChecker<PAIR> convergenceChecker) {
        this.maxEvaluations = maxEvaluations;
        this.convergenceChecker = convergenceChecker;
    }

    @Override
    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    @Override
    public int getEvaluations() {
        int currentEvaluations = getEvaluations();
        optimize(new DummyObjectiveFunction(), new DummyPoint(), new DummyInitializer());
        return getEvaluations() - currentEvaluations;
    }

    @Override
    public ConvergenceChecker<PAIR> getConvergenceChecker() {
        return convergenceChecker;
    }
}

class DummyObjectiveFunction implements ObjectiveFunction<DummyPoint, Double> {
    @Override
    public Double value(DummyPoint point) {
        return 0.0;
    }
}

class DummyPoint implements Point {
}

class DummyInitializer implements Initializer<DummyPoint> {
    @Override
    public DummyPoint initialize() {
        return new DummyPoint();
    }
}