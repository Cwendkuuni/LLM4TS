import org.apache.commons.math.ExceptionUtil;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.QuinticFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.optimization.univariate.AbstractUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.UnivariateRealPointValuePair;
import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;

public class MultiStartUnivariateRealOptimizerTest {
    private static final double DEFAULT_TOLERANCE = 1e-6;

    @Test
    public void testConvergence() throws Exception {
        ConvergenceChecker<UnivariateRealPointValuePair> checker = new ConvergenceChecker<UnivariateRealPointValuePair>() {
            public boolean converged(int iteration, UnivariateRealPointValuePair previous, UnivariateRealPointValuePair current) {
                return Math.abs(previous.getValue() - current.getValue()) < DEFAULT_TOLERANCE;
            }
        };

        MultiStartUnivariateRealOptimizer<QuinticFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<QuinticFunction>(new TestOptimizer(checker), 3, null);

        UnivariateRealPointValuePair result = optimizer.optimize(new QuinticFunction(), GoalType.MINIMIZE, -1, 1);
        assertNotNull(result);
    }

    @Test
    public void testNoConvergence() throws Exception {
        ConvergenceChecker<UnivariateRealPointValuePair> checker = new ConvergenceChecker<UnivariateRealPointValuePair>() {
            public boolean converged(int iteration, UnivariateRealPointValuePair previous, UnivariateRealPointValuePair current) {
                return false;
            }
        };

        MultiStartUnivariateRealOptimizer<QuinticFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<QuinticFunction>(new TestOptimizer(checker), 3, null);

        try {
            UnivariateRealPointValuePair result = optimizer.optimize(new QuinticFunction(), GoalType.MINIMIZE, -1, 1);
        } catch (ConvergenceException e) {
            assertEquals(LocalizedFormats.NO_CONVERGENCE_WITH_ANY_START_POINT, e.getPattern());
            assertEquals(3, e.getArguments()[0]);
        }
    }

    private static class TestOptimizer extends AbstractUnivariateRealOptimizer {
        private ConvergenceChecker<UnivariateRealPointValuePair> checker;

        TestOptimizer(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
            this.checker = checker;
        }

        public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goalType, double min, double max) throws FunctionEvaluationException {
            double x = (min + max) / 2;
            double value = f.value(x);
            return new UnivariateRealPointValuePair(x, value);
        }

        public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker) {
            checker = convergenceChecker;
        }

        public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
            return checker;
        }
    }

}