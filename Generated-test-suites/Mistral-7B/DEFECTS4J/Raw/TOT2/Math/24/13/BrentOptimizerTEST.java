package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer.ConvergenceChecker;
import org.apache.commons.math3.optim.univariate.BrentOptimizer.StoppingCondition;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.SimpleGoalType;
import org.apache.commons.math3.special.MathConstants;
import org.junit.Test;

/**
 * Test class for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}
     * with custom {@link ConvergenceChecker}.
     */
    @Test
    public void testBrentOptimizerWithCustomConvergenceChecker() {
        // Create test function
        UnivariateFunction testFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2.0);
            }
        };

        // Create Brent optimizer with custom convergence checker
        ConvergenceChecker<PointValuePair> convergenceChecker
            = new ConvergenceChecker<PointValuePair>() {
                @Override
                public boolean converged(int iteration, PointValuePair start, PointValuePair end) {
                    double epsilon = 1e-10;
                    return Math.abs(end.getValue() - testFunction.value(end.getPoint())) < epsilon;
                }
            };
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01, convergenceChecker);

        // Initialize optimizer with test function
        UnivariateOptimizer<UnivariateFunction> optimizerInstance = new BrentOptimizer(testFunction);
        optimizer.init(optimizerInstance);

        // Set goal type
        optimizer.setGoalType(SimpleGoalType.MINIMIZE);

        // Run optimization
        PointValuePair result = optimizer.optimize();

        // Check result
        assertNotNull(result);
        assertEquals(-MathConstants.PI, result.getPoint(), 1e-8);
        assertEquals(1.111111111111111E-16, result.getValue(), 1e-14);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}
     * with default stopping condition.
     */
    @Test
    public void testBrentOptimizerWithDefaultStoppingCondition() {
        // Create test function
        UnivariateFunction testFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.tan(x), 2.0);
            }
        };

        // Create Brent optimizer with default stopping condition
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8);

        // Initialize optimizer with test function
        UnivariateOptimizer<UnivariateFunction> optimizerInstance = new BrentOptimizer(testFunction);
        optimizer.init(optimizerInstance);

        // Set goal type
        optimizer.setGoalType(SimpleGoalType.MINIMIZE);

        // Run optimization
        PointValuePair result = optimizer.optimize();

        // Check result
        assertNotNull(result);
        assertEquals(Math.PI / 4.0, result.getPoint(), 1e-6);
        assertEquals(1.111111111111111E-16, result.getValue(), 1e-14);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}
     * with default stopping condition.
     */
    @Test
    public void testBrentOptimizerWithDefaultStoppingConditionAndCustomGoalType() {
        // Create test function
        UnivariateFunction testFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.cos(x), 2.0);
            }
        };

        // Create Brent optimizer with default stopping condition and custom goal type
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8, new StoppingCondition() {
            @Override
            public boolean isSatisfied(UnivariateOptimizer<?> optimizer, PointValuePair current, PointValuePair best) {
                return best.getValue() <= 0.0;
            }
        });

        // Initialize optimizer with test function
        UnivariateOptimizer<UnivariateFunction> optimizerInstance = new BrentOptimizer(testFunction);
        optimizer.init(optimizerInstance);

        // Set goal type
        optimizer.setGoalType(SimpleGoalType.MAXIMIZE);

        // Run optimization
        PointValuePair result = optimizer.optimize();

        // Check result
        assertNotNull(result);
        assertEquals(Math.PI / 2.0, result.getPoint(), 1e-6);
        assertEquals(1.111111111111111E-16, result.getValue(), 1e-14);
    }
}