
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    private BrentOptimizer brentOptimizer;

    @Before
    public void setUp() {
        brentOptimizer = new BrentOptimizer();
    }

    @Test
    public void testOptimizeWithStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = x -> x * x;
        double result = brentOptimizer.optimize(f, GoalType.MINIMIZE, -10, 10, 5);
        assertEquals(0, result, 1e-10);
    }

    @Test
    public void testOptimizeWithoutStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = x -> x * x;
        double result = brentOptimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
        assertEquals(0, result, 1e-10);
    }

    @Test
    public void testLocalMinWithMinimizeGoalType() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = x -> x * x;
        double result = brentOptimizer.localMin(true, f, GoalType.MINIMIZE, -10, 0, 10, 1e-10, 1e-10);
        assertEquals(0, result, 1e-10);
    }

    @Test
    public void testLocalMinWithMaximizeGoalType() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = x -> -x * x;
        double result = brentOptimizer.localMin(false, f, GoalType.MAXIMIZE, -10, 0, 10, 1e-10, 1e-10);
        assertEquals(0, result, 1e-10);
    }
}
