package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.SimpleGoalType;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair.ComparisonType;
import org.apache.commons.math3.optim.pointvalues.CalculusPointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeSorter;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.special.Polynomials;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class MultiStartUnivariateRealOptimizerTest<FUNC extends UnivariateRealFunction> {
    
    private static class TestMultiStartUnivariateRealOptimizer extends MultiStartUnivariateRealOptimizer<FUNC> {
        public TestMultiStartUnivariateRealOptimizer(BaseUnivariateRealOptimizer<FUNC> optimizer, int starts, RandomGenerator generator) {
            super(optimizer, starts, generator);
        }

        @Override
        public UnivariateRealPointValuePair optimize(FUNC f, GoalType goal, double min, double max)
                throws FunctionEvaluationException {
            return super.optimize(f, goal, min, max, 0);
        }
    }

    private BaseUnivariateRealOptimizer<PolynomialFunction> optimizer;
    private int starts;
    private RandomGenerator generator;

    public TestMultiStartUnivariateRealOptimizerTest(BaseUnivariateRealOptimizer<PolynomialFunction> optimizer, int starts, RandomGenerator generator) {
        this.optimizer = optimizer;
        this.starts = starts;
        this.generator = generator;
    }

    @Parameterized.Parameters(name = "starts={0}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new FibonacciGoldenSectionOptimizer(), 3, new RandomDataGenerator()},
                {new BrentOptimizer(), 5, new RandomDataGenerator()},
                {new GoldenSectionFinder(), 10, new RandomDataGenerator()},
                {new BisectionMethod(), 20, new RandomDataGenerator()}
        });
    }

    /**
     * Test the optimization of f(x) = x^3 - 5x + 7
     */
    @Test
    public void testCubicFunction() throws Exception {
        FuncCubicFunction func = new FuncCubicFunction();
        TestMultiStartUnivariateRealOptimizer<FuncCubicFunction> optimizer = new TestMultiStartUnivariateRealOptimizer<>(
                new FibonacciGoldenSectionOptimizer<>(), starts, generator);

        SimpleBounds bounds = new SimpleBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        SimpleGoalType goal = new SimpleGoalType(GoalType.MINIMIZE);

        UnivariateOptimizer<FuncCubicFunction> optimizerWrapper = optimizer;
        optimizerWrapper.setBounds(bounds);
        optimizerWrapper.setGoalType(goal);

        PointValuePair bestPoint = optimizerWrapper.optimize(func, goal, 0.0, 1.0);
        double bestValue = bestPoint.getValue();

        // Basic test
        assertEquals(0.0, bestValue, 1e-6);
        assertEquals(1.0, bestPoint.getPoint(), 1e-6);

        // Test optimization with multiple starts
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertEquals(starts, optima.length);
        for (UnivariateRealPointValuePair pair : optima) {
            if (pair != null) {
                PointValuePair pointValuePair = new CalculusPointValuePair(pair.getPoint(), pair.getValue());
                assertEquals(bestPoint, pointValuePair, ComparisonType.TOLERANT);
            } else {
                assertNull(pair);
            }
        }
        
        // Test that the same optima are returned in each run
        TestMultiStartUnivariateRealOptimizer<FuncCubicFunction> optimizerInstance = new TestMultiStartUnivariateRealOptimizer<>(optimizer, starts, generator);
        UnivariateRealPointValuePair[] optimaSameInstance = optimizerInstance.getOptima();
        GoalTypeSorter sorter = new GoalTypeSorter(new SimpleGoalType(GoalType.MINIMIZE));
        Arrays.sort(optimaSameInstance, sorter);
        for (UnivariateRealPointValuePair pair : optimaSameInstance) {
            assertEquals(bestPoint, pair, ComparisonType.TOLERANT);
        }
    }

    /**
     * Test the optimization of f(x) = |x - 1|
     */
    @Test
    public void testAbsFunction() throws Exception {
        FuncAbsFunction func = new FuncAbsFunction();
        TestMultiStartUnivariateRealOptimizer<FuncAbsFunction> optimizer = new TestMultiStartUnivariateRealOptimizer<>(
                new GoldenSectionFinder(), starts, generator);

        SimpleBounds bounds = new SimpleBounds(0.0, 2.0);
        SimpleGoalType goal = new SimpleGoalType(GoalType.MINIMIZE);

        UnivariateOptimizer<FuncAbsFunction> optimizerWrapper = optimizer;
        optimizerWrapper.setBounds(bounds);
        optimizerWrapper.setGoalType(goal);

        PointValuePair bestPoint = optimizerWrapper.optimize(func, goal, 0.0, 1.0);
        double bestValue = bestPoint.getValue();

        // Basic test
        assertEquals(0.0, bestValue, 1e-6);
        assertEquals(1.0, bestPoint.getPoint(), 1e-6);

        // Test optimization with multiple starts
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertEquals(starts, optima.length);
        for (UnivariateRealPointValuePair pair : optima) {
            if (pair != null) {
                PointValuePair pointValuePair = new CalculusPointValuePair(pair.getPoint(), pair.getValue());
                assertEquals(bestPoint, pointValuePair, ComparisonType.TOLERANT);
            } else {
                assertNull(pair);
            }
        }
        
        // Test that the same optima are returned in each run
        TestMultiStartUnivariateRealOptimizer<FuncAbsFunction> optimizerInstance = new TestMultiStartUnivariateRealOptimizer<>(optimizer, starts, generator);
        UnivariateRealPointValuePair[] optimaSameInstance = optimizerInstance.getOptima();
        GoalTypeSorter sorter = new GoalTypeSorter(new SimpleGoalType(GoalType.MINIMIZE));
        Arrays.sort(optimaSameInstance, sorter);
        for (UnivariateRealPointValuePair pair : optimaSameInstance) {
            assertEquals(bestPoint, pair, ComparisonType.TOLERANT);
        }
    }

    /**
     * Test the optimization of f(x) = x^2
     */
    @Test
    public void testSquareFunction() throws Exception {
        FuncSquareFunction func = new FuncSquareFunction();
        TestMultiStartUnivariateRealOptimizer<FuncSquareFunction> optimizer = new TestMultiStartUnivariateRealOptimizer<>(
                new BrentOptimizer(), starts, generator);

        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);
        SimpleGoalType goal = new SimpleGoalType(GoalType.MINIMIZE);

        UnivariateOptimizer<FuncSquareFunction> optimizerWrapper = optimizer;
        optimizerWrapper.setBounds(bounds);
        optimizerWrapper.setGoalType(goal);

        PointValuePair bestPoint = optimizerWrapper.optimize(func, goal, 0.0, 1.0);
        double bestValue = bestPoint.getValue();

        // Basic test
        assertEquals(0.0, bestValue, 1e-6);
        assertEquals(0.0, bestPoint.getPoint(), 1e-6);

        // Test optimization with multiple starts
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertEquals(starts, optima.length);
        for (UnivariateRealPointValuePair pair : optima) {
            if (pair != null) {
                PointValuePair pointValuePair = new CalculusPointValuePair(pair.getPoint(), pair.getValue());
                assertEquals(bestPoint, pointValuePair, ComparisonType.TOLERANT);
            } else {
                assertNull(pair);
            }
        }
        
        // Test that the same optima are returned in each run
        TestMultiStartUnivariateRealOptimizer<FuncSquareFunction> optimizerInstance = new TestMultiStartUnivariateRealOptimizer<>(optimizer, starts, generator);
        UnivariateRealPointValuePair[] optimaSameInstance = optimizerInstance.getOptima();
        GoalTypeSorter sorter = new GoalTypeSorter(new SimpleGoalType(GoalType.MINIMIZE));
        Arrays.sort(optimaSameInstance, sorter);
        for (UnivariateRealPointValuePair pair : optimaSameInstance) {
            assertEquals(bestPoint, pair, ComparisonType.TOLERANT);
        }
    }

    private static class FuncCubicFunction implements PolynomialFunction {
        @Override
        public double value(double x) {
            return x * x * x - 5 * x + 7;
        }

        @Override
        public double value(double[] x) {
            throw new UnsupportedOperationException("This function does not accept arrays.");
        }

        @Override
        public double value(double x, int derivative) {
            switch (derivative) {
            case 0:
                return value(x);
            case 1:
                return 3.0 * x * x - 5;
            case 2:
                return 6.0 * x;
            default:
                throw new IllegalArgumentException("This function does not support derivatives of order " + derivative);
            }
        }

        @Override
        public double[] derivative(double[] x) {
            throw new UnsupportedOperationException("This function does not accept arrays.");
        }

        @Override
        public double[] derivative(double x, int maxOrder) {
            double[] result = new double[maxOrder + 1];
            result[0] = 3 * x * x - 5;
            result[1] = 6.0 * x;
            return result;
        }

        @Override
        public int getNumberOfDerivatives() {
            return 2;
        }
    }

    private static class FuncAbsFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.abs(x - 1.0);
        }

        @Override
        public double value(double[] x) {
            throw new UnsupportedOperationException("This function does not accept arrays.");
        }

        @Override
        public double[] gradient(double[] x) {
            throw new UnsupportedOperationException("This function does not accept arrays.");
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }
    }

    private static class FuncSquareFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2);
        }

        @Override
        public double value(double[] x) {
            throw new UnsupportedOperationException("This function does not accept arrays.");
        }

        @Override
        public double[] gradient(double[] x) {
            throw new UnsupportedOperationException("This function does not accept arrays.");
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }
    }
}