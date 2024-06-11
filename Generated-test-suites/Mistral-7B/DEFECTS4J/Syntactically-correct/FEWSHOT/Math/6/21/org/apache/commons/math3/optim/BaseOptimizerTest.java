package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.goal.SimpleGoalType;
import org.junit.Test;

/**
 * Test cases for the {@link BaseOptimizer} interface.
 */
@Deprecated
public class BaseOptimizerTest {

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new DummyBaseOptimizer();
        assertEquals(MaxEval.UNLIMITED, optimizer.getMaxEvaluations());

        optimizer = new DummyBaseOptimizer(10);
        assertEquals(10, optimizer.getMaxEvaluations());
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new DummyBaseOptimizer();
        assertNull(optimizer.getEvaluations());

        optimizer.optimize(new DummyObjectiveFunction(), new SimpleBounds(0.0, 10.0), new SimpleGoalType(GoalType.MINIMIZE, 0.0));
        assertEquals(0, optimizer.getEvaluations().getValue());
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}.
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new DummyBaseOptimizer();
        assertNull(optimizer.getConvergenceChecker());

        optimizer = new DummyBaseOptimizer() {
            @Override
            public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
                return new DummyConvergenceChecker();
            }
        };
        assertNotNull(optimizer.getConvergenceChecker());
    }

    /**
     * Dummy implementation of the {@link BaseOptimizer} interface.
     */
    static class DummyBaseOptimizer implements BaseOptimizer<PointValuePair> {
        private int maxEvaluations;

        public DummyBaseOptimizer() {
            this(MaxEval.UNLIMITED);
        }

        public DummyBaseOptimizer(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        @Override
        public int getMaxEvaluations() {
            return maxEvaluations;
        }

        @Override
        public int getEvaluations() {
            return new IntPointValuePair(0);
        }

        @Override
        public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
            return null;
        }

        private static class IntPointValuePair implements PointValuePair {
            private int value;

            public IntPointValuePair(int value) {
                this.value = value;
            }

            @Override
            public double getPointValue() {
                throw new UnsupportedOperationException("This method is not applicable to the argument");
            }

            @Override
            public PointValuePair getValue() {
                return new PointValuePair() {
                    @Override
                    public double getPointValue() {
                        return value;
                    }
                };
            }
        }

        /**
         * Dummy objective function for testing purposes.
         */
        static class DummyObjectiveFunction implements ObjectiveFunction {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public PointValuePair gradient(double x) {
                return new PointValuePair() {
                    @Override
                    public double getPointValue() {
                        return value(x);
                    }

                    @Override
                    public PointValuePair getGradient() {
                        return new PointValuePair() {
                            @Override
                            public double getPointValue() {
                                return 2.0 * x;
                            }
                        };
                    }
                };
            }
        }

        /**
         * Dummy convergence checker for testing purposes.
         */
        static class DummyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
            @Override
            public boolean isConverged(PointValuePair currentValue, PointValuePair previousValue) {
                return currentValue.getPointValue() < 0.01;
            }
        }
    }
}