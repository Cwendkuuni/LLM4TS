import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Test;

public class BrentOptimizerTest {
    private BrentOptimizer optimizer;

    @Test
    public void testConstructor() {
        optimizer = new BrentOptimizer();
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        assertEquals(100, optimizer.getMaximalIterationCount());
        assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0);
        assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinInvalidEpsilon() throws MaxIterationsExceededException, FunctionEvaluationException {
        optimizer = new BrentOptimizer();
        optimizer.localMin(true, null, GoalType.MINIMIZE, 0, 0, 0, -1, 1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinInvalidTolerance() throws MaxIterationsExceededException, FunctionEvaluationException {
        optimizer = new BrentOptimizer();
        optimizer.localMin(true, null, GoalType.MINIMIZE, 0, 0, 0, 1, -1);
    }

    // Add more test cases to comprehensively test the BrentOptimizer methods

}