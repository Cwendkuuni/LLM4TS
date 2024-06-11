package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.SimpleConvergenceChecker;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the BaseOptimizer interface.
 */
public class BaseOptimizerTest {

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = new DummyBaseOptimizer(10);
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(10, maxEvaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()} with normal scenario
     */
    @Test
    public void testGetEvaluationsNormalScenario() {
        BaseOptimizer<Object> optimizer = new DummyBaseOptimizer(10);
        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations);

        optimizer.optimize(new DummyObjectiveFunction(), new DummyPoint(), new MaxEval(10));

        evaluations = optimizer.getEvaluations();
        assertEquals(10, evaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()} with convergence scenario
     */
    @Test
    public void testGetEvaluationsConvergenceScenario() {
        BaseOptimizer<Object> optimizer = new DummyBaseOptimizer(10);
        optimizer.setConvergenceChecker(new SimpleConvergenceChecker(1e-10));

        optimizer.optimize(new DummyObjectiveFunction(), new DummyPoint(), new MaxEval(1));
        int evaluations = optimizer.getEvaluations();
        assertEquals(1, evaluations);

        optimizer.optimize(new DummyObjectiveFunction(), new DummyPoint(), new MaxEval(1000));
        evaluations = optimizer.getEvaluations();
        assertEquals(1000, evaluations);

        // Check convergence
        assertTrue(optimizer.isConverged());
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Object> optimizer = new DummyBaseOptimizer(10);
        ConvergenceChecker<Object> convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull(convergenceChecker);
    }

    /**
     * Test edge case for {@link BaseOptimizer#getMaxEvaluations()} with minimum value
     */
    @Test(expected = IllegalStateException.class)
    public void testGetMaxEvaluationsMinimumValue() {
        BaseOptimizer<Object> optimizer = new DummyBaseOptimizer(0);
        optimizer.getMaxEvaluations(); // No exception thrown in the constructor
    }

    /**
     * Test edge case for {@link BaseOptimizer#optimize()} with null objective function
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeNullObjectiveFunction() {
        BaseOptimizer<Object> optimizer = new DummyBaseOptimizer(10);
        optimizer.optimize(null, new DummyPoint(), new MaxEval(1));
    }

    /**
     * Test edge case for {@link BaseOptimizer#optimize()} with null point
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeNullPoint() {
        BaseOptimizer<Object> optimizer = new DummyBaseOptimizer(10);
        optimizer.optimize(new DummyObjectiveFunction(), null, new MaxEval(1));
    }

    /**
     * Test edge case for {@link BaseOptimizer#optimize()} with null evaluations limit
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeNullEvaluationsLimit() {
        BaseOptimizer<Object> optimizer = new DummyBaseOptimizer(10);
        optimizer.optimize(new DummyObjectiveFunction(), new DummyPoint(), null);
    }

    /**
     * Dummy BaseOptimizer implementation used for testing.
     */
    static class DummyBaseOptimizer implements BaseOptimizer<Object> {
        private int maxEvaluations;

        public DummyBaseOptimizer(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        @Override
        public int getMaxEvaluations() {
            return maxEvaluations;
        }

        @Override
        public int getEvaluations() {
            return 0;
        }

        @Override
        public ConvergenceChecker<Object> getConvergenceChecker() {
            return null;
        }

        @Override
        public Object optimize(ObjectiveFunction<?> function, Point startPoint, MaxEval evaluations) {
            return null;
        }

        @Override
        public boolean isConverged() {
            return false;
        }

        @Override
        public void setConvergenceChecker(ConvergenceChecker<? super Object> checker) {
        }
    }

    /**
     * Dummy ObjectiveFunction implementation used for testing.
     */
    static class DummyObjectiveFunction implements ObjectiveFunction<Object> {
        @Override
        public Object value(Object point) {
            return 0.0;
        }

        @Override
        public Point gradient(Object point) {
            return new DummyPoint(1.0);
        }

        @Override
        public Point vectorGradient(Object point) {
            return new DummyPoint(1.0, 1.0);
        }

        @Override
        public void checkGradient(Object point, Point gradient) {
        }

        @Override
        public void checkGradient(Object point, Point gradient, double tolerance) {
        }

        @Override
        public void checkHessian(Object point, HessianMatrix hessian) {
        }

        @Override
        public void checkHessian(Object point, HessianMatrix hessian, double tolerance) {
        }

        @Override
        public void checkScaling(ScalarFunction scalingFunction, double tolerance) {
        }

        @Override
        public String toString() {
            return "DummyObjectiveFunction";
        }
    }

    /**
     * Dummy Point implementation used for testing.
     */
    static class DummyPoint implements Point {
        private double[] coordinates;

        public DummyPoint(double... coordinates) {
            this.coordinates = coordinates;
        }

        @Override
        public double getDimension() {
            return coordinates.length;
        }

        @Override
        public double[] getCoordinates() {
            return coordinates;
        }

        @Override
        public double getX(int dimension) {
            return coordinates[dimension];
        }

        @Override
        public void setX(int dimension, double value) {
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < coordinates.length; i++) {
                sb.append(String.format("%.3f, ", coordinates[i]));
            }
            sb.setLength(sb.length() - 2); // Remove the last comma
            sb.append("]");
            return sb.toString();
        }
    }
}