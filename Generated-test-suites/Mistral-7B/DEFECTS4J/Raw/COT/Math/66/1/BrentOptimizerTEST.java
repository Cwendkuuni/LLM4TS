import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.AbstractUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.NonConvergentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;
    private UnivariateRealFunction function;

    @Before
    public void setup() {
        optimizer = new BrentOptimizer();
        function = f -> Math.pow(Math.sin(f), 2);
    }

    @Test
    public void testBasicOptimizeMin() throws MaxIterationsExceededException, NonConvergentException {
        double min = optimizer.optimize(function, GoalType.MINIMIZE, -2.0, 2.0, 0.0);
        Assert.assertEquals(Math.PI / 2, min, 1e-6);
    }

    @Test
    public void testBasicOptimizeMax() throws MaxIterationsExceededException, NonConvergentException {
        double max = optimizer.optimize(function.negate(), GoalType.MAXIMIZE, -2.0, 2.0, 0.0);
        Assert.assertEquals(Math.PI / 2, max, 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationExceeded() throws MaxIterationsExceededException {
        SimpleBounds bounds = new SimpleBounds(0.0, 1.0);
        optimizer.setMaxEvaluations(0);
        optimizer.optimize(function, GoalType.MINIMIZE, bounds.getMin(), bounds.getMax());
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        UnivariateRealFunction exceptionFunction = f -> {
            throw new FunctionEvaluationException("Function Evaluation Exception");
        };
        optimizer.optimize(exceptionFunction, GoalType.MINIMIZE, -1.0, 1.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveException() {
        optimizer.setMaxEvaluations(-1);
    }

}