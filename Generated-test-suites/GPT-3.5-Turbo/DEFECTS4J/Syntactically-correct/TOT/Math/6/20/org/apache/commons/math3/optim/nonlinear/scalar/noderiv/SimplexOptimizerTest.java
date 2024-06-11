import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.direct.SimplexOptimizer;

public class SimplexOptimizerTest {

    private SimplexOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new SimplexOptimizer();
    }

    @Test
    public void testOptimizeInternal() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] variables) {
                return variables[0] * variables[0] + variables[1] * variables[1];
            }
        };

        int maxEval = 100;
        GoalType goalType = GoalType.MINIMIZE;

        double[] startPoint = new double[] {1.0, 1.0};

        PointValuePair result = optimizer.optimizeInternal(maxEval, function, goalType, startPoint);

        double[] expectedPoint = new double[] {0.0, 0.0};
        double[] actualPoint = result.getPoint();
        assertEquals(expectedPoint.length, actualPoint.length);
        for (int i = 0; i < expectedPoint.length; i++) {
            assertEquals(expectedPoint[i], actualPoint[i], 1e-9);
        }

        double expectedValue = 0.0;
        double actualValue = result.getValue();
        assertEquals(expectedValue, actualValue, 1e-9);
    }

    @Test
    public void testDoOptimize() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] variables) {
                return variables[0] * variables[0] + variables[1] * variables[1];
            }
        };

        int maxEval = 100;
        GoalType goalType = GoalType.MINIMIZE;

        double[] startPoint = new double[] {1.0, 1.0};

        PointValuePair result = optimizer.optimize(maxEval, function, goalType, startPoint);

        double[] expectedPoint = new double[] {0.0, 0.0};
        double[] actualPoint = result.getPoint();
        assertEquals(expectedPoint.length, actualPoint.length);
        for (int i = 0; i < expectedPoint.length; i++) {
            assertEquals(expectedPoint[i], actualPoint[i], 1e-9);
        }

        double expectedValue = 0.0;
        double actualValue = result.getValue();
        assertEquals(expectedValue, actualValue, 1e-9);
    }

}