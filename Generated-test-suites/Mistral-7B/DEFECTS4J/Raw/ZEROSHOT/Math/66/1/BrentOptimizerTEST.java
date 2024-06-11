import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        UnivariateRealFunction function = new MyFunction();
        double min = -3.0;
        double max = 3.0;
        double startValue = 0.0;
        double relativeAccuracy = 1e-5;
        double absoluteAccuracy = 1e-5;
        int maximalIterationCount = 50;

        try {
            PointValuePair result = new BrentOptimizer().optimize(function, GoalType.MINIMIZE, min, max, startValue);
            double optimum = result.getPoint();
            double minimumValue = result.getValue();
            assertEquals("Brent Optimizer failed to find minimum", -1.0, minimumValue, 1e-5);
            assertEquals("Brent Optimizer failed to find correct minimum", 0.0, optimum, 1e-5);

            result = new BrentOptimizer().optimize(function, GoalType.MAXIMIZE, min, max, startValue);
            double maximumValue = result.getValue();
            double maximumPoint = result.getPoint();
            assertEquals("Brent Optimizer failed to find maximum", 1.0, maximumValue, 1e-5);
            assertEquals("Brent Optimizer failed to find correct maximum", 1.0, maximumPoint, 1e-5);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("An error occurred during optimization: " + e.getMessage());
        }
    }

    private static class MyFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 3) - 2 * x * x + x;
        }

        @Override
        public double derive(double x) {
            double functionValue = value(x);
            return 3 * Math.pow(x, 2) - 2 * x;
        }
    }
}