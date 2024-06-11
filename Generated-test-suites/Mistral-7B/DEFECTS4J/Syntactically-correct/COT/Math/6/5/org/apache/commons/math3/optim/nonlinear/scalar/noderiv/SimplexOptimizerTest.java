package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalings.SquaredSumScaling;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeVector;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePairList;
import org.apache.commons.math3.optim.nonlinear.scalar.SimplexIterate;
import org.apache.commons.math3.optim.nonlinear.scalar.SimplexSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.SimplexSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexMethod;
import org.apache.commons.math3.optim.point.PointValuePairComparator;
import org.junit.Test;

/**
 * Test cases for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {

    /**
     * Test method for {@link SimplexOptimizer#SimplexOptimizer()}.
     */
    @Test
    public void testConstructor() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNull(optimizer.getSimplex());
        SimplexOptimizer optimizerWithChecker = new SimplexOptimizer(new SimpleValueChecker());
        assertEquals(SimpleValueChecker.class, optimizerWithChecker.getChecker().getClass());
    }

    /**
     * Test method for {@link SimplexOptimizer#SimplexOptimizer(ConvergenceChecker)}.
     */
    @Test
    public void testConstructorWithChecker() {
        SimpleValueChecker checker = new SimpleValueChecker();
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        assertEquals(checker, optimizer.getChecker());
    }

    /**
     * Test method for {@link SimplexOptimizer#SimplexOptimizer(double, double)}.
     */
    @Test
    public void testConstructorWithRelAndAbs() {
        SimpleValueChecker checker = new SimpleValueChecker(1e-6, 1e-8);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker.getRelativePrecision(), checker.getAbsolutePrecision());
        assertEquals(checker.getRelativePrecision(), optimizer.getChecker().getRelativePrecision(), 0.0);
        assertEquals(checker.getAbsolutePrecision(), optimizer.getChecker().getAbsolutePrecision(), 0.0);
    }

    /**
     * Test method for {@link SimplexOptimizer#setSimplex(AbstractSimplex)}.
     */
    @Test
    public void testSetSimplex() {
        AbstractSimplex simplex = new SimplexSimplex();
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.getSimplex());
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[])}.
     */
    @Test
    public void testOptimize() {
        MultivariateFunction function = new ObjectiveFunction(new double[]{-1.0, -1.0}, new double[]{2.0, 2.0}, x -> x[0] * x[0] + x[1] * x[1]);
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = new double[]{1.0, 1.0};
        PointValuePair expectedResult = new PointValuePair(new double[]{0.0, 0.0}, 0.0);

        SimplexOptimizer optimizer = new SimplexOptimizer();
        SimpleValueChecker checker = new SimpleValueChecker();
        optimizer.setChecker(checker);
        optimizer.setSimplex(new SimplexSearch(new SimplexMethod(), new SquaredSumScaling(), new PointValuePairComparator()));

        PointValuePair result = optimizer.optimize(new MaxEval(100), function, goalType, startPoint);
        assertEquals(expectedResult.getValue(), result.getValue(), 0.0);
        assertArrayEquals(expectedResult.getPoint(), result.getPoint(), 0.0);
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[])}.
     * with null function.
     */
    @Test
    public void testOptimizeWithNullFunction() {
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = new double[]{1.0, 1.0};
        MultivariateFunction nullFunction = null;

        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(new MaxEval(1), nullFunction, goalType, startPoint));
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[])}.
     * with null startPoint.
     */
    @Test
    public void testOptimizeWithNullStartPoint() {
        MultivariateFunction function = new ObjectiveFunction(new double[]{-1.0, -1.0}, new double[]{2.0, 2.0}, x -> x[0] * x[0] + x[1] * x[1]);
        GoalType goalType = GoalType.MINIMIZE;
        double[] nullStartPoint = null;

        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(new MaxEval(100), function, goalType, nullStartPoint));
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[], InitialGuess...)}.
     */
    @Test
    public void testOptimizeWithInitialGuess() {
        MultivariateFunction function = new ObjectiveFunction(new double[]{-1.0, -1.0}, new double[]{2.0, 2.0}, x -> x[0] * x[0] + x[1] * x[1]);
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = new double[]{1.0, 1.0};
        InitialGuess initialGuess = new InitialGuess(new double[]{0.0, 0.0});

        SimplexOptimizer optimizer = new SimplexOptimizer();
        SimpleValueChecker checker = new SimpleValueChecker();
        optimizer.setChecker(checker);
        optimizer.setSimplex(new SimplexSearch(new SimplexMethod(), new SquaredSumScaling(), new PointValuePairComparator()));

        PointValuePair result = optimizer.optimize(new MaxEval(100), function, goalType, startPoint, initialGuess);
        PointValuePair expectedResult = new PointValuePair(initialGuess.getPoint(), result.getValue());
        assertEquals(expectedResult.getValue(), result.getValue(), 0.0);
        assertArrayEquals(initialGuess.getPoint(), result.getPoint(), 0.0);
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[], OptimizationData...)}.
     */
    @Test
    public void testOptimizeWithOptimizationData() {
        MultivariateFunction function = new ObjectiveFunction(new double[]{-1.0, -1.0}, new double[]{2.0, 2.0}, x -> x[0] * x[0] + x[1] * x[1]);
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = new double[]{1.0, 1.0};
        OptimizationData data = new Object[][]{new AbstractSimplex(new SimplexSearch(new SimplexMethod(), new SquaredSumScaling(), new PointValuePairComparator()))};

        SimplexOptimizer optimizer = new SimplexOptimizer();

        PointValuePair result = optimizer.optimize(new MaxEval(100), function, goalType, startPoint, (OptimizationData[]) data);
        PointValuePair expectedResult = new PointValuePair(new double[]{0.0, 0.0}, 0.0);
        assertEquals(expectedResult.getValue(), result.getValue(), 0.0);
        assertArrayEquals(expectedResult.getPoint(), result.getPoint(), 0.0);
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[], OptimizationData...)}.
     * with null optimization data.
     */
    @Test
    public void testOptimizeWithNullOptimizationData() {
        MultivariateFunction function = new ObjectiveFunction(new double[]{-1.0, -1.0}, new double[]{2.0, 2.0}, x -> x[0] * x[0] + x[1] * x[1]);
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = new double[]{1.0, 1.0};
        OptimizationData nullData = null;

        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(new MaxEval(100), function, goalType, startPoint, nullData));
    }

    /**
     * Test method for {@link SimplexOptimizer#parseOptimizationData(OptimizationData...)}.
     */
    @Test
    public void testParseOptimizationData() {
        AbstractSimplex simplex = new SimplexSearch(new SimplexMethod(), new SquaredSumScaling(), new PointValuePairComparator());
        OptimizationData data = new Object[]{simplex};
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.parseOptimizationData(data);
        assertEquals(simplex, optimizer.getSimplex());
    }

    /**
     * Test method for {@link SimplexOptimizer#parseOptimizationData(OptimizationData...)}.
     * with no simplex provided.
     */
    @Test
    public void testParseOptimizationDataWithNoSimplex() {
        OptimizationData noSimplexData = new Object[0];
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.parseOptimizationData(noSimplexData);
        assertNull(optimizer.getSimplex());
    }

}