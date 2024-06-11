import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.AbstractUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.NonNegativeMaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Assert;
import org.junit.Test;

import java.util.function.Supplier;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        UnivariateRealFunction function = new MyFunction();
        BrentOptimizer optimizer = new BrentOptimizer();

        // Test optimization with goal type MINIMIZE
        double min = optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, 0.0)[0];
        Assert.assertEquals(1.0, min, 0.01);

        // Test optimization with goal type MAXIMIZE
        double max = optimizer.optimize(function, GoalType.MAXIMIZE, -10.0, 10.0, 0.0)[0];
        Assert.assertEquals(-1.0, max, 0.01);

        // Test optimization with initial point
        double initialPoint = 5.0;
        double result = optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, initialPoint)[0];
        Assert.assertEquals(result, 1.0, 0.01);

        // Test optimization with bounds
        double minBound = 0.0;
        double maxBound = 5.0;
        double result2 = optimizer.optimize(function, GoalType.MINIMIZE, minBound, maxBound, initialPoint)[0];
        Assert.assertEquals(result2, 1.0, 0.01);

        // Test optimization with max iterations
        BrentOptimizer optimizerMaxIterations = new BrentOptimizer();
        optimizerMaxIterations.setMaxIterations(1);
        try {
            optimizerMaxIterations.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, 0.0);
            Assert.fail();
        } catch (MaxIterationsExceededException e) {
            // Expected
        }

        // Test optimization with max evaluations
        UnivariateRealOptimizer optimizerWithEvaluations = new NonNegativeMaxEval(new BrentOptimizer(), 1);
        try {
            optimizerWithEvaluations.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, 0.0);
            Assert.fail();
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    static class MyFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return x * x - 2 * x + 1;
        }

        @Override
        public double derivative(double x) {
            return 2 * x - 2;
        }
    }
}

class AbstractUnivariateRealOptimizer {
    // Empty class
}