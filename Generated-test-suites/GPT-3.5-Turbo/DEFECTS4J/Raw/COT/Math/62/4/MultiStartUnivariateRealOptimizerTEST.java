import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.FunctionEvaluationException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.UnivariateRealPointValuePair;
import org.junit.Assert;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {
    @Test
    public void testOptimize() throws FunctionEvaluationException {
        // Create a mock optimizer for testing.
        BaseUnivariateRealOptimizer<UnivariateFunction> optimizer = new BaseUnivariateRealOptimizer<UnivariateFunction>() {
            public UnivariateRealPointValuePair optimize(UnivariateFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException {
                double result = (max - min) / 2; // Return the midpoint of the range as the result.
                return new UnivariateRealPointValuePair(result, f.value(result));
            }
            public int getMaxEvaluations() {
                return 0;
            }
            public int getEvaluations() {
                return 0;
            }
            public void setMaxEvaluations(int maxEvaluations) {}

        };

        // Create a MultiStartUnivariateRealOptimizer with 3 starts.
        MultiStartUnivariateRealOptimizer<UnivariateFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, null);

        // Test optimize method.
        double result = multiStartOptimizer.optimize(new UnivariateFunction() {
            public double value(double x) {
                return x * x; // Square the input.
            }
        }, GoalType.MINIMIZE, -1, 1).getPoint();

        Assert.assertEquals(0.0, result, 0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithNoConvergence() throws FunctionEvaluationException {
        // Create a mock optimizer for testing.
        BaseUnivariateRealOptimizer<UnivariateFunction> optimizer = new BaseUnivariateRealOptimizer<UnivariateFunction>() {
            public UnivariateRealPointValuePair optimize(UnivariateFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException {
                throw new ConvergenceException(LocalizedFormats.NO_CONVERGENCE_WITH_ANY_START_POINT, 3);
            }
            public int getMaxEvaluations() {
                return 0;
            }
            public int getEvaluations() {
                return 0;
            }
            public void setMaxEvaluations(int maxEvaluations) {}

        };

        // Create a MultiStartUnivariateRealOptimizer with 3 starts.
        MultiStartUnivariateRealOptimizer<UnivariateFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, null);

        // Test optimize method with an exception expected.
        multiStartOptimizer.optimize(new UnivariateFunction() {
            public double value(double x) {
                return x;
            }
        }, GoalType.MINIMIZE, -1, 1);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaWithoutOptimization() {
        // Create a mock optimizer for testing.
        BaseUnivariateRealOptimizer<UnivariateFunction> optimizer = new BaseUnivariateRealOptimizer<UnivariateFunction>() {
            public UnivariateRealPointValuePair optimize(UnivariateFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException {
                return null; // Return null in order to trigger MathIllegalStateException in getOptima method.
            }
            public int getMaxEvaluations() {
                return 0;
            }
            public int getEvaluations() {
                return 0;
            }
            public void setMaxEvaluations(int maxEvaluations) {}

        };

        // Create a MultiStartUnivariateRealOptimizer with 3 starts.
        MultiStartUnivariateRealOptimizer<UnivariateFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, null);

        // Test getOptima method without optimization.
        multiStartOptimizer.getOptima();
    }
}