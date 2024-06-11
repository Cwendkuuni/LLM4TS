import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.UnivariateRealPointValuePair;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.exception.MathIllegalStateException;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testOptimize() throws FunctionEvaluationException {
        BaseUnivariateRealOptimizer optimizer = new BaseUnivariateRealOptimizer() {
            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f,
                                                         GoalType goal,
                                                         double min, double max)
                throws FunctionEvaluationException {
                if (min > max) {
                    throw new IllegalArgumentException("Invalid interval");
                }
                double x = (max + min) / 2;
                double y = f.value(x);
                return new UnivariateRealPointValuePair(x, y);
            }
            public int getMaxEvaluations() {
                return 100;
            }
            public int getEvaluations() {
                return 0;
            }
            public void setMaxEvaluations(int maxEvaluations) {
            }
        };

        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, null);

        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        GoalType goal = GoalType.MINIMIZE;
        double min = -10;
        double max = 10;

        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(f, goal, min, max);

        double actual = result.getValue();
        double expected = 0;

        assertEquals(expected, actual, 0.0001);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaWithoutOptimize() {
        BaseUnivariateRealOptimizer optimizer = new BaseUnivariateRealOptimizer() {
            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f,
                                                         GoalType goal,
                                                         double min, double max)
                throws FunctionEvaluationException {
                throw new MathIllegalStateException("Optimize method not called");
            }
            public int getMaxEvaluations() {
                return 100;
            }
            public int getEvaluations() {
                return 0;
            }
            public void setMaxEvaluations(int maxEvaluations) {
            }
        };

        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, null);

        multiStartOptimizer.getOptima();
    }

}