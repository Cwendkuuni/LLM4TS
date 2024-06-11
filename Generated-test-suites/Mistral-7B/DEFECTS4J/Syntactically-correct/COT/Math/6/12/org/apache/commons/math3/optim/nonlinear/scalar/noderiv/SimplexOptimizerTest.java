package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MultivariateFunction;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.initializer.ArrayRealVectorInitializer;
import org.apache.commons.math3.optimization.simplexs.NelderMeadSimplex;
import org.apache.commons.math3.optimization.simplexs.NelderMeadSimplexConfig;
import org.junit.Test;

/**
 * Test class for SimplexOptimizer.
 */
public class SimplexOptimizerTest {

    /**
     * Test method for {@link SimplexOptimizer#SimplexOptimizer()}.
     */
    @Test
    public void testConstructor() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer);
    }

    /**
     * Test method for {@link SimplexOptimizer#SimplexOptimizer(ConvergenceChecker)}
     * and {@link SimplexOptimizer#SimplexOptimizer(double, double)}.
     */
    @Test
    public void testConstructorsWithArguments() {
        SimpleValueChecker checker = new SimpleValueChecker(1e-6, 1e-6);
        SimplexOptimizer optimizer1 = new SimplexOptimizer(checker);
        SimplexOptimizer optimizer2 = new SimplexOptimizer(1e-6, 1e-6);
        assertNotNull(optimizer1);
        assertNotNull(optimizer2);
    }

    /**
     * Test method for {@link SimplexOptimizer#setSimplex(AbstractSimplex)}.
     */
    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        NelderMeadSimplex simplex = new NelderMeadSimplex(new NelderMeadSimplexConfig(3));
        optimizer.setSimplex(simplex);
        assertNotNull(optimizer.getSimplex());
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}.
     */
    @Test
    public void testOptimize() throws Exception {
        // Test with default parameters
        MultivariateFunction function = f -> FunctionUtils.square(f);
        OptimizationData data = new OptimizationData();
        data.setInitialGuess(new double[] { 1.0, 1.0, 1.0 });
        SimplexOptimizer optimizer = new SimplexOptimizer();
        PointValuePair result = optimizer.optimize(1000, function, GoalType.MINIMIZE, data);
        assertEquals(result.getValue(), 1.0, 1e-6);

        // Test with custom convergence checker
        SimpleValueChecker checker = new SimpleValueChecker(1e-6, 1e-6);
        optimizer = new SimplexOptimizer(checker);
        result = optimizer.optimize(1000, function, GoalType.MINIMIZE, data);
        assertEquals(result.getValue(), 1.0, 1e-6);

        // Test with custom initial guess
        ArrayRealVectorInitializer initializer = new ArrayRealVectorInitializer(new double[] { 2.0, 2.0, 2.0 });
        data = new OptimizationData();
        data.setInitialGuess(initializer);
        result = optimizer.optimize(1000, function, GoalType.MINIMIZE, data);
        assertEquals(result.getValue(), 4.0, 1e-6);
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}.
     * Null input test.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullInput() {
        MultivariateFunction function = null;
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(1000, function, GoalType.MINIMIZE, new OptimizationData[0]);
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}.
     * Function evaluation exception test.
     */
    @Test(expected = RuntimeException.class)
    public void testOptimizeFunctionEvaluationException() {
        MultivariateFunction function = f -> { throw new RuntimeException(); };
        OptimizationData data = new OptimizationData();
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(1000, function, GoalType.MINIMIZE, data);
    }
}