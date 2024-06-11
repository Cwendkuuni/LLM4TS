import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.BaseUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateRealPointValuePair;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testConstructor() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FooUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> msOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwisterRandomGenerator());
        Assert.assertEquals(5, msOptimizer.getStarts());
    }

    @Test
    public void testSetConvergenceChecker() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FooUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> msOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwisterRandomGenerator());
        ConvergenceChecker<UnivariateRealPointValuePair> checker = new ConvergenceChecker<UnivariateRealPointValuePair>() {
            @Override
            public boolean convergedIn(UnivariateRealPointValuePair best, int iteration) {
                return true;
            }
        };
        msOptimizer.setConvergenceChecker(checker);
        Assert.assertSame(checker, msOptimizer.getConvergenceChecker());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FooUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> msOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwisterRandomGenerator());
        ConvergenceChecker<UnivariateRealPointValuePair> checker = new ConvergenceChecker<UnivariateRealPointValuePair>() {
            @Override
            public boolean convergedIn(UnivariateRealPointValuePair best, int iteration) {
                return true;
            }
        };
        msOptimizer.setConvergenceChecker(checker);
        Assert.assertSame(checker, msOptimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FooUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> msOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwisterRandomGenerator());
        msOptimizer.setMaxEvaluations(10);
        Assert.assertEquals(10, msOptimizer.getMaxEvaluations());
        Assert.assertEquals(10, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FooUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> msOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwisterRandomGenerator());
        msOptimizer.optimize(new TestFunction(), GoalType.MINIMIZE, 0.0, 1.0);
        Assert.assertNotEquals(0, msOptimizer.getEvaluations());
        Assert.assertEquals(msOptimizer.getEvaluations(), optimizer.getEvaluations());
    }

    @Test
    public void testGetOptima() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FooUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> msOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwisterRandomGenerator());
        msOptimizer.optimize(new TestFunction(), GoalType.MINIMIZE, 0.0, 1.0);
        UnivariateRealPointValuePair[] optima = msOptimizer.getOptima();
        Assert.assertEquals(5, optima.length);
        for (UnivariateRealPointValuePair pair : optima) {
            assertNull(pair);
        }
    }

    @Test
    public void testOptimizeMin() throws FunctionEvaluationException, ConvergenceException {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FooUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> msOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwisterRandomGenerator());
        msOptimizer.optimize(new TestFunction(), GoalType.MINIMIZE, 0.0, 1.0);
        UnivariateRealPointValuePair best = msOptimizer.getOptima()[0];
        assertEquals(0.123, best.getX(), 0.001);
        assertEquals(0.546, best.getValue(), 0.001);
    }

    @Test
    public void testOptimizeMax() throws FunctionEvaluationException, ConvergenceException {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FooUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> msOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwisterRandomGenerator());
        msOptimizer.optimize(new TestFunction(), GoalType.MAXIMIZE, 0.0, 1.0);
        UnivariateRealPointValuePair best = msOptimizer.getOptima()[0];
        assertEquals(0.879, best.getX(), 0.001);
        assertEquals(0.456, best.getValue(), 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testNoConvergence() throws FunctionEvaluationException, ConvergenceException {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FooUnivariateRealOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> msOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwisterRandomGenerator());
        msOptimizer.optimize(new TestFunctionNoConvergence(), GoalType.MINIMIZE, 0.0, 1.0);
    }

    private static class FooUnivariateRealOptimizer extends BaseUnivariateRealOptimizer<UnivariateRealFunction> {
        @Override
        public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double start, double end)
                throws FunctionEvaluationException, ConvergenceException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setMaxEvaluations(int maxEvaluations) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getMaxEvaluations() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
            throw new UnsupportedOperationException();
        }
    }

    private static class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.sin(x) * Math.cos(x);
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }

        @Override
        public double[] getDerivatives(double[] x) {
            double[] result = new double[1];
            result[0] = Math.cos(x)[0] * (1 - Math.sin(x)[0]) - Math.sin(x)[0] * Math.cos(x)[0];
            return result;
        }

        @Override
        public SimpleBounds getRange() {
            return new SimpleBounds(-3.0, 3.0);
        }
    }

    private static class TestFunctionNoConvergence implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            throw new RuntimeException();
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }

        @Override
        public double[] getDerivatives(double[] x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public SimpleBounds getRange() {
            return new SimpleBounds(-3.0, 3.0);
        }
    }

    private static class MersenneTwisterRandomGenerator implements RandomGenerator {
        @Override
        public double nextGaussian() {
            throw new UnsupportedOperationException();
        }

        @Override
        public double nextUniform() {
            throw new UnsupportedOperationException();
        }

        @Override
        public double nextUniform(double min, double max) {
            double[] r = new double[2];
            nextBytes(r);
            return min + (max - min) * (r[0] / (double) 0xffffffffL);
        }

        @Override
        public void nextBytes(byte[] bytes) {
            // Implementation of Mersenne Twister algorithm
        }

        @Override
        public int nextInt() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int nextInt(int n) {
            int bits, mask = 1 << 32;
            do {
                bits = (int) nextUniform();
                mask >>= 1;
            } while ((bits & mask) == 0);
            return bits & (mask - 1);
        }
    }

}