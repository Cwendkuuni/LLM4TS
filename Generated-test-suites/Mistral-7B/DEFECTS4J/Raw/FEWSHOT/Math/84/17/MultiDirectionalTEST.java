package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.function.Fraction;
import org.apache.commons.math3.optim.function.FunctionEvaluationException;
import org.apache.commons.math3.optim.initializer.AveragePointInitializer;
import org.apache.commons.math3.optim.point.PointValuePairCollection;
import org.apache.commons.math3.optim.restart.RestartStrategy;
import org.apache.commons.math3.optim.simple.SimpleOptimizer;
import org.apache.commons.math3.optim.stopping.SimpleStoppingCondition;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalOptimizer() throws FunctionEvaluationException, OptimizationException,
            IllegalArgumentException, MaxEvalExceededException {

        // Create a function to be optimized
        Fraction func = new Fraction(new SimplePolynomialFunction(3, 4, 5), new SimplePolynomialFunction(1, -2, -4));

        // Create a multi-directional optimizer with default coefficients
        MultiDirectional optimizer = new MultiDirectional();

        // Define function bounds
        SimpleBounds bounds = new SimpleBounds(new double[]{-10.0, -10.0}, new double[]{10.0, 10.0});

        // Define initial simplex
        PointValuePair[] initialSimpleX = new PointValuePair[] { new RealPointValuePair(new double[]{-3.0, -3.0}, 5.0),
                                                                   new RealPointValuePair(new double[]{3.0, -3.0}, 12.0),
                                                                   new RealPointValuePair(new double[]{-3.0, 3.0}, 8.0),
                                                                   new RealPointValuePair(new double[]{3.0, 3.0}, 15.0),
                                                                   new RealPointValuePair(new double[]{0.0, 0.0}, Double.POSITIVE_INFINITY) };

        // Define stopping conditions
        SimpleStoppingCondition stoppingCondition = new SimpleStoppingCondition(1.0e-6);

        // Define restart strategy
        RestartStrategy restartStrategy = new RestartStrategy(5, 1.5);

        // Initialize optimizer with given function, bounds, initial simplex, stopping condition, and restart strategy
        SimpleOptimizer optimizerInstance = new SimpleOptimizer(func, bounds, optimizer, stoppingCondition, restartStrategy,
                new AveragePointInitializer(5));

        // Run optimization process
        PointValuePair bestSolution = optimizerInstance.optimize();

        // Print best solution and function value
        System.out.println("Best Solution: " + bestSolution.getPoint());
        System.out.println("Function Value: " + bestSolution.getValue());

        // Check solution
        assertArrayEquals(new double[]{0.0, 0.0}, bestSolution.getPoint(), 0.0);
        assertTrue(Math.abs(bestSolution.getValue() - 1.0) < 1.0e-6);

        // Test optimization with custom coefficients
        MultiDirectional customOptimizer = new MultiDirectional(3.0, 0.7);
        PointValuePair customBestSolution = optimizerInstance.optimize();
        assertArrayEquals(new double[]{0.0, 0.0}, customBestSolution.getPoint(), 0.0);
        assertTrue(Math.abs(customBestSolution.getValue() - 1.0) < 1.0e-6);
    }
}