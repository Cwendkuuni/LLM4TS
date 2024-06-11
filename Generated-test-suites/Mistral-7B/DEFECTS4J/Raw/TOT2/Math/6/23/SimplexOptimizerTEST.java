package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnboundedOptimizationException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.OptimizationData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

@RunWith(Parameterized.class)
public class SimplexOptimizerTest {
    private static final double EPSILON = 1e-6;
    private static final Random RANDOM = new Random();

    private MultivariateFunction function;
    private double[] startPoint;
    private GoalType goalType;
    private OptimizationData optimizationData;

    public SimplexOptimizerTest(MultivariateFunction function, double[] startPoint, GoalType goalType, OptimizationData optimizationData) {
        this.function = function;
        this.startPoint = startPoint;
        this.goalType = goalType;
        this.optimizationData = optimizationData;
    }

    @Parameterized.Parameters(name = "Function: {0}, Start Point: {1}, Goal Type: {2}, Optimization Data: {3}")
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {new SimpleFunction(), new double[]{0.0, 0.0}, GoalType.MINIMIZE, null},
                {new RaschFunction(), new double[]{1.0, 1.0}, GoalType.MAXIMIZE, new RandomSearchInitialGuess(2)},
                {new QuadraticFunction(), new double[]{1.0, 1.0}, GoalType.MINIMIZE, new AbstractSimplex(2, new NelderMeadSimplexRules())},
                {new QuadraticFunction(), new double[]{1.0, 1.0}, GoalType.MAXIMIZE, new AbstractSimplex(2, new NelderMeadSimplexRules())},
                {new QuadraticFunction3D(), new double[]{1.0, 1.0, 1.0}, GoalType.MINIMIZE, new AbstractSimplex(3, new NelderMeadSimplexRules())},
        });
    }

    @Test
    public void testOptimize() {
        MultivariateOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(EPSILON, EPSILON));
        PointValuePair result = optimizer.optimize(100, function, goalType, optimizationData);
        assertArrayEquals(startPoint, result.getPoint(), 0.001);
        assertEquals(goalType, optimizer.getGoalType());
        assertEquals(goalType, result.getGoalType());
        assertEquals(function.value(result.getPoint()), result.getValue(), EPSILON);
    }

    @Test
    public void testOptimize_NullFunction() {
        assertThrows(NullArgumentException.class, () -> new SimplexOptimizer(new SimpleValueChecker(EPSILON, EPSILON)).optimize(100, null, goalType, optimizationData));
    }

    @Test
    public void testOptimize_NullStartPoint() {
        assertThrows(NullArgumentException.class, () -> new SimplexOptimizer(new SimpleValueChecker(EPSILON, EPSILON)).optimize(100, function, goalType, null));
    }

    @Test
    public void testOptimize_NullOptimizationData() {
        assertThrows(NullArgumentException.class, () -> new SimplexOptimizer(new SimpleValueChecker(EPSILON, EPSILON)).optimize(100, function, goalType, null));
    }

    @Test
    public void testOptimize_MaxEvalReached() {
        MultivariateOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(EPSILON, EPSILON));
        optimizer.setMaxEval(1);
        assertThrows(UnboundedOptimizationException.class, () -> optimizer.optimize(100, function, goalType, optimizationData));
    }
}

class SimpleFunction implements MultivariateFunction {
    @Override
    public double value(double[] x) {
        return x[0] * x[0] + x[1] * x[1];
    }
}

class RaschFunction implements MultivariateFunction {
    @Override
    public double value(double[] x) {
        return Math.exp(x[0] - x[1]) / (1 + Math.exp(x[0] - x[1]));
    }
}

class QuadraticFunction implements MultivariateFunction {
    @Override
    public double value(double[] x) {
        return x[0] * x[0] + x[1] * x[1];
    }
}

class QuadraticFunction3D implements MultivariateFunction {
    @Override
    public double value(double[] x) {
        return x[0] * x[0] + x[1] * x[1] + x[2] * x[2];
    }
}

class RandomSearchInitialGuess implements OptimizationData {
    private final int dimension;

    public RandomSearchInitialGuess(int dimension) {
        this.dimension = dimension;
    }

    @Override
    public double[] getInitialGuess() {
        double[] guess = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            guess[i] = RANDOM.nextDouble();
        }
        return guess;
    }
}