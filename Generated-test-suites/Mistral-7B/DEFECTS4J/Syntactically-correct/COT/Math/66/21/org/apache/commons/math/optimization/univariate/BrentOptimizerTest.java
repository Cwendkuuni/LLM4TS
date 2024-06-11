import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer();
        // Test optimization with bounds and initial guess
        UnivariateRealFunction testFunction = new TestFunction();
        double min = -5.0;
        double max = 5.0;
        double startValue = 0.0;
        try {
            PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, min, max, startValue);
            assertEquals(-2.0, result.getPoint(), 0.001);
            assertEquals(-4.023356237E-14, result.getValue(), 0.001);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during optimization: " + e.getMessage());
        }

        // Test optimization without bounds
        try {
            PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, null, null, startValue);
            assertEquals(-2.0, result.getPoint(), 0.001);
            assertEquals(-4.023356237E-14, result.getValue(), 0.001);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during optimization: " + e.getMessage());
        }

        // Test optimization with an unbounded function
        UnivariateRealFunction unboundedFunction = new UnboundedFunction();
        try {
            PointValuePair result = optimizer.optimize(unboundedFunction, GoalType.MINIMIZE, min, max, startValue);
            fail("Expected MaxIterationsExceededException but optimization completed.");
        } catch (MaxIterationsExceededException e) {
            // Expected result
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test optimization with a non-univariate function
        UnivariateRealFunction nonUnivariateFunction = new NonUnivariateFunction();
        try {
            PointValuePair result = optimizer.optimize(nonUnivariateFunction, GoalType.MINIMIZE, min, max, startValue);
            fail("Expected IllegalArgumentException but optimization completed.");
        } catch (IllegalArgumentException e) {
            // Expected result
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test optimization with a null function
        try {
            PointValuePair result = optimizer.optimize(null, GoalType.MINIMIZE, min, max, startValue);
            fail("Expected NullPointerException but optimization completed.");
        } catch (NullPointerException e) {
            // Expected result
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    private static class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x - 2, 2);
        }
    }

    private static class UnboundedFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return 1.0 / x;
        }
    }

    private static class NonUnivariateFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            throw new UnsupportedOperationException("This function is not univariate.");
        }
    }
}