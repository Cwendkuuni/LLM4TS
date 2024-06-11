import org.junit.Assert;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testOptimize() throws FunctionEvaluationException {
        // Create the optimizer and the objective function
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 4;
            }
        };
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(new UnivariateRealOptimizerImpl(), 3, new RandomGeneratorImpl());

        // Expected result
        UnivariateRealPointValuePair expected = new UnivariateRealPointValuePair(2, 0);

        // Optimize the function
        UnivariateRealPointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 10);

        // Check the result
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testOptimize_NoConvergence() throws FunctionEvaluationException {
        // Create the optimizer and the objective function
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(new UnivariateRealOptimizerImpl(), 3, new RandomGeneratorImpl());

        // Optimize the function
        try {
            optimizer.optimize(function, GoalType.MINIMIZE, 0, 10);
            Assert.fail("No convergence exception should be thrown");
        } catch (ConvergenceException e) {
            // Exception should be thrown
        }
    }

    @Test
    public void testGetOptima() throws FunctionEvaluationException {
        // Create the optimizer and the objective function
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.abs(x);
            }
        };
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(new UnivariateRealOptimizerImpl(), 3, new RandomGeneratorImpl());

        // Expected results
        UnivariateRealPointValuePair[] expected = new UnivariateRealPointValuePair[3];
        expected[0] = new UnivariateRealPointValuePair(0, 0);
        expected[1] = new UnivariateRealPointValuePair(-1, 1);
        expected[2] = new UnivariateRealPointValuePair(1, 1);

        // Optimize the function
        optimizer.optimize(function, GoalType.MINIMIZE, -10, 10);

        // Check the optima
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        Assert.assertArrayEquals(expected, optima);
    }

    private class UnivariateRealOptimizerImpl implements BaseUnivariateRealOptimizer<UnivariateRealFunction> {
        public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {}
        public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {return null;}
        public int getMaxEvaluations() {return 0;}
        public int getEvaluations() {return 0;}
        public void setMaxEvaluations(int maxEvaluations) {}
        public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException {
            return null;
        }
    }

    private class RandomGeneratorImpl implements RandomGenerator {
        public void setSeed(long seed) {}
        public double nextDouble() {return 0;}
        public void nextBytes(byte[] bytes) {}
        public void nextBytes(byte[] bytes, int start, int len) {}
        public int nextInt() {return 0;}
        public int nextInt(int n) {return 0;}
        public boolean nextBoolean() {return false;}
        public float nextFloat() {return 0;}
        public long nextLong() {return 0;}
        public double nextGaussian() {return 0;}
    }
}