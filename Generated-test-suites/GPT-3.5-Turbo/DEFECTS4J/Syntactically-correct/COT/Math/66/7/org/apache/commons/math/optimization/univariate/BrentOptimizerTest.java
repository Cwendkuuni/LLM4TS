
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;
    
    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }

    @Test
    public void testOptimize() throws Exception {
        double result = optimizer.optimize(new TestFunction(), GoalType.MINIMIZE, 0, 1, 0.5);
        assertEquals(0.0, result, 1e-10);
    }

    @Test
    public void testOptimizeWithMaxIterationsExceeded() {
        try {
            optimizer.setMaximalIterationCount(1);
            optimizer.optimize(new TestFunction(), GoalType.MINIMIZE, 0, 1);
            fail("MaxIterationsExceededException not thrown");
        } catch (MaxIterationsExceededException e) {
            // Exception expected
        } catch (Exception e) {
            fail("Unexpected exception type thrown");
        }
    }

    @Test
    public void testOptimizeWithFunctionEvaluationException() {
        try {
            optimizer.optimize(new NaNFunction(), GoalType.MINIMIZE, 0, 1);
            fail("FunctionEvaluationException not thrown");
        } catch (FunctionEvaluationException e) {
            // Exception expected
        } catch (Exception e) {
            fail("Unexpected exception type thrown");
        }
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testOptimizeWithEmptyInterval() {
        try {
            optimizer.optimize(new TestFunction(), GoalType.MINIMIZE, 1, 0);
            fail("IllegalArgumentException not thrown for empty interval");
        } catch (IllegalArgumentException e) {
            // Exception expected
        } catch (Exception e) {
            fail("Unexpected exception type thrown");
        }
    }

    @Test
    public void testOptimizeWithNegativeTolerance() {
        try {
            optimizer.optimize(new TestFunction(), GoalType.MINIMIZE, 0, 1, -1);
            fail("NotStrictlyPositiveException not thrown for negative tolerance");
        } catch (NotStrictlyPositiveException e) {
            // Exception expected
        } catch (Exception e) {
            fail("Unexpected exception type thrown");
        }
    }

    @Test
    public void testOptimizeWithZeroTolerance() {
        try {
            optimizer.optimize(new TestFunction(), GoalType.MINIMIZE, 0, 1, 0);
            fail("NotStrictlyPositiveException not thrown for zero tolerance");
        } catch (NotStrictlyPositiveException e) {
            // Exception expected
        } catch (Exception e) {
            fail("Unexpected exception type thrown");
        }
    }

    private static class TestFunction implements UnivariateRealFunction {
        public double value(double x) {
            return x;
        }
    }

    private static class NaNFunction implements UnivariateRealFunction {
        public double value(double x) {
            return Double.NaN;
        }
    }

}
