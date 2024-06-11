package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.FunctionEvaluationException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
    private MockOptimizer mockOptimizer;
    private final int starts = 5;
    private final RandomGenerator generator = null;

    @Before
    public void setUp() throws Exception {
        mockOptimizer = new MockOptimizer();
        optimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(mockOptimizer, starts, generator);
    }

    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checker = new DummyConvergenceChecker<>();
        optimizer.setConvergenceChecker(checker);
        assertEquals(checker, mockOptimizer.getConvergenceChecker());
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checker = new DummyConvergenceChecker<>();
        mockOptimizer.setConvergenceChecker(checker);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = 100;
        mockOptimizer.setMaxEvaluations(maxEvaluations);
        assertEquals(maxEvaluations, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        int evaluations = 50;
        mockOptimizer.setEvaluations(evaluations);
        assertEquals(evaluations, optimizer.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 100;
        optimizer.setMaxEvaluations(maxEvaluations);
        assertEquals(maxEvaluations, mockOptimizer.getMaxEvaluations());
    }

    @Test
    public void testGetOptima() {
        UnivariateRealPointValuePair[] optima = new UnivariateRealPointValuePair[starts];
        mockOptimizer.setOptima(optima);
        assertEquals(optima, optimizer.getOptima());
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaNoOptimumComputedYet() {
        optimizer.getOptima();
    }

    @Test
    public void testOptimize() throws FunctionEvaluationException {
        UnivariateRealFunction f = new DummyFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = 0;
        double max = 1;
        UnivariateRealPointValuePair optimum = new UnivariateRealPointValuePair(0, 0);
        mockOptimizer.setOptimum(optimum);

        UnivariateRealPointValuePair result = optimizer.optimize(f, goal, min, max);
        assertEquals(optimum, result);
        assertEquals(1, mockOptimizer.getOptimizeCalls());
        assertEquals(f, mockOptimizer.getOptimizeFunction());
        assertEquals(goal, mockOptimizer.getOptimizeGoal());
        assertEquals(min, mockOptimizer.getOptimizeMin(), 0);
        assertEquals(max, mockOptimizer.getOptimizeMax(), 0);
    }

    @Test
    public void testOptimizeWithStartValue() throws FunctionEvaluationException {
        UnivariateRealFunction f = new DummyFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = 0;
        double max = 1;
        double startValue = 0.5;
        UnivariateRealPointValuePair optimum = new UnivariateRealPointValuePair(0, 0);
        mockOptimizer.setOptimum(optimum);

        UnivariateRealPointValuePair result = optimizer.optimize(f, goal, min, max, startValue);
        assertEquals(optimum, result);
        assertEquals(1, mockOptimizer.getOptimizeCalls());
        assertEquals(f, mockOptimizer.getOptimizeFunction());
        assertEquals(goal, mockOptimizer.getOptimizeGoal());
        assertEquals(min, mockOptimizer.getOptimizeMin(), 0);
        assertEquals(max, mockOptimizer.getOptimizeMax(), 0);
        assertEquals(startValue, mockOptimizer.getOptimizeStartValue(), 0);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeConvergenceException() throws FunctionEvaluationException {
        UnivariateRealFunction f = new DummyFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = 0;
        double max = 1;
        mockOptimizer.setConvergenceException(new ConvergenceException(LocalizedFormats.NO_CONVERGENCE_WITH_ANY_START_POINT, starts));

        optimizer.optimize(f, goal, min, max);
    }

    private class DummyConvergenceChecker<T> implements ConvergenceChecker<T> {
        @Override
        public boolean converged(int iteration, T previous, T current) {
            return false;
        }
    }

    private class MockOptimizer implements BaseUnivariateRealOptimizer<UnivariateRealFunction> {
        private ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker;
        private int maxEvaluations;
        private int evaluations;
        private UnivariateRealPointValuePair[] optima;
        private int optimizeCalls;
        private UnivariateRealFunction optimizeFunction;
        private GoalType optimizeGoal;
        private double optimizeMin;
        private double optimizeMax;
        private double optimizeStartValue;
        private FunctionEvaluationException convergenceException;

        @Override
        public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
            this.convergenceChecker = checker;
        }

        @Override
        public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
            return convergenceChecker;
        }

        @Override
        public int getMaxEvaluations() {
            return maxEvaluations;
        }

        @Override
        public void setMaxEvaluations(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        @Override
        public int getEvaluations() {
            return evaluations;
        }

        @Override
        public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goalType, double min, double max) throws FunctionEvaluationException {
            optimizeCalls++;
            optimizeFunction = f;
            optimizeGoal = goalType;
            optimizeMin = min;
            optimizeMax = max;
            if (convergenceException != null) {
                throw convergenceException;
            }
            evaluations++;
            return optima != null ? optima[0] : null;
        }

        public void setOptima(UnivariateRealPointValuePair[] optima) {
            this.optima = optima;
        }

        public void setOptimum(UnivariateRealPointValuePair optimum) {
            this.optima = new UnivariateRealPointValuePair[]{optimum};
        }

        public int getOptimizeCalls() {
            return optimizeCalls;
        }

        public UnivariateRealFunction getOptimizeFunction() {
            return optimizeFunction;
        }

        public GoalType getOptimizeGoal() {
            return optimizeGoal;
        }

        public double getOptimizeMin() {
            return optimizeMin;
        }

        public double getOptimizeMax() {
            return optimizeMax;
        }

        public double getOptimizeStartValue() {
            return optimizeStartValue;
        }

        public void setEvaluations(int evaluations) {
            this.evaluations = evaluations;
        }

        public void setConvergenceException(FunctionEvaluationException convergenceException) {
            this.convergenceException = convergenceException;
        }
    }

    private class DummyFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return 0;
        }
    }
}