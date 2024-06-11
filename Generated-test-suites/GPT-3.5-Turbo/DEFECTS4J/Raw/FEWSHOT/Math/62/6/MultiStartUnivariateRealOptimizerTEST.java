import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.apache.commons.math.optimization.univariate.*;

public class MultiStartUnivariateRealOptimizerTest {
    @Test
    public void testSetConvergenceChecker() {
        DummyUnivariateRealOptimizer optimizer = new DummyUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<DummyFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, null);
        multiStartOptimizer.setConvergenceChecker(null);
        assertEquals(null, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetConvergenceChecker() {
        DummyUnivariateRealOptimizer optimizer = new DummyUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<DummyFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, null);
        multiStartOptimizer.setConvergenceChecker(null);
        assertEquals(null, multiStartOptimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        DummyUnivariateRealOptimizer optimizer = new DummyUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<DummyFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, null);
        optimizer.setMaxEvaluations(10);
        assertEquals(10, multiStartOptimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        DummyUnivariateRealOptimizer optimizer = new DummyUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<DummyFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, null);
        optimizer.setEvaluations(10);
        assertEquals(10, multiStartOptimizer.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        DummyUnivariateRealOptimizer optimizer = new DummyUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<DummyFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, null);
        multiStartOptimizer.setMaxEvaluations(10);
        assertEquals(10, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetOptima() {
        DummyUnivariateRealOptimizer optimizer = new DummyUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<DummyFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, null);
        DummyFunction function = new DummyFunction();
        multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0, 10);
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertEquals(5, optima.length);
    }

    @Test
    public void testOptimizeWithoutStartValue() {
        DummyUnivariateRealOptimizer optimizer = new DummyUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<DummyFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, null);
        DummyFunction function = new DummyFunction();
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0, 10);
        assertEquals(5.0, result.getValue(), 0.0001);
    }

    @Test
    public void testOptimizeWithStartValue() {
        DummyUnivariateRealOptimizer optimizer = new DummyUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<DummyFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, null);
        DummyFunction function = new DummyFunction();
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0, 10, 2.5);
        assertEquals(3.0, result.getValue(), 0.0001);
    }

    private class DummyUnivariateRealOptimizer implements BaseUnivariateRealOptimizer<DummyFunction> {
        private ConvergenceChecker<UnivariateRealPointValuePair> checker;
        private int maxEvaluations;
        private int evaluations;

        @Override
        public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
            this.checker = checker;
        }

        @Override
        public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
            return checker;
        }

        @Override
        public int getMaxEvaluations() {
            return maxEvaluations;
        }

        @Override
        public int getEvaluations() {
            return evaluations;
        }

        @Override
        public void setMaxEvaluations(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        @Override
        public UnivariateRealPointValuePair optimize(DummyFunction f, GoalType goal, double min, double max)
            throws FunctionEvaluationException {
            evaluations++;
            return new UnivariateRealPointValuePair(5.0, 5.0);
        }
    }

    private class DummyFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return x;
        }
    }
}