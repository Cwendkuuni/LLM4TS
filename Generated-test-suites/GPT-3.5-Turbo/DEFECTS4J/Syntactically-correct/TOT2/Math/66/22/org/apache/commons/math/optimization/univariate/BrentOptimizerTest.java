import org.junit.Test;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BrentOptimizerTest {

    private BrentOptimizer brentOptimizer;

    @Test
    public void testOptimize_StartValue_MaxIterationsExceededException() {
        brentOptimizer = new BrentOptimizer();
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);

        try {
            brentOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0, 0.5, 0);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            assertTrue(e instanceof MaxIterationsExceededException);
        }
    }

    @Test
    public void testOptimize_NoStartValue_Success() throws MaxIterationsExceededException, FunctionEvaluationException {
        brentOptimizer = new BrentOptimizer();
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);

        double result = brentOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        // Additional assertions can be added here
    }

    @Test
    public void testLocalMin_WithNotStrictlyPositiveEps_ExceptionThrown() {
        brentOptimizer = new BrentOptimizer();
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);

        try {
            brentOptimizer.localMin(true, function, GoalType.MINIMIZE, 0.0, 0.5, 1.0, 0, 1.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            assertTrue(true);
        }
    }

    // Additional test cases can be added

}