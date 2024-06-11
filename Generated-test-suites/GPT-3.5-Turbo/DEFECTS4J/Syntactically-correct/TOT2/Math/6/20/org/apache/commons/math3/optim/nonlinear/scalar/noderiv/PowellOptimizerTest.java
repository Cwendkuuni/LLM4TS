import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.direct.PowellOptimizer;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Before;

public class PowellOptimizerTest {

    private PowellOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new PowellOptimizer(1e-6, 1e-6);
    }

    @Test
    public void testDoOptimizeMinimize() {
        double[] startPoint = {1, 2};
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1];
            }
        };
        PointValuePair result = optimizer.optimize(10, function, GoalType.MINIMIZE, startPoint);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimizeMaximize() {
        double[] startPoint = {1, 2};
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return -point[0] - point[1];
            }
        };
        PointValuePair result = optimizer.optimize(10, function, GoalType.MAXIMIZE, startPoint);
        assertEquals(-3.0, result.getValue(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidAbsThreshold() {
        new PowellOptimizer(1e-6, -1e-6);
    }

    @Test
    public void testDefaultConstructor() {
        PowellOptimizer defaultOptimizer = new PowellOptimizer(1e-6, 1e-6);
        assertNotNull(defaultOptimizer);
    }
}