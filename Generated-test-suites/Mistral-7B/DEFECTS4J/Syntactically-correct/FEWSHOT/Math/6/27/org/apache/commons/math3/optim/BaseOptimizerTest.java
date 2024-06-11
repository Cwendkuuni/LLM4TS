package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker.Status;
import org.apache.commons.math3.optim.initializer.StartingPoint;
import org.junit.Test;

public class BaseOptimizerTest<PAIR extends PointValuePair<Point, Number>> {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new DummyBaseOptimizer<>();
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = new DummyBaseOptimizer<>();
        assertEquals(0, optimizer.getEvaluations());
        optimizer.optimize(new DummyObjectiveFunction(), new DummyStartingPoint());
        assertEquals(1, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceCheckerNull() {
        BaseOptimizer<PAIR> optimizer = new DummyBaseOptimizer<>();
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new DummyBaseOptimizer<>();
        ConvergenceChecker<PAIR> convergenceChecker = new DummyConvergenceChecker();
        optimizer.setConvergenceChecker(convergenceChecker);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetConvergenceCheckerDefault() {
        BaseOptimizer<PAIR> optimizer = new DummyBaseOptimizer<>();
        ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
        assertNull(convergenceChecker);
    }

    private static class DummyBaseOptimizer<PAIR> implements BaseOptimizer<PAIR> {
        private int evaluations = 0;

        @Override
        public int getMaxEvaluations() {
            return Integer.MAX_VALUE;
        }

        @Override
        public int getEvaluations() {
            return evaluations;
        }

        @Override
        public ConvergenceChecker<PAIR> getConvergenceChecker() {
            return null;
        }

        @Override
        public void optimize(ObjectiveFunction<PAIR> objectiveFunction, StartingPoint startingPoint) {
            evaluations++;
        }

        @Override
        public void setConvergenceChecker(ConvergenceChecker<PAIR> convergenceChecker) {
            // empty implementation
        }
    }

    private static class DummyConvergenceChecker implements ConvergenceChecker<PAIR> {
        private Status status = Status.Diverging;

        @Override
        public Status checkConvergence(PAIR currentValue) {
            status = Status.CONVERGED;
            return status;
        }

        @Override
        public boolean isConverged() {
            return status == Status.CONVERGED;
        }
    }

    private static class DummyObjectiveFunction implements ObjectiveFunction<PAIR> {
        @Override
        public PAIR value(Point point) {
            return new PointValuePair(point, 0.0);
        }
    }

    private static class DummyStartingPoint implements StartingPoint {
        @Override
        public Point getPoint() {
            return new Point(0.0, 0.0);
        }
    }

    private static class Point {
        private double x;
        private double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }
}