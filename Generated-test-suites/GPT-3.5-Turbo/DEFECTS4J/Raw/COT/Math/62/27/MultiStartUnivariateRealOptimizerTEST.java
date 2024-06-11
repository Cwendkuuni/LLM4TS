import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.util.FastMath;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {


    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer;

    @Before
    public void setUp() {
        // Create a new optimizer with a single-start optimizer and 3 starts
        optimizer = new MultiStartUnivariateRealOptimizer<>(new SingleStartUnivariateRealOptimizer<>(), 3, new RandomGenerator() {
            // Dummy random generator implementation
            @Override
            public void setSeed(long seed) {}

            @Override
            public void setSeed(int[] seed) {}

            @Override
            public void setSeed(int seed) {}

            @Override
            public void nextBytes(byte[] bytes) {}

            @Override
            public int nextInt() {
                return 0;
            }

            @Override
            public int nextInt(int n) {
                return 0;
            }

            @Override
            public long nextLong() {
                return 0;
            }

            @Override
            public boolean nextBoolean() {
                return false;
            }

            @Override
            public float nextFloat() {
                return 0;
            }

            @Override
            public double nextDouble() {
                return 0;
            }

            @Override
            public double nextGaussian() {
                return 0;
            }
        });
    }

    @Test
    public void testSetConvergenceChecker() {
        // Test setConvergenceChecker method
        ConvergenceChecker<UnivariateRealPointValuePair> checker = (p1, p2) -> true;
        optimizer.setConvergenceChecker(checker);

        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        // Test getMaxEvaluations method
        assertEquals(0, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        // Test getEvaluations method
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        // Test setMaxEvaluations method
        int maxEvaluations = 100;
        optimizer.setMaxEvaluations(maxEvaluations);

        assertEquals(maxEvaluations, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetOptima() {
        // Test getOptima method
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();

        assertTrue(optima == null);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaThrowsMathIllegalStateException() {
        // Test getOptima method when called before optimize
        optimizer.getOptima();
    }

    @Test
    public void testOptimize() throws FunctionEvaluationException {
        // Test optimize method
        double minValue = 0;
        double maxValue = 1;
        UnivariateRealPointValuePair result = optimizer.optimize(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x; // Example objective function
            }
        }, GoalType.MINIMIZE, minValue, maxValue);

        assertEquals(0.0, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeThrowsConvergenceException() throws FunctionEvaluationException {
        // Test optimize method when convergence is not reached
        optimizer.optimize(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x; // Example objective function
            }
        }, GoalType.MINIMIZE, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testOptimizeThrowsMathIllegalStateException() throws FunctionEvaluationException {
        // Test optimize method when called before setting maxEvaluations
        optimizer.optimize(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x; // Example objective function
            }
        }, GoalType.MINIMIZE, 0, 1);
    }

    @Test
    public void testGetOptimaAfterOptimize() throws FunctionEvaluationException {
        // Test getOptima method after optimize
        optimizer.optimize(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x; // Example objective function
            }
        }, GoalType.MINIMIZE, 0, 1);

        UnivariateRealPointValuePair[] optima = optimizer.getOptima();

        assertEquals(3, optima.length);
        assertTrue(optima[0] != null);
    }


}