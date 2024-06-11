import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.optimization.univariate.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Comparator;

public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateRealOptimizer optimizer;

    @Before
    public void setup() {
        optimizer = new MultiStartUnivariateRealOptimizer(new UnivariateRealOptimizerImpl(), 3, new RandomGeneratorImpl());
    }

    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checker = new ConvergenceCheckerImpl();
        optimizer.setConvergenceChecker(checker);

        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testMaxEvaluations() {
        optimizer.setMaxEvaluations(100);

        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() throws FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunctionImpl();
        GoalType goal = GoalType.MINIMIZE;
        double min = -1.0;
        double max = 1.0;
        
        optimizer.optimize(function, goal, min, max);
        
        assertEquals(100, optimizer.getEvaluations());
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeNoConvergence() throws FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunctionImpl();
        GoalType goal = GoalType.MINIMIZE;
        double min = -1.0;
        double max = 1.0;
        double startValue = 0.0;
        
        optimizer.setMaxEvaluations(10);
        optimizer.optimize(function, goal, min, max, startValue);
    }

    @Test
    public void testGetOptima() throws FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunctionImpl();
        GoalType goal = GoalType.MINIMIZE;
        double min = -1.0;
        double max = 1.0;
        
        optimizer.optimize(function, goal, min, max);
        
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        
        assertEquals(3, optima.length);
    }

    @Test
    public void testOptimize() throws FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunctionImpl();
        GoalType goal = GoalType.MINIMIZE;
        double min = -1.0;
        double max = 1.0;
        
        UnivariateRealPointValuePair optima = optimizer.optimize(function, goal, min, max);
        
        assertNotNull(optima);
    }

    // Custom implementation of the required interfaces for testing
    private class UnivariateRealFunctionImpl implements UnivariateRealFunction {
        public double value(double x) {
            return x * x;
        }
    }

    private class UnivariateRealOptimizerImpl implements BaseUnivariateRealOptimizer<UnivariateRealFunction> {
        public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
            // No implementation needed for testing
        }
        
        public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
            // No implementation needed for testing
            return null;
        }

        public int getMaxEvaluations() {
            return 0;
        }

        public int getEvaluations() {
            return 0;
        }

        public void setMaxEvaluations(int maxEvaluations) {
            // No implementation needed for testing
        }
        
        public UnivariateRealPointValuePair optimize(
            UnivariateRealFunction f, GoalType goalType, double min, double max)
            throws FunctionEvaluationException, ConvergenceException {
            double value = f.value((min + max) / 2.0);
            return new UnivariateRealPointValuePair((min + max) / 2.0, value);
        }
    }

    private class RandomGeneratorImpl implements RandomGenerator {
        public void setSeed(long seed) {
            // No implementation needed for testing
        }

        public void setSeed(int[] seed) {
            // No implementation needed for testing
        }

        public void setSeed(int seed) {
            // No implementation needed for testing
        }

        public void nextBytes(byte[] bytes) {
            // No implementation needed for testing
        }

        public int nextInt() {
            return 0;
        }

        public int nextInt(int n) {
            return 0;
        }

        public long nextLong() {
            return 0;
        }

        public boolean nextBoolean() {
            return false;
        }

        public float nextFloat() {
            return 0;
        }

        public double nextDouble() {
            return 0;
        }

        public double nextGaussian() {
            return 0;
        }
    }

    private class ConvergenceCheckerImpl implements ConvergenceChecker<UnivariateRealPointValuePair> {
        public boolean converged(
            int iteration, UnivariateRealPointValuePair previous, UnivariateRealPointValuePair current) {
            return false;
        }
    }
}