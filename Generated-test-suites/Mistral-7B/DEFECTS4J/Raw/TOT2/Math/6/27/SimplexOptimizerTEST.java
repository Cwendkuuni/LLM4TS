package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.AbstractSimplex;
import org.apache.commons.math3.optimization.InitialGuess;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.SimpleBound;

import java.util.Random;

/**
 * Test for SimplexOptimizer class.
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
     * Test method for {@link SimplexOptimizer#SimplexOptimizer(ConvergenceChecker)}.
     */
    @Test
    public void testConstructorWithChecker() {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        assertNotNull(optimizer);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    /**
     * Test method for {@link SimplexOptimizer#SimplexOptimizer(double, double)}.
     */
    @Test
    public void testConstructorWithRelAndAbs() {
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(0.001, 0.01);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        assertEquals(checker.getRelativeThreshold(), optimizer.getConvergenceChecker().getRelativeThreshold(), 0.0);
        assertEquals(checker.getAbsoluteThreshold(), optimizer.getConvergenceChecker().getAbsoluteThreshold(), 0.0);
    }

    /**
     * Test method for {@link SimplexOptimizer#setSimplex(AbstractSimplex)}.
     */
    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNull(optimizer.getSimplex());

        AbstractSimplex simplex = new SimpleSimplex();
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.getSimplex());
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}.
     */
    @Test
    public void testOptimize() throws Exception {
        // Test with default parameters
        MultivariateFunction function = new SimpleFunction();
        GoalType goalType = GoalType.MINIMIZE;
        SimplexOptimizer optimizer = new SimplexOptimizer();
        OptimizationData[] optData = new OptimizationData[1];
        optData[0] = new InitialGuess(new double[] { 1.0, 1.0 });
        PointValuePair result = optimizer.optimize(1000, function, goalType, optData);
        assertNotNull(result);
        assertEquals(GoalType.MINIMIZE, goalType);
        assertEquals(result.getValue(), 0.0, 0.01);

        // Test with custom convergence checker
        SimpleValueChecker checker = new SimpleValueChecker(0.001, 0.01);
        optimizer = new SimplexOptimizer(checker);
        result = optimizer.optimize(1000, function, goalType, optData);
        assertNotNull(result);
        assertEquals(goalType, result.getGoalType());
        assertEquals(checker.getRelativeThreshold(), optimizer.getConvergenceChecker().getRelativeThreshold(), 0.0);
        assertEquals(checker.getAbsoluteThreshold(), optimizer.getConvergenceChecker().getAbsoluteThreshold(), 0.0);

        // Test with custom simplex
        AbstractSimplex customSimplex = new CustomSimplex();
        optimizer = new SimplexOptimizer();
        optimizer.setSimplex(customSimplex);
        result = optimizer.optimize(1000, function, goalType, optData);
        assertNotNull(result);
        assertEquals(goalType, result.getGoalType());
        assertNotSame(simplex, optimizer.getSimplex());

        // Test with custom optimization data
        InitialGuess customInitialGuess = new InitialGuess(new double[] { 2.0, 2.0 });
        optimizer = new SimplexOptimizer();
        OptimizationData customData = new OptimizationData();
        customData.setInitialGuess(customInitialGuess);
        optData = new OptimizationData[] { customData };
        result = optimizer.optimize(1000, function, goalType, optData);
        assertNotNull(result);
        assertEquals(goalType, result.getGoalType());
        assertNotEquals(optData[0].getInitialGuess(), optData[0].getInitialGuess());

        // Test with maxEval
        MaxEval maxEval = new MaxEval(100);
        optData = new OptimizationData[] { maxEval };
        optimizer = new SimplexOptimizer();
        result = optimizer.optimize(function, goalType, optData);
        assertNotNull(result);
        assertEquals(goalType, result.getGoalType());
        assertEquals(maxEval.getMaxEval(), optimizer.getMaxIterations(), 0);

        // Test with bounds
        SimpleBound bound = new SimpleBound(0.0, null, Double.POSITIVE_INFINITY);
        optData = new OptimizationData[] { new OptimizationData(bound) };
        optimizer = new SimplexOptimizer();
        result = optimizer.optimize(function, goalType, optData);
        assertNotNull(result);
        assertEquals(goalType, result.getGoalType());
        assertEquals(bound.getMin(), optimizer.getLowerBound(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, optimizer.getUpperBound(), 0.0);
    }

    /**
     * Test method for {@link SimplexOptimizer#parseOptimizationData(OptimizationData[])}.
     */
    @Test
    public void testParseOptimizationData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();

        AbstractSimplex simplex = new SimpleSimplex();
        OptimizationData optData = new InitialGuess(new double[] { 1.0, 1.0 });
        optData.setSimplex(simplex);

        optimizer.parseOptimizationData(new OptimizationData[] { optData });
        assertEquals(simplex, optimizer.getSimplex());
    }

    /**
     * Test method for {@link SimplexOptimizer#doOptimize()}.
     */
    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.doOptimize();
    }

    /**
     * Test method for {@link SimplexOptimizer#optimizeInternal(int, MultivariateFunction, GoalType, OptimizationData[])}.
     */
    @Test
    public void testOptimizeInternal() throws Exception {
        MultivariateFunction function = new SimpleFunction();
        GoalType goalType = GoalType.MINIMIZE;
        SimplexOptimizer optimizer = new SimplexOptimizer();
        OptimizationData[] optData = new OptimizationData[1];
        optData[0] = new InitialGuess(new double[] { 1.0, 1.0 });

        // Test with default convergence checker
        PointValuePair result = optimizer.optimizeInternal(1000, function, goalType, optData);
        assertNotNull(result);
        assertEquals(goalType, result.getGoalType());
        assertEquals(result.getValue(), 0.0, 0.01);

        // Test with custom convergence checker
        SimpleValueChecker checker = new SimpleValueChecker(0.001, 0.01);
        optimizer = new SimplexOptimizer(checker);
        result = optimizer.optimizeInternal(1000, function, goalType, optData);
        assertNotNull(result);
        assertEquals(goalType, result.getGoalType());
        assertEquals(checker.getRelativeThreshold(), optimizer.getConvergenceChecker().getRelativeThreshold(), 0.0);
        assertEquals(checker.getAbsoluteThreshold(), optimizer.getConvergenceChecker().getAbsoluteThreshold(), 0.0);

        // Test with custom simplex
        AbstractSimplex customSimplex = new CustomSimplex();
        optimizer = new SimplexOptimizer();
        optimizer.setSimplex(customSimplex);
        result = optimizer.optimizeInternal(1000, function, goalType, optData);
        assertNotNull(result);
        assertEquals(goalType, result.getGoalType());
        assertNotSame(simplex, optimizer.getSimplex());

        // Test with custom optimization data
        InitialGuess customInitialGuess = new InitialGuess(new double[] { 2.0, 2.0 });
        optimizer = new SimplexOptimizer();
        OptimizationData customData = new OptimizationData();
        customData.setInitialGuess(customInitialGuess);
        optData = new OptimizationData[] { customData };
        result = optimizer.optimizeInternal(function, goalType, optData);
        assertNotNull(result);
        assertEquals(goalType, result.getGoalType());
        assertNotEquals(optData[0].getInitialGuess(), optData[0].getInitialGuess());

        // Test with maxEval
        MaxEval maxEval = new MaxEval(100);
        optData = new OptimizationData[] { maxEval };
        optimizer = new SimplexOptimizer();
        result = optimizer.optimizeInternal(function, goalType, optData);
        assertNotNull(result);
        assertEquals(goalType, result.getGoalType());
        assertEquals(maxEval.getMaxEval(), optimizer.getMaxIterations(), 0);

        // Test with bounds
        SimpleBound bound = new SimpleBound(0.0, null, Double.POSITIVE_INFINITY);
        optData = new OptimizationData[] { new OptimizationData(bound) };
        optimizer = new SimplexOptimizer();
        result = optimizer.optimizeInternal(function, goalType, optData);
        assertNotNull(result);
        assertEquals(goalType, result.getGoalType());
        assertEquals(bound.getMin(), optimizer.getLowerBound(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, optimizer.getUpperBound(), 0.0);
    }
}

class SimpleFunction implements MultivariateFunction {
    public double value(double[] x) {
        return x[0] * x[0] + x[1] * x[1];
    }
}

class SimpleSimplex implements AbstractSimplex {
    public SimpleSimplex() {}

    public void build(double[] startPoint) {}

    public void evaluate(MultivariateFunction f, Comparator<PointValuePair> comparator) {}

    public void iterate(MultivariateFunction f, Comparator<PointValuePair> comparator) {}

    public PointValuePair getPoint(int index) {
        return new PointValuePair(new double[] { index, index }, 0.0);
    }

    public int getSize() {
        return 3;
    }
}

class CustomSimplex implements AbstractSimplex {
    public CustomSimplex() {}

    public void build(double[] startPoint) {}

    public void evaluate(MultivariateFunction f, Comparator<PointValuePair> comparator) {}

    public void iterate(MultivariateFunction f, Comparator<PointValuePair> comparator) {}

    public PointValuePair getPoint(int index) {
        return new PointValuePair(new double[] { index, index }, Math.random());
    }

    public int getSize() {
        return 3;
    }
}