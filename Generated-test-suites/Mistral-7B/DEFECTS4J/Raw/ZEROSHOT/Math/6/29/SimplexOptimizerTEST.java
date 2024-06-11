import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.optimizers.SimpleX;
import org.apache.commons.math3.optim.initializers.ArrayRealVectorInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeEnum;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimplexOptimizerTest {

    private MultivariateFunction function;
    private SimplexOptimizer optimizer;
    private PointValuePair result;
    private double[] startPoint = {1.0, 2.0};

    @Before
    public void setUp() {
        function = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(x[0] - 3.0, 2.0) + Math.pow(x[1] - 4.0, 2.0);
            }
        };

        optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-5, 1e-5));
        optimizer.setMaxEvaluations(100);
    }

    @Test
    public void testOptimize() {
        optimizer.optimize(startPoint, GoalTypeEnum.MINIMIZE, function);
        result = optimizer.getOptimum();
        Assert.assertEquals(0.0, result.getValue(), 1e-5);
        Assert.assertArrayEquals(new double[]{3.0, 4.0}, result.getPoint(), 1e-5);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        optimizer.optimize(startPoint, null, null);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullStartPoint() {
        optimizer.optimize(null, GoalTypeEnum.MINIMIZE, function);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullGoalType() {
        optimizer.optimize(startPoint, null, function);
    }

    @Test
    public void testOptimizerWithCustomSimplex() {
        SimpleX simplex = new SimpleX(new ArrayRealVectorInitializer(2));
        optimizer = new SimplexOptimizer(simplexim, new SimpleValueChecker(1e-5, 1e-5));
        optimizer.optimize(startPoint, GoalTypeEnum.MINIMIZE, function);
        result = optimizer.getOptimum();
        Assert.assertEquals(0.0, result.getValue(), 1e-5);
        Assert.assertArrayEquals(new double[]{3.0, 4.0}, result.getPoint(), 1e-5);
    }
}