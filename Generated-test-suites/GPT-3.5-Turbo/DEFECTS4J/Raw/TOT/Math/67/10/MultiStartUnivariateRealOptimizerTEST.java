import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.JDKRandomGenerator;
import org.apache.commons.math.util.MathUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;

    @Before
    public void setUp() {
        UnivariateRealOptimizer underlyingOptimizer = new NewtonSolver();
        int starts = 5;
        JDKRandomGenerator generator = new JDKRandomGenerator();
        this.optimizer = new MultiStartUnivariateRealOptimizer(underlyingOptimizer, starts, generator);
    }

    @Test
    public void testOptimize_withMinimizeGoalType() {
        try {
            UnivariateRealFunction f = new QuadraticFunction();

            double min = -10.0;
            double max = 10.0;
            double expectedOptimum = 0.0;

            double optimum = optimizer.optimize(f, GoalType.MINIMIZE, min, max);
            Assert.assertEquals(expectedOptimum, optimum, MathUtils.EPSILON);

            double[] optima = optimizer.getOptima();
            Assert.assertEquals(5, optima.length);
            for (double opt : optima) {
                Assert.assertEquals(expectedOptimum, opt, MathUtils.EPSILON);
            }

            double[] optimaValues = optimizer.getOptimaValues();
            Assert.assertEquals(5, optimaValues.length);

            int numConverged = 0;
            for (double value : optimaValues) {
                Assert.assertEquals(f.value(expectedOptimum), value, MathUtils.EPSILON);
                if (!Double.isNaN(value)) {
                    numConverged++;
                }
            }

            Assert.assertEquals(5, numConverged);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testOptimize_withMaximizeGoalType() {
        try {
            UnivariateRealFunction f = new QuadraticFunction();

            double min = -10.0;
            double max = 10.0;
            double expectedOptimum = 0.0;

            double optimum = optimizer.optimize(f, GoalType.MAXIMIZE, min, max);
            Assert.assertEquals(expectedOptimum, optimum, MathUtils.EPSILON);

            double[] optima = optimizer.getOptima();
            Assert.assertEquals(5, optima.length);
            for (double opt : optima) {
                Assert.assertEquals(expectedOptimum, opt, MathUtils.EPSILON);
            }

            double[] optimaValues = optimizer.getOptimaValues();
            Assert.assertEquals(5, optimaValues.length);

            int numConverged = 0;
            for (double value : optimaValues) {
                Assert.assertEquals(f.value(expectedOptimum), value, MathUtils.EPSILON);
                if (!Double.isNaN(value)) {
                    numConverged++;
                }
            }

            Assert.assertEquals(5, numConverged);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test(expected = OptimizationException.class)
    public void testOptimize_withConvergenceException() throws OptimizationException {
        UnivariateRealFunction f = new NonConvergingFunction();

        double min = -10.0;
        double max = 10.0;

        optimizer.optimize(f, GoalType.MINIMIZE, min, max);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimize_withFunctionEvaluationException() throws FunctionEvaluationException {
        UnivariateRealFunction f = new ErrorFunction();

        double min = -10.0;
        double max = 10.0;

        optimizer.optimize(f, GoalType.MINIMIZE, min, max);
    }

    private static class QuadraticFunction implements UnivariateRealFunction {
        public double value(double x) {
            return x * x;
        }
    }

    private static class NonConvergingFunction implements UnivariateRealFunction {
        public double value(double x) {
            return x + 1;
        }
    }

    private static class ErrorFunction implements UnivariateRealFunction {
        public double value(double x) throws FunctionEvaluationException {
            throw new FunctionEvaluationException(x);
        }
    }

}