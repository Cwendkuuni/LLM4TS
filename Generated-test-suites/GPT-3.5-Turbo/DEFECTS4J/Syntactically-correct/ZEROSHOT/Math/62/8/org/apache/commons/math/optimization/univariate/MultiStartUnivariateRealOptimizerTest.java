import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.RandomGenerator;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer;

    @Before
    public void setUp() {
        optimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(new BaseUnivariateRealOptimizer<UnivariateRealFunction>() {
            
            public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
                
            }

            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
                return null;
            }

            public int getMaxEvaluations() {
                return 0;
            }

            public int getEvaluations() {
                return 0;
            }

            public void setMaxEvaluations(int maxEvaluations) {
                
            }

            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) {
                return null;
            }

            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max, double startValue) {
                return null;
            }
        }, 5, new RandomGenerator() {
            
            public void setSeed(int seed) {
                
            }

            public void setSeed(int[] seed) {
                
            }

            public void setSeed(long seed) {
                
            }

            public double nextDouble() {
                return 0;
            }

            public double nextDouble(double n) {
                return 0;
            }

            public void nextBytes(byte[] bytes) {
                
            }

            public float nextFloat() {
                return 0;
            }

            public boolean nextBoolean() {
                return false;
            }

            public void nextBytes(byte bytes[]) {
                
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

            public long nextLong(long n) {
                return 0;
            }
        });
    }

    @Test
    public void testSetConvergenceChecker() {
        optimizer.setConvergenceChecker(null);
        // No exception should be thrown
    }

    @Test
    public void testGetConvergenceChecker() {
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(0, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        optimizer.setMaxEvaluations(100);
        // No exception should be thrown
    }

    @Test
    public void testGetOptimaWhenNotComputed() {
        try {
            optimizer.getOptima();
            fail("Expected MathIllegalStateException");
        } catch (MathIllegalStateException e) {
            assertEquals(LocalizedFormats.NO_OPTIMUM_COMPUTED_YET, e.getPattern());
        }
    }

    @Test
    public void testOptimize() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        GoalType goal = GoalType.MINIMIZE;
        double min = 0;
        double max = 1;

        try {
            optimizer.optimize(f, goal, min, max, 0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            assertEquals(LocalizedFormats.NO_CONVERGENCE_WITH_ANY_START_POINT, e.getPattern());
            assertEquals(5, e.getArguments()[0]);
        }
    }

    @Test
    public void testSortPairs() {
        UnivariateRealPointValuePair[] pairs = new UnivariateRealPointValuePair[5];
        pairs[0] = new UnivariateRealPointValuePair(1.0, 1.0);
        pairs[1] = new UnivariateRealPointValuePair(2.0, 2.0);
        pairs[2] = null;
        pairs[3] = new UnivariateRealPointValuePair(0.0, 0.0);
        pairs[4] = null;

        optimizer.sortPairs(GoalType.MINIMIZE);
        assertEquals(4, pairs[0].getValue(), 0.0);
        assertEquals(2, pairs[1].getValue(), 0.0);
        assertNull(pairs[2]);
        assertEquals(1, pairs[3].getValue(), 0.0);
        assertNull(pairs[4]);
    }

}