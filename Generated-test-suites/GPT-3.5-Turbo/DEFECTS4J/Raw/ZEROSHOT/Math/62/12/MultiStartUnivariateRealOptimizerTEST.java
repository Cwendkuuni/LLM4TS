import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.UnivariateRealPointValuePair;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.util.FastMath;

public class MultiStartUnivariateRealOptimizerTest {
    
    // Test variables
    private final double EPSILON = 1e-10;
    private final UnivariateRealFunction function = new UnivariateRealFunction() {
        public double value(double x) {
            return x * x;
        }
    };
    private final BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BaseUnivariateRealOptimizer<UnivariateRealFunction>() {
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
    };
    private final int starts = 5;
    private final RandomGenerator generator = new RandomGenerator() {
        public void setSeed(long seed) {

        }

        public void setSeed(int[] seed) {

        }

        public void setSeed(int seed) {

        }

        public double nextDouble() {
            return 0;
        }

        public void nextBytes(byte[] bytes) {

        }

        public int nextInt() {
            return 0;
        }

        public int nextInt(int n) {
            return 0;
        }

        public boolean nextBoolean() {
            return false;
        }

        public float nextFloat() {
            return 0;
        }

        public double nextGaussian() {
            return 0;
        }
    };
    private final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, starts, generator);

    @Test
    public void testOptimize() {
        double min = -10.0;
        double max = 10.0;

        try {
            UnivariateRealPointValuePair result = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
            Assert.assertEquals(0.0, result.getPoint(), EPSILON);
            Assert.assertEquals(0.0, result.getValue(), EPSILON);
        } catch (ConvergenceException e) {
            e.printStackTrace();
        } catch (MathIllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeNoConvergence() throws ConvergenceException, MathIllegalStateException {
        double min = 0.0;
        double max = 1.0;
      
        multiStartOptimizer.optimize(function, GoalType.MAXIMIZE, min, max);
    }
}