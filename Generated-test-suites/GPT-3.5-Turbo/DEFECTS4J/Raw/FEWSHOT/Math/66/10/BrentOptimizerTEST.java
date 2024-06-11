import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;
    private UnivariateRealFunction function;

    @Before
    public void setup() {
        optimizer = new BrentOptimizer();
        function = mock(UnivariateRealFunction.class);
    }

    @Test
    public void testOptimize_withStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        when(function.value(0.5)).thenReturn(5.0);
        double result = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0, 0.5);
        assertEquals(0.5, result, 1e-5);
    }

    @Test
    public void testOptimize_withoutStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        when(function.value(0.618)).thenReturn(10.0);
        double result = optimizer.optimize(function, GoalType.MINIMIZE, 0.6, 0.7);
        assertEquals(0.618, result, 1e-5);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimize_maxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        optimizer.setMaximalIterationCount(10);
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimize_functionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        when(function.value(0.5)).thenThrow(new FunctionEvaluationException(0.5));
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0, 0.5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMin_invalidEps() throws MaxIterationsExceededException, FunctionEvaluationException {
        optimizer.localMin(false, function, GoalType.MINIMIZE, 0.0, 0.5, 1.0, -0.1, 0.1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMin_invalidT() throws MaxIterationsExceededException, FunctionEvaluationException {
        optimizer.localMin(false, function, GoalType.MINIMIZE, 0.0, 0.5, 1.0, 0.1, -0.1);
    }

    @Test
    public void testLocalMin_stopCriteria() throws MaxIterationsExceededException, FunctionEvaluationException {
        double result = optimizer.localMin(true, function, GoalType.MINIMIZE, 0.0, 0.5, 1.0, 0.1, 0.1);
        assertNull(result);
    }

    @Test
    public void testLocalMin_parabolicInterpolation() throws MaxIterationsExceededException, FunctionEvaluationException {
        double result = optimizer.localMin(true, function, GoalType.MINIMIZE, 0.5, 1.0, 1.5, 0.1, 0.1);
        assertEquals(1.0, result, 1e-5);
    }

    @Test
    public void testLocalMin_goldenSection() throws MaxIterationsExceededException, FunctionEvaluationException {
        double result = optimizer.localMin(true, function, GoalType.MINIMIZE, 0.0, 0.2, 1.0, 0.1, 0.1);
        assertEquals(0.2, result, 1e-5);
    }
}