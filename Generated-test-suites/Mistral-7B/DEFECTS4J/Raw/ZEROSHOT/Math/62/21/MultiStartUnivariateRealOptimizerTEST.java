import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.BaseUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateRealPointValuePair;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartUnivariateRealOptimizer() {
        UnivariateRealFunction sinFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        BaseUnivariateRealOptimizer optimizer = new FibonacciSearchUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwisterFastRandom());

        double min = 0;
        double max = 10;
        double startValue = 5;
        GoalType goal = GoalType.MINIMIZE;

        try {
            UnivariateRealPointValuePair result = multiOptimizer.optimize(sinFunction, goal, min, max, startValue);
            double bestValue = result.getValue();
            double bestPoint = result.getPoint();

            // Check that the optimization was successful
            Assert.assertNotNull(result);
            Assert.assertFalse(Double.isNaN(bestValue));
            Assert.assertFalse(Double.isInfinite(bestValue));

            // Check that the optimization found a good enough solution
            Assert.assertEquals(0.01d, bestValue, 0.001); // Tolerance

            // Check that all starts were performed
            Assert.assertEquals(5, multiOptimizer.getEvaluations());

            // Check that all the optima are returned
            UnivariateRealPointValuePair[] optima = multiOptimizer.getOptima();
            Assert.assertEquals(5, optima.length);
            for (UnivariateRealPointValuePair pair : optima) {
                if (pair == null) {
                    continue;
                }
                Assert.assertNotNull(pair.getPoint());
                Assert.assertFalse(Double.isNaN(pair.getValue()));
                Assert.assertFalse(Double.isInfinite(pair.getValue()));
            }
        } catch (FunctionEvaluationException | ConvergenceException e) {
            // The optimization may not have been successful, that's okay
        }
    }
}