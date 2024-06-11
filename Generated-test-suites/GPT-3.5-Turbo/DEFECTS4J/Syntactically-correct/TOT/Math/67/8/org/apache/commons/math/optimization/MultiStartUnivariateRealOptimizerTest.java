import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;
    private MockOptimizer mockOptimizer;
    private MockRandomGenerator mockGenerator;

    @Before
    public void setup() {
        mockOptimizer = new MockOptimizer();
        mockGenerator = new MockRandomGenerator();
        optimizer = new MultiStartUnivariateRealOptimizer(mockOptimizer, 3, mockGenerator);
    }

    @Test
    public void testOptimize_TypicalCase() throws ConvergenceException, FunctionEvaluationException {
        // typical case where the optimizer successfully finds the optimum
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
        assertEquals(0.0, result, 0.0001);
        assertEquals(1, mockOptimizer.getOptimizationCount());
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimize_ConvergenceException() throws ConvergenceException, FunctionEvaluationException {
        // edge case where the optimizer fails to converge
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 3);
            }
        };
        optimizer.optimize(f, GoalType.MAXIMIZE, -10, 10);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimize_FunctionEvaluationException() throws ConvergenceException, FunctionEvaluationException {
        // error scenario where a function evaluation exception is thrown
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };
        optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
    }

    private class MockOptimizer implements UnivariateRealOptimizer {

        private int optimizationCount;

        public double optimize(UnivariateRealFunction f, GoalType goalType, double min, double max)
                throws ConvergenceException, FunctionEvaluationException {
            optimizationCount++;
            return 0.0;
        }

        public double optimize(UnivariateRealFunction f, GoalType goalType, double min, double max, double startValue)
                throws ConvergenceException, FunctionEvaluationException {
            return optimize(f, goalType, min, max);
        }

        public double getFunctionValue() {
            return 0.0;
        }

        public double getResult() {
            return 0.0;
        }

        public double getAbsoluteAccuracy() {
            return 0.0;
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
            return 0.0;
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

        public int getOptimizationCount() {
            return optimizationCount;
        }
    }

    private class MockRandomGenerator implements RandomGenerator {

        public double nextDouble() {
            return 0.5;
        }

        public void setSeed(int seed) {
        }

        public void setSeed(int[] seed) {
        }

        public void setSeed(long seed) {
        }

        public void nextBytes(byte[] bytes) {
        }
    }
}