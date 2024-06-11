import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.AbstractUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new SimpleFunction();
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;

        try {
            PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);
            double optimum = result.getPoint();
            double fx = result.getValue();
            System.out.println("Minimum found at x = " + optimum + ", f(x) = " + fx);
            assertEquals(0.36269, optimum, 1e-6);
            assertEquals(-0.028232129527377, fx, 1e-6);

            function = new SimpleFunctionNegative();
            result = optimizer.optimize(function, GoalType.MAXIMIZE, min, max, startValue);
            optimum = result.getPoint();
            fx = result.getValue();
            System.out.println("Maximum found at x = " + optimum + ", f(x) = " + fx);
            assertEquals(0.63712, optimum, 1e-6);
            assertEquals(0.028232129527377, fx, 1e-6);

            function = new SimpleFunctionWithInflectionPoint();
            result = optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);
            optimum = result.getPoint();
            fx = result.getValue();
            System.out.println("Minimum found at x = " + optimum + ", f(x) = " + fx);
            assertEquals(0.4111, optimum, 1e-6);
            assertEquals(-0.005221022024363, fx, 1e-6);

            result = optimizer.optimize(function, GoalType.MAXIMIZE, min, max, startValue);
            optimum = result.getPoint();
            fx = result.getValue();
            System.out.println("Maximum found at x = " + optimum + ", f(x) = " + fx);
            assertEquals(0.5889, optimum, 1e-6);
            assertEquals(0.005221022024363, fx, 1e-6);

            // Test MaxIterationsExceededException
            optimizer.setMaxIterations(1);
            try {
                optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);
                fail("MaxIterationsExceededException expected");
            } catch (MaxIterationsExceededException e) {
                // Expected
            }

            // Test NotStrictlyPositiveException
            optimizer.setMaxIterations(100);
            optimizer.setMaxEvaluations(1);
            try {
                optimizer.optimize(function, GoalType.MINIMIZE, -1.0, max, startValue);
                fail("NotStrictlyPositiveException expected");
            } catch (NotStrictlyPositiveException e) {
                // Expected
            }
        } catch (FunctionEvaluationException e) {
            System.err.println("Error evaluating function.");
            e.printStackTrace();
        }
    }

    private static class SimpleFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return Math.pow(x - 0.4, 2) - 0.05;
        }
    }

    private static class SimpleFunctionNegative implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return -Math.pow(x - 0.4, 2) + 0.05;
        }
    }

    private static class SimpleFunctionWithInflectionPoint implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            if (x < 0.4) {
                return Math.pow(x, 2) - 0.04;
            } else {
                return Math.pow(x - 0.6, 2) + 0.04;
            }
        }
    }
}