import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.optimization.univariate.AbstractUnivariateRealOptimizer;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }

    @Test
    public void testOptimizeWithStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 5, 2);
        assertEquals(result, 2, 1E-10);
    }

    @Test
    public void testOptimizeWithoutStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 5);
        assertEquals(result, 2, 1E-10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeWithNegativeEps() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };
        optimizer.optimize(f, GoalType.MINIMIZE, 0, 5, 2);
        optimizer.setRelativeAccuracy(-1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeWithNegativeT() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };
        optimizer.optimize(f, GoalType.MINIMIZE, 0, 5, 2);
        optimizer.setAbsoluteAccuracy(-1);
    }
}

