import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.direct.SimplexOptimizer;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.functions.SphereFunction;
import org.apache.commons.math3.optim.nonlinear.functions.SimpleFunction;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.initializer.RandomMinimumInitializer;
import org.apache.commons.math3.optim.initializer.SimpleXInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.convergence.SimpleValueChecker;
import org.apache.commons.math3.optim.PointValues;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SimplexOptimizerTest {
    /**
     * Test constructor with SimpleValueChecker.
     */
    @Test
    public void testSimplexOptimizerConstructionWithSimpleValueChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer);
    }

    /**
     * Test constructor with SimpleValueChecker and given relative and absolute thresholds.
     */
    @Test
    public void testSimplexOptimizerConstructionWithSimpleValueCheckerThresholds() {
        double rel = 1e-6;
        double abs = 1e-6;
        SimplexOptimizer optimizer = new SimplexOptimizer(rel, abs);
        assertNotNull(optimizer);
    }

    /**
     * Test constructor with provided ConvergenceChecker.
     */
    @Test
    public void testSimplexOptimizerConstructionWithCustomConvergenceChecker() {
        SimpleValueChecker checker = new SimpleValueChecker();
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        assertNotNull(optimizer);
    }

    /**
     * Test setSimplex with a valid AbstractSimplex object.
     */
    @Test
    public void testSetSimplexWithValidAbstractSimplex() {
        AbstractSimplex simplex = new SimpleXInitializer(3).getInitialSimplex();
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(simplex);
        assertNotNull(optimizer.simplex);
    }

    /**
     * Test optimize with a valid MultivariateFunction, GoalType, and optimization data.
     */
    @Test
    public void testOptimizeWithValidMultivariateFunctionGoalTypeAndOptimizationData() {
        SphereFunction function = new SphereFunction(2);
        SimplexOptimizer optimizer = new SimplexOptimizer();

        double[] startPoint = new double[]{1.0, 1.0};
        PointValues pointValues = optimizer.optimize(10, function, GoalType.MINIMIZE, new Object[]{startPoint});

        PointValuePair expectedPointValuePair = new PointValuePair(new double[]{0.0, 0.0}, 0.0);
        PointValuePair actualPointValuePair = pointValues.getPointValues().get(0);

        assertEquals(expectedPointValuePair, actualPointValuePair);
    }

    /**
     * Test optimize with a valid MultivariateFunction, GoalType, and optimization data with initalized AbstractSimplex.
     */
    @Test
    public void testOptimizeWithValidMultivariateFunctionGoalTypeAndOptimizationDataWithInitializedAbstractSimplex() {
        SphereFunction function = new SphereFunction(2);
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new SimpleXInitializer(3).getInitialSimplex();
        optimizer.setSimplex(simplex);

        double[] startPoint = new double[]{1.0, 1.0};
        PointValues pointValues = optimizer.optimize(10, function, GoalType.MINIMIZE, new Object[]{startPoint});

        PointValuePair expectedPointValuePair = new PointValuePair(new double[]{0.0, 0.0}, 0.0);
        PointValuePair actualPointValuePair = pointValues.getPointValues().get(0);

        assertEquals(expectedPointValuePair, actualPointValuePair);
    }

    /**
     * Test optimize with a valid MultivariateFunction, GoalType, and optimization data with random initialization.
     */
    @Test
    public void testOptimizeWithValidMultivariateFunctionGoalTypeAndOptimizationDataWithRandomInitialization() {
        SphereFunction function = new SphereFunction(2);
        SimplexOptimizer optimizer = new SimplexOptimizer();

        double[] startPoint = new double[]{2.0, 2.0};
        PointValues pointValues = optimizer.optimize(10, function, GoalType.MINIMIZE, new Object[]{startPoint});

        PointValuePair expectedPointValuePair = new PointValuePair(new double[]{0.0, 0.0}, 0.0);
        PointValuePair actualPointValuePair = pointValues.getPointValues().get(0);

        assertEquals(expectedPointValuePair, actualPointValuePair);
    }

    /**
     * Test optimize with a null MultivariateFunction.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeWithNullMultivariateFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(null, GoalType.MINIMIZE, null);
    }

    /**
     * Test optimize with a valid GoalType and null optimization data.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeWithValidGoalTypeAndNullOptimizationData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(10, new SimpleFunction("x^2"), GoalType.MINIMIZE, null);
    }

    /**
     * Test optimize with invalid optimization data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithInvalidOptimizationData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(10, new SimpleFunction("x^2"), GoalType.MINIMIZE, new Object[]{"InvalidData"});
    }
}