import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.TestMultivariateFunctions;
import org.apache.commons.math3.optim.initializers.ArrayRealVectorInitializer;
import org.apache.commons.math3.optim.initializers.SimplexInitializer;
import org.apache.commons.math3.optim.point.RealPointValuePair;
import org.apache.commons.math3.optim.points.RealPoint;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.simpledirect.AbstractSimplex;
import org.apache.commons.math3.optim.simpledirect.NelderMeadSimplex;
import org.apache.commons.math3.optim.test.AbstractOptimizationTest;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SimplexOptimizerTest extends AbstractOptimizationTest {

    private MultivariateFunction testFunction;
    private MultivariateFunction testFunction2;
    private AbstractSimplex simplex;

    @Before
    public void setUp() {
        testFunction = TestMultivariateFunctions.ROSENBROCK;
        testFunction2 = TestMultivariateFunctions.RASTRIGIN;
        simplex = new NelderMeadSimplex(10);
    }

    @Test
    public void testEmptyConstructor() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer.getChecker());
        assertNull(optimizer.getSimplex());
    }

    @Test
    public void testConstructorWithDefaultChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer.getChecker());
        assertNull(optimizer.getSimplex());
    }

    @Test
    public void testConstructorWithChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        assertNotNull(optimizer.getChecker());
        assertNull(optimizer.getSimplex());
    }

    @Test
    public void testConstructorWithRelAndAbs() {
        SimplexOptimizer optimizer = new SimplexOptimizer(1.0E-5, 1.0E-5);
        assertNotNull(optimizer.getChecker());
        assertNull(optimizer.getSimplex());
    }

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(simplex);
        assertNotNull(optimizer.getSimplex());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetSimplexNull() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(null);
    }

    @Test
    public void testOptimizeWithNoEvaluations() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, null);
        assertNotNull(result);
    }

    @Test
    public void testOptimizeWithNoFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        PointValuePair result = optimizer.optimize(null, GoalType.MINIMIZE, null);
        assertNull(result);
    }

    @Test
    public void testOptimizeWithGoalTypeNull() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        PointValuePair result = optimizer.optimize(testFunction, null, null);
        assertNull(result);
    }

    @Test
    public void testOptimizeWithDataNull() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, null);
        assertNull(result);
    }

    @Test
    public void testOptimizeWithInitialGuessNull() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        OptimizationData optimizationData = new OptimizationData();
        optimizationData.setInitialGuess(null);
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, optimizationData);
        assertNull(result);
    }

    @Test
    public void testOptimizeWithInitialGuessOneDim() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        OptimizationData optimizationData = new OptimizationData();
        optimizationData.setInitialGuess(new RealPoint(1.0));
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, optimizationData);
        assertNotNull(result);
        assertEquals(1.0, result.getPoint().getComponent(0), 1.0E-9);
    }

    @Test
    public void testOptimizeWithInitialGuessMultiDim() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        OptimizationData optimizationData = new OptimizationData();
        optimizationData.setInitialGuess(new RealPoint(new double[]{1.0, 2.0}));
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, optimizationData);
        assertNotNull(result);
        assertEquals(1.0, result.getPoint().getComponent(0), 1.0E-9);
        assertEquals(2.0, result.getPoint().getComponent(1), 1.0E-9);
    }

    @Test
    public void testOptimizeWithInitialSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        OptimizationData optimizationData = new OptimizationData();
        optimizationData.setInitialGuess(new RealPoint(new double[]{1.0, 2.0}));
        optimizationData.setSimplex(new SimplexInitializer(3).getInitialSimpleX());
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, optimizationData);
        assertNotNull(result);
        assertEquals(1.0, result.getPoint().getComponent(0), 1.0E-9);
        assertEquals(2.0, result.getPoint().getComponent(1), 1.0E-9);
    }

    @Test
    public void testOptimizeWithFunctionAndData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        OptimizationData optimizationData = new OptimizationData();
        optimizationData.setInitialGuess(new RealPoint(new double[]{1.0, 2.0}));
        optimizationData.setSimplex(new SimplexInitializer(3).getInitialSimpleX());
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, optimizationData);
        assertNotNull(result);
        assertEquals(-3.0, result.getValue(), 1.0E-9);
        assertEquals(1.0, result.getPoint().getComponent(0), 1.0E-9);
        assertEquals(1.0, result.getPoint().getComponent(1), 1.0E-9);
    }

    @Test
    public void testOptimizeWithFunctionAndDataDifferentSize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        OptimizationData optimizationData = new OptimizationData();
        optimizationData.setInitialGuess(new RealPoint(new double[]{1.0, 2.0, 3.0}));
        optimizationData.setSimplex(new SimplexInitializer(2).getInitialSimpleX());
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, optimizationData);
        assertNull(result);
    }

    @Test
    public void testOptimizeWithFunctionAndDataEmpty() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        OptimizationData optimizationData = new OptimizationData();
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, optimizationData);
        assertNull(result);
    }

    @Test
    public void testOptimizeNoSolution() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction testFunction = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return FunctionUtils.MAX;
            }
        };
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, null);
        assertNull(result);
    }

    @Test
    public void testOptimizeNoSolution2() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction testFunction = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                if (Arrays.equals(x, new double[]{0.0, 0.0})) {
                    return FunctionUtils.MIN;
                }
                return FunctionUtils.MAX;
            }
        };
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, null);
        assertNull(result);
    }

    @Test
    public void testOptimizeSolution() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, null);
        assertNotNull(result);
        assertEquals(-3.0, result.getValue(), 1.0E-9);
        assertEquals(1.0, result.getPoint().getComponent(0), 1.0E-9);
        assertEquals(1.0, result.getPoint().getComponent(1), 1.0E-9);
    }

    @Test
    public void testOptimizeSolution2() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        PointValuePair result = optimizer.optimize(testFunction2, GoalType.MINIMIZE, null);
        assertNotNull(result);
        assertEquals(0.0, result.getValue(), 1.0E-9);
        assertEquals(Arrays.asList(1.0, 1.0, 1.0).toString(), Arrays.toString(result.getPoint().toArray()));
    }

    @Test
    public void testOptimizeSolution3() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        PointValuePair result = optimizer.optimize(testFunction2, GoalType.MAXIMIZE, null);
        assertNotNull(result);
        assertEquals(-20.0, result.getValue(), 1.0E-9);
        assertEquals(Arrays.asList(-3.2, -3.2, -3.2).toString(), Arrays.toString(result.getPoint().toArray()));
    }

    @Test
    public void testOptimizeSolutionCustomChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new MyCustomValueChecker());
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, null);
        assertNotNull(result);
        assertEquals(-3.0, result.getValue(), 1.0E-9);
        assertEquals(1.0, result.getPoint().getComponent(0), 1.0E-9);
        assertEquals(1.0, result.getPoint().getComponent(1), 1.0E-9);
    }

    private static class MyCustomValueChecker extends SimpleValueChecker {
        @Override
        public boolean isSatisfied(double optimalityGap, double currentValue, double previousValue) {
            // Custom logic for checking convergence
            return optimalityGap < 0.001;
        }
    }
}