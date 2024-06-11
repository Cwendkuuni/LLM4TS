import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.function.Functions;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.RealVectorValue;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MultiDirectionalTest {

    private ObjectiveFunction function;
    private RealVectorValue bestSolution;
    private MultiDirectional optimizer;

    @Before
    public void setUp() {
        function = new Functions.Sine();
        optimizer = new MultiDirectional();
        bestSolution = new RealVectorValue(new double[] {Math.PI / 2});
    }

    @Test
    public void testIterateSimplexWithDefaultValues() throws OptimizationException {
        SimpleBounds bounds = new SimpleBounds(new double[] {-Math.PI, Math.PI}, new double[] {-Math.PI, Math.PI});
        RealPointValuePair initialPoint = new RealPointValuePair(new double[] {0}, function.value(new double[] {0}), false);
        optimizer.optimize(function, bounds, initialPoint, GoalType.MINIMIZE, 100);
        assertEquals(bestSolution.getPoint(), optimizer.getBestSolution().getPointRef(), 0.001);
        assertEquals(bestSolution.getValue(), optimizer.getBestSolution().getValue(), 0.001);
    }

    @Test
    public void testIterateSimplexWithCustomValues() throws OptimizationException {
        double khi = 3.0;
        double gamma = 0.7;
        SimpleBounds bounds = new SimpleBounds(new double[] {-Math.PI, Math.PI}, new double[] {-Math.PI, Math.PI});
        RealPointValuePair initialPoint = new RealPointValuePair(new double[] {0}, function.value(new double[] {0}), false);
        optimizer = new MultiDirectional(khi, gamma);
        optimizer.optimize(function, bounds, initialPoint, GoalType.MINIMIZE, 100);
        assertEquals(bestSolution.getPoint(), optimizer.getBestSolution().getPointRef(), 0.001);
        assertEquals(bestSolution.getValue(), optimizer.getBestSolution().getValue(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() throws OptimizationException {
        SimpleBounds bounds = new SimpleBounds(new double[] {-Math.PI, Math.PI}, new double[] {Math.PI, -Math.PI});
        RealPointValuePair initialPoint = new RealPointValuePair(new double[] {0}, Double.NaN, false);
        optimizer.optimize(function, bounds, initialPoint, GoalType.MINIMIZE, 100);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws OptimizationException {
        SimpleBounds bounds = new SimpleBounds(new double[] {-Math.PI, Math.PI}, new double[] {-Math.PI, Math.PI});
        RealPointValuePair initialPoint = new RealPointValuePair(new double[] {0}, Math.PI, false);
        optimizer.optimize(function, bounds, initialPoint, GoalType.MINIMIZE, 100);
    }

    @Test(expected = OptimizationException.class)
    public void testOptimizationException() throws OptimizationException {
        SimpleBounds bounds = new SimpleBounds(new double[] {-Math.PI, Math.PI}, new double[] {-Math.PI, Math.PI});
        RealPointValuePair initialPoint = new RealPointValuePair(new double[] {0}, Double.POSITIVE_INFINITY, false);
        optimizer.optimize(function, bounds, initialPoint, GoalType.MINIMIZE, 100);
    }
}