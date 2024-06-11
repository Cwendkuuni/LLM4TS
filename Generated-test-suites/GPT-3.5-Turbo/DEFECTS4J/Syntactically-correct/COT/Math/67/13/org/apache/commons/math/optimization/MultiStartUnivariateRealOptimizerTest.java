import org.apache.commons.math.optimization.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.UnivariateRealOptimizer;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.random.RandomDataGenerator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    private static final double EPSILON = 1e-10;

    private MultiStartUnivariateRealOptimizer optimizer;
    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        UnivariateRealOptimizer singleOptimizer = new SingleOptimizer();
        RandomGenerator randomGenerator = new RandomDataGenerator();
        optimizer = new MultiStartUnivariateRealOptimizer(singleOptimizer, 5, randomGenerator);
        function = new TestFunction();
    }

    @Test
    public void testOptimize() {
        double result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 1);
        assertEquals(0, result, EPSILON);
    }

    @Test
    public void testOptimizeWithStartValue() {
        double result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 1, 0.5);
        assertEquals(0, result, EPSILON);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeWithFunctionEvaluationException() {
        UnivariateRealFunction faultyFunction = new FaultyFunction();
        optimizer.optimize(faultyFunction, GoalType.MINIMIZE, 0, 1);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithConvergenceException() {
        UnivariateRealFunction divergentFunction = new DivergentFunction();
        optimizer.optimize(divergentFunction, GoalType.MINIMIZE, 0, 1);
    }

    private class SingleOptimizer implements UnivariateRealOptimizer {
        public double getFunctionValue() {
            return 0;
        }

        public double getResult() {
            return 0;
        }

        public double getAbsoluteAccuracy() {
            return 0;
        }

        public int getIterationCount() {
            return 0;
        }

        public int getMaximalIterationCount() {
            return 0;
        }

        public int getMaxEvaluations() {
            return 0;
        }

        public int getEvaluations() {
            return 0;
        }

        public double getRelativeAccuracy() {
            return 0;
        }

        public void resetAbsoluteAccuracy() {
        }

        public void resetMaximalIterationCount() {
        }

        public void resetRelativeAccuracy() {
        }

        public void setAbsoluteAccuracy(double accuracy) {
        }

        public void setMaximalIterationCount(int count) {
        }

        public void setMaxEvaluations(int maxEvaluations) {
        }

        public void setRelativeAccuracy(double accuracy) {
        }

        public double optimize(UnivariateRealFunction f, GoalType goalType, double min, double max)
                throws ConvergenceException, FunctionEvaluationException {
            return 0;
        }

        public double optimize(UnivariateRealFunction f, GoalType goalType, double min, double max, double startValue)
                throws ConvergenceException, FunctionEvaluationException {
            return 0;
        }
    }

    private class TestFunction implements UnivariateRealFunction {
        public double value(double x) {
            return x;
        }
    }

    private class FaultyFunction implements UnivariateRealFunction {
        public double value(double x) {
            throw new RuntimeException();
        }
    }

    private class DivergentFunction implements UnivariateRealFunction {
        public double value(double x) {
            return 1 / x;
        }
    }

}