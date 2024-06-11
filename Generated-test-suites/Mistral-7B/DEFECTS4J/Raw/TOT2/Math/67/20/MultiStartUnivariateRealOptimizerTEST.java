package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.initializers.AveragedStartingPointInitializer;
import org.apache.commons.math3.optim.initializers.RandomStartingPointInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearchOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.simple.SquaredErrorSimpleConstraint;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateRealOptimizer optimizer;
    private MultiStartUnivariateRealOptimizer multiStartOptimizer;

    @Before
    public void setUp() {
        optimizer = new FibonacciSearchOptimizer();
        optimizer.setMaxEvaluations(1000);
        optimizer.setMaxIterations(1000);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new RandomGenerator() {
            @Override
            public double nextDouble() {
                return Math.random();
            }
        });
    }

    /**
     * Test that multi-start optimization works correctly with 1-start
     * (single-start optimization is identical to multi-start with 1-start)
     */
    @Test
    public void testOneStart() {
        final ObjectiveFunction function = new PolynomialFunction(2, 1);
        double result = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0);
        double expected = function.value(0.0);
        assertEquals(expected, result, 1e-8);
    }

    /**
     * Test that multi-start optimization works correctly with multiple starts
     */
    @Test
    public void testMultipleStarts() {
        final ObjectiveFunction function = new PolynomialFunction(2, 1);
        double[] results = new double[MultiStartUnivariateRealOptimizer.DEFAULT_STARTS];
        double[] values = new double[MultiStartUnivariateRealOptimizer.DEFAULT_STARTS];
        GoalType goalType = GoalType.MINIMIZE;
        List<PointValuePair> optima = Arrays.asList(
                new PointValuePair(1.0, 1.0),
                new PointValuePair(3.0, 2.5),
                new PointValuePair(4.0, 2.25),
                new PointValuePair(2.0, 2.0),
                new PointValuePair(0.5, 0.5)
        );

        for (int i = 0; i < MultiStartUnivariateRealOptimizer.DEFAULT_STARTS; ++i) {
            results[i] = multiStartOptimizer.optimize(function, goalType, -5.0, 5.0);
            values[i] = function.value(results[i]);
        }

        // sort results and compare with expected values
        Arrays.sort(results);
        Collections.sort(optima, (p1, p2) -> Double.compare(p1.getValue(), p2.getValue()));
        for (int i = 0; i < MultiStartUnivariateRealOptimizer.DEFAULT_STARTS; ++i) {
            assertEquals(Precision.round(optima.get(i).getValue(), 8), values[i], 1e-8);
            assertEquals(Precision.round(optima.get(i).getPoint(), 8), results[i], 1e-8);
        }
    }

    /**
     * Test that multi-start optimization with a very simple function converges
     */
    @Test
    public void testConvergence() {
        final ObjectiveFunction function = new PolynomialFunction(1, 1);
        double result = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        double expected = function.value(0.0);
        assertEquals(expected, result, 1e-8);
    }

    /**
     * Test that multi-start optimization with a non-convergent function does not converge
     */
    @Test(expected = ConvergenceException.class)
    public void testNonConvergence() {
        final ObjectiveFunction function = new PolynomialFunction(-2, 1);
        multiStartOptimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0);
    }

    /**
     * Test that MultiStartUnivariateRealOptimizer supports different initialization methods
     */
    @Test
    public void testDifferentInitializers() {
        final ObjectiveFunction function = new PolynomialFunction(2, 1);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5,
                new AveragedStartingPointInitializer(optimizer));
        double[] results = new double[MultiStartUnivariateRealOptimizer.DEFAULT_STARTS];
        double[] values = new double[MultiStartUnivariateRealOptimizer.DEFAULT_STARTS];
        for (int i = 0; i < MultiStartUnivariateRealOptimizer.DEFAULT_STARTS; ++i) {
            results[i] = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0);
            values[i] = function.value(results[i]);
        }
        // sort results and compare with expected values
        Arrays.sort(results);
        // The averaged initializer returns the minimum and maximum of the range
        double[] expected = {-4.5, 4.5};
        for (int i = 0; i < MultiStartUnivariateRealOptimizer.DEFAULT_STARTS; ++i) {
            assertEquals(Precision.round(expected[i % 2], 8), values[i], 1e-8);
            assertEquals(Precision.round(expected[i % 2], 8), results[i], 1e-8);
        }

        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5,
                new RandomStartingPointInitializer(optimizer));
        results = new double[MultiStartUnivariateRealOptimizer.DEFAULT_STARTS];
        values = new double[MultiStartUnivariateRealOptimizer.DEFAULT_STARTS];
        for (int i = 0; i < MultiStartUnivariateRealOptimizer.DEFAULT_STARTS; ++i) {
            results[i] = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0);
            values[i] = function.value(results[i]);
        }
        // sort results and compare with expected values
        Arrays.sort(results);
        // The random initializer returns random points in the given range
        double min = -5.0;
        double max = 5.0;
        for (int i = 0; i < MultiStartUnivariateRealOptimizer.DEFAULT_STARTS; ++i) {
            assertEquals(min + (max - min) * Math.random(), results[i], 1e-8);
        }
    }

    /**
     * Test that MultiStartUnivariateRealOptimizer supports custom constraints
     */
    @Test
    public void testConstraints() {
        GoalType goalType = GoalType.MINIMIZE;
        ObjectiveFunction function = new PolynomialFunction(2, 1);
        UnivariateRealOptimizer optimizer = new FibonacciSearchOptimizer();
        optimizer.setMaxEvaluations(1000);
        optimizer.setMaxIterations(1000);
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer,
                5, new RandomGenerator() {
            @Override
            public double nextDouble() {
                return Math.random();
            }
        });

        // Test that constraint is applied for optimization
        ObjectiveFunction objectiveFunction = new PolynomialFunction(2, 1);
        GoalTypeConstraint constraint = new SquaredErrorSimpleConstraint(objectiveFunction, 1.0);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, constraint);
        double[] results = new double[MultiStartUnivariateRealOptimizer.DEFAULT_STARTS];
        double[] values = new double[MultiStartUnivariateRealOptimizer.DEFAULT_STARTS];
        for (int i = 0; i < MultiStartUnivariateRealOptimizer.DEFAULT_STARTS; ++i) {
            results[i] = multiStartOptimizer.optimize(objectiveFunction, goalType, -5.0, 5.0)[0];
            values[i] = objectiveFunction.value(results[i]);
        }
        // sort results and compare with expected values
        Arrays.sort(results);
        double[] expected = {1.0, 1.0, 1.0, 1.0, 1.0};
        for (int i = 0; i < MultiStartUnivariateRealOptimizer.DEFAULT_STARTS; ++i) {
            assertEquals(expected[i], results[i], 1e-8);
            assertEquals(1.0, values[i], 1e-8);
        }

        // Test that constraint is applied during optimization
        function = new PolynomialFunction(2, 1);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, constraint);
        try {
            multiStartOptimizer.optimize(function, goalType, -5.0, 5.0);
            fail();
        } catch (FunctionEvaluationException feex) {
            // Expected exception
        }

        // Test that exception is propagated for each optimization attempt
        for (int i = 0; i < MultiStartUnivariateRealOptimizer.DEFAULT_STARTS; ++i) {
            try {
                multiStartOptimizer.optimize(function, goalType, -5.0, 5.0);
                fail();
            } catch (FunctionEvaluationException feex) {
                // Expected exception
            }
        }
    }
}