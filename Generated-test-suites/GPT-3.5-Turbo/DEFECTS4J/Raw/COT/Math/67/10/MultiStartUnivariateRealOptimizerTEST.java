import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.UnivariateRealOptimizer;
import org.apache.commons.math.random.GaussianRandomGenerator;
import org.apache.commons.math.random.JDKRandomGenerator;
import org.apache.commons.math.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;

    @Before
    public void setUp() {
        UnivariateRealOptimizer singleOptimizer = getSingleStartOptimizer();
        RandomGenerator generator = getGenerator();
        optimizer = new MultiStartUnivariateRealOptimizer(singleOptimizer, 3, generator);
    }

    @Test
    public void testGetResult() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x - 2, 2);
            }
        };
        double min = -10;
        double max = 10;
        optimizer.optimize(function, UnivariateRealOptimizer.GoalType.MINIMIZE, min, max);
        double result = optimizer.getResult();
        assertEquals(2.0, result, 0.00001);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x + 1;
            }
        };
        double min = -10;
        double max = 10;
        optimizer.optimize(function, UnivariateRealOptimizer.GoalType.MINIMIZE, min, max);
    }

    @Test
    public void testGetOptima() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x - 2, 2);
            }
        };
        double min = -10;
        double max = 10;
        optimizer.optimize(function, UnivariateRealOptimizer.GoalType.MINIMIZE, min, max);
        double[] optima = optimizer.getOptima();
        double[] expectedOptima = {2.0, Double.NaN, Double.NaN};
        assertArrayEquals(expectedOptima, optima, 0.00001);
    }

    @Test
    public void testGetOptimaValues() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x - 2, 2);
            }
        };
        double min = -10;
        double max = 10;
        optimizer.optimize(function, UnivariateRealOptimizer.GoalType.MINIMIZE, min, max);
        double[] optimaValues = optimizer.getOptimaValues();
        double[] expectedOptimaValues = {0.0, Double.NaN, Double.NaN};
        assertArrayEquals(expectedOptimaValues, optimaValues, 0.00001);
    }

    private void assertArrayEquals(double[] expected, double[] actual, double delta) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i], delta);
        }
    }

    private UnivariateRealOptimizer getSingleStartOptimizer() {
        return new UnivariateRealOptimizer() {
            public double optimize(UnivariateRealFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException, ConvergenceException {
                return 2.0; // Always return 2 as the result
            }

            public double optimize(UnivariateRealFunction f, GoalType goal, double min, double max, double startValue) throws FunctionEvaluationException, ConvergenceException {
                return 2.0; // Always return 2 as the result
            }

            public int getEvaluations() {
                return 0;
            }

            public int getIterationCount() {
                return 0;
            }

            public double getFunctionValue() {
                return 0;
            }

            public double getResult() {
                return 0;
            }

            public double getAbsoluteAccuracy() {
                return 0;
            }

            public double getRelativeAccuracy() {
                return 0;
            }

            public void resetAbsoluteAccuracy() {
            }

            public void resetRelativeAccuracy() {
            }

            public int getMaximalIterationCount() {
                return 0;
            }

            public int getMaxEvaluations() {
                return 0;
            }

            public void setMaximalIterationCount(int maxIter) {
            }

            public void setMaxEvaluations(int maxEvaluations) {
            }

            public void setAbsoluteAccuracy(double accuracy) {
            }

            public void setRelativeAccuracy(double accuracy) {
            }

            public RandomGenerator getRandomGenerator() {
                return null;
            }

            public void setRandomGenerator(RandomGenerator randomGenerator) {
            }
        };
    }

    private RandomGenerator getGenerator() {
        RandomGenerator generator = new JDKRandomGenerator();
        GaussianRandomGenerator gaussianGenerator = new GaussianRandomGenerator(generator);
        generator.setSeed(1234567890);
        return gaussianGenerator;
    }
}