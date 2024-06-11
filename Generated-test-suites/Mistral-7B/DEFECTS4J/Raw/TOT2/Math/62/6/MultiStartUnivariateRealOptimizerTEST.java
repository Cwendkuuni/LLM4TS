package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointGenerator;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeRegistry;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunctionGradient;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BFGS;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.Test;

/**
 * Test class for MultiStartUnivariateRealOptimizer.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test
    public void testMultiStartOptimization() {
        UnivariateRealFunction function = new PolynomialFunction(3, -1, 1);
        GoalType goalType = GoalTypeRegistry.MINIMIZE;
        double min = -10;
        double max = 10;
        MultiStartUnivariateRealOptimizer<PolynomialFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new BFGS(), 10, new RandomDataGenerator());
        optimizer.setConvergenceChecker(new MaxEval(1000));

        PointValuePair bestSolution = optimizer.optimize(function, goalType, min, max);
        assertNotNull(bestSolution);
        System.out.println("Best solution: " + bestSolution);

        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertArrayEquals(new UnivariateRealPointValuePair[] { bestSolution, null, null, null, null, null, null, null, null }, optima);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     */
    @Test
    public void testMultiStartOptimizationWithCustomStartingPoint() {
        UnivariateRealFunction function = new PolynomialFunction(3, -1, 1);
        GoalType goalType = GoalTypeRegistry.MINIMIZE;
        double min = -10;
        double max = 10;
        double startValue = 0;
        MultiStartUnivariateRealOptimizer<PolynomialFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new BFGS(), 10, new RandomDataGenerator());
        optimizer.setConvergenceChecker(new MaxEval(1000));

        PointValuePair bestSolution = optimizer.optimize(function, goalType, min, max, startValue);
        assertNotNull(bestSolution);
        System.out.println("Best solution: " + bestSolution);

        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertArrayEquals(new UnivariateRealPointValuePair[] { bestSolution, null, null, null, null, null, null, null, null }, optima);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Test with a function which does not have a solution.
     */
    @Test(expected = ConvergenceException.class)
    public void testMultiStartOptimizationNoSolution() {
        ObjectiveFunction<Double> function = new ObjectiveFunction<Double>() {
            @Override
            public Double value(Double x) {
                return x * x - 1;
            }

            @Override
            public Double value(Double x, Double[] grad) {
                // Not implemented
                return null;
            }
        };

        GoalType goalType = GoalTypeRegistry.MINIMIZE;
        double min = -5;
        double max = 5;
        MultiStartUnivariateRealOptimizer<ObjectiveFunction<Double>> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new BFGS(), 10, new RandomDataGenerator());
        optimizer.setConvergenceChecker(new MaxEval(1000));

        optimizer.optimize(function, goalType, min, max);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Test with a function which does not allow optimization (throws an exception).
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testMultiStartOptimizationFunctionEvaluationException() {
        ObjectiveFunction<Double> function = new ObjectiveFunction<Double>() {
            @Override
            public Double value(Double x) {
                throw new FunctionEvaluationException("Evaluation exception.");
            }

            @Override
            public Double value(Double x, Double[] grad) {
                // Not implemented
                return null;
            }
        };

        GoalType goalType = GoalTypeRegistry.MINIMIZE;
        double min = -5;
        double max = 5;
        MultiStartUnivariateRealOptimizer<ObjectiveFunction<Double>> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new BFGS(), 10, new RandomDataGenerator());
        optimizer.setConvergenceChecker(new MaxEval(1000));

        optimizer.optimize(function, goalType, min, max);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Test with a function which has a local minimum.
     */
    @Test
    public void testMultiStartOptimizationLocalMinimum() {
        ObjectiveFunction<Double> function = new ObjectiveFunction<Double>() {
            @Override
            public Double value(Double x) {
                return (x - 2) * (x - 2);
            }

            @Override
            public Double value(Double x, Double[] grad) {
                grad[0] = -4 * (x - 2);
                return value(x);
            }
        };

        GoalType goalType = GoalTypeRegistry.MINIMIZE;
        double min = -10;
        double max = 10;
        MultiStartUnivariateRealOptimizer<ObjectiveFunction<Double>> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new BFGS(), 10, new RandomDataGenerator());
        optimizer.setConvergenceChecker(new MaxEval(1000));

        PointValuePair bestSolution = optimizer.optimize(function, goalType, min, max);
        assertNotNull(bestSolution);
        System.out.println("Best solution: " + bestSolution);

        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertArrayEquals(new UnivariateRealPointValuePair[] { bestSolution, null, null, null, null, null, null, null, null, bestSolution }, optima);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Test with a function which has multiple optima.
     */
    @Test
    public void testMultiStartOptimizationMultipleOptima() {
        ObjectiveFunction<Double> function = new ObjectiveFunction<Double>() {
            @Override
            public Double value(Double x) {
                return (x - 0.5) * (x - 0.5) + Math.sin(x);
            }

            @Override
            public Double value(Double x, Double[] grad) {
                grad[0] = 2 * (x - 0.5) - Math.cos(x);
                return value(x);
            }
        };

        GoalType goalType = GoalTypeRegistry.MINIMIZE;
        double min = -5;
        double max = 5;
        MultiStartUnivariateRealOptimizer<ObjectiveFunction<Double>> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new BFGS(), 10, new RandomDataGenerator());
        optimizer.setConvergenceChecker(new MaxEval(1000));

        PointValuePair bestSolution = optimizer.optimize(function, goalType, min, max);
        assertNotNull(bestSolution);
        System.out.println("Best solution: " + bestSolution);

        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertArrayEquals(new UnivariateRealPointValuePair[] { bestSolution, null, null, null, null, null, null, null, null, null, null, null, null, null }, optima);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     * Test with a function which has multiple optima and a custom starting point.
     */
    @Test
    public void testMultiStartOptimizationMultipleOptimaCustomStartingPoint() {
        ObjectiveFunction<Double> function = new ObjectiveFunction<Double>() {
            @Override
            public Double value(Double x) {
                return (x - 0.5) * (x - 0.5) + Math.sin(x);
            }

            @Override
            public Double value(Double x, Double[] grad) {
                grad[0] = 2 * (x - 0.5) - Math.cos(x);
                return value(x);
            }
        };

        GoalType goalType = GoalTypeRegistry.MINIMIZE;
        double min = -5;
        double max = 5;
        double startValue = 1;
        MultiStartUnivariateRealOptimizer<ObjectiveFunction<Double>> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new BFGS(), 10, new RandomDataGenerator());
        optimizer.setConvergenceChecker(new MaxEval(1000));

        PointValuePair bestSolution = optimizer.optimize(function, goalType, min, max, startValue);
        assertNotNull(bestSolution);
        System.out.println("Best solution: " + bestSolution);

        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertArrayEquals(new UnivariateRealPointValuePair[] { bestSolution, null, null, null, null, null, null, null, null, null, null, null, null, null }, optima);
    }
}