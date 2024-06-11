import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.FunctionEvaluationException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

public class MultiStartUnivariateRealOptimizerTest {

    // Define a dummy objective function for testing
    private static class DummyObjectiveFunction implements UnivariateRealFunction {
        public double value(double x) {
            return x * x;
        }
    }

    // Define a dummy optimizer for testing
    private static class DummyOptimizer implements BaseUnivariateRealOptimizer<DummyObjectiveFunction> {
        private int evaluations = 0;
        private int maxEvaluations = Integer.MAX_VALUE;
        private ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker;

        public int getMaxEvaluations() {
            return maxEvaluations;
        }

        public int getEvaluations() {
            return evaluations;
        }

        public void setMaxEvaluations(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
            this.convergenceChecker = checker;
        }

        public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
            return convergenceChecker;
        }

        public UnivariateRealPointValuePair optimize(DummyObjectiveFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException {
            evaluations++;
            // Dummy optimization algorithm
            double x = (min + max) / 2;
            double y = f.value(x);
            return new UnivariateRealPointValuePair(x, y);
        }
    }

    @Test
    public void testOptimize() throws FunctionEvaluationException {
        // Create the optimizer and the objective function
        BaseUnivariateRealOptimizer<DummyObjectiveFunction> optimizer = new DummyOptimizer();
        MultiStartUnivariateRealOptimizer<DummyObjectiveFunction> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, null);
        DummyObjectiveFunction f = new DummyObjectiveFunction();

        // Optimize the objective function
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(f, GoalType.MINIMIZE, -10, 10);

        // Verify the result
        Assert.assertEquals(0.0, result.getPoint(), 1e-6);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeNoConvergence() throws FunctionEvaluationException {
        // Create the optimizer and the objective function
        BaseUnivariateRealOptimizer<DummyObjectiveFunction> optimizer = new DummyOptimizer();
        MultiStartUnivariateRealOptimizer<DummyObjectiveFunction> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, null);
        DummyObjectiveFunction f = new DummyObjectiveFunction();

        // Set maxEvaluations to 1 to force convergence failure
        optimizer.setMaxEvaluations(1);

        // Optimize the objective function (should throw ConvergenceException)
        multiStartOptimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
    }

    @Test
    public void testGetOptima() throws FunctionEvaluationException {
        // Create the optimizer and the objective function
        BaseUnivariateRealOptimizer<DummyObjectiveFunction> optimizer = new DummyOptimizer();
        MultiStartUnivariateRealOptimizer<DummyObjectiveFunction> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, null);
        DummyObjectiveFunction f = new DummyObjectiveFunction();

        // Optimize the objective function
        multiStartOptimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();

        // Verify the optima
        Assert.assertEquals(5, optima.length);
        Assert.assertNotNull(optima[0]);
        Assert.assertNull(optima[1]);
        Assert.assertNull(optima[2]);
        Assert.assertNull(optima[3]);
        Assert.assertNull(optima[4]);
    }

}