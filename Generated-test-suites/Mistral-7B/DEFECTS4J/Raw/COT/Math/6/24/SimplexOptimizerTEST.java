package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunction;
import org.apache.commons.math3.optim.points.PointValuePair;
import org.apache.commons.math3.optim.simple.SimpleValueChecker;
import org.apache.commons.math3.optim.simple.AbstractSimplex;
import org.apache.commons.math3.optim.initializer.RandomSimplexInitializer;
import org.apache.commons.math3.optim.function.Add;
import org.apache.commons.math3.optim.function.Multiply;
import org.apache.commons.math3.optim.function.Subtract;
import org.apache.commons.math3.optim.function.UnaryFunction;
import org.apache.commons.math3.optim.function.Sine;
import org.apache.commons.math3.optim.function.Cos;
import org.apache.commons.math3.optim.function.Tan;
import org.apache.commons.math3.optim.function.Pow;
import org.apache.commons.math3.optim.function.Sqrt;
import org.apache.commons.math3.optim.function.Log;
import org.apache.commons.math3.optim.function.Exp;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testEmptyConstructor() {
        try {
            SimplexOptimizer optimizer = new SimplexOptimizer();
            fail("A null ConvergenceChecker is expected.");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testDeprecatedConstructorWithDefaultChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer.getChecker());
        assertEquals(SimpleValueChecker.class, optimizer.getChecker().getClass());
    }

    @Test
    public void testConstructorWithCustomChecker() {
        ConvergenceChecker<PointValuePair> customChecker = new SimpleValueChecker(1e-6, 1e-6);
        SimplexOptimizer optimizer = new SimplexOptimizer(customChecker);
        assertEquals(customChecker, optimizer.getChecker());
    }

    @Test
    public void testConstructorWithRelativeAbsolute() {
        SimplexOptimizer optimizer = new SimplexOptimizer(1e-6, 1e-6);
        assertNotNull(optimizer.getChecker());
        assertEquals(SimpleValueChecker.class, optimizer.getChecker().getClass());
        assertEquals(1e-6, optimizer.getChecker().getRelativeThreshold(), 1e-9);
        assertEquals(1e-6, optimizer.getChecker().getAbsoluteThreshold(), 1e-9);
    }

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new RandomSimplexInitializer(3).getInitialSimplex();
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.getSimplex());
    }

    @Test(expected = NullPointerException.class)
    public void testSetSimplexNull() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(null);
    }

    @Test
    public void testOptimizeWithNullFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        try {
            optimizer.optimize(1, null, GoalType.MINIMIZE, null);
            fail("NullPointerException expected.");
        } catch (NullArgumentException e) {
            //expected
        }
    }

    @Test
    public void testOptimizeWithNullOptimizationData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        OptimizationData[] data = new OptimizationData[1];
        try {
            optimizer.optimize(1, null, GoalType.MINIMIZE, data);
            fail("NullPointerException expected.");
        } catch (NullArgumentException e) {
            //expected
        }
    }

    @Test
    public void testMinimizationSimpleFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new Add(new Multiply(new Const(1.0), new Var(0)),
                new Multiply(new Const(1.0), new Var(1)));
        PointValuePair result = optimizer.optimize(10, function, GoalType.MINIMIZE, null);
        assertEquals(-2.0, result.getValue(), 1e-8);
        assertArrayEquals(new double[]{0.0, 1.0}, result.getPoint(), 1e-6);
    }

    @Test
    public void testMinimizationFunctionWithConstraints() {
        // This test case is not supported by this optimizer.
    }

    @Test
    public void testMaximizationSimpleFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new Add(new Multiply(new Const(1.0), new Var(0)),
                new Multiply(new Const(1.0), new Var(1)));
        PointValuePair result = optimizer.optimize(10, function, GoalType.MAXIMIZE, null);
        assertEquals(2.0, result.getValue(), 1e-8);
        assertArrayEquals(new double[]{0.0, 1.0}, result.getPoint(), 1e-6);
    }

    @Test
    public void testMinimizationSineFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new Sine(new Var(0));
        PointValuePair result = optimizer.optimize(10, function, GoalType.MINIMIZE, null);
        assertEquals(0.0, result.getValue(), 1e-5);
        assertArrayEquals(new double[]{Math.PI / 2}, result.getPoint(), 1e-3);
    }

    @Test
    public void testMaximizationSineFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new Negate(new Sine(new Var(0)));
        PointValuePair result = optimizer.optimize(10, function, GoalType.MAXIMIZE, null);
        assertEquals(Math.PI / 2, result.getValue(), 1e-5);
        assertArrayEquals(new double[]{-Math.PI / 2}, result.getPoint(), 1e-3);
    }

    @Test
    public void testMinimizationCosFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new Cos(new Var(0));
        PointValuePair result = optimizer.optimize(10, function, GoalType.MINIMIZE, null);
        assertEquals(1.0, result.getValue(), 1e-5);
        assertArrayEquals(new double[]{Math.PI}, result.getPoint(), 1e-3);
    }

    @Test
    public void testMaximizationCosFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new Negate(new Cos(new Var(0)));
        PointValuePair result = optimizer.optimize(10, function, GoalType.MAXIMIZE, null);
        assertEquals(-1.0, result.getValue(), 1e-5);
        assertArrayEquals(new double[]{-Math.PI}, result.getPoint(), 1e-3);
    }

    @Test
    public void testMinimizationTanFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new Tan(new Var(0));
        PointValuePair result = optimizer.optimize(10, function, GoalType.MINIMIZE, null);
        assertEquals(-Math.PI / 2, result.getValue(), 1e-5);
        assertArrayEquals(new double[]{Math.PI / 2}, result.getPoint(), 1e-3);
    }

    @Test
    public void testMaximizationTanFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new Negate(new Tan(new Var(0)));
        PointValuePair result = optimizer.optimize(10, function, GoalType.MAXIMIZE, null);
        assertEquals(Math.PI / 2, result.getValue(), 1e-5);
        assertArrayEquals(new double[]{-Math.PI / 2}, result.getPoint(), 1e-3);
    }

    @Test
    public void testMinimizationPowFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new Pow(new Var(0), new Const(2));
        PointValuePair result = optimizer.optimize(10, function, GoalType.MINIMIZE, null);
        assertEquals(0.0, result.getValue(), 1e-5);
        assertArrayEquals(new double[]{Math.log(0.25)}, result.getPoint(), 1e-3);
    }

    @Test
    public void testMaximizationPowFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new Negate(new Pow(new Var(0), new Const(2)));
        PointValuePair result = optimizer.optimize(10, function, GoalType.MAXIMIZE, null);
        assertEquals(1.0, result.getValue(), 1e-5);
        assertArrayEquals(new double[]{Math.log(1.0 / 0.25)}, result.getPoint(), 1e-3);
    }

    @Test
    public void testMinimizationSqrtFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new Sqrt(new Multiply(new Var(0), new Var(0)));
        PointValuePair result = optimizer.optimize(10, function, GoalType.MINIMIZE, null);
        assertEquals(0.0, result.getValue(), 1e-5);
        assertArrayEquals(new double[]{1.0}, result.getPoint(), 1e-3);
    }

    @Test
    public void testMaximizationSqrtFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new Negate(new Sqrt(new Multiply(new Var(0), new Var(0))));
        PointValuePair result = optimizer.optimize(10, function, GoalType.MAXIMIZE, null);
        assertEquals(1.0, result.getValue(), 1e-5);
        assertArrayEquals(new double[]{1.0}, result.getPoint(), 1e-3);
    }

    @Test
    public void testMinimizationLogFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new Log(new Exp(new Var(0)));
        PointValuePair result = optimizer.optimize(10, function, GoalType.MINIMIZE, null);
        assertEquals(0.0, result.getValue(), 1e-5);
        assertArrayEquals(new double[]{1.0}, result.getPoint(), 1e-3);
    }

    @Test
    public void testMaximizationLogFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new Negate(new Log(new Exp(new Var(0))));
        PointValuePair result = optimizer.optimize(10, function, GoalType.MAXIMIZE, null);
        assertEquals(1.0, result.getValue(), 1e-5);
        assertArrayEquals(new double[]{1.0 / Math.E}, result.getPoint(), 1e-3);
    }

    @Test
    public void testMinimizationExpFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new Exp(new Var(0));
        PointValuePair result = optimizer.optimize(10, function, GoalType.MINIMIZE, null);
        assertEquals(0.0, result.getValue(), 1e-5);
        assertArrayEquals(new double[]{Math.log(1.0)}, result.getPoint(), 1e-3);
    }

    @Test
    public void testMaximizationExpFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new Negate(new Exp(new Var(0)));
        PointValuePair result = optimizer.optimize(10, function, GoalType.MAXIMIZE, null);
        assertEquals(1.0, result.getValue(), 1e-5);
        assertArrayEquals(new double[]{Math.log(2.0)}, result.getPoint(), 1e-3);
    }

    @Test
    public void testMinimizationComplexFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new Add(new Multiply(new Var(0), new Var(1)),
                new Multiply(new Pow(new Var(0), new Const(2)), new Sqrt(new Var(0))));
        PointValuePair result = optimizer.optimize(10, function, GoalType.MINIMIZE, null);
        assertEquals(-1.0, result.getValue(), 1e-5);
        assertArrayEquals(new double[]{Math.log(1.0 / 2.0), Math.sqrt(Math.log(1.0 / 2.0))}, result.getPoint(), 1e-3);
    }

    @Test
    public void testMaximizationComplexFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new Negate(new Add(new Multiply(new Var(0), new Var(1)),
                new Multiply(new Pow(new Var(0), new Const(2)), new Sqrt(new Var(0)))));
        PointValuePair result = optimizer.optimize(10, function, GoalType.MAXIMIZE, null);
        assertEquals(1.0, result.getValue(), 1e-5);
        assertArrayEquals(new double[]{Math.log(2.0 / Math.sqrt(Math.log(2.0))), Math.sqrt(Math.log(2.0 / Math.sqrt(Math.log(2.0))))}, result.getPoint(), 1e-3);
    }

    @Test
    public void testMaxIterationsReached() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new Multiply(new Var(0), new Var(0));
        PointValuePair result = optimizer.optimize(1, function, GoalType.MINIMIZE, null);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-3);
        assertNotNull(result.getValue());
    }
}