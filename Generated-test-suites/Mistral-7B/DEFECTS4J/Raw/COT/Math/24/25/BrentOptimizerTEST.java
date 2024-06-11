package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.AveragePointInitializer;
import org.apache.commons.math3.optim.initializer.ValueInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.apache.commons.math3.special.E;
import org.apache.commons.math3.test.AbstractMathTestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest extends AbstractMathTestCase {
    private UnivariateFunction function;
    private UnivariateOptimizer optimizer;

    @Before
    public void setUp() {
        function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-Math.pow(x, 2)) - Math.sin(Math.PI * x);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };

        optimizer = new BrentOptimizer(1e-6, 1e-6);
    }

    @Test
    public void testBrentOptimizerMinimum() {
        assertEquals(
            new PointValuePair(-1.127424240189874, -2.718281828459045),
            optimizer.optimize(function, new ValueInitializer(-3), null)
        );

        // Test with AveragePointInitializer
        assertEquals(
            new PointValuePair(-1.127424240189874, -2.718281828459045),
            optimizer.optimize(function, new AveragePointInitializer(2), null)
        );
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerThrowsExceptionWithNegativeAbsoluteThreshold() {
        new BrentOptimizer(-1e-6, 1e-6, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerThrowsExceptionWithTooSmallRelativeThreshold() {
        new BrentOptimizer(1e-8, 1e-6, null);
    }

    @Test
    public void testBrentOptimizerConvergenceChecker() {
        final int maxIter = 20;
        final double epsilon = 1e-6;
        final UnivariateOptimizer optimizer = new BrentOptimizer(epsilon, 0,
                new ConvergenceChecker<PointValuePair>() {
                    private int iter;

                    @Override
                    public boolean converged(int currentIteration,
                                             PointValuePair currentBest,
                                             PointValuePair newBest) {
                        iter++;
                        if (iter > maxIter) {
                            return true;
                        }
                        return currentBest.getValue() - newBest.getValue() < epsilon;
                    }
                });

        PointValuePair result = optimizer.optimize(function,
                new ValueInitializer(-3), null);

        assertTrue(result.getValue() < -1); // Ensure the result is less than -1
        assertEquals(-1.127424240189874, result.getPoint(), 1e-6);
    }
}