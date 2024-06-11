import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.optim.InitialGuess.AUTO;
import static org.apache.commons.math3.optim.MaxEval.INFINITY;
import static org.apache.commons.math3.optim.PointValuePair.Pair;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() throws Exception {
        // Test with a simple function
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4 * x + 4;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(function, GoalType.MINIMIZE, -3.0, 3.0, 0.0).getPoint();
        Assert.assertEquals(1.0, result, 1e-6);

        // Test with a function that has multiple local minima
        UnivariateRealFunction function2 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x < 0) {
                    return Math.pow(Math.abs(x), 0.5) * Math.sin(Math.PI * x);
                } else {
                    return Math.pow(x, 3) - 4 * x + 1;
                }
            }
        };

        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(100);
        optimizer.setInitialGuess(AUTO);

        double min = optimizer.optimize(function2, GoalType.MINIMIZE, -5.0, 5.0, 0.0).getPoint();
        Assert.assertTrue(min > -3.0 && min < 3.0);

        Pair pair = optimizer.optimize(function2, GoalType.MINIMIZE, -10.0, -1.0, 1.0);
        double min2 = pair.getPoint();
        Assert.assertEquals(-1.0, min2, 1e-6);

        // Test with a function that has no real root
        UnivariateRealFunction function3 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(Math.abs(x)) - Math.log(Math.abs(x) + 1);
            }
        };

        try {
            optimizer.optimize(function3, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
            Assert.fail();
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }
}