import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.UnivariateRealPointValuePair;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.random.JDKRandomGenerator;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testOptimizeBasic() throws FunctionEvaluationException{
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(MockOptimizer(), 5, new JDKRandomGenerator());
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = (MultiStartUnivariateRealOptimizer<UnivariateRealFunction>) optimizer;

        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(new MockFunction(), GoalType.MINIMIZE, 0.0, 10.0);

        assertNotNull(result);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaBeforeOptimize() {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(MockOptimizer(), 5, new JDKRandomGenerator());
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = (MultiStartUnivariateRealOptimizer<UnivariateRealFunction>) optimizer;

        multiStartOptimizer.getOptima();
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeNoConvergence() throws FunctionEvaluationException {
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(MockOptimizerNoConvergence(), 5, new JDKRandomGenerator());
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = (MultiStartUnivariateRealOptimizer<UnivariateRealFunction>) optimizer;

        multiStartOptimizer.optimize(new MockFunction(), GoalType.MINIMIZE, 0.0, 10.0);
    }

    private BaseUnivariateRealOptimizer<UnivariateRealFunction> MockOptimizer() {
        return new BaseUnivariateRealOptimizer<UnivariateRealFunction>() {
            @Override
            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException {
                return new UnivariateRealPointValuePair(0.0, 0.0);
            }

            @Override
            public int getMaxEvaluations() {
                return 0;
            }

            @Override
            public int getEvaluations() {
                return 0;
            }

            @Override
            public void setMaxEvaluations(int maxEvaluations) {}

            @Override
            public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {}

            @Override
            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
                return null;
            }
        };
    }

    private BaseUnivariateRealOptimizer<UnivariateRealFunction> MockOptimizerNoConvergence() {
        return new BaseUnivariateRealOptimizer<UnivariateRealFunction>() {
            @Override
            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException {
                throw new ConvergenceException();
            }

            @Override
            public int getMaxEvaluations() {
                return 0;
            }

            @Override
            public int getEvaluations() {
                return 0;
            }

            @Override
            public void setMaxEvaluations(int maxEvaluations) {}

            @Override
            public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {}

            @Override
            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
                return null;
            }
        };
    }

    private class MockFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return x * x + 2 * x + 1;
        }
    }

}